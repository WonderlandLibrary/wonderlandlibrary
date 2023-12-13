package summer.cheat.cheats.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import summer.Summer;
import summer.base.manager.CheatManager;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.Draw;
import summer.base.utilities.TimerUtils;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.render.EventRender2D;
import summer.cheat.guiutil.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class HUD extends Cheats {
    public static Minecraft mc = Minecraft.getMinecraft();
    TimerUtils timer = new TimerUtils();
    public float hue = 0.0F;
    private final Setting rainbow;
    public static Setting hotbar;
    private final Setting miniMe;
    private final Setting playerX;
    private final Setting playerY;
    private final Setting hudName;
    public static Setting hudStyle;
    private final Setting hudFont;
    private final Setting background;
    private static Setting rainbowSpeed;
    private static Setting rainbowOffset;
    private static Setting rainbowBrightness;
    private static Setting rainbowSaturation;
    public static Setting hudHue;
    private final Setting hudSaturation;
    private final Setting flow;
    public static Setting hotbarHue;
    public static Setting hotbarRainbow;
    private final Setting potionEffects;
    private static Setting customFont;

    public HUD() {
        super("HUD", "Shows a HUD (Heads Up Display) on your screen", Selection.RENDER, true);
        ArrayList<String> name = new ArrayList<>();
        name.add("Logo");
        name.add("Text");
        ArrayList<String> style = new ArrayList<>();
        style.add("Summer");
        style.add("Astolfo");
        ArrayList<String> font = new ArrayList<>();
        font.add("WHIT");
        font.add("ROBO");
        font.add("TAHOMA");
        font.add("COM");
        font.add("None"); //doing tmmr bro dont touch me hud!
        Summer.INSTANCE.settingsManager.Property(hudName = new Setting("Name", this, "Text", name));
        Summer.INSTANCE.settingsManager.Property(hudStyle = new Setting("Styles", this, "Summer", style));
        Summer.INSTANCE.settingsManager.Property(customFont = new Setting("Custom Font", this, true));
        Summer.INSTANCE.settingsManager.Property(hudFont = new Setting("Font", this, "ROBO", font, customFont::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(background = new Setting("Background", this, true, customFont::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbow = new Setting("Rainbow Array", this, false));
        Summer.INSTANCE.settingsManager.Property(rainbowSpeed = new Setting("Rainbow Speed", this, 3, 1, 6, false, rainbow::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbowOffset = new Setting("Rainbow Offset", this, 2, 1, 6, false, rainbow::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbowBrightness = new Setting("Brightness", this, 1, 0.5, 1, false, rainbow::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(rainbowSaturation = new Setting("Saturation", this, 1, 0.1, 1, false, rainbow::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(hudHue = new Setting("Hud Hue", this, 0.17F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(hudSaturation = new Setting("Hud Sat", this, 1.0F, 0F, 1.0F, false));
        Summer.INSTANCE.settingsManager.Property(flow = new Setting("Flow Brightness", this, 0.3F, 0.0F, 0.98F, false));
        Summer.INSTANCE.settingsManager.Property(hotbar = new Setting("Hotbar", this, false));
        Summer.INSTANCE.settingsManager.Property(hotbarHue = new Setting("Hotbar Hue", this, 0.17F, 0F, 1.0F, false, hotbar::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(hotbarRainbow = new Setting("Rainbow Hotbar", this, false, hotbar::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(potionEffects = new Setting("Potion Effects", this, false));
        Summer.INSTANCE.settingsManager.Property(miniMe = new Setting("3D Player", this, false));
        Summer.INSTANCE.settingsManager.Property(playerX = new Setting("Player X", this, 26, 26, 930, false, miniMe::getValBoolean));
        Summer.INSTANCE.settingsManager.Property(playerY = new Setting("Player Y", this, 5, 5, 430, false, miniMe::getValBoolean));
        this.setToggled(true);
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        if (!mc.gameSettings.showDebugInfo) {
            if (miniMe.getValBoolean()) {
                int x = playerX.getValInt();
                int y = playerY.getValInt();
                GuiInventory.drawEntityOnScreen(e.getWidth() - x, e.getHeight() - y, 25, 0, 0, Minecraft.thePlayer);
            }
            this.hue += 2 / 5.0F;
            if (this.hue > 255.0F) {
                this.hue = 0.0F;
            }
            double currSpeed = Math.sqrt(Minecraft.thePlayer.motionX * Minecraft.thePlayer.motionX + Minecraft.thePlayer.motionZ * Minecraft.thePlayer.motionZ);
            float h = this.hue;
            String name = hudName.getValString();
            String font = hudFont.getValString();
            //  Client.instance.fm.getFont(font + " 15").drawString(String.format("BPS: %.2f", currSpeed), 10, 515, new Color(180, 159, 239).getRGB());
            Color c = Color.getHSBColor(hudHue.getValFloat(), hudSaturation.getValFloat(), 1.0F);
            if (name.equalsIgnoreCase("Logo")) {
                Summer.INSTANCE.fontManager.getFont(font + " 15").drawStringWithShadow("REBORN ", 23, 75, c.getRGB());
                Draw.drawImg(new ResourceLocation("client/icons/icon.png"), 10, 10, 64, 64);
            } else if (name.equalsIgnoreCase("Text")) {
                    Summer.INSTANCE.fontManager.getFont(font + " 30").drawStringWithShadow("S\u00A7fummer", 4, 4, c.getRGB());
            }
            List<Cheats> cheats = new ArrayList<>(Summer.INSTANCE.cheatManager.getModuleList());
            // 68 60
            if (customFont.getValBoolean()) {
                cheats.sort((o1, o2) -> Float.compare(Summer.INSTANCE.fontManager
                        .getFont(font + " 18").getWidth(o2.getDisplayName()), Summer.INSTANCE.fontManager.getFont(font + " 18").getWidth(o1.getDisplayName())));
            } else if (!customFont.getValBoolean()) {
                cheats.sort((o1, o2) -> Float.compare(Minecraft.fontRendererObj.getStringWidth(o2.getDisplayName()), Minecraft.fontRendererObj.getStringWidth(o1.getDisplayName())));
            }

            ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
            ArrayList<Cheats> enabledMods = new ArrayList<>();
            for (Cheats m : cheats) {
                if (m.isToggled() && !m.getName().contains("HUD")) {
                    enabledMods.add(m);
                }
            }
            int yText = 3;
            for (Cheats m : enabledMods) {
                int xText = scaledRes.getScaledWidth()
                        - (int) Summer.INSTANCE.fontManager.getFont(font + " 18").getWidth(m.getDisplayName()) - 1;
                Color color = Color.getHSBColor(h / 255.0F, 0.5F, 1.0F);
                // Client.instance.fm.getFont("ROBO 21").drawString(m.getDisplayName(), xText,
                // yText, new Color(255, 255, 255).getRGB());
                yText += 12;
                h -= 20.0F;
            }

            yText = 2;

            for (Cheats m : enabledMods) {
                String style = hudStyle.getValString();
                boolean setTransition = true;
                if (m.getTransition() > -9.8) {
                    m.setTransition(m.getTransition() - 1);
                }
                if (h > 255.0F) {
                    h = 0.0F;
                }
                if (CheatManager.getInstance(HUD.class).isToggled()) {
                    Color c2 = Color.getHSBColor(0.0F, 0.0F, flow.getValFloat());
                    Color color = Color.getHSBColor(h / 255.0F, 0.6F, 1.0F);
                    int xText = scaledRes.getScaledWidth()
                            - (int) Summer.INSTANCE.fontManager.getFont(font + " 18").getWidth(m.getDisplayName()) - 1;
                    int xTextDefault = scaledRes.getScaledWidth()
                            - Minecraft.fontRendererObj.getStringWidth(m.getDisplayName()) - 1;
                    int summer = getGradientOffset(c, c2, (Math.abs(((System.currentTimeMillis()) / 10)) / 80D) + (yText / ((Summer.INSTANCE.fontManager.getFont(font + " 18").getHeight(m.getDisplayName()) + 135)))).getRGB();
                    int astolfo = getGradientOffset(new Color(250, 100, 234), new Color(27, 249, 255), (Math.abs(((System.currentTimeMillis()) / 8)) / 120D) + (yText / ((Summer.INSTANCE.fontManager.getFont(font + " 18").getHeight(m.getDisplayName()) + 135)))).getRGB();
                    if (rainbow.getValBoolean() && customFont.getValBoolean()) {
                        if (background.getValBoolean()) {
                            Gui.drawRect(scaledRes.getScaledWidth() - 1, yText - 3, scaledRes.getScaledWidth(),
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, getRainbow(6000, -15 * yText));
                            Gui.drawHorizontalLine(xText - 2, Minecraft.getMinecraft().displayWidth,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, getRainbow(6000, -15 * yText));
                            Gui.drawRect(xText - 2, yText - 2, scaledRes.getScaledWidth() - 1,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, new Color(0, 0, 0).getRGB());
                        }
                        Gui.drawVerticalLine(xText - 2, yText - 3, yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1,
                                getRainbow(6000, -15 * yText));
                        Summer.INSTANCE.fontManager.getFont(font + " 18").drawStringWithShadow(m.getDisplayName(), xText + m.getTransition() + 9.8F, yText,
                                getRainbow(6000, -15 * yText));
                    }
                    if (style.equalsIgnoreCase("Summer") && !rainbow.getValBoolean() && customFont.getValBoolean()) {
                        if (background.getValBoolean()) {
                            Gui.drawRect(scaledRes.getScaledWidth() - 1, yText - 3, scaledRes.getScaledWidth(),
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, c.getRGB());
                            Gui.drawHorizontalLine(xText - 2, Minecraft.getMinecraft().displayWidth,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, summer);
                            Gui.drawRect(xText - 2, yText - 2, scaledRes.getScaledWidth() - 1,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, new Color(0, 0, 0).getRGB());
                        }
                        Gui.drawRect(scaledRes.getScaledWidth() - 1, yText - 3, scaledRes.getScaledWidth(),
                                yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, c.getRGB());
                        Gui.drawVerticalLine(xText - 2, yText - 3, yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, summer);
                        Summer.INSTANCE.fontManager.getFont(font + " 18").drawStringWithShadow(m.getDisplayName(), xText + m.getTransition() + 9.8F, yText, summer);
                    }
                    if (style.equalsIgnoreCase("Astolfo") && !rainbow.getValBoolean() && customFont.getValBoolean()) {
                        if (background.getValBoolean()) {
                            Gui.drawRect(scaledRes.getScaledWidth() - 1, yText - 3, scaledRes.getScaledWidth(),
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, new Color(250, 100, 234).getRGB());
                            Gui.drawRect(scaledRes.getScaledWidth() - 1, yText - 3, scaledRes.getScaledWidth(),
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, astolfo);
                            Gui.drawHorizontalLine(xText - 2, Minecraft.getMinecraft().displayWidth,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, astolfo);
                            Gui.drawRect(xText - 2, yText - 2, scaledRes.getScaledWidth() - 1,
                                    yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, new Color(0, 0, 0).getRGB());
                        }
                        Gui.drawVerticalLine(xText - 2, yText - 3, yText + Minecraft.fontRendererObj.FONT_HEIGHT + 1, astolfo);
                        Summer.INSTANCE.fontManager.getFont(font + " 18").drawStringWithShadow(m.getDisplayName(), xText + m.getTransition() + 9.8F, yText, astolfo);
                    }
                    if (style.equalsIgnoreCase("Summer") && !customFont.getValBoolean()) {
                        Minecraft.fontRendererObj.drawStringWithShadow(m.getDisplayName(), xTextDefault + m.getTransition() + 9.8F, yText, summer);
                    }
                    if (rainbow.getValBoolean() && !customFont.getValBoolean()) {
                        Minecraft.fontRendererObj.drawStringWithShadow(m.getDisplayName(), xTextDefault + m.getTransition() + 9.8F, yText,
                                getRainbow(6000, -15 * yText));
                    }
                    if (style.equalsIgnoreCase("Astolfo") && !customFont.getValBoolean() && !rainbow.getValBoolean()) {
                        Minecraft.fontRendererObj.drawStringWithShadow(m.getDisplayName(), xTextDefault + m.getTransition() + 9.8F, yText, astolfo);
                    }
                    yText += 12;
                    h -= 20.0F;
                }
            }
        }
    }

    public Color getGradientOffset(Color color1, Color color2, double offset) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;
        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart);
    }

    public static Color fade(long offset, float fade) {
        float hue = (float) (System.nanoTime() + offset) / 1.0E10F % 1.0F;
        long color = Long.parseLong(Integer.toHexString(Integer.valueOf(Color.HSBtoRGB(hue, 1.0F, 1.0F)).intValue()),
                16);
        Color c = new Color((int) color);
        return new Color(c.getRed() / 255.0F * fade, c.getGreen() / 255.0F * fade, c.getBlue() / 255.0F * fade,
                c.getAlpha() / 155.0F);
    }

    public static int getRainbow(int speed, int offset) {
        long rainspeed = rainbowSpeed.getValLong();
        long rainoffset = rainbowOffset.getValLong();
        float hue = (float) ((System.currentTimeMillis() * rainspeed + offset / rainoffset) % speed * 2);
        float saturation = rainbowSaturation.getValFloat();
        float brightness = rainbowBrightness.getValFloat();
        hue /= speed;
        return Color.getHSBColor(hue, saturation, brightness).getRGB();
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        int screenX = scaledRes.getScaledWidth();
        int screenY = scaledRes.getScaledHeight();
        if (!potionEffects.getValBoolean())
            return;
        int potionY = 12;
        for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String effectName = I18n.format(potion.getName()) + " " + (effect.getAmplifier() + 1) + " \2477" + Potion.getDurationString(effect);
            mc.fontRendererObj.drawStringWithShadow(effectName, screenX - 2 - mc.fontRendererObj.getStringWidth(effectName), screenY - potionY, potion.getLiquidColor());

            potionY += 10;
        }
    }
}