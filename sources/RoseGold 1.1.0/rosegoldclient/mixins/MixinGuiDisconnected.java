package rosegoldclient.mixins;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rosegoldclient.features.BlockBanPacket;


@Mixin(GuiDisconnected.class)
public class MixinGuiDisconnected extends GuiScreen {

    @Shadow
    private int textHeight;

    @Shadow @Final private ITextComponent message;

    @Inject(method = "initGui", at = @At("RETURN"))
    private void initGui(CallbackInfo ci) {
        if(message.getUnformattedText().contains("Kicked whilst connecting to WC")) {
            buttonList.add(new GuiButton(1, width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30) + 30, "Block ban packet"));
        }
    }

    @Inject(method = "actionPerformed", at = @At("RETURN"))
    private void actionPerformed(GuiButton button, CallbackInfo ci) {
        if(message.getUnformattedText().contains("Kicked whilst connecting to WC")) {
            if (button.id == 1) {
                BlockBanPacket.reconnect();
            }
        }
    }
}
