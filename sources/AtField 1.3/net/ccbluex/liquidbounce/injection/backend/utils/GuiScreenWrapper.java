/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.injection.backend.GuiButtonImpl;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.Nullable;

public final class GuiScreenWrapper
extends GuiScreen {
    private final WrappedGuiScreen wrapped;

    protected void func_146284_a(@Nullable GuiButton guiButton) {
        block0: {
            GuiButton guiButton2 = guiButton;
            if (guiButton2 == null) break block0;
            GuiButton guiButton3 = guiButton2;
            boolean bl = false;
            boolean bl2 = false;
            GuiButton guiButton4 = guiButton3;
            boolean bl3 = false;
            GuiButton guiButton5 = guiButton;
            WrappedGuiScreen wrappedGuiScreen = this.wrapped;
            boolean bl4 = false;
            IGuiButton iGuiButton = new GuiButtonImpl(guiButton5);
            wrappedGuiScreen.actionPerformed(iGuiButton);
        }
    }

    protected void func_146286_b(int n, int n2, int n3) {
        this.wrapped.mouseReleased(n, n2, n3);
    }

    public final void superHandleMouseInput() {
        super.func_146274_d();
    }

    public void func_146274_d() {
        this.wrapped.handleMouseInput();
    }

    public final void superMouseClicked(int n, int n2, int n3) {
        super.func_73864_a(n, n2, n3);
    }

    protected void func_73869_a(char c, int n) {
        this.wrapped.keyTyped(c, n);
    }

    public void func_146281_b() {
        this.wrapped.onGuiClosed();
    }

    public void func_73863_a(int n, int n2, float f) {
        this.wrapped.drawScreen(n, n2, f);
    }

    public boolean func_73868_f() {
        return this.wrapped.doesGuiPauseGame();
    }

    protected void func_73864_a(int n, int n2, int n3) {
        this.wrapped.mouseClicked(n, n2, n3);
    }

    public void func_73866_w_() {
        this.wrapped.initGui();
    }

    public void func_73876_c() {
        this.wrapped.updateScreen();
    }

    public final void superKeyTyped(char c, int n) {
        super.func_73869_a(c, n);
    }

    public final void superDrawScreen(int n, int n2, float f) {
        super.func_73863_a(n, n2, f);
    }

    public final void superMouseReleased(int n, int n2, int n3) {
        super.func_146286_b(n, n2, n3);
    }

    public GuiScreenWrapper(WrappedGuiScreen wrappedGuiScreen) {
        this.wrapped = wrappedGuiScreen;
    }

    public final WrappedGuiScreen getWrapped() {
        return this.wrapped;
    }
}

