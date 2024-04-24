package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.client.module.tweaks.BetterScreenshot;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.util.ScreenshotRecorder;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.File;
import java.util.function.Consumer;

@Mixin(ScreenshotRecorder.class)
public abstract class MixinScreenshotRecorder {
    @Inject(at = @At(value = "TAIL"), method = "method_1661", remap = false)
    private static void screenshotCaptured(NativeImage nativeImage, File file, Consumer<Text> consumer, CallbackInfo ci) {
        SessionManager.INSTANCE.protection();
        if (!BetterScreenshot.INSTANCE.getEnabled()) return;
        Image img = BetterScreenshot.getLatestScreenshot();
        BetterScreenshot.copyToClipboard(img);
    }
}
