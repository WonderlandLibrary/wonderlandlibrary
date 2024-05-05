/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package com.wallhacks.losebypass.gui;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.systems.clientsetting.clientsettings.ClickGuiConfig;
import com.wallhacks.losebypass.utils.Animation;
import com.wallhacks.losebypass.utils.GuiUtil;
import com.wallhacks.losebypass.utils.Timer;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class InfoComponent {
    ResourceLocation icon = new ResourceLocation("/textures/icons/info.png");
    String info = "";
    String title = "";
    Timer timer = new Timer().reset();
    Animation animation = new Animation(0.0f, 0.005f);

    public void setInfo(String info, String title) {
        if (!ClickGuiConfig.getInstance().description()) return;
        this.info = info;
        this.title = title;
        this.timer.reset();
    }

    public String getInfo() {
        return this.info;
    }

    public int getHeight() {
        return (int)(this.animation.value() * 30.0f);
    }

    public void draw(double deltaTime, int posX, int posY) {
        if (!this.timer.passedMs(500L)) {
            this.animation.update(1.0f, deltaTime);
        } else {
            this.animation.update(0.0f, deltaTime);
        }
        if (this.getHeight() == 0) return;
        GuiUtil.glScissor(posX, posY - 40, 440, 40);
        GL11.glEnable((int)3089);
        GuiUtil.setup(new Color(-1742198744, true).getRGB());
        GuiUtil.corner(posX + 432, posY - 8, 8.0, 0, 90);
        GL11.glVertex2d((double)(posX + 440), (double)(posY - 10 - this.getHeight()));
        GL11.glVertex2d((double)posX, (double)(posY - 10 - this.getHeight()));
        GL11.glVertex2d((double)posX, (double)posY);
        GuiUtil.finish();
        GuiUtil.drawCompleteImage(posX + 5, (posY -= 10 + this.getHeight()) + 5, 30.0, 30.0, this.icon, Color.WHITE);
        LoseBypass.fontManager.getThickFont().drawString(this.title, posX + 40, posY + 5, -1);
        String[] splitted = this.info.split(" ");
        int width = 0;
        int line = 0;
        String[] stringArray = splitted;
        int n = stringArray.length;
        int n2 = 0;
        while (true) {
            if (n2 >= n) {
                GL11.glDisable((int)3089);
                return;
            }
            String word = stringArray[n2];
            int wide = LoseBypass.fontManager.getTextWidth(word = word + " ");
            if (width + wide > 400) {
                width = 0;
                ++line;
            }
            LoseBypass.fontManager.drawString(word, posX + 40 + width, posY + 17 + line * 10, -1);
            width += wide;
            ++n2;
        }
    }
}

