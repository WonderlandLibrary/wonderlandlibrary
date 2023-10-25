package fun.rich.client.utils.other.font;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Fonts {

    public static CFontRenderer Regular35, Regular30, Regular20, Regular25,Monstserrat30, Monstserrat20, Monstserrat16,Monstserrat17, Monstserrat, Esp,Icon;

    public static void loadFonts() {
        try {

            Regular35 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(35F), true, false);
            Regular30 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(30F), true, false);
            Regular25 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(25F), true, false);
            Regular20 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(20F), true, false);

            Monstserrat30 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(30F), true, false);
            Monstserrat20 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(20F), true, false);
            Monstserrat16 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(16F), true, false);

            Monstserrat17 = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsr.ttf"))).deriveFont(17F), true, false);
            Monstserrat = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsr.ttf"))).deriveFont(15F), true, false);

            Esp = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/esp.ttf"))).deriveFont(27F), true, false);
            Icon = new CFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/stylesicons.ttf"))).deriveFont(25F), true, false);


        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

}

