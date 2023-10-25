package net.optifine.expr;

import net.optifine.expr.TokenType;

static class ExpressionParser.1 {
    static final /* synthetic */ int[] $SwitchMap$net$optifine$expr$TokenType;

    static {
        $SwitchMap$net$optifine$expr$TokenType = new int[TokenType.values().length];
        try {
            ExpressionParser.1.$SwitchMap$net$optifine$expr$TokenType[TokenType.NUMBER.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ExpressionParser.1.$SwitchMap$net$optifine$expr$TokenType[TokenType.IDENTIFIER.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ExpressionParser.1.$SwitchMap$net$optifine$expr$TokenType[TokenType.BRACKET_OPEN.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ExpressionParser.1.$SwitchMap$net$optifine$expr$TokenType[TokenType.OPERATOR.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
