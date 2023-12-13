// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import exhibition.module.Module;

public class GroupBox
{
    public float x;
    public float y;
    public float ySize;
    public CategoryPanel categoryPanel;
    public Module module;
    
    public GroupBox(final Module module, final CategoryPanel categoryPanel, final float x, final float y, final float ySize) {
        this.x = x;
        this.y = y;
        this.categoryPanel = categoryPanel;
        this.module = module;
        this.ySize = ySize;
        categoryPanel.categoryButton.panel.theme.groupBoxConstructor(this, x, y);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.groupBoxDraw(this, x, y);
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.groupBoxMouseClicked(this, x, y, button);
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.groupBoxMouseMovedOrUp(this, x, y, button);
        }
    }
}
