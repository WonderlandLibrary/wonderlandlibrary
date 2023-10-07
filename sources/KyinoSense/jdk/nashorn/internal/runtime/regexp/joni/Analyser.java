/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.BitStatus;
import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.MinMaxLen;
import jdk.nashorn.internal.runtime.regexp.joni.NodeOptInfo;
import jdk.nashorn.internal.runtime.regexp.joni.OptEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.Parser;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;
import jdk.nashorn.internal.runtime.regexp.joni.ast.AnchorNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.ConsAltNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.EncloseNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.ast.QuantifierNode;
import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.SyntaxException;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

final class Analyser
extends Parser {
    private static final int GET_CHAR_LEN_VARLEN = -1;
    private static final int GET_CHAR_LEN_TOP_ALT_VARLEN = -2;
    private static final int THRESHOLD_CASE_FOLD_ALT_FOR_EXPANSION = 8;
    private static final int IN_ALT = 1;
    private static final int IN_NOT = 2;
    private static final int IN_REPEAT = 4;
    private static final int IN_VAR_REPEAT = 8;
    private static final int EXPAND_STRING_MAX_LENGTH = 100;
    private static final int MAX_NODE_OPT_INFO_REF_COUNT = 5;

    protected Analyser(ScanEnvironment env, char[] chars, int p, int end) {
        super(env, chars, p, end);
    }

    protected final void compile() {
        this.reset();
        this.regex.numMem = 0;
        this.regex.numRepeat = 0;
        this.regex.numNullCheck = 0;
        this.regex.repeatRangeLo = null;
        this.regex.repeatRangeHi = null;
        this.parse();
        this.root = this.setupTree(this.root, 0);
        this.regex.captureHistory = this.env.captureHistory;
        this.regex.btMemStart = this.env.btMemStart;
        this.regex.btMemEnd = this.env.btMemEnd;
        if (Option.isFindCondition(this.regex.options)) {
            this.regex.btMemEnd = BitStatus.bsAll();
        } else {
            this.regex.btMemEnd = this.env.btMemEnd;
            this.regex.btMemEnd |= this.regex.captureHistory;
        }
        this.regex.clearOptimizeInfo();
        this.setOptimizedInfoFromTree(this.root);
        this.env.memNodes = null;
        this.regex.stackPopLevel = this.regex.numRepeat != 0 || this.regex.btMemEnd != 0 ? 2 : (this.regex.btMemStart != 0 ? 1 : 0);
    }

    private void swap(Node a, Node b) {
        a.swap(b);
        if (this.root == b) {
            this.root = a;
        } else if (this.root == a) {
            this.root = b;
        }
    }

    private int quantifiersMemoryInfo(Node node) {
        int info = 0;
        block0 : switch (node.getType()) {
            case 8: 
            case 9: {
                ConsAltNode can = (ConsAltNode)node;
                do {
                    int v;
                    if ((v = this.quantifiersMemoryInfo(can.car)) <= info) continue;
                    info = v;
                } while ((can = can.cdr) != null);
                break;
            }
            case 5: {
                QuantifierNode qn = (QuantifierNode)node;
                if (qn.upper == 0) break;
                info = this.quantifiersMemoryInfo(qn.target);
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 1: {
                        return 2;
                    }
                    case 2: 
                    case 4: {
                        info = this.quantifiersMemoryInfo(en.target);
                        break block0;
                    }
                }
                break;
            }
        }
        return info;
    }

    private int getMinMatchLength(Node node) {
        int min = 0;
        block0 : switch (node.getType()) {
            case 4: {
                BackRefNode br = (BackRefNode)node;
                if (br.isRecursion()) break;
                if (br.backRef > this.env.numMem) {
                    throw new ValueException("invalid backref number");
                }
                min = this.getMinMatchLength(this.env.memNodes[br.backRef]);
                break;
            }
            case 8: {
                ConsAltNode can = (ConsAltNode)node;
                do {
                    min += this.getMinMatchLength(can.car);
                } while ((can = can.cdr) != null);
                break;
            }
            case 9: {
                ConsAltNode y = (ConsAltNode)node;
                do {
                    Node x = y.car;
                    int tmin = this.getMinMatchLength(x);
                    if (y == node) {
                        min = tmin;
                        continue;
                    }
                    if (min <= tmin) continue;
                    min = tmin;
                } while ((y = y.cdr) != null);
                break;
            }
            case 0: {
                min = ((StringNode)node).length();
                break;
            }
            case 2: {
                min = 1;
                break;
            }
            case 1: 
            case 3: {
                min = 1;
                break;
            }
            case 5: {
                QuantifierNode qn = (QuantifierNode)node;
                if (qn.lower <= 0) break;
                min = this.getMinMatchLength(qn.target);
                min = MinMaxLen.distanceMultiply(min, qn.lower);
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 1: {
                        if (en.isMinFixed()) {
                            min = en.minLength;
                            break block0;
                        }
                        en.minLength = min = this.getMinMatchLength(en.target);
                        en.setMinFixed();
                        break block0;
                    }
                    case 2: 
                    case 4: {
                        min = this.getMinMatchLength(en.target);
                        break block0;
                    }
                }
                break;
            }
        }
        return min;
    }

    private int getMaxMatchLength(Node node) {
        int max = 0;
        block0 : switch (node.getType()) {
            case 8: {
                ConsAltNode ln = (ConsAltNode)node;
                do {
                    int tmax = this.getMaxMatchLength(ln.car);
                    max = MinMaxLen.distanceAdd(max, tmax);
                } while ((ln = ln.cdr) != null);
                break;
            }
            case 9: {
                ConsAltNode an = (ConsAltNode)node;
                do {
                    int tmax;
                    if (max >= (tmax = this.getMaxMatchLength(an.car))) continue;
                    max = tmax;
                } while ((an = an.cdr) != null);
                break;
            }
            case 0: {
                max = ((StringNode)node).length();
                break;
            }
            case 2: {
                max = 1;
                break;
            }
            case 1: 
            case 3: {
                max = 1;
                break;
            }
            case 4: {
                BackRefNode br = (BackRefNode)node;
                if (br.isRecursion()) {
                    max = Integer.MAX_VALUE;
                    break;
                }
                if (br.backRef > this.env.numMem) {
                    throw new ValueException("invalid backref number");
                }
                int tmax = this.getMaxMatchLength(this.env.memNodes[br.backRef]);
                if (max >= tmax) break;
                max = tmax;
                break;
            }
            case 5: {
                QuantifierNode qn = (QuantifierNode)node;
                if (qn.upper == 0 || (max = this.getMaxMatchLength(qn.target)) == 0) break;
                if (!QuantifierNode.isRepeatInfinite(qn.upper)) {
                    max = MinMaxLen.distanceMultiply(max, qn.upper);
                    break;
                }
                max = Integer.MAX_VALUE;
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 1: {
                        if (en.isMaxFixed()) {
                            max = en.maxLength;
                            break block0;
                        }
                        en.maxLength = max = this.getMaxMatchLength(en.target);
                        en.setMaxFixed();
                        break block0;
                    }
                    case 2: 
                    case 4: {
                        max = this.getMaxMatchLength(en.target);
                        break block0;
                    }
                }
                break;
            }
        }
        return max;
    }

    protected final int getCharLengthTree(Node node) {
        return this.getCharLengthTree(node, 0);
    }

    private int getCharLengthTree(Node node, int levelp) {
        int level = levelp + 1;
        int len = 0;
        this.returnCode = 0;
        block0 : switch (node.getType()) {
            case 8: {
                ConsAltNode ln = (ConsAltNode)node;
                do {
                    int tlen = this.getCharLengthTree(ln.car, level);
                    if (this.returnCode != 0) continue;
                    len = MinMaxLen.distanceAdd(len, tlen);
                } while (this.returnCode == 0 && (ln = ln.cdr) != null);
                break;
            }
            case 9: {
                ConsAltNode an = (ConsAltNode)node;
                boolean varLen = false;
                int tlen = this.getCharLengthTree(an.car, level);
                while (this.returnCode == 0 && (an = an.cdr) != null) {
                    int tlen2 = this.getCharLengthTree(an.car, level);
                    if (this.returnCode != 0 || tlen == tlen2) continue;
                    varLen = true;
                }
                if (this.returnCode != 0) break;
                if (varLen) {
                    if (level == 1) {
                        this.returnCode = -2;
                        break;
                    }
                    this.returnCode = -1;
                    break;
                }
                len = tlen;
                break;
            }
            case 0: {
                StringNode sn = (StringNode)node;
                len = sn.length();
                break;
            }
            case 5: {
                QuantifierNode qn = (QuantifierNode)node;
                if (qn.lower == qn.upper) {
                    int tlen = this.getCharLengthTree(qn.target, level);
                    if (this.returnCode != 0) break;
                    len = MinMaxLen.distanceMultiply(tlen, qn.lower);
                    break;
                }
                this.returnCode = -1;
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                len = 1;
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 1: {
                        if (en.isCLenFixed()) {
                            len = en.charLength;
                            break;
                        }
                        len = this.getCharLengthTree(en.target, level);
                        if (this.returnCode != 0) break block0;
                        en.charLength = len;
                        en.setCLenFixed();
                        break;
                    }
                    case 2: 
                    case 4: {
                        len = this.getCharLengthTree(en.target, level);
                        break;
                    }
                }
                break;
            }
            case 7: {
                break;
            }
            default: {
                this.returnCode = -1;
            }
        }
        return len;
    }

    private static boolean isNotIncluded(Node xn, Node yn) {
        Node x = xn;
        Node y = yn;
        block17: while (true) {
            int yType = y.getType();
            switch (x.getType()) {
                case 2: {
                    Node tmp;
                    switch (yType) {
                        case 1: {
                            tmp = x;
                            x = y;
                            y = tmp;
                            continue block17;
                        }
                        case 0: {
                            tmp = x;
                            x = y;
                            y = tmp;
                            continue block17;
                        }
                    }
                    break block17;
                }
                case 1: {
                    Node tmp;
                    CClassNode xc = (CClassNode)x;
                    switch (yType) {
                        case 1: {
                            CClassNode yc = (CClassNode)y;
                            for (int i = 0; i < 256; ++i) {
                                boolean v = xc.bs.at(i);
                                if ((!v || xc.isNot()) && (v || !xc.isNot()) || (!(v = yc.bs.at(i)) || yc.isNot()) && (v || !yc.isNot())) continue;
                                return false;
                            }
                            return xc.mbuf == null && !xc.isNot() || yc.mbuf == null && !yc.isNot();
                        }
                        case 0: {
                            tmp = x;
                            x = y;
                            y = tmp;
                            continue block17;
                        }
                    }
                    break block17;
                }
                case 0: {
                    StringNode xs = (StringNode)x;
                    if (xs.length() == 0) break block17;
                    switch (yType) {
                        case 1: {
                            CClassNode cc = (CClassNode)y;
                            char code = xs.chars[xs.p];
                            return !cc.isCodeInCC(code);
                        }
                        case 0: {
                            StringNode ys = (StringNode)y;
                            int len = xs.length();
                            if (len > ys.length()) {
                                len = ys.length();
                            }
                            if (xs.isAmbig() || ys.isAmbig()) {
                                return false;
                            }
                            int i = 0;
                            int pt = ys.p;
                            int q = xs.p;
                            while (i < len) {
                                if (ys.chars[pt] != xs.chars[q]) {
                                    return true;
                                }
                                ++i;
                                ++pt;
                                ++q;
                            }
                            break block17;
                        }
                    }
                    break block17;
                }
            }
            break;
        }
        return false;
    }

    private Node getHeadValueNode(Node node, boolean exact) {
        Node n = null;
        block0 : switch (node.getType()) {
            case 3: 
            case 4: 
            case 9: {
                break;
            }
            case 1: 
            case 2: {
                if (exact) break;
                n = node;
                break;
            }
            case 8: {
                n = this.getHeadValueNode(((ConsAltNode)node).car, exact);
                break;
            }
            case 0: {
                StringNode sn = (StringNode)node;
                if (sn.end <= sn.p || exact && !sn.isRaw() && Option.isIgnoreCase(this.regex.options)) break;
                n = node;
                break;
            }
            case 5: {
                QuantifierNode qn = (QuantifierNode)node;
                if (qn.lower <= 0) break;
                if (qn.headExact != null) {
                    n = qn.headExact;
                    break;
                }
                n = this.getHeadValueNode(qn.target, exact);
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 2: {
                        int options = this.regex.options;
                        this.regex.options = en.option;
                        n = this.getHeadValueNode(en.target, exact);
                        this.regex.options = options;
                        break block0;
                    }
                    case 1: 
                    case 4: {
                        n = this.getHeadValueNode(en.target, exact);
                        break block0;
                    }
                }
                break;
            }
            case 7: {
                AnchorNode an = (AnchorNode)node;
                if (an.type != 1024) break;
                n = this.getHeadValueNode(an.target, exact);
                break;
            }
        }
        return n;
    }

    private boolean checkTypeTree(Node node, int typeMask, int encloseMask, int anchorMask) {
        if ((node.getType2Bit() & typeMask) == 0) {
            return true;
        }
        boolean invalid = false;
        switch (node.getType()) {
            case 8: 
            case 9: {
                ConsAltNode can = (ConsAltNode)node;
                while (!(invalid = this.checkTypeTree(can.car, typeMask, encloseMask, anchorMask)) && (can = can.cdr) != null) {
                }
                break;
            }
            case 5: {
                invalid = this.checkTypeTree(((QuantifierNode)node).target, typeMask, encloseMask, anchorMask);
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                if ((en.type & encloseMask) == 0) {
                    return true;
                }
                invalid = this.checkTypeTree(en.target, typeMask, encloseMask, anchorMask);
                break;
            }
            case 7: {
                AnchorNode an = (AnchorNode)node;
                if ((an.type & anchorMask) == 0) {
                    return true;
                }
                if (an.target == null) break;
                invalid = this.checkTypeTree(an.target, typeMask, encloseMask, anchorMask);
                break;
            }
        }
        return invalid;
    }

    private Node divideLookBehindAlternatives(Node nodep) {
        Node node = nodep;
        AnchorNode an = (AnchorNode)node;
        int anchorType = an.type;
        Node head = an.target;
        Node np = ((ConsAltNode)head).car;
        this.swap(node, head);
        Node tmp = node;
        node = head;
        head = tmp;
        ((ConsAltNode)node).setCar(head);
        ((AnchorNode)head).setTarget(np);
        np = node;
        while ((np = ((ConsAltNode)np).cdr) != null) {
            AnchorNode insert = new AnchorNode(anchorType);
            insert.setTarget(((ConsAltNode)np).car);
            ((ConsAltNode)np).setCar(insert);
        }
        if (anchorType == 8192) {
            np = node;
            do {
                ((ConsAltNode)np).toListNode();
            } while ((np = ((ConsAltNode)np).cdr) != null);
        }
        return node;
    }

    private Node setupLookBehind(Node node) {
        AnchorNode an = (AnchorNode)node;
        int len = this.getCharLengthTree(an.target);
        switch (this.returnCode) {
            case 0: {
                an.charLength = len;
                break;
            }
            case -1: {
                throw new SyntaxException("invalid pattern in look-behind");
            }
            case -2: {
                if (this.syntax.differentLengthAltLookBehind()) {
                    return this.divideLookBehindAlternatives(node);
                }
                throw new SyntaxException("invalid pattern in look-behind");
            }
        }
        return node;
    }

    private void nextSetup(Node nodep, Node nextNode) {
        Node node = nodep;
        while (true) {
            EncloseNode en;
            int type;
            if ((type = node.getType()) == 5) {
                Node y;
                Node x;
                QuantifierNode qn = (QuantifierNode)node;
                if (!qn.greedy || !QuantifierNode.isRepeatInfinite(qn.upper)) break;
                StringNode n = (StringNode)this.getHeadValueNode(nextNode, true);
                if (n != null && n.chars[n.p] != '\u0000') {
                    qn.nextHeadExact = n;
                }
                if (qn.lower > 1 || !qn.target.isSimple() || (x = this.getHeadValueNode(qn.target, false)) == null || (y = this.getHeadValueNode(nextNode, false)) == null || !Analyser.isNotIncluded(x, y)) break;
                EncloseNode en2 = new EncloseNode(4);
                en2.setStopBtSimpleRepeat();
                this.swap(node, en2);
                en2.setTarget(node);
                break;
            }
            if (type != 6 || !(en = (EncloseNode)node).isMemory()) break;
            node = en.target;
        }
    }

    private void updateStringNodeCaseFoldMultiByte(StringNode sn) {
        char[] ch = sn.chars;
        int end = sn.end;
        this.value = sn.p;
        int sp = 0;
        while (this.value < end) {
            char buf;
            int ovalue = this.value;
            if (ch[ovalue] != (buf = EncodingHelper.toLowerCase(ch[this.value++]))) {
                char[] sbuf = new char[sn.length() << 1];
                System.arraycopy(ch, sn.p, sbuf, 0, ovalue - sn.p);
                this.value = ovalue;
                while (this.value < end) {
                    buf = EncodingHelper.toLowerCase(ch[this.value++]);
                    if (sp >= sbuf.length) {
                        char[] tmp = new char[sbuf.length << 1];
                        System.arraycopy(sbuf, 0, tmp, 0, sbuf.length);
                        sbuf = tmp;
                    }
                    sbuf[sp++] = buf;
                }
                sn.set(sbuf, 0, sp);
                return;
            }
            ++sp;
        }
    }

    private void updateStringNodeCaseFold(Node node) {
        StringNode sn = (StringNode)node;
        this.updateStringNodeCaseFoldMultiByte(sn);
    }

    private Node expandCaseFoldMakeRemString(char[] ch, int pp, int end) {
        StringNode node = new StringNode(ch, pp, end);
        this.updateStringNodeCaseFold(node);
        node.setAmbig();
        node.setDontGetOptInfo();
        return node;
    }

    private static boolean expandCaseFoldStringAlt(int itemNum, char[] items, char[] chars, int p, int slen, int end, ObjPtr<Node> node) {
        ConsAltNode altNode = ConsAltNode.newAltNode(null, null);
        node.p = altNode;
        StringNode snode = new StringNode(chars, p, p + slen);
        altNode.setCar(snode);
        for (int i = 0; i < itemNum; ++i) {
            snode = new StringNode();
            snode.catCode(items[i]);
            ConsAltNode an = ConsAltNode.newAltNode(null, null);
            an.setCar(snode);
            altNode.setCdr(an);
            altNode = an;
        }
        return false;
    }

    private Node expandCaseFoldString(Node node) {
        int pt;
        StringNode sn = (StringNode)node;
        if (sn.isAmbig() || sn.length() <= 0) {
            return node;
        }
        char[] chars1 = sn.chars;
        int end = sn.end;
        int altNum = 1;
        Node topRoot = null;
        ConsAltNode r = null;
        ObjPtr<Node> prevNode = new ObjPtr<Node>();
        StringNode stringNode = null;
        for (pt = sn.p; pt < end; ++pt) {
            char[] items = EncodingHelper.caseFoldCodesByString(this.regex.caseFoldFlag, chars1[pt]);
            if (items.length == 0) {
                if (stringNode == null) {
                    if (r == null && prevNode.p != null) {
                        r = ConsAltNode.listAdd(null, (Node)prevNode.p);
                        topRoot = r;
                    }
                    stringNode = new StringNode();
                    prevNode.p = stringNode;
                    if (r != null) {
                        ConsAltNode.listAdd(r, stringNode);
                    }
                }
                stringNode.cat(chars1, pt, pt + 1);
                continue;
            }
            if ((altNum *= items.length + 1) > 8) break;
            if (r == null && prevNode.p != null) {
                r = ConsAltNode.listAdd(null, (Node)prevNode.p);
                topRoot = r;
            }
            Analyser.expandCaseFoldStringAlt(items.length, items, chars1, pt, 1, end, prevNode);
            if (r != null) {
                ConsAltNode.listAdd(r, (Node)prevNode.p);
            }
            stringNode = null;
        }
        if (pt < end) {
            Node srem = this.expandCaseFoldMakeRemString(chars1, pt, end);
            if (prevNode.p != null && r == null) {
                r = ConsAltNode.listAdd(null, (Node)prevNode.p);
                topRoot = r;
            }
            if (r == null) {
                prevNode.p = srem;
            } else {
                ConsAltNode.listAdd(r, srem);
            }
        }
        Node xnode = topRoot != null ? topRoot : (Node)prevNode.p;
        this.swap(node, xnode);
        return xnode;
    }

    /*
     * Unable to fully structure code
     */
    protected final Node setupTree(Node nodep, int statep) {
        node = nodep;
        state = statep;
        block22: while (true) lbl-1000:
        // 3 sources

        {
            switch (node.getType()) {
                case 8: {
                    lin = (ConsAltNode)node;
                    prev = null;
                    do {
                        this.setupTree(lin.car, state);
                        if (prev != null) {
                            this.nextSetup(prev, lin.car);
                        }
                        prev = lin.car;
                    } while ((lin = lin.cdr) != null);
                    break block22;
                }
                case 9: {
                    aln = (ConsAltNode)node;
                    do {
                        this.setupTree(aln.car, state | 1);
                    } while ((aln = aln.cdr) != null);
                    break block22;
                }
                case 1: {
                    break block22;
                }
                case 0: {
                    if (!Option.isIgnoreCase(this.regex.options) || ((StringNode)node).isRaw()) break block22;
                    node = this.expandCaseFoldString(node);
                    break block22;
                }
                case 2: 
                case 3: {
                    break block22;
                }
                case 4: {
                    br = (BackRefNode)node;
                    if (br.backRef > this.env.numMem) {
                        throw new ValueException("invalid backref number");
                    }
                    this.env.backrefedMem = BitStatus.bsOnAt(this.env.backrefedMem, br.backRef);
                    this.env.btMemStart = BitStatus.bsOnAt(this.env.btMemStart, br.backRef);
                    ((EncloseNode)this.env.memNodes[br.backRef]).setMemBackrefed();
                    break block22;
                }
                case 5: {
                    qn = (QuantifierNode)node;
                    target = qn.target;
                    if ((state & 4) != 0) {
                        qn.setInRepeat();
                    }
                    if ((QuantifierNode.isRepeatInfinite(qn.upper) || qn.lower >= 1) && (d = this.getMinMatchLength(target)) == 0) {
                        qn.targetEmptyInfo = 1;
                        info = this.quantifiersMemoryInfo(target);
                        if (info > 0) {
                            qn.targetEmptyInfo = info;
                        }
                    }
                    state |= 4;
                    if (qn.lower != qn.upper) {
                        state |= 8;
                    }
                    if ((target = this.setupTree(target, state)).getType() == 0 && !QuantifierNode.isRepeatInfinite(qn.lower) && qn.lower == qn.upper && qn.lower > 1 && qn.lower <= 100 && (len = (sn = (StringNode)target).length()) * qn.lower <= 100) {
                        str = qn.convertToString(sn.flag);
                        n = qn.lower;
                        for (i = 0; i < n; ++i) {
                            str.cat(sn.chars, sn.p, sn.end);
                        }
                        break block22;
                    }
                    if (!qn.greedy || qn.targetEmptyInfo == 0) break block22;
                    if (target.getType() == 5) {
                        tqn = (QuantifierNode)target;
                        if (tqn.headExact == null) break block22;
                        qn.headExact = tqn.headExact;
                        tqn.headExact = null;
                        break block22;
                    }
                    qn.headExact = this.getHeadValueNode(qn.target, true);
                    break block22;
                }
                case 6: {
                    en = (EncloseNode)node;
                    switch (en.type) {
                        case 2: {
                            options = this.regex.options;
                            this.regex.options = en.option;
                            this.setupTree(en.target, state);
                            this.regex.options = options;
                            break;
                        }
                        case 1: {
                            if ((state & 11) != 0) {
                                this.env.btMemStart = BitStatus.bsOnAt(this.env.btMemStart, en.regNum);
                            }
                            this.setupTree(en.target, state);
                            break;
                        }
                        case 4: {
                            this.setupTree(en.target, state);
                            if (en.target.getType() != 5) break;
                            tqn = (QuantifierNode)en.target;
                            if (!QuantifierNode.isRepeatInfinite(tqn.upper) || tqn.lower > 1 || !tqn.greedy || !tqn.target.isSimple()) break block22;
                            en.setStopBtSimpleRepeat();
                            break;
                        }
                    }
                    break block22;
                }
                case 7: {
                    an = (AnchorNode)node;
                    switch (an.type) {
                        case 1024: {
                            this.setupTree(an.target, state);
                            break block22;
                        }
                        case 2048: {
                            this.setupTree(an.target, state | 2);
                            break block22;
                        }
                        case 4096: {
                            if (this.checkTypeTree(an.target, 2031, 1, 4135)) {
                                throw new SyntaxException("invalid pattern in look-behind");
                            }
                            if ((node = this.setupLookBehind(node)).getType() != 7) ** GOTO lbl-1000
                            this.setupTree(((AnchorNode)node).target, state);
                            break block22;
                        }
                        case 8192: {
                            if (!this.checkTypeTree(an.target, 2031, 1, 4135)) continue block22;
                            throw new SyntaxException("invalid pattern in look-behind");
                            if ((node = this.setupLookBehind(node)).getType() != 7) continue block22;
                            this.setupTree(((AnchorNode)node).target, state | 2);
                            break block22;
                        }
                    }
                    break block22;
                }
            }
            break;
        }
        return node;
    }

    private void optimizeNodeLeft(Node node, NodeOptInfo opt, OptEnvironment oenv) {
        opt.clear();
        opt.setBoundNode(oenv.mmd);
        block0 : switch (node.getType()) {
            case 8: {
                OptEnvironment nenv = new OptEnvironment();
                NodeOptInfo nopt = new NodeOptInfo();
                nenv.copy(oenv);
                ConsAltNode lin = (ConsAltNode)node;
                do {
                    this.optimizeNodeLeft(lin.car, nopt, nenv);
                    nenv.mmd.add(nopt.length);
                    opt.concatLeftNode(nopt);
                } while ((lin = lin.cdr) != null);
                break;
            }
            case 9: {
                NodeOptInfo nopt = new NodeOptInfo();
                ConsAltNode aln = (ConsAltNode)node;
                do {
                    this.optimizeNodeLeft(aln.car, nopt, oenv);
                    if (aln == node) {
                        opt.copy(nopt);
                        continue;
                    }
                    opt.altMerge(nopt, oenv);
                } while ((aln = aln.cdr) != null);
                break;
            }
            case 0: {
                StringNode sn = (StringNode)node;
                int slen = sn.length();
                if (!sn.isAmbig()) {
                    opt.exb.concatStr(sn.chars, sn.p, sn.end, sn.isRaw());
                    if (slen > 0) {
                        opt.map.addChar(sn.chars[sn.p]);
                    }
                    opt.length.set(slen, slen);
                } else {
                    int max;
                    if (sn.isDontGetOptInfo()) {
                        max = sn.length();
                    } else {
                        opt.exb.concatStr(sn.chars, sn.p, sn.end, sn.isRaw());
                        opt.exb.ignoreCase = true;
                        if (slen > 0) {
                            opt.map.addCharAmb(sn.chars, sn.p, sn.end, oenv.caseFoldFlag);
                        }
                        max = slen;
                    }
                    opt.length.set(slen, max);
                }
                if (opt.exb.length != slen) break;
                opt.exb.reachEnd = true;
                break;
            }
            case 1: {
                CClassNode cc = (CClassNode)node;
                if (cc.mbuf != null || cc.isNot()) {
                    opt.length.set(1, 1);
                    break;
                }
                for (int i = 0; i < 256; ++i) {
                    boolean z = cc.bs.at(i);
                    if ((!z || cc.isNot()) && (z || !cc.isNot())) continue;
                    opt.map.addChar(i);
                }
                opt.length.set(1, 1);
                break;
            }
            case 3: {
                opt.length.set(1, 1);
                break;
            }
            case 7: {
                AnchorNode an = (AnchorNode)node;
                switch (an.type) {
                    case 1: 
                    case 2: 
                    case 4: 
                    case 8: 
                    case 16: 
                    case 32: {
                        opt.anchor.add(an.type);
                        break;
                    }
                    case 1024: {
                        NodeOptInfo nopt = new NodeOptInfo();
                        this.optimizeNodeLeft(an.target, nopt, oenv);
                        if (nopt.exb.length > 0) {
                            opt.expr.copy(nopt.exb);
                        } else if (nopt.exm.length > 0) {
                            opt.expr.copy(nopt.exm);
                        }
                        opt.expr.reachEnd = false;
                        if (nopt.map.value <= 0) break block0;
                        opt.map.copy(nopt.map);
                        break;
                    }
                    case 2048: 
                    case 4096: 
                    case 8192: {
                        break;
                    }
                }
                break;
            }
            case 4: {
                BackRefNode br = (BackRefNode)node;
                if (br.isRecursion()) {
                    opt.length.set(0, Integer.MAX_VALUE);
                    break;
                }
                Node[] nodes = oenv.scanEnv.memNodes;
                int min = this.getMinMatchLength(nodes[br.backRef]);
                int max = this.getMaxMatchLength(nodes[br.backRef]);
                opt.length.set(min, max);
                break;
            }
            case 5: {
                NodeOptInfo nopt = new NodeOptInfo();
                QuantifierNode qn = (QuantifierNode)node;
                this.optimizeNodeLeft(qn.target, nopt, oenv);
                if (qn.lower == 0 && QuantifierNode.isRepeatInfinite(qn.upper)) {
                    if (oenv.mmd.max == 0 && qn.target.getType() == 3 && qn.greedy) {
                        if (Option.isMultiline(oenv.options)) {
                            opt.anchor.add(32768);
                        } else {
                            opt.anchor.add(16384);
                        }
                    }
                } else if (qn.lower > 0) {
                    opt.copy(nopt);
                    if (nopt.exb.length > 0 && nopt.exb.reachEnd) {
                        int i;
                        for (i = 2; i <= qn.lower && !opt.exb.isFull(); ++i) {
                            opt.exb.concat(nopt.exb);
                        }
                        if (i < qn.lower) {
                            opt.exb.reachEnd = false;
                        }
                    }
                    if (qn.lower != qn.upper) {
                        opt.exb.reachEnd = false;
                        opt.exm.reachEnd = false;
                    }
                    if (qn.lower > 1) {
                        opt.exm.reachEnd = false;
                    }
                }
                int min = MinMaxLen.distanceMultiply(nopt.length.min, qn.lower);
                int max = QuantifierNode.isRepeatInfinite(qn.upper) ? (nopt.length.max > 0 ? Integer.MAX_VALUE : 0) : MinMaxLen.distanceMultiply(nopt.length.max, qn.upper);
                opt.length.set(min, max);
                break;
            }
            case 6: {
                EncloseNode en = (EncloseNode)node;
                switch (en.type) {
                    case 2: {
                        int save = oenv.options;
                        oenv.options = en.option;
                        this.optimizeNodeLeft(en.target, opt, oenv);
                        oenv.options = save;
                        break;
                    }
                    case 1: {
                        if (++en.optCount > 5) {
                            int min = 0;
                            int max = Integer.MAX_VALUE;
                            if (en.isMinFixed()) {
                                min = en.minLength;
                            }
                            if (en.isMaxFixed()) {
                                max = en.maxLength;
                            }
                            opt.length.set(min, max);
                            break;
                        }
                        this.optimizeNodeLeft(en.target, opt, oenv);
                        if (!opt.anchor.isSet(49152) || !BitStatus.bsAt(oenv.scanEnv.backrefedMem, en.regNum)) break block0;
                        opt.anchor.remove(49152);
                        break;
                    }
                    case 4: {
                        this.optimizeNodeLeft(en.target, opt, oenv);
                        break;
                    }
                }
                break;
            }
            default: {
                throw new InternalException("internal parser error (bug)");
            }
        }
    }

    protected final void setOptimizedInfoFromTree(Node node) {
        NodeOptInfo opt = new NodeOptInfo();
        OptEnvironment oenv = new OptEnvironment();
        oenv.options = this.regex.options;
        oenv.caseFoldFlag = this.regex.caseFoldFlag;
        oenv.scanEnv = this.env;
        oenv.mmd.clear();
        this.optimizeNodeLeft(node, opt, oenv);
        this.regex.anchor = opt.anchor.leftAnchor & 0xC005;
        this.regex.anchor |= opt.anchor.rightAnchor & 0x18;
        if ((this.regex.anchor & 0x18) != 0) {
            this.regex.anchorDmin = opt.length.min;
            this.regex.anchorDmax = opt.length.max;
        }
        if (opt.exb.length > 0 || opt.exm.length > 0) {
            opt.exb.select(opt.exm);
            if (opt.map.value > 0 && opt.exb.compare(opt.map) > 0) {
                this.regex.setOptimizeMapInfo(opt.map);
                this.regex.setSubAnchor(opt.map.anchor);
            } else {
                this.regex.setExactInfo(opt.exb);
                this.regex.setSubAnchor(opt.exb.anchor);
            }
        } else if (opt.map.value > 0) {
            this.regex.setOptimizeMapInfo(opt.map);
            this.regex.setSubAnchor(opt.map.anchor);
        } else {
            this.regex.subAnchor |= opt.anchor.leftAnchor & 2;
            if (opt.length.max == 0) {
                this.regex.subAnchor |= opt.anchor.rightAnchor & 0x20;
            }
        }
    }
}

