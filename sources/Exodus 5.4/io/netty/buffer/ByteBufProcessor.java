/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

public interface ByteBufProcessor {
    public static final ByteBufProcessor FIND_NON_LF;
    public static final ByteBufProcessor FIND_LINEAR_WHITESPACE;
    public static final ByteBufProcessor FIND_CR;
    public static final ByteBufProcessor FIND_NON_NUL;
    public static final ByteBufProcessor FIND_NON_CRLF;
    public static final ByteBufProcessor FIND_NON_CR;
    public static final ByteBufProcessor FIND_CRLF;
    public static final ByteBufProcessor FIND_NUL;
    public static final ByteBufProcessor FIND_NON_LINEAR_WHITESPACE;
    public static final ByteBufProcessor FIND_LF;

    public boolean process(byte var1) throws Exception;

    static {
        FIND_NUL = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by != 0;
            }
        };
        FIND_NON_NUL = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by == 0;
            }
        };
        FIND_CR = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by != 13;
            }
        };
        FIND_NON_CR = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by == 13;
            }
        };
        FIND_LF = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by != 10;
            }
        };
        FIND_NON_LF = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by == 10;
            }
        };
        FIND_CRLF = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by != 13 && by != 10;
            }
        };
        FIND_NON_CRLF = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by == 13 || by == 10;
            }
        };
        FIND_LINEAR_WHITESPACE = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by != 32 && by != 9;
            }
        };
        FIND_NON_LINEAR_WHITESPACE = new ByteBufProcessor(){

            @Override
            public boolean process(byte by) throws Exception {
                return by == 32 || by == 9;
            }
        };
    }
}

