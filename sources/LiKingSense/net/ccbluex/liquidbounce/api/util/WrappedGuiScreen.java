/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.util;

import java.io.IOException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\f\n\u0002\b\b\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J \u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\nH\u0016J\b\u0010\u0016\u001a\u00020\nH\u0016J\u0018\u0010\u0017\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0011H\u0016J \u0010\u001b\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u0011H\u0016J \u0010\u001d\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u0011H\u0016J\b\u0010\u001f\u001a\u00020\nH\u0016J\b\u0010 \u001a\u00020\nH\u0016R\u001a\u0010\u0003\u001a\u00020\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b\u00a8\u0006!"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "()V", "representedScreen", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "getRepresentedScreen", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;", "setRepresentedScreen", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiScreen;)V", "actionPerformed", "", "button", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IGuiButton;", "doesGuiPauseGame", "", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "handleMouseInput", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "mouseReleased", "state", "onGuiClosed", "updateScreen", "LiKingSense"})
public abstract class WrappedGuiScreen
extends MinecraftInstance {
    @NotNull
    public IGuiScreen representedScreen;

    @NotNull
    public final IGuiScreen getRepresentedScreen() {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        return iGuiScreen;
    }

    public final void setRepresentedScreen(@NotNull IGuiScreen iGuiScreen) {
        Intrinsics.checkParameterIsNotNull((Object)iGuiScreen, (String)"<set-?>");
        this.representedScreen = iGuiScreen;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        iGuiScreen.superDrawScreen(mouseX, mouseY, partialTicks);
    }

    public void initGui() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        iGuiScreen.superMouseClicked(mouseX, mouseY, mouseButton);
    }

    public void updateScreen() {
    }

    public void handleMouseInput() throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        iGuiScreen.superHandleMouseInput();
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        iGuiScreen.superKeyTyped(typedChar, keyCode);
    }

    public void actionPerformed(@NotNull IGuiButton button) throws IOException {
        Intrinsics.checkParameterIsNotNull((Object)button, (String)"button");
    }

    public void onGuiClosed() {
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        IGuiScreen iGuiScreen = this.representedScreen;
        if (iGuiScreen == null) {
            Intrinsics.throwUninitializedPropertyAccessException((String)"representedScreen");
        }
        iGuiScreen.superMouseReleased(mouseX, mouseY, state);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}

