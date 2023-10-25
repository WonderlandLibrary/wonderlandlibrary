package wtf.evolution.click;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.awt.*;
import java.io.IOException;

public class Skid extends GuiScreen {
public Category selected = Category.Movement;
public int x = 150;
public int y = 150;
public int prevMouseX = 0;
public int prevMouseY = 0;
public boolean drag;
public Module selMod = Main.m.m.get(0);

@Override
protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    if (mouseButton == 0 && !this.drag && isHovered(x, y, mouseX, mouseY, 250, 10)) {
        this.prevMouseX = x - mouseX;
        this.prevMouseY = y - mouseY;
        this.drag = true;
    }
    int y = this.y + 20;
    for (Category c : Category.values()) {
        if (isHovered(x + 7.5f, y, mouseX, mouseY, 20, 20)) {
            selected = c;
        }
        y += 30;
    }
    int x = this.x + 40;
    int yMod = this.y + 50;
    for (Module m : Main.m.getModulesFromCategory(selected)) {
        if (isHovered(x - 2, this.y + 15, mouseX, mouseY, Fonts.REG16.getStringWidth(m.name) + 4, 7)) {
            selMod = m;
        }
        if (m == selMod) {
            if (isHovered(this.x + 40, this.y + 35, mouseX, mouseY, 10, 10)) {
                selMod.toggle();
            }
            for (Setting s : selMod.getSettings()) {
                if (s instanceof BooleanSetting) {
                    BooleanSetting b = (BooleanSetting) s;
                    if (isHovered(this.x + 40, yMod, mouseX, mouseY, 10, 10)) {
                        b.set(! b.get());
                    }
                    yMod += 15;
                }
                if (s instanceof SliderSetting) {
                    SliderSetting sl = (SliderSetting) s;
                    if (isHovered(this.x + 40, yMod + 7, mouseX, mouseY, 100, 3))
                        sl.sliding = true;
                    yMod += 15;
                }
                if (s instanceof ModeSetting) {
                    ModeSetting mS = (ModeSetting) s;
//                    if (isHovered(this.x + 40, yMod, mouseX, mouseY, 50, 10))
//                        mS.index++;
//                    if (mS.index >= mS.modes.size())
//                        mS.index = 0;
//                    mS.currentMode = mS.modes.get(mS.index);
//                    yMod += 15;
                    if (mouseButton == 1) {
                        if (isHovered(this.x + 40, yMod + 10, mouseX, mouseY, 100, 10 + (mS.opened ? (mS.modes.size() * 8) + 2 : 0)))
                            mS.opened = ! mS.opened;
                    }

                    if (mS.opened) {
                        for (String mode : mS.modes) {
                            if (isHovered(this.x + 40, yMod + 20, mouseX, mouseY, 100, 10)) {
                                mS.currentMode = mode;
                            }
                            yMod += 8;
                        }
                    }
                }
            }
        }
        x += 30;
    }
}

@Override
protected void mouseReleased(int mouseX, int mouseY, int state) {
    super.mouseReleased(mouseX, mouseY, state);
    if (this.drag) {
        this.drag = false;
    }
    for (Setting s : selMod.getSettings()) {
        if (s instanceof SliderSetting) {
            SliderSetting sl = (SliderSetting) s;
            sl.sliding = false;
        }
    }
}

public boolean isHovered(float x, float y, int mouseX, int mouseY, int width, int height) {
    return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
}


public void drag(int mouseX, int mouseY) {
    if (drag) {
        x = mouseX + prevMouseX;
        y = mouseY + prevMouseY;
    }
}

public double preX;
public double rotX;
@Override
public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    drag(mouseX, mouseY);
    rotX = wtf.evolution.helpers.math.MathHelper.interpolate(drag ? (preX - mouseX) * 20 : 0, rotX, 0.01);
    GL11.glPushMatrix();
    GL11.glTranslated(x + (250 / 2f), y + (10 / 2f), 0);
    GL11.glRotated(-rotX, 0, 0, 1);
    GL11.glTranslated(-x - (250 / 2f), -y - (10 / 2f), 0);
    RenderUtil.drawRectWH(x, y, 250, 230, new Color(21, 21, 21, 255).getRGB());
    RenderUtil.drawRectWH(x, y, 35, 230, new Color(15, 15, 15, 255).getRGB());
    RenderUtil.drawRectWH(x, y, 250, 10, new Color(10, 10, 10, 255).getRGB());
    Fonts.REG16.drawString("skidproject - " + selected.name() + " - " + selMod.name, x + 2.5f, y + 2, new Color(255, 255, 255, 255).getRGB());
    int y = this.y + 20;
    for (Category c : Category.values()) {
        RenderUtil.drawRectWH(x + 7.5f, y, 20, 20, new Color(0, 0, 0).getRGB());
        y += 30;
    }
    int x = this.x + 40;
    int yMod = this.y + 50;
    for (Module m : Main.m.getModulesFromCategory(selected)) {
        if (m == selMod)
            RenderUtil.drawRectWH(x - 2, this.y + 25, Fonts.REG16.getStringWidth(m.name) + 4, 1, new Color(100, 100, 255).getRGB());
        Fonts.REG16.drawString(m.name, x, this.y + 15, new Color(255, 255, 255).getRGB());
        if (m == selMod) {
            RenderUtil.drawRectWH(this.x + 40, this.y + 35, 10, 10, m.state ? new Color(100, 100, 255).getRGB() : new Color(5, 5, 5).getRGB());
            Fonts.REG16.drawString("Activate", this.x + 55, this.y + 37, - 1);

            for (Setting s : selMod.getSettings()) {
                if (s instanceof BooleanSetting) {
                    BooleanSetting b = (BooleanSetting) s;
                    RenderUtil.drawRectWH(this.x + 40, yMod, 10, 10, b.get() ? new Color(100, 100, 255).getRGB() : new Color(5, 5, 5).getRGB());
                    Fonts.REG16.drawString(b.name, this.x + 55, yMod + 2, - 1);
                    yMod += 15;
                }
                if (s instanceof SliderSetting) {
                    SliderSetting b = (SliderSetting) s;

                    if (b.sliding)
                        b.current = MathHelper.clamp((float) ((double) (mouseX - this.x - 140) * (b.maximum - b.minimum) / (double) 100 + b.maximum), b.minimum, b.maximum);
                    float amountWidth = (float) (((b.current) - b.minimum) / (b.maximum - b.minimum));
                    RoundedUtil.drawRound(this.x + 40, yMod + 7, 100, 3, 0, new Color(56, 56, 56));
                    RoundedUtil.drawRound((float) (this.x + 40), yMod + 7, (float) (amountWidth) * 100, 3, 0, new Color(255, 56, 56));
                    Fonts.REG16.drawString(String.valueOf(b.current), this.x + 140 - Fonts.REG16.getStringWidth(String.valueOf(b.current)), yMod - 1, Color.GRAY.getRGB());
                    Fonts.REG16.drawString(b.name, this.x + 40, yMod - 1, Color.GRAY.getRGB());
                    yMod += 15;
                }
                if (s instanceof ModeSetting) {
                    ModeSetting b = (ModeSetting) s;
                    RoundedUtil.drawRound(this.x + 40, yMod + 10, 100, 10 + (b.opened ? (b.modes.size() * 8) + 2 : 0), 2, new Color(27, 27, 27));
                    Fonts.REG16.drawCenteredString(b.currentMode, this.x + 90, yMod + 12, - 1);
                    Fonts.REG16.drawString(b.name, this.x + 40, yMod + 2, Color.GRAY.getRGB());
                    if (b.opened) {
                        for (String mode : b.modes) {
                            if (mode == b.currentMode) {
                                RoundedUtil.drawRound(this.x + 40, yMod + 20, 100, 8, 2, new Color(39, 39, 39));
                            }
                            Fonts.REG16.drawCenteredString(mode, this.x + 90, yMod + 21, - 1);

                            yMod += 8;
                        }
                    }
                    yMod += 15;
                }
            }
        }
        x += 30;
    }
    GL11.glPopMatrix();
    preX = mouseX;

}
}
