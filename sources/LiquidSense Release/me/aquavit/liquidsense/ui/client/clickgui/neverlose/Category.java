package me.aquavit.liquidsense.ui.client.clickgui.neverlose;

import me.aquavit.liquidsense.ui.client.clickgui.neverlose.hud.HUD;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.ui.font.Fonts;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

public class Category {
    private float x;
    private float y;
    private ModuleCategory category;
    private String expandname;

    public Category(float renderX, float renderY, ModuleCategory moduleCategory, String expandCategory) {
        this.x = renderX;
        this.y = renderY;
        this.category = moduleCategory;
        this.expandname = expandCategory;
    }

    private int getbuttonColor(){
        int buttonColor;
        switch (Impl.hue) {
            case "white":
                buttonColor = new Color(214, 214, 214).getRGB();
                break;
            case "black":
                buttonColor = new Color(44, 44, 44).getRGB();
                break;
            default:
                buttonColor = new Color(5, 53, 76).getRGB();
                break;
        }
        return buttonColor;
    }

    private int getdisplayColor(){
        int displayColor;
        switch (Impl.hue) {
            case "blue":
            case "black":
                displayColor = -1;
                break;
            default:
                displayColor = new Color(1, 1, 1).getRGB();
                break;
        }
        return displayColor;
    }

    private int gettextColor(){
        int textColor;
        switch (Impl.hue) {
            case "white":
                textColor = new Color(140, 140, 140).getRGB();
                break;
            case "black":
                textColor = new Color(70, 70, 70).getRGB();
                break;
            default:
                textColor = new Color(46, 67, 81).getRGB();
                break;
        }
        return textColor;
    }

    public void drawCategory(int mouseX, int mouseY, Main gui) {

        if (Impl.selectedCategory.equals(category.getDisplayName()) && Impl.theType == Impl.Type.CLIENT) {
            RenderUtils.drawNLRect(
                    Impl.coordinateX + x - 20.5f,
                    Impl.coordinateY + y - 6f,
                    Impl.coordinateX + x + 66f,
                    Impl.coordinateY + y + 8.5f,
                    4f,
                    getbuttonColor()
            );
        }

        switch (expandname) {
            case "Combat":
                Fonts.font14.drawString(expandname, Impl.coordinateX + 10, Impl.coordinateY + y - 32, gettextColor());
                break;
            case "Utility":
                Fonts.font14.drawString(expandname, Impl.coordinateX + 10, Impl.coordinateY + y - 105, gettextColor());
                break;
            case "Miscellaneous":
                Fonts.font14.drawString(expandname, Impl.coordinateX + 10, Impl.coordinateY + y - 50, gettextColor());
                break;
        }

        GlStateManager.resetColor();
        Fonts.font15.drawString(category.getDisplayName(), Impl.coordinateX + x, Impl.coordinateY + y, getdisplayColor());

        int iconColor = new Color(2, 169, 245).getRGB();
        switch (category.getDisplayName()) {
            case "Blatant":
                Fonts.csgo40.drawString("B", Impl.coordinateX + (int)x - 14, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "Ghost":
                Fonts.csgo40.drawString("Y", Impl.coordinateX + (int)x - 13, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "Player":
                Fonts.csgo40.drawString("L", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y, iconColor);
                break;
            case "Movement":
                Fonts.csgo40.drawString("W", Impl.coordinateX + (int)x - 13, Impl.coordinateY + (int)y, iconColor);
                break;
            case "Render":
                Fonts.csgo40.drawString("X", Impl.coordinateX + (int)x - 13, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "World":
                Fonts.csgo40.drawString("C", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "Exploit":
                Fonts.csgo40.drawString("D", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "Misc":
                Fonts.csgo40.drawString("H", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y - 1, iconColor);
                break;
            case "Client":
                Fonts.csgo40.drawString("Q", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y - 2, iconColor);
                break;
            case "Scripts":
                Fonts.csgo40.drawString("V", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y, iconColor);
                break;
            case "HUD":
                Fonts.csgo40.drawString("U", Impl.coordinateX + (int)x - 15, Impl.coordinateY + (int)y, iconColor);
                break;
        }

        if (gui.hovertoFloatL(Impl.coordinateX + x - 22.5f, Impl.coordinateY + y - 5, Impl.coordinateX + x + 72.5f, Impl.coordinateY + y + 15, mouseX, mouseY, true)) {
            if (!category.getDisplayName().equals(Impl.selectedCategory)) {
                Impl.theType = Impl.Type.CLIENT;
                Impl.selectedCategory = category.getDisplayName();
                Impl.lwheel = 0f;
                Impl.rwheel = 0f;
            }
        }

    }
}
