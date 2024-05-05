/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline;

import java.awt.Color;
import java.util.ArrayList;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.AnimationUtil;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.ValueButton;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.Window;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class Button {
    public Module cheat;
    public Window parent;
    public int x;
    public float y;
    public int index;
    public int remander;
    public double opacity;
    public ArrayList<ValueButton> buttons = new ArrayList();
    public boolean expand;
    boolean hover;
    int smoothalpha;
    float animationsize;
    public long rticks;

    public Button(Module cheat, int x, int y) {
        this.cheat = cheat;
        this.x = x;
        this.y = y;
        int y2 = y + 15;
        for (Value<?> v : this.cheat.getValues()) {
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15;
        }
    }

    public float processFPS(float fps, float defF, float defV) {
        return defV / (fps / defF);
    }

    public void render(int mouseX, int mouseY, int x11, int y11, int x22, int y22) {
        GameFontRenderer font = Fonts.font35;
        float y2 = this.y + 15.0f;
        this.buttons.clear();
        for (Value<?> v : this.cheat.getValues()) {
            this.buttons.add(new ValueButton(v, this.x + 5, y2));
            y2 += 15.0f;
        }
        if (this.index != 0) {
            int n;
            Minecraft.func_71410_x();
            if (Minecraft.func_175610_ah() == 0) {
                n = 1;
            } else {
                Minecraft.func_71410_x();
                n = Minecraft.func_175610_ah();
            }
            int FPS = n;
            Button b2 = this.parent.buttons.get(this.index - 1);
            this.y = b2.y + 15.0f + this.animationsize;
            if (b2.expand) {
                this.parent.buttonanim = true;
                this.animationsize = AnimationUtil.moveUD(this.animationsize, 15 * b2.buttons.size(), this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
            } else {
                this.parent.buttonanim = true;
                this.animationsize = AnimationUtil.moveUD(this.animationsize, 0.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
            }
        }
        if (this.parent.buttonanim) {
            this.parent.buttonanim = false;
        }
        int i = 0;
        float size = this.buttons.size();
        while ((float)i < size) {
            this.buttons.get((int)i).y = this.y + 17.0f + (float)(15 * i);
            this.buttons.get((int)i).x = this.x + 5;
            ++i;
        }
        this.smoothalphas();
        GL11.glPushMatrix();
        this.hover = mouseX > this.x - 7 && mouseX < this.x + 85 && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)font.field_78288_b + 4.0f;
        RenderUtils.drawRect((float)(this.x - 5), this.y - 5.0f, (float)(this.x + 85 + this.parent.allX), this.y + (float)font.field_78288_b + 5.0f, new Color(40, 40, 40).getRGB());
        RenderUtils.drawRect((float)(this.x - 5), this.y - 5.0f - 1.0f, (float)(this.x + 85 + this.parent.allX), this.y + (float)font.field_78288_b + 3.0f + 1.0f, this.hudcolorwithalpha());
        ++this.rticks;
        Color Ranbow = new Color(Color.HSBtoRGB((float)((double)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 50.0 + Math.sin(0.0)) % 1.0f, 0.5f, 1.0f));
        ValueButton.valuebackcolor = Ranbow.getRGB();
        if (!this.expand && size >= 1.0f) {
            Fonts.font35.drawString("+", this.x + 75 + this.parent.allX, this.y - 1.0f, -1);
        } else if (size >= 1.0f) {
            Fonts.font35.drawString("-", this.x + 75 + this.parent.allX, this.y - 1.0f, -1);
        }
        font.func_175063_a(this.cheat.getName(), this.x, this.y - 1.0f, -1);
        if (this.expand) {
            this.buttons.forEach(b -> b.render(mouseX, mouseY, this.parent));
        }
        GL11.glPopMatrix();
    }

    private int hudcolorwithalpha() {
        Color Ranbow = new Color(Color.HSBtoRGB((float)((double)Minecraft.func_71410_x().field_71439_g.field_70173_aa / 50.0 + Math.sin(1.6)) % 1.0f, 0.5f, 1.0f));
        return this.cheat.getState() ? Ranbow.getRGB() : new Color(40, 40, 40).getRGB();
    }

    private void smoothalphas() {
        int n;
        Minecraft.func_71410_x();
        if (Minecraft.func_175610_ah() == 0) {
            n = 1;
        } else {
            Minecraft.func_71410_x();
            n = Minecraft.func_175610_ah();
        }
        int FPS = n;
        this.smoothalpha = this.cheat.getState() ? (int)AnimationUtil.moveUD(this.smoothalpha, 255.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f)) : (int)AnimationUtil.moveUD(this.smoothalpha, 0.0f, this.processFPS(FPS, 1000.0f, 0.013f), this.processFPS(FPS, 1000.0f, 0.011f));
    }

    public void key(char typedChar, int keyCode) {
        this.buttons.forEach(b -> b.key(typedChar, keyCode));
    }

    public void click(int mouseX, int mouseY, int button) {
        if (this.parent.drag) {
            return;
        }
        if (mouseX > this.x - 7 && mouseX < this.x + 85 + this.parent.allX && (float)mouseY > this.y - 6.0f && (float)mouseY < this.y + (float)Fonts.font35.field_78288_b) {
            if (button == 0) {
                this.cheat.setState(!this.cheat.getState());
            }
            if (button == 1 && !this.buttons.isEmpty()) {
                boolean bl = this.expand = !this.expand;
            }
        }
        if (this.expand) {
            this.buttons.forEach(b -> b.click(mouseX, mouseY, button));
        }
    }

    public void setParent(Window parent) {
        this.parent = parent;
        for (int i = 0; i < this.parent.buttons.size(); ++i) {
            if (this.parent.buttons.get(i) != this) continue;
            this.index = i;
            this.remander = this.parent.buttons.size() - i;
            break;
        }
    }
}

