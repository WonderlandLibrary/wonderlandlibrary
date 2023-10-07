package cc.novoline.modules.visual.tabgui;

import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.visual.TabGUI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.List;

import static cc.novoline.utils.fonts.impl.Fonts.SF.SF_18.SF_18;

public final class TabType {

    private final List<TabModule> modules = new ObjectArrayList<>();
    private final EnumModuleType type;
    private final TabGUI tabGUI;
    private boolean opened;

    public TabType(TabGUI tabGUI, EnumModuleType enumModuleType) {
        this.type = enumModuleType;
        this.tabGUI = tabGUI;

        setOpened(false);

        for (AbstractModule module : tabGUI.getNovoline().getModuleManager().getModuleListByCategory(enumModuleType)) {
            this.modules.add(new TabModule(module, this));
        }
    }

    private float i = 0;

    public void render() {
        double y = 15 + this.tabGUI.getTypes().indexOf(this) * 12;
        double eY = y + 12;
        double gay = Minecraft.getInstance().getDebugFPS() / 13;

        if (isSelected()) {
            if (i < 3) i = (float) MathHelper.clamp_double(i + 3 / gay, 0, 3);
        } else if (i > 0) {
            i = (float) MathHelper.clamp_double(i - 3 / gay, 0, 3);
        }
        String name = this.type.name().substring(0, 1) + this.type.name().substring(1).toLowerCase();
        Gui.drawRect(0, y, 65, eY,
                new Color(20, 20, 20,
                        170).getRGB());
        if (isSelected()) Gui.drawRect(0, y, 65, eY, new Color(tabGUI.getColor()).getRGB());
        SF_18.drawString(name, 3 + i, (float) (y + 3), 0xffffffff,true);
        String l = "";
        if (type.name().equalsIgnoreCase("Combat")) {
            l = "D";
        } else if (type.name().equalsIgnoreCase("Movement")) {
            l = "A";
        } else if (type.name().equalsIgnoreCase("Player")) {
            l = "B";
        } else if (type.name().equalsIgnoreCase("Visuals")) {
            l = "C";
        } else if (type.name().equalsIgnoreCase("Exploits")) {
            l = "G";
        } else if (type.name().equalsIgnoreCase("Misc")) {
            l = "F";
        }
        //ICONFONT_16.drawString(l, (float) getCoords()[0], (float) getCoords()[1], 0xffffffff);

        if (isOpened()) {
            this.modules.forEach(TabModule::render);
        }
    }


    private double[] getCoords() {
        double y = 15 + this.tabGUI.getTypes().indexOf(this) * 12;
        switch (type.name().toUpperCase()) { // @off
            case "COMBAT":
                return new double[]{61, (float) (y + 5)};
            case "MOVEMENT":
            case "PLAYER":
            case "EXPLOITS":
                return new double[]{60.5, (float) (y + 5)};
            case "VISUALS":
            case "MISC":
            default:
                return new double[]{60, (float) (y + 5)};
        } // @on
    }

    public TabModule getSelectedModule() {
        for (TabModule type1 : this.modules) {
            if (type1.isSelected()) {
                return type1;
            }
        }

        return null;
    }

    public boolean isSelected() {
        return this.tabGUI.getTypes().indexOf(this) == this.tabGUI.getTypeN();
    }

    public boolean isOpened() {
        return this.opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public TabGUI getTabGUI() {
        return this.tabGUI;
    }

    public List<TabModule> getModules() {
        return this.modules;
    }

    public EnumModuleType getType() {
        return this.type;
    }

}
