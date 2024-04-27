package net.ccbluex.liquidbounce.api.util;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\b\n\b\n\n\b\n\f\n\b\b\b&\u000020B¢J\t0\n20\fHJ\b\r0HJ 0\n202020HJ\b0\nHJ\b0\nHJ0\n2020HJ 0\n202020HJ 0\n202020HJ\b0\nHJ\b 0\nHR0X.¢\n\u0000\b\"\b\b¨!"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "representedScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "getRepresentedScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "setRepresentedScreen", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;)V", "actionPerformed", "", "button", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "doesGuiPauseGame", "", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "handleMouseInput", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "mouseReleased", "state", "onGuiClosed", "updateScreen", "Pride"})
public abstract class WrappedGuiScreen
extends MinecraftInstance {
    @NotNull
    public IGuiScreen representedScreen;

    @NotNull
    public final IGuiScreen getRepresentedScreen() {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        return iGuiScreen;
    }

    public final void setRepresentedScreen(@NotNull IGuiScreen iGuiScreen) {
        Intrinsics.checkParameterIsNotNull(iGuiScreen, "<set-?>");
        this.representedScreen = iGuiScreen;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        iGuiScreen.superDrawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        iGuiScreen.superMouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
    }

    public void handleMouseInput() throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        iGuiScreen.superHandleMouseInput();
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        iGuiScreen.superKeyTyped(typedChar, keyCode);
    }

    public void actionPerformed(@NotNull IGuiButton button) throws IOException {
        Intrinsics.checkParameterIsNotNull(button, "button");
    }

    public void onGuiClosed() {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException("representedScreen");
        }
        iGuiScreen.superMouseReleased(mouseX, mouseY, state);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
