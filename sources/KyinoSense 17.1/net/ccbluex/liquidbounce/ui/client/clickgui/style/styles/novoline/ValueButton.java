/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  org.lwjgl.input.Mouse
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import net.ccbluex.liquidbounce.ui.client.clickgui.style.styles.novoline.Window;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.ui.font.GameFontRenderer;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class ValueButton {
    public final Value value;
    public String name;
    public boolean custom;
    public boolean change;
    public int x;
    public float y;
    public static int valuebackcolor;

    public ValueButton(Value value, int x, float y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.name = "";
        if (this.value instanceof BoolValue) {
            this.change = (Boolean)this.value.get();
        } else if (this.value instanceof ListValue) {
            this.name = String.valueOf(this.value.get());
        } else if (value instanceof IntegerValue) {
            IntegerValue v = (IntegerValue)value;
            this.name = this.name + ((Number)v.get()).intValue();
        }
    }

    public void render(int mouseX, int mouseY, Window parent) {
        GameFontRenderer font = Fonts.font35;
        RenderUtils.drawRect23(this.x - 10, this.y - 7.0f, this.x + 80 + parent.allX, this.y + 11.0f, new Color(40, 40, 40).getRGB());
        if (this.value instanceof BoolValue) {
            this.change = (Boolean)this.value.get();
        } else if (this.value instanceof ListValue) {
            this.name = String.valueOf(this.value.get()).toUpperCase();
        } else if (this.value instanceof IntegerValue) {
            IntegerValue v = (IntegerValue)this.value;
            this.name = String.valueOf(((Number)v.get()).doubleValue());
            if (mouseX > this.x - 9 && mouseX < this.x + 87 && (float)mouseY > this.y - 4.0f && (float)mouseY < this.y + (float)font.field_78288_b + 4.0f && Mouse.isButtonDown((int)0)) {
                double min = v.getMinimum();
                double max = v.getMaximum();
                double inc = 1.0;
                double valAbs = mouseX - (this.x + 1);
                double perc = valAbs / 68.0;
                perc = Math.min(Math.max(0.0, perc), 1.0);
                double valRel = (max - min) * perc;
                double val = min + valRel;
                val = (double)Math.round(val * 1.0) / 1.0;
                v.set((int)val);
            }
            double number = 86.0f * (((Number)v.get()).floatValue() - (float)v.getMinimum()) / (float)(v.getMaximum() - v.getMinimum());
            GlStateManager.func_179094_E();
            GlStateManager.func_179109_b((float)-9.0f, (float)1.0f, (float)0.0f);
            RenderUtils.drawRect23(this.x + 1, this.y - 6.0f, (float)this.x + 87.0f + (float)parent.allX, this.y + (float)font.field_78288_b + 6.0f, new Color(29, 29, 29).getRGB());
            RenderUtils.drawRect23((double)(this.x + 1), (double)(this.y - 6.0f), (double)this.x + number + 1.0 + (double)parent.allX, (double)(this.y + (float)font.field_78288_b + 6.0f), valuebackcolor);
            GlStateManager.func_179121_F();
        }
        if (this.value instanceof BoolValue) {
            int size = 2;
            if (this.change) {
                RenderUtils.drawRect23(this.x + 62 + 2 + parent.allX + 4, this.y - 4.0f + 2.0f - 1.0f, this.x + 76 - 2 + parent.allX + 4, this.y + 9.0f - 2.0f, new Color(29, 29, 29).getRGB());
                RenderUtils.drawRect23(this.x + 62 + 2 + parent.allX + 5, this.y - 4.0f + 2.0f, this.x + 76 - 2 + parent.allX + 3, this.y + 8.0f - 2.0f, new Color(255, 255, 255, 218).getRGB());
            } else {
                RenderUtils.drawRect23(this.x + 62 + 2 + parent.allX + 4, this.y - 4.0f + 2.0f - 1.0f, this.x + 76 - 2 + parent.allX + 4, this.y + 9.0f - 2.0f, new Color(29, 29, 29).getRGB());
            }
        }
        if (!(this.value instanceof IntegerValue)) {
            font.func_175063_a(this.value.getName(), this.x - 7 + 2, this.y, -1);
        }
        if (this.value instanceof BoolValue) {
            font.func_175063_a(this.name, this.x + font.func_78256_a(this.value.getName()) + 2, this.y, -1);
        }
        if (this.value instanceof IntegerValue) {
            font.func_175063_a(this.value.getName(), this.x - 7, this.y - 1.0f, -1);
            font.func_175063_a(this.name, this.x + font.func_78256_a(this.value.getName()) + 2, this.y - 1.0f, -1);
        }
        if (this.value instanceof ListValue) {
            font.func_175063_a(this.name, this.x + 90 - font.func_78256_a(this.name) + 2, this.y, -1);
        }
    }

    public void key(char typedChar, int keyCode) {
    }

    public void click(int mouseX, int mouseY, int button) {
        if (!this.custom && mouseX > this.x - 9 && mouseX < this.x + 87 && (float)mouseY > this.y - 4.0f && (float)mouseY < this.y + (float)Fonts.font35.field_78288_b + 4.0f) {
            if (this.value instanceof BoolValue) {
                BoolValue m1;
                m1.set((Boolean)(m1 = (BoolValue)this.value).get() == false);
                return;
            }
            if (this.value instanceof ListValue) {
                ListValue m = (ListValue)this.value;
                if (button == 0 || button == 1) {
                    List<String> options = Arrays.asList(m.getValues());
                    int index = options.indexOf(m.get());
                    index = button == 0 ? ++index : --index;
                    if (index >= options.size()) {
                        index = 0;
                    } else if (index < 0) {
                        index = options.size() - 1;
                    }
                    this.value.set(m.getValues()[index]);
                }
            }
        }
    }
}

