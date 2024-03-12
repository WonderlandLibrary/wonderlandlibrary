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

public class MicrosoftInfoPanel extends Panel
{
    private final List<Pair<String, String>> steps;
    
    public MicrosoftInfoPanel() {
        this.steps = new ArrayList<Pair<String, String>>();
        this.setHeight(135.0f);
        this.steps.add(Pair.of("1", "Type the email and password either as a combo or in each respective field"));
        this.steps.add(Pair.of("2", "Click the microsoft login button"));
        this.steps.add(Pair.of("3", "Your browser will open with a microsoft login panel"));
        this.steps.add(Pair.of("INFO", "Make sure that you are logged out of all microsoft accounts so that you are prompted with a login panel"));
        this.steps.add(Pair.of("4", "The email and password will be copied to the clipboard"));
        this.steps.add(Pair.of("5", "Follow all the steps Microsoft gives you to log into your account"));
        this.steps.add(Pair.of("6", "Enjoy! You are now logged in to your microsoft account"));
    }
    
    @Override
    public void initGui() {
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY) {
        this.setHeight(119.0f);
        super.drawScreen(mouseX, mouseY);
        MicrosoftInfoPanel.tenacityBoldFont26.drawCenteredString("How to use Microsoft Login", this.getX() + this.getWidth() / 2.0f, this.getY() + 4.0f, ColorUtil.applyOpacity(-1, 0.75f));
        float controlY = this.getY() + MicrosoftInfoPanel.tenacityBoldFont32.getHeight() + 8.0f;
        for (final Pair<String, String> control : this.steps) {
            if (control.getFirst().equals("INFO")) {
                MicrosoftInfoPanel.tenacityFont16.drawCenteredString("Make sure that you are logged out of all microsoft accounts so that you are", this.getX() + this.getWidth() / 2.0f, controlY, ColorUtil.applyOpacity(-1, 0.75f));
                controlY += MicrosoftInfoPanel.tenacityBoldFont16.getHeight() + 4;
                MicrosoftInfoPanel.tenacityFont16.drawCenteredString("prompted with a new login panel", this.getX() + this.getWidth() / 2.0f, controlY, ColorUtil.applyOpacity(-1, 0.75f));
                controlY += MicrosoftInfoPanel.tenacityBoldFont16.getHeight() + 6;
            }
            else {
                MicrosoftInfoPanel.tenacityBoldFont16.drawString(control.getFirst() + ". ", this.getX() + 12.0f, controlY, ColorUtil.applyOpacity(-1, 0.5f));
                MicrosoftInfoPanel.tenacityFont16.drawString(control.getSecond(), this.getX() + MicrosoftInfoPanel.tenacityBoldFont16.getStringWidth(control.getFirst() + ". ") + 14.0f, controlY, ColorUtil.applyOpacity(-1, 0.35f));
                controlY += MicrosoftInfoPanel.tenacityBoldFont16.getHeight() + 6;
            }
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
    }
}
