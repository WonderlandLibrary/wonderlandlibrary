package team.massacre.api.ui.framework;

public interface CustomFontRenderer {
   void drawString(String var1, float var2, float var3, int var4);

   void drawStringWithShadow(String var1, float var2, float var3, int var4);

   void drawStringWithOutline(String var1, float var2, float var3, int var4, int var5);

   float getWidth(String var1);

   float getHeight(String var1);
}
