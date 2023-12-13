// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.Client;
import java.util.ArrayList;
import exhibition.gui.click.ui.UI;

public class MainPanel
{
    public float x;
    public float y;
    public String headerString;
    public float dragX;
    public float dragY;
    public float lastDragX;
    public float lastDragY;
    public boolean dragging;
    UI theme;
    public boolean opened;
    public ArrayList<CategoryButton> typeButton;
    public ArrayList<CategoryPanel> typePanel;
    public ArrayList<SLButton> slButtons;
    
    public boolean isOpened() {
        return this.opened;
    }
    
    public void setOpened(final boolean opened) {
        this.opened = opened;
    }
    
    public MainPanel(final String header, final float x, final float y, final UI theme) {
        this.headerString = header;
        this.x = x;
        this.y = y;
        this.theme = theme;
        this.typeButton = new ArrayList<CategoryButton>();
        this.typePanel = new ArrayList<CategoryPanel>();
        this.slButtons = new ArrayList<SLButton>();
        theme.panelConstructor(this, x, y);
    }
    
    public void mouseClicked(final int x, final int y, final int state) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.panelMouseClicked(this, x, y, state);
        }
    }
    
    public void mouseMovedOrUp(final int x, final int y, final int state) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.panelMouseMovedOrUp(this, x, y, state);
        }
    }
    
    public void draw(final int mouseX, final int mouseY) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.mainPanelDraw(this, mouseX, mouseY);
        }
    }
    
    public void keyPressed(final int key) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.mainPanelKeyPress(this, key);
        }
    }
}
