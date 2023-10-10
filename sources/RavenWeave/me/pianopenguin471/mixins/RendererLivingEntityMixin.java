package me.pianopenguin471.mixins;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.weavemc.loader.api.event.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ravenweave.client.event.impl.RenderLabelEvent;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin<T extends EntityLivingBase> extends Render<T> {
    protected RendererLivingEntityMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @ModifyVariable(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"), ordinal = 4)
    public float changeYaw(float input) {
        System.out.println(input);
        return input + 90;
    }

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true)
    private void onRenderLabel(EntityLivingBase entity, double x, double y, double z, CallbackInfo ci) {
        RenderLabelEvent e = new RenderLabelEvent(entity, x, y, z);
        EventBus.callEvent(e);
        if (e.isCancelled())
            ci.cancel();
    }
}
