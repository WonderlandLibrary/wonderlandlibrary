package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;

public class GuiLanguage extends GuiScreen {
   private final LanguageManager languageManager;
   protected GuiScreen parentScreen;
   private GuiOptionButton forceUnicodeFontBtn;
   private GuiLanguage.List list;
   private final GameSettings game_settings_3;
   private GuiOptionButton confirmSettingsBtn;

   static GuiOptionButton access$3(GuiLanguage var0) {
      return var0.forceUnicodeFontBtn;
   }

   static GameSettings access$1(GuiLanguage var0) {
      return var0.game_settings_3;
   }

   public void initGui() {
      this.buttonList.add(this.forceUnicodeFontBtn = new GuiOptionButton(100, width / 2 - 155, height - 38, GameSettings.Options.FORCE_UNICODE_FONT, this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT)));
      this.buttonList.add(this.confirmSettingsBtn = new GuiOptionButton(6, width / 2 - 155 + 160, height - 38, I18n.format("gui.done")));
      this.list = new GuiLanguage.List(this, this.mc);
      this.list.registerScrollButtons(7, 8);
   }

   static LanguageManager access$0(GuiLanguage var0) {
      return var0.languageManager;
   }

   public GuiLanguage(GuiScreen var1, GameSettings var2, LanguageManager var3) {
      this.parentScreen = var1;
      this.game_settings_3 = var2;
      this.languageManager = var3;
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      this.list.handleMouseInput();
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.list.drawScreen(var1, var2, var3);
      this.drawCenteredString(this.fontRendererObj, I18n.format("options.language"), width / 2, 16, 16777215);
      this.drawCenteredString(this.fontRendererObj, "(" + I18n.format("options.languageWarning") + ")", width / 2, height - 56, 8421504);
      super.drawScreen(var1, var2, var3);
   }

   static GuiOptionButton access$2(GuiLanguage var0) {
      return var0.confirmSettingsBtn;
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         switch(var1.id) {
         case 5:
            break;
         case 6:
            this.mc.displayGuiScreen(this.parentScreen);
            break;
         case 100:
            if (var1 instanceof GuiOptionButton) {
               this.game_settings_3.setOptionValue(((GuiOptionButton)var1).returnEnumOptions(), 1);
               var1.displayString = this.game_settings_3.getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
               ScaledResolution var2 = new ScaledResolution(this.mc);
               int var3 = var2.getScaledWidth();
               int var4 = ScaledResolution.getScaledHeight();
               this.setWorldAndResolution(this.mc, var3, var4);
            }
            break;
         default:
            this.list.actionPerformed(var1);
         }
      }

   }

   class List extends GuiSlot {
      private final Map languageMap;
      final GuiLanguage this$0;
      private final java.util.List langCodeList;

      protected void drawBackground() {
         this.this$0.drawDefaultBackground();
      }

      protected void elementClicked(int var1, boolean var2, int var3, int var4) {
         Language var5 = (Language)this.languageMap.get(this.langCodeList.get(var1));
         GuiLanguage.access$0(this.this$0).setCurrentLanguage(var5);
         GuiLanguage.access$1(this.this$0).language = var5.getLanguageCode();
         this.mc.refreshResources();
         this.this$0.fontRendererObj.setUnicodeFlag(GuiLanguage.access$0(this.this$0).isCurrentLocaleUnicode() || GuiLanguage.access$1(this.this$0).forceUnicodeFont);
         this.this$0.fontRendererObj.setBidiFlag(GuiLanguage.access$0(this.this$0).isCurrentLanguageBidirectional());
         GuiLanguage.access$2(this.this$0).displayString = I18n.format("gui.done");
         GuiLanguage.access$3(this.this$0).displayString = GuiLanguage.access$1(this.this$0).getKeyBinding(GameSettings.Options.FORCE_UNICODE_FONT);
         GuiLanguage.access$1(this.this$0).saveOptions();
      }

      protected int getSize() {
         return this.langCodeList.size();
      }

      public List(GuiLanguage var1, Minecraft var2) {
         super(var2, GuiLanguage.width, GuiLanguage.height, 32, GuiLanguage.height - 65 + 4, 18);
         this.this$0 = var1;
         this.langCodeList = Lists.newArrayList();
         this.languageMap = Maps.newHashMap();
         Iterator var4 = GuiLanguage.access$0(var1).getLanguages().iterator();

         while(var4.hasNext()) {
            Language var3 = (Language)var4.next();
            this.languageMap.put(var3.getLanguageCode(), var3);
            this.langCodeList.add(var3.getLanguageCode());
         }

      }

      protected int getContentHeight() {
         return this.getSize() * 18;
      }

      protected void drawSlot(int var1, int var2, int var3, int var4, int var5, int var6) {
         this.this$0.fontRendererObj.setBidiFlag(true);
         this.this$0.drawCenteredString(this.this$0.fontRendererObj, ((Language)this.languageMap.get(this.langCodeList.get(var1))).toString(), this.width / 2, var3 + 1, 16777215);
         this.this$0.fontRendererObj.setBidiFlag(GuiLanguage.access$0(this.this$0).getCurrentLanguage().isBidirectional());
      }

      protected boolean isSelected(int var1) {
         return ((String)this.langCodeList.get(var1)).equals(GuiLanguage.access$0(this.this$0).getCurrentLanguage().getLanguageCode());
      }
   }
}
