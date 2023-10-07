/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;

public class Tab {
    private final ModuleCategory enumModuleType;
    private float posX;
    private float posY;
    public boolean dragging;
    public boolean opened;
    public List<Module> modules = new CopyOnWriteArrayList<Module>();

    public Tab(ModuleCategory enumModuleType, float posX, float posY) {
        this.enumModuleType = enumModuleType;
        this.posX = posX;
        this.posY = posY;
        for (net.ccbluex.liquidbounce.features.module.Module abstractModule : LiquidBounce.moduleManager.getModuleInCategory(enumModuleType)) {
            this.modules.add(new Module(abstractModule, this));
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        String l = "";
        if (this.enumModuleType.name().equalsIgnoreCase("Combat")) {
            l = "D";
        } else if (this.enumModuleType.name().equalsIgnoreCase("Movement")) {
            l = "A";
        } else if (this.enumModuleType.name().equalsIgnoreCase("Player")) {
            l = "B";
        } else if (this.enumModuleType.name().equalsIgnoreCase("Render")) {
            l = "C";
        } else if (this.enumModuleType.name().equalsIgnoreCase("Exploit")) {
            l = "G";
        } else if (this.enumModuleType.name().equalsIgnoreCase("Misc")) {
            l = "F";
        }
        RenderUtils.drawRect(this.posX - 1.0f, this.posY, this.posX + 101.0f, this.posY + 15.0f, new Color(29, 29, 29, 255).getRGB());
        Fonts.ICONFONT.ICONFONT_24.ICONFONT_24.drawString((CharSequence)l, this.posX + 88.0f, this.posY + 5.0f, -1);
        if (this.enumModuleType.name().equalsIgnoreCase("World")) {
            Fonts.CheckFont.CheckFont_24.CheckFont_24.drawString((CharSequence)"b", this.posX + 88.0f, this.posY + 5.0f, -1);
        }
        Fonts.SF.SF_20.SF_20.drawString((CharSequence)(this.enumModuleType.name().charAt(0) + this.enumModuleType.name().substring(1).toLowerCase()), this.posX + 4.0f, this.posY + 4.0f, -1, true);
        if (this.opened) {
            RenderUtils.drawRect(this.posX - 1.0f, this.posY + 15.0f, this.posX + 101.0f, this.posY + 15.0f + (float)this.getTabHeight() + 1.0f, new Color(29, 29, 29, 255).getRGB());
            this.modules.forEach(module -> module.drawScreen(mouseX, mouseY));
        } else {
            this.modules.forEach(module -> {
                module.yPerModule = 0;
            });
        }
    }

    public int getTabHeight() {
        int height = 0;
        for (Module module : this.modules) {
            height += module.yPerModule;
        }
        return height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return (float)mouseX >= this.posX && (float)mouseY >= this.posY && (float)mouseX <= this.posX + 101.0f && (float)mouseY <= this.posY + 15.0f;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.opened) {
            this.modules.forEach(module -> module.mouseReleased(mouseX, mouseY, state));
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        this.modules.forEach(module -> module.keyTyped(typedChar, keyCode));
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY) && mouseButton == 1) {
            boolean bl = this.opened = !this.opened;
            if (this.opened) {
                for (Module module2 : this.modules) {
                    module2.fraction = 0.0f;
                }
            }
        }
        if (this.opened) {
            this.modules.forEach(module -> {
                try {
                    module.mouseClicked(mouseX, mouseY, mouseButton);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosY() {
        return this.posY;
    }

    public float getPosX() {
        return this.posX;
    }

    public List<Module> getModules() {
        return this.modules;
    }
}

