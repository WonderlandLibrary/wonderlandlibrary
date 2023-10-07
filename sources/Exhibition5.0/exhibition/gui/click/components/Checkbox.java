// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import exhibition.module.data.Setting;

public class Checkbox
{
    public CategoryPanel panel;
    public boolean enabled;
    public float x;
    public float y;
    public String name;
    public Setting setting;
    
    public Checkbox(final CategoryPanel panel, final String name, final float x, final float y, final Setting setting) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.setting = setting;
        this.enabled = setting.getValue();
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (this.panel.visible) {
                theme.checkBoxDraw(this, x, y, this.panel);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.checkBoxMouseClicked(this, x, y, button, this.panel);
        }
    }
}
