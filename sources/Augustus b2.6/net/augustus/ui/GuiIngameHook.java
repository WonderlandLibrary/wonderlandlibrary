// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.ui;

import com.sun.javafx.geom.Vec2d;
import net.minecraft.client.gui.FontRenderer;
import java.text.DateFormat;
import net.augustus.font.testfontbase.FontUtil;
import java.awt.Color;
import net.augustus.utils.RenderUtil;
import net.augustus.Augustus;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.utils.ResourceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.augustus.font.CustomFontUtil;
import net.augustus.utils.RainbowUtil;
import net.augustus.utils.interfaces.MC;
import net.augustus.utils.interfaces.MM;
import net.minecraft.client.gui.GuiIngame;

public class GuiIngameHook extends GuiIngame implements MM, MC
{
    private boolean leftSide;
    private int lastWidth;
    private float yAdd;
    public static float lastArrayListY;
    private final RainbowUtil rainbowUtil;
    public static CustomFontUtil customFont;
    private ResourceLocation augustusResourceLocation;
    private ResourceLocation amogusResourceLocation;
    
    public GuiIngameHook(final Minecraft client) {
        super(client);
        this.leftSide = false;
        this.lastWidth = 0;
        this.yAdd = 0.0f;
        this.rainbowUtil = new RainbowUtil();
        this.augustusResourceLocation = null;
        this.amogusResourceLocation = null;
        this.augustusResourceLocation = ResourceUtil.loadResourceLocation("pictures/augustus.png", "augustus");
        this.amogusResourceLocation = ResourceUtil.loadResourceLocation("pictures/amogus.png", "amogus");
        GuiIngameHook.customFont = new CustomFontUtil("Verdana", 16);
    }
    
    @Override
    public void renderGameOverlay(final float partialTicks) {
        super.renderGameOverlay(partialTicks);
        if (GuiIngameHook.mm.arrayList.isToggled()) {
            GuiIngameHook.mm.arrayList.renderArrayList();
        }
        if (GuiIngameHook.mm.hud.isToggled()) {
            this.hud();
        }
        else {
            this.yAdd = 0.0f;
        }
        if (GuiIngameHook.mm.hud.isToggled() && GuiIngameHook.mm.hud.armor.getBoolean()) {
            this.drawArmorHud();
        }
    }
    
    @Override
    public void updateTick() {
        super.updateTick();
        if (GuiIngameHook.mm.arrayList.isToggled()) {}
    }
    
    private void hud() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final String selected = GuiIngameHook.mm.hud.mode.getSelected();
        switch (selected) {
            case "Other": {
                GL11.glPushMatrix();
                GL11.glScaled(1.5, 1.5, 0.0);
                this.yAdd = (float)(int)((this.getFontRenderer().FONT_HEIGHT * 1.75f + 4.0f + 1.0f) * 1.5);
                final String selected2 = GuiIngameHook.mm.hud.side.getSelected();
                switch (selected2) {
                    case "Left": {
                        if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                            Gui.drawRect(0, 0, (int)(this.getFontRenderer().getStringWidth("A") * 1.75f + this.getFontRenderer().getStringWidth("ugustus") + 4.0f), (int)(this.getFontRenderer().FONT_HEIGHT * 1.75f + 3.0f), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                        }
                        GL11.glPushMatrix();
                        GL11.glScaled(1.75, 1.75, 0.0);
                        this.getFontRenderer().drawString("A", 1.0f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        GL11.glScaled(1.0, 1.0, 0.0);
                        GL11.glPopMatrix();
                        this.getFontRenderer().drawString("ugustus", this.getFontRenderer().getStringWidth("A") * 1.75f + 2.0f, 2 + this.getFontRenderer().FONT_HEIGHT - 4.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        break;
                    }
                    case "Right": {
                        if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                            Gui.drawRect((int)(sr.getScaledWidth() / 1.5f - (int)(this.getFontRenderer().getStringWidth("A") * 1.75f + this.getFontRenderer().getStringWidth("ugustus") + 4.0f)), 0, (int)(sr.getScaledWidth() / 1.5f) + 1, (int)(this.getFontRenderer().FONT_HEIGHT * 1.75f + 3.0f), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                        }
                        GL11.glPushMatrix();
                        GL11.glScaled(1.75, 1.75, 0.0);
                        this.getFontRenderer().drawString("A", sr.getScaledWidth() / 1.5f / 1.75f - this.getFontRenderer().getStringWidth("A") - this.getFontRenderer().getStringWidth("ugustus") / 1.75f - 0.25f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        GL11.glScaled(1.0, 1.0, 0.0);
                        GL11.glPopMatrix();
                        this.getFontRenderer().drawString("ugustus", sr.getScaledWidth() / 1.5f - 1.75f - this.getFontRenderer().getStringWidth("A") * 1.5f * 1.75f - this.getFontRenderer().getStringWidth("ugustus") / 1.5f + 4.0f, 2 + this.getFontRenderer().FONT_HEIGHT - 4.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        break;
                    }
                }
                GL11.glScaled(1.0, 1.0, 0.0);
                GL11.glPopMatrix();
                break;
            }
            case "Basic": {
                final GregorianCalendar cal = (GregorianCalendar)Calendar.getInstance();
                final Date time = new Date();
                cal.setTime(time);
                final int h = (GuiIngameHook.mm.protector.isToggled() && GuiIngameHook.mm.protector.protectTime.getBoolean()) ? GuiIngameHook.mm.protector.getRandomHour() : cal.get(10);
                String m = (cal.get(12) <= 9) ? ("0" + cal.get(12)) : String.valueOf(cal.get(12));
                m = ((GuiIngameHook.mm.protector.isToggled() && GuiIngameHook.mm.protector.protectTime.getBoolean()) ? String.valueOf(GuiIngameHook.mm.protector.getRandomMinute()) : m);
                String printTime;
                if (cal.get(9) == 0) {
                    printTime = ((h < 9) ? (" (0" + h + ":" + m + " AM)") : (" (" + h + ":" + m + " AM)"));
                }
                else {
                    printTime = ((h < 9) ? (" (0" + h + ":" + m + " PM)") : (" (" + h + ":" + m + " PM)"));
                }
                final String name = Augustus.getInstance().getName() + " " + Augustus.getInstance().getVersion();
                final float width = (float)this.getFontRenderer().getStringWidth(name);
                this.yAdd = (float)(this.getFontRenderer().FONT_HEIGHT + 1);
                final String selected3 = GuiIngameHook.mm.hud.side.getSelected();
                switch (selected3) {
                    case "Left": {
                        if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                            RenderUtil.drawFloatRect(0.0f, 0.0f, this.getFontRenderer().getStringWidth(printTime) + 3 + width, (float)(this.getFontRenderer().FONT_HEIGHT + 1), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                        }
                        this.getFontRenderer().drawString(name, 1.0f, 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        this.getFontRenderer().drawString(printTime, width, 1.0f, new Color(182, 186, 189).getRGB(), true, 0.5f);
                        break;
                    }
                    case "Right": {
                        if (GuiIngameHook.mm.hud.backGround.getBoolean()) {
                            RenderUtil.drawFloatRect(sr.getScaledWidth() - this.getFontRenderer().getStringWidth(printTime) - 3 - width, 0.0f, (float)sr.getScaledWidth(), (float)(this.getFontRenderer().FONT_HEIGHT + 1), GuiIngameHook.mm.hud.backGroundColor.getColor().getRGB());
                        }
                        this.getFontRenderer().drawString(name, sr.getScaledWidth() - width - this.getFontRenderer().getStringWidth(printTime), 1.0f, GuiIngameHook.mm.hud.color.getColor().getRGB(), true, 0.5f);
                        this.getFontRenderer().drawString(printTime, (float)(sr.getScaledWidth() - this.getFontRenderer().getStringWidth(printTime) - 1), 1.0f, new Color(182, 186, 189).getRGB(), true, 0.5f);
                        break;
                    }
                }
                break;
            }
            default: {
                this.yAdd = 0.0f;
                break;
            }
        }
        if (!GuiIngameHook.mm.arrayList.sideOption.getSelected().equals(GuiIngameHook.mm.hud.side.getSelected())) {
            this.yAdd = 0.0f;
        }
    }
    
    public static void drawCustomHotBar() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final FontRenderer fr = GuiIngameHook.mc.fontRendererObj;
        final int i = (GuiIngameHook.mc.getNetHandler() != null && GuiIngameHook.mc.getNetHandler().getPlayerInfo(GuiIngameHook.mc.thePlayer.getUniqueID()) != null) ? GuiIngameHook.mc.getNetHandler().getPlayerInfo(GuiIngameHook.mc.thePlayer.getUniqueID()).getResponseTime() : 0;
        final int x = GuiIngameHook.mm.protector.isToggled() ? ((int)GuiIngameHook.mc.thePlayer.posX + GuiIngameHook.mm.protector.getRandomX()) : ((int)GuiIngameHook.mc.thePlayer.posX);
        final int y = (int)GuiIngameHook.mc.thePlayer.posY;
        final int z = GuiIngameHook.mm.protector.isToggled() ? ((int)GuiIngameHook.mc.thePlayer.posZ + GuiIngameHook.mm.protector.getRandomZ()) : ((int)GuiIngameHook.mc.thePlayer.posZ);
        fr.drawString("§fFPS: §7" + Minecraft.getDebugFPS() + "   §fPing: §7" + i, (int)(4.0f + FontUtil.espHotbar.getStringWidth("A")), sr.getScaledHeight() - 20, Color.white.getRGB());
        fr.drawString("§fX: §7" + x + "  §fY: §7" + y + "  §fZ: §7" + z, (int)(4.0f + FontUtil.espHotbar.getStringWidth("A")), sr.getScaledHeight() - 9, Color.white.getRGB());
        FontUtil.espHotbar.drawStringWithShadow("A", 1.5, sr.getScaledHeight() - 19, Color.white.getRGB());
        if (!GuiIngameHook.mm.protector.isToggled() || !GuiIngameHook.mm.protector.protectTime.getBoolean()) {
            final GregorianCalendar now = new GregorianCalendar();
            final DateFormat df = DateFormat.getDateInstance(2);
            fr.drawString(df.format(now.getTime()), (float)(sr.getScaledWidth() - 10 - fr.getStringWidth(df.format(now.getTime()))), (float)(sr.getScaledHeight() - 1 - fr.FONT_HEIGHT), new Color(255, 255, 255, 221).getRGB(), true, 0.5f);
            final DateFormat df2 = DateFormat.getTimeInstance(3);
            fr.drawString(df2.format(now.getTime()), (float)(sr.getScaledWidth() - 10 - fr.getStringWidth(df2.format(now.getTime())) * 0.5 - fr.getStringWidth(df.format(now.getTime())) * 0.5), (float)(sr.getScaledHeight() - 11 - fr.FONT_HEIGHT), new Color(255, 255, 255, 221).getRGB(), true, 0.5f);
        }
    }
    
    public static void drawCustomCrossHair() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final double screenWidth = sr.getScaledWidth_double();
        final double screenHeight = sr.getScaledHeight_double();
        final Vec2d middle = new Vec2d(screenWidth / 2.0, screenHeight / 2.0);
        final double crossHairWidth = GuiIngameHook.mm.crossHair.width.getValue() / 2.0;
        final double crossHairLength = GuiIngameHook.mm.crossHair.length.getValue();
        final double crossHairMargin = GuiIngameHook.mm.crossHair.margin.getValue();
        int color = GuiIngameHook.mm.crossHair.color.getColor().getRGB();
        if (GuiIngameHook.mm.crossHair.rainbow.getBoolean()) {
            GuiIngameHook.mm.crossHair.rainbowUtil.updateRainbow((GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() == 1000.0) ? ((float)(GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() * 9.999999747378752E-6)) : ((float)(GuiIngameHook.mm.crossHair.rainbowSpeed.getValue() * 9.999999974752427E-7)), 255);
            color = GuiIngameHook.mm.crossHair.rainbowUtil.getColor().getRGB();
        }
        RenderUtil.drawRect(middle.x - crossHairWidth, middle.y + crossHairMargin + crossHairLength, middle.x + crossHairWidth, middle.y + crossHairMargin, color);
        if (!GuiIngameHook.mm.crossHair.tStyle.getBoolean()) {
            RenderUtil.drawRect(middle.x - crossHairWidth, middle.y - crossHairMargin - crossHairLength, middle.x + crossHairWidth, middle.y - crossHairMargin, color);
        }
        RenderUtil.drawRect(middle.x + crossHairMargin, middle.y + crossHairWidth, middle.x + crossHairMargin + crossHairLength, middle.y - crossHairWidth, color);
        RenderUtil.drawRect(middle.x - crossHairMargin - crossHairLength, middle.y + crossHairWidth, middle.x - crossHairMargin, middle.y - crossHairWidth, color);
        if (GuiIngameHook.mm.crossHair.dot.getBoolean()) {
            RenderUtil.drawRect(middle.x - crossHairWidth, middle.y + crossHairWidth, middle.x + crossHairWidth, middle.y - crossHairWidth, color);
        }
    }
    
    private void drawArmorHud() {
        final ScaledResolution sr = new ScaledResolution(GuiIngameHook.mc);
        final int height = sr.getScaledHeight() - 56;
        final int width = sr.getScaledWidth() / 2 + 91;
        GuiIngameHook.mc.getRenderItem().zLevel = -100.0f;
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(3) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(3), width - 81, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(2) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(2), width - 61, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(1) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(1), width - 41, height);
        }
        if (GuiIngameHook.mc.thePlayer.getCurrentArmor(0) != null) {
            GuiIngameHook.mc.getRenderItem().renderItemIntoGUI(GuiIngameHook.mc.thePlayer.getCurrentArmor(0), width - 21, height);
        }
    }
}
