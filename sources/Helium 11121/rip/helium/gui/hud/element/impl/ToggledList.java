package rip.helium.gui.hud.element.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.cheat.impl.visual.Hud;
import rip.helium.utils.Render2DUtil;
import rip.helium.gui.hud.element.Element;
import rip.helium.gui.hud.element.Quadrant;
import rip.helium.ui.main.tab.TabGui;
import rip.helium.utils.ColorCreator;
import rip.helium.utils.Draw;
import rip.helium.utils.RenderUtil;
import rip.helium.utils.font.Fonts;
import rip.helium.utils.property.impl.ColorProperty;
import rip.helium.utils.property.impl.DoubleProperty;
import rip.helium.utils.property.impl.StringsProperty;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author antja03
 */
public class ToggledList extends Element {

    public Color color2;
    protected Minecraft mc = Minecraft.getMinecraft();
    int Colorer;
    int y3 = 0;
    private final DoubleProperty propertyHeight = new DoubleProperty("Entry Height", "The height of each cheat name", null,
            10, 8, 15, 1, "px");
    private final StringsProperty propertyColorMode = new StringsProperty("Color Type", "The way that each cheat name will be colored", null,
            false, true, new String[]{"Solid", "Pulsing", "Rainbow", "Kingmount"}, new Boolean[]{true, false, false, false});
    private final ColorProperty propertyNameColor = new ColorProperty("Name Color", "The color of each cheat name", () -> !propertyColorMode.getValue().get("Rainbow"),
            0, 0, 100, 255);
    private final ColorProperty propertyBackgroundColor = new ColorProperty("Background Color", "The color of the background", null,
            0, 0, 0, 120);
    private int brightness = 130;
    //int catColor = mCat.equals(CheatCategory.COMBAT) ? new Color(140, 78, 83).getRGB() : mCat.equals(CheatCategory.MOVEMENT) ? new Color(103, 144, 177).getRGB() : mCat.equals(CheatCategory.VISUAL) ? new Color(176,149,147).getRGB() : mCat.equals(CheatCategory.MISC) ? new Color(193,207,226).getRGB() : mCat.equals(CheatCategory.PLAYER) ? new Color(140, 78, 83).getRGB() : 0xFFFFFF;
    private float hue;
    private boolean ascending;
    private float[] hsb = new float[3];
    private Hud hud;
    public ToggledList() {
        super("Toggled List", Quadrant.TOP_RIGHT, 2, 2);
        values.add(propertyHeight);
        values.add(propertyColorMode);
        values.add(propertyNameColor);
        values.add(propertyBackgroundColor);
    }

    @Override
    public void drawElement(boolean editor) {

        if (Hud.tabui.getValue()) {
            TabGui.drawTabGui();
        }

        ScaledResolution sr2 = new ScaledResolution(Minecraft.getMinecraft());
        int locY = ScaledResolution.getScaledHeight() - mc.fontRendererObj.FONT_HEIGHT - 2;
        int locX = ScaledResolution.getScaledWidth() - mc.fontRendererObj.FONT_HEIGHT - 2;
        double xC = Math.round(mc.thePlayer.posX);
        double yC = Math.round(mc.thePlayer.posY);
        double zC = Math.round(mc.thePlayer.posZ);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (!mc.ingameGUI.getChatGUI().getChatOpen()) {
            String release;
            if (Helium.instance.developement) {
                release = "Development";
            } else {
                release = "Beta";
            }
            String messageBuilder = "§7" + release + " Build - §f§l" + Helium.client_build + " §7- " + Helium.clientUser;
            mc.fontRendererObj.drawStringWithShadow(messageBuilder, locX - mc.fontRendererObj.getStringWidth(messageBuilder) + 15, locY, new Color(255, 32, 0).getRGB());
            Draw.drawRectangle(0, locY + 10, mc.fontRendererObj.getStringWidth(String.format("§7%s, %s, %s", xC, yC, zC)) + 3, locY - 2.5, new Color(0, 0, 0, 140).getRGB());
            mc.fontRendererObj.drawStringWithShadow(String.format("%s, %s, %s", xC, yC, zC), 2, locY, new Color(255, 32, 0).getRGB());
            Draw.drawRectangle(mc.fontRendererObj.getStringWidth(String.format("§7%s, %s, %s", xC, yC, zC)) + 3, locY + 10, mc.fontRendererObj.getStringWidth(String.format("§7%s, %s, %s", xC, yC, zC)) + 5, locY - 2.5, new Color(255, 0, 10, 255).getRGB());
        }

        if ((boolean) values.get(0).getValue() && !editor) {
            return;
        }

        this.editX = positionX - 20;
        this.editY = positionY;
        this.width = getLongestToggledLabelLength() + 3;
        this.height = getToggledCheats().size() * propertyHeight.getValue();

        if (hud == null) {
            hud = (Hud) Helium.instance.cheatManager.getCheatRegistry().get("Hud");
        }

        if (!Hud.prop_arraylist.getValue() || !Helium.instance.cheatManager.isCheatEnabled("Hud")) {
            return;
        }
        if (Hud.prop_colormode.getValue().get("Pulsing")) {
            if (ascending) {
                brightness++;
            } else {
                brightness--;
            }

            if (brightness <= 160F) {
                ascending = true;
            }

            if (brightness >= 255F) {
                ascending = false;
            }

            Color c = Hud.prop_color.getValue();
            this.hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
            color2 = Color.getHSBColor(this.hsb[0], this.hsb[1], this.brightness / 255.0F);
        }
        int y = (int) positionY - 2;
        int y2 = (int) positionY + 2;
        int y3 = (int) positionY + 2;
        int color = 0;

        if (Hud.radar.getValue()) {
            for (Entity ent : mc.theWorld.loadedEntityList) {
                if (ent instanceof EntityPlayer && !(ent == mc.thePlayer)) {
                    Fonts.verdana3.drawStringWithShadow(ent.getName() + " §7[§f" + Math.round(((EntityPlayer) ent).getHealth()) + "§4 \u2764§7] §f- §7[" + Math.round(mc.thePlayer.getDistanceToEntity(ent)) + "m]", 1, 25 + y3, -1);
                    y3 += mc.fontRendererObj.FONT_HEIGHT + 1;
                }
            }
        }

        for (Cheat m : getToggledModulesSortedLTS()) {
            CheatCategory mCat = m.getCategory();
            int catColor = mCat.equals(CheatCategory.COMBAT) ? new Color(140, 78, 83).getRGB() : mCat.equals(CheatCategory.MOVEMENT) ? new Color(103, 144, 177).getRGB() : mCat.equals(CheatCategory.VISUAL) ? new Color(176, 149, 147).getRGB() : mCat.equals(CheatCategory.MISC) ? new Color(193, 207, 226).getRGB() : mCat.equals(CheatCategory.PLAYER) ? new Color(140, 78, 83).getRGB() : 0xFFFFFF;
            String name;
            if (Hud.lowercase.value) {
                name = (m.getId() + (m.getMode() != null ? " " + EnumChatFormatting.GRAY + m.getMode() : "")).toLowerCase();
            } else {
                name = m.getId() + (m.getMode() != null ? " " + EnumChatFormatting.GRAY + m.getMode() : "");
            }

            switch (Hud.prop_colormode.getSelectedStrings().get(0)) {
                case "Rainbow": {
                    color = ColorCreator.createRainbowFromOffset(-6000, y * 35);
                    break;
                }
                case "Wave": {
                    color = ColorCreator.fade(Hud.prop_color.getValue(), 1100, y * 2 + 10).getRGB();
                    break;
                }
                case "Rainbow2": {
                    color = ColorCreator.createRainbowFromOffset2(-6000, y * 35);
                    break;
                }
                case "Categories": {
                    color = catColor;
                    break;
                }
                case "Pulsing": {
                    color = color2.getRGB();
                    break;
                }
                case "Custom": {
                    color = Hud.prop_color.getValue().getRGB();
                    break;
                }
            }
            if (!name.toLowerCase().contains("interface")) {
                switch (Hud.prop_theme.getSelectedStrings().get(0)) {
                    case "Helium": {
                        String time = new SimpleDateFormat("hh:mm a").format(new Date());
                        if (time.startsWith("0")) {
                            time = time.replaceFirst("0", "");
                        }
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        double length = mc.fontRendererObj.getStringWidth(name);
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        Gui.drawRect((int) (ScaledResolution.getScaledWidth() - this.positionX - 2.0D - length + m.getAnimation() - 1.5), y, (int) (ScaledResolution.getScaledWidth() - this.positionX + 3.5D), y + 12, (new Color(0, 0, 0, 163)).getRGB());
                        mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 2), color);
                        String server = mc.isSingleplayer() ? "local server" : mc.getCurrentServerData().serverIP.toLowerCase();
                        String text = "helium.rip | " + mc.getDebugFPS() + " fps | " + server;
                        float width = Fonts.verdana3.getStringWidth(text) + 9;
                        int height = 20;
                        int posX = 2;
                        int posY = 2;
                        Render2DUtil.drawRect(posX, posY, posX + width + 2, posY + height, new Color(5, 5, 5, 255).getRGB());
                        Render2DUtil.drawBorderedRect(posX + .5, posY + .5, posX + width + 1.5, posY + height - .5, 0.5, new Color(40, 40, 40, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                        Render2DUtil.drawBorderedRect(posX + 2, posY + 2, posX + width, posY + height - 2, 0.5, new Color(22, 22, 22, 255).getRGB(), new Color(60, 60, 60, 255).getRGB(), true);
                        Render2DUtil.drawRect(posX + 2.5, posY + 2.5, posX + width - .5, posY + 4.5, new Color(9, 9, 9, 255).getRGB());
                        Render2DUtil.drawGradientSideways(4, posY + 3, 4 + (width / 3), posY + 4, new Color(81, 149, 219, 255).getRGB(), new Color(180, 49, 218, 255).getRGB());
                        Render2DUtil.drawGradientSideways(4 + (width / 3), posY + 3, 4 + ((width / 3) * 2), posY + 4, new Color(180, 49, 218, 255).getRGB(), new Color(236, 93, 128, 255).getRGB());
                        Render2DUtil.drawGradientSideways(4 + ((width / 3) * 2), posY + 3, ((width / 3) * 3) + 1, posY + 4, new Color(236, 93, 128, 255).getRGB(), new Color(167, 171, 90, 255).getRGB());
                        Fonts.verdana3.drawString(text, 5 + posX, 8 + posY, -1);
                        //GL11.glPopMatrix();
                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3;
                        break;
                    }
                    case "Virtue": {
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        double length = mc.fontRendererObj.getStringWidth(name);

                        mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), y, color);
                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1;
                        break;
                    }
                    case "NSFWImage": {
                        if (Helium.instance.is18Mode) {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            Draw.drawImg(new ResourceLocation("client/watermark.jpg"), 2.0, 2.0, 150, 250);
                            GL11.glPopMatrix();
                            if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                            double length = mc.fontRendererObj.getStringWidth(name);
                            if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                            Gui.drawRect((int) (ScaledResolution.getScaledWidth() - this.positionX - 2.0D - length + m.getAnimation() - 1.5), y, (int) (ScaledResolution.getScaledWidth() - this.positionX + 3.5D), y + 12, (new Color(0, 0, 0, 163)).getRGB());
                            mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 2), color);
                            //GL11.glPushMatrix();
                            //GL11.glScaled(1.1, 1.1, 1.1);
                            //GL11.glPopMatrix();
                            y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3;
                        }
                        break;
                    }
                    case "NSFWImage2": {
                        if (Helium.instance.is18Mode) {
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            Draw.drawImg(new ResourceLocation("client/watermark2.png"), 2.0, 2.0, 150, 250);
                            GL11.glPopMatrix();
                            if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                            double length = mc.fontRendererObj.getStringWidth(name);
                            if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                            Gui.drawRect((int) (ScaledResolution.getScaledWidth() - this.positionX - 2.0D - length + m.getAnimation() - 1.5), y, (int) (ScaledResolution.getScaledWidth() - this.positionX + 3.5D), y + 12, (new Color(0, 0, 0, 163)).getRGB());
                            mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 2), color);
                            //GL11.glPushMatrix();
                            //GL11.glScaled(1.1, 1.1, 1.1);
                            //GL11.glPopMatrix();
                            y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 3;
                        }
                        break;
                    }
                    case "Exhi": {
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        if (hud.prop_arraylistmode.getValue().get("Helium")) {
                            double length = mc.fontRendererObj.getStringWidth(name);
                        }
                        if (Hud.prop_colormode.getValue().get("Categories")) {
                            mc.fontRendererObj.drawStringWithShadow(Helium.getClient_name + " v.69 §7[§f" + Minecraft.debugFPS + " FPS§7] ", 4, 4, ColorCreator.createRainbowFromOffset(-6000, y * 35));
                        } else {
                            mc.fontRendererObj.drawStringWithShadow(Helium.getClient_name + " v.69 §7[§f" + Minecraft.debugFPS + " FPS§7] ", 4, 4, color);
                        }
                        mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), 2 + y, color);
                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 1;
                        break;
                    }
                    case "Memestick": {
                        double length = mc.fontRendererObj.getStringWidth(name);

                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        Draw.drawBorderedRectangle((int) (sr.getScaledWidth() - this.positionX - 3.0D - length + m.getAnimation()), y, (int) (sr.getScaledWidth() - this.positionX + 3.0D), (y + 12), 1.0D, (new Color(30, 30, 30)).getRGB(), color, true);
                        Gui.drawRect((int) (sr.getScaledWidth() - this.positionX - 2.0D - length + m.getAnimation()), y, (int) (sr.getScaledWidth() - this.positionX + 3.0D), y + 9, (new Color(30, 30, 30)).getRGB());
                        mc.fontRendererObj.drawStringWithShadow(name, (float) (sr.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 1), color);
                        mc.fontRendererObj.drawStringWithShadow(Helium.getClient_name, 5, 5, new Color(255, 0, 0, 255).getRGB());
                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
                        break;
                    }
                    case "Kingmount": {
                        mc.fontRendererObj.drawStringWithShadow(Helium.getClient_name, 5, 5, m.getColor());
                        double length = mc.fontRendererObj.getStringWidth(name);
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        mc.fontRendererObj.drawStringWithShadow(name, (float) (ScaledResolution.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 1), m.getColor());
                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
                        break;
                    }
                    case "Memeware": {
                        double length = mc.fontRendererObj.getStringWidth(name);
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        Gui.drawRect((int) (sr.getScaledWidth() - positionX - 4 - length - 2 + 1), y, (int) (sr.getScaledWidth() - positionX + 3), y + 9 + 2, new Color(0, 0, 0, 180).getRGB());
                        Gui.drawRect(sr.getScaledWidth() - this.positionX - 5 - length + m.getAnimation(), y, sr.getScaledWidth() - this.positionX - mc.fontRendererObj.getStringWidth(name) - 3.5, (y + 11), color);

                        mc.fontRendererObj.drawStringWithShadow(name, (float) (sr.getScaledWidth() - this.positionX + m.getAnimation() - mc.fontRendererObj.getStringWidth(name)), (y + 1), color);
                        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow( Helium.getClient_name + " v69", 5, 5, color);


                        y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
                        break;
                    }
                    case "Helium2": {
                        if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                        if (hud.prop_arraylistmode.getValue().get("Helium")) {
                            double length = mc.fontRendererObj.getStringWidth(name);
                            Gui.drawRect(-m.getAnimation(), y2 + 10, (int) (ScaledResolution.getScaledWidth() / positionX / 100 + length - m.getAnimation()), y2 + 9 + 2 + 10, RenderUtil.withTransparency(0, Hud.backgroundcolor.getValue().floatValue()));
                        }
                        mc.fontRendererObj.drawStringWithShadow(Helium.getClient_name + " §7(Build: " + Helium.client_build + "§7) §7(User: " + Helium.clientUser + "§7)", 2, 3, color);
                        mc.fontRendererObj.drawStringWithShadow(name, (float) (3 - m.getAnimation()), y2 + 10, color); //with shadow???
                        Gui.drawRect((int) (1.5), y2 + 10, 0, y2 + 9 + 2 + 10, color);
                        y2 += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + 2;
                        break;
                    }
                }
            }
        }
    }


    private ArrayList<Cheat> getToggledCheats() {
        ArrayList<Cheat> toggledCheats = new ArrayList<>();
        for (Cheat cheat : Helium.instance.cheatManager.getCheatRegistry().values()) {
            if (cheat.getState()) {
                toggledCheats.add(cheat);
            }
        }
        return toggledCheats;
    }

    public java.util.ArrayList<Cheat> getToggledModulesSortedLTS() {
        java.util.ArrayList<Cheat> toggledCheats = getToggledCheats();
        //if (Hud.prop_theme.getValue().get("Helium")) {
        //    toggledCheats.sort((cheat1, cheat2) -> (hud.prop_customfont.getValue() ? Fonts.verdana3.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : "")) : Fonts.verdana3.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : ""))) - (hud.prop_customfont.getValue() ? Fonts.verdana3.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : "")) : Fonts.verdana3.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : ""))));
        //} else {
            toggledCheats.sort((cheat1, cheat2) -> (hud.prop_customfont.getValue() ? mc.fontRendererObj.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : "")) : mc.fontRendererObj.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : ""))) - (hud.prop_customfont.getValue() ? mc.fontRendererObj.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : "")) : mc.fontRendererObj.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : ""))));
        //}
        return toggledCheats;
    }

    public double getLongestToggledLabelLength() {
        double longestLabelLength = 0;
        for (Cheat cheat : getToggledCheats()) {
            final double labelLength;
            //if (Hud.prop_theme.getValue().get("Helium")) {
           //     labelLength = Fonts.verdana3.getStringWidth(cheat.getId());
            //} else {
                labelLength = mc.fontRendererObj.getStringWidth(cheat.getId());
            //}
            if (labelLength > longestLabelLength) {
                longestLabelLength = labelLength;
            }
        }
        return longestLabelLength;
    }

}
