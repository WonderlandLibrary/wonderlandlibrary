// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;

public class DropdownButton
{
    public String name;
    public float x;
    public float y;
    public DropdownBox box;
    
    public DropdownButton(final String name, final float x, final float y, final DropdownBox box) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.box = box;
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownButtonDraw(this, this.box, x, y);
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.dropDownButtonMouseClicked(this, this.box, x, y, button);
        }
    }
}
