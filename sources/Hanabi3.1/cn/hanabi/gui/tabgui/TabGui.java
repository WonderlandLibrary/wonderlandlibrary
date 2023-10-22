package cn.hanabi.gui.tabgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class TabGui {
   static final int OFFSET = 3;
   static @NotNull Color BACKGROUND = new Color(0, 0, 0, 175);
   static @NotNull Color BORDER = new Color(0, 0, 0, 255);
   static @NotNull Color SELECTED = new Color(38, 164, 78, 200);
   static Color FOREGROUND;
   private final @NotNull List tabs = new ArrayList();
   private int selectedTab = 0;
   private int selectedSubTab = -1;

   public static void drawRect(int glFlag, int left, int top, int right, int bottom, int color) {
      int j;
      if (left < right) {
         j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         j = top;
         top = bottom;
         bottom = j;
      }

      float f3 = (float)(color >> 24 & 255) / 255.0F;
      float f = (float)(color >> 16 & 255) / 255.0F;
      float f1 = (float)(color >> 8 & 255) / 255.0F;
      float f2 = (float)(color & 255) / 255.0F;
      Tessellator tessellator = Tessellator.getInstance();
      WorldRenderer worldrenderer = tessellator.getWorldRenderer();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture2D();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
      GlStateManager.color(f, f1, f2, f3);
      worldrenderer.begin(glFlag, DefaultVertexFormats.POSITION);
      worldrenderer.pos((double)left, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)bottom, 0.0).endVertex();
      worldrenderer.pos((double)right, (double)top, 0.0).endVertex();
      worldrenderer.pos((double)left, (double)top, 0.0).endVertex();
      tessellator.draw();
      GlStateManager.enableTexture2D();
      GlStateManager.disableBlend();
   }

   public void addTab(Tab tab) {
      this.tabs.add(tab);
   }

   public void render(int x, int y) {
      GL11.glTranslated((double)x, (double)y, 0.0);
      FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
      int height = (font.FONT_HEIGHT + 3) * this.tabs.size();
      int width = 0;
      Iterator var6 = this.tabs.iterator();

      while(var6.hasNext()) {
         Tab tab = (Tab)var6.next();
         if (font.getStringWidth(tab.getText()) > width) {
            width = font.getStringWidth(tab.getText());
         }
      }

      width += 4;
      drawRect(7, 0, 0, width, height, BACKGROUND.getRGB());
      int offset = 2;
      int i = 0;

      for(Iterator var8 = this.tabs.iterator(); var8.hasNext(); ++i) {
         Tab tab = (Tab)var8.next();
         if (this.selectedTab == i) {
            drawRect(7, 0, offset - 2, width, offset + font.FONT_HEIGHT + 3 - 2, SELECTED.getRGB());
            if (this.selectedSubTab != -1) {
               tab.renderSubTabs(width, offset - 2, this.selectedSubTab);
            }
         }

         font.drawString(tab.getText(), 2, offset, FOREGROUND.getRGB());
         offset += font.FONT_HEIGHT + 3;
      }

      GL11.glLineWidth(1.0F);
      drawRect(2, 0, 0, width, height, BORDER.getRGB());
      GL11.glTranslated((double)(-x), (double)(-y), 0.0);
   }

   public void handleKey(int keycode) {
      if (keycode == 208) {
         if (this.selectedSubTab == -1) {
            ++this.selectedTab;
            if (this.selectedTab >= this.tabs.size()) {
               this.selectedTab = 0;
            }
         } else {
            ++this.selectedSubTab;
            if (this.selectedSubTab >= ((Tab)this.tabs.get(this.selectedTab)).getSubTabs().size()) {
               this.selectedSubTab = 0;
            }
         }
      } else if (keycode == 200) {
         if (this.selectedSubTab == -1) {
            --this.selectedTab;
            if (this.selectedTab < 0) {
               this.selectedTab = this.tabs.size() - 1;
            }
         } else {
            --this.selectedSubTab;
            if (this.selectedSubTab < 0) {
               this.selectedSubTab = ((Tab)this.tabs.get(this.selectedTab)).getSubTabs().size() - 1;
            }
         }
      } else if (keycode == 203) {
         this.selectedSubTab = -1;
      } else if (this.selectedSubTab != -1 || keycode != 28 && keycode != 205) {
         if (keycode == 28 || keycode == 205) {
            ((SubTab)((Tab)this.tabs.get(this.selectedTab)).getSubTabs().get(this.selectedSubTab)).press();
         }
      } else {
         this.selectedSubTab = 0;
      }

   }

   static {
      FOREGROUND = Color.white;
   }
}
