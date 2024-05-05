/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.BitStatus;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.Syntax;
import jdk.nashorn.internal.runtime.regexp.joni.ast.Node;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

public final class ScanEnvironment {
    private static final int SCANENV_MEMNODES_SIZE = 8;
    int option;
    final int caseFoldFlag;
    public final Syntax syntax;
    int captureHistory;
    int btMemStart;
    int btMemEnd;
    int backrefedMem;
    public final Regex reg;
    public int numMem;
    public Node[] memNodes;

    public ScanEnvironment(Regex regex, Syntax syntax) {
        this.reg = regex;
        this.option = regex.options;
        this.caseFoldFlag = regex.caseFoldFlag;
        this.syntax = syntax;
    }

    public void clear() {
        this.captureHistory = BitStatus.bsClear();
        this.btMemStart = BitStatus.bsClear();
        this.btMemEnd = BitStatus.bsClear();
        this.backrefedMem = BitStatus.bsClear();
        this.numMem = 0;
        this.memNodes = null;
    }

    public int addMemEntry() {
        if (this.numMem++ == 0) {
            this.memNodes = new Node[8];
        } else if (this.numMem >= this.memNodes.length) {
            Node[] tmp = new Node[this.memNodes.length << 1];
            System.arraycopy(this.memNodes, 0, tmp, 0, this.memNodes.length);
            this.memNodes = tmp;
        }
        return this.numMem;
    }

    public void setMemNode(int num, Node node) {
        if (this.numMem < num) {
            throw new InternalException("internal parser error (bug)");
        }
        this.memNodes[num] = node;
    }

    public int convertBackslashValue(int c) {
        if (this.syntax.opEscControlChars()) {
            switch (c) {
                case 110: {
                    return 10;
                }
                case 116: {
                    return 9;
                }
                case 114: {
                    return 13;
                }
                case 102: {
                    return 12;
                }
                case 97: {
                    return 7;
                }
                case 98: {
                    return 8;
                }
                case 101: {
                    return 27;
                }
                case 118: {
                    if (!this.syntax.op2EscVVtab()) break;
                    return 11;
                }
            }
        }
        return c;
    }

    void ccEscWarn(String s) {
        if (this.syntax.warnCCOpNotEscaped() && this.syntax.backSlashEscapeInCC()) {
            this.reg.warnings.warn("character class has '" + s + "' without escape");
        }
    }
}

