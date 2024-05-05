/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.InventoryEffectRenderer
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.injection.forge.mixins.gui.MixinGuiContainer;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={InventoryEffectRenderer.class})
public abstract class MixinInventoryEffectRenderer
extends MixinGuiContainer {
    @Shadow
    private boolean field_147045_u;

    @Overwrite
    public void func_175378_g() {
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        if (((Boolean)hud.invEffectOffset.get()).booleanValue()) {
            this.field_147003_i = (this.field_146294_l - this.field_146999_f) / 2;
            this.field_147045_u = !this.field_146297_k.field_71439_g.func_70651_bq().isEmpty();
        } else if (!this.field_146297_k.field_71439_g.func_70651_bq().isEmpty()) {
            this.field_147003_i = 160 + (this.field_146294_l - this.field_146999_f - 200) / 2;
            this.field_147045_u = true;
        } else {
            this.field_147003_i = (this.field_146294_l - this.field_146999_f) / 2;
            this.field_147045_u = false;
        }
    }
}

