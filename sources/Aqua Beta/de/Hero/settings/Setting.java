package de.Hero.settings;

import de.Hero.settings.GuiColorChooser;
import de.Hero.settings.GuiColorChooser2;
import de.Hero.settings.Setting;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.fr.lavache.anime.Animate;
import intent.AquaDev.aqua.fr.lavache.anime.Easing;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.modules.visual.Arraylist;
import intent.AquaDev.aqua.modules.visual.Blur;
import intent.AquaDev.aqua.modules.visual.Shadow;
import intent.AquaDev.aqua.utils.ColorUtil;
import intent.AquaDev.aqua.utils.RenderUtil;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;

public class Setting {
    double deltaXmouse;
    double deltaYmouse = 0.0;
    private final String name;
    private final String displayName;
    private final Module module;
    public Type type;
    public boolean state;
    public double currentNumber;
    public double min;
    public double max;
    public boolean isInt;
    public int color;
    private String currentMode;
    public String[] modes;
    private boolean visible = true;
    private int modX;
    private int modY;
    private int modWidth;
    private int modHeight;
    private boolean dragging;
    private int mouseX;
    private int mouseY;
    private boolean comboExtended;
    private boolean hovered;
    private int comboHoverIndex;
    private float sliderMinX;
    private float sliderMaxX;
    private double boxHeight;
    private double colorPickerHeight;
    public static GuiColorChooser colorChooser;
    public static GuiColorChooser2 colorChooser2;
    Animate anim = new Animate();
    Animate anim2 = new Animate();

    public Setting(String name, Module module, String currentMode, String[] modes) {
        this.name = module.getName() + name;
        this.displayName = name;
        this.module = module;
        this.currentMode = currentMode;
        this.type = Type.STRING;
        this.modes = modes;
    }

    public Setting(String name, Module module) {
        colorChooser = new GuiColorChooser(this.modX, this.modY + 10);
        colorChooser2 = new GuiColorChooser2(this.modX, this.modY + 10);
        this.name = module.getName() + name;
        this.displayName = name;
        this.module = module;
        this.type = Type.COLOR;
    }

    public Setting(String name, Module module, boolean state) {
        this.name = module.getName() + name;
        this.displayName = name;
        this.module = module;
        this.state = state;
        this.type = Type.BOOLEAN;
    }

    public Setting(String name, Module module, double currentNumber, double min, double max, boolean isInt) {
        this.name = module.getName() + name;
        this.displayName = name;
        this.module = module;
        this.currentNumber = currentNumber;
        this.min = min;
        this.max = max;
        this.isInt = isInt;
        this.type = Type.NUMBER;
    }

    public Object getValue() {
        switch (type) {
            case BOOLEAN: {
                return this.state;
            }
            case NUMBER: {
                return this.currentNumber;
            }
            case STRING: {
                return this.currentMode;
            }
            case COLOR: {
                return this.color;
            }
        }
        return null;
    }

    public double getCurrentNumber() {
        return this.isInt ? (this.currentNumber = (double)((int)this.currentNumber)) : this.currentNumber;
    }

    public int getHeight() {
        if (type == Type.COLOR)
            return 85;
        return 15;
    }

    public void drawSettingOwn(int modX, int modY, int modWidth, int modHeight, int backGroundColor, int activatedTextColor) {
        this.modX = modX;
        this.modY = modY;
        this.modWidth = modWidth;
        this.modHeight = modHeight;
        String camelCase = this.displayName.substring(0, 1).toUpperCase() + this.getDisplayName().substring(1);
        switch (type) {
            case BOOLEAN: {
                Aqua.INSTANCE.comfortaa4.drawString(this.getDisplayName(), (float)(modX + 2), (float)modY, Color.gray.getRGB());
                Gui.drawRect((int)(modX + modWidth - 15), (int)(modY + modHeight / 2 - 2), (int)(modX + modWidth - 4), (int)(modY + modHeight / 2 + 2), (int)activatedTextColor);
                if (this.state) {
                    Gui.drawRect((int)(modX + modWidth - 9), (int)(modY + modHeight / 2 - 4), (int)(modX + modWidth - 5), (int)(modY + modHeight / 2 + 4), (int)Color.white.getRGB());
                    break;
                }
                Gui.drawRect((int)(modX + modWidth - 14), (int)(modY + modHeight / 2 - 4), (int)(modX + modWidth - 10), (int)(modY + modHeight / 2 + 4), (int)Color.white.getRGB());
                break;
            }
            case NUMBER: {
                if (!Mouse.isButtonDown((int)0)) {
                    this.dragging = false;
                } else if (this.isSettingHovered(this.mouseX, this.mouseY)) {
                    this.dragging = true;
                }
                String displayval = "" + (double)Math.round((double)(this.getCurrentNumber() * 100.0)) / 100.0;
                Color temp = ColorUtil.getClickGUIColor();
                int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), this.dragging ? 250 : 200).getRGB();
                int color2 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), this.dragging ? 255 : 230).getRGB();
                double percentBar = (this.getCurrentNumber() - this.getMin()) / (this.getMax() - this.getMin());
                Aqua.INSTANCE.comfortaa4.drawString(camelCase, (float)(modX + 1), (float)(modY + 2), -1);
                Aqua.INSTANCE.comfortaa4.drawString(displayval, (float)(modX + modWidth - Aqua.INSTANCE.comfortaa4.getStringWidth(displayval) - 3), (float)(modY + 2), -1);
                Gui.drawRect((int)modX, (int)(modY + 12), (int)(modX + modWidth), (int)((int)((double)modY + 14.5)), (int)Color.darkGray.getRGB());
                Gui.drawRect((int)modX, (int)(modY + 12), (int)((int)((double)modX + percentBar * (double)modWidth)), (int)((int)((double)modY + 14.5)), (int)activatedTextColor);
                if (!(percentBar > 0.0) || percentBar < 1.0) {
                    // empty if block
                }
                if (!this.dragging) break;
                double diff = this.getMax() - this.getMin();
                double val = this.getMin() + MathHelper.clamp_double((double)((double)(this.mouseX - this.getModX()) / (double)this.getModWidth()), (double)0.0, (double)1.0) * diff;
                this.setCurrentNumber(val);
                break;
            }
            case STRING: {
                Color temp = ColorUtil.getClickGUIColor();
                int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
                Aqua.INSTANCE.comfortaa4.drawCenteredString(camelCase, (float)(modX + modWidth / 2), (float)(modY + 7), -1);
                if (!this.comboExtended) break;
                double ay = modY + 15;
                for (String sld : this.getModes()) {
                    String elementtitle = sld.substring(0, 1).toUpperCase() + sld.substring(1);
                    int n = (int)ay;
                    Aqua.INSTANCE.comfortaa4.getClass();
                    Gui.drawRect((int)(modX + 5), (int)n, (int)(modX + 91), (int)((int)(ay + 9.0 + 2.0)), (int)new Color(0, 0, 0, 60).getRGB());
                    if (sld.equalsIgnoreCase(this.getCurrentMode())) {
                        int n2 = (int)ay;
                        Aqua.INSTANCE.comfortaa4.getClass();
                        Gui.drawRect((int)(modX + 5), (int)n2, (int)(modX + 91), (int)((int)(ay + 9.0 + 2.0)), (int)activatedTextColor);
                    }
                    Aqua.INSTANCE.comfortaa4.drawCenteredString(elementtitle, (float)(modX + modWidth / 2), (float)(ay + 2.0), -1);
                    if (this.mouseX >= modX && this.mouseX <= modX + modWidth && (double)this.mouseY >= ay) {
                        double d = this.mouseY;
                        Aqua.INSTANCE.comfortaa4.getClass();
                        if (d < ay + 9.0 + 2.0) {
                            int n3 = (int)((double)(modX + modWidth) - 1.2);
                            int n4 = (int)ay;
                            Aqua.INSTANCE.comfortaa4.getClass();
                            Gui.drawRect((int)n3, (int)n4, (int)(modX + modWidth), (int)((int)(ay + 9.0 + 2.0)), (int)new Color(0, 0, 0, 0).getRGB());
                        }
                    }
                    Aqua.INSTANCE.comfortaa4.getClass();
                    ay += (double)(9 + 2);
                }
                this.boxHeight = ay - (double)modY - 15.0;
                break;
            }
            case COLOR: {
                Setting.colorChooser2.x = this.modX;
                Setting.colorChooser2.y = this.modY - 65;
                modHeight = 10 + colorChooser2.getHeight();
                colorChooser2.draw(this.mouseX, this.mouseY);
                this.color = Setting.colorChooser2.color;
                colorChooser2.setWidth(this.modWidth);
            }
        }
    }

    public void drawSetting(int modX, int modY, int modWidth, int modHeight, int backGroundColor, int activatedTextColor) {
        this.modX = modX;
        this.modY = modY;
        this.modWidth = modWidth;
        this.modHeight = modHeight;
        String camelCase = this.getDisplayName().substring(0, 1).toUpperCase() + this.getDisplayName().substring(1);
        switch (type) {
            case BOOLEAN: {
                int finalModHeight2;
                int finalModY5;
                int finalModY4;
                if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                    finalModY4 = modY;
                    finalModY5 = modY;
                    finalModHeight2 = modHeight;
                    Blur.drawBlurred(() -> Gui.drawRect((int)modX, (int)finalModY4, (int)(modX + modWidth), (int)(finalModY5 + finalModHeight2), (int)backGroundColor), (boolean)false);
                }
                if (Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                    finalModY4 = modY;
                    finalModY5 = modY;
                    finalModHeight2 = modHeight;
                    Shadow.drawGlow(() -> Gui.drawRect((int)modX, (int)finalModY4, (int)(modX + modWidth), (int)(finalModY5 + finalModHeight2), (int)Color.black.getRGB()), (boolean)false);
                }
                Gui.drawRect((int)modX, (int)modY, (int)(modX + modWidth), (int)(modY + modHeight), (int)backGroundColor);
                Aqua.INSTANCE.comfortaa4.drawString(this.getDisplayName(), (float)(modX + 2), (float)modY, Color.gray.getRGB());
                if (Aqua.setmgr.getSetting("GUIBooleanMode").getCurrentMode().equalsIgnoreCase("Novoline")) {
                    if (this.state) {
                        // empty if block
                    }
                    if (this.state) {
                        Gui.drawRect((int)(modX + modWidth - 15), (int)(modY + modHeight / 2 - 2), (int)(modX + modWidth - 3), (int)(modY + modHeight / 2 + 2), (int)activatedTextColor);
                        this.anim.setEase(Easing.CUBIC_OUT).setMin(0.0f).setMax(7.0f).setSpeed(15.0f).setReversed(false).update();
                        Gui.drawRect2((double)((float)(modX + modWidth - 15) + this.anim.getValue()), (double)((float)modY + (float)modHeight / 2.0f - 5.0f), (double)((float)(modX + modWidth - 11) + this.anim.getValue()), (double)((float)modY + (float)modHeight / 2.0f + 4.0f), (int)Color.white.getRGB());
                        this.anim2.reset();
                    } else {
                        this.anim.reset();
                        this.anim2.setEase(Easing.CUBIC_OUT).setMin(0.0f).setMax(7.0f).setSpeed(15.0f).setReversed(false).update();
                        Gui.drawRect((int)(modX + modWidth - 15), (int)(modY + modHeight / 2 - 2), (int)(modX + modWidth - 3), (int)(modY + modHeight / 2 + 2), (int)Color.gray.getRGB());
                        Gui.drawRect2((double)((float)(modX + modWidth - 7) - this.anim2.getValue()), (double)((float)modY + (float)modHeight / 2.0f - 4.0f), (double)((float)(modX + modWidth - 3) - this.anim2.getValue()), (double)((float)modY + (float)modHeight / 2.0f + 4.0f), (int)Color.white.getRGB());
                    }
                }
                if (Aqua.setmgr.getSetting("GUIBooleanMode").getCurrentMode().equalsIgnoreCase("Rounded")) {
                    if (this.state) {
                        this.anim.setEase(Easing.CUBIC_OUT).setMin(0.0f).setMax(7.0f).setSpeed(15.0f).setReversed(false).update();
                        RenderUtil.drawRoundedRect2Alpha((double)(modX + modWidth - 15), (double)(modY + modHeight / 2 - 6), (double)(this.anim.getValue() + 6.0f), (double)8.0, (double)1.0, (Color)new Color(activatedTextColor));
                    } else {
                        this.anim.reset();
                        RenderUtil.drawRoundedRect2Alpha((double)(modX + modWidth - 15), (double)(modY + modHeight / 2 - 6), (double)13.0, (double)8.0, (double)1.0, (Color)Color.darkGray);
                    }
                }
                if (!Aqua.setmgr.getSetting("GUIBooleanMode").getCurrentMode().equalsIgnoreCase("Square")) break;
                if (this.state) {
                    this.anim.setEase(Easing.CUBIC_OUT).setMin(0.0f).setMax(7.0f).setSpeed(15.0f).setReversed(false).update();
                    Gui.drawRect2((double)(modX + modWidth - 14), (double)((float)modY + (float)modHeight / 2.0f - 6.0f), (double)((float)(modX + modWidth - 11) + this.anim.getValue()), (double)((float)modY + (float)modHeight / 2.0f + 2.0f), (int)activatedTextColor);
                    break;
                }
                this.anim.reset();
                Gui.drawRect((int)(modX + modWidth - 14), (int)(modY + modHeight / 2 - 6), (int)(modX + modWidth - 4), (int)(modY + modHeight / 2 + 2), (int)Color.darkGray.getRGB());
                break;
            }
            case 2: {
                if (!Mouse.isButtonDown((int)0)) {
                    this.dragging = false;
                } else if (this.isSettingHovered(this.mouseX, this.mouseY)) {
                    this.dragging = true;
                }
                String displayval = "" + (double)Math.round((double)(this.getCurrentNumber() * 100.0)) / 100.0;
                Color temp = ColorUtil.getClickGUIColor();
                int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), this.dragging ? 250 : 200).getRGB();
                int color2 = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), this.dragging ? 255 : 230).getRGB();
                double percentBar = (this.getCurrentNumber() - this.getMin()) / (this.getMax() - this.getMin());
                int finalModY = modY;
                int finalModY1 = modY;
                int finalModHeight = modHeight;
                if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                    Blur.drawBlurred(() -> Gui.drawRect((int)modX, (int)finalModY, (int)(modX + modWidth), (int)(finalModY1 + finalModHeight), (int)backGroundColor), (boolean)false);
                }
                if (Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                    int finalModY6 = modY;
                    int finalModY7 = modY;
                    int finalModHeight3 = modHeight;
                    Shadow.drawGlow(() -> Gui.drawRect((int)modX, (int)finalModY6, (int)(modX + modWidth), (int)(finalModY7 + finalModHeight3), (int)Color.black.getRGB()), (boolean)false);
                }
                Gui.drawRect((int)modX, (int)modY, (int)(modX + modWidth), (int)(modY + modHeight), (int)backGroundColor);
                Aqua.INSTANCE.comfortaa4.drawString(camelCase, (float)(modX + 1), (float)(modY + 2), -1);
                Aqua.INSTANCE.comfortaa4.drawString(displayval, (float)(modX + modWidth - Aqua.INSTANCE.comfortaa4.getStringWidth(displayval) - 3), (float)(modY + 2), -1);
                Gui.drawRect((int)modX, (int)(modY + 12), (int)(modX + modWidth), (int)((int)((double)modY + 14.5)), (int)Color.darkGray.getRGB());
                Gui.drawRect2((double)modX, (double)(modY + 12), (double)((int)((double)modX + percentBar * (double)modWidth)), (double)((int)((double)modY + 14.5)), (int)activatedTextColor);
                if (!(percentBar > 0.0) || percentBar < 1.0) {
                    // empty if block
                }
                if (!this.dragging) break;
                double diff = this.getMax() - this.getMin();
                double val = this.getMin() + MathHelper.clamp_double((double)((double)(this.mouseX - this.getModX()) / (double)this.getModWidth()), (double)0.0, (double)1.0) * diff;
                this.setCurrentNumber(val);
                break;
            }
            case 3: {
                int finalModHeight1;
                int finalModY3;
                int finalModY2;
                Color temp = ColorUtil.getClickGUIColor();
                int color = new Color(temp.getRed(), temp.getGreen(), temp.getBlue(), 150).getRGB();
                if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                    finalModY2 = modY;
                    finalModY3 = modY;
                    finalModHeight1 = modHeight;
                    Blur.drawBlurred(() -> Gui.drawRect((int)modX, (int)finalModY2, (int)(modX + modWidth), (int)(finalModY3 + finalModHeight1), (int)backGroundColor), (boolean)false);
                }
                if (Aqua.moduleManager.getModuleByName("Shadow").isToggled()) {
                    finalModY2 = modY;
                    finalModY3 = modY;
                    finalModHeight1 = modHeight;
                    Shadow.drawGlow(() -> Gui.drawRect((int)modX, (int)finalModY2, (int)(modX + modWidth), (int)(finalModY3 + finalModHeight1), (int)Color.black.getRGB()), (boolean)false);
                }
                Gui.drawRect((int)modX, (int)modY, (int)(modX + modWidth), (int)(modY + modHeight), (int)backGroundColor);
                int shift = 6;
                Aqua.INSTANCE.comfortaa4.drawCenteredString(camelCase, (float)modX + (float)modWidth / 2.0f, (float)(modY -= shift) + 7.5f, -1);
                if (this.comboExtended) {
                    double ay;
                    this.anim.setEase(Easing.CUBIC_OUT).setMin(0.0f).setMax(7.0f).setSpeed(15.0f).setReversed(false).update();
                    double a2 = ay = (double)(modY + 15);
                    for (String sld : this.getModes()) {
                        String elementtitle = sld.substring(0, 1).toUpperCase() + sld.substring(1);
                        int n = (int)ay + shift;
                        Aqua.INSTANCE.comfortaa4.getClass();
                        Gui.drawRect((int)modX, (int)n, (int)(modX + 96), (int)((int)(ay + 9.0 + 2.0) + shift), (int)backGroundColor);
                        if (Aqua.moduleManager.getModuleByName("Blur").isToggled()) {
                            double finalAy = ay;
                            Blur.drawBlurred(() -> {
                                int n = (int)finalAy + 2;
                                Aqua.INSTANCE.comfortaa4.getClass();
                                Gui.drawRect((int)(modX + 5), (int)n, (int)(modX + 91), (int)((int)(finalAy + (double)(9 * 2) - 7.0)), (int)new Color(0, 0, 0, 100).getRGB());
                            }, (boolean)false);
                        }
                        int n2 = (int)ay;
                        Aqua.INSTANCE.comfortaa4.getClass();
                        Gui.drawRect((int)(modX + 5), (int)n2, (int)(modX + 91), (int)((int)(ay + (double)(9 * 2) - 7.0)), (int)new Color(0, 0, 0, 100).getRGB());
                        if (sld.equalsIgnoreCase(this.getCurrentMode())) {
                            int n3 = (int)ay + 2;
                            Aqua.INSTANCE.comfortaa4.getClass();
                            Gui.drawRect((int)(modX + 5), (int)n3, (int)(modX + 91), (int)((int)(ay + 9.0 + 2.0)), (int)activatedTextColor);
                        }
                        Aqua.INSTANCE.comfortaa4.drawCenteredString(elementtitle, (float)(modX + modWidth / 2), (float)(ay + 2.0), -1);
                        if (this.mouseX >= modX && this.mouseX <= modX + modWidth && (double)this.mouseY >= ay) {
                            double d = this.mouseY;
                            Aqua.INSTANCE.comfortaa4.getClass();
                            if (d < ay + 9.0 + 2.0) {
                                int n4 = (int)((double)(modX + modWidth) - 1.2);
                                int n5 = (int)ay;
                                Aqua.INSTANCE.comfortaa4.getClass();
                                Gui.drawRect((int)n4, (int)n5, (int)(modX + modWidth), (int)((int)(ay + 9.0 + 2.0)), (int)new Color(0, 0, 0, 0).getRGB());
                            }
                        }
                        Aqua.INSTANCE.comfortaa4.getClass();
                        ay += (double)(9 + 2);
                    }
                    this.boxHeight = ay - (double)modY - 15.0;
                    break;
                }
                this.anim.reset();
                break;
            }
            case 4: {
                Setting.colorChooser.x = this.modX;
                Setting.colorChooser.y = this.modY - 65;
                modHeight = 10 + colorChooser.getHeight();
                if (Aqua.setmgr.getSetting("GUIColorPickerGlow").isState() && Aqua.setmgr.getSetting("GUIGlowMode").getCurrentMode().equalsIgnoreCase("AmbientLighting") && Aqua.setmgr.getSetting("GUIRoundedPicker").isState()) {
                    Arraylist.drawGlowArray(() -> colorChooser.draw(this.mouseX, this.mouseY), (boolean)false);
                }
                colorChooser.draw(this.mouseX, this.mouseY);
                this.color = Setting.colorChooser.color;
                colorChooser.setWidth(this.modWidth);
            }
        }
    }

    public void clickMouse(int mouseX, int mouseY, int mouseButton) {
        switch (1.$SwitchMap$de$Hero$settings$Setting$Type[this.type.ordinal()]) {
            case 1: {
                if (!this.isSettingHovered(mouseX, mouseY)) break;
                this.state = !this.state;
                break;
            }
            case 2: {
                break;
            }
            case 3: {
                if (this.isSettingHovered(mouseX, mouseY)) {
                    boolean bl = this.comboExtended = !this.comboExtended;
                }
                if (!this.comboExtended) break;
                double ay = this.modY + 15;
                for (String sld : this.getModes()) {
                    if (mouseX >= this.modX && mouseX <= this.modX + this.modWidth && (double)mouseY >= ay) {
                        double d = mouseY;
                        Aqua.INSTANCE.comfortaa4.getClass();
                        if (d < ay + 9.0 + 2.0) {
                            for (Setting set : Aqua.setmgr.getSettingsFromModule(this.module)) {
                                if (!this.getName().equalsIgnoreCase(set.getName())) continue;
                                this.setCurrentMode(sld);
                            }
                        }
                    }
                    Aqua.INSTANCE.comfortaa4.getClass();
                    ay += (double)(9 + 2);
                }
                break;
            }
            case 4: {
                this.color = Setting.colorChooser.color;
            }
        }
    }

    public boolean isHovered() {
        return this.hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public double getDeltaXmouse() {
        return this.deltaXmouse;
    }

    public void setDeltaXmouse(double deltaXmouse) {
        this.deltaXmouse = deltaXmouse;
    }

    public float getSliderMinX() {
        return this.sliderMinX;
    }

    public void setSliderMinX(float sliderMinX) {
        this.sliderMinX = sliderMinX;
    }

    public float getSliderMaxX() {
        return this.sliderMaxX;
    }

    public void setSliderMaxX(float sliderMaxX) {
        this.sliderMaxX = sliderMaxX;
    }

    public boolean isSettingHovered(int mouseX, int mouseY) {
        return mouseX >= this.modX && mouseX <= this.modX + this.modWidth && mouseY >= this.modY && mouseY <= this.modY + this.modHeight;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public boolean isComboExtended() {
        return this.comboExtended;
    }

    public void setComboExtended(boolean comboExtended) {
        this.comboExtended = comboExtended;
    }

    public int getComboHoverIndex() {
        return this.comboHoverIndex;
    }

    public void setComboHoverIndex(int comboHoverIndex) {
        this.comboHoverIndex = comboHoverIndex;
    }

    public double getBoxHeight() {
        return this.boxHeight;
    }

    public double getColorPickerHeight() {
        return this.colorPickerHeight;
    }

    boolean isPointInCircle(double x, double y, double pX, double pY, double radius) {
        return (pX - x) * (pX - x) + (pY - y) * (pY - y) <= radius * radius;
    }

    void setColor(Color selectedColor) {
        float[] hsb = Color.RGBtoHSB((int)selectedColor.getRed(), (int)selectedColor.getGreen(), (int)selectedColor.getBlue(), null);
        this.deltaXmouse = (double)(hsb[1] * 20.0f) * (Math.sin((double)Math.toRadians((double)(hsb[0] * 360.0f))) / Math.sin((double)Math.toRadians((double)90.0)));
        this.deltaYmouse = (double)(hsb[1] * 20.0f) * (Math.sin((double)Math.toRadians((double)(90.0f - hsb[0] * 360.0f))) / Math.sin((double)Math.toRadians((double)90.0)));
    }

    float getHue() {
        return (float)(-(Math.toDegrees((double)Math.atan2((double)this.deltaYmouse, (double)this.deltaXmouse)) + 270.0) % 360.0) / 360.0f;
    }

    float getSaturation() {
        return (float)(Math.hypot((double)this.deltaXmouse, (double)this.deltaYmouse) / 20.0);
    }

    public double getDeltaYmouse() {
        return this.deltaYmouse;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Module getModule() {
        return this.module;
    }

    public Type getType() {
        return this.type;
    }

    public boolean isState() {
        return this.state;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public boolean isInt() {
        return this.isInt;
    }

    public int getColor() {
        return this.color;
    }

    public String getCurrentMode() {
        return this.currentMode;
    }

    public String[] getModes() {
        return this.modes;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public int getModX() {
        return this.modX;
    }

    public int getModY() {
        return this.modY;
    }

    public int getModWidth() {
        return this.modWidth;
    }

    public int getModHeight() {
        return this.modHeight;
    }

    public boolean isDragging() {
        return this.dragging;
    }

    public int getMouseX() {
        return this.mouseX;
    }

    public int getMouseY() {
        return this.mouseY;
    }

    public Animate getAnim() {
        return this.anim;
    }

    public Animate getAnim2() {
        return this.anim2;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setCurrentNumber(double currentNumber) {
        this.currentNumber = currentNumber;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
