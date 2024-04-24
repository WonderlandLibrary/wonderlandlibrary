package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.FontManagerr;
import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.api.structure.ColorMutable;
import me.offeex.bloomware.client.module.tweaks.BetterTab;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;



@Mixin(PlayerListHud.class)
public class MixinPlayerTab {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(II)I", ordinal = 0), index = 1)
    private int modifyCount(int count) {
        SessionManager.INSTANCE.protection();
        BetterTab module = BetterTab.INSTANCE;
        return module.getEnabled() && BetterTab.INSTANCE.getModifySize().getToggled() ? (int) BetterTab.INSTANCE.getMaxSize().getValue() : count;
    }

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
    public void getPlayerName(PlayerListEntry playerListEntry, CallbackInfoReturnable<Text> info) {
        BetterTab module = BetterTab.INSTANCE;
        if (module.getEnabled()) info.setReturnValue(module.getText(playerListEntry));
    }

    @Shadow
    protected void renderLatencyIcon(MatrixStack matrices, int width, int x, int y, PlayerListEntry entry) {}

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/PlayerListHud;renderLatencyIcon(Lnet/minecraft/client/util/math/MatrixStack;IIILnet/minecraft/client/network/PlayerListEntry;)V"))
    protected void renderLatencyIcon(PlayerListHud instance, MatrixStack matrixStack, int i, int j, int k, PlayerListEntry playerListEntry) {
        // TODO: Prikoli s mixinami
        BetterTab module = BetterTab.INSTANCE;
        if (module.getEnabled() && BetterTab.INSTANCE.getPing().getToggled()) {
            int ping = playerListEntry.getLatency();
            if (BetterTab.INSTANCE.getCustomFont().getToggled()) FontManagerr.INSTANCE.drawString(matrixStack, String.valueOf(ping), i + j - FontManagerr.INSTANCE.width(String.valueOf(ping)), k - 3, calculateColor(ping), null);
            else Bloomware.INSTANCE.getMc().textRenderer.draw(matrixStack, String.valueOf(ping), i + j - Bloomware.INSTANCE.getMc().textRenderer.getWidth(String.valueOf(ping)), k, calculateColor(ping).getArgb());
        } else renderLatencyIcon(matrixStack, i, j, k, playerListEntry);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;FFI)I"))
    protected int drawString(TextRenderer instance, MatrixStack matrixStack, Text text, float f, float g, int i) {
        BetterTab module = BetterTab.INSTANCE;
        if (module.getEnabled() && BetterTab.INSTANCE.getCustomFont().getToggled()) {
            FontManagerr.INSTANCE.drawString(matrixStack, text.getString(), f, g - 3, text.getStyle().getColor() == null ? new ColorMutable(-1) : new ColorMutable(text.getStyle().getColor().getRgb()), null);
        } else {
            Bloomware.INSTANCE.getMc().textRenderer.draw(matrixStack, text, f, g, i);
        }
        return 0;
    }

    private ColorMutable calculateColor(int ping) {
        return ping < 150 ? ColorMutable.Companion.getGREEN() : ping < 300 ? ColorMutable.Companion.getYELLOW() : ColorMutable.Companion.getRED();
    }
}
