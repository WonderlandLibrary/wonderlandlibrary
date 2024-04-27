/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.gui;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.client.button.AbstractButtonRenderer;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@SideOnly(value=Side.CLIENT)
@Mixin(value={GuiButton.class})
public abstract class MixinGuiButton
extends Gui {
    @Shadow
    @Final
    protected static ResourceLocation field_146122_a;
    @Shadow
    public boolean field_146125_m;
    @Shadow
    public int field_146128_h;
    @Shadow
    public int field_146129_i;
    @Shadow
    public int field_146120_f;
    @Shadow
    public int field_146121_g;
    @Shadow
    public boolean field_146124_l;
    @Shadow
    public String field_146126_j;
    @Shadow
    protected boolean field_146123_n;
    private float cut;
    private float alpha;
    HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
    protected final AbstractButtonRenderer buttonRenderer = this.hud.getButtonRenderer((GuiButton)this);

    @Shadow
    protected abstract void func_146119_b(Minecraft var1, int var2, int var3);

    @Overwrite
    public void func_191745_a(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.buttonRenderer != null) {
            if (!this.field_146125_m) {
                return;
            }
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            this.field_146123_n = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
            this.func_146119_b(mc, mouseX, mouseY);
            this.buttonRenderer.render(mouseX, mouseY, mc);
            this.buttonRenderer.drawButtonText(mc);
        }
    }
}

