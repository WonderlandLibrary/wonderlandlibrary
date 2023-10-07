/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.ScaledResolution
 *  net.minecraft.util.MathHelper
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.cn.Insane.Module.fonts.impl.Fonts;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Setting;
import net.ccbluex.liquidbounce.cn.Insane.Ui.TestUi.dropdown.Tab;
import net.ccbluex.liquidbounce.features.module.modules.render.ClickGUI;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.Timer;
import net.ccbluex.liquidbounce.value.TextValue;
import net.ccbluex.liquidbounce.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class Module {
    private final net.ccbluex.liquidbounce.features.module.Module module;
    public int yPerModule;
    public int y;
    public Tab tab;
    public boolean opened;
    public List<Setting> settings = new CopyOnWriteArrayList<Setting>();
    public Timer hoverTimer = new Timer();
    private double length = 3.0;
    private double anim = 5.0;
    private int alph = 0;
    float fraction = 0.0f;
    float fractionBackground = 0.0f;
    private float alpha = 0.0f;

    public Module(net.ccbluex.liquidbounce.features.module.Module module, Tab tab) {
        this.module = module;
        this.tab = tab;
        for (Value<?> setting : module.getValues()) {
            this.settings.add(new Setting(setting, this));
        }
    }

    public void drawScreen(int mouseX, int mouseY) {
        Minecraft instance = Minecraft.func_71410_x();
        int debugFPS = instance.func_175610_ah();
        if (this.module.getState() && this.fraction < 1.0f) {
            this.fraction = (float)((double)this.fraction + 0.0025 * (double)(2000 / debugFPS));
        }
        if (!this.module.getState() && this.fraction > 0.0f) {
            this.fraction = (float)((double)this.fraction - 0.0025 * (double)(2000 / debugFPS));
        }
        if (!this.module.getState()) {
            if (this.isHovered(mouseX, mouseY) && this.fractionBackground < 1.0f) {
                this.fractionBackground = (float)((double)this.fractionBackground + 0.0025 * (double)(2000 / debugFPS));
            }
            if (!this.isHovered(mouseX, mouseY) && this.fractionBackground > 0.0f) {
                this.fractionBackground = (float)((double)this.fractionBackground - 0.0025 * (double)(2000 / debugFPS));
            }
        }
        this.fraction = MathHelper.func_76131_a((float)this.fraction, (float)0.0f, (float)1.0f);
        this.fractionBackground = MathHelper.func_76131_a((float)this.fractionBackground, (float)0.0f, (float)1.0f);
        if (this.yPerModule < this.getY()) {
            this.yPerModule = (int)((double)this.yPerModule + (double)(this.getY() + 9 - this.yPerModule) * 0.1);
        } else if (this.yPerModule > this.getY()) {
            this.yPerModule = (int)((double)this.yPerModule + (double)(this.getY() - this.yPerModule) * 0.1);
        }
        this.y = (int)(this.tab.getPosY() + 15.0f);
        for (Module tabModule : this.tab.getModules()) {
            if (tabModule == this) break;
            this.y += tabModule.yPerModule;
        }
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        Color colorHUD = ClickGUI.generateColor();
        Color white = new Color(0xFFFFFF);
        if (colorHUD.getRed() > 220 && colorHUD.getBlue() > 220 && colorHUD.getGreen() > 220) {
            white = colorHUD.darker().darker();
        }
        RenderUtils.drawRect(this.tab.getPosX(), (float)this.y, this.tab.getPosX() + 100.0f, (float)(this.y + this.yPerModule), new Color(40, 40, 40, 255).getRGB());
        if (!this.module.getState() && this.fraction == 0.0f) {
            RenderUtils.drawRect(this.tab.getPosX(), (float)this.y, this.tab.getPosX() + 100.0f, (float)(this.y + 14), this.interpolateColor(new Color(40, 40, 40, 255), new Color(29, 29, 29, 255), MathHelper.func_76131_a((float)this.fractionBackground, (float)0.0f, (float)1.0f)));
        } else {
            RenderUtils.drawRect(this.tab.getPosX(), (float)this.y, this.tab.getPosX() + 100.0f, (float)(this.y + 14), this.interpolateColor(new Color(40, 40, 40, 255), colorHUD, MathHelper.func_76131_a((float)this.fraction, (float)0.0f, (float)1.0f)));
        }
        Fonts.SF.SF_18.SF_18.drawString((CharSequence)this.module.getName(), this.tab.getPosX() + 2.0f, this.y + 4, -1, true);
        if (!this.settings.isEmpty()) {
            double val = (double)debugFPS / 8.3;
            if (this.opened && this.length > -3.0) {
                this.length -= 3.0 / val;
            } else if (!this.opened && this.length < 3.0) {
                this.length += 3.0 / val;
            }
            if (this.opened && this.anim < 8.0) {
                this.anim += 3.0 / val;
            } else if (!this.opened && this.anim > 5.0) {
                this.anim -= 3.0 / val;
            }
            RenderUtils.drawArrow(this.tab.getPosX() + 92.0f, (double)this.y + this.anim, 2, -1, this.length);
        }
        if (this.opened || this.yPerModule != 14) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.func_71410_x());
            if (this.yPerModule != this.getY() && scaledResolution.func_78325_e() != 1) {
                GL11.glScissor((int)0, (int)(scaledResolution.func_78328_b() * 2 - this.y * 2 - this.yPerModule * 2), (int)(scaledResolution.func_78326_a() * 2), (int)(this.yPerModule * 2));
                GL11.glEnable((int)3089);
                this.settings.stream().filter(s -> s.setting.getDisplayable()).forEach(setting -> setting.drawScreen(mouseX, mouseY));
                GL11.glDisable((int)3089);
                this.settings.stream().filter(s -> !s.setting.getDisplayable()).forEach(setting -> setting.setPercent(0.0f));
            } else {
                this.settings.stream().filter(s -> s.setting.getDisplayable()).forEach(setting -> setting.drawScreen(mouseX, mouseY));
            }
        } else {
            this.settings.forEach(setting -> setting.setPercent(0.0f));
        }
    }

    private int interpolateColor(Color color1, Color color2, float fraction) {
        int red = (int)((float)color1.getRed() + (float)(color2.getRed() - color1.getRed()) * fraction);
        int green = (int)((float)color1.getGreen() + (float)(color2.getGreen() - color1.getGreen()) * fraction);
        int blue = (int)((float)color1.getBlue() + (float)(color2.getBlue() - color1.getBlue()) * fraction);
        int alpha = (int)((float)color1.getAlpha() + (float)(color2.getAlpha() - color1.getAlpha()) * fraction);
        try {
            return new Color(red, green, blue, alpha).getRGB();
        }
        catch (Exception ex) {
            return -1;
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (this.opened) {
            this.settings.stream().filter(s -> s.setting.getDisplayable()).forEach(setting -> setting.keyTyped(typedChar, keyCode));
        }
    }

    public int getY() {
        if (this.opened) {
            int gay = 17;
            for (Setting setting : this.settings.stream().filter(s -> s.setting.getDisplayable()).collect(Collectors.toList())) {
                gay += 15;
            }
            return this.tab.modules.indexOf(this) == this.tab.modules.size() - 1 ? gay : gay;
        }
        return 14;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (this.isHovered(mouseX, mouseY)) {
            switch (mouseButton) {
                case 0: {
                    this.module.toggle();
                    break;
                }
                case 1: {
                    if (this.module.getValues().isEmpty()) break;
                    ClickGUI clickGUI = (ClickGUI)LiquidBounce.moduleManager.getModule(ClickGUI.class);
                    if (!this.opened && ((Boolean)clickGUI.getClosePrevious.get()).booleanValue()) {
                        this.tab.modules.forEach(module -> {
                            if (module.opened) {
                                module.opened = false;
                            }
                        });
                    }
                    this.opened = !this.opened;
                    for (Value<?> setting2 : this.module.getValues()) {
                        if (!(setting2 instanceof TextValue)) continue;
                        setting2.setTextHovered(false);
                    }
                    break;
                }
            }
        }
        if (this.opened) {
            this.settings.stream().filter(s -> s.setting.getDisplayable()).forEach(setting -> {
                try {
                    setting.mouseClicked(mouseX, mouseY, mouseButton);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.opened) {
            this.settings.stream().filter(s -> s.setting.getDisplayable()).forEach(setting -> setting.mouseReleased(mouseX, mouseY, state));
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        this.y = (int)(this.tab.getPosY() + 15.0f);
        for (Module tabModule : this.tab.getModules()) {
            if (tabModule == this) break;
            this.y += tabModule.yPerModule;
        }
        if (this.opened) {
            return (float)mouseX >= this.tab.getPosX() && mouseY >= this.y && (float)mouseX <= this.tab.getPosX() + 101.0f && mouseY <= this.y + 14;
        }
        return (float)mouseX >= this.tab.getPosX() && mouseY >= this.y && (float)mouseX <= this.tab.getPosX() + 101.0f && mouseY <= this.y + this.yPerModule;
    }

    private void update() {
    }

    public net.ccbluex.liquidbounce.features.module.Module getModule() {
        return this.module;
    }
}

