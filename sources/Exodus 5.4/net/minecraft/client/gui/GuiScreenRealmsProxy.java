/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonRealmsProxy;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy
extends GuiScreen {
    private RealmsScreen field_154330_a;

    public void func_154327_a(RealmsButton realmsButton) {
        this.buttonList.add(realmsButton.getProxy());
    }

    @Override
    public void drawScreen(int n, int n2, float f) {
        this.field_154330_a.render(n, n2, f);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.field_154330_a.mouseEvent();
        super.handleMouseInput();
    }

    public List<RealmsButton> func_154320_j() {
        ArrayList arrayList = Lists.newArrayListWithExpectedSize((int)this.buttonList.size());
        for (GuiButton guiButton : this.buttonList) {
            arrayList.add(((GuiButtonRealmsProxy)guiButton).getRealmsButton());
        }
        return arrayList;
    }

    @Override
    public void confirmClicked(boolean bl, int n) {
        this.field_154330_a.confirmResult(bl, n);
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }

    public void func_154324_i() {
        this.buttonList.clear();
    }

    public void func_154325_a(String string, int n, int n2, int n3) {
        super.drawCenteredString(this.fontRendererObj, string, n, n2, n3);
    }

    public void func_154328_b(RealmsButton realmsButton) {
        this.buttonList.remove(realmsButton);
    }

    @Override
    public void initGui() {
        this.field_154330_a.init();
        super.initGui();
    }

    @Override
    public void updateScreen() {
        this.field_154330_a.tick();
        super.updateScreen();
    }

    @Override
    public final void actionPerformed(GuiButton guiButton) throws IOException {
        this.field_154330_a.buttonClicked(((GuiButtonRealmsProxy)guiButton).getRealmsButton());
    }

    @Override
    public void keyTyped(char c, int n) throws IOException {
        this.field_154330_a.keyPressed(c, n);
    }

    public GuiScreenRealmsProxy(RealmsScreen realmsScreen) {
        this.field_154330_a = realmsScreen;
        this.buttonList = Collections.synchronizedList(Lists.newArrayList());
    }

    public RealmsScreen func_154321_a() {
        return this.field_154330_a;
    }

    public int func_154326_c(String string) {
        return this.fontRendererObj.getStringWidth(string);
    }

    public int func_154329_h() {
        return this.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void drawHoveringText(List<String> list, int n, int n2) {
        super.drawHoveringText(list, n, n2);
    }

    public void func_154322_b(String string, int n, int n2, int n3) {
        super.drawString(this.fontRendererObj, string, n, n2, n3);
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.field_154330_a.mouseReleased(n, n2, n3);
    }

    @Override
    public void drawWorldBackground(int n) {
        super.drawWorldBackground(n);
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        this.field_154330_a.keyboardEvent();
        super.handleKeyboardInput();
    }

    public List<String> func_154323_a(String string, int n) {
        return this.fontRendererObj.listFormattedStringToWidth(string, n);
    }

    public void func_154319_c(String string, int n, int n2, int n3) {
        this.fontRendererObj.drawStringWithShadow(string, n, n2, n3);
    }

    @Override
    public void drawTexturedModalRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.field_154330_a.blit(n, n2, n3, n4, n5, n6);
        super.drawTexturedModalRect(n, n2, n3, n4, n5, n6);
    }

    @Override
    public void drawCreativeTabHoveringText(String string, int n, int n2) {
        super.drawCreativeTabHoveringText(string, n, n2);
    }

    @Override
    public void onGuiClosed() {
        this.field_154330_a.removed();
        super.onGuiClosed();
    }

    public void drawGradientRect(int n, int n2, int n3, int n4, int n5, int n6) {
        GuiScreen.drawGradientRect(n, n2, n3, n4, n5, n6);
    }

    @Override
    public void renderToolTip(ItemStack itemStack, int n, int n2) {
        super.renderToolTip(itemStack, n, n2);
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) throws IOException {
        this.field_154330_a.mouseClicked(n, n2, n3);
        super.mouseClicked(n, n2, n3);
    }

    @Override
    public void mouseClickMove(int n, int n2, int n3, long l) {
        this.field_154330_a.mouseDragged(n, n2, n3, l);
    }
}

