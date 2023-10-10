/*
 * Decompiled with CFR 0.152.
 */
package javassist.compiler;

import javassist.compiler.KeywordTable;
import javassist.compiler.Token;
import javassist.compiler.TokenId;

public class Lex
implements TokenId {
    private int lastChar = -1;
    private StringBuffer textBuffer = new StringBuffer();
    private Token currentToken = new Token();
    private Token lookAheadTokens = null;
    private String input;
    private int position;
    private int maxlen;
    private int lineNumber;
    private static final int[] equalOps = new int[]{350, 0, 0, 0, 351, 352, 0, 0, 0, 353, 354, 0, 355, 0, 356, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 357, 358, 359, 0};
    private static final KeywordTable ktable = new KeywordTable();

    public Lex(String s2) {
        this.input = s2;
        this.position = 0;
        this.maxlen = s2.length();
        this.lineNumber = 0;
    }

    public int get() {
        Token t2;
        if (this.lookAheadTokens == null) {
            return this.get(this.currentToken);
        }
        this.currentToken = t2 = this.lookAheadTokens;
        this.lookAheadTokens = this.lookAheadTokens.next;
        return t2.tokenId;
    }

    public int lookAhead() {
        return this.lookAhead(0);
    }

    public int lookAhead(int i2) {
        Token tk = this.lookAheadTokens;
        if (tk == null) {
            this.lookAheadTokens = tk = this.currentToken;
            tk.next = null;
            this.get(tk);
        }
        while (i2-- > 0) {
            if (tk.next == null) {
                Token tk2;
                tk.next = tk2 = new Token();
                this.get(tk2);
            }
            tk = tk.next;
        }
        this.currentToken = tk;
        return tk.tokenId;
    }

    public String getString() {
        return this.currentToken.textValue;
    }

    public long getLong() {
        return this.currentToken.longValue;
    }

    public double getDouble() {
        return this.currentToken.doubleValue;
    }

    private int get(Token token) {
        int t2;
        while ((t2 = this.readLine(token)) == 10) {
        }
        token.tokenId = t2;
        return t2;
    }

    private int readLine(Token token) {
        int c2 = this.getNextNonWhiteChar();
        if (c2 < 0) {
            return c2;
        }
        if (c2 == 10) {
            ++this.lineNumber;
            return 10;
        }
        if (c2 == 39) {
            return this.readCharConst(token);
        }
        if (c2 == 34) {
            return this.readStringL(token);
        }
        if (48 <= c2 && c2 <= 57) {
            return this.readNumber(c2, token);
        }
        if (c2 == 46) {
            c2 = this.getc();
            if (48 <= c2 && c2 <= 57) {
                StringBuffer tbuf = this.textBuffer;
                tbuf.setLength(0);
                tbuf.append('.');
                return this.readDouble(tbuf, c2, token);
            }
            this.ungetc(c2);
            return this.readSeparator(46);
        }
        if (Character.isJavaIdentifierStart((char)c2)) {
            return this.readIdentifier(c2, token);
        }
        return this.readSeparator(c2);
    }

    private int getNextNonWhiteChar() {
        int c2;
        block0: do {
            if ((c2 = this.getc()) != 47) continue;
            c2 = this.getc();
            if (c2 == 47) {
                while ((c2 = this.getc()) != 10 && c2 != 13 && c2 != -1) {
                }
                continue;
            }
            if (c2 == 42) {
                while ((c2 = this.getc()) != -1) {
                    if (c2 != 42) continue;
                    c2 = this.getc();
                    if (c2 == 47) {
                        c2 = 32;
                        continue block0;
                    }
                    this.ungetc(c2);
                }
            } else {
                this.ungetc(c2);
                c2 = 47;
            }
        } while (Lex.isBlank(c2));
        return c2;
    }

    private int readCharConst(Token token) {
        int c2;
        int value = 0;
        while ((c2 = this.getc()) != 39) {
            if (c2 == 92) {
                value = this.readEscapeChar();
                continue;
            }
            if (c2 < 32) {
                if (c2 == 10) {
                    ++this.lineNumber;
                }
                return 500;
            }
            value = c2;
        }
        token.longValue = value;
        return 401;
    }

    private int readEscapeChar() {
        int c2 = this.getc();
        if (c2 == 110) {
            c2 = 10;
        } else if (c2 == 116) {
            c2 = 9;
        } else if (c2 == 114) {
            c2 = 13;
        } else if (c2 == 102) {
            c2 = 12;
        } else if (c2 == 10) {
            ++this.lineNumber;
        }
        return c2;
    }

    private int readStringL(Token token) {
        int c2;
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        while (true) {
            if ((c2 = this.getc()) != 34) {
                if (c2 == 92) {
                    c2 = this.readEscapeChar();
                } else if (c2 == 10 || c2 < 0) {
                    ++this.lineNumber;
                    return 500;
                }
                tbuf.append((char)c2);
                continue;
            }
            while (true) {
                if ((c2 = this.getc()) == 10) {
                    ++this.lineNumber;
                    continue;
                }
                if (!Lex.isBlank(c2)) break;
            }
            if (c2 != 34) break;
        }
        this.ungetc(c2);
        token.textValue = tbuf.toString();
        return 406;
    }

    private int readNumber(int c2, Token token) {
        long value = 0L;
        int c22 = this.getc();
        if (c2 == 48) {
            if (c22 == 88 || c22 == 120) {
                while (true) {
                    if (48 <= (c2 = this.getc()) && c2 <= 57) {
                        value = value * 16L + (long)(c2 - 48);
                        continue;
                    }
                    if (65 <= c2 && c2 <= 70) {
                        value = value * 16L + (long)(c2 - 65 + 10);
                        continue;
                    }
                    if (97 > c2 || c2 > 102) break;
                    value = value * 16L + (long)(c2 - 97 + 10);
                }
                token.longValue = value;
                if (c2 == 76 || c2 == 108) {
                    return 403;
                }
                this.ungetc(c2);
                return 402;
            }
            if (48 <= c22 && c22 <= 55) {
                value = c22 - 48;
                while (48 <= (c2 = this.getc()) && c2 <= 55) {
                    value = value * 8L + (long)(c2 - 48);
                }
                token.longValue = value;
                if (c2 == 76 || c2 == 108) {
                    return 403;
                }
                this.ungetc(c2);
                return 402;
            }
        }
        value = c2 - 48;
        while (48 <= c22 && c22 <= 57) {
            value = value * 10L + (long)c22 - 48L;
            c22 = this.getc();
        }
        token.longValue = value;
        if (c22 == 70 || c22 == 102) {
            token.doubleValue = value;
            return 404;
        }
        if (c22 == 69 || c22 == 101 || c22 == 68 || c22 == 100 || c22 == 46) {
            StringBuffer tbuf = this.textBuffer;
            tbuf.setLength(0);
            tbuf.append(value);
            return this.readDouble(tbuf, c22, token);
        }
        if (c22 == 76 || c22 == 108) {
            return 403;
        }
        this.ungetc(c22);
        return 402;
    }

    private int readDouble(StringBuffer sbuf, int c2, Token token) {
        if (c2 != 69 && c2 != 101 && c2 != 68 && c2 != 100) {
            sbuf.append((char)c2);
            while (48 <= (c2 = this.getc()) && c2 <= 57) {
                sbuf.append((char)c2);
            }
        }
        if (c2 == 69 || c2 == 101) {
            sbuf.append((char)c2);
            c2 = this.getc();
            if (c2 == 43 || c2 == 45) {
                sbuf.append((char)c2);
                c2 = this.getc();
            }
            while (48 <= c2 && c2 <= 57) {
                sbuf.append((char)c2);
                c2 = this.getc();
            }
        }
        try {
            token.doubleValue = Double.parseDouble(sbuf.toString());
        }
        catch (NumberFormatException e2) {
            return 500;
        }
        if (c2 == 70 || c2 == 102) {
            return 404;
        }
        if (c2 != 68 && c2 != 100) {
            this.ungetc(c2);
        }
        return 405;
    }

    private int readSeparator(int c2) {
        int c22;
        if (33 <= c2 && c2 <= 63) {
            int t2 = equalOps[c2 - 33];
            if (t2 == 0) {
                return c2;
            }
            c22 = this.getc();
            if (c2 == c22) {
                switch (c2) {
                    case 61: {
                        return 358;
                    }
                    case 43: {
                        return 362;
                    }
                    case 45: {
                        return 363;
                    }
                    case 38: {
                        return 369;
                    }
                    case 60: {
                        int c3 = this.getc();
                        if (c3 == 61) {
                            return 365;
                        }
                        this.ungetc(c3);
                        return 364;
                    }
                    case 62: {
                        int c3 = this.getc();
                        if (c3 == 61) {
                            return 367;
                        }
                        if (c3 == 62) {
                            c3 = this.getc();
                            if (c3 == 61) {
                                return 371;
                            }
                            this.ungetc(c3);
                            return 370;
                        }
                        this.ungetc(c3);
                        return 366;
                    }
                }
            } else if (c22 == 61) {
                return t2;
            }
        } else if (c2 == 94) {
            c22 = this.getc();
            if (c22 == 61) {
                return 360;
            }
        } else if (c2 == 124) {
            c22 = this.getc();
            if (c22 == 61) {
                return 361;
            }
            if (c22 == 124) {
                return 368;
            }
        } else {
            return c2;
        }
        this.ungetc(c22);
        return c2;
    }

    private int readIdentifier(int c2, Token token) {
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        do {
            tbuf.append((char)c2);
        } while (Character.isJavaIdentifierPart((char)(c2 = this.getc())));
        this.ungetc(c2);
        String name = tbuf.toString();
        int t2 = ktable.lookup(name);
        if (t2 >= 0) {
            return t2;
        }
        token.textValue = name;
        return 400;
    }

    private static boolean isBlank(int c2) {
        return c2 == 32 || c2 == 9 || c2 == 12 || c2 == 13 || c2 == 10;
    }

    private static boolean isDigit(int c2) {
        return 48 <= c2 && c2 <= 57;
    }

    private void ungetc(int c2) {
        this.lastChar = c2;
    }

    public String getTextAround() {
        int end;
        int begin = this.position - 10;
        if (begin < 0) {
            begin = 0;
        }
        if ((end = this.position + 10) > this.maxlen) {
            end = this.maxlen;
        }
        return this.input.substring(begin, end);
    }

    private int getc() {
        if (this.lastChar < 0) {
            if (this.position < this.maxlen) {
                return this.input.charAt(this.position++);
            }
            return -1;
        }
        int c2 = this.lastChar;
        this.lastChar = -1;
        return c2;
    }

    static {
        ktable.append("abstract", 300);
        ktable.append("boolean", 301);
        ktable.append("break", 302);
        ktable.append("byte", 303);
        ktable.append("case", 304);
        ktable.append("catch", 305);
        ktable.append("char", 306);
        ktable.append("class", 307);
        ktable.append("const", 308);
        ktable.append("continue", 309);
        ktable.append("default", 310);
        ktable.append("do", 311);
        ktable.append("double", 312);
        ktable.append("else", 313);
        ktable.append("extends", 314);
        ktable.append("false", 411);
        ktable.append("final", 315);
        ktable.append("finally", 316);
        ktable.append("float", 317);
        ktable.append("for", 318);
        ktable.append("goto", 319);
        ktable.append("if", 320);
        ktable.append("implements", 321);
        ktable.append("import", 322);
        ktable.append("instanceof", 323);
        ktable.append("int", 324);
        ktable.append("interface", 325);
        ktable.append("long", 326);
        ktable.append("native", 327);
        ktable.append("new", 328);
        ktable.append("null", 412);
        ktable.append("package", 329);
        ktable.append("private", 330);
        ktable.append("protected", 331);
        ktable.append("public", 332);
        ktable.append("return", 333);
        ktable.append("short", 334);
        ktable.append("static", 335);
        ktable.append("strictfp", 347);
        ktable.append("super", 336);
        ktable.append("switch", 337);
        ktable.append("synchronized", 338);
        ktable.append("this", 339);
        ktable.append("throw", 340);
        ktable.append("throws", 341);
        ktable.append("transient", 342);
        ktable.append("true", 410);
        ktable.append("try", 343);
        ktable.append("void", 344);
        ktable.append("volatile", 345);
        ktable.append("while", 346);
    }
}

