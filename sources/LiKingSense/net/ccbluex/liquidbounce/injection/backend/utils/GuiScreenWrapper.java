/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.injection.backend.GuiButtonImpl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\f\n\u0002\b\r\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0014J\b\u0010\u000b\u001a\u00020\fH\u0016J \u0010\r\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\bH\u0016J\b\u0010\u0014\u001a\u00020\bH\u0016J\u0018\u0010\u0015\u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000fH\u0014J \u0010\u0019\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u000fH\u0014J \u0010\u001b\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000fH\u0014J\b\u0010\u001d\u001a\u00020\bH\u0016J\u001e\u0010\u001e\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u001f\u001a\u00020\bJ\u0016\u0010 \u001a\u00020\b2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u000fJ\u001e\u0010!\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u000fJ\u001e\u0010\"\u001a\u00020\b2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u000fJ\b\u0010#\u001a\u00020\bH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006$"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/utils/GuiScreenWrapper;", "Lnet/minecraft/client/gui/GuiScreen;", "wrapped", "Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "(Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;)V", "getWrapped", "()Lnet/ccbluex/liquidbounce/api/util/WrappedGuiScreen;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "doesGuiPauseGame", "", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "handleMouseInput", "initGui", "keyTyped", "typedChar", "", "keyCode", "mouseClicked", "mouseButton", "mouseReleased", "state", "onGuiClosed", "superDrawScreen", "superHandleMouseInput", "superKeyTyped", "superMouseClicked", "superMouseReleased", "updateScreen", "LiKingSense"})
public final class GuiScreenWrapper
extends GuiScreen {
    @NotNull
    private final WrappedGuiScreen wrapped;

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.wrapped.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void func_73866_w_() {
        this.wrapped.initGui();
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
        this.wrapped.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void func_73876_c() {
        this.wrapped.updateScreen();
    }

    public void func_146274_d() {
        this.wrapped.handleMouseInput();
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        this.wrapped.keyTyped(typedChar, keyCode);
    }

    /*
     * WARNING - void declaration
     */
    protected void func_146284_a(@Nullable GuiButton button) {
        block0: {
            void $this$wrap$iv;
            GuiButton guiButton = button;
            if (guiButton == null) break block0;
            GuiButton guiButton2 = guiButton;
            boolean bl = false;
            boolean bl2 = false;
            GuiButton it = guiButton2;
            boolean bl3 = false;
            GuiButton guiButton3 = button;
            WrappedGuiScreen wrappedGuiScreen = this.wrapped;
            boolean $i$f$wrap = false;
            IGuiButton iGuiButton = new GuiButtonImpl((GuiButton)$this$wrap$iv);
            wrappedGuiScreen.actionPerformed(iGuiButton);
        }
    }

    public void func_146281_b() {
        this.wrapped.onGuiClosed();
    }

    protected void func_146286_b(int mouseX, int mouseY, int state) {
        this.wrapped.mouseReleased(mouseX, mouseY, state);
    }

    public boolean func_73868_f() {
        return this.wrapped.doesGuiPauseGame();
    }

    public final void superMouseReleased(int mouseX, int mouseY, int state) {
        super.func_146286_b(mouseX, mouseY, state);
    }

    public final void superKeyTyped(char typedChar, int keyCode) {
        super.func_73869_a(typedChar, keyCode);
    }

    public final void superHandleMouseInput() {
        super.func_146274_d();
    }

    public final void superMouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }

    public final void superDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    @NotNull
    public final WrappedGuiScreen getWrapped() {
        return this.wrapped;
    }

    public GuiScreenWrapper(@NotNull WrappedGuiScreen wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

