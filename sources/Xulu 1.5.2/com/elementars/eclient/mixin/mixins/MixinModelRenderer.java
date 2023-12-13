// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.client.renderer.GlStateManager;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.module.render.ViewmodelChanger;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.model.ModelRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ ModelRenderer.class })
public class MixinModelRenderer
{
    @Inject(method = { "render" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;callList(I)V", shift = At.Shift.BEFORE) })
    private void test(final float scale, final CallbackInfo ci) {
        if (ModelRenderer.class.cast(this) == Wrapper.getMinecraft().renderManager.playerRenderer.getMainModel().bipedRightArm) {
            if (Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue() && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled()) {
                GlStateManager.scale((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizex.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizey.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizez.getValue());
                GlStateManager.translate((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posX.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posY.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posZ.getValue());
            }
        }
        else if (ModelRenderer.class.cast(this) == Wrapper.getMinecraft().renderManager.playerRenderer.getMainModel().bipedRightArmwear && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class) != null && Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).hand.getValue() && Xulu.MODULE_MANAGER.getModule(ViewmodelChanger.class).isToggled()) {
            GlStateManager.scale((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizex.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizey.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).sizez.getValue());
            GlStateManager.translate((float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posX.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posY.getValue(), (float)Xulu.MODULE_MANAGER.getModuleT(ViewmodelChanger.class).posZ.getValue());
        }
    }
}
