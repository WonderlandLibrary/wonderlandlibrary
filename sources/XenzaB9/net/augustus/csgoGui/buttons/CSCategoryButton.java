// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.csgoGui.buttons;

import net.augustus.modules.Module;
import java.io.IOException;
import java.util.Iterator;
import net.augustus.Augustus;
import java.util.ArrayList;
import net.augustus.modules.Categorys;

public class CSCategoryButton extends CSButton
{
    public Categorys Categorys;
    public CSModButton currentMod;
    public ArrayList<CSModButton> buttons;
    
    public CSCategoryButton(final int x, final int y, final int width, final int height, final int color, final String displayString, final Categorys Categorys) {
        super(x, y, width, height, color, displayString);
        this.buttons = new ArrayList<CSModButton>();
        this.Categorys = Categorys;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int color = (this.isHovered(mouseX, mouseY) || Augustus.getInstance().csgogui.currentCategory == this) ? Augustus.getInstance().getClientColor().getRGB() : this.color;
        this.fr.drawString(this.displayString, this.x, this.y, color);
        for (final CSModButton csm : this.buttons) {
            if (Augustus.getInstance().csgogui.currentCategory == this) {
                csm.drawScreen(mouseX, mouseY, partialTicks);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        final boolean hoveredx = mouseX > this.x && mouseX < this.x + this.width;
        final boolean hoveredy = mouseY > this.y && mouseY < this.y + this.height;
        return hoveredx && hoveredy;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final CSModButton cmb : this.buttons) {
            cmb.mouseClicked(mouseX, mouseY, mouseButton);
            if (mouseButton == 1 && cmb.isHovered(mouseX, mouseY)) {
                this.currentMod = cmb;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void initButton() {
        this.buttons.clear();
        final int x = this.x + 65;
        int y = 110;
        for (int i = 0; i < Augustus.getInstance().getModuleManager().getModules().size(); ++i) {
            final Module m = Augustus.getInstance().getModuleManager().getModules().get(i);
            if (m.getCategory() == this.Categorys) {
                final CSModButton csm = new CSModButton(x, y, this.fr.getStringWidth(m.getName()), this.fr.FONT_HEIGHT, -1, m.getName(), m);
                this.buttons.add(csm);
                y += 22;
            }
        }
        super.initButton();
    }
    
    private boolean isCurrentPanel() {
        return Augustus.getInstance().csgogui.currentCategory == this;
    }
}
