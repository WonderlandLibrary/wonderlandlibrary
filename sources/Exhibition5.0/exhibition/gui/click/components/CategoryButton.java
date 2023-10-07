// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;

public class CategoryButton
{
    public float x;
    public float y;
    public String name;
    public MainPanel panel;
    public boolean enabled;
    public CategoryPanel categoryPanel;
    
    public CategoryButton(final MainPanel panel, final String name, final float x, final float y) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        panel.theme.categoryButtonConstructor(this, this.panel);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonDraw(this, x, y);
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseClicked(this, this.panel, x, y, button);
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryButtonMouseReleased(this, x, y, button);
        }
    }
}
