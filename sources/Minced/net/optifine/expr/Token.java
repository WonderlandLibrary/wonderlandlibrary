// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.expr;

public class Token
{
    private TokenType type;
    private String text;
    
    public Token(final TokenType type, final String text) {
        this.type = type;
        this.text = text;
    }
    
    public TokenType getType() {
        return this.type;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public String toString() {
        return this.text;
    }
}
