package com.masterof13fps.features.ui.guiscreens.altmanager;

import com.masterof13fps.Client;
import com.masterof13fps.Wrapper;
import com.masterof13fps.manager.fontmanager.UnicodeFontRenderer;
import com.masterof13fps.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class AltSlot {
    private String username;
    private String password;
    public int x;
    public int y;
    public int WIDTH;
    private ScaledResolution res;
    public static final int HEIGHT = 25;
    private boolean clicked;
    public boolean selected;
    public float opacity;
    public int MIN_HEIGHT;
    public int MAX_HEIGHT;

    public AltSlot(String username, String password) {
        this.username = username;
        this.password = password;
        this.x = 12;
    }

    private void update() {
        this.res = new ScaledResolution(Wrapper.mc);
    }

    public void drawScreen(int mouseX, int mouseY) {
        if (this.y <= this.MAX_HEIGHT - 25) {
            if (this.y >= this.MIN_HEIGHT) {
                this.update();
                int lightGray = -15066598;
                if (this == GuiAltManager.selected) {
                    Gui.drawRect(this.x, this.y + 1, this.WIDTH + 7, this.y + 25 - 1, RenderUtils.reAlpha(lightGray, this.opacity));
                }

                if (this.isHovering(mouseX, mouseY)) {
                    Gui.drawRect(this.x, this.y + 1, this.WIDTH + 7, this.y + 25 - 1, RenderUtils.reAlpha(251658240, 0.2F * this.opacity));
                }

                String text = this.username + ":******";
                UnicodeFontRenderer comfortaa20 = Client.main().fontMgr().font("Comfortaa", 20, Font.PLAIN);
                comfortaa20.drawString(text, res.width() / 2 - comfortaa20.getStringWidth(text) / 2, (this.y + 12 - comfortaa20.getStringHeight(text) / 2), RenderUtils.reAlpha(-4340793, this.opacity));
            }
        }
    }

    public boolean isHovering(int mouseX, int mouseY) {
        return this.y <= this.MAX_HEIGHT - 25 && (mouseY > this.y && mouseY <= this.y + 25 && mouseX >= this.x && mouseX <= this.WIDTH && mouseY <= this.MAX_HEIGHT - 25 && mouseY >= this.MIN_HEIGHT + 25);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
