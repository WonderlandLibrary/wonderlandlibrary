/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.ui.tabgui;

import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.ttf.FontUtils;
import cow.milkgod.cheese.ui.tabgui.TabGui;
import cow.milkgod.cheese.utils.R2DUtils;
import cow.milkgod.cheese.utils.Wrapper;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import net.minecraft.client.Minecraft;

public final class TabguiItem {
    private final TabGui gui;
    private final ArrayList<GuiItem> modules = new ArrayList();
    private int menuHeight = 0;
    private int menuWidth = 0;
    private String tabName;

    public TabguiItem(TabGui gui, String tabName) {
        this.gui = gui;
        this.tabName = tabName;
    }

    public void drawTabMenu(Minecraft mc2, int x2, int y2) {
        this.countMenuSize(mc2);
        int boxY = y2;
        R2DUtils.drawBorderedRect(x2, y2, x2 + this.menuWidth + 2, y2 + this.menuHeight, this.gui.getColourBody(), Integer.MIN_VALUE);
        int i2 = 0;
        while (i2 < this.modules.size()) {
            if (this.gui.getSelectedItem() == i2) {
                int transitionTop = this.gui.getTransition() + (this.gui.getSelectedItem() == 0 && this.gui.getTransition() < 0 ? - this.gui.getTransition() : 0);
                int transitionBottom = this.gui.getTransition() + (this.gui.getSelectedItem() == this.modules.size() - 1 && this.gui.getTransition() > 0 ? - this.gui.getTransition() : 0);
                R2DUtils.drawBorderedRect(x2, boxY + transitionTop, x2 + this.menuWidth + 2, boxY + 12 + transitionBottom, this.gui.getColourBox(), Integer.MIN_VALUE);
            }
            Collator collator = Collator.getInstance(Locale.US);
            this.modules.sort((mod1, mod2) -> collator.compare(mod1.getModule().getName(), mod2.getModule().getName()));
            Wrapper.fu_default.drawStringWithShadow(String.valueOf(String.valueOf(this.modules.get(i2).getModule().getState() ? this.gui.getColourHightlight() : this.gui.getColourNormal())) + this.modules.get(i2).getModule().getName(), x2 + 2, y2 + this.gui.getTabHeight() * i2 - 1, -1);
            boxY += 12;
            ++i2;
        }
    }

    private void countMenuSize(Minecraft mc2) {
        int maxWidth = 0;
        for (GuiItem module : this.modules) {
            if (Wrapper.fu_default.getWidthInt(module.getModule().getName()) <= maxWidth) continue;
            maxWidth = Wrapper.fu_default.getWidthInt(module.getModule().getName()) + 4;
        }
        this.menuWidth = maxWidth;
        this.menuHeight = this.modules.size() * this.gui.getTabHeight();
    }

    public String getTabName() {
        return this.tabName;
    }

    public ArrayList<GuiItem> getModules() {
        return this.modules;
    }

    public static class GuiItem {
        private final Module mod;

        public GuiItem(Module mod) {
            this.mod = mod;
        }

        public Module getModule() {
            return this.mod;
        }
    }

}

