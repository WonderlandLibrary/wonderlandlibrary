// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import java.util.ArrayList;
import exhibition.management.ColorObject;

public class ColorPreview
{
    public String colorName;
    public float x;
    public float y;
    public CategoryButton categoryPanel;
    public ColorObject colorObject;
    public ArrayList<RGBSlider> sliders;
    
    public ColorPreview(final ColorObject colorObject, final String colorName, final float x, final float y, final CategoryButton categoryPanel) {
        this.sliders = new ArrayList<RGBSlider>();
        this.colorObject = colorObject;
        this.categoryPanel = categoryPanel;
        this.colorName = colorName;
        this.x = x;
        this.y = y;
        categoryPanel.panel.theme.colorConstructor(this, x, y);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            if (this.categoryPanel.enabled) {
                theme.colorPrewviewDraw(this, x, y);
            }
        }
    }
}
