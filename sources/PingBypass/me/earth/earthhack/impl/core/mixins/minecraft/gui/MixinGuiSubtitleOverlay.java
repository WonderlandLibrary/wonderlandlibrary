package me.earth.earthhack.impl.core.mixins.minecraft.gui;

import me.earth.earthhack.impl.Earthhack;
import me.earth.earthhack.impl.event.events.render.Render2DEvent;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSubtitleOverlay.class)
public abstract class MixinGuiSubtitleOverlay
{

    @Inject(method = "renderSubtitles", at = @At(value = "HEAD"))
    private void renderSubtitlesHook(CallbackInfo callbackInfo)
    {
        Earthhack.EVENT_BUS.post(new Render2DEvent());
    }

}
