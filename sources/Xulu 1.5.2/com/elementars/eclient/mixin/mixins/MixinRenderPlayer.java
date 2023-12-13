// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.Redirect;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.render.ViewmodelChanger;
import org.lwjgl.opengl.GL11;
import java.awt.Color;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.module.render.Chams;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.module.render.Nametags;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Shadow
    public ModelPlayer getMainModel() {
        return new ModelPlayer(0.0f, false);
    }
    
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void renderLivingLabel(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo info) {
        if (Nametags.INSTANCE.isToggled()) {
            info.cancel();
        }
    }
    
    @Redirect(method = { "renderRightArm" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    private void renderRightArm(final ModelRenderer modelRenderer, final float scale) {
        final Chams chams = Xulu.MODULE_MANAGER.getModuleT(Chams.class);
        final Color c = chams.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(chams.r.getValue(), chams.g.getValue(), chams.b.getValue());
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPushMatrix();
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
        }
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled() && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue()) {
            final ModelPlayer modelplayer = this.getMainModel();
            modelplayer.bipedRightArm.rotateAngleX = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).x.getValue() * 8.0f;
            modelplayer.bipedRightArm.rotateAngleY = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).y.getValue() * 8.0f;
            modelplayer.bipedRightArm.rotateAngleZ = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).z.getValue() * 8.0f;
            modelplayer.bipedRightArm.render(scale);
        }
        else {
            modelRenderer.render(scale);
        }
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
    
    @Redirect(method = { "renderRightArm" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal = 1))
    private void renderRightArmWear(final ModelRenderer modelRenderer, final float scale) {
        final Chams chams = Xulu.MODULE_MANAGER.getModuleT(Chams.class);
        final Color c = chams.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(chams.r.getValue(), chams.g.getValue(), chams.b.getValue());
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPushMatrix();
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
        }
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled() && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue()) {
            final ModelPlayer modelplayer = this.getMainModel();
            modelplayer.bipedRightArmwear.rotateAngleX = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).x.getValue() * 4.0f;
            modelplayer.bipedRightArmwear.rotateAngleY = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).y.getValue() * 4.0f;
            modelplayer.bipedRightArmwear.rotateAngleZ = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).z.getValue() * 4.0f;
            modelplayer.bipedRightArmwear.render(scale);
        }
        else {
            modelRenderer.render(scale);
        }
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
    
    @Redirect(method = { "renderLeftArm" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V"))
    private void renderLeftArm(final ModelRenderer modelRenderer, final float scale) {
        final Chams chams = Xulu.MODULE_MANAGER.getModuleT(Chams.class);
        final Color c = chams.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(chams.r.getValue(), chams.g.getValue(), chams.b.getValue());
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPushMatrix();
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
        }
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled() && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue()) {
            final ModelPlayer modelplayer = this.getMainModel();
            modelplayer.bipedLeftArm.rotateAngleX = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).x.getValue() * 8.0f;
            modelplayer.bipedLeftArm.rotateAngleY = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).y.getValue() * 8.0f;
            modelplayer.bipedLeftArm.rotateAngleZ = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).z.getValue() * 8.0f;
            modelplayer.bipedLeftArm.render(scale);
        }
        else {
            modelRenderer.render(scale);
        }
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
    
    @Redirect(method = { "renderLeftArm" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelRenderer;render(F)V", ordinal = 1))
    private void renderLeftArmWear(final ModelRenderer modelRenderer, final float scale) {
        final Chams chams = Xulu.MODULE_MANAGER.getModuleT(Chams.class);
        final Color c = chams.rainbow.getValue() ? new Color(Xulu.rgb) : new Color(chams.r.getValue(), chams.g.getValue(), chams.b.getValue());
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPushMatrix();
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
        }
        if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled() && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue()) {
            final ModelPlayer modelplayer = this.getMainModel();
            modelplayer.bipedLeftArmwear.rotateAngleX = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).x.getValue() * 8.0f;
            modelplayer.bipedLeftArmwear.rotateAngleY = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).y.getValue() * 8.0f;
            modelplayer.bipedLeftArmwear.rotateAngleZ = Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).z.getValue() * 8.0f;
            modelplayer.bipedLeftArmwear.render(scale);
        }
        else {
            modelRenderer.render(scale);
        }
        if (chams.hand.getValue() && chams.isToggled() && Chams.mode.getValue().equalsIgnoreCase("ESP")) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 100000.0f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }
}
