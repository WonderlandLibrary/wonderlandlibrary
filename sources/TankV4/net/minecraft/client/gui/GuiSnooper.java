package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiSnooper extends GuiScreen {
   private String field_146610_i;
   private final GameSettings game_settings_2;
   private final java.util.List field_146609_h = Lists.newArrayList();
   private GuiSnooper.List field_146606_s;
   private String[] field_146607_r;
   private GuiButton field_146605_t;
   private final GuiScreen field_146608_a;
   private final java.util.List field_146604_g = Lists.newArrayList();

   static java.util.List access$0(GuiSnooper var0) {
      return var0.field_146604_g;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 2) {
            this.game_settings_2.saveOptions();
            this.game_settings_2.saveOptions();
            this.mc.displayGuiScreen(this.field_146608_a);
         }

         if (var1.id == 1) {
            this.game_settings_2.setOptionValue(GameSettings.Options.SNOOPER_ENABLED, 1);
            this.field_146605_t.displayString = this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED);
         }
      }

   }

   public void initGui() {
      this.field_146610_i = I18n.format("options.snooper.title");
      String var1 = I18n.format("options.snooper.desc");
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = this.fontRendererObj.listFormattedStringToWidth(var1, width - 30).iterator();

      while(var4.hasNext()) {
         Object var3 = var4.next();
         var2.add((String)var3);
      }

      this.field_146607_r = (String[])var2.toArray(new String[var2.size()]);
      this.field_146604_g.clear();
      this.field_146609_h.clear();
      this.buttonList.add(this.field_146605_t = new GuiButton(1, width / 2 - 152, height - 30, 150, 20, this.game_settings_2.getKeyBinding(GameSettings.Options.SNOOPER_ENABLED)));
      this.buttonList.add(new GuiButton(2, width / 2 + 2, height - 30, 150, 20, I18n.format("gui.done")));
      boolean var6 = this.mc.getIntegratedServer() != null && this.mc.getIntegratedServer().getPlayerUsageSnooper() != null;
      Iterator var5 = (new TreeMap(this.mc.getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();

      Entry var7;
      while(var5.hasNext()) {
         var7 = (Entry)var5.next();
         this.field_146604_g.add((var6 ? "C " : "") + (String)var7.getKey());
         this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)var7.getValue(), width - 220));
      }

      if (var6) {
         var5 = (new TreeMap(this.mc.getIntegratedServer().getPlayerUsageSnooper().getCurrentStats())).entrySet().iterator();

         while(var5.hasNext()) {
            var7 = (Entry)var5.next();
            this.field_146604_g.add("S " + (String)var7.getKey());
            this.field_146609_h.add(this.fontRendererObj.trimStringToWidth((String)var7.getValue(), width - 220));
         }
      }

      this.field_146606_s = new GuiSnooper.List(this);
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.field_146606_s.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, this.field_146610_i, width / 2, 8, 16777215);
      int var4 = 22;
      String[] var8;
      int var7 = (var8 = this.field_146607_r).length;

      for(int var6 = 0; var6 < var7; ++var6) {
         String var5 = var8[var6];
         this.drawCenteredString(this.fontRendererObj, var5, width / 2, var4, 8421504);
         var4 += this.fontRendererObj.FONT_HEIGHT;
      }

      super.drawScreen(var1, var2, var3);
   }

   static java.util.List access$1(GuiSnooper var0) {
      return var0.field_146609_h;
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.field_146606_s.handleMouseInput();
   }

   public GuiSnooper(GuiScreen var1, GameSettings var2) {
      this.field_146608_a = var1;
      this.game_settings_2 = var2;
   }

   class List extends GuiSlot {
      final GuiSnooper this$0;

      protected boolean isSelected(int var1) {
         return false;
      }

      protected void drawBackground() {
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
      }

      protected int getSize() {
         return GuiSnooper.access$0(this.this$0).size();
      }

      protected int getScrollBarX() {
         return this.width - 10;
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.this$0.fontRendererObj.drawString((String)GuiSnooper.access$0(this.this$0).get(var1), 10.0D, (double)var3, 16777215);
         this.this$0.fontRendererObj.drawString((String)GuiSnooper.access$1(this.this$0).get(var1), 230.0D, (double)var3, 16777215);
      }

      public List(GuiSnooper var1) {
         super(var1.mc, GuiSnooper.width, GuiSnooper.height, 80, GuiSnooper.height - 40, var1.fontRendererObj.FONT_HEIGHT + 1);
         this.this$0 = var1;
      }
   }
}
