package net.optifine.expr;

import net.optifine.expr.FunctionType;

/*
 * Exception performing whole class analysis ignored.
 */
static class FunctionType.1 {
    static final /* synthetic */ int[] $SwitchMap$net$optifine$expr$FunctionType;

    static {
        $SwitchMap$net$optifine$expr$FunctionType = new int[FunctionType.values().length];
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.PLUS.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.MINUS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.MUL.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.DIV.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.MOD.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.NEG.ordinal()] = 6;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.PI.ordinal()] = 7;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SIN.ordinal()] = 8;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.COS.ordinal()] = 9;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ASIN.ordinal()] = 10;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ACOS.ordinal()] = 11;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.TAN.ordinal()] = 12;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ATAN.ordinal()] = 13;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ATAN2.ordinal()] = 14;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.TORAD.ordinal()] = 15;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.TODEG.ordinal()] = 16;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.MIN.ordinal()] = 17;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.MAX.ordinal()] = 18;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.CLAMP.ordinal()] = 19;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ABS.ordinal()] = 20;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.EXP.ordinal()] = 21;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.FLOOR.ordinal()] = 22;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.CEIL.ordinal()] = 23;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.FRAC.ordinal()] = 24;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.LOG.ordinal()] = 25;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.POW.ordinal()] = 26;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.RANDOM.ordinal()] = 27;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.ROUND.ordinal()] = 28;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SIGNUM.ordinal()] = 29;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SQRT.ordinal()] = 30;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.FMOD.ordinal()] = 31;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.TIME.ordinal()] = 32;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.IF.ordinal()] = 33;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SMOOTH.ordinal()] = 34;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.TRUE.ordinal()] = 35;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.FALSE.ordinal()] = 36;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.NOT.ordinal()] = 37;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.AND.ordinal()] = 38;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.OR.ordinal()] = 39;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.GREATER.ordinal()] = 40;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.GREATER_OR_EQUAL.ordinal()] = 41;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SMALLER.ordinal()] = 42;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.SMALLER_OR_EQUAL.ordinal()] = 43;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.EQUAL.ordinal()] = 44;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.NOT_EQUAL.ordinal()] = 45;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.BETWEEN.ordinal()] = 46;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.EQUALS.ordinal()] = 47;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.IN.ordinal()] = 48;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.VEC2.ordinal()] = 49;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.VEC3.ordinal()] = 50;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            FunctionType.1.$SwitchMap$net$optifine$expr$FunctionType[FunctionType.VEC4.ordinal()] = 51;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
