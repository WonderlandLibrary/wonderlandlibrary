package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.blatant.Aura;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.module.modules.client.Rotations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
@SideOnly(Side.CLIENT)

public abstract class MixinLayerArmorBase <T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow
    public abstract ItemStack getCurrentArmor(EntityLivingBase p_getCurrentArmor_1_, int p_getCurrentArmor_2_);

    @Shadow
    public abstract T getArmorModel(int p_getArmorModel_1_);

    @Final
    @Shadow
    private RendererLivingEntity<?> renderer;

    @Shadow
    protected abstract void renderGlint(EntityLivingBase entitylivingbase, T model, float p_177183_3_, float p_177183_4_, float partialTicks, float p_177183_6_, float p_177183_7_, float p_177183_8_, float scale);

    @Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.color(FF FF)V", shift = At.Shift.AFTER, ordinal = 1), cancellable = true)
    private void renderLayer(EntityLivingBase entitylivingbase, float p_renderLayer_2_, float p_renderLayer_3_, float partialTicks, float p_renderLayer_5_, float p_renderLayer_6_, float p_renderLayer_7_, float scale, int armorSlot, CallbackInfo callbackInfo) {

        final RenderChanger renderChanger = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);
        final Rotations rotations = (Rotations) LiquidSense.moduleManager.getModule(Rotations.class);
        final Aura aura = (Aura) LiquidSense.moduleManager.getModule(Aura.class);

        ItemStack itemstack = this.getCurrentArmor(entitylivingbase, armorSlot);
        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            T t = this.getArmorModel(armorSlot);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(entitylivingbase, p_renderLayer_2_, p_renderLayer_3_, partialTicks);

            if (entitylivingbase.equals(Minecraft.getMinecraft().thePlayer)) {
                float alpha = (aura.getTarget() != null && rotations.getState() && Rotations.ghost.get()) ? 15.9375f : RenderChanger.armorAlpha.get();

                if (renderChanger.getState()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, !renderChanger.getState() ? 1f : (alpha / 255f));
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.alphaFunc(516, 0.003921569F);
                }

                //BackgroundShader.BACKGROUND_SHADER.startShader();
                t.render(entitylivingbase, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, scale);
                if (itemstack.hasEffect()) {
                    this.renderGlint(entitylivingbase, t, p_renderLayer_2_, p_renderLayer_3_, partialTicks, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, scale);
                }
                //BackgroundShader.BACKGROUND_SHADER.stopShader();

                if (renderChanger.getState()) {
                    GlStateManager.disableBlend();
                    GlStateManager.alphaFunc(516, 0.1F);
                    GlStateManager.popMatrix();
                }
            } else {
                if (entitylivingbase.hurtTime > 0)
                    GlStateManager.color(1.0f, 0.75f, 0.75f, 1f);

                t.render(entitylivingbase, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, scale);
                if (itemstack.hasEffect()) {
                    this.renderGlint(entitylivingbase, t, p_renderLayer_2_, p_renderLayer_3_, partialTicks, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, scale);
                }
            }

            callbackInfo.cancel();
        }

    }

    @Redirect(method = "renderGlint", at = @At(value = "INVOKE", target = "L net/minecraft/client/renderer/GlStateManager;color(FF FF)V", ordinal = 1))
    private void renderGlintColor(float colorRed, float colorGreen, float colorBlue, float colorAlpha) {
        if (LiquidSense.moduleManager.getModule(RenderChanger.class).getState()) {
            GlStateManager.color(RenderChanger.armorRed.get() / 255f, RenderChanger.armorGreen.get() / 255f, RenderChanger.armorBlue.get() / 255f, 0.66666667F);
        } else {
            float f2 = 0.76F;
            GlStateManager.color(0.5F * f2, 0.25F * f2, 0.8F * f2, 1.0F);
        }
    }
}
