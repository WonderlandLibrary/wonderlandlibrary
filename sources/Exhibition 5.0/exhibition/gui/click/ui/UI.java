// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.gui.click.ui;

import exhibition.gui.click.components.RGBSlider;
import exhibition.gui.click.components.ColorPreview;
import exhibition.gui.click.components.SLButton;
import exhibition.gui.click.components.Slider;
import exhibition.gui.click.components.DropdownButton;
import exhibition.gui.click.components.DropdownBox;
import exhibition.gui.click.components.Checkbox;
import exhibition.gui.click.components.Button;
import exhibition.gui.click.components.GroupBox;
import exhibition.gui.click.components.CategoryPanel;
import exhibition.gui.click.components.CategoryButton;
import exhibition.gui.click.components.MainPanel;
import exhibition.gui.click.ClickGui;

public abstract class UI
{
    public abstract void mainConstructor(final ClickGui p0);
    
    public abstract void mainPanelDraw(final MainPanel p0, final int p1, final int p2);
    
    public abstract void mainPanelKeyPress(final MainPanel p0, final int p1);
    
    public abstract void panelConstructor(final MainPanel p0, final float p1, final float p2);
    
    public abstract void panelMouseClicked(final MainPanel p0, final int p1, final int p2, final int p3);
    
    public abstract void panelMouseMovedOrUp(final MainPanel p0, final int p1, final int p2, final int p3);
    
    public abstract void categoryButtonConstructor(final CategoryButton p0, final MainPanel p1);
    
    public abstract void categoryButtonMouseClicked(final CategoryButton p0, final MainPanel p1, final int p2, final int p3, final int p4);
    
    public abstract void categoryButtonDraw(final CategoryButton p0, final float p1, final float p2);
    
    public abstract void categoryPanelConstructor(final CategoryPanel p0, final CategoryButton p1, final float p2, final float p3);
    
    public abstract void categoryPanelMouseClicked(final CategoryPanel p0, final int p1, final int p2, final int p3);
    
    public abstract void categoryPanelDraw(final CategoryPanel p0, final float p1, final float p2);
    
    public abstract void categoryPanelMouseMovedOrUp(final CategoryPanel p0, final int p1, final int p2, final int p3);
    
    public abstract void groupBoxConstructor(final GroupBox p0, final float p1, final float p2);
    
    public abstract void groupBoxMouseClicked(final GroupBox p0, final int p1, final int p2, final int p3);
    
    public abstract void groupBoxDraw(final GroupBox p0, final float p1, final float p2);
    
    public abstract void groupBoxMouseMovedOrUp(final GroupBox p0, final int p1, final int p2, final int p3);
    
    public abstract void buttonContructor(final Button p0, final CategoryPanel p1);
    
    public abstract void buttonMouseClicked(final Button p0, final int p1, final int p2, final int p3, final CategoryPanel p4);
    
    public abstract void buttonDraw(final Button p0, final float p1, final float p2, final CategoryPanel p3);
    
    public abstract void buttonKeyPressed(final Button p0, final int p1);
    
    public abstract void checkBoxMouseClicked(final Checkbox p0, final int p1, final int p2, final int p3, final CategoryPanel p4);
    
    public abstract void checkBoxDraw(final Checkbox p0, final float p1, final float p2, final CategoryPanel p3);
    
    public abstract void dropDownContructor(final DropdownBox p0, final float p1, final float p2, final CategoryPanel p3);
    
    public abstract void dropDownMouseClicked(final DropdownBox p0, final int p1, final int p2, final int p3, final CategoryPanel p4);
    
    public abstract void dropDownDraw(final DropdownBox p0, final float p1, final float p2, final CategoryPanel p3);
    
    public abstract void dropDownButtonMouseClicked(final DropdownButton p0, final DropdownBox p1, final int p2, final int p3, final int p4);
    
    public abstract void dropDownButtonDraw(final DropdownButton p0, final DropdownBox p1, final float p2, final float p3);
    
    public abstract void SliderContructor(final Slider p0, final CategoryPanel p1);
    
    public abstract void SliderMouseClicked(final Slider p0, final int p1, final int p2, final int p3, final CategoryPanel p4);
    
    public abstract void SliderMouseMovedOrUp(final Slider p0, final int p1, final int p2, final int p3, final CategoryPanel p4);
    
    public abstract void SliderDraw(final Slider p0, final float p1, final float p2, final CategoryPanel p3);
    
    public abstract void categoryButtonMouseReleased(final CategoryButton p0, final int p1, final int p2, final int p3);
    
    public abstract void slButtonDraw(final SLButton p0, final float p1, final float p2, final MainPanel p3);
    
    public abstract void slButtonMouseClicked(final SLButton p0, final float p1, final float p2, final int p3, final MainPanel p4);
    
    public abstract void colorConstructor(final ColorPreview p0, final float p1, final float p2);
    
    public abstract void colorPrewviewDraw(final ColorPreview p0, final float p1, final float p2);
    
    public abstract void rgbSliderDraw(final RGBSlider p0, final float p1, final float p2);
    
    public abstract void rgbSliderClick(final RGBSlider p0, final float p1, final float p2, final int p3);
    
    public abstract void rgbSliderMovedOrUp(final RGBSlider p0, final float p1, final float p2, final int p3);
}
