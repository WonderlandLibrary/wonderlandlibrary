package de.violence.font;

import de.violence.font.FontLoader;
import de.violence.font.SlickFont;

public class FontManager {
   public static FontLoader fontLoader = new FontLoader();
   public static SlickFont mainMenu = FontLoader.getFont("BebasNeue.ttf", 100, false);
   public static SlickFont clickGUI = FontLoader.getFont("big_noodle_titling.ttf", 43, false);
   public static SlickFont arrayList_Bignoodletitling = FontLoader.getFont("big_noodle_titling.ttf", 44, false);
   public static SlickFont arrayList_Bebasneue = FontLoader.getFont("BebasNeue.ttf", 40, false);
   public static SlickFont arrayList_Arial = FontLoader.getFont("VERDANA.TTF", 31, false);
}
