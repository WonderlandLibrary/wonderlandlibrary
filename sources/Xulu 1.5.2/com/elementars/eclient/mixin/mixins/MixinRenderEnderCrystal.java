// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Inject;
import dev.xulu.newgui.util.ColorUtil;
import com.elementars.eclient.util.OutlineUtils;
import net.minecraft.util.math.MathHelper;
import com.elementars.eclient.util.Wrapper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.render.OutlineESP;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.Chams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.spongepowered.asm.mixin.Final;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEnderCrystal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderEnderCrystal.class })
public abstract class MixinRenderEnderCrystal
{
    @Shadow
    public ModelBase modelEnderCrystal;
    @Shadow
    public ModelBase modelEnderCrystalNoBase;
    @Final
    @Shadow
    private static ResourceLocation ENDER_CRYSTAL_TEXTURES;
    
    @Shadow
    public abstract void doRender(final EntityEnderCrystal p0, final double p1, final double p2, final double p3, final float p4, final float p5);
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void render1(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (Xulu.MODULE_MANAGER.getModuleT(Chams.class).isToggled() && Chams.crystals.getValue() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            return;
        }
        if (!Xulu.MODULE_MANAGER.getModuleT(OutlineESP.class).isToggled() || Xulu.MODULE_MANAGER.getModuleT(OutlineESP.class).renderCrystals.getValue()) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Redirect(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V", ordinal = 1))
    private void render2(final ModelBase modelBase, final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (Xulu.MODULE_MANAGER.getModuleT(Chams.class).isToggled() && Chams.crystals.getValue() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            return;
        }
        if (!Xulu.MODULE_MANAGER.getModuleT(OutlineESP.class).isToggled() || Xulu.MODULE_MANAGER.getModuleT(OutlineESP.class).renderCrystals.getValue()) {
            modelBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    @Inject(method = { "doRender(Lnet/minecraft/entity/item/EntityEnderCrystal;DDDFF)V" }, at = { @At("RETURN") }, cancellable = true)
    public void IdoRender(final EntityEnderCrystal entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo callback) {
        final OutlineESP outlineESP = Xulu.MODULE_MANAGER.getModuleT(OutlineESP.class);
        final Chams chams = Xulu.MODULE_MANAGER.getModuleT(Chams.class);
        if (outlineESP == null || chams == null) {
            return;
        }
        if (chams.isToggled() && Chams.crystals.getValue() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            final Color c = chams.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(chams.r.getValue(), chams.g.getValue(), chams.b.getValue());
            GL11.glPushMatrix();
            final float f = entity.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float f2 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
            f2 += f2 * f2;
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!chams.lines.getValue()) {
                GL11.glPolygonMode(1028, 6914);
            }
            else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, chams.a.getValue() / 255.0f);
            if (chams.lines.getValue()) {
                GL11.glLineWidth((float)chams.width.getValue());
            }
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
        else if (chams.isToggled() && Chams.crystals.getValue() && Chams.mode.getValue().equalsIgnoreCase("Walls")) {
            GL11.glPushMatrix();
            final float f3 = entity.innerRotation + partialTicks;
            GlStateManager.translate(x, y, z);
            Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
            float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
            f4 += f4 * f4;
            GL11.glDisable(2929);
            GL11.glColor4f(chams.Wr.getValue() / 255.0f, chams.Wg.getValue() / 255.0f, chams.Wb.getValue() / 255.0f, 1.0f);
            GL11.glDisable(3553);
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GL11.glEnable(2929);
            GL11.glColor4f(chams.Vr.getValue() / 255.0f, chams.Vg.getValue() / 255.0f, chams.Vb.getValue() / 255.0f, 1.0f);
            if (entity.shouldShowBottom()) {
                this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            else {
                this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
            }
            GL11.glEnable(3553);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
        if (outlineESP.mode.getValue().equalsIgnoreCase("Shader") && outlineESP.crystals.getValue() && outlineESP.isToggled()) {
            entity.setGlowing(true);
        }
        else {
            entity.setGlowing(false);
            if (outlineESP.crystals.getValue() && outlineESP.isToggled()) {
                if (outlineESP.mode.getValue().equalsIgnoreCase("Outline")) {
                    final float f3 = entity.innerRotation + partialTicks;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, z);
                    Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                    float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
                    f4 += f4 * f4;
                    GL11.glLineWidth(5.0f);
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    else {
                        this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    OutlineUtils.renderOne(outlineESP.width.getValue());
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    else {
                        this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    OutlineUtils.renderTwo();
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    else {
                        this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    OutlineUtils.renderThree();
                    OutlineUtils.renderFour(outlineESP.color.getValue().equalsIgnoreCase("Rainbow") ? new Color(Xulu.rgb) : ColorUtil.getClickGUIColor());
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    else {
                        this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    OutlineUtils.renderFive();
                    GlStateManager.popMatrix();
                }
                else {
                    final float f3 = entity.innerRotation + partialTicks;
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x, y, z);
                    Wrapper.getMinecraft().renderManager.renderEngine.bindTexture(MixinRenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
                    float f4 = MathHelper.sin(f3 * 0.2f) / 2.0f + 0.5f;
                    f4 += f4 * f4;
                    GL11.glPushAttrib(1048575);
                    GL11.glPolygonMode(1032, 6913);
                    GL11.glDisable(3553);
                    GL11.glDisable(2896);
                    GL11.glDisable(2929);
                    GL11.glEnable(2848);
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    OutlineUtils.setColor(ColorUtil.getClickGUIColor());
                    GL11.glLineWidth((float)outlineESP.width.getValue());
                    if (entity.shouldShowBottom()) {
                        this.modelEnderCrystal.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    else {
                        this.modelEnderCrystalNoBase.render((Entity)entity, 0.0f, f3 * 3.0f, f4 * 0.2f, 0.0f, 0.0f, 0.0625f);
                    }
                    GL11.glPopAttrib();
                    GL11.glPopMatrix();
                }
            }
        }
    }
}
