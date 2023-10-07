/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import java.lang.ref.WeakReference;
import jdk.nashorn.internal.runtime.regexp.joni.Matcher;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.StackEntry;
import jdk.nashorn.internal.runtime.regexp.joni.constants.StackType;

abstract class StackMachine
extends Matcher
implements StackType {
    protected static final int INVALID_INDEX = -1;
    protected StackEntry[] stack;
    protected int stk;
    protected final int[] repeatStk;
    protected final int memStartStk;
    protected final int memEndStk;
    static final ThreadLocal<WeakReference<StackEntry[]>> stacks = new ThreadLocal<WeakReference<StackEntry[]>>(){

        @Override
        protected WeakReference<StackEntry[]> initialValue() {
            return new WeakReference<StackEntry[]>(StackMachine.allocateStack());
        }
    };

    protected StackMachine(Regex regex, char[] chars, int p, int end) {
        super(regex, chars, p, end);
        this.stack = regex.stackNeeded ? StackMachine.fetchStack() : null;
        int n = regex.numRepeat + (regex.numMem << 1);
        this.repeatStk = n > 0 ? new int[n] : null;
        this.memStartStk = regex.numRepeat - 1;
        this.memEndStk = this.memStartStk + regex.numMem;
    }

    private static StackEntry[] allocateStack() {
        StackEntry[] stack = new StackEntry[64];
        stack[0] = new StackEntry();
        return stack;
    }

    private void doubleStack() {
        StackEntry[] newStack = new StackEntry[this.stack.length << 1];
        System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
        this.stack = newStack;
    }

    private static StackEntry[] fetchStack() {
        WeakReference<StackEntry[]> ref = stacks.get();
        StackEntry[] stack = (StackEntry[])ref.get();
        if (stack == null) {
            stack = StackMachine.allocateStack();
            ref = new WeakReference<StackEntry[]>(stack);
            stacks.set(ref);
        }
        return stack;
    }

    protected final void init() {
        if (this.stack != null) {
            this.pushEnsured(1, this.regex.codeLength - 1);
        }
        if (this.repeatStk != null) {
            for (int i = 1; i <= this.regex.numMem; ++i) {
                this.repeatStk[i + this.memEndStk] = -1;
                this.repeatStk[i + this.memStartStk] = -1;
            }
        }
    }

    protected final StackEntry ensure1() {
        StackEntry e;
        if (this.stk >= this.stack.length) {
            this.doubleStack();
        }
        if ((e = this.stack[this.stk]) == null) {
            this.stack[this.stk] = e = new StackEntry();
        }
        return e;
    }

    protected final void pushType(int type) {
        this.ensure1().type = type;
        ++this.stk;
    }

    private void push(int type, int pat, int s, int prev) {
        StackEntry e = this.ensure1();
        e.type = type;
        e.setStatePCode(pat);
        e.setStatePStr(s);
        e.setStatePStrPrev(prev);
        ++this.stk;
    }

    protected final void pushEnsured(int type, int pat) {
        StackEntry e = this.stack[this.stk];
        e.type = type;
        e.setStatePCode(pat);
        ++this.stk;
    }

    protected final void pushAlt(int pat, int s, int prev) {
        this.push(1, pat, s, prev);
    }

    protected final void pushPos(int s, int prev) {
        this.push(1280, -1, s, prev);
    }

    protected final void pushPosNot(int pat, int s, int prev) {
        this.push(3, pat, s, prev);
    }

    protected final void pushStopBT() {
        this.pushType(1536);
    }

    protected final void pushLookBehindNot(int pat, int s, int sprev) {
        this.push(2, pat, s, sprev);
    }

    protected final void pushRepeat(int id, int pat) {
        StackEntry e = this.ensure1();
        e.type = 1792;
        e.setRepeatNum(id);
        e.setRepeatPCode(pat);
        e.setRepeatCount(0);
        ++this.stk;
    }

    protected final void pushRepeatInc(int sindex) {
        StackEntry e = this.ensure1();
        e.type = 768;
        e.setSi(sindex);
        ++this.stk;
    }

    protected final void pushMemStart(int mnum, int s) {
        StackEntry e = this.ensure1();
        e.type = 256;
        e.setMemNum(mnum);
        e.setMemPstr(s);
        e.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memStartStk + mnum] = this.stk++;
        this.repeatStk[this.memEndStk + mnum] = -1;
    }

    protected final void pushMemEnd(int mnum, int s) {
        StackEntry e = this.ensure1();
        e.type = 33280;
        e.setMemNum(mnum);
        e.setMemPstr(s);
        e.setMemStart(this.repeatStk[this.memStartStk + mnum]);
        e.setMemEnd(this.repeatStk[this.memEndStk + mnum]);
        this.repeatStk[this.memEndStk + mnum] = this.stk++;
    }

    protected final void pushMemEndMark(int mnum) {
        StackEntry e = this.ensure1();
        e.type = 33792;
        e.setMemNum(mnum);
        ++this.stk;
    }

    protected final int getMemStart(int mnum) {
        int level = 0;
        int stkp = this.stk;
        while (stkp > 0) {
            StackEntry e = this.stack[--stkp];
            if ((e.type & 0x8000) != 0 && e.getMemNum() == mnum) {
                ++level;
                continue;
            }
            if (e.type != 256 || e.getMemNum() != mnum) continue;
            if (level == 0) break;
            --level;
        }
        return stkp;
    }

    protected final void pushNullCheckStart(int cnum, int s) {
        StackEntry e = this.ensure1();
        e.type = 12288;
        e.setNullCheckNum(cnum);
        e.setNullCheckPStr(s);
        ++this.stk;
    }

    protected final void pushNullCheckEnd(int cnum) {
        StackEntry e = this.ensure1();
        e.type = 20480;
        e.setNullCheckNum(cnum);
        ++this.stk;
    }

    protected final void popOne() {
        --this.stk;
    }

    protected final StackEntry pop() {
        switch (this.regex.stackPopLevel) {
            case 0: {
                return this.popFree();
            }
            case 1: {
                return this.popMemStart();
            }
        }
        return this.popDefault();
    }

    private StackEntry popFree() {
        StackEntry e;
        do {
            e = this.stack[--this.stk];
        } while ((e.type & 0xFF) == 0);
        return e;
    }

    private StackEntry popMemStart() {
        while (true) {
            StackEntry e = this.stack[--this.stk];
            if ((e.type & 0xFF) != 0) {
                return e;
            }
            if (e.type != 256) continue;
            this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
            this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
        }
    }

    private StackEntry popDefault() {
        while (true) {
            StackEntry e = this.stack[--this.stk];
            if ((e.type & 0xFF) != 0) {
                return e;
            }
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
                continue;
            }
            if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
                continue;
            }
            if (e.type != 33280) continue;
            this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
            this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
        }
    }

    protected final void popTilPosNot() {
        while (true) {
            --this.stk;
            StackEntry e = this.stack[this.stk];
            if (e.type == 3) break;
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemStart();
                continue;
            }
            if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
                continue;
            }
            if (e.type != 33280) continue;
            this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
            this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemStart();
        }
    }

    protected final void popTilLookBehindNot() {
        while (true) {
            --this.stk;
            StackEntry e = this.stack[this.stk];
            if (e.type == 2) break;
            if (e.type == 256) {
                this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
                this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
                continue;
            }
            if (e.type == 768) {
                this.stack[e.getSi()].decreaseRepeatCount();
                continue;
            }
            if (e.type != 33280) continue;
            this.repeatStk[this.memStartStk + e.getMemNum()] = e.getMemStart();
            this.repeatStk[this.memEndStk + e.getMemNum()] = e.getMemEnd();
        }
    }

    protected final int posEnd() {
        int k = this.stk;
        while (true) {
            StackEntry e = this.stack[--k];
            if ((e.type & 0x10FF) != 0) {
                e.type = 2560;
                continue;
            }
            if (e.type == 1280) break;
        }
        e.type = 2560;
        return k;
    }

    protected final void stopBtEnd() {
        int k = this.stk;
        while (true) {
            StackEntry e = this.stack[--k];
            if ((e.type & 0x10FF) != 0) {
                e.type = 2560;
                continue;
            }
            if (e.type == 1536) break;
        }
        e.type = 2560;
    }

    protected final int nullCheck(int id, int s) {
        StackEntry e;
        int k = this.stk;
        do {
            e = this.stack[--k];
        } while (e.type != 12288 || e.getNullCheckNum() != id);
        return e.getNullCheckPStr() == s ? 1 : 0;
    }

    protected final int nullCheckMemSt(int id, int s) {
        return -this.nullCheck(id, s);
    }

    protected final int getRepeat(int id) {
        int level = 0;
        int k = this.stk;
        while (true) {
            StackEntry e = this.stack[--k];
            if (e.type == 1792) {
                if (level != 0 || e.getRepeatNum() != id) continue;
                return k;
            }
            if (e.type == 2048) {
                --level;
                continue;
            }
            if (e.type != 2304) continue;
            ++level;
        }
    }

    protected final int sreturn() {
        int level = 0;
        int k = this.stk;
        while (true) {
            StackEntry e = this.stack[--k];
            if (e.type == 2048) {
                if (level == 0) {
                    return e.getCallFrameRetAddr();
                }
                --level;
                continue;
            }
            if (e.type != 2304) continue;
            ++level;
        }
    }
}

