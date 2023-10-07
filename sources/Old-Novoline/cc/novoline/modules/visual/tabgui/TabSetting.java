package cc.novoline.modules.visual.tabgui;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.SettingType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;
import java.util.List;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_18.SFTHIN_18;

public final class TabSetting {

    private final List<TabValue> values = new ObjectArrayList<>();
    private final cc.novoline.gui.screen.setting.Setting setting;
    private final TabModule module;
    private boolean opened;

    public TabSetting(cc.novoline.gui.screen.setting.Setting setting, TabModule module) {
        this.setting = setting;
        this.module = module;

        setOpened(false);

        switch (setting.getSettingType()) {
            case SLIDER: {
                this.values.add(new TabValue(this));
                break;
            }
            case SELECTBOX: {
                for (String string : setting.getSelectBoxProperty().getAcceptableValues()) {
                    this.values.add(new TabValue(this, string));
                }

                break;
            }
            case COMBOBOX: {
                for (String string : setting.getComboBox().getAcceptableValues()) {
                    this.values.add(new TabValue(this, string));
                }

                break;
            }
        }
    }

    private float i = 0;

    public void render() {
        float y = 15 + this.module.getSettings().indexOf(this) * 12;
        float eY = y + 12;

        double gay = Minecraft.getInstance().getDebugFPS()/13;

        if(isSelected()){
            if(i < 3) i = (float) MathHelper.clamp_double(i + 3 / gay,0,3);
        }else if(i > 0){
            i = (float) MathHelper.clamp_double(i - 3 / gay,0,3);
        }

        Gui.drawRect(89 + this.module.getLongest(), y, 108 + this.module.getLongest() + getLongestS(), eY,
                new Color(20, 20, 20, 170).getRGB());
        if(isSelected()) Gui.drawRect(89 + this.module.getLongest(), y, 108 + this.module.getLongest() + getLongestS(), eY, module.getType().getTabGUI().getColor());
        SF_18.drawString(this.setting.getDisplayName(), 93 + i + this.module.getLongest(), y + 3, this.setting.getSettingType() == SettingType.CHECKBOX ?
                getSetting().getCheckBoxProperty().get() ? 0xffffffff : new Color(163, 163, 163, 255).getRGB() :
                0xffffffff,true);

        if (isOpened()) {
            this.values.forEach(TabValue::render);
        }

        // a + b
        x = a + b;

    }

    int x = 0,a = 3,b = 2;

    @Nullable
    public TabValue getSelectedValue() {
        return this.values.stream().filter(TabValue::isSelected).findFirst().orElse(null);
    }

    public int getLongestS() {
        return Manager.getSettingsByMod(this.module.getMod()).stream()
                .mapToInt(setting1 -> SFTHIN_18.stringWidth(setting1.getDisplayName()))
                .filter(setting1 -> setting1 >= 0).max().orElse(0);
    }

    public boolean isSelected() {
        return this.module.getSettings().indexOf(this) == this.module.getType().getTabGUI().getSettingN();
    }

    public cc.novoline.gui.screen.setting.Setting getSetting() {
        return this.setting;
    }

    public TabModule getModule() {
        return this.module;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public List<TabValue> getValues() {
        return this.values;
    }

}
