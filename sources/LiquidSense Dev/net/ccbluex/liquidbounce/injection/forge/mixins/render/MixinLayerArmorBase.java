package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LayerArmorBase.class)
@SideOnly(Side.CLIENT)
public abstract class MixinLayerArmorBase <T extends ModelBase> implements LayerRenderer<EntityLivingBase> {

    @Shadow
    public abstract ItemStack getCurrentArmor(EntityLivingBase p_getCurrentArmor_1_, int p_getCurrentArmor_2_);

    @Shadow
    public abstract T getArmorModel(int p_getArmorModel_1_);

    @Shadow
    private RendererLivingEntity<?> renderer;

    @Shadow
    protected abstract T getArmorModelHook(EntityLivingBase p_getArmorModelHook_1_, ItemStack p_getArmorModelHook_2_, int p_getArmorModelHook_3_, T p_getArmorModelHook_4_);

    @Shadow
    protected abstract void setModelPartVisible(T var1, int var2);

    @Shadow
    public abstract boolean isSlotForLeggings(int p_isSlotForLeggings_1_);

    @Shadow
    public abstract ResourceLocation getArmorResource(Entity p_getArmorResource_1_, ItemStack p_getArmorResource_2_, int p_getArmorResource_3_, String p_getArmorResource_4_);

    @Shadow
    private float alpha = 1.0F;

    @Shadow
    private float colorR = 1.0F;

    @Shadow
    private float colorG = 1.0F;

    @Shadow
    private float colorB = 1.0F;

    @Shadow
    private boolean skipRenderGlint;

    @Shadow
    public abstract void renderGlint(EntityLivingBase p_renderGlint_1_, T p_renderGlint_2_, float p_renderGlint_3_, float p_renderGlint_4_, float p_renderGlint_5_, float p_renderGlint_6_, float p_renderGlint_7_, float p_renderGlint_8_, float p_renderGlint_9_);

    @Overwrite
    private void renderLayer(EntityLivingBase p_renderLayer_1_, float p_renderLayer_2_, float p_renderLayer_3_, float p_renderLayer_4_, float p_renderLayer_5_, float p_renderLayer_6_, float p_renderLayer_7_, float p_renderLayer_8_, int p_renderLayer_9_) {
        Rotations ra = (Rotations) LiquidBounce.moduleManager.getModule(Rotations.class);
        Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
        ItemStack itemstack = this.getCurrentArmor(p_renderLayer_1_, p_renderLayer_9_);
        if (itemstack != null && itemstack.getItem() instanceof ItemArmor) {
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            T t = this.getArmorModel(p_renderLayer_9_);
            t.setModelAttributes(this.renderer.getMainModel());
            t.setLivingAnimations(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_4_);
            t = this.getArmorModelHook(p_renderLayer_1_, itemstack, p_renderLayer_9_, t);
            this.setModelPartVisible(t, p_renderLayer_9_);
            boolean flag = this.isSlotForLeggings(p_renderLayer_9_);
            this.renderer.bindTexture(this.getArmorResource(p_renderLayer_1_, itemstack, flag ? 2 : 1, (String)null));
            int i = itemarmor.getColor(itemstack);
            if (i != -1) {
                float f = (float)(i >> 16 & 255) / 255.0F;
                float f1 = (float)(i >> 8 & 255) / 255.0F;
                float f2 = (float)(i & 255) / 255.0F;
                GlStateManager.color(this.colorR * f, this.colorG * f1, this.colorB * f2, this.alpha);
                if(ra.getState() && ra.getModeValue().get().equalsIgnoreCase("Ghost") && p_renderLayer_1_.equals(Minecraft.getMinecraft().thePlayer)){
                    if(killAura.getTarget() !=null && RotationUtils.serverRotation != null){
                        GlStateManager.pushMatrix();
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.35F);
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(770, 771);
                        GlStateManager.alphaFunc(516, 0.003921569F);
                        t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
                        GlStateManager.disableBlend();
                        GlStateManager.alphaFunc(516, 0.1F);
                        GlStateManager.popMatrix();
                        GlStateManager.depthMask(true);
                    } else {
                        t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
                    }
                } else {
                    t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
                }
                this.renderer.bindTexture(this.getArmorResource(p_renderLayer_1_, itemstack, flag ? 2 : 1, "overlay"));
            }

            GlStateManager.color(this.colorR, this.colorG, this.colorB, this.alpha);

            if(ra.getState() && ra.getModeValue().get().equalsIgnoreCase("Ghost") && p_renderLayer_1_.equals(Minecraft.getMinecraft().thePlayer)){
                if(killAura.getTarget() !=null && RotationUtils.serverRotation != null){
                    GlStateManager.pushMatrix();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 0.35F);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    GlStateManager.alphaFunc(516, 0.003921569F);
                    t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
                    GlStateManager.disableBlend();
                    GlStateManager.alphaFunc(516, 0.1F);
                    GlStateManager.popMatrix();
                    GlStateManager.depthMask(true);
                } else {
                    t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
                }
            } else {
                t.render(p_renderLayer_1_, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
            }

            if (!this.skipRenderGlint && itemstack.hasEffect()) {
                this.renderGlint(p_renderLayer_1_, t, p_renderLayer_2_, p_renderLayer_3_, p_renderLayer_4_, p_renderLayer_5_, p_renderLayer_6_, p_renderLayer_7_, p_renderLayer_8_);
            }
        }

    }

}
