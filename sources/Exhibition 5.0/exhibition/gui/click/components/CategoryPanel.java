// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.components;

import java.util.Iterator;
import exhibition.gui.click.ui.UI;
import exhibition.Client;
import java.util.ArrayList;

public class CategoryPanel
{
    public float x;
    public float y;
    public boolean visible;
    public CategoryButton categoryButton;
    public String headerString;
    public ArrayList<Button> buttons;
    public ArrayList<Slider> sliders;
    public ArrayList<DropdownBox> dropdownBoxes;
    public ArrayList<DropdownButton> dropdownButtons;
    public ArrayList<Label> labels;
    public ArrayList<Checkbox> checkboxes;
    public ArrayList<ColorPreview> colorPreviews;
    public ArrayList<GroupBox> groupBoxes;
    public ArrayList<RGBSlider> rgbSliders;
    
    public CategoryPanel(final String name, final CategoryButton categoryButton, final float x, final float y) {
        this.headerString = name;
        this.x = x;
        this.y = y;
        this.categoryButton = categoryButton;
        this.colorPreviews = new ArrayList<ColorPreview>();
        this.buttons = new ArrayList<Button>();
        this.sliders = new ArrayList<Slider>();
        this.dropdownBoxes = new ArrayList<DropdownBox>();
        this.dropdownButtons = new ArrayList<DropdownButton>();
        this.labels = new ArrayList<Label>();
        this.checkboxes = new ArrayList<Checkbox>();
        this.groupBoxes = new ArrayList<GroupBox>();
        this.visible = false;
        categoryButton.panel.theme.categoryPanelConstructor(this, categoryButton, x, y);
    }
    
    public void draw(final float x, final float y) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelDraw(this, x, y);
        }
    }
    
    public void mouseClicked(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelMouseClicked(this, x, y, button);
        }
    }
    
    public void mouseReleased(final int x, final int y, final int button) {
        for (final UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelMouseMovedOrUp(this, x, y, button);
        }
    }
}
