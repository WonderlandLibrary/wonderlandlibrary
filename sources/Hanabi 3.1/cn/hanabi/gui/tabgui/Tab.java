package cn.hanabi.gui.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class Tab {
   private final @NotNull List subTabs = new ArrayList();
   private String text;

   public Tab(String text) {
      this.text = text;
   }

   public void addSubTab(SubTab subTab) {
      this.subTabs.add(subTab);
   }

   public @NotNull List getSubTabs() {
      return this.subTabs;
   }

   public void renderSubTabs(int x, int y, int selectedSubTab) {
      GL11.glTranslated((double)x, (double)y, 0.0);
      FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
      int height = (font.FONT_HEIGHT + 3) * this.subTabs.size();
      int width = 0;
      Iterator var7 = this.subTabs.iterator();

      while(var7.hasNext()) {
         SubTab tab = (SubTab)var7.next();
         if (font.getStringWidth(tab.getText()) > width) {
            width = font.getStringWidth(tab.getText());
         }
      }

      width += 4;
      TabGui.drawRect(7, 0, 0, width, height, TabGui.BACKGROUND.getRGB());
      GL11.glLineWidth(1.0F);
      TabGui.drawRect(2, 0, 0, width, height, TabGui.BORDER.getRGB());
      int offset = 2;
      int i = 0;

      for(Iterator var9 = this.subTabs.iterator(); var9.hasNext(); ++i) {
         SubTab tab = (SubTab)var9.next();
         if (selectedSubTab == i) {
            TabGui.drawRect(7, 0, offset - 2, width, offset + font.FONT_HEIGHT + 3 - 1, TabGui.SELECTED.getRGB());
         }

         font.drawString(tab.getText(), 2, offset, TabGui.FOREGROUND.getRGB());
         offset += font.FONT_HEIGHT + 3;
      }

      GL11.glTranslated((double)(-x), (double)(-y), 0.0);
   }

   public String getText() {
      return this.text;
   }

   public void setText(String text) {
      this.text = text;
   }
}
