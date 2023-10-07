// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui;

import java.io.IOException;
import java.util.Iterator;
import net.augustus.Augustus;
import net.minecraft.client.gui.Gui;
import net.augustus.modules.Categorys;
import net.augustus.csgoGui.buttons.CSCategoryButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class CSGOGui extends GuiScreen
{
    public ArrayList<CSCategoryButton> buttons;
    public CSCategoryButton currentCategory;
    public int x;
    public int y;
    public int width;
    public int height;
    
    public CSGOGui() {
        this.buttons = new ArrayList<CSCategoryButton>();
        this.x = 100;
        this.y = 100;
        this.width -= 100;
        this.height -= 100;
    }
    
    private void initButtons() {
        this.buttons.clear();
        final int x = 110;
        int y = 110;
        for (final Categorys c : Categorys.values()) {
            final String categoryname = c.name().substring(0, 1).toUpperCase() + c.name().substring(1, c.name().length()).toLowerCase();
            final CSCategoryButton cscb = new CSCategoryButton(x, y, this.mc.fontRendererObj.getStringWidth(categoryname), this.mc.fontRendererObj.FONT_HEIGHT, -1, categoryname, c);
            this.buttons.add(cscb);
            y += 35;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(this.x, this.y, this.width, this.height, Integer.MIN_VALUE);
        for (final CSCategoryButton cb : this.buttons) {
            cb.drawScreen(mouseX, mouseY, partialTicks);
        }
        Gui.drawRect(this.x + 65, this.y, this.x + 67, this.height, Augustus.getInstance().getClientColor().getRGB());
        Gui.drawRect(this.x + 165, this.y, this.x + 167, this.height, Augustus.getInstance().getClientColor().getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final CSCategoryButton cb : this.buttons) {
            cb.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        boolean anyhovered = true;
        for (final CSCategoryButton cb : this.buttons) {
            if (cb.isHovered(mouseX, mouseY) && mouseButton == 0) {
                anyhovered = true;
                this.currentCategory = cb;
            }
            cb.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final CSCategoryButton cb : this.buttons) {
            cb.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void initGui() {
        this.initButtons();
        this.x = 100;
        this.y = 100;
        this.width -= 100;
        this.height -= 100;
        for (final CSCategoryButton cb : this.buttons) {
            cb.initButton();
        }
        super.initGui();
    }
}
