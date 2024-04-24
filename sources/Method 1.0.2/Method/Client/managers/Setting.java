package Method.Client.managers;

import Method.Client.module.Module;
import Method.Client.utils.Screens.SubGui;
import Method.Client.utils.visual.ColorUtils;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Setting implements Serializable {

    private String name;
    private Module parent;
    private final SettingType mode;
    private ArrayList<String> options;
    private GuiScreen screen;
    private SubGui subGui;
    private Setting Dependant = null;
    private boolean onlyint = false;


    private String selected;

    private String sval; // Selected String value for combo
    private boolean bval; // Selected Boolean value for switch
    private double dval;  //double value for slider
    private double min;  // min slider
    private double max;  // max slider

    private double saval; //color saturation
    private double brval;  // color brightness
    private double Alval; // color alpha


    public void setall(Setting inputsetting) {
        this.selected = inputsetting.getselected();
        this.sval = inputsetting.getValString();
        this.dval = inputsetting.getValDouble();
        this.bval = inputsetting.getValBoolean();
        this.min = inputsetting.getMin();
        this.max = inputsetting.getMax();
        this.saval = inputsetting.getSat();
        this.brval = inputsetting.getBri();
        this.Alval = inputsetting.getAlpha();
    }

    public Setting(Setting setting) {
        this.name = setting.getName();
        this.parent = setting.getParentMod();
        this.mode = setting.getMode();
        this.options = setting.getOptions();
        this.screen = setting.getScreen();
        this.Dependant = setting.getDependant();
        this.onlyint = setting.onlyint;
        this.selected = setting.getselected();

        this.dval = setting.getValDouble();
        this.min = setting.getMin();
        this.max = setting.getMax();
        this.saval = setting.getSat();
        this.brval = setting.getBri();
        this.Alval = setting.getAlpha();
        this.Dependant = setting.getDependant();

        this.sval = setting.getValString();
        this.bval = setting.getValBoolean();
    }

    // Standard combo a,b,c
    public Setting(String name, Module parent, String sval, ArrayList<String> options) {
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.mode = SettingType.Combo;
    }

    // Standard combo a,b,c
    public Setting(String name, Module parent, String sval, String... modes) {
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = new ArrayList<>(Arrays.asList(modes));
        this.mode = SettingType.Combo;
    }

    // Standard check On,Off
    public Setting(String name, Module parent, boolean bval) {
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = SettingType.Check;
    }

    // Gui toggle
    public Setting(String name, Module parent, GuiScreen screen) {
        this.name = name;
        this.parent = parent;
        this.screen = screen;
        this.mode = SettingType.Screen;
    }

    // SubGui toggle
    public Setting(String name, Module parent, SubGui subGui) {
        this.name = name;
        this.parent = parent;
        this.subGui = subGui;
        this.mode = SettingType.SubGui;
    }

    // Standard Slider 1-20
    public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = SettingType.Slider;
    }

    // Standard Color RED
    public Setting(String name, Module parent, double HUE, double Saturation, double Brightness, double Alpha) {
        this.name = name;
        this.parent = parent;
        this.dval = HUE;  // HUE
        this.min = 0;   // Contained min is reached Rainbow = true;
        this.max = 1;
        this.saval = Saturation; //Saturation
        this.brval = Brightness; //Brightness
        this.Alval = Alpha; //Alpha
        this.mode = SettingType.Color;
    }


    ///
    /// Start of Dependent Settings
    ///

    // Standard Color Dependant
    public Setting(String name, Module parent, double HUE, double Saturation, double Brightness, double Alpha, Setting dependant) {
        this.name = name;
        this.parent = parent;
        this.dval = HUE;  // HUE
        this.min = 0;   // Contained min is reached Rainbow = true;
        this.max = 1;
        this.saval = Saturation; //Saturation
        this.brval = Brightness; //Brightness
        this.Alval = Alpha; //Alpha
        this.Dependant = dependant;
        this.mode = SettingType.Color;
    }

    public Setting(String name, Module parent, double HUE, double Saturation, double Brightness, double Alpha, Setting dependant, String selected) {
        this.name = name;
        this.parent = parent;
        this.dval = HUE;  // HUE
        this.min = 0;   // Contained min is reached Rainbow = true;
        this.max = 1;
        this.saval = Saturation; //Saturation
        this.brval = Brightness; //Brightness
        this.Alval = Alpha; //Alpha
        this.Dependant = dependant;
        this.selected = selected;
        this.mode = SettingType.Color;
    }

    // Option for dependant slider!
    public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint, Setting Dependant) {
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.Dependant = Dependant;
        this.onlyint = onlyint;
        this.mode = SettingType.Slider;
    }

    public Setting(String name, Module parent, double dval, double min, double max, boolean onlyint, Setting Dependant, String selected) {
        this.name = name;
        this.parent = parent;
        this.Dependant = Dependant;
        this.selected = selected;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = SettingType.Slider;
    }


    // Option for dependant boolean
    public Setting(String name, Module parent, boolean bval, Setting Dependant) {
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.Dependant = Dependant;
        this.mode = SettingType.Check;
    }

    // Option for dependant bool for string
    public Setting(String name, Module parent, boolean bval, Setting Dependant, String selected) {
        this.name = name;
        this.parent = parent;
        this.selected = selected;
        this.bval = bval;
        this.Dependant = Dependant;
        this.mode = SettingType.Check;
    }

    public String getName() {
        return name;
    }

    public void setName(String nename) {
        this.name = nename;
    }

    public Module getParentMod() {
        return parent;
    }

    public void setParent(Module parent) {
        this.parent = parent;
    }

    public String getValString() {
        return this.sval;
    }

    public void setValString(String in) {
        this.sval = in;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    public String getTooltip() {
        return "CTRL + Click Repeat Exact Input";
    }

    public boolean getValBoolean() {
        return this.bval;
    }

    public void setValBoolean(boolean in) {
        this.bval = in;
    }

    public double getValDouble() {
        if (this.onlyint)
            this.dval = (int) dval;
        return this.dval;
    }

    public double getSat() {
        return this.saval;
    }

    public double getBri() {
        return this.brval;
    }

    public double getAlpha() {
        return this.Alval;
    }

    public int getcolor() {
        double saturation = saval; //saturation
        double brightness = brval; //brightness
        if (dval == 0)
            return ColorUtils.rainbow(saturation, brightness, Alval);

        int rgba = Color.HSBtoRGB((float) dval, (float) saturation, (float) brightness);
        float red = (rgba >> 16 & 0xFF) / 255.0F;
        float green = (rgba >> 8 & 0xFF) / 255.0F;
        float blue = (rgba & 0xFF) / 255.0F;

        Color c = new Color(red, green, blue, (float) Alval);
        return c.getRGB();
    }


    public void setDependant(Setting dependant) {
        Dependant = dependant;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setValDouble(double in) {
        this.dval = in;
    }

    public void setScreen(GuiScreen in) {
        this.screen = in;
    }

    public void setsaturation(float in) {
        this.saval = in;
    }

    public void setbrightness(float in) {
        this.brval = in;
    }

    public void setAlpha(float in) {
        this.Alval = in;
    }

    public double getMin() {
        return this.min;
    }

    public GuiScreen getScreen() {
        return this.screen;
    }

    public SubGui getSubGui() {
        return this.subGui;
    }

    public SettingType getMode() {
        return this.mode;
    }

    public Setting getDependant() {
        return this.Dependant;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getselected() {
        return this.selected;
    }

    public double getMax() {
        return this.max;
    }

    public boolean isCombo() {
        return this.mode.equals(SettingType.Combo);
    }

    public boolean isCheck() {
        return this.mode.equals(SettingType.Check);
    }

    public boolean isSlider() {
        return this.mode.equals(SettingType.Slider);
    }

    public boolean isGui() {
        return this.mode.equals(SettingType.Screen);
    }

    public boolean isSub() {
        return this.mode.equals(SettingType.SubGui);
    }

    public boolean isColor() {
        return this.mode.equals(SettingType.Color);
    }

    public void setOnlyint(boolean onlyint) {
        this.onlyint = onlyint;
    }

    public boolean onlyInt() {
        return this.onlyint;
    }

    public enum SettingType implements Serializable {
        Combo, Check,
        Slider, Screen,
        Color, SubGui
    }

}
