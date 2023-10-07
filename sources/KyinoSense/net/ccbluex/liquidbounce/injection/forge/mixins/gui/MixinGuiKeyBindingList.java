/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiKeyBindingList
 *  net.minecraft.client.gui.GuiSlot
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={GuiKeyBindingList.class})
public abstract class MixinGuiKeyBindingList
extends GuiSlot {
    public MixinGuiKeyBindingList(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
    }

    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }
}

