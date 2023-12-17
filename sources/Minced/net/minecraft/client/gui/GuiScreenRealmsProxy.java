// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.realms.RealmsButton;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy extends GuiScreen
{
    private final RealmsScreen proxy;
    
    public GuiScreenRealmsProxy(final RealmsScreen proxyIn) {
        this.proxy = proxyIn;
        this.buttonList = Collections.synchronizedList((List<GuiButton>)Lists.newArrayList());
    }
    
    public RealmsScreen getProxy() {
        return this.proxy;
    }
    
    @Override
    public void initGui() {
        this.proxy.init();
        super.initGui();
    }
    
    public void drawCenteredString(final String text, final int x, final int y, final int color) {
        super.drawCenteredString(this.fontRenderer, text, x, y, color);
    }
    
    public void drawString(final String text, final int x, final int y, final int color, final boolean p_154322_5_) {
        if (p_154322_5_) {
            super.drawString(this.fontRenderer, text, x, y, color);
        }
        else {
            this.fontRenderer.drawString(text, x, y, color);
        }
    }
    
    @Override
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        this.proxy.blit(x, y, textureX, textureY, width, height);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }
    
    public void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
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
    public void drawWorldBackground(final int tint) {
        super.drawWorldBackground(tint);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.proxy.render(mouseX, mouseY, partialTicks);
    }
    
    public void renderToolTip(final ItemStack stack, final int x, final int y) {
        super.renderToolTip(stack, x, y);
    }
    
    @Override
    public void drawHoveringText(final String text, final int x, final int y) {
        super.drawHoveringText(text, x, y);
    }
    
    @Override
    public void drawHoveringText(final List<String> textLines, final int x, final int y) {
        super.drawHoveringText(textLines, x, y);
    }
    
    @Override
    public void updateScreen() {
        this.proxy.tick();
        super.updateScreen();
    }
    
    public int getFontHeight() {
        return this.fontRenderer.FONT_HEIGHT;
    }
    
    public int getStringWidth(final String text) {
        return this.fontRenderer.getStringWidth(text);
    }
    
    public void fontDrawShadow(final String text, final int x, final int y, final int color) {
        this.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public List<String> fontSplit(final String text, final int wrapWidth) {
        return this.fontRenderer.listFormattedStringToWidth(text, wrapWidth);
    }
    
    public final void actionPerformed(final GuiButton button) throws IOException {
        this.proxy.buttonClicked(((GuiButtonRealmsProxy)button).getRealmsButton());
    }
    
    public void buttonsClear() {
        this.buttonList.clear();
    }
    
    public void buttonsAdd(final RealmsButton button) {
        this.buttonList.add(button.getProxy());
    }
    
    public List<RealmsButton> buttons() {
        final List<RealmsButton> list = (List<RealmsButton>)Lists.newArrayListWithExpectedSize(this.buttonList.size());
        for (final GuiButton guibutton : this.buttonList) {
            list.add(((GuiButtonRealmsProxy)guibutton).getRealmsButton());
        }
        return list;
    }
    
    public void buttonsRemove(final RealmsButton button) {
        this.buttonList.remove(button.getProxy());
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
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
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.proxy.mouseReleased(mouseX, mouseY, state);
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.proxy.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.proxy.keyPressed(typedChar, keyCode);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        this.proxy.confirmResult(result, id);
    }
    
    @Override
    public void onGuiClosed() {
        this.proxy.removed();
        super.onGuiClosed();
    }
}
