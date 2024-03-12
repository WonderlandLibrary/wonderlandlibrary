// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

public enum TokenType
{
    IDENTIFIER("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_:."), 
    NUMBER("0123456789", "0123456789."), 
    OPERATOR("+-*/%!&|<>=", "&|="), 
    COMMA(","), 
    BRACKET_OPEN("("), 
    BRACKET_CLOSE(")");
    
    private String charsFirst;
    private String charsNext;
    public static final TokenType[] VALUES;
    
    private TokenType(final String charsFirst) {
        this(charsFirst, "");
    }
    
    private TokenType(final String charsFirst, final String charsNext) {
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
    
    static {
        VALUES = values();
    }
    
    private static class Const
    {
        static final String ALPHAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        static final String DIGITS = "0123456789";
    }
}
