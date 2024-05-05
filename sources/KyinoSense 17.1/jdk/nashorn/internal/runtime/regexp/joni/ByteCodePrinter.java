/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.BitSet;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.ast.CClassNode;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

class ByteCodePrinter {
    final int[] code;
    final int codeLength;
    final char[][] templates;
    Object[] operands;
    private static final String[] OpCodeNames = new String[]{"finish", "end", "exact1", "exact2", "exact3", "exact4", "exact5", "exactn", "exactmb2-n1", "exactmb2-n2", "exactmb2-n3", "exactmb2-n", "exactmb3n", "exactmbn", "exact1-ic", "exactn-ic", "cclass", "cclass-mb", "cclass-mix", "cclass-not", "cclass-mb-not", "cclass-mix-not", "cclass-node", "anychar", "anychar-ml", "anychar*", "anychar-ml*", "anychar*-peek-next", "anychar-ml*-peek-next", "word", "not-word", "word-bound", "not-word-bound", "word-begin", "word-end", "begin-buf", "end-buf", "begin-line", "end-line", "semi-end-buf", "begin-position", "backref1", "backref2", "backrefn", "backrefn-ic", "backref_multi", "backref_multi-ic", "backref_at_level", "mem-start", "mem-start-push", "mem-end-push", "mem-end-push-rec", "mem-end", "mem-end-rec", "fail", "jump", "push", "pop", "push-or-jump-e1", "push-if-peek-next", "repeat", "repeat-ng", "repeat-inc", "repeat-inc-ng", "repeat-inc-sg", "repeat-inc-ng-sg", "null-check-start", "null-check-end", "null-check-end-memst", "null-check-end-memst-push", "push-pos", "pop-pos", "push-pos-not", "fail-pos", "push-stop-bt", "pop-stop-bt", "look-behind", "push-look-behind-not", "fail-look-behind-not", "call", "return", "state-check-push", "state-check-push-or-jump", "state-check", "state-check-anychar*", "state-check-anychar-ml*", "set-option-push", "set-option"};
    private static final int[] OpCodeArgTypes = new int[]{0, 0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, -1, -1, -1, -1, 4, 4, 4, 4, 4, 4, 0, 1, 1, 0, -1, -1, -1, -1, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 1, 0, 0, 0, -1, -1, 0, 2, 0, -1, -1, 6, 6, 6, 5, 5};

    public ByteCodePrinter(Regex regex) {
        this.code = regex.code;
        this.codeLength = regex.codeLength;
        this.operands = regex.operands;
        this.templates = regex.templates;
    }

    public String byteCodeListToString() {
        return this.compiledByteCodeListToString();
    }

    private void pString(StringBuilder sb, int len, int s) {
        sb.append(":");
        sb.append(new String(this.code, s, len));
    }

    private void pLenString(StringBuilder sb, int len, int s) {
        sb.append(":").append(len).append(":");
        sb.append(new String(this.code, s, len));
    }

    private static void pLenStringFromTemplate(StringBuilder sb, int len, char[] tm, int idx) {
        sb.append(":T:").append(len).append(":");
        sb.append(tm, idx, len);
    }

    public int compiledByteCodeToString(StringBuilder sb, int bptr) {
        int bp = bptr;
        sb.append("[").append(OpCodeNames[this.code[bp]]);
        int argType = OpCodeArgTypes[this.code[bp]];
        int ip = bp++;
        if (argType != -1) {
            switch (argType) {
                default: {
                    break;
                }
                case 1: {
                    sb.append(":(").append(this.code[bp]).append(")");
                    ++bp;
                    break;
                }
                case 2: {
                    sb.append(":(").append(this.code[bp]).append(")");
                    ++bp;
                    break;
                }
                case 3: {
                    sb.append(":").append(this.code[bp]);
                    ++bp;
                    break;
                }
                case 4: {
                    sb.append(":").append(this.code[bp]);
                    ++bp;
                    break;
                }
                case 5: {
                    sb.append(":").append(this.code[bp]);
                    ++bp;
                    break;
                }
                case 6: {
                    sb.append(":").append(this.code[bp]);
                    bp += 2;
                    break;
                }
            }
        } else {
            switch (this.code[bp++]) {
                case 2: 
                case 27: 
                case 28: {
                    this.pString(sb, 1, bp++);
                    break;
                }
                case 3: {
                    this.pString(sb, 2, bp);
                    bp += 2;
                    break;
                }
                case 4: {
                    this.pString(sb, 3, bp);
                    bp += 3;
                    break;
                }
                case 5: {
                    this.pString(sb, 4, bp);
                    bp += 4;
                    break;
                }
                case 6: {
                    this.pString(sb, 5, bp);
                    bp += 5;
                    break;
                }
                case 7: {
                    int len = this.code[bp];
                    int tm = this.code[++bp];
                    int idx = this.code[++bp];
                    ++bp;
                    ByteCodePrinter.pLenStringFromTemplate(sb, len, this.templates[tm], idx);
                    break;
                }
                case 14: {
                    this.pString(sb, 1, bp);
                    ++bp;
                    break;
                }
                case 15: {
                    int len = this.code[bp];
                    int tm = this.code[++bp];
                    int idx = this.code[++bp];
                    ++bp;
                    ByteCodePrinter.pLenStringFromTemplate(sb, len, this.templates[tm], idx);
                    break;
                }
                case 16: {
                    BitSet bs = new BitSet();
                    System.arraycopy(this.code, bp, bs.bits, 0, 8);
                    int n = bs.numOn();
                    bp += 8;
                    sb.append(":").append(n);
                    break;
                }
                case 19: {
                    BitSet bs = new BitSet();
                    System.arraycopy(this.code, bp, bs.bits, 0, 8);
                    int n = bs.numOn();
                    bp += 8;
                    sb.append(":").append(n);
                    break;
                }
                case 17: 
                case 20: {
                    int len = this.code[bp];
                    int cod = this.code[++bp];
                    bp += len;
                    sb.append(":").append(cod).append(":").append(len);
                    break;
                }
                case 18: 
                case 21: {
                    BitSet bs = new BitSet();
                    System.arraycopy(this.code, bp, bs.bits, 0, 8);
                    int n = bs.numOn();
                    int len = this.code[bp += 8];
                    int cod = this.code[++bp];
                    bp += len;
                    sb.append(":").append(n).append(":").append(cod).append(":").append(len);
                    break;
                }
                case 22: {
                    CClassNode cc = (CClassNode)this.operands[this.code[bp]];
                    ++bp;
                    int n = cc.bs.numOn();
                    sb.append(":").append(cc).append(":").append(n);
                    break;
                }
                case 44: {
                    int mem = this.code[bp];
                    ++bp;
                    sb.append(":").append(mem);
                    break;
                }
                case 45: 
                case 46: {
                    sb.append(" ");
                    int len = this.code[bp];
                    ++bp;
                    for (int i = 0; i < len; ++i) {
                        int mem = this.code[bp];
                        ++bp;
                        if (i > 0) {
                            sb.append(", ");
                        }
                        sb.append(mem);
                    }
                    break;
                }
                case 47: {
                    int option = this.code[bp];
                    sb.append(":").append(option);
                    int level = this.code[++bp];
                    sb.append(":").append(level);
                    sb.append(" ");
                    int len = this.code[++bp];
                    ++bp;
                    for (int i = 0; i < len; ++i) {
                        int mem = this.code[bp];
                        ++bp;
                        if (i > 0) {
                            sb.append(", ");
                        }
                        sb.append(mem);
                    }
                    break;
                }
                case 60: 
                case 61: {
                    int mem = this.code[bp];
                    int addr = this.code[++bp];
                    ++bp;
                    sb.append(":").append(mem).append(":").append(addr);
                    break;
                }
                case 58: 
                case 59: {
                    int addr = this.code[bp];
                    sb.append(":(").append(addr).append(")");
                    this.pString(sb, 1, ++bp);
                    ++bp;
                    break;
                }
                case 76: {
                    int len = this.code[bp];
                    ++bp;
                    sb.append(":").append(len);
                    break;
                }
                case 77: {
                    int addr = this.code[bp];
                    int len = this.code[++bp];
                    ++bp;
                    sb.append(":").append(len).append(":(").append(addr).append(")");
                    break;
                }
                case 81: 
                case 82: {
                    int scn = this.code[bp];
                    int addr = this.code[++bp];
                    ++bp;
                    sb.append(":").append(scn).append(":(").append(addr).append(")");
                    break;
                }
                default: {
                    throw new InternalException("undefined code: " + this.code[--bp]);
                }
            }
        }
        sb.append("]");
        return bp;
    }

    private String compiledByteCodeListToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code length: ").append(this.codeLength).append("\n");
        int ncode = 0;
        int bp = 0;
        int end = this.codeLength;
        while (bp < end) {
            ++ncode;
            if (bp > 0) {
                sb.append(ncode % 5 == 0 ? "\n" : " ");
            }
            bp = this.compiledByteCodeToString(sb, bp);
        }
        sb.append("\n");
        return sb.toString();
    }
}

