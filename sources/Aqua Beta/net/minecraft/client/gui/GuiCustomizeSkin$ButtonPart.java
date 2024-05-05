package net.minecraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiCustomizeSkin;
import net.minecraft.entity.player.EnumPlayerModelParts;

/*
 * Exception performing whole class analysis ignored.
 */
class GuiCustomizeSkin.ButtonPart
extends GuiButton {
    private final EnumPlayerModelParts playerModelParts;

    private GuiCustomizeSkin.ButtonPart(int p_i45514_2_, int p_i45514_3_, int p_i45514_4_, int p_i45514_5_, int p_i45514_6_, EnumPlayerModelParts playerModelParts) {
        super(p_i45514_2_, p_i45514_3_, p_i45514_4_, p_i45514_5_, p_i45514_6_, GuiCustomizeSkin.access$200((GuiCustomizeSkin)GuiCustomizeSkin.this, (EnumPlayerModelParts)playerModelParts));
        this.playerModelParts = playerModelParts;
    }

    static /* synthetic */ EnumPlayerModelParts access$100(GuiCustomizeSkin.ButtonPart x0) {
        return x0.playerModelParts;
    }
}
