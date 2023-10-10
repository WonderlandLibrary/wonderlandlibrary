package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class RealmsSubscriptionScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private final RealmsScreen lastScreen;
   private final RealmsServer serverData;
   private final int BUTTON_BACK_ID = 0;
   private int daysLeft;
   private String startDate;
   private Subscription.SubscriptionType type;
   private final String baseUrl = "https://account.mojang.com";
   private final String path = "/buy/realms";
   private boolean onLink;

   public RealmsSubscriptionScreen(RealmsScreen var1, RealmsServer var2) {
      this.lastScreen = var1;
      this.serverData = var2;
   }

   public void tick() {
   }

   public void init() {
      this.getSubscription(this.serverData.id);
      Keyboard.enableRepeatEvents(true);
      this.buttonsAdd(newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 12, getLocalizedString("gui.back")));
   }

   private void getSubscription(long var1) {
      RealmsClient var3 = RealmsClient.createRealmsClient();

      try {
         Subscription var4 = var3.subscriptionFor(var1);
         this.daysLeft = var4.daysLeft;
         this.startDate = this.localPresentation(var4.startDate);
         this.type = var4.type;
      } catch (RealmsServiceException var5) {
         LOGGER.error("Couldn't get subscription");
         Realms.setScreen(new RealmsGenericErrorScreen(var5, this));
      } catch (IOException var6) {
         LOGGER.error("Couldn't parse response subscribing");
      }

   }

   private String localPresentation(long var1) {
      GregorianCalendar var3 = new GregorianCalendar(TimeZone.getDefault());
      var3.setTimeInMillis(var1);
      return SimpleDateFormat.getDateTimeInstance().format(var3.getTime());
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 0) {
            Realms.setScreen(this.lastScreen);
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.lastScreen);
      }

   }

   private String getProfileUuid() {
      String var1 = Realms.getSessionId();
      String[] var2 = var1.split(":");
      return var2.length == 3 ? var2[2] : "";
   }

   private void browseTo(String var1) {
      try {
         URI var2 = new URI(var1);
         Class var3 = Class.forName("java.awt.Desktop");
         Object var4 = var3.getMethod("getDesktop").invoke((Object)null);
         var3.getMethod("browse", URI.class).invoke(var4, var2);
      } catch (Throwable var5) {
         LOGGER.error("Couldn't open link");
      }

   }

   public void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      if (this.onLink) {
         String var4 = "https://account.mojang.com/buy/realms?sid=" + this.serverData.remoteSubscriptionId + "&pid=" + this.getProfileUuid();
         Clipboard var5 = Toolkit.getDefaultToolkit().getSystemClipboard();
         var5.setContents(new StringSelection(var4), (ClipboardOwner)null);
         this.browseTo(var4);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.drawCenteredString(getLocalizedString("mco.configure.world.subscription.title"), this.width() / 2, 17, 16777215);
      this.drawString(getLocalizedString("mco.configure.world.subscription.start"), this.width() / 2 - 100, 53, 10526880);
      this.drawString(this.startDate, this.width() / 2 - 100, 66, 16777215);
      if (this.type == Subscription.SubscriptionType.NORMAL) {
         this.drawString(getLocalizedString("mco.configure.world.subscription.daysleft"), this.width() / 2 - 100, 85, 10526880);
         this.drawString(this.getDaysLeft(), this.width() / 2 - 100, 98, 16777215);
      } else if (this.type == Subscription.SubscriptionType.RECURRING) {
         this.drawString(getLocalizedString("mco.configure.world.subscription.recurring.daysleft"), this.width() / 2 - 100, 85, 10526880);
         this.drawString(this.daysLeftPresentation(this.daysLeft), this.width() / 2 - 100, 98, 16777215);
      }

      this.drawString(getLocalizedString("mco.configure.world.subscription.extendHere"), this.width() / 2 - 100, 117, 10526880);
      String var4 = "https://account.mojang.com/buy/realms";
      int var5 = 3368635;
      int var6 = 7107012;
      short var7 = 130;
      int var8 = this.fontWidth(var4);
      int var9 = this.width() / 2 - var8 / 2 - 1;
      int var10 = var7 - 1;
      int var11 = var9 + var8 + 1;
      int var12 = var7 + 1 + this.fontLineHeight();
      if (var9 <= var1 && var1 <= var11 && var10 <= var2 && var2 <= var12) {
         this.onLink = true;
         this.drawString("§n" + var4, this.width() / 2 - var8 / 2, var7, var6);
      } else {
         this.onLink = false;
         this.drawString(var4, this.width() / 2 - var8 / 2, var7, var5);
      }

      super.render(var1, var2, var3);
   }

   private String daysLeftPresentation(int var1) {
      if (var1 == -1) {
         return "Expired";
      } else {
         return var1 > 1 ? var1 + " days" : getLocalizedString("mco.configure.world.subscription.less_than_a_day");
      }
   }

   private String getDaysLeft() {
      return this.daysLeft >= 0 ? String.valueOf(this.daysLeft) : "Expired";
   }
}
