package net.optifine.shaders.config;

import net.minecraft.util.Util;

static class ShaderMacros.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$util$Util$EnumOS;

    static {
        $SwitchMap$net$minecraft$util$Util$EnumOS = new int[Util.EnumOS.values().length];
        try {
            ShaderMacros.1.$SwitchMap$net$minecraft$util$Util$EnumOS[Util.EnumOS.WINDOWS.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ShaderMacros.1.$SwitchMap$net$minecraft$util$Util$EnumOS[Util.EnumOS.OSX.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ShaderMacros.1.$SwitchMap$net$minecraft$util$Util$EnumOS[Util.EnumOS.LINUX.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
