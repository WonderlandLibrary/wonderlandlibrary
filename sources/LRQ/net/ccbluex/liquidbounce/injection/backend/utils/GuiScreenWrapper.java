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

    public final WrappedGuiScreen getWrapped() {
        return this.wrapped;
    }

    public GuiScreenWrapper(WrappedGuiScreen wrapped) {
        this.wrapped = wrapped;
    }
}

