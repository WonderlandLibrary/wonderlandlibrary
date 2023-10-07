// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import exhibition.module.Module;

public class Button
{
    public float x;
    public float y;
    public String name;
    public CategoryPanel panel;
    public boolean enabled;
    public Module module;
    public boolean isBinding;
    
    public Button(final CategoryPanel panel, final String name, final float x, final float y, final Module module) {
        this.panel = panel;
        this.name = name;
        this.x = x;
        this.y = y;
        this.module = module;
        this.enabled = module.isEnabled();
        panel.categoryButton.panel.theme.buttonContructor(this, this.panel);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (this.panel.visible) {
                theme.buttonDraw(this, x, y, this.panel);
            }
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.buttonMouseClicked(this, x, y, button, this.panel);
        }
    }
    
    public void keyPressed(final int key) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.buttonKeyPressed(this, key);
        }
    }
}
