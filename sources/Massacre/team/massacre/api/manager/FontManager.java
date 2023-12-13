package team.massacre.api.manager;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import team.massacre.utils.TTFFontRenderer;

public class FontManager {
   public TTFFontRenderer robotoRegularMedium;
   public TTFFontRenderer latoRegularMedium;
   public TTFFontRenderer latoRegularSmall;
   public TTFFontRenderer latoRegularMainMenu;

   public FontManager() {
      try {
         Font font = Font.createFont(0, ClassLoader.getSystemClassLoader().getResourceAsStream("assets/minecraft/massacre/fonts/Roboto-Regular.ttf")).deriveFont(0, 18.0F);
         Font font2 = Font.createFont(0, ClassLoader.getSystemClassLoader().getResourceAsStream("assets/minecraft/massacre/fonts/Lato-Regular.ttf")).deriveFont(0, 17.0F);
         Font font3 = Font.createFont(0, ClassLoader.getSystemClassLoader().getResourceAsStream("assets/minecraft/massacre/fonts/Lato-Regular.ttf")).deriveFont(0, 11.0F);
         Font font4 = Font.createFont(0, ClassLoader.getSystemClassLoader().getResourceAsStream("assets/minecraft/massacre/fonts/Lato-Regular.ttf")).deriveFont(0, 20.0F);
         this.robotoRegularMedium = new TTFFontRenderer(font);
         this.latoRegularMedium = new TTFFontRenderer(font2);
         this.latoRegularSmall = new TTFFontRenderer(font3);
         this.latoRegularMainMenu = new TTFFontRenderer(font4);
      } catch (FontFormatException | IOException var6) {
      }

   }

   public TTFFontRenderer getRobotoRegularMedium() {
      return this.robotoRegularMedium;
   }

   public TTFFontRenderer getLatoRegularMainMenu() {
      return this.latoRegularMainMenu;
   }

   public TTFFontRenderer getLatoRegularMedium() {
      return this.latoRegularMedium;
   }

   public TTFFontRenderer getLatoRegularSmall() {
      return this.latoRegularSmall;
   }
}
