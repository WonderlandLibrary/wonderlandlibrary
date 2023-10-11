package com.initial.modules.impl.visual;

import com.initial.modules.*;
import net.minecraft.util.*;
import com.initial.font.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import com.initial.*;
import java.util.*;
import com.initial.events.*;
import com.initial.events.impl.*;

public class TabGUI extends Module
{
    public boolean expandedCategory;
    public int cTab;
    public int mTab;
    MCFontRenderer font;
    
    public TabGUI() {
        super("TabGUI", 0, Category.VISUAL);
        this.expandedCategory = false;
        this.cTab = 0;
        this.mTab = 0;
        this.font = new MCFontRenderer(Fonts.fontFromTTF(new ResourceLocation("Desync/fonts/SF-Pro.ttf"), 18.0f, 0), true, true);
    }
    
    @EventTarget
    public void onDraw(final EventRender2D e) {
        final double x = 2.0;
        double y = 14.0;
        final Category selectedCategory = Category.values()[this.cTab];
        final double inc = 11.0;
        final double categoryWitdh = 60.0;
        final double moduleWidth = 70.0;
        for (final Category c : Category.values()) {
            final boolean isCategoryHovered = selectedCategory == c;
            int color = new Color(-2130706432, true).getRGB();
            if (isCategoryHovered) {
                color = new Color(255, 150, 200).getRGB();
            }
            Gui.drawRect(x, y, x + categoryWitdh, y + inc, color);
            this.font.drawStringWithShadow(c.name, x + 3.0 + (isCategoryHovered ? 2 : -1), y + 2.0, -1);
            y += inc;
        }
        double modCount = 14.0;
        if (this.expandedCategory) {
            for (final Module m : Astomero.instance.moduleManager.getModulesByCategory(selectedCategory)) {
                final boolean isModuleHovered = m == Astomero.instance.moduleManager.getModulesByCategory(selectedCategory).get(this.mTab);
                int modColor = new Color(-2130706432, true).getRGB();
                if (isModuleHovered) {
                    modColor = new Color(255, 150, 200).getRGB();
                }
                int textColor = new Color(13290186).getRGB();
                if (m.isToggled()) {
                    textColor = -1;
                }
                Gui.drawRect(x + categoryWitdh + 1.0, modCount, x + categoryWitdh + moduleWidth + 1.0, modCount + inc, modColor);
                this.font.drawStringWithShadow(m.getName(), x + 4.0 + categoryWitdh + (isModuleHovered ? 2 : -1), modCount + 2.0, textColor);
                modCount += inc;
            }
        }
    }
    
    @EventTarget
    public void onKey(final EventKey e) {
        final int key = e.getKey();
        final Category selectedCategory = Category.values()[this.cTab];
        if (key == 200) {
            if (this.expandedCategory) {
                if (this.mTab <= 0) {
                    this.mTab = Astomero.instance.moduleManager.getModulesByCategory(selectedCategory).size() - 1;
                }
                else {
                    --this.mTab;
                }
            }
            else if (this.cTab <= 0) {
                this.cTab = Category.values().length - 1;
            }
            else {
                --this.cTab;
            }
        }
        if (key == 208) {
            if (this.expandedCategory) {
                if (this.mTab >= Astomero.instance.moduleManager.getModulesByCategory(selectedCategory).size() - 1) {
                    this.mTab = 0;
                }
                else {
                    ++this.mTab;
                }
            }
            else if (this.cTab >= Category.values().length - 1) {
                this.cTab = 0;
            }
            else {
                ++this.cTab;
            }
        }
        if (key == 205) {
            if (this.expandedCategory) {
                final Module mod = Astomero.instance.moduleManager.getModulesByCategory(selectedCategory).get(this.mTab);
                mod.setToggled(!mod.isToggled());
            }
            else {
                this.mTab = 0;
            }
            this.expandedCategory = true;
        }
        if (key == 203) {
            this.mTab = 0;
            this.expandedCategory = false;
        }
    }
}
