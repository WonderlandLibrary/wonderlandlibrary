package me.earth.earthhack.impl.core.mixins.minecraft.render.entity;

import me.earth.earthhack.api.event.bus.instance.Bus;
import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import net.minecraft.client.renderer.EntityRenderer;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer
{

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/GlStateManager.clear(I)V", ordinal = 1, shift = At.Shift.AFTER))
    private void renderWorldPassHook(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo)
    {
        if (Display.isActive() || Display.isVisible())
        {
            Bus.EVENT_BUS.post(new Render3DEvent(partialTicks));
        }
    }

}
