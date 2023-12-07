package Velo.api.ClickGui.Display.VapeGui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import Velo.api.Main.Main;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Module.Module.Category;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.Render.RenderUtils;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.api.setting.Setting;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class VapeClickGui extends GuiScreen {

    private boolean close = false;
    private boolean closed;






    private float dragX, dragY;
    private boolean drag = false;
    private int valuemodx = 0;
    private static float modsRole, modsRoleNow;
    private static float valueRoleNow, valueRole;

    public static ArrayList<Config> configs = new ArrayList<Config>();



    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    @Override
    public void initGui() {
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;

        configs.clear();

       
      
    //    configInputBox = new EmptyInputBox(2, mc.fontRendererObj, 0, 0, 85, 10);
    }


    /*
    ????? = 500
    ????? = 310
    ???????? = 100
    ???? = 325(??values)
    ?????? = 60
     */


    static float windowX = 200, windowY = 200;
    static float width = 500, height = 310;

    static ClickType selectType = ClickType.Home;
    static Category category = Category.COMBAT;
    static Module selectMod;

    float[] typeXAnim = new float[]{windowX + 10, windowX + 10, windowX + 10, windowX + 10};

    float hy = windowY + 40;

    Timer valuetimer = new Timer();

    public float smoothTrans(double current, double last){
        return (float) (current + (last - current) / (Minecraft.debugFPS / 10));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sResolution = new ScaledResolution(mc);
        ScaledResolution sr = new ScaledResolution(mc);


        float outro = smoothTrans(this.outro, lastOutro);
        if (mc.currentScreen == null) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(outro, outro, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        }


        //animation
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);


        if (percent > 0.98) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(percent, percent, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        } else {
            if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }
        }


        if(percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if(percent >= 1.4  &&  close){
            percent = 1.5f;
            closed = true;
            mc.currentScreen = null;
        }


       
        	Gui.drawGradientRect(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(101, 0, 255, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());
        
//        if (inAnim > 0) {
//            inAnim -= 5000f / mc.debugFPS;
//        } else {
//            inAnim += 0.1f;
//        }
//        if (inAnim > 5) {
//            GlStateManager.translate(inAnim, 0, 0);
//        }

        //??
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
            drag = true;
        } else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }


        //?????
       RenderUtils.drawRect(windowX, windowY, windowX + width, windowY + height, new Color(21, 22, 25).getRGB());
        if (selectMod == null) {
            Fonts.f.drawString(Main.name.toUpperCase(Locale.ROOT), windowX + 20, windowY + height - 20, new Color(77, 78, 84).getRGB());
        }
        //??????
        float typeX = windowX + 20;
        int i = 0;
        for (Enum<?> e : ClickType.values()) {
            if (!isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] += (typeX - typeXAnim[i]) / 20;
                }
            } else {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] = typeX;
                }
            }
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    RenderUtils.drawImage(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), new Color(255, 255, 255));
                    Fonts.f.drawString(e.name(), typeXAnim[i] + 20, windowY + 15, new Color(255, 255, 255).getRGB());
                    typeX += (32 + Fonts.fs.getStringWidth(e.name() + " "));
                } else {
                    RenderUtils.drawImage(typeXAnim[i], windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), new Color(79, 80, 86));
                    typeX += (32);
                }
            } else {
                RenderUtils.drawImage(windowX + width - 20, windowY + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), e == selectType ? new Color(255, 255, 255) : new Color(79, 80, 86));
            }
            i++;
        }


        if (selectType == ClickType.Home) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(0, 2 * ((int) (sr.getScaledHeight_double() - (windowY + height))) + 40, (int) (sr.getScaledWidth_double() * 2), (int) ((height) * 2) - 160);
            if (selectMod == null) {
                //??????
                float cateY = windowY + 65;
                for (Category m : Category.values()) {
                    if (m == category) {
                   
                       RenderUtils.drawRect(windowX + 20, hy + Fonts.fs.getStringHeight("") + 22, windowX + 30, hy + Fonts.fs.getStringHeight("") + 4, new Color(51, 112, 203).getRGB());
                       Fonts.fs.drawString(m.name(), windowX + 20, cateY, -1);
                       if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            hy = cateY;
                        } else {
                            if (hy != cateY) {
                                hy += (cateY - hy) / 10;
                            }
                        }
                    } else {
                        Fonts.fs.drawString(m.name(), windowX + 20, cateY, new Color(108, 109, 113).getRGB());
                    }


                    cateY += 25;
                }
            }
            if (selectMod != null) {
                if (valuemodx > -80) {
                    valuemodx -= 5;
                }
            } else {
                if (valuemodx < 0) {
                    valuemodx += 5;
                }
            }

            if (selectMod != null) {
            	
               RenderUtils.drawRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + height - 20, new Color(32, 31, 35).getRGB());
               RenderUtils.drawRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + 85, new Color(39, 38, 42).getRGB());
                RenderUtils.drawImage(windowX + 435 + valuemodx, windowY + 65, 16, 16, new ResourceLocation("client/vapeclickgui/back.png"), new Color(82, 82, 85));
                if (isHovered(windowX + 435 + valuemodx, windowY + 65, windowX + 435 + valuemodx + 16, windowY + 65 + 16, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    selectMod = null;
                    valuetimer.reset2();
                }


                //??
                int dWheel = Mouse.getDWheel();
                if (isHovered(windowX + 430 + (int) valuemodx, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                    if (dWheel < 0 && Math.abs(valueRole) + 170 < (selectMod.settings.size() * 25)) {
                        valueRole -= 32;
                    }
                    if (dWheel > 0 && valueRole < 0) {
                        valueRole += 32;
                    }
                }

                if (valueRoleNow != valueRole) {
                    valueRoleNow += (valueRole - valueRoleNow) / 20;
                    valueRoleNow = (int) valueRoleNow;
                }

                float valuey = windowY + 100 + valueRoleNow;

                if(selectMod == null) {
                	return;
                }
                
                for (Setting v : selectMod.settings) {
                    if (v instanceof BooleanSetting) {
                    	BooleanSetting bool = (BooleanSetting) v;
                        if (valuey + 4 > windowY + 100) {
                            if (bool.enabled) {
                                Fonts.fs.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 2, -1);
                                v.optionAnim = 100;
                               RenderUtils.drawRoundedRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(101, 0, 172, (int) (v.optionAnimNow / 100 * 255)).getRGB());
                                RenderUtils.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5, new Color(255, 255, 255).getRGB());
                            } else {
                                Fonts.fs.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 2, new Color(73, 72, 76).getRGB());
                                v.optionAnim = 0;
                               RenderUtils.drawRoundedRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(59, 60, 65).getRGB());
                               RenderUtils.drawRoundedRect(windowX + width - 29, valuey + 3, windowX + width - 11, valuey + 11, 3, new Color(32, 31, 35).getRGB());
                                RenderUtils.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5, new Color(59, 60, 65).getRGB());
                            }
                            if (isHovered(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                if (valuetimer.delay(300)) {
                                    ((BooleanSetting)v).setEnabled(!(Boolean) bool.enabled);
                                    valuetimer.reset2();
                                }
                            }
                        }

                        if (v.optionAnimNow != v.optionAnim) {
                            v.optionAnimNow += (v.optionAnim - v.optionAnimNow) / 20;
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.settings) {
                    if (v instanceof NumberSetting) {
                        if (valuey + 4 > windowY + 100) {
NumberSetting num = (NumberSetting) v;
                            float present = (float) (((windowX + width - 11) - (windowX + 450 + valuemodx))
                                    * (( num.getValue()) - ( num).getMinimum())
                                    / (num.getMaximum() - ( num).getMinimum()));

                            Fonts.fs.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 5, new Color(73, 72, 76).getRGB());
                            Fonts.fs.drawCenteredString(num.getValue() + "", windowX + width - 20, valuey + 5, new Color(255, 255, 255).getRGB());
                           Gui.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + width - 11, valuey + 21.5f, new Color(77, 76, 79).getRGB());
                            Gui.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + 450 + valuemodx + present, valuey + 21.5f, new Color(101, 0, 172).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(101, 0, 172).getRGB());

                            if (isHovered(windowX + 450 + valuemodx, valuey + 18, windowX + width - 11, valuey + 23.5f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                float render2 = (float) (num).getMinimum();
                                double max = ( num).getMaximum();
                                double inc = 0.1;
                                double valAbs = (double) mouseX - ((double) (windowX + 450 + valuemodx));
                                double perc = valAbs / (((windowX + width - 11) - (windowX + 450 + valuemodx)));
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                double valRel = (max - render2) * perc;
                                double val = render2 + valRel;
                                val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                                ( num).setValue(Double.valueOf(val));
                            }
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.settings) {
                    if (v instanceof ModeSetting) {
                        ModeSetting modeValue = (ModeSetting) v;
                        
                        if (valuey + 4 > windowY + 100 & valuey < (windowY + height)) {
                           RenderUtils.drawRoundedRect(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                           RenderUtils.drawRoundedRect(windowX + 446 + valuemodx, valuey + 3, windowX + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
							Fonts.fs.drawString(v.getName() + ":" + modeValue.getMode(), windowX + 455 + valuemodx, valuey + 6, new Color(230, 230, 230).getRGB());
                            Fonts.fs.drawString(">", windowX + width - 15, valuey + 6, new Color(73, 72, 76).getRGB());
                            if (isHovered(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
                           
                            	((ModeSetting) v).cycle();
                                
                                valuetimer.reset2();
                            }
                        }
                        valuey += 25;
                    }
                }

            }

            float modY = windowY + 70 + modsRoleNow;
           
            for (Module m : ModuleManager.modules) {
                if (m.category != category)
                    continue;

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (valuetimer.delay(300) && modY + 40 > (windowY + 70) && modY < (windowY + height)) {
                        m.toggle();
                        
                        valuetimer.reset2();
                    }
                } else if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    if (valuetimer.delay(300)) {
                        if (selectMod != m) {
                            valueRole = 0;
                            selectMod = m;
                        } else if (selectMod == m) {
                            selectMod = null;
                        }
                        valuetimer.reset2();
                    }
                }

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
                    if (m.isEnabled()) {
                       RenderUtils.drawRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(43, 41, 45).getRGB());
                    } else {
                       RenderUtils.drawRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(35, 35, 35).getRGB());
                    }
                } else {
                    if (m.isEnabled()) {
                       RenderUtils.drawRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(36, 34, 38).getRGB());
                    } else {
                    	RenderUtils.drawRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, new Color(32, 31, 33).getRGB());
                    }
                }
                RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25,3 , new Color(37, 35, 39));
                RenderUtil.drawRoundedRect(windowX + 410 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25,3 , new Color(39, 38, 42));
                Fonts.fs.drawString(".", windowX + 416 + valuemodx, modY - 5, new Color(66, 64, 70).getRGB());
                Fonts.fs.drawString(".", windowX + 416 + valuemodx, modY - 1, new Color(66, 64, 70).getRGB());
                Fonts.fs.drawString(".", windowX + 416 + valuemodx, modY + 3, new Color(66, 64, 70).getRGB());

        

                if (m.isEnabled()) {
                
                    Fonts.fs.drawString(m.getName(), windowX + 140 + valuemodx, modY, new Color(220, 220, 220).getRGB());
                   
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, 3, new Color(101, 0, 172, (int) (m.optionAnimNow / 100f * 255)));
                   GlStateManager.pushMatrix();
                    RenderUtil.draw2DImage( new ResourceLocation("client/vapeclickgui/module.png"), windowX + 105 + valuemodx, modY, 16, 16, new Color(220, 220, 220));
                   GlStateManager.popMatrix();
                    m.optionAnim = 100;
                	
                    RenderUtils.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(101, 0, 172, (int) (m.optionAnimNow / 100f * 255)).getRGB());
                    RenderUtils.drawCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5, new Color(255, 255, 255).getRGB());
                } else {
                    Fonts.fs.drawString(m.getName(), windowX + 140 + valuemodx, modY , new Color(108, 109, 113).getRGB());
          //          RenderUtils.drawImage(windowX + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(92, 90, 94));
               //     RenderUtils.drawImage(windowX + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(220, 220, 220));
               //     RenderUtils.drawImage(windowX + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(220, 220, 220));
                    RenderUtil.draw2DImage( new ResourceLocation("client/vapeclickgui/module1.png"), windowX + 105 + valuemodx, modY, 16, 16, new Color(220, 220, 220));

                    m.optionAnim = 0;

                    RenderUtils.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(59, 60, 65).getRGB());
                    RenderUtils.drawRoundedRect(windowX + 381 + valuemodx, modY + 3, windowX + 399 + valuemodx, modY + 11, 3, new Color(29, 27, 31).getRGB());
                    RenderUtils.drawCircle(windowX + 385 + 10 * m.optionAnimNow / 100 + valuemodx, modY + 7, 3.5, new Color(59, 60, 65).getRGB());
                }

                if (m.optionAnimNow != m.optionAnim) {
                    m.optionAnimNow += (m.optionAnim - m.optionAnimNow) / 20;
                }


                modY += 40;
            }
            //??
            int dWheel2 = Mouse.getDWheel();
            if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
                if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (ModuleManager.getModulesByCategory(category).size() * 40)) {
                    modsRole -= 32;
                }
                if (dWheel2 > 0 && modsRole < 0) {
                    modsRole += 32;
                }
            }

            if (modsRoleNow != modsRole) {
                modsRoleNow += (modsRole - modsRoleNow) / 20;
                modsRoleNow = (int) modsRoleNow;
            }


            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }

        int dWheel2 = Mouse.getDWheel();
        if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
            if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (ModuleManager.getModulesByCategory(category).size() * 40)) {
                modsRole -= 16;
            }
            if (dWheel2 > 0 && modsRole < 0) {
                modsRole += 16;
            }
        }

        if (modsRoleNow != modsRole) {
            modsRoleNow += (modsRole - modsRoleNow) / 20;
            modsRoleNow = (int) modsRoleNow;
        }

        //?????



    }

    public int findArray(float[] a, float b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        //????
        float typeX = windowX + 20;
        for (Enum<?> e : ClickType.values()) {
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    if (isHovered(typeX, windowY + 10, typeX + 16 + Fonts.fss.getStringWidth(e.name() + " "), windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32 + Fonts.fss.getStringWidth(e.name() + " "));
                } else {
                    if (isHovered(typeX, windowY + 10, typeX + 16, windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32);
                }
            } else {
                if (isHovered(windowX + width - 32, windowY + 10, windowX + width, windowY + 10 + 16, mouseX, mouseY)) {
                    selectType = (ClickType) e;
                }
            }
        }

        if (selectType == ClickType.Home) {
            //????
            float cateY = windowY + 65;
            for (Category m : Category.values()) {

                if (isHovered(windowX, cateY - 8, windowX + 50, cateY + Fonts.fss.getStringHeight("") + 8, mouseX, mouseY)) {
                    if (category != m) {
                        modsRole = 0;
                    }

                    category = m;
                    for (Module mod : ModuleManager.modules){
                        mod.optionAnim = 0;
                        mod.optionAnimNow = 0;

                    }
                }

                cateY += 25;
            }

        }


    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(!closed && keyCode == Keyboard.KEY_ESCAPE){
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }
        
        if(close) {
            this.mc.displayGuiScreen((GuiScreen) null);
        }

        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed(){

    }
    
    
    @Override
    public boolean doesGuiPauseGame() {
    	return false;
    }
}
