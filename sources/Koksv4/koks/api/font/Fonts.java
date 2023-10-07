package koks.api.font;

import koks.api.utils.Logger;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.IOException;

/*
 * Created on 3/4/2021 by dirt.
 */
public class Fonts {

    public static DirtyFontRenderer robotoThin25, segoeUIVF25, officinaSansBook55, arial18, ralewayRegular30, ralewayRegular120, ralewayRegular35, ralewayRegular25 ,ralewayRegular17, ralewayRegular13, arial25;

    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    public static void loadFonts() {
        Logger.getInstance().log("Loading fonts");
        arial18 = new DirtyFontRenderer(new Font("Arial", Font.PLAIN, 18));
        arial25 = new DirtyFontRenderer(new Font("Arial", Font.PLAIN, 25));
        try {
            ralewayRegular120 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(120F));
            ralewayRegular30 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(30F));
            ralewayRegular35 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(35F));
            ralewayRegular25 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(25F));
            ralewayRegular17 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(17F));
            ralewayRegular13 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Raleway-Regular.ttf")).deriveFont(13F));
            officinaSansBook55 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/OfficinaSans-Book.ttf")).deriveFont(55F));
            robotoThin25 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/Roboto-Thin.ttf")).deriveFont(25F));
            segoeUIVF25 = new DirtyFontRenderer(Font.createFont(Font.TRUETYPE_FONT,Fonts.class.getResourceAsStream("/fonts/SegoeUI-VF.ttf")).deriveFont(25F));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}
