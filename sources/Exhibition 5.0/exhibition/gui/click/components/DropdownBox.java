// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import java.util.ArrayList;
import exhibition.module.data.Options;

public class DropdownBox
{
    public Options option;
    public float x;
    public float y;
    public ArrayList<DropdownButton> buttons;
    public CategoryPanel panel;
    public boolean active;
    
    public DropdownBox(final Options option, final float x, final float y, final CategoryPanel panel) {
        this.buttons = new ArrayList<DropdownButton>();
        this.option = option;
        this.panel = panel;
        this.x = x;
        this.y = y;
        panel.categoryButton.panel.theme.dropDownContructor(this, x, y, this.panel);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (this.panel.visible) {
                theme.dropDownDraw(this, x, y, this.panel);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownMouseClicked(this, x, y, button, this.panel);
        }
    }
}
