/*
 * Decompiled with CFR 0.150.
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
    private final RealmsScreen proxy;

    public GuiScreenRealmsProxy(RealmsScreen proxyIn) {
        this.proxy = proxyIn;
        this.buttonList = Collections.synchronizedList(Lists.newArrayList());
    }

    public RealmsScreen getProxy() {
        return this.proxy;
    }

    @Override
    public void initGui() {
        this.proxy.init();
        super.initGui();
    }

    public void drawCenteredString(String p_154325_1_, int p_154325_2_, int p_154325_3_, int p_154325_4_) {
        super.drawCenteredString(this.fontRendererObj, p_154325_1_, p_154325_2_, p_154325_3_, p_154325_4_);
    }

    public void drawString(String p_154322_1_, int p_154322_2_, int p_154322_3_, int p_154322_4_, boolean p_154322_5_) {
        if (p_154322_5_) {
            super.drawString(this.fontRendererObj, p_154322_1_, p_154322_2_, p_154322_3_, p_154322_4_);
        } else {
            this.fontRendererObj.drawString(p_154322_1_, p_154322_2_, p_154322_3_, p_154322_4_);
        }
    }

    @Override
    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        this.proxy.blit(x, y, textureX, textureY, width, height);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }

    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }

    @Override
    public void drawWorldBackground(int tint) {
        super.drawWorldBackground(tint);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.proxy.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderToolTip(ItemStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }

    @Override
    public void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
        super.drawCreativeTabHoveringText(tabName, mouseX, mouseY);
    }

    @Override
    public void drawHoveringText(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }

    @Override
    public void updateScreen() {
        this.proxy.tick();
        super.updateScreen();
    }

    public int getFontHeight() {
        return this.fontRendererObj.FONT_HEIGHT;
    }

    public int getStringWidth(String p_154326_1_) {
        return this.fontRendererObj.getStringWidth(p_154326_1_);
    }

    public void fontDrawShadow(String p_154319_1_, int p_154319_2_, int p_154319_3_, int p_154319_4_) {
        this.fontRendererObj.drawStringWithShadow(p_154319_1_, p_154319_2_, p_154319_3_, p_154319_4_);
    }

    public List<String> fontSplit(String p_154323_1_, int p_154323_2_) {
        return this.fontRendererObj.listFormattedStringToWidth(p_154323_1_, p_154323_2_);
    }

    @Override
    public final void actionPerformed(GuiButton button) throws IOException {
        this.proxy.buttonClicked(((GuiButtonRealmsProxy)button).getRealmsButton());
    }

    public void buttonsClear() {
        this.buttonList.clear();
    }

    public void buttonsAdd(RealmsButton button) {
        this.buttonList.add(button.getProxy());
    }

    public List<RealmsButton> buttons() {
        ArrayList<RealmsButton> list = Lists.newArrayListWithExpectedSize(this.buttonList.size());
        for (GuiButton guibutton : this.buttonList) {
            list.add(((GuiButtonRealmsProxy)guibutton).getRealmsButton());
        }
        return list;
    }

    public void buttonsRemove(RealmsButton button) {
        this.buttonList.remove(button.getProxy());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.proxy.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        this.proxy.mouseEvent();
        super.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() throws IOException {
        this.proxy.keyboardEvent();
        super.handleKeyboardInput();
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.proxy.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        this.proxy.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        this.proxy.keyPressed(typedChar, keyCode);
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        this.proxy.confirmResult(result, id);
    }

    @Override
    public void onGuiClosed() {
        this.proxy.removed();
        super.onGuiClosed();
    }
}

