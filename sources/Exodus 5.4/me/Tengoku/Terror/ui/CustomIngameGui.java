/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.Tengoku.Terror.ui;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import me.Tengoku.Terror.API.ExodusAPI;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.events.EventRenderGUI;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.module.render.HUD;
import me.Tengoku.Terror.ui.Draw;
import me.Tengoku.Terror.util.ColorUtils;
import me.Tengoku.Terror.util.font.FontUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class CustomIngameGui {
    static Color colorwave2;
    static int colorwaveint;
    static Color colorwave3;
    public String name = "xodus";
    FontRenderer fr;
    public static int color;
    ScaledResolution sr;
    public String firstLetter = "E";
    public Minecraft mc = Minecraft.getMinecraft();
    static int colorwaveint2;

    public int colorR(int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(237, 53, 40, fArray);
        float f = Math.abs((CustomIngameGui.getOffset() + (float)(n / n2) * 4.0f) % 2.0f - 1.0f);
        f = 0.4f + 0.4f * f;
        fArray[2] = f % 1.0f;
        return Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]);
    }

    static {
        color = 0;
    }

    private static float getOffset() {
        return (float)(System.currentTimeMillis() % 2000L) / 1000.0f;
    }

    public static final Color fade(Color color, int n, int n2) {
        float[] fArray = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), fArray);
        float f = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0f + (float)n / (float)n2 * 1.0f) % 2.0f - 1.0f);
        f = f * 0.35f + 0.29999998f;
        fArray[2] = f % 2.0f;
        return new Color(Color.HSBtoRGB(fArray[0], fArray[1], fArray[2]));
    }

    public CustomIngameGui() {
        this.sr = new ScaledResolution(this.mc);
        this.fr = Minecraft.fontRendererObj;
    }

    public void draw() throws IOException {
        int n;
        int n2;
        EventRenderGUI eventRenderGUI = new EventRenderGUI();
        eventRenderGUI.call();
        Exodus.onEvent(eventRenderGUI);
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Font").getValString();
        String string2 = Exodus.INSTANCE.settingsManager.getSettingByName("HUD Mode").getValString();
        String string3 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Color").getValString();
        if (string2.equalsIgnoreCase("Skeet")) {
            n2 = 1;
            int n3 = 2;
            n = 15;
            Gui.drawRect(n2, n3, n2 + n + this.fr.getStringWidth(ExodusAPI.getUserName()) + 160, n3 + n + 2, new Color(55, 55, 55).darker().getRGB());
            if (string.equalsIgnoreCase("Normal")) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                this.fr.drawStringWithShadow("exodus", 4.0f, 7.0f, -1);
                this.fr.drawStringWithShadow("sense", 40.0f, 7.0f, new Color(29, 153, 145).getRGB());
                this.fr.drawStringWithShadow("|", 80.0f, 7.0f, -1);
                this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + " fps", 90.0f, 7.0f, -1);
                this.fr.drawStringWithShadow("|", 140.0f, 7.0f, -1);
                this.fr.drawStringWithShadow(ExodusAPI.getUserName(), 150.0f, 7.0f, -1);
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                FontUtil.normal.drawStringWithShadow("exodus", 4.0, 7.0f, -1);
                FontUtil.normal.drawStringWithShadow("sense", 38.0, 7.0f, new Color(29, 153, 145).getRGB());
                FontUtil.normal.drawStringWithShadow("|", 80.0, 7.0f, -1);
                FontUtil.normal.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + " fps", 90.0, 7.0f, -1);
                FontUtil.normal.drawStringWithShadow("|", 140.0, 7.0f, -1);
                FontUtil.normal.drawStringWithShadow(ExodusAPI.getUserName(), 150.0, 7.0f, -1);
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            }
        }
        if (string2.equalsIgnoreCase("Exodus")) {
            if (string.equalsIgnoreCase("Normal")) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                this.fr.drawStringWithShadow("E", 4.0f, 4.0f, color);
                this.fr.drawStringWithShadow("xodus", 10.0f, 4.0f, -1);
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            } else {
                GlStateManager.pushMatrix();
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                FontUtil.normal.drawStringWithShadow("E", 2.0, 4.0f, color);
                FontUtil.normal.drawStringWithShadow("xodus", 8.0, 4.0f, -1);
                GlStateManager.translate(4.0f, 4.0f, 0.0f);
                GlStateManager.scale(0.5, 0.5, 1.0);
                GlStateManager.translate(-4.0f, -4.0f, 0.0f);
                GlStateManager.popMatrix();
            }
        }
        if (string2.equalsIgnoreCase("Flux B4")) {
            GL11.glPushMatrix();
            n2 = 2;
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow("b3", 60.0f, 1.0f, -1);
            GL11.glPushMatrix();
            GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow("E", 2.0f, 2.0f, new Color(235, 0, 255).getRGB());
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow("xodus", 2 + Minecraft.fontRendererObj.getStringWidth("F"), 2.0f, new Color(0, 87, 255).getRGB());
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
            Minecraft.getMinecraft();
            String string4 = "X: " + (int)Minecraft.thePlayer.posX;
            int n4 = this.mc.displayWidth / 2;
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow(string4, n4 - (Minecraft.fontRendererObj.getStringWidth("X: " + (int)Minecraft.thePlayer.posX) + 1), Minecraft.displayHeight / 2 - 29, -1);
            Minecraft.getMinecraft();
            String string5 = "Y: " + (int)Minecraft.thePlayer.posY;
            int n5 = this.mc.displayWidth / 2;
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow(string5, n5 - (Minecraft.fontRendererObj.getStringWidth("Y: " + (int)Minecraft.thePlayer.posY) + 1), Minecraft.displayHeight / 2 - 19, -1);
            Minecraft.getMinecraft();
            String string6 = "Z: " + (int)Minecraft.thePlayer.posZ;
            int n6 = this.mc.displayWidth / 2;
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow(string6, n6 - (Minecraft.fontRendererObj.getStringWidth("Z: " + (int)Minecraft.thePlayer.posZ) + 1), Minecraft.displayHeight / 2 - 9, -1);
            Minecraft.getMinecraft();
            Minecraft.fontRendererObj.drawStringWithShadow("\u00a7aName: \u00a7c" + Minecraft.thePlayer.getName(), 3.0f, 16.0f, 0x999999);
            if (this.mc.getCurrentServerData() != null) {
                Minecraft.getMinecraft();
                Minecraft.fontRendererObj.drawStringWithShadow("\u00a7aServer: \u00a7c" + this.mc.getCurrentServerData().serverIP, 3.0f, 25.0f, 0x999999);
            }
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
        n2 = 0;
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Module module3 : Exodus.INSTANCE.moduleManager.getModules()) {
            if (!module3.isToggled() || module3.getDisplayName().equalsIgnoreCase("TabGUI")) continue;
            arrayList.add(module3);
        }
        n = Exodus.INSTANCE.settingsManager.getSettingByName("Show Options").getValBoolean() ? 1 : 0;
        if (n != 0) {
            if (string.equalsIgnoreCase("Normal")) {
                arrayList.sort((module, module2) -> this.fr.getStringWidth(module2.getDisplayName()) - this.fr.getStringWidth(module.getDisplayName()));
            } else {
                arrayList.sort((module, module2) -> (int)FontUtil.normal.getStringWidth(module2.getDisplayName()) - (int)FontUtil.normal.getStringWidth(module.getDisplayName()));
            }
        } else if (string.equalsIgnoreCase("Normal")) {
            arrayList.sort((module, module2) -> this.fr.getStringWidth(module2.getName()) - this.fr.getStringWidth(module.getName()));
        } else {
            arrayList.sort((module, module2) -> (int)FontUtil.normal.getStringWidth(module2.getName()) - (int)FontUtil.normal.getStringWidth(module.getName()));
        }
        double d = n2 * this.fr.FONT_HEIGHT;
        int n7 = 2;
        for (Module module4 : arrayList) {
            if ((double)module4.getTransition() > -9.8) {
                module4.setTransition(module4.getTransition() - 1);
            }
            String string7 = Exodus.INSTANCE.settingsManager.getSettingByClass("Bar Mode", HUD.class).getValString();
            boolean bl = Exodus.INSTANCE.settingsManager.getSettingByClass("Show Background", HUD.class).getValBoolean();
            String string8 = Exodus.INSTANCE.settingsManager.getSettingByName("Wave Color").getValString();
            double d2 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Speed").getValDouble();
            Color color = Color.getHSBColor(0.0f, 0.0f, (float)d2);
            Color color2 = Color.getHSBColor(255.0f, 0.6f, 1.0f);
            int n8 = this.getGradientOffset(Color.BLACK, Color.RED, (double)Math.abs(System.currentTimeMillis() - 100L) / d2).getRGB();
            int n9 = this.sr.getScaledWidth() - Minecraft.fontRendererObj.getStringWidth(module4.getDisplayName()) - 1;
            String string9 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList").getValString();
            if (string3.equalsIgnoreCase("OldSaturn")) {
                CustomIngameGui.color = ColorUtils.getRainbowWave((float)d2, 0.4f, 1.0f, n2 * 100);
            }
            if (string3.equalsIgnoreCase("Astolfo")) {
                CustomIngameGui.color = ColorUtils.astolfo((float)d2, 0.5f, 1.0f, n2 * 50).getRGB();
            }
            if (string3.equalsIgnoreCase("White")) {
                CustomIngameGui.color = -1;
            }
            if (string3.equalsIgnoreCase("Wave")) {
                if (string8.equalsIgnoreCase("Red")) {
                    colorwaveint = new Color(255, 0, 0).brighter().getRGB();
                    CustomIngameGui.color = CustomIngameGui.fade(Color.red, n2, (int)d2).brighter().getRGB();
                }
                if (string8.equalsIgnoreCase("Candy")) {
                    colorwave3 = new Color(255, 0, 0).brighter();
                    colorwave2 = new Color(0, 0, 255).brighter();
                    CustomIngameGui.color = ColorUtils.getMixedColor(colorwave2, colorwave3, n2 * 90, (int)d2).getRGB();
                }
                if (string8.equalsIgnoreCase("Blue")) {
                    colorwaveint = new Color(0, 0, 255).brighter().getRGB();
                    CustomIngameGui.color = CustomIngameGui.fade(new Color(0, 0, 255), n2, (int)d2).brighter().getRGB();
                }
                if (string8.equalsIgnoreCase("Aqua")) {
                    colorwaveint = new Color(40, 184, 176).brighter().getRGB();
                    CustomIngameGui.color = CustomIngameGui.fade(new Color(40, 184, 176), n2, (int)d2).brighter().getRGB();
                }
                if (string8.equalsIgnoreCase("Purple")) {
                    colorwaveint = new Color(128, 0, 128).brighter().getRGB();
                    CustomIngameGui.color = CustomIngameGui.fade(new Color(128, 0, 128), n2, (int)d2).brighter().getRGB();
                }
                if (string8.equalsIgnoreCase("White")) {
                    colorwaveint = -1;
                    CustomIngameGui.color = CustomIngameGui.fade(new Color(255, 255, 255), n2, (int)d2).brighter().getRGB();
                }
            }
            if (string3.equalsIgnoreCase("Static") && string8.equalsIgnoreCase("Red")) {
                colorwaveint = new Color(255, 0, 0).getRGB();
                CustomIngameGui.color = new Color(255, 0, 0).getRGB();
            }
            if (string3.equalsIgnoreCase("Static") && string8.equalsIgnoreCase("Blue")) {
                colorwaveint = new Color(0, 0, 255).getRGB();
                CustomIngameGui.color = new Color(0, 0, 255).getRGB();
            }
            if (string3.equalsIgnoreCase("Static") && string8.equalsIgnoreCase("Aqua")) {
                colorwaveint = new Color(40, 184, 176).getRGB();
                CustomIngameGui.color = new Color(40, 184, 176).getRGB();
            }
            if (string3.equalsIgnoreCase("Static") && string8.equalsIgnoreCase("Purple")) {
                colorwaveint = new Color(255, 0, 255).getRGB();
                CustomIngameGui.color = new Color(255, 0, 255).getRGB();
            }
            if (string3.equalsIgnoreCase("Static") && string8.equalsIgnoreCase("White")) {
                colorwaveint = new Color(255, 255, 255).getRGB();
                CustomIngameGui.color = new Color(255, 255, 255).getRGB();
            }
            if (string3.equalsIgnoreCase("RGB")) {
                CustomIngameGui.color = ColorUtils.getRainbowWave((float)d2, 1.0f, 1.0f, n2 * 100);
            }
            if (string9.equalsIgnoreCase("Left")) {
                GL11.glPushMatrix();
                GL11.glColor3ub((byte)0, (byte)0, (byte)0);
                Draw.fixTextFlickering();
                GL11.glPopMatrix();
                if (string.equalsIgnoreCase("Normal")) {
                    if (n != 0) {
                        this.fr.drawStringWithShadow(module4.getDisplayName(), 2.0f, 35 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                    } else {
                        this.fr.drawStringWithShadow(module4.getName(), 2.0f, 35 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                    }
                } else if (n != 0) {
                    FontUtil.normal.drawStringWithShadow(module4.getDisplayName(), 2.0, 35 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                } else {
                    FontUtil.normal.drawStringWithShadow(module4.getName(), 2.0, 35 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                }
            }
            if (string9.equalsIgnoreCase("Right")) {
                if (bl) {
                    if (n != 0) {
                        if (string.equalsIgnoreCase("Normal")) {
                            if (string7.equalsIgnoreCase("Full")) {
                                Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 8, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                            } else {
                                Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 8, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                            }
                        } else if (string7.equalsIgnoreCase("Full")) {
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 8.0, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                        } else {
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 8.0, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                        }
                    } else if (string.equalsIgnoreCase("Normal")) {
                        if (string7.equalsIgnoreCase("Full")) {
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 8, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                        } else {
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 8, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                        }
                    } else if (string7.equalsIgnoreCase("Full")) {
                        Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 8.0, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                    } else {
                        Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 8.0, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1879048192);
                    }
                }
                if (string7.equalsIgnoreCase("Right")) {
                    if (string3.equalsIgnoreCase("White")) {
                        Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1);
                    } else {
                        GL11.glPushMatrix();
                        GL11.glColor3ub((byte)0, (byte)0, (byte)0);
                        Draw.fixTextFlickering();
                        Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                        GL11.glPopMatrix();
                    }
                }
                if (string7.equalsIgnoreCase("Full")) {
                    float f;
                    if (string3.equalsIgnoreCase("White")) {
                        try {
                            f = this.fr.getStringWidth(((Module)arrayList.get(arrayList.indexOf(module4) + 1)).getDisplayName()) + 9;
                        }
                        catch (Exception exception) {
                            f = this.fr.getStringWidth(module4.getDisplayName()) - 150;
                        }
                        Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 9, n2 * this.fr.FONT_HEIGHT + 10, (float)GuiScreen.width - f, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1);
                        Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 9, n2 * this.fr.FONT_HEIGHT, GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 10 + 2, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT + 1, -1);
                        Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, -1);
                    } else {
                        GL11.glPushMatrix();
                        GL11.glColor3ub((byte)0, (byte)0, (byte)0);
                        Draw.fixTextFlickering();
                        if (string.equalsIgnoreCase("Normal") && n != 0) {
                            try {
                                f = this.fr.getStringWidth(((Module)arrayList.get(arrayList.indexOf(module4) + 1)).getDisplayName()) + 9;
                            }
                            catch (Exception exception) {
                                f = this.fr.getStringWidth(module4.getDisplayName()) - 150;
                            }
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 9, n2 * this.fr.FONT_HEIGHT + 10, (float)GuiScreen.width - f, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 9, n2 * this.fr.FONT_HEIGHT, GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 10 + 2, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT + 1, CustomIngameGui.color);
                            Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                        }
                        if (string.equalsIgnoreCase("Normal") && n == 0) {
                            try {
                                f = this.fr.getStringWidth(((Module)arrayList.get(arrayList.indexOf(module4) + 1)).getName()) + 9;
                            }
                            catch (Exception exception) {
                                f = this.fr.getStringWidth(module4.getName()) - 150;
                            }
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 9, n2 * this.fr.FONT_HEIGHT + 10, (float)GuiScreen.width - f, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                            Gui.drawRect(GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 9, n2 * this.fr.FONT_HEIGHT, GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 10 + 2, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT + 1, CustomIngameGui.color);
                            Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                        }
                        if (string.equalsIgnoreCase("Arial") && n != 0) {
                            try {
                                f = (float)(FontUtil.normal.getStringWidth(((Module)arrayList.get(arrayList.indexOf(module4) + 1)).getDisplayName()) + 9.0);
                            }
                            catch (Exception exception) {
                                f = (float)(FontUtil.normal.getStringWidth(module4.getDisplayName()) - 150.0);
                            }
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 9.0, n2 * this.fr.FONT_HEIGHT + 10, (float)GuiScreen.width - f, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 9.0, n2 * this.fr.FONT_HEIGHT, (double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 10.0 + 2.0, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT + 1, CustomIngameGui.color);
                            Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                        }
                        if (string.equalsIgnoreCase("Arial") && n == 0) {
                            try {
                                f = (float)(FontUtil.normal.getStringWidth(((Module)arrayList.get(arrayList.indexOf(module4) + 1)).getName()) + 9.0);
                            }
                            catch (Exception exception) {
                                f = (float)(FontUtil.normal.getStringWidth(module4.getName()) - 150.0);
                            }
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 9.0, n2 * this.fr.FONT_HEIGHT + 10, (float)GuiScreen.width - f, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                            Gui.drawRect((double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 9.0, n2 * this.fr.FONT_HEIGHT, (double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 10.0 + 2.0, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT + 1, CustomIngameGui.color);
                            Gui.drawRect((float)GuiScreen.width - 1.25f, n2 * this.fr.FONT_HEIGHT, GuiScreen.width, this.fr.FONT_HEIGHT + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                        }
                        GL11.glPopMatrix();
                    }
                }
                if (string.equalsIgnoreCase("Arial")) {
                    if (n != 0) {
                        FontUtil.normal.drawStringWithShadow(module4.getDisplayName(), (double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getDisplayName()) - 4.5 + (double)module4.getTransition() + (double)9.8f, 1 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                    } else {
                        FontUtil.normal.drawStringWithShadow(module4.getName(), (double)GuiScreen.width - FontUtil.normal.getStringWidth(module4.getName()) - 4.5 + (double)module4.getTransition() + (double)9.8f, 1 + n2 * this.fr.FONT_HEIGHT, CustomIngameGui.color);
                    }
                }
                if (string.equalsIgnoreCase("Normal")) {
                    if (n != 0) {
                        Minecraft.fontRendererObj.drawStringWithShadow(module4.getDisplayName(), (float)(GuiScreen.width - this.fr.getStringWidth(module4.getDisplayName()) - 4 + module4.getTransition()) + 9.8f, 4 + n2 * this.fr.FONT_HEIGHT - 4, CustomIngameGui.color);
                    } else {
                        Minecraft.fontRendererObj.drawStringWithShadow(module4.getName(), (float)(GuiScreen.width - this.fr.getStringWidth(module4.getName()) - 4 + module4.getTransition()) + 9.8f, 4 + n2 * this.fr.FONT_HEIGHT - 4, CustomIngameGui.color);
                    }
                }
            }
            ++n2;
            n7 += 12;
        }
    }

    public Color getGradientOffset(Color color, Color color2, double d) {
        int n;
        double d2;
        if (d > 1.0) {
            d2 = d % 1.0;
            n = (int)d;
            d = n % 2 == 0 ? d2 : 1.0 - d2;
        }
        d2 = 1.0 - d;
        n = (int)((double)color.getRed() * d2 + (double)color2.getRed() * d);
        int n2 = (int)((double)color.getGreen() * d2 + (double)color2.getGreen() * d);
        int n3 = (int)((double)color.getBlue() * d2 + (double)color2.getBlue() * d);
        return new Color(n, n2, n3);
    }

    public static int getColorInt(int n) {
        int n2 = 0;
        double d = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Speed").getValDouble();
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Wave Color").getValString();
        String string2 = Exodus.INSTANCE.settingsManager.getSettingByName("ArrayList Color").getValString();
        if (string2.equalsIgnoreCase("OldSaturn")) {
            n2 = ColorUtils.getRainbowWave((float)d, 0.4f, 1.0f, n * 100);
        }
        if (string2.equalsIgnoreCase("Astolfo")) {
            n2 = ColorUtils.astolfo((float)d, 0.5f, 1.0f, n * 50).getRGB();
        }
        if (string2.equalsIgnoreCase("White")) {
            n2 = -1;
        }
        if (string2.equalsIgnoreCase("Static")) {
            n2 = colorwaveint;
        }
        if (string2.equalsIgnoreCase("Wave")) {
            if (string.equalsIgnoreCase("Red")) {
                n2 = CustomIngameGui.fade(Color.red, n, (int)d).getRGB();
            }
            if (string.equalsIgnoreCase("Candy")) {
                colorwave3 = new Color(255, 0, 0).brighter();
                colorwave2 = new Color(0, 0, 255).brighter();
                n2 = ColorUtils.getMixedColor(colorwave2, colorwave3, n * 20, (int)d).getRGB();
            }
            if (string.equalsIgnoreCase("Blue")) {
                n2 = CustomIngameGui.fade(new Color(0, 0, 255), n, (int)d).getRGB();
            }
            if (string.equalsIgnoreCase("Aqua")) {
                n2 = CustomIngameGui.fade(new Color(40, 184, 176), n, (int)d).getRGB();
            }
            if (string.equalsIgnoreCase("Purple")) {
                n2 = CustomIngameGui.fade(new Color(128, 0, 128), n, (int)d).getRGB();
            }
            if (string.equalsIgnoreCase("White")) {
                n2 = CustomIngameGui.fade(new Color(255, 255, 255), n, (int)d).getRGB();
            }
        }
        if (string2.equalsIgnoreCase("RGB")) {
            n2 = ColorUtils.getRainbowWave((float)d, 1.0f, 1.0f, n * 100);
        }
        return n2;
    }

    public static class ModuleComparator
    implements Comparator<Module> {
        @Override
        public int compare(Module module, Module module2) {
            Minecraft.getMinecraft();
            int n = Minecraft.fontRendererObj.getStringWidth(module.getName());
            Minecraft.getMinecraft();
            if (n > Minecraft.fontRendererObj.getStringWidth(module2.getName())) {
                return -1;
            }
            Minecraft.getMinecraft();
            int n2 = Minecraft.fontRendererObj.getStringWidth(module.getName());
            Minecraft.getMinecraft();
            if (n2 < Minecraft.fontRendererObj.getStringWidth(module2.getName())) {
                return 1;
            }
            return 0;
        }
    }
}

