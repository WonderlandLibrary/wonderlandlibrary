package net.minecraft.client.gui.stream;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

public class GuiTwitchUserMode extends GuiScreen {
   private final IChatComponent field_152338_i;
   private final IStream stream;
   private final ChatUserInfo field_152337_h;
   private final List field_152332_r = Lists.newArrayList();
   private static final EnumChatFormatting field_152335_f;
   private static final EnumChatFormatting field_152336_g;
   private static final EnumChatFormatting field_152331_a;
   private int field_152334_t;

   static {
      field_152331_a = EnumChatFormatting.DARK_GREEN;
      field_152335_f = EnumChatFormatting.RED;
      field_152336_g = EnumChatFormatting.DARK_PURPLE;
   }

   public void initGui() {
      int var1 = width / 3;
      int var2 = var1 - 130;
      this.buttonList.add(new GuiButton(1, var1 * 0 + var2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.timeout")));
      this.buttonList.add(new GuiButton(0, var1 * 1 + var2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.ban")));
      this.buttonList.add(new GuiButton(2, var1 * 2 + var2 / 2, height - 70, 130, 20, I18n.format("stream.userinfo.mod")));
      this.buttonList.add(new GuiButton(5, var1 * 0 + var2 / 2, height - 45, 130, 20, I18n.format("gui.cancel")));
      this.buttonList.add(new GuiButton(3, var1 * 1 + var2 / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unban")));
      this.buttonList.add(new GuiButton(4, var1 * 2 + var2 / 2, height - 45, 130, 20, I18n.format("stream.userinfo.unmod")));
      int var3 = 0;

      IChatComponent var4;
      for(Iterator var5 = this.field_152332_r.iterator(); var5.hasNext(); var3 = Math.max(var3, this.fontRendererObj.getStringWidth(var4.getFormattedText()))) {
         var4 = (IChatComponent)var5.next();
      }

      this.field_152334_t = width / 2 - var3 / 2;
   }

   public static IChatComponent func_152330_a(ChatUserSubscription var0, String var1, boolean var2) {
      ChatComponentTranslation var3 = null;
      if (var0 == ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) {
         if (var1 == null) {
            var3 = new ChatComponentTranslation("stream.user.subscription.subscriber", new Object[0]);
         } else if (var2) {
            var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.self", new Object[0]);
         } else {
            var3 = new ChatComponentTranslation("stream.user.subscription.subscriber.other", new Object[]{var1});
         }

         var3.getChatStyle().setColor(field_152331_a);
      } else if (var0 == ChatUserSubscription.TTV_CHAT_USERSUB_TURBO) {
         var3 = new ChatComponentTranslation("stream.user.subscription.turbo", new Object[0]);
         var3.getChatStyle().setColor(field_152336_g);
      }

      return var3;
   }

   public static IChatComponent func_152329_a(ChatUserMode var0, String var1, boolean var2) {
      ChatComponentTranslation var3 = null;
      if (var0 == ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) {
         var3 = new ChatComponentTranslation("stream.user.mode.administrator", new Object[0]);
         var3.getChatStyle().setColor(field_152336_g);
      } else if (var0 == ChatUserMode.TTV_CHAT_USERMODE_BANNED) {
         if (var1 == null) {
            var3 = new ChatComponentTranslation("stream.user.mode.banned", new Object[0]);
         } else if (var2) {
            var3 = new ChatComponentTranslation("stream.user.mode.banned.self", new Object[0]);
         } else {
            var3 = new ChatComponentTranslation("stream.user.mode.banned.other", new Object[]{var1});
         }

         var3.getChatStyle().setColor(field_152335_f);
      } else if (var0 == ChatUserMode.TTV_CHAT_USERMODE_BROADCASTER) {
         if (var1 == null) {
            var3 = new ChatComponentTranslation("stream.user.mode.broadcaster", new Object[0]);
         } else if (var2) {
            var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.self", new Object[0]);
         } else {
            var3 = new ChatComponentTranslation("stream.user.mode.broadcaster.other", new Object[0]);
         }

         var3.getChatStyle().setColor(field_152331_a);
      } else if (var0 == ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) {
         if (var1 == null) {
            var3 = new ChatComponentTranslation("stream.user.mode.moderator", new Object[0]);
         } else if (var2) {
            var3 = new ChatComponentTranslation("stream.user.mode.moderator.self", new Object[0]);
         } else {
            var3 = new ChatComponentTranslation("stream.user.mode.moderator.other", new Object[]{var1});
         }

         var3.getChatStyle().setColor(field_152331_a);
      } else if (var0 == ChatUserMode.TTV_CHAT_USERMODE_STAFF) {
         var3 = new ChatComponentTranslation("stream.user.mode.staff", new Object[0]);
         var3.getChatStyle().setColor(field_152336_g);
      }

      return var3;
   }

   public static List func_152328_a(Set var0, Set var1, IStream var2) {
      String var3 = var2 == null ? null : var2.func_152921_C();
      boolean var4 = var2 != null && var2.func_152927_B();
      ArrayList var5 = Lists.newArrayList();
      Iterator var7 = var0.iterator();

      IChatComponent var8;
      ChatComponentText var9;
      while(var7.hasNext()) {
         ChatUserMode var6 = (ChatUserMode)var7.next();
         var8 = func_152329_a(var6, var3, var4);
         if (var8 != null) {
            var9 = new ChatComponentText("- ");
            var9.appendSibling(var8);
            var5.add(var9);
         }
      }

      var7 = var1.iterator();

      while(var7.hasNext()) {
         ChatUserSubscription var10 = (ChatUserSubscription)var7.next();
         var8 = func_152330_a(var10, var3, var4);
         if (var8 != null) {
            var9 = new ChatComponentText("- ");
            var9.appendSibling(var8);
            var5.add(var9);
         }
      }

      return var5;
   }

   public GuiTwitchUserMode(IStream var1, ChatUserInfo var2) {
      this.stream = var1;
      this.field_152337_h = var2;
      this.field_152338_i = new ChatComponentText(var2.displayName);
      this.field_152332_r.addAll(func_152328_a(var2.modes, var2.subscriptions, var1));
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      this.drawCenteredString(this.fontRendererObj, this.field_152338_i.getUnformattedText(), width / 2, 70, 16777215);
      int var4 = 80;

      for(Iterator var6 = this.field_152332_r.iterator(); var6.hasNext(); var4 += this.fontRendererObj.FONT_HEIGHT) {
         IChatComponent var5 = (IChatComponent)var6.next();
         this.drawString(this.fontRendererObj, var5.getFormattedText(), this.field_152334_t, var4, 16777215);
      }

      super.drawScreen(var1, var2, var3);
   }

   protected void actionPerformed(GuiButton var1) throws IOException {
      if (var1.enabled) {
         if (var1.id == 0) {
            this.stream.func_152917_b("/ban " + this.field_152337_h.displayName);
         } else if (var1.id == 3) {
            this.stream.func_152917_b("/unban " + this.field_152337_h.displayName);
         } else if (var1.id == 2) {
            this.stream.func_152917_b("/mod " + this.field_152337_h.displayName);
         } else if (var1.id == 4) {
            this.stream.func_152917_b("/unmod " + this.field_152337_h.displayName);
         } else if (var1.id == 1) {
            this.stream.func_152917_b("/timeout " + this.field_152337_h.displayName);
         }

         this.mc.displayGuiScreen((GuiScreen)null);
      }

   }
}
