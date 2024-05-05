package rosegoldclient.mixins;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldclient.events.ActionbarMessage;

@Mixin(GuiIngameForge.class)
public class MixinGuiIngame {
    @Inject(method = "setOverlayMessage", at = @At("RETURN"), cancellable = true)
    public void setOverlayMessage(ITextComponent component, boolean animateColor, CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new ActionbarMessage(component.getUnformattedText()))) ci.cancel();
    }
}
