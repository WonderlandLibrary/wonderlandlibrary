package wtf.evolution.click;


import net.minecraft.client.gui.GuiScreen;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.StencilUtil;
import wtf.evolution.helpers.animation.Animation;
import wtf.evolution.helpers.animation.Direction;
import wtf.evolution.helpers.animation.impl.EaseBackIn;
import wtf.evolution.helpers.animation.impl.EaseInOutQuad;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.helpers.render.GaussianBlur;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.RoundedUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.impl.Render.ClickGui;
import wtf.evolution.settings.Setting;
import wtf.evolution.settings.options.*;


import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static wtf.evolution.helpers.render.RenderUtil.isHovered;
import static wtf.evolution.helpers.render.RoundedUtil.drawRound;
import static wtf.evolution.helpers.render.RoundedUtil.drawRoundOutline;

public class Screen extends GuiScreen {

    public int x, y;

    public boolean dragging = false;
    public boolean categoryOpen = false;
    public Category selectedType = Category.Combat;
    public Category[] types;


    public Screen() {
        types = Category.values();
        this.x = 200;
        this.y = 100;
    }

    public Animation openAnimation = new EaseBackIn(250, 1, 1);

    public Animation categoryAnimation = new EaseInOutQuad(250, 1);

    public double scrollX, scrollXA;
    public boolean bue;
    public double prevX, prevY;
    public boolean drag;

    public int prevMouseX, prevMouseY;

    public boolean binding;

    @Override
    public void initGui() {
        super.initGui();
        openAnimation = new EaseBackIn(500, 1, 1);
        openAnimation.setDirection(Direction.FORWARDS);
    }

    public String text = "";

    @Override
    public void drawScreen(int mouseX1, int mouseY1, float partialTicks) {
        super.drawScreen(mouseX1, mouseX1, partialTicks);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];

        if (openAnimation.getOutput() == 0) {
            mc.player.closeScreen();
        }


        dragging(mouseX, mouseY);
        ScaledResolution sr = new ScaledResolution(mc);
        x = (int) MathHelper.clamp(x, 0,  ScaleUtil.calc(sr.getScaledWidth()) - 300);
        y = (int) MathHelper.clamp(y, 0, ScaleUtil.calc(sr.getScaledHeight()) - 200);
        GaussianBlur.renderBlur(3);
        drawDefaultBackground();
        RenderUtil.drawRect(0, 0, mc.displayWidth, mc.displayHeight, new Color(new Color(ClickGui.getColor()).getRed(), new Color(ClickGui.getColor()).getGreen(), new Color(ClickGui.getColor()).getBlue(), (int) (openAnimation.getOutput() * 30)).darker().darker().darker().getRGB());
        ScaleUtil.scale_pre();
        categoryAnimation.setDirection(categoryOpen ? Direction.FORWARDS : Direction.BACKWARDS);
        categoryOpen = isHovered(mouseX, mouseY, x - 2, y, (float) (25f + categoryAnimation.getOutput() * 50), 200);




        int alpha = 255;
        RenderUtil.drawBlurredShadow(x, y, 300, 200, 15, Color.BLACK);
        StencilUtil.initStencilToWrite();

        drawRound(x, y, 300, 200, 7, new Color(21, 21, 21, alpha));
        StencilUtil.readStencilBuffer(1);
        drawRound(x, y, 300, 200, 7, new Color(21, 21, 21, alpha));
        // grid
        if (animating) {
            selectedType = preSelected;
            preSelected = null;
            animating = false;
            scrollX = 0;
        }
        for (int i = 0; i < 15; i++) {
            RenderUtil.drawRectWH((float) (x + categoryAnimation.getOutput() * 50f + 5 + i * (25)), y, 0.5f, 200, new Color(25, 25, 25, 255).getRGB());
            RenderUtil.drawRectWH(x + 5, y + i * (25), 300, 0.5f, new Color(25, 25, 25, 255).getRGB());
        }
        float y1;
        {
            List<Module> list = Main.m.getModulesFromCategory(selectedType);
            List<Module> modules = Main.m.getModulesFromCategory(selectedType).stream().filter(f1 -> list.indexOf(f1) % 2 == 0).collect(Collectors.toList());
            List<Module> modules2 = Main.m.getModulesFromCategory(selectedType).stream().filter(f1 -> list.indexOf(f1) % 2 != 0).collect(Collectors.toList());
            int x = (int) (this.x + 35 + categoryAnimation.getOutput() * 50);
            float y = (float) (this.y + 10 + scrollXA);



            for (Module m : modules) {
                float heightBoost = 0;
                for (Setting s : m.getSettingsForGUI()) {
                    if (m.opened) {
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            heightBoost += (b.opened ? b.modes.size() * 10 + 5 : 0);
                        }
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            if (b.hidden.get()) continue;
                            heightBoost += (b.opened ? b.list.size() * 10 + 5 : 0);
                        }

                        if (s instanceof ColorSetting) {
                            if (s.hidden.get()) continue;
                            heightBoost += 20;
                        }
                    }
                }
                drawRound(x, y, 125, 20 + (m.opened ? (m.getSettingsForGUI().size() * 20) : 0) + heightBoost, 3, m.state ? new Color(30, 30, 30, alpha) : new Color(27, 27, 27, alpha));
                Fonts.REG16.drawString(m.name, x + 5, y + 7, m.state ? ClickGui.getColor() : new Color(255, 255, 255, alpha).getRGB());
                if (binding && bindingModule == m) {
                    Fonts.REG16.drawString("[binding..]", x + 120 - Fonts.REG16.getStringWidth("[" + "binding.." + "]"), y + 7, new Color(255, 255, 255, alpha).getRGB());
                } else if (m.bind > 1) {
                    Fonts.REG16.drawString("[" + Keyboard.getKeyName(m.bind) + "]", x + 120 - Fonts.REG16.getStringWidth("[" + Keyboard.getKeyName(m.bind) + "]"), y + 7, new Color(255, 255, 255, alpha).getRGB());
                }



                if (m.opened) {
                    for (Setting s : m.getSettingsForGUI()) {
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            if (b.hidden.get()) continue;
                            y += 20;
                            drawRound(x + 2, y + 5, 121, 10, 3, new Color(36, 36, 36, alpha));
                            Fonts.REG16.drawString(b.name, x + 3, y - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.REG16.drawCenteredString("selected " + b.selected.size() + "/" + b.list.size(), x + 60, y + 7, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {

                                drawRound(x + 2, y + 20, 121, (b.opened ? b.list.size() * 10 : 0), 3, new Color(36, 36, 36, alpha));
                                for (int i = 0; i < b.list.size(); i++) {
                                    Fonts.REG16.drawCenteredString(b.list.get(i), x + 60, y + 22 + (i * 10), b.selected.contains(b.list.get(i)) ? ClickGui.getColor() : -1);
                                }
                                y += b.list.size() * 10 + 5;
                            }
                        }
                        if (s instanceof BooleanSetting) {
                            BooleanSetting b = (BooleanSetting) s;
                            if (b.hidden.get()) continue;
                            y += 20;
                            b.animation.setDirection((b.get() ? Direction.FORWARDS : Direction.BACKWARDS));
                            drawRound(x + 2, y, 121, 15, 3, new Color(36, 36, 36, alpha));
                            drawRound(x + 100, y + 5, 10, 5, 2, new Color(45, 45, 45, alpha));
                            RenderUtil.drawBlurredShadow((float) (x + 100 + (b.animation.getOutput() * 5)), y + 5, 5, 5, 15, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                            drawRound((float) (x + 100 + b.animation.getOutput() * 5), y + 5, 5, 5, 2, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                            Fonts.REG16.drawString(b.name.toLowerCase(), x + 5, y + 4, new Color(100, 100, 100, alpha).getRGB());

                        }
                        if (s instanceof SliderSetting) {
                            SliderSetting b = (SliderSetting) s;
                            if (b.hidden.get()) continue;
                            y += 20;
                            drawRound(x + 2, y, 121, 15, 3, new Color(36, 36, 36, alpha));
                            if (b.sliding)
                                b.current = (float) MathHelper.round(net.minecraft.util.math.MathHelper.clamp((float) ((double) (mouseX - x - 120) * (b.maximum - b.minimum) / (double) 115 + b.maximum), b.minimum, b.maximum), b.increment);
                            b.sliderWidth = MathHelper.interpolate((((b.current) - b.minimum) / (b.maximum - b.minimum)) * 115, b.sliderWidth, 0.3);
                            float amountWidth = ((b.current) - b.minimum) / (b.maximum - b.minimum);
                            drawRound(x + 5, y + 9, 115, 2, 1, new Color(56, 56, 56, alpha));
                            RenderUtil.drawBlurredShadow((float) (x + 5), y + 9, b.sliderWidth, 2, 15, new Color(ClickGui.getColor()));
                            drawRound((float) (x + 5), y + 9, b.sliderWidth, 2, 1, new Color(ClickGui.getColor()));
                            RenderUtil.drawBlurredShadow((float) (x + 3) + b.sliderWidth, y + 8, 4, 4, 15, new Color(ClickGui.getColor()));
                            drawRound((float) (x + 3) + b.sliderWidth, y + 8, 4, 4, 1.5f, new Color(ClickGui.getColor()));
                            Fonts.REG12.drawString(String.valueOf(b.current), x + 120 - Fonts.REG16.getStringWidth(String.valueOf(b.current)), y + 2, new Color(100, 100, 100, alpha).getRGB());
                            Fonts.REG12.drawString(b.name, x + 5, y + 2, new Color(100, 100, 100, alpha).getRGB());
                        }
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            y += 20;

                            drawRound(x + 2, y + 5, 121, 10, 3, new Color(36, 36, 36, alpha));
                            Fonts.REG16.drawString(b.name, x + 3, y - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.REG16.drawCenteredString(b.currentMode, x + 60, y + 7, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {

                                drawRound(x + 2, y + 20, 121, (b.opened ? b.modes.size() * 10 : 0), 3, new Color(36, 36, 36, alpha));
                                for (int i = 0; i < b.modes.size(); i++) {
                                    Fonts.REG16.drawCenteredString(b.modes.get(i), x + 60, y + 22 + (i * 10), b.currentMode.equalsIgnoreCase(b.modes.get(i)) ? ClickGui.getColor() : new Color(255, 255, 255, alpha).getRGB());
                                }
                                y += b.modes.size() * 10 + 5;
                            }
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting b = (ColorSetting) s;
                            if (b.hidden.get()) continue;
                            y += 20;

                            double soX = mouseX - x;
                            double soY = mouseY - y;
                            soX -= 20;
                            soY -= 17.5;
                            double dst = Math.sqrt(soX * soX + soY * soY);
                            double dst1 = Math.sqrt(soX * soX);
                            final float[] hsb = Color.RGBtoHSB(new Color(b.get()).getRed(), new Color(b.get()).getGreen(), new Color(b.get()).getBlue(), null);

                            double poX =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(hsb[0] * 360)) / Math
                                            .sin(Math.toRadians(90)));
                            double poY =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(90 - (hsb[0] * 360))) / Math
                                            .sin(Math.toRadians(90)));
                            if (dst > 15) {
                                b.slid = false;
                            }
                            if (b.slid) {
                                b.color = Color.HSBtoRGB((float) (Math.atan2(soX, soY) / (Math.PI * 2) - 1), (float) (dst / 15), 1);
                            }
                            drawRound(x + 2, y, 121, 35, 3, new Color(36, 36, 36, alpha));
                            Fonts.RUB16.drawString(b.name, x + 40, y + 14, new Color(255, 255, 255).getRGB());
                            RenderUtil.drawBlurredShadow(x + 40, y + 25, 20, 5, 10, new Color(b.get()));
                            RenderUtil.drawRectWH(x + 40, y + 25, 20, 5, b.get());
                            RenderUtil.drawColoredCircle(x + 20, y + 17.5, 15, 1);
                            drawRoundCircle(x + 20, y + 17.5f, 32, new Color(36, 36, 36, alpha));
                            RoundedUtil.drawRoundCircle(x + (float) poX + 20.5f, y + (float) poY + 18, 3f, new Color(0, 0, 0, 255));
                            RoundedUtil.drawRoundCircle(x + (float) poX + 20.5f, y + (float) poY + 18, 2, new Color(b.get()));

                            y += 20;
                        }
                    }
                }
                y += 25;

            }

            int x1 = (int) (this.x + 165 + categoryAnimation.getOutput() * 50);
            y1 = (float) (this.y + 10 + scrollXA);
            for (Module m1 : modules2) {
                float heightBoost1 = 0;
                for (Setting s : m1.getSettingsForGUI()) {
                    if (m1.opened) {
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            heightBoost1 += (b.opened ? b.modes.size() * 10 + 5 : 0);
                        }
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            if (b.hidden.get()) continue;
                            heightBoost1 += (b.opened ? b.list.size() * 10 + 5 : 0);
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting b = (ColorSetting) s;
                            if (b.hidden.get()) continue;
                            heightBoost1 += 20;
                        }
                    }
                }

                drawRound(x1, y1, 125, 20 + (m1.opened ? (m1.getSettingsForGUI().size() * 20) : 0) + heightBoost1, 3, m1.state ? new Color(30, 30, 30, alpha) : new Color(27, 27, 27, alpha));
                Fonts.REG16.drawString(m1.name, x1 + 5, y1 + 7, m1.state ? ClickGui.getColor() : new Color(255, 255, 255, alpha).getRGB());
                if (binding && bindingModule == m1) {
                    Fonts.REG16.drawString("[binding..]", x1 + 120 - Fonts.REG16.getStringWidth("[" + "binding.." + "]"), y1 + 7, new Color(255, 255, 255, alpha).getRGB());
                } else if (m1.bind > 1) {
                    Fonts.REG16.drawString("[" + Keyboard.getKeyName(m1.bind) + "]", x1 + 120 - Fonts.REG16.getStringWidth("[" + Keyboard.getKeyName(m1.bind) + "]"), y1 + 7, new Color(255, 255, 255, alpha).getRGB());
                }

                if (m1.opened) {
                    for (Setting s : m1.getSettingsForGUI()) {
                        if (s instanceof ListSetting) {
                            ListSetting b = (ListSetting) s;
                            if (b.hidden.get()) continue;
                            y1 += 20;
                            String finalOut = "";
                            drawRound(x1 + 2, y1 + 5, 121, 10, 3, new Color(36, 36, 36, alpha));
                            Fonts.REG16.drawString(b.name, x1 + 3, y1 - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.REG16.drawCenteredString("selected " + b.selected.size() + "/" + b.list.size(), x1 + 60, y1 + 7, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {

                                drawRound(x1 + 2, y1 + 20, 121, (b.opened ? b.list.size() * 10 : 0), 3, new Color(36, 36, 36, alpha));
                                for (int i = 0; i < b.list.size(); i++) {
                                    Fonts.REG16.drawCenteredString(b.list.get(i), x1 + 60, y1 + 22 + (i * 10), b.selected.contains(b.list.get(i)) ? ClickGui.getColor() : -1);
                                    if (b.selected.contains(b.list.get(i)))
                                        finalOut += b.list.get(i).substring(0, 2) + " ";
                                }
                                y1 += b.list.size() * 10 + 5;
                            }
                        }
                        if (s instanceof BooleanSetting) {
                            BooleanSetting b = (BooleanSetting) s;
                            if (b.hidden.get()) continue;
                            y1 += 20;
                            b.animation.setDirection((b.get() ? Direction.FORWARDS : Direction.BACKWARDS));
                            drawRound(x1 + 2, y1, 121, 15, 3, new Color(36, 36, 36, alpha));
                            drawRound(x1 + 100, y1 + 5, 10, 5, 2, new Color(45, 45, 45, alpha));
                            RenderUtil.drawBlurredShadow((float) (x1 + 100 + (b.animation.getOutput() * 5)), y1 + 5, 5, 5, 15, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                            drawRound((float) (x1 + 100 + (b.animation.getOutput() * 5)), y1 + 5, 5, 5, 2, b.get() ? new Color(ClickGui.getColor()) : new Color(89, 89, 89, alpha));
                            Fonts.REG16.drawString(b.name.toLowerCase(), x1 + 5, y1 + 4, new Color(100, 100, 100, alpha).getRGB());
                        }
                        if (s instanceof SliderSetting) {
                            SliderSetting b = (SliderSetting) s;
                            if (b.hidden.get()) continue;
                            y1 += 20;
                            drawRound(x1 + 2, y1, 121, 15, 3, new Color(36, 36, 36, alpha));
                            if (b.sliding)
                                b.current = (float) MathHelper.round(net.minecraft.util.math.MathHelper.clamp((float) ((double) (mouseX - x1 - 120) * (b.maximum - b.minimum) / (double) 115 + b.maximum), b.minimum, b.maximum), b.increment);
                            b.sliderWidth = MathHelper.interpolate((((b.current) - b.minimum) / (b.maximum - b.minimum)) * 115, b.sliderWidth, 0.3);
                            float amountWidth = ((b.current) - b.minimum) / (b.maximum - b.minimum);
                            drawRound(x1 + 5, y1 + 9, 115, 2, 1, new Color(56, 56, 56, alpha));
                            RenderUtil.drawBlurredShadow((float) (x1 + 5), y1 + 9, b.sliderWidth, 2, 15, new Color(ClickGui.getColor()));
                            drawRound((float) (x1 + 5), y1 + 9, b.sliderWidth, 2, 1, new Color(ClickGui.getColor()));
                            RenderUtil.drawBlurredShadow((float) (x1 + 3) + b.sliderWidth, y1 + 8, 4, 4, 15, new Color(ClickGui.getColor()));
                            drawRound((float) (x1 + 3) + b.sliderWidth, y1 + 8, 4, 4, 1.5f, new Color(ClickGui.getColor()));
                            Fonts.REG12.drawString(String.valueOf(b.current), x1 + 120 - Fonts.REG16.getStringWidth(String.valueOf(b.current)), y1 + 2, new Color(100, 100, 100, alpha).getRGB());
                            Fonts.REG12.drawString(b.name, x1 + 5, y1 + 2, new Color(100, 100, 100, alpha).getRGB());
                        }
                        if (s instanceof ModeSetting) {
                            ModeSetting b = (ModeSetting) s;
                            if (b.hidden.get()) continue;
                            y1 += 20;
                            drawRound(x1 + 2, y1 + 5, 121, 10, 3, new Color(36, 36, 36, alpha));
                            Fonts.REG16.drawString(b.name, x1 + 3, y1 - 3, new Color(73, 73, 73, alpha).getRGB());
                            Fonts.REG16.drawCenteredString(b.currentMode, x1 + 60, y1 + 7, new Color(255, 255, 255, alpha).getRGB());

                            if (b.opened) {
                                drawRound(x1 + 2, y1 + 20, 121, (b.opened ? b.modes.size() * 10 : 0), 3, new Color(36, 36, 36, alpha));
                                for (int i = 0; i < b.modes.size(); i++) {
                                    Fonts.REG16.drawCenteredString(b.modes.get(i), x1 + 60, y1 + 22 + (i * 10), b.currentMode.equalsIgnoreCase(b.modes.get(i)) ? ClickGui.getColor() : new Color(255, 255, 255, alpha).getRGB());
                                }
                                y1 += b.modes.size() * 10 + 5;
                            }
                        }
                        if (s instanceof ColorSetting) {
                            ColorSetting b = (ColorSetting) s;
                            if (b.hidden.get()) continue;
                            y1 += 20;
                            double soX = mouseX - x1;
                            double soY = mouseY - y1;
                            soX -= 20;
                            soY -= 17.5;
                            double dst = Math.sqrt(soX * soX + soY * soY);
                            final float[] hsb = Color.RGBtoHSB(new Color(b.get()).getRed(), new Color(b.get()).getGreen(), new Color(b.get()).getBlue(), null);

                            double poX =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(hsb[0] * 360)) / Math
                                            .sin(Math.toRadians(90)));
                            double poY =
                                    hsb[1] * 15 * (Math.sin(Math.toRadians(90 - (hsb[0] * 360))) / Math
                                            .sin(Math.toRadians(90)));
                            if (dst > 15) {
                                b.slid = false;
                            }
                            if (b.slid) {

                                if (getColor() != new Color(36, 36, 36).getRGB()) {
                                    b.color = Color.HSBtoRGB((float) (Math.atan2(soX, soY) / (Math.PI * 2) - 1), (float) (dst / 15), 1);
                                }
                            }
                            drawRound(x1 + 2, y1, 121, 35, 3, new Color(36, 36, 36, alpha));
                            RenderUtil.drawBlurredShadow(x1 + 40, y1 + 25, 20, 5, 10, new Color(b.get()));
                            RenderUtil.drawRectWH(x1 + 40, y1 + 25, 20, 5, b.get());
                            Fonts.RUB16.drawString(b.name, x1 + 40, y1 + 14, new Color(255, 255, 255, alpha).getRGB());

                            RenderUtil.drawColoredCircle(x1 + 20, y1 + 17.5, 15, 1);
                            drawRoundCircle(x1 + 20, y1 + 17.5f, 32, new Color(36, 36, 36, alpha));
                            RoundedUtil.drawRoundCircle(x1 + (float) poX + 20.5f, y1 + (float) poY + 18, 3, new Color(0, 0, 0, 255));
                            RoundedUtil.drawRoundCircle(x1 + (float) poX + 20.5f, y1 + (float) poY + 18, 2, new Color(b.get()));
                            y1 += 20;
                        }
                    }
                }
                y1 += 25;
            }

            scrollX = MathHelper.clamp((float) scrollX, (float) (-Math.max(y - this.y, y1 - this.y) - Math.max(y - this.y, y1 - this.y) + scrollX) + 170, (float) (0));
        }



        RenderUtil.drawRectWH((float) (x - 2), y - 2, (float) (25f + categoryAnimation.getOutput() * 50), 204, new Color(25, 25, 25, alpha).getRGB());
        {
            StencilUtil.initStencilToWrite();
            RenderUtil.drawRectWH((float) (x - 2), y, (float) (25f + categoryAnimation.getOutput() * 50), 200, new Color(25, 25, 25, alpha).getRGB());
            StencilUtil.readStencilBuffer(1);
            int i = 5;
            for (Category type : types) {
                RenderUtil.drawRectWH((float) (x - 1), y + i + 6, (float) (24f + categoryAnimation.getOutput() * 50), 20, selectedType == type ? new Color(30, 30, 30, alpha).getRGB() : new Color(25, 25, 25, alpha).getRGB());
                if (categoryAnimation.getOutput() > 0.05)
                    Fonts.REG16.drawString(type.name(), x + 20, y + 14 + i, new Color(255, 255, 255, (int) net.minecraft.util.math.MathHelper.clamp(categoryAnimation.getOutput() * 255, 30, 255)).getRGB());
                RenderUtil.drawTexture(new ResourceLocation("icons/" + type.name().toLowerCase() + ".png"), (float) (x + 5), (float) (y + 10) + i, 12, 12, new Color(255, 255, 255, alpha));
                i += 20;
            }
            StencilUtil.uninitStencilBuffer();

        }

        StencilUtil.uninitStencilBuffer();
        prevMouseX = mouseX;
        prevMouseY = mouseY;
        scrollXA = MathHelper.interpolate(scrollX, scrollXA, 0.1);
        scrollX += ((Mouse.getDWheel() / 120f) * 30);

        ScaleUtil.scale_post();

    }

    public static void drawRoundCircle(float x, float y, float radius, Color color) {
        drawRoundOutline(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2) - 0.5f, 0.1f, new Color(0, 0, 0, 0), color);
    }

    public int getColor() {
        final ByteBuffer rgb = BufferUtils.createByteBuffer(100);
        GL11.glReadPixels(Mouse.getX(), Mouse.getY(), 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, rgb);
        return new Color(rgb.get(0) & 0xFF, rgb.get(1) & 0xFF, rgb.get(2) & 0xFF).getRGB();
    }

    public void dragging(int mouseX, int mouseY) {
        if (drag) {
            x = (int) (mouseX + prevX);
            y = (int) (mouseY + prevY);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        drag = false;

        for (Module s : Main.m.m) {
            for (Setting setting : s.getSettingsForGUI()) {
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    sliderSetting.sliding = false;
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting b = (ColorSetting) setting;
                    b.slid = false;
                }
            }

        }
    }


    @Override
    protected void mouseReleased(int mouseX1, int mouseY1, int state) {
        super.mouseReleased(mouseX1, mouseY1, state);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];
        drag = false;
        for (Module s : Main.m.m) {
            for (Setting setting : s.getSettingsForGUI()) {
                if (setting instanceof SliderSetting) {
                    SliderSetting sliderSetting = (SliderSetting) setting;
                    sliderSetting.sliding = false;
                }
                if (setting instanceof ColorSetting) {
                    ColorSetting b = (ColorSetting) setting;
                    b.slid = false;
                }
            }

        }
    }

    public Module bindingModule;
    public Category preSelected;
    public boolean animating;

    public ArrayList<String> texts = new ArrayList<>();

    @Override
    protected void mouseClicked(int mouseX1, int mouseY1, int mouseButton) throws IOException {
        super.mouseClicked(mouseX1, mouseY1, mouseButton);
        int mouseX = (int) ScaleUtil.calc(mouseX1, mouseY1)[0];
        int mouseY = (int) ScaleUtil.calc(mouseX1, mouseY1)[1];
        if (isHovered(mouseX, mouseY, x, y, 300, 5)) {
            drag = true;
            prevX = x - mouseX;
            prevY = y - mouseY;
        }
        int i = 5;
        for (Category type : types) {
            if (isHovered(mouseX, mouseY, (float) (x - 1), y + i + 6, (float) (25f + categoryAnimation.getOutput() * 50), 20)) {
                preSelected = type;
                animating = true;
            }
            i += 20;
        }
        if (categoryOpen) return;
        List<Module> list = Main.m.getModulesFromCategory(selectedType);
        List<Module> modules = Main.m.getModulesFromCategory(selectedType).stream().filter(f1 -> list.indexOf(f1) % 2 == 0).collect(Collectors.toList());
        List<Module> modules2 = Main.m.getModulesFromCategory(selectedType).stream().filter(f1 -> list.indexOf(f1) % 2 != 0).collect(Collectors.toList());

        int x = this.x + 35;
        float y = (float) (this.y + 10 + scrollXA);

        for (Module m : modules) {

            if (isHovered(mouseX, mouseY, x, y, 125, 20)) {
                if (mouseButton == 0)
                    m.toggle();
                if (mouseButton == 2) {
                    bindingModule = m;
                    binding = true;
                }
                if (mouseButton == 1) {
                    m.opened = !m.opened;
                }

            }
            if (m.opened) {
                for (Setting s : m.getSettingsForGUI()) {
                    if (s instanceof ListSetting) {
                        ListSetting b = (ListSetting) s;
                        y += 20;
                        if (RenderUtil.isHovered(mouseX, mouseY, x, y + 5, 125, 10)) {
                            b.opened = !b.opened;
                        }
                        if (b.opened) {
                            RenderUtil.drawRectWH(x, y + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                            for (int i1 = 0; i1 < b.list.size(); i1++) {
                                if (RenderUtil.isHovered(mouseX, mouseY, x, y + 22 + (i1 * 10), 125, 9f)) {
                                    if (b.selected.contains(b.list.get(i1))) {
                                        b.selected.remove(b.list.get(i1));
                                    } else {
                                        b.selected.add(b.list.get(i1));
                                    }
                                }
                            }
                            y += b.list.size() * 10 + 5;
                        }
                    }
                    if (s instanceof SliderSetting) {

                        SliderSetting sl = (SliderSetting) s;
                        if (sl.hidden.get()) continue;
                        y += 20;
                        if (isHovered(mouseX, mouseY, x, y, 125, 15))
                            sl.sliding = true;
                    }
                    if (s instanceof BooleanSetting) {
                        BooleanSetting b = (BooleanSetting) s;
                        if (b.hidden.get()) continue;
                        y += 20;
                        if (isHovered(mouseX, mouseY, x, y, 125, 15)) {
                            b.set(!b.get());
                        }
                    }
                    if (s instanceof ModeSetting) {
                        ModeSetting b = (ModeSetting) s;
                        if (b.hidden.get()) continue;
                        y += 20;
                        if (RenderUtil.isHovered(mouseX, mouseY, x, y + 5, 125, 10)) {
                            b.opened = !b.opened;
                        }
                        if (b.opened) {
                            RenderUtil.drawRectWH(x, y + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                            for (int i1 = 0; i1 < b.modes.size(); i1++) {
                                if (RenderUtil.isHovered(mouseX, mouseY, x, y + 22 + (i1 * 10), 125, 9f))
                                    b.currentMode = b.modes.get(i1);
                            }
                            y += b.modes.size() * 10 + 5;
                        }
                    }
                    if (s instanceof ColorSetting) {
                        ColorSetting b = (ColorSetting) s;
                        if (b.hidden.get()) continue;
                        y += 20;
                        double soX = mouseX - x;
                        double soY = mouseY - y;
                        soX -= 20;
                        soY -= 17.5;
                        double dst = Math.sqrt(soX * soX + soY * soY);
                        if (dst <= 15) {
                            b.slid = true;
                        }
                        y += 20;
                    }
                }

            }
            y += 25;
        }

        int x1 = this.x + 165;
        float y1 = (float) (this.y + 10 + scrollXA);
        for (Module m : modules2) {
            if (isHovered(mouseX, mouseY, x1, y1, 125, 20)) {
                if (mouseButton == 0)
                    m.toggle();
                if (mouseButton == 2) {
                    bindingModule = m;
                    binding = true;
                }
                if (mouseButton == 1) {
                    m.opened = !m.opened;
                }
            }
            if (m.opened) {
                for (Setting s : m.getSettingsForGUI()) {
                    if (s instanceof ListSetting) {
                        ListSetting b = (ListSetting) s;
                        y1 += 20;
                        if (RenderUtil.isHovered(mouseX, mouseY, x1, y1 + 5, 125, 10)) {
                            b.opened = !b.opened;
                        }
                        if (b.opened) {
                            RenderUtil.drawRectWH(x1, y1 + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                            for (int i1 = 0; i1 < b.list.size(); i1++) {
                                if (RenderUtil.isHovered(mouseX, mouseY, x1, y1 + 22 + (i1 * 10), 125, 9f)) {
                                    if (b.selected.contains(b.list.get(i1))) {
                                        b.selected.remove(b.list.get(i1));
                                    } else {
                                        b.selected.add(b.list.get(i1));
                                    }
                                }
                            }
                            y1 += b.list.size() * 10 + 5;
                        }
                    }
                    if (s instanceof SliderSetting) {


                        SliderSetting sl = (SliderSetting) s;
                        if (sl.hidden.get()) continue;
                        y1 += 20;
                        if (isHovered(mouseX, mouseY, x1, y1, 125, 15))
                            sl.sliding = true;
                    }
                    if (s instanceof BooleanSetting) {
                        BooleanSetting b = (BooleanSetting) s;
                        if (b.hidden.get()) continue;
                        y1 += 20;
                        if (isHovered(mouseX, mouseY, x1, y1, 125, 15)) {
                            b.set(!b.get());
                        }
                    }
                    if (s instanceof ModeSetting) {
                        ModeSetting b = (ModeSetting) s;
                        if (b.hidden.get()) continue;
                        y1 += 20;
                        if (RenderUtil.isHovered(mouseX, mouseY, x1, y1 + 5, 125, 10)) {
                            b.opened = !b.opened;
                        }
                        if (b.opened) {
                            RenderUtil.drawRectWH(x1, y1 + 14, 125, 0.5f, new Color(73, 73, 73, 255).getRGB());
                            for (int i1 = 0; i1 < b.modes.size(); i1++) {
                                if (RenderUtil.isHovered(mouseX, mouseY, x1, y1 + 22 + (i1 * 10), 125, 9f))
                                    b.currentMode = b.modes.get(i1);
                            }
                            y1 += b.modes.size() * 10 + 5;
                        }
                    }
                    if (s instanceof ColorSetting) {
                        ColorSetting b = (ColorSetting) s;
                        if (b.hidden.get()) continue;
                        y1 += 20;
                        double soX = mouseX - x1;
                        double soY = mouseY - y1;
                        soX -= 20;
                        soY -= 17.5;
                        double dst = Math.sqrt(soX * soX + soY * soY);
                        if (dst <= 15) {
                            b.slid = true;
                        }
                        y1 += 20;
                    }
                }
            }
            y1 += 25;
        }
    }


    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (binding) {
            if (keyCode == 1) {
                bindingModule.bind = 0;
                binding = false;
                bindingModule = null;
                return;
            }
            bindingModule.bind = keyCode;
            binding = false;
            bindingModule = null;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
