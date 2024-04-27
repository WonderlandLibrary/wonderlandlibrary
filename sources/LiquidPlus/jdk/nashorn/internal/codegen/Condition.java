/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

enum Condition {
    EQ,
    NE,
    LE,
    LT,
    GE,
    GT;


    static int toUnary(Condition c) {
        switch (c) {
            case EQ: {
                return 153;
            }
            case NE: {
                return 154;
            }
            case LE: {
                return 158;
            }
            case LT: {
                return 155;
            }
            case GE: {
                return 156;
            }
            case GT: {
                return 157;
            }
        }
        throw new UnsupportedOperationException("toUnary:" + c.toString());
    }

    static int toBinary(Condition c, boolean isObject) {
        switch (c) {
            case EQ: {
                return isObject ? 165 : 159;
            }
            case NE: {
                return isObject ? 166 : 160;
            }
            case LE: {
                return 164;
            }
            case LT: {
                return 161;
            }
            case GE: {
                return 162;
            }
            case GT: {
                return 163;
            }
        }
        throw new UnsupportedOperationException("toBinary:" + c.toString());
    }
}

