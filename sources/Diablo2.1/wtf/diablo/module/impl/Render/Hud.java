package wtf.diablo.module.impl.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import wtf.diablo.Diablo;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.utils.animations.Animation;
import wtf.diablo.utils.font.Fonts;
import wtf.diablo.utils.font.MCFontRenderer;
import wtf.diablo.utils.render.ColorUtil;
import wtf.diablo.utils.render.RenderUtil;

import java.awt.*;


public class Hud extends Module {
    public static BooleanSetting customFont = new BooleanSetting("Custom Font", true);
    public static BooleanSetting hideVisuals = new BooleanSetting("Hide Visuals", false);
    public static BooleanSetting blurOn = new BooleanSetting("Blur", false);
    public static ModeSetting hudType = new ModeSetting("Type", "Diablo","Sigma", "Smooth");
    public static ModeSetting colorMode = new ModeSetting("Color", "Astolfo", "Rainbow","LightRainbow", "Static");
    public static ModeSetting suffixMode = new ModeSetting("Suffix", "None", "Dash","Boxed");


    public Hud() {
        super("Hud", "Displays Stuff", Category.RENDER, ServerType.All);
        addSettings(hudType, colorMode, customFont,hideVisuals,suffixMode, blurOn);
        setTogglable(false);
        setToggled(true);
    }

    public void drawHud(ScaledResolution sr) {
        String bps = "BPS\247s: " + String.format("%.2f", Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (double) mc.timer.timerSpeed * 20.0D);
        String coords = "XYZ\247s: " + mc.thePlayer.getPosition().getX() + "," + mc.thePlayer.getPosition().getY() + "," + mc.thePlayer.getPosition().getZ();
        String titleString = Diablo.name.charAt(0) + ColorUtil.COLORRESET + Diablo.name.substring(1) + " " + Diablo.version;
        String fps = "FPS\247s: " + Minecraft.getDebugFPSString();
        int count = 0;
        switch (hudType.getMode()) {
            case "Smooth":
                Fonts.axi45.drawString("Diablo", 5, 5, 0x9FFFFFFF);
                Fonts.axi24.drawString("Jello ", 6, -3 + Fonts.axi45.getHeight(), 0x9FFFFFFF);

                for (Module mod : Diablo.moduleManager.sortedLength(Fonts.axi18)) {
                    mod.updateRender();
                    if(!mod.isToggled() && !mod.isAnimating())
                        continue;
                    double animValue = 20 + Fonts.axi18.getStringWidth(mod.getFormattedName());
                    Fonts.axi18.drawString(mod.getFormattedName(), sr.getScaledWidth() - Fonts.axi18.getStringWidth(mod.getFormattedName()) - 5 + Animation.getDoubleFromPercentage(100 - mod.animation.getPercent(),animValue), 5 + (count * (Fonts.axi18.getHeight())), 0x9FFFFFFF);
                    count++;
                }

                break;
            case "Sigma":
                Gui.drawRect(2, 2, 55, 20, 0x81111111);
                Gui.drawRect(55, 2, 175, 15, 0x81111111);
                Gui.drawRect(7, 18, 50, 19, 0xcbcbcbcb);
                Fonts.axi24.drawString("Sigma", 4, 3, 0xcbcbcbcb);
                Fonts.axi12.drawRainbowString("v4.0", Fonts.axi24.getStringWidth("Sigma") + 3, 3, 5);
                Fonts.axi18.drawRainbowString("Beta " + Diablo.version, 57, 3 + (Fonts.axi18.getHeight() / 2f) - (15 / 4f), 57);
                Fonts.axi18.drawString("User : No Namer", 60 + Fonts.axi18.getStringWidth("Beta  " + Diablo.version), 3 + (Fonts.axi18.getHeight() / 2f) - (15 / 4f), -1);

                int cc = 0;
                for (Category c : Category.values()) {
                    Gui.drawRect(2, 20 + (cc * 12), 55, 20 + 12 + (cc * 12), 0x81111111);
                    if (cc == 0) {
                        RenderUtil.drawGradientSideways(3, 20 + (cc * 12f), 54, 20 + 12 + (cc * 12f), 0xff192582, 0xff2cc7db);
                    }
                    Fonts.axi16.drawString(c.getName(), 6, 20 + 12 + (cc * 12f) - (Fonts.axi16.getHeight()), -1);
                    cc++;
                }
                count = 0;
                MCFontRenderer arrayListFont = Fonts.SFBold18;
                for(Module mod : Diablo.moduleManager.sortedLengthToggled(arrayListFont,false)){
                    double y = count * 10;
                    int mainColor = ColorUtil.getColor((int) y * 3);
                    double x = sr.getScaledWidth() - arrayListFont.getStringWidth(mod.getFormattedName(false)) - 2;
                    Gui.drawRect(x, y, sr.getScaledWidth(), 10 + y, 0x81111111);
                    arrayListFont.drawString(mod.getFormattedName(false), sr.getScaledWidth() - 1 - (arrayListFont.getStringWidth(mod.getFormattedName(false))),(count * 10) + 10 - arrayListFont.getHeight() - 1, mainColor);
                    count++;
                    Gui.drawRect(x - 1, y, x, y + 11,mainColor);
                    Module nextMod = null;
                    if(Diablo.moduleManager.sortedLengthToggled(arrayListFont,false).size() > count)
                        nextMod = Diablo.moduleManager.sortedLengthToggled(arrayListFont,false).get(count);
                    if(nextMod != null){
                        Gui.drawRect(sr.getScaledWidth() - arrayListFont.getStringWidth(mod.getFormattedName(false)) - 2, y + 10, sr.getScaledWidth() - arrayListFont.getStringWidth(nextMod.getFormattedName(false)) - 2,y + 11, mainColor);
                    }else{
                        Gui.drawRect(sr.getScaledWidth() - 3 - arrayListFont.getStringWidth(mod.getFormattedName(false)), y + 10, sr.getScaledWidth(),y + 11,mainColor);
                    }
                }
                break;
            case "Diablo2":
                String finalStr =  ColorUtil.COLORRESET + Diablo.name + " " + Diablo.version + " | " + "Eclipse Ready by LuMi" + " | " + Diablo.buildType.name();

                double width = mc.fontRendererObj.getStringWidth(finalStr) + 4;

                double xBar = 5;
                double yBar = 5;
                double height = mc.fontRendererObj.FONT_HEIGHT + 2;

                RenderUtil.drawRect(xBar,yBar,xBar+width,yBar+height,new Color(35, 34, 34, 128).getRGB());

                mc.fontRendererObj.drawString(finalStr, (int) xBar, (int) (yBar + 1),-1);

                break;
            default:
                Color backdrop = new Color(26, 26, 26, 179);
                if (!customFont.getValue())
                    mc.fontRendererObj.drawStringWithShadow(titleString, 4.5f, 3.5f, ColorUtil.getColor(0));
                else
                    Fonts.SFReg24.drawStringWithShadow(titleString, 3.5f, 5.5f,  ColorUtil.getColor(0));

                float y = 3f;
                count = 0;
                double xOffset = 3;

                try {
                    for (Module mod : Diablo.moduleManager.sortedLength(Fonts.apple18)) {
                        mod.updateRender();
                        if(!mod.isToggled() && !mod.isAnimating())
                            continue;

                        double animValue = (customFont.getValue() ? Fonts.apple18.getStringWidth(mod.getFormattedName()) : mc.fontRendererObj.getStringWidth(mod.getFormattedName())) * 2f;

                        double x1 = sr.getScaledWidth() - (customFont.getValue() ? Fonts.apple18.getStringWidth(mod.getFormattedName()) : mc.fontRendererObj.getStringWidth(mod.getFormattedName())) - xOffset - 5;
                        x1 += Animation.getDoubleFromPercentage(100 - mod.animation.getPercent(), animValue);
                        double y1 = y;
                        double x2 = sr.getScaledWidth() - xOffset;
                        x2 += Animation.getDoubleFromPercentage(100 - mod.animation.getPercent(), animValue);
                        double y2 = y + 11;

                        RenderUtil.drawRect(x1, y1, x2, y2, backdrop.getRGB());

                        int color = ColorUtil.getColor(count * 20);
                        double finalX = (float) ((Fonts.apple18.getStringWidth(mod.getFormattedName()) - xOffset)) - 3;

                        if(customFont.getValue()) {
                            Fonts.apple18.drawStringWithShadow(mod.getFormattedName(), (float) (sr.getScaledWidth() - Fonts.apple18.getStringWidth(mod.getFormattedName()) - xOffset) - 3 + Animation.getDoubleFromPercentage(100 - mod.animation.getPercent(), animValue), y + 2.5, color);
                        } else {
                            mc.fontRendererObj.drawStringWithShadow(mod.getFormattedName(), (float) ((float) (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(mod.getFormattedName()) - xOffset) - 2.5 + Animation.getDoubleFromPercentage(100 - mod.animation.getPercent(), animValue)), (float) (y + 2.5), color);
                        }

                        RenderUtil.drawRect(x2, y1, x2 + 1, y2, color);
                        y += 11f;
                        count += 1;
                    }
                } catch (Exception ignored) {
                }
                break;
        }
        Fonts.apple18.drawStringWithShadow(fps, 2.0f, sr.getScaledHeight() - 27, ColorUtil.getAstolfoColor(10000, 0).getRGB());
        Fonts.apple18.drawStringWithShadow(bps, 2.0f, sr.getScaledHeight() - 18, ColorUtil.getAstolfoColor(10000, 0).getRGB());
        Fonts.apple18.drawStringWithShadow(coords, 2.0f, sr.getScaledHeight() - 9, ColorUtil.getAstolfoColor(10000, 0).getRGB());

    }
}
