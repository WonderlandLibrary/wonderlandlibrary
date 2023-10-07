package cc.novoline.modules.visual.tabgui;

import cc.novoline.gui.screen.setting.Manager;
import cc.novoline.gui.screen.setting.SettingType;
import cc.novoline.modules.AbstractModule;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.awt.*;
import java.util.List;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;
import static cc.novoline.utils.fonts.impl.Fonts.SFTHIN.SFTHIN_18.SFTHIN_18;

public final class TabModule {

    /* fields */
    private final List<TabSetting> settings = new ObjectArrayList<>();
    private final TabType type;
    private final AbstractModule mod;
    private boolean opened;

    /* constructors */
    public TabModule(AbstractModule mod, TabType type) {
        this.mod = mod;
        this.type = type;

        setOpened(false);

        for (cc.novoline.gui.screen.setting.Setting setting : Manager.getSettingsByMod(mod)) {
            if (isOpenable(setting)) {
                this.settings.add(new TabSetting(setting, this));
            }
        }
    }

    private float i = 0;

    public void render() {
        float y = 15 + this.type.getModules().indexOf(this) * 12;
        float eY = y + 12;

        double gay = Minecraft.getInstance().getDebugFPS()/13;

        if(isSelected()){
            if(i < 3) i = (float) MathHelper.clamp_double(i + 3 / gay,0,3);
        }else if(i > 0){
            i = (float) MathHelper.clamp_double(i - 3 / gay,0,3);
        }

        Gui.drawRect(66, y, 88 + getLongest(), eY,
                new Color(20, 20, 20, 170).getRGB());
        if(isSelected()) Gui.drawRect(66, y, 88 + getLongest(), eY, type.getTabGUI().getColor());
        SF_18.drawString(this.mod.getName(),69 + i, y + 3,
                this.mod.isEnabled() ? 0xffffffff : new Color(163, 163, 163, 255).getRGB(),true);

        if (isOpened()) {
            this.settings.forEach(TabSetting::render);
        }
    }

    @Nullable
    public TabSetting getSelectedSetting() {
        return this.settings.stream().filter(TabSetting::isSelected).findFirst().orElse(null);

    }

    public boolean isOpenable(cc.novoline.gui.screen.setting.Setting setting) {
        return setting.getSettingType() == SettingType.COMBOBOX || setting
                .getSettingType() == SettingType.CHECKBOX || setting.getSettingType() == SettingType.SLIDER || setting
                .getSettingType() == SettingType.SELECTBOX;
    }

    public boolean areSettingsEmpty() {
        return this.settings.isEmpty();
    }

    public int getLongest() {
        int longest = 0;

        for (AbstractModule module : this.type.getTabGUI().getNovoline().getModuleManager()
                .getModuleListByCategory(this.type.getType())) {
            if (SFTHIN_18.stringWidth(module.getName()) > longest) {
                longest = SFTHIN_18.stringWidth(module.getName());
            }
        }

        return longest;
    }

    //region Lombok
    public AbstractModule getMod() {
        return this.mod;
    }

    public boolean isSelected() {
        return this.type.getModules().indexOf(this) == this.type.getTabGUI().getModuleN();
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public List<TabSetting> getSettings() {
        return this.settings;
    }

    public TabType getType() {
        return this.type;
    }
    //endregion

}
