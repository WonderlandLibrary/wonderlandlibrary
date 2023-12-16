// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.expr;

public enum TokenType
{
    IDENTIFIER("IDENTIFIER", 0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_:."), 
    NUMBER("NUMBER", 1, "0123456789", "0123456789."), 
    OPERATOR("OPERATOR", 2, "+-*/%!&|<>=", "&|="), 
    COMMA("COMMA", 3, ","), 
    BRACKET_OPEN("BRACKET_OPEN", 4, "("), 
    BRACKET_CLOSE("BRACKET_CLOSE", 5, ")");
    
    private String charsFirst;
    private String charsNext;
    public static final TokenType[] VALUES;
    
    static {
        VALUES = values();
    }
    
    private TokenType(final String s, final int n, final String charsFirst) {
        this(s, n, charsFirst, "");
    }
    
    private TokenType(final String s, final int n, final String charsFirst, final String charsNext) {
        this.charsFirst = charsFirst;
        this.charsNext = charsNext;
    }
    
    public String getCharsFirst() {
        return this.charsFirst;
    }
    
    public String getCharsNext() {
        return this.charsNext;
    }
    
    public static TokenType getTypeByFirstChar(final char ch) {
        for (int i = 0; i < TokenType.VALUES.length; ++i) {
            final TokenType tokentype = TokenType.VALUES[i];
            if (tokentype.getCharsFirst().indexOf(ch) >= 0) {
                return tokentype;
            }
        }
        return null;
    }
    
    public boolean hasCharNext(final char ch) {
        return this.charsNext.indexOf(ch) >= 0;
    }
    
    private static class Const
    {
        static final String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        static final String DIGITS = "0123456789";
    }
}
