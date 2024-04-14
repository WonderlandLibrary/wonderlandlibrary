package de.violence.tabgui;

import de.violence.font.FontManager;
import de.violence.module.ui.Category;
import de.violence.tabgui.componenents.CategoryTab;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;

public class TabGui {
   private List categoryTabList = new ArrayList();
   public static int x;
   public static int y;
   private int currentTab = 0;

   public TabGui(int x, int y) {
      this.x = x;
      this.y = y;
      this.createTabs();
   }

   public List getCategoryTabList() {
      return this.categoryTabList;
   }

   private void createTabs() {
      int y = this.y + 5;
      Category[] var5;
      int var4 = (var5 = Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Category categorys = var5[var3];
         if(categorys != Category.SETTINGS) {
            this.categoryTabList.add(new CategoryTab(categorys, x, y, this.getWidth()));
            y += 10;
         }
      }

   }

   public void renderTabGui() {
      int y = this.categoryTabList.size() * 11;
      GuiIngame var10000 = Minecraft.getMinecraft().ingameGUI;
      GuiIngame.drawRect(x, y + 5, x + this.getWidth() + 1, y + y - 1, Integer.MIN_VALUE);
      int yTabs = y + 5;

      for(Iterator var4 = this.categoryTabList.iterator(); var4.hasNext(); yTabs += 10) {
         CategoryTab categoryTabs = (CategoryTab)var4.next();
         categoryTabs.setX(x);
         categoryTabs.setY(yTabs);
         categoryTabs.draw(this.currentTab == this.categoryTabList.indexOf(categoryTabs));
      }

   }

   private int getWidth() {
      int w = 0;
      Category[] var5;
      int var4 = (var5 = Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Category categorys = var5[var3];
         w = Math.max(w, FontManager.arrayList_Bignoodletitling.getStringWidth(categorys.name()) * 2) - 3;
      }

      return w;
   }

   public void onSelect(int key) {
      if(this.categoryTabList.size() >= this.currentTab) {
         ((CategoryTab)this.categoryTabList.get(this.currentTab)).onSelect(key);
         if(!((CategoryTab)this.categoryTabList.get(this.currentTab)).isExtend()) {
            if(key == 208) {
               if(this.currentTab < this.categoryTabList.size() - 1) {
                  ++this.currentTab;
               } else {
                  this.currentTab = 0;
               }
            } else if(key == 200) {
               if(this.currentTab > 0) {
                  --this.currentTab;
               } else {
                  this.currentTab = this.categoryTabList.size() - 1;
               }
            }
         }
      }

   }

   public void setX(int x) {
      this.x = x;
   }

   public void setY(int y) {
      this.y = y;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }
}
