/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.gui.ServerSelectionList
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={ServerSelectionList.class})
public abstract class MixinServerSelectionList
extends GuiSlot {
    public MixinServerSelectionList(Minecraft mcIn, int width, int height, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, width, height, topIn, bottomIn, slotHeightIn);
    }

    @Overwrite
    protected int func_148137_d() {
        return this.field_148155_a - 5;
    }
}

