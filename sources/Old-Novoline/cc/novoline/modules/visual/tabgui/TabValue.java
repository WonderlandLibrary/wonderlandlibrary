package cc.novoline.modules.visual.tabgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;

public final class TabValue {

    private final TabSetting setting;
    private String value;

    public TabValue(TabSetting setting) {
        this.setting = setting;
    }

    public TabValue(TabSetting setting, String value) {
        this.value = value;
        this.setting = setting;
    }

    private float i = 0;

    public void render() {
        float y = 15 + this.setting.getValues().indexOf(this) * 12;
        float ey = y + 12;

        double gay2 = Minecraft.getInstance().getDebugFPS()/13;

        if(isSelected()){
            if(i < 3) i = (float) MathHelper.clamp_double(i + 3 / gay2,0,3);
        }else if(i > 0){
            i = (float) MathHelper.clamp_double(i - 3 / gay2,0,3);
        }

        switch (this.setting.getSetting().getSettingType()) {
            case SLIDER: {
                double d = this.setting.getSetting().getDouble();
                double rounded = (int) (d * 100) / 100D;
                String gay;

                if (rounded % 1 == 0) {
                    gay = new DecimalFormat("0.##").format(rounded);
                } else {
                    gay = rounded + "";
                }

                Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(), y,
                        119 + this.setting.getModule().getLongest() + this.setting.getLongestS() + getLong(), ey,
                                 new Color(20, 20, 20, 170).getRGB());
                Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(),y,109 + this.setting.getModule().getLongest() +
                        this.setting.getLongestS(),ey, setting.getModule().getType().getTabGUI().getColor());
                SF_18.drawString(gay, 114 + this.setting.getModule().getLongest() + this.setting.getLongestS(),
                        y + 3, 0xffffffff,true);
                break;
            }
            case COMBOBOX: {
                Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(), y,
                        129 + this.setting.getModule().getLongest() + this.setting.getLongestS() + getLong(), ey,
                                 new Color(20, 20, 20, 170).getRGB());
                if(isSelected()) Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(), y,
                        129 + this.setting.getModule().getLongest() + this.setting.getLongestS() + getLong(), ey, setting.getModule().getType().getTabGUI().getColor());
                SF_18.drawString(this.value,
                        114 + i + this.setting.getModule().getLongest() + this.setting.getLongestS(), y + 3,
                        this.setting.getSetting().getComboBoxValue().equalsIgnoreCase(this.value) ?
                                0xffffffff :
                                new Color(163, 163, 163, 255).getRGB(),true);
                break;
            }
            case SELECTBOX: {
                Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(), y,
                        129 + this.setting.getModule().getLongest() + this.setting.getLongestS() + getLong(), ey,
                                 new Color(20, 20, 20, 170).getRGB());
                if(isSelected()) Gui.drawRect(109 + this.setting.getModule().getLongest() + this.setting.getLongestS(), y,
                        129 + this.setting.getModule().getLongest() + this.setting.getLongestS() + getLong(), ey, setting.getModule().getType().getTabGUI().getColor());
                SF_18.drawString(this.value,
                         114 + i + this.setting.getModule().getLongest() + this.setting.getLongestS(), y + 3,
                        this.setting.getSetting().getSelectBoxProperty().contains(this.value) ?
                                0xffffffff :
                                new Color(163, 163, 163, 255).getRGB(),true);
                break;
            }
        }
    }

    public int getLong() {
        int longest = 0;

        switch (this.setting.getSetting().getSettingType()) {
            case SLIDER: {
                double d = this.setting.getSetting().getDouble();
                double rounded = (int) (d * 100) / 100D;
                String gay;

                if (rounded % 1 == 0) {
                    gay = new DecimalFormat("0.##").format(rounded);
                } else {
                    gay = rounded + "";
                }

                return SF_18.stringWidth(gay);
            }
            case COMBOBOX: {
                for (String string : this.setting.getSetting().getComboBox().getAcceptableValues()) {
                    if (SF_18.stringWidth(string) > longest) {
                        longest = SF_18.stringWidth(string);
                    }
                }

                break;
            }
            case SELECTBOX: {
                for (String string : this.setting.getSetting().getSelectBoxProperty().getAcceptableValues()) {
                    if (SF_18.stringWidth(string) > longest) {
                        longest = SF_18.stringWidth(string);
                    }
                }

                break;
            }
        }
        return longest;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isSelected() {
        return this.setting.getValues().indexOf(this) == this.setting.getModule().getType().getTabGUI().getValueN();
    }

}
