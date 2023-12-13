// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.mixin.mixins;

import com.elementars.eclient.event.Event;
import com.elementars.eclient.event.events.EventModelPlayerRender;
import net.minecraft.client.model.ModelBase;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.elementars.eclient.module.render.Skeleton;
import net.minecraft.entity.player.EntityPlayer;
import com.elementars.eclient.util.Wrapper;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ModelPlayer.class }, priority = 9999)
public class MixinModelPlayer
{
    @Shadow
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
    }
    
    @Inject(method = { "setRotationAngles" }, at = { @At("RETURN") })
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn, final CallbackInfo callbackInfo) {
        if (Wrapper.getMinecraft().world != null && Wrapper.getMinecraft().player != null && entityIn instanceof EntityPlayer) {
            Skeleton.addEntity((EntityPlayer)entityIn, (ModelPlayer)this);
        }
    }
    
    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void renderPre(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final EventModelPlayerRender modelrenderpre = new EventModelPlayerRender((ModelBase)ModelPlayer.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        modelrenderpre.setState(Event.State.PRE);
        modelrenderpre.call();
        if (modelrenderpre.isCancelled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "render" }, at = { @At("RETURN") })
    private void renderPost(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final EventModelPlayerRender modelrenderpost = new EventModelPlayerRender((ModelBase)ModelPlayer.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        modelrenderpost.setState(Event.State.POST);
        modelrenderpost.call();
    }
}
