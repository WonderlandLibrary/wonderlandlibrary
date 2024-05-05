/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.gui.components;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.settings.SettingComponent;
import com.wallhacks.losebypass.systems.clientsetting.ClientSetting;
import com.wallhacks.losebypass.utils.GuiUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientSettingComponent {
    double renderX;
    double renderY;
    private final ClientSetting clientSetting;
    private final ArrayList<SettingComponent> components = new ArrayList();

    public ClientSettingComponent(ClientSetting clientSetting) {
        this.clientSetting = clientSetting;
        this.components.addAll(GuiUtil.getComponents(clientSetting));
    }

    public void setPosition(double posX, double posY) {
        this.renderY = posY;
        this.renderX = posX;
    }

    public int drawComponent(double posX, double posY, int smoothScroll, double deltaTime, int click, int mouseX, int mouseY) {
        if (this.renderX > posX) {
            this.renderX = Math.max(posX, this.renderX - deltaTime);
        } else if (this.renderX < posX) {
            this.renderX = Math.min(posX, this.renderX + deltaTime);
        }
        if (this.renderY > posY) {
            this.renderY = Math.max(posY, this.renderY - deltaTime);
        } else if (this.renderY < posY) {
            this.renderY = Math.min(posY, this.renderY + deltaTime);
        }
        int height = 5;
        for (SettingComponent setting : this.components) {
            if (!setting.visible()) continue;
            height += setting.getHeight();
        }
        GuiUtil.rounded((int)this.renderX, (int)this.renderY - smoothScroll, (int)this.renderX + 201, (int)this.renderY + 31 - smoothScroll + height, new Color(0, 0, 0, 50).getRGB(), 5);
        GuiUtil.rounded((int)this.renderX, (int)this.renderY - smoothScroll, (int)this.renderX + 200, (int)this.renderY + 30 - smoothScroll + height, ClickGui.background3(), 5);
        LoseBypass.fontManager.getThickFont().drawString(this.clientSetting.getName(), (int)this.renderX + 5, (int)this.renderY + 10 - smoothScroll, -1);
        int max = (double)ClickGui.minOffset > this.renderY - (double)smoothScroll + 25.0 ? ClickGui.maxOffset : (int)Math.max((double)(ClickGui.minOffset + ClickGui.maxOffset) - (this.renderY - (double)smoothScroll + 25.0), 0.0);
        int min = Math.max((int)this.renderY - smoothScroll + 25, ClickGui.minOffset);
        int currentY = (int)this.renderY + 25;
        ClickGui.maxOffset = max;
        ClickGui.minOffset = min;
        GuiUtil.glScissor((int)this.renderX, ClickGui.minOffset, 200, ClickGui.maxOffset);
        GuiUtil.drawRect((float)this.renderX, currentY - smoothScroll, (float)this.renderX + 200.0f, (float)this.renderY - (float)smoothScroll + (float)height + 20.0f, ClickGui.background2());
        Iterator<SettingComponent> iterator = this.components.iterator();
        while (iterator.hasNext()) {
            SettingComponent setting = iterator.next();
            if (!setting.visible()) continue;
            if (currentY - smoothScroll > ClickGui.minOffset + ClickGui.maxOffset) {
                return 35 + height;
            }
            if (setting.getHeight() + currentY - smoothScroll < ClickGui.minOffset) {
                currentY += setting.getHeight();
                continue;
            }
            GuiUtil.glScissor((int)this.renderX, ClickGui.minOffset, 200, ClickGui.maxOffset);
            currentY += setting.drawComponent((int)this.renderX, currentY - smoothScroll, deltaTime, click, mouseX, mouseY, false);
        }
        return 35 + height;
    }
}

