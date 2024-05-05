/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  net.minecraft.inventory.ClickType
 *  net.minecraft.inventory.Slot
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={GuiContainer.class})
public abstract class MixinGuiContainer
implements IMixinGuiContainer {
    @Shadow
    protected abstract void func_184098_a(Slot var1, int var2, int var3, ClickType var4);

    @Override
    public void publicHandleMouseClick(Slot slot, int slotNumber, int clickedButton, ClickType clickType) {
        this.func_184098_a(slot, slotNumber, clickedButton, clickType);
    }
}

