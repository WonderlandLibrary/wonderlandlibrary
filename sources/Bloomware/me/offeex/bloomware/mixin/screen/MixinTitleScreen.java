package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.gui.screen.LoginScreen;
import me.offeex.bloomware.api.manager.managers.RenderManager;
import me.offeex.bloomware.client.module.client.Gui;
import me.offeex.bloomware.client.module.client.Hud;
import me.offeex.bloomware.event.events.EventRender;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Text text) {
        super(text);
    }

    @Unique
    private Screen currentScreen() {
        return Bloomware.INSTANCE.getCurrentScreen();
    }

    @Inject(method = "render" , at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/RotatingCubeMapRenderer;render(FF)V", shift = At.Shift.AFTER), cancellable = true)
    private void renderAfterPanorama(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        new EventRender.TitleScreen.AfterPanorama(matrices, mouseX, mouseY, delta).post(ci);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;drawCenteredTextWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"))
    public void onDrawString(MatrixStack matrices, TextRenderer textRenderer, String s, int x, int y, int color) {
        if (currentScreen() == null) textRenderer.drawWithShadow(matrices, s, x, y, color);
    }

//    @Inject(method = "render", at = @At(
//        value = "INVOKE",
//        target = "Lnet/minecraft/client/gui/LogoDrawer;draw(Lnet/minecraft/client/util/math/MatrixStack;IF)V"
//    ))
//    private void renderBackgroundPost(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        RenderManager.INSTANCE.getBackgroundShader().render(delta);
//    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderPost(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Bloomware.INSTANCE.getMc().textRenderer.drawWithShadow(matrices, Formatting.DARK_PURPLE + Bloomware.NAME + Formatting.WHITE + " " + Bloomware.INSTANCE.getVERSION() + " made by OffeeX & Rikonardo", 5, 5, 0);
        if (currentScreen() != null) currentScreen().render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentScreen() != null) {
            currentScreen().mouseClicked(mouseX, mouseY, button);
            return false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (currentScreen() != null) currentScreen().mouseReleased(mouseX, mouseY, button);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!(currentScreen() instanceof LoginScreen)) {
            if (keyCode == Gui.INSTANCE.getKey()) Bloomware.INSTANCE.setCurrentScreen(Bloomware.INSTANCE.getGui());
            else if (keyCode == Hud.INSTANCE.getKey()) Bloomware.INSTANCE.setCurrentScreen(Bloomware.INSTANCE.getHud());
            else if (keyCode == GLFW.GLFW_KEY_ESCAPE) Bloomware.INSTANCE.setCurrentScreen(null);
        }

        if (currentScreen() != null) currentScreen().keyPressed(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (currentScreen() != null) currentScreen().charTyped(c, i);
        return super.charTyped(c, i);
    }
}
