package summer.cheat.guiutil;


import java.util.ArrayList;
import java.util.function.Supplier;

import summer.base.manager.config.Cheats;


//Deine Imports

/**
 * Made by HeroCode
 * it's free to use
 * but you have to credit me
 *
 * @author HeroCode
 */
public class Setting {

    private String name;
    private Cheats parent;
    public String mode;

    private String sval;
    private ArrayList<String> options;

    private boolean bval;
    private double dval;
    private double min;
    private double max;
    private boolean onlyint = false;
    public boolean percentage;
    private boolean comboExpand;
    private Supplier<Boolean> visibility;

    public Setting(String name, Cheats parent, String sval, ArrayList<String> options) {
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.comboExpand = false;
        this.mode = "Combo";
        this.visibility = () -> true;
    }

    public Setting(String name, Cheats parent, String sval, ArrayList<String> options, Supplier<Boolean> visibility) {
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.comboExpand = false;
        this.mode = "Combo";
        this.visibility = visibility;
    }

    public Setting(String name, Cheats parent, boolean bval) {
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
        this.visibility = () -> true;
    }

    public Setting(String name, Cheats parent, boolean bval, Supplier<Boolean> visibility) {
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
        this.visibility = visibility;
    }

    public Setting(String name, Cheats parent, double dval, double min, double max, boolean onlyint) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.visibility = () -> true;
    }

    public Setting(String name, Cheats parent, double dval, double min, double max, boolean onlyint, Supplier<Boolean> visibility) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.visibility = visibility;
    }

    public Setting(String name, Cheats parent, double dval, double min, double max, boolean onlyint, boolean perc) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.percentage = perc;
        this.visibility = () -> true;
    }

    public Setting(String name, Cheats parent, double dval, double min, double max, boolean onlyint, boolean perc, Supplier<Boolean> visibility) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
        this.percentage = perc;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public boolean isVisible() {
        return visibility.get();
    }

    public Cheats getParentMod() {
        return parent;
    }

    public String getValString() {
        return this.sval;
    }

    public void setValString(String in) {
        this.sval = in;

    }

    public void setValStringNoSave(String in) {
        this.sval = in;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public boolean getValBoolean() {
        return this.bval;
    }

    public void setValBoolean(boolean in) {
        this.bval = in;
    }

    public void setValBooleanNoSave(boolean in) {
        this.bval = in;
    }

    public double getValDouble() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return this.dval;
    }

    public void setValDouble(double in) {
        this.dval = in;

    }

    public void setValDoubleNoSave(double in) {
        this.dval = in;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public float getValFloat() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return (float) this.dval;
    }

    public long getValLong() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return (long) this.dval;
    }

    public int getValInt() {
        if (this.onlyint) {
            this.dval = (int) dval;
        }
        return (int) this.dval;
    }


    public boolean isCombo() {
        return this.mode.equalsIgnoreCase("Combo") ? true : false;
    }

    public boolean isCheck() {
        return this.mode.equalsIgnoreCase("Check") ? true : false;
    }

    public boolean isSlider() {
        return this.mode.equalsIgnoreCase("Slider") ? true : false;
    }

    public boolean onlyInt() {
        return this.onlyint;
    }

    public boolean isComboExpand() {
        return comboExpand;
    }

    public void setComboExpand(boolean comboExpand) {
        this.comboExpand = comboExpand;
    }

    public String getMode() {
        return this.sval;
    }
}


