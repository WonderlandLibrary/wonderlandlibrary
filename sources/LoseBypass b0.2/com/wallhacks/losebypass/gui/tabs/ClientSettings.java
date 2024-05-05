/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui.tabs;

import com.wallhacks.losebypass.gui.ClickGui;
import com.wallhacks.losebypass.gui.components.ClientSettingComponent;
import com.wallhacks.losebypass.gui.tabs.ClickGuiTab;
import com.wallhacks.losebypass.manager.SystemManager;
import com.wallhacks.losebypass.systems.clientsetting.ClientSetting;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.MathUtil;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ClientSettings
extends ClickGuiTab {
    private final Animation animation = new Animation(1.0f, 0.015f);
    ArrayList<ClientSettingComponent> components = new ArrayList();
    boolean flag = true;
    double scroll = 0.0;
    double smoothScroll = 0.0;
    double height;

    public ClientSettings() {
        super("Settings", new ResourceLocation("textures/icons/settingsicon.png"));
        Iterator<ClientSetting> iterator = SystemManager.getClientSettings().iterator();
        while (iterator.hasNext()) {
            ClientSetting clientSetting = iterator.next();
            this.components.add(new ClientSettingComponent(clientSetting));
        }
    }

    @Override
    public void init() {
        this.animation.forceValue(10.0f);
    }

    @Override
    public void drawTab(int mouseX, int mouseY, int click, int posX, int posY, double deltaTime) {
        if (mouseX > posX && mouseY > posY && mouseY < posX + 400 && mouseX < posX + 440) {
            this.scroll = -((double)Mouse.getDWheel() * 0.3) + this.scroll;
        }
        this.scroll = Math.max(0.0, Math.min(this.scroll, this.height - 380.0));
        this.smoothScroll = MathUtil.lerp(this.smoothScroll, this.scroll, deltaTime * 0.02);
        this.animation.update(0.0f, deltaTime);
        double heightL = (float)(10 + posY) - this.animation.value() * 50.0f;
        double heightR = (float)(10 + posY) - this.animation.value() * 50.0f;
        GL11.glEnable((int)3089);
        Iterator<ClientSettingComponent> iterator = this.components.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                GL11.glDisable((int)3089);
                this.height = Math.max(heightL, heightR);
                this.height -= (double)(10 + posY);
                this.flag = false;
                return;
            }
            ClientSettingComponent component = iterator.next();
            ClickGui.maxOffset = 380;
            ClickGui.minOffset = posY + 10;
            GuiUtil.glScissor(posX, ClickGui.minOffset, 440, ClickGui.maxOffset);
            if (heightL <= heightR) {
                if (this.flag || ClickGui.animation.value() != 1.0f) {
                    component.setPosition(posX + 10, heightL);
                }
                heightL += (double)component.drawComponent(posX + 10, heightL, (int)this.smoothScroll, deltaTime, click, mouseX, mouseY);
                continue;
            }
            if (this.flag || ClickGui.animation.value() != 1.0f) {
                component.setPosition(posX + 220, heightR);
            }
            heightR += (double)component.drawComponent(posX + 220, heightR, (int)this.smoothScroll, deltaTime, click, mouseX, mouseY);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }
}

