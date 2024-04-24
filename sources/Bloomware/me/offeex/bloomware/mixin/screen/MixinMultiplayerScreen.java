package me.offeex.bloomware.mixin.screen;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.api.manager.managers.SessionManager;
import me.offeex.bloomware.client.module.client.Gui;
import me.offeex.bloomware.client.module.client.Hud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerServerListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MixinMultiplayerScreen extends Screen {
    protected MixinMultiplayerScreen(Text title) {
        super(title);
    }

    private Screen currentScreen() {
        return Bloomware.INSTANCE.getCurrentScreen();
    }

    @Inject(method = "connect*", at = @At("TAIL"))
    public void connect(CallbackInfo ci) {
        SessionManager.INSTANCE.reset();
        SessionManager.INSTANCE.start();
    }

    @Inject(method = "connect*", at = @At("HEAD"), cancellable = true)
    public void onConnect(CallbackInfo ci) {
        if (currentScreen() != null) ci.cancel();
    }

    @Inject(method = "select", at = @At("HEAD"), cancellable = true)
    public void onSelect(MultiplayerServerListWidget.Entry entry, CallbackInfo ci) {
        if (currentScreen() != null) ci.cancel();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (currentScreen() == Bloomware.INSTANCE.getGui() || currentScreen() == Bloomware.INSTANCE.getHud())
            currentScreen().render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (currentScreen() != null) {
            return currentScreen().mouseClicked(mouseX, mouseY, button);
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
        if (keyCode == Gui.INSTANCE.getKey()) Bloomware.INSTANCE.setCurrentScreen(Bloomware.INSTANCE.getGui());
        else if (keyCode == Hud.INSTANCE.getKey()) Bloomware.INSTANCE.setCurrentScreen(Bloomware.INSTANCE.getHud());
        else if (keyCode == GLFW.GLFW_KEY_ESCAPE && currentScreen() != null) {
            Bloomware.INSTANCE.setCurrentScreen(null);
            return false;
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
