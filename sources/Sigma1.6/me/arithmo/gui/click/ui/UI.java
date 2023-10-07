/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.gui.click.ui;

import me.arithmo.gui.click.ClickGui;
import me.arithmo.gui.click.components.Button;
import me.arithmo.gui.click.components.CategoryButton;
import me.arithmo.gui.click.components.CategoryPanel;
import me.arithmo.gui.click.components.Checkbox;
import me.arithmo.gui.click.components.ColorPreview;
import me.arithmo.gui.click.components.DropdownBox;
import me.arithmo.gui.click.components.DropdownButton;
import me.arithmo.gui.click.components.MainPanel;
import me.arithmo.gui.click.components.RGBSlider;
import me.arithmo.gui.click.components.SLButton;
import me.arithmo.gui.click.components.Slider;
import net.minecraft.client.Minecraft;

public abstract class UI {
    public Minecraft g = Minecraft.getMinecraft();

    public abstract void mainConstructor(ClickGui var1, MainPanel var2);

    public abstract void onClose();

    public abstract void mainPanelDraw(MainPanel var1, int var2, int var3);

    public abstract void mainPanelKeyPress(MainPanel var1, int var2);

    public abstract void panelConstructor(MainPanel var1, float var2, float var3);

    public abstract void panelMouseClicked(MainPanel var1, int var2, int var3, int var4);

    public abstract void panelMouseMovedOrUp(MainPanel var1, int var2, int var3, int var4);

    public abstract void categoryButtonConstructor(CategoryButton var1, MainPanel var2);

    public abstract void categoryButtonMouseClicked(CategoryButton var1, MainPanel var2, int var3, int var4, int var5);

    public abstract void categoryButtonDraw(CategoryButton var1, float var2, float var3);

    public abstract void categoryPanelConstructor(CategoryPanel var1, CategoryButton var2, float var3, float var4);

    public abstract void categoryPanelMouseClicked(CategoryPanel var1, int var2, int var3, int var4);

    public abstract void categoryPanelDraw(CategoryPanel var1, float var2, float var3);

    public abstract void categoryPanelMouseMovedOrUp(CategoryPanel var1, int var2, int var3, int var4);

    public abstract void buttonContructor(Button var1, CategoryPanel var2);

    public abstract void buttonMouseClicked(Button var1, int var2, int var3, int var4, CategoryPanel var5);

    public abstract void buttonDraw(Button var1, float var2, float var3, CategoryPanel var4);

    public abstract void buttonKeyPressed(Button var1, int var2);

    public abstract void checkBoxMouseClicked(Checkbox var1, int var2, int var3, int var4, CategoryPanel var5);

    public abstract void checkBoxDraw(Checkbox var1, float var2, float var3, CategoryPanel var4);

    public abstract void dropDownContructor(DropdownBox var1, float var2, float var3, CategoryPanel var4);

    public abstract void dropDownMouseClicked(DropdownBox var1, int var2, int var3, int var4, CategoryPanel var5);

    public abstract void dropDownDraw(DropdownBox var1, float var2, float var3, CategoryPanel var4);

    public abstract void dropDownButtonMouseClicked(DropdownButton var1, DropdownBox var2, int var3, int var4, int var5);

    public abstract void dropDownButtonDraw(DropdownButton var1, DropdownBox var2, float var3, float var4);

    public abstract void SliderContructor(Slider var1, CategoryPanel var2);

    public abstract void SliderMouseClicked(Slider var1, int var2, int var3, int var4, CategoryPanel var5);

    public abstract void SliderMouseMovedOrUp(Slider var1, int var2, int var3, int var4, CategoryPanel var5);

    public abstract void SliderDraw(Slider var1, float var2, float var3, CategoryPanel var4);

    public abstract void categoryButtonMouseReleased(CategoryButton var1, int var2, int var3, int var4);

    public abstract void slButtonDraw(SLButton var1, float var2, float var3, MainPanel var4);

    public abstract void slButtonMouseClicked(SLButton var1, float var2, float var3, int var4, MainPanel var5);

    public abstract void colorConstructor(ColorPreview var1, float var2, float var3);

    public abstract void colorPrewviewDraw(ColorPreview var1, float var2, float var3);

    public abstract void rgbSliderDraw(RGBSlider var1, float var2, float var3);

    public abstract void rgbSliderClick(RGBSlider var1, float var2, float var3, int var4);

    public abstract void rgbSliderMovedOrUp(RGBSlider var1, float var2, float var3, int var4);
}

