// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.ui.altmanager.panels;

import java.util.Iterator;
import dev.tenacity.utils.render.ColorUtil;
import java.util.ArrayList;
import dev.tenacity.utils.tuples.Pair;
import java.util.List;
import dev.tenacity.ui.altmanager.Panel;

public class InfoPanel extends Panel
{
    private final List<Pair<String, String>> controlInfo;
    
    public InfoPanel() {
        this.controlInfo = new ArrayList<Pair<String, String>>();
        this.setHeight(135.0f);
        this.controlInfo.add(Pair.of("CTRL+V", "Paste a combo or combo list anywhere on the screen to import it"));
        this.controlInfo.add(Pair.of("DELETE", "When an alt is selected, you can delete it by pressing the delete key"));
        this.controlInfo.add(Pair.of("CTRL+A", "Selects the entire alt list"));
        this.controlInfo.add(Pair.of("CTRL+C", "Copies the combos of the currently selected alts"));
        this.controlInfo.add(Pair.of("SHIFT+CLICK", "Allows you to select a specfic range of alts"));
        this.controlInfo.add(Pair.of("DOUBLE-CLICK", "Logs into the selected alt"));
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        super.drawScreen(mouseX, mouseY);
        InfoPanel.tenacityBoldFont32.drawCenteredString("Information", this.getX() + this.getWidth() / 2.0f, this.getY() + 3.0f, ColorUtil.applyOpacity(-1, 0.75f));
        float controlY = this.getY() + InfoPanel.tenacityBoldFont32.getHeight() + 8.0f;
        for (final Pair<String, String> control : this.controlInfo) {
            InfoPanel.tenacityBoldFont18.drawString(control.getFirst() + " -", this.getX() + 12.0f, controlY, ColorUtil.applyOpacity(-1, 0.5f));
            InfoPanel.tenacityFont18.drawString(control.getSecond(), this.getX() + InfoPanel.tenacityBoldFont18.getStringWidth(control.getFirst() + " -") + 14.0f, controlY, ColorUtil.applyOpacity(-1, 0.35f));
            controlY += InfoPanel.tenacityBoldFont18.getHeight() + 6;
        }
        final String text = "Combos must be formatted in the following format: ";
        final String text2 = "email:password";
        final float textWidth = InfoPanel.tenacityFont18.getStringWidth(text);
        final float text2Width = InfoPanel.tenacityBoldFont18.getStringWidth(text2);
        final float middleX = this.getX() + this.getWidth() / 2.0f - (textWidth + text2Width) / 2.0f;
        InfoPanel.tenacityFont18.drawString(text, middleX, controlY + 4.0f, ColorUtil.applyOpacity(-1, 0.5f));
        InfoPanel.tenacityBoldFont18.drawString(text2, middleX + textWidth, controlY + 4.0f, ColorUtil.applyOpacity(-1, 0.5f));
        InfoPanel.tenacityFont18.drawCenteredString("Combo lists must have a new line seperating each combo", this.getX() + this.getWidth() / 2.0f, controlY + 16.0f, ColorUtil.applyOpacity(-1, 0.5f));
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
