/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.entity.RenderPlayer
 *  net.minecraft.util.ResourceLocation
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import me.report.liquidware.modules.render.CustomModel;
import me.report.liquidware.modules.render.PlayerSize;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={RenderPlayer.class})
public abstract class MixinRenderPlayer {
    private final ResourceLocation rabbit = new ResourceLocation("liquidbounce/models/rabbit.png");
    private final ResourceLocation fred = new ResourceLocation("liquidbounce/models/freddy.png");

    @Inject(method={"renderLivingAt"}, at={@At(value="HEAD")})
    protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z, CallbackInfo callbackInfo) {
        PlayerSize playerSize = (PlayerSize)LiquidBounce.moduleManager.getModule(PlayerSize.class);
        if (LiquidBounce.moduleManager.getModule(PlayerSize.class).getState() && entityLivingBaseIn.equals((Object)Minecraft.func_71410_x().field_71439_g)) {
            GlStateManager.func_179152_a((float)((Float)playerSize.getPlayerSizeValue().get()).floatValue(), (float)((Float)playerSize.getPlayerSizeValue().get()).floatValue(), (float)((Float)playerSize.getPlayerSizeValue().get()).floatValue());
        }
    }

    @Inject(method={"getEntityTexture"}, at={@At(value="HEAD")}, cancellable=true)
    public void getEntityTexture(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> ci) {
        CustomModel customModel = (CustomModel)LiquidBounce.moduleManager.getModule(CustomModel.class);
        if (LiquidBounce.moduleManager.getModule(CustomModel.class).getState() && (!((Boolean)customModel.getOnlySelf().get()).booleanValue() || entity == Minecraft.func_71410_x().field_71439_g)) {
            if (((String)customModel.getMode2().get()).contains("Rabbit")) {
                ci.setReturnValue(this.rabbit);
            }
            if (((String)customModel.getMode2().get()).contains("Freddy")) {
                ci.setReturnValue(this.fred);
            }
            if (((String)customModel.getMode2().get()).contains("No")) {
                // empty if block
            }
        }
    }
}

