package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.client.module.tweaks.BetterChat;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class MixinChatScreen {
    @Shadow protected TextFieldWidget chatField;

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;setMaxLength(I)V", shift = At.Shift.AFTER))
    private void changeMaxLength(CallbackInfo ci) {
        SessionManager.INSTANCE.protection();
        BetterChat module = BetterChat.INSTANCE;
        if (module.getEnabled() && BetterChat.INSTANCE.getInfiniteTextField().getToggled()) chatField.setMaxLength(Integer.MAX_VALUE);
    }

    @Inject(method = "onChatFieldUpdate", at = @At("HEAD"))
    private void onChatFieldUpdate(String string, CallbackInfo ci) {
        BetterChat module = BetterChat.INSTANCE;
        chatField.setMaxLength(BetterChat.INSTANCE.getInfiniteChat().getToggled() && module.getEnabled() ? Integer.MAX_VALUE : 256);
    }
}
