package appu26j.settings;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import appu26j.mods.Mod;

public class Setting
{
    private float minSliderValue, sliderValue, maxSliderValue, increment, index;
    private String name, typeOfSetting, modeValue, textBoxValue;
    private boolean checkBoxValue, focused, aBoolean, dragging;
    private ArrayList<String> modes;
    private Mod parentMod;
    private int[] colors;
    
    public Setting(String name, Mod parentMod, boolean checkBoxValue)
    {
        this.name = name;
        this.parentMod = parentMod;
        this.checkBoxValue = checkBoxValue;
        this.index = checkBoxValue ? 1 : 0;
        this.typeOfSetting = "Check Box";
    }
    
    public Setting(String name, Mod parentMod, String modeValue, String... modes)
    {
        this.name = name;
        this.parentMod = parentMod;
        this.modeValue = modeValue;
        this.modes = new ArrayList<String>();
        
        for (String mode : modes)
        {
            this.modes.add(mode);
        }
        
        this.typeOfSetting = "Mode";
    }
    
    public Setting(String name, Mod parentMod, float minSliderValue, float sliderValue, float maxSliderValue, float increment)
    {
        this.name = name;
        this.parentMod = parentMod;
        this.minSliderValue = minSliderValue;
        this.sliderValue = sliderValue;
        this.maxSliderValue = maxSliderValue;
        this.increment = increment;
        this.typeOfSetting = "Slider";
    }
    
    public Setting(String name, Mod parentMod, String textBoxValue)
    {
        this.name = name;
        this.parentMod = parentMod;
        this.textBoxValue = textBoxValue;
        this.focused = false;
        this.typeOfSetting = "Text Box";
    }
    
    public Setting(String name, Mod parentMod, int[] colors)
    {
        this.name = name;
        this.parentMod = parentMod;
        this.colors = colors;
        this.typeOfSetting = "Color Box";
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public Mod getParentMod()
    {
        return this.parentMod;
    }
    
    public boolean getCheckBoxValue()
    {
        return this.checkBoxValue;
    }
    
    public void setCheckBoxValue(boolean checkBoxValue)
    {
        this.checkBoxValue = checkBoxValue;
    }
    
    public boolean isFocused()
    {
    	return this.focused;
    }
    
    public void setFocused(boolean focused)
    {
    	this.focused = focused;
    }
    
    public boolean getBoolean()
    {
    	return this.aBoolean;
    }
    
    public void setBoolean(boolean aBoolean)
    {
    	this.aBoolean = aBoolean;
    }
    
    public String getModeValue()
    {
        return this.modeValue;
    }
    
    public void setModeValue(String modeValue)
    {
        this.modeValue = modeValue;
    }
    
    public ArrayList<String> getModes()
    {
        return this.modes;
    }
    
    public float getIncrement()
    {
    	return this.increment;
    }
    
    public float getMinSliderValue()
    {
        return this.minSliderValue;
    }
    
    public float getSliderValue()
    {
        return this.sliderValue;
    }
    
    public void setSliderValue(float sliderValue)
    {
        this.sliderValue = sliderValue;
        int places = String.valueOf(this.increment).length() - 2;
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.ENGLISH);
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        numberFormat.setMaximumFractionDigits(places);
        float temp = Float.parseFloat(numberFormat.format(this.sliderValue).replaceAll(",", ""));
        this.sliderValue = temp;
    }
    
    public float getMaxSliderValue()
    {
        return this.maxSliderValue;
    }
    
    public int[] getColors()
    {
    	return this.colors;
    }
    
    public void setColors(int[] colors)
    {
    	this.colors = colors;
    }
    
    public String getTypeOfSetting()
    {
        return this.typeOfSetting;
    }
    
    public String getTextBoxValue()
    {
    	return this.textBoxValue;
    }
    
    public void setTextBoxValue(String textBoxValue)
    {
    	this.textBoxValue = textBoxValue;
    }
    
    public float getIndex()
    {
        return this.index;
    }
    
    public void setIndex(float index)
    {
        this.index = index;
    }
    
    public boolean isDragging()
    {
        return this.dragging;
    }
    
    public void setDragging(boolean dragging)
    {
        this.dragging = dragging;
    }
}

