package club.cloverhook.gui.hud.element.impl;

import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.utils.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

import java.awt.*;
import java.util.ArrayList;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.impl.visual.Hud;
import club.cloverhook.gui.hud.element.Element;
import club.cloverhook.gui.hud.element.Quadrant;
import club.cloverhook.utils.ColorCreator;
import club.cloverhook.utils.Draw;
import club.cloverhook.utils.font.Fonts;
import club.cloverhook.utils.property.impl.ColorProperty;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;

/**
 * @author antja03
 */
public class ToggledList extends Element {

    protected Minecraft mc = Minecraft.getMinecraft();

    private DoubleProperty propertyHeight = new DoubleProperty("Entry Height", "The height of each cheat name", null,
            10, 8, 15, 1, "px");

    private StringsProperty propertyColorMode = new StringsProperty("Color Type", "The way that each cheat name will be colored", null,
            false, true, new String[]{"Solid", "Pulsing", "Rainbow"}, new Boolean[]{true, false, false});

    private ColorProperty propertyNameColor = new ColorProperty("Name Color", "The color of each cheat name", () -> !propertyColorMode.getValue().get("Rainbow"),
            0, 0, 100, 255);

    private ColorProperty propertyBackgroundColor = new ColorProperty("Background Color", "The color of the background", null,
            0, 0, 0, 120);

    public ToggledList() {
        super("Toggled List", Quadrant.TOP_RIGHT, 2, 2);
        values.add(propertyHeight);
        values.add(propertyColorMode);
        values.add(propertyNameColor);
        values.add(propertyBackgroundColor);
    }

    private Hud hud;

    @Override
    public void drawElement(boolean editor) {
        if ((boolean) values.get(0).getValue() && !editor)
            return;

        this.editX = positionX + 1;
        this.editY = positionY;
        this.width = getLongestToggledLabelLength() + 3;
        this.height = getToggledCheats().size() * propertyHeight.getValue();

        if (hud == null) {
            hud = (Hud) Cloverhook.instance.cheatManager.getCheatRegistry().get("Overlay");
        }
        if (!hud.prop_arraylist.getValue() || !Cloverhook.instance.cheatManager.isCheatEnabled("Overlay"))
            return;
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        int y = (int) this.positionY - 2;
        for (Cheat m : getToggledModulesSortedLTS()) {
            CheatCategory mCat = m.getCategory();
            int catColor = mCat.equals(CheatCategory.COMBAT) ? new Color(248, 1, 67).getRGB() : mCat.equals(CheatCategory.MOVEMENT) ? new Color(7, 139, 186).getRGB() : mCat.equals(CheatCategory.VISUAL) ? new Color(252, 151, 0).getRGB() : mCat.equals(CheatCategory.MISC) ? new Color(232,165,250).getRGB() : mCat.equals(CheatCategory.PLAYER) ? new Color(232,165,250).getRGB() : 0xFFFFFF;
            int color = hud.prop_colormode.getValue().get("Rainbow") ? ColorCreator.createRainbowFromOffset(3200, y * -15) : hud.prop_colormode.getValue().get("Custom") ? hud.prop_color.getValue().getRGB() : hud.prop_colormode.getValue().get("Categories") ? catColor : 0xFFFFFF;
            String name = m.getId() + (m.getMode() != null ? " " + EnumChatFormatting.GRAY + m.getMode() : "");
            if (!name.toLowerCase().contains("interface")) {
                if (m.getAnimation() > 0) m.setAnimation(m.getAnimation() - 1);
                if (hud.prop_arraylistmode.getValue().get("Cloverhook")) {
                    double length = (hud.prop_customfont.getValue() ? Fonts.verdana3.getStringWidth(name) : mc.fontRendererObj.getStringWidth(name));
                    Draw.drawBorderedRectangle((int) (sr.getScaledWidth() - positionX - 3 - length + m.getAnimation()), y, (int) (sr.getScaledWidth() - positionX + 3), y + 10, 1, new Color(30, 30, 30).getRGB(), color, true);
                    Gui.drawRect((int) (sr.getScaledWidth() - positionX - 2 - length + m.getAnimation()), y, (int) (sr.getScaledWidth() - positionX + 3), y + 9, new Color(30, 30, 30).getRGB());
                }
                if (hud.prop_customfont.getValue())
                    Fonts.verdana3.drawStringWithShadow(name, (float) (sr.getScaledWidth() - positionX  + m.getAnimation()- Fonts.verdana3.getStringWidth(name)), y + 1, color);
                else
                    mc.fontRendererObj.drawStringWithShadow(name, (float) (sr.getScaledWidth() - positionX  + m.getAnimation()- mc.fontRendererObj.getStringWidth(name)), y + 1, color);
                y += Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
            }
        }
    }

    private ArrayList<Cheat> getToggledCheats() {
        ArrayList<Cheat> toggledCheats = new ArrayList<>();
        for (Cheat cheat : Cloverhook.instance.cheatManager.getCheatRegistry().values()) {
            if (cheat.getState()) {
                toggledCheats.add(cheat);
            }
        }
        return toggledCheats;
    }

    public java.util.ArrayList<Cheat> getToggledModulesSortedLTS() {
        java.util.ArrayList<Cheat> toggledCheats = getToggledCheats();
        toggledCheats.sort((cheat1, cheat2) -> (hud.prop_customfont.getValue() ? Fonts.verdana3.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : "")) : mc.fontRendererObj.getStringWidth(cheat2.getId() + (cheat2.getMode() != null ? " " + cheat2.getMode() : ""))) - (hud.prop_customfont.getValue() ? Fonts.verdana3.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : "")) : mc.fontRendererObj.getStringWidth(cheat1.getId() + (cheat1.getMode() != null ? " " + cheat1.getMode() : ""))));
        return toggledCheats;
    }

    public double getLongestToggledLabelLength() {
        double longestLabelLength = 0;
        for (Cheat cheat : getToggledCheats()) {
            final double labelLength = Fonts.verdana3.getStringWidth(cheat.getId());
            if (labelLength > longestLabelLength) {
                longestLabelLength = labelLength;
            }
        }
        return longestLabelLength;
    }

}
