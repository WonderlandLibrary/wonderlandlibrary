/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Module;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.math.MathUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.Timer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class Setting {
    public Value setting;
    private Module module;
    public boolean opened;
    private final Timer backSpace = new Timer();
    private final Timer caretTimer = new Timer();
    public int height;
    public float percent = 0.0f;
    private boolean dragging;
    private boolean dragging2;

    public Setting(Value setting, Module module) {
        this.setting = setting;
        this.module = module;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void drawScreen(int mouseX, int mouseY) {
        double set;
        double value;
        double difference;
        double percentBar;
        double rounded;
        int y = this.getY();
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
        boolean scissor = scaledResolution.func_78325_e() != 1;
        Minecraft.func_71410_x();
        double clamp = MathHelper.func_151237_a((double)(Minecraft.func_175610_ah() / 30), (double)1.0, (double)9999.0);
        if (this.setting instanceof FloatValue) {
            FloatValue numberValue = (FloatValue)this.setting;
            if (this.module.yPerModule == this.module.getY() && scissor) {
                GL11.glPushMatrix();
                GL11.glScissor((int)((int)(this.module.tab.getPosX() * 2.0f + 1.0f)), (int)0, (int)197, (int)999999999);
                GL11.glEnable((int)3089);
            }
            rounded = (double)((int)((double)((Float)numberValue.get()).floatValue() * 100.0)) / 100.0;
            percentBar = (((Float)numberValue.get()).floatValue() - numberValue.getMinimum()) / (numberValue.getMaximum() - numberValue.getMinimum());
            this.percent = Math.max(0.0f, Math.min(1.0f, (float)((double)this.percent + (Math.max(0.0, Math.min(percentBar, 1.0)) - (double)this.percent) * (0.2 / clamp))));
            RenderUtils.drawRect(this.module.tab.getPosX() + 1.0f, (float)(y + 3), this.module.tab.getPosX() + 99.0f, (float)(y + 14), new Color(0, 0, 0, 50).getRGB());
            RenderUtils.drawRect(this.module.tab.getPosX() + 1.0f, (float)(y + 3), this.module.tab.getPosX() + 1.0f + 98.0f * this.percent, (float)(y + 14), ClickGUI.generateColor());
            Fonts.SF.SF_18.SF_18.drawString((CharSequence)(numberValue.getName() + " " + rounded), this.module.tab.getPosX() + 4.0f, (float)y + 5.5f, -1, true);
            if (this.dragging) {
                difference = numberValue.getMaximum() - numberValue.getMinimum();
                value = (double)numberValue.getMinimum() + MathHelper.func_151237_a((double)(((float)mouseX - (this.module.tab.getPosX() + 1.0f)) / 99.0f), (double)0.0, (double)1.0) * difference;
                set = MathUtils.incValue(value, 0.01);
                numberValue.set(set);
            }
            if (this.module.yPerModule == this.module.getY() && scissor) {
                GL11.glDisable((int)3089);
                GL11.glPopMatrix();
            }
        }
        if (this.setting instanceof IntegerValue) {
            IntegerValue integerValue = (IntegerValue)this.setting;
            if (this.module.yPerModule == this.module.getY() && scissor) {
                GL11.glPushMatrix();
                GL11.glScissor((int)((int)(this.module.tab.getPosX() * 2.0f + 1.0f)), (int)0, (int)197, (int)999999999);
                GL11.glEnable((int)3089);
            }
            rounded = (double)((int)((double)((Integer)integerValue.get()).intValue() * 100.0)) / 100.0;
            percentBar = (((Integer)integerValue.get()).doubleValue() - (double)integerValue.getMinimum()) / (double)(integerValue.getMaximum() - integerValue.getMinimum());
            this.percent = Math.max(0.0f, Math.min(1.0f, (float)((double)this.percent + (Math.max(0.0, Math.min(percentBar, 1.0)) - (double)this.percent) * (0.2 / clamp))));
            RenderUtils.drawRect(this.module.tab.getPosX() + 1.0f, (float)(y + 3), this.module.tab.getPosX() + 99.0f, (float)(y + 14), new Color(0, 0, 0, 50).getRGB());
            RenderUtils.drawRect(this.module.tab.getPosX() + 1.0f, (float)(y + 3), this.module.tab.getPosX() + 1.0f + 98.0f * this.percent, (float)(y + 14), ClickGUI.generateColor());
            Fonts.SF.SF_18.SF_18.drawString((CharSequence)(integerValue.getName() + " " + rounded), this.module.tab.getPosX() + 4.0f, (float)y + 5.5f, -1, true);
            if (this.dragging2) {
                difference = integerValue.getMaximum() - integerValue.getMinimum();
                value = (double)integerValue.getMinimum() + MathHelper.func_151237_a((double)(((float)mouseX - (this.module.tab.getPosX() + 1.0f)) / 99.0f), (double)0.0, (double)1.0) * difference;
                set = MathUtils.incValue(value, 1.0);
                integerValue.set(set);
            }
            if (this.module.yPerModule == this.module.getY() && scissor) {
                GL11.glDisable((int)3089);
                GL11.glPopMatrix();
            }
        }
        if (this.setting instanceof BoolValue) {
            BoolValue boolValue = (BoolValue)this.setting;
            RenderUtils.drawRect(this.module.tab.getPosX() + 89.0f, (float)(y + 4), this.module.tab.getPosX() + 99.0f, (float)(y + 14), new Color(0, 0, 0, 50).getRGB());
            if (((Boolean)boolValue.get()).booleanValue()) {
                RenderUtils.drawCheck(this.module.tab.getPosX() + 91.0f, (float)y + 8.5f, 2, ClickGUI.generateColor().brighter().getRGB());
            }
            Fonts.SF.SF_18.SF_18.drawString((CharSequence)boolValue.getName(), this.module.tab.getPosX() + 4.0f, (float)y + 5.5f, new Color(227, 227, 227, 255).getRGB(), true);
        }
        if (this.setting instanceof ListValue) {
            ListValue listValue = (ListValue)this.setting;
            Fonts.SF.SF_17.SF_17.drawString((CharSequence)listValue.getName(), this.module.tab.getPosX() + 3.0f, y + 6, -1, true);
            Fonts.SF.SF_17.SF_17.drawString((CharSequence)((String)listValue.get()).toUpperCase(), this.module.tab.getPosX() + 97.0f - (float)Fonts.SF.SF_17.SF_17.stringWidth(((String)listValue.get()).toUpperCase()), (float)y + 7.0f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        if (this.setting instanceof TextValue) {
            TextValue textValue = (TextValue)this.setting;
            String s = (String)textValue.get();
            if (textValue.getTextHovered() && Keyboard.isKeyDown((int)14) && this.backSpace.delay(100.0) && s.length() >= 1) {
                textValue.set(s.substring(0, s.length() - 1));
                this.backSpace.reset();
            }
            RenderUtils.drawRect((double)(this.module.tab.getPosX() + 6.0f), (double)(y + 16), (double)(this.module.tab.getPosX() + 84.0f), (double)y + 16.5, new Color(195, 195, 195, 220).getRGB());
            Fonts.SF.SF_16.SF_16.drawString((CharSequence)textValue.getName(), this.module.tab.getPosX() + 5.5f, (float)y + 1.5f, new Color(227, 227, 227, 255).getRGB());
            if (Fonts.SF.SF_16.SF_16.stringWidth(s) > 65) {
                Fonts.SF.SF_16.SF_16.drawString((CharSequence)Fonts.SF.SF_16.SF_16.trimStringToWidth(s, 78, true), this.module.tab.getPosX() + 6.0f, (float)(y + 10), -1);
            } else {
                Fonts.SF.SF_16.SF_16.drawString((CharSequence)s, this.module.tab.getPosX() + 6.0f, (float)(y + 10), -1);
            }
        }
    }

    private int getY() {
        Setting dropDownSetting;
        int y = this.module.y + 14;
        Iterator iterator2 = this.module.settings.stream().filter(s -> s.setting.getDisplayable()).collect(Collectors.toList()).iterator();
        while (iterator2.hasNext() && (dropDownSetting = (Setting)iterator2.next()) != this) {
            y += dropDownSetting.getHeight();
        }
        return y;
    }

    public int getHeight() {
        return 15;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.setting instanceof BoolValue) {
                BoolValue boolValue = (BoolValue)this.setting;
                if (mouseButton == 0) {
                    if (((Boolean)boolValue.get()).booleanValue()) {
                        boolValue.set(false);
                    } else {
                        boolValue.set(true);
                    }
                }
            }
            if (this.setting instanceof ListValue && mouseButton == 0) {
                ListValue m = (ListValue)this.setting;
                String current = (String)m.get();
                this.setting.set(m.getValues()[m.getModeListNumber(current) + 1 >= m.getValues().length ? 0 : m.getModeListNumber(current) + 1]);
            }
            if (this.setting instanceof IntegerValue && mouseButton == 0) {
                this.dragging2 = true;
            }
            if (this.setting instanceof FloatValue && mouseButton == 0) {
                this.dragging = true;
            }
        }
        if (this.setting instanceof TextValue) {
            if (this.isHovered(mouseX, mouseY)) {
                this.setting.setTextHovered(!this.setting.getTextHovered());
            } else if (this.setting.getTextHovered()) {
                this.setting.setTextHovered(false);
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.setting instanceof TextValue && this.setting.getTextHovered()) {
            if (keyCode == 1 || keyCode == 28) {
                this.setting.setTextHovered(false);
            } else if (keyCode != 14 && keyCode != 157 && keyCode != 29 && keyCode != 54 && keyCode != 42 && keyCode != 15 && keyCode != 58 && keyCode != 211 && keyCode != 199 && keyCode != 210 && keyCode != 200 && keyCode != 208 && keyCode != 205 && keyCode != 203 && keyCode != 56 && keyCode != 184 && keyCode != 197 && keyCode != 70 && keyCode != 207 && keyCode != 201 && keyCode != 209 && keyCode != 221 && keyCode != 59 && keyCode != 60 && keyCode != 61 && keyCode != 62 && keyCode != 63 && keyCode != 64 && keyCode != 65 && keyCode != 66 && keyCode != 67 && keyCode != 68 && keyCode != 87 && keyCode != 88) {
                ((TextValue)this.setting).append(Character.valueOf(typedChar));
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            this.dragging = false;
        }
        if (state == 0) {
            this.dragging2 = false;
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        int y = this.getY();
        if (this.setting instanceof FloatValue) {
            // empty if block
        }
        if (this.setting instanceof IntegerValue) {
            // empty if block
        }
        if (this.setting instanceof BoolValue) {
            return (float)mouseX >= this.module.tab.getPosX() + 89.0f && mouseY >= y + 4 && (float)mouseX <= this.module.tab.getPosX() + 99.0f && mouseY <= y + 14;
        }
        return (float)mouseX >= this.module.tab.getPosX() && mouseY >= y && (float)mouseX <= this.module.tab.getPosX() + 90.0f && mouseY <= y + 17;
    }
}

