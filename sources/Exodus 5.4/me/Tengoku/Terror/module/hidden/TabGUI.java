/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.hidden;

import java.awt.Color;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.Event;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventKey;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.render.HUD;
import me.Tengoku.Terror.ui.CustomIngameGui;
import me.Tengoku.Terror.util.font.FontUtil;
import me.Tengoku.Terror.util.font.MinecraftFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class TabGUI
extends Module {
    public int currentTab;
    public boolean expanded;

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public TabGUI() {
        super("TabGUI", 0, Category.RENDER, "");
        this.toggle();
    }

    @Override
    @EventTarget
    public void onEvent(Event event) {
        int n;
        if (event instanceof EventRenderGUI) {
            Category category;
            int n2 = 1;
            int n3 = 20;
            n = 60;
            Gui.drawRect(n2, n3, n2 + n, n3 + n + 52, -15592942);
            int n4 = 0;
            int n5 = -14;
            int n6 = 3;
            Gui.drawRect(n4 + 59, n5 + 36 + 16, n4 + n6, n5 + n6 + 33 + 16 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 32, n4 + n6, n5 + n6 + 33 + 32 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 48, n4 + n6, n5 + n6 + 33 + 48 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 64, n4 + n6, n5 + n6 + 33 + 64 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 80, n4 + n6, n5 + n6 + 33 + 80 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 96, n4 + n6, n5 + n6 + 33 + 96 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + 0, n4 + n6, n5 + n6 + 33 + 0 + 12, new Color(52, 52, 52).darker().getRGB());
            Gui.drawRect(n4 + 59, n5 + 36 + this.currentTab * 16, n4 + n6, n5 + n6 + 33 + this.currentTab * 16 + 12, CustomIngameGui.color);
            MinecraftFontRenderer minecraftFontRenderer = FontUtil.normal;
            String string = Exodus.INSTANCE.getSettingsManager().getSettingByClass("HUD Font", HUD.class).getValString();
            int n7 = 0;
            Category[] categoryArray = Category.values();
            int n8 = categoryArray.length;
            int n9 = 0;
            while (n9 < n8) {
                category = categoryArray[n9];
                if (!category.name().equalsIgnoreCase("Hidden")) {
                    if (string.equalsIgnoreCase("Normal")) {
                        Minecraft.fontRendererObj.drawStringWithShadow(String.valueOf(category.name().substring(0, 1).toUpperCase()) + category.name().substring(1).toLowerCase(), 4.5f, 25 + n7 * 16, -1);
                    } else {
                        minecraftFontRenderer.drawStringWithShadow(String.valueOf(category.name().substring(0, 1).toUpperCase()) + category.name().substring(1).toLowerCase(), 4.5, 25 + n7 * 16, -1);
                    }
                    ++n7;
                }
                ++n9;
            }
            if (this.expanded) {
                category = Category.values()[this.currentTab];
                List<Module> list = Exodus.INSTANCE.moduleManager.getModulesByCategory(category);
                Gui.drawRect(n + 60 + list.size() + 1, n3, n2 + n, (float)n3 + (float)(n * list.size()) / 3.8f, -15592942);
                Gui.drawRect(n6 + 115 + list.size() + 1, n5 + 35 + category.moduleIndex * 16, n4 + n6 + 60, n5 + n6 + 33 + category.moduleIndex * 16 + 12, CustomIngameGui.color);
                n7 = 0;
                for (Module module : list) {
                    if (module.getDisplayName().equalsIgnoreCase("TabGUI")) continue;
                    if (string.equalsIgnoreCase("Arial")) {
                        minecraftFontRenderer.drawStringWithShadow(module.getName(), 65.5, 25 + n7 * 16, -1);
                    } else {
                        Minecraft.fontRendererObj.drawStringWithShadow(module.getName(), 65.5f, 25 + n7 * 16, -1);
                    }
                    ++n7;
                }
            }
        }
        if (event instanceof EventKey) {
            Category category = Category.values()[this.currentTab];
            List<Module> list = Exodus.INSTANCE.moduleManager.getModulesByCategory(Category.values()[this.currentTab]);
            n = ((EventKey)event).getKey();
            if (n == 200) {
                if (!this.expanded) {
                    this.currentTab = this.currentTab <= 0 ? Category.values().length - 1 : --this.currentTab;
                } else {
                    category.moduleIndex = category.moduleIndex <= 0 ? list.size() - 1 : --category.moduleIndex;
                }
            }
            if (n == 208) {
                if (!this.expanded) {
                    this.currentTab = this.currentTab >= Category.values().length - 1 ? 0 : ++this.currentTab;
                } else {
                    category.moduleIndex = category.moduleIndex >= list.size() - 1 ? 0 : ++category.moduleIndex;
                }
            }
            if (n == 205) {
                if (this.expanded) {
                    list.get(category.moduleIndex).toggle();
                } else {
                    this.expanded = true;
                }
            }
            if (n == 203) {
                this.expanded = false;
            }
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }
}

