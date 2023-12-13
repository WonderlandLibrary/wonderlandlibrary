// 
// Decompiled by Procyon v0.5.36
// 

package Lavish.ClickGUI.LavishClickGUI;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.util.Iterator;
import Lavish.ClickGUI.LavishClickGUI.component.Component;
import Lavish.modules.Category;
import Lavish.ClickGUI.LavishClickGUI.component.Frame;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGui extends GuiScreen
{
    public static ArrayList<Frame> frames;
    public static int color;
    
    static {
        ClickGui.color = -1;
    }
    
    public ClickGui() {
        ClickGui.frames = new ArrayList<Frame>();
        int frameX = 5;
        Category[] values;
        for (int length = (values = Category.values()).length, i = 0; i < length; ++i) {
            final Category category = values[i];
            final Frame frame = new Frame(category);
            frame.setX(frameX);
            ClickGui.frames.add(frame);
            frameX += frame.getWidth() + 1;
        }
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        for (final Frame frame : ClickGui.frames) {
            frame.renderFrame(this.fontRendererObj);
            frame.updatePosition(mouseX, mouseY);
            for (final Component comp : frame.getComponents()) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
                frame.setDrag(true);
                frame.dragX = mouseX - frame.getX();
                frame.dragY = mouseY - frame.getY();
            }
            if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) {
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && keyCode != 1 && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.keyTyped(typedChar, keyCode);
                }
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final Frame frame : ClickGui.frames) {
            frame.setDrag(false);
        }
        for (final Frame frame : ClickGui.frames) {
            if (frame.isOpen() && !frame.getComponents().isEmpty()) {
                for (final Component component : frame.getComponents()) {
                    component.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    private void drawFace(final double x, final double y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        try {
            final ResourceLocation skin = this.mc.thePlayer.getLocationSkin();
            this.mc.getTextureManager().bindTexture(skin);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawScaledCustomSizeModalRect((int)x, (int)y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        }
        catch (Exception ex) {}
    }
}
