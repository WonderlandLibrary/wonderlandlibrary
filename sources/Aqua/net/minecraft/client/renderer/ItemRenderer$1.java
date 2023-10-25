package net.minecraft.client.renderer;

import net.minecraft.item.EnumAction;

static class ItemRenderer.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$item$EnumAction;

    static {
        $SwitchMap$net$minecraft$item$EnumAction = new int[EnumAction.values().length];
        try {
            ItemRenderer.1.$SwitchMap$net$minecraft$item$EnumAction[EnumAction.NONE.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ItemRenderer.1.$SwitchMap$net$minecraft$item$EnumAction[EnumAction.EAT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ItemRenderer.1.$SwitchMap$net$minecraft$item$EnumAction[EnumAction.DRINK.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ItemRenderer.1.$SwitchMap$net$minecraft$item$EnumAction[EnumAction.BLOCK.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            ItemRenderer.1.$SwitchMap$net$minecraft$item$EnumAction[EnumAction.BOW.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
