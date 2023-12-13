package com.mojang.realmsclient;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.exception.RetryCallException;
import com.mojang.realmsclient.gui.ChatFormatting;
import com.mojang.realmsclient.gui.RealmsConnectTask;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import com.mojang.realmsclient.gui.screens.BuyRealmsScreen;
import com.mojang.realmsclient.gui.screens.CreateRealmsWorldScreen;
import com.mojang.realmsclient.gui.screens.LongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.LongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.PendingInvitationScreen;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsMth;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import net.minecraft.realms.RealmsServerStatusPinger;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class RealmsMainScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static boolean overrideConfigure = false;
   private static boolean stageEnabled = false;
   protected static final int BACK_BUTTON_ID = 0;
   protected static final int PLAY_BUTTON_ID = 1;
   protected static final int BUY_BUTTON_ID = 2;
   protected static final int CONFIGURE_BUTTON_ID = 3;
   protected static final int GET_MORE_INFO_BUTTON_ID = 4;
   protected static final int LEAVE_BUTTON_ID = 5;
   private static final String ON_ICON_LOCATION = "realms:textures/gui/realms/on_icon.png";
   private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
   private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
   private static final String INVITATION_ICONS_LOCATION = "realms:textures/gui/realms/invitation_icons.png";
   private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
   private static final String WORLDICON_LOCATION = "realms:textures/gui/realms/world_icon.png";
   private static final String LOGO_LOCATION = "realms:textures/gui/title/realms.png";
   private static RealmsDataFetcher realmsDataFetcher = new RealmsDataFetcher();
   private static RealmsServerStatusPinger statusPinger = new RealmsServerStatusPinger();
   private static final ThreadPoolExecutor THREAD_POOL = new ScheduledThreadPoolExecutor(5, (new ThreadFactoryBuilder()).setNameFormat("Server Pinger #%d").setDaemon(true).build());
   private static int lastScrollYPosition = -1;
   private RealmsScreen lastScreen;
   private volatile RealmsMainScreen.ServerSelectionList serverSelectionList;
   private long selectedServerId = -1L;
   private RealmsButton configureButton;
   private RealmsButton leaveButton;
   private RealmsButton playButton;
   private RealmsButton buyButton;
   private String toolTip;
   private List realmsServers = Lists.newArrayList();
   private static final String mcoInfoUrl = "https://minecraft.net/realms";
   private volatile int numberOfPendingInvites = 0;
   public static final int EXPIRATION_NOTIFICATION_DAYS = 7;
   private int animTick;
   private static volatile boolean mcoEnabled;
   private static volatile boolean mcoEnabledCheck;
   private static boolean checkedMcoAvailability;
   private static RealmsScreen realmsGenericErrorScreen = null;
   private static boolean regionsPinged = false;
   private boolean onLink = false;
   private int mindex = 0;
   private char[] mchars = new char[]{'3', '2', '1', '4', '5', '6'};
   private int sindex = 0;
   private char[] schars = new char[]{'9', '8', '7', '1', '2', '3'};

   public RealmsMainScreen(RealmsScreen var1) {
      this.lastScreen = var1;
      this.checkIfMcoEnabled();
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.serverSelectionList.mouseEvent();
   }

   public void init() {
      if (realmsGenericErrorScreen != null) {
         Realms.setScreen(realmsGenericErrorScreen);
      } else {
         Keyboard.enableRepeatEvents(true);
         this.buttonsClear();
         this.postInit();
         if (this.isMcoEnabled()) {
            realmsDataFetcher.init();
         }

      }
   }

   public void postInit() {
      this.buttonsAdd(this.playButton = newButton(1, this.width() / 2 - 154, this.height() - 52, 154, 20, getLocalizedString("mco.selectServer.play")));
      this.buttonsAdd(this.leaveButton = newButton(5, this.width() / 2 - 154, this.height() - 28, 102, 20, getLocalizedString("mco.selectServer.leave")));
      this.buttonsAdd(this.configureButton = newButton(3, this.width() / 2 + 6, this.height() - 52, 154, 20, getLocalizedString("mco.selectServer.configure")));
      this.buttonsAdd(this.buyButton = newButton(2, this.width() / 2 - 48, this.height() - 28, 102, 20, getLocalizedString("mco.selectServer.buy")));
      this.buttonsAdd(newButton(0, this.width() / 2 + 58, this.height() - 28, 102, 20, getLocalizedString("gui.back")));
      this.serverSelectionList = new RealmsMainScreen.ServerSelectionList(this);
      if (lastScrollYPosition != -1) {
         this.serverSelectionList.scroll(lastScrollYPosition);
      }

      RealmsServer var1 = this.findServer(this.selectedServerId);
      this.playButton.active(var1 != null && var1.state == RealmsServer.State.OPEN && !var1.expired);
      this.configureButton.active(overrideConfigure || var1 != null && var1.state != RealmsServer.State.ADMIN_LOCK && var1.owner.equals(Realms.getName()));
      this.leaveButton.active(var1 != null && !var1.owner.equals(Realms.getName()));
   }

   public void tick() {
      ++this.animTick;
      if (this != false) {
         Realms.setScreen(new RealmsParentalConsentScreen(this.lastScreen));
      }

      if (this.isMcoEnabled()) {
         realmsDataFetcher.init();
         if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.SERVER_LIST)) {
            List var1 = realmsDataFetcher.getServers();
            boolean var2 = false;
            Iterator var3 = var1.iterator();

            while(true) {
               while(var3.hasNext()) {
                  RealmsServer var4 = (RealmsServer)var3.next();
                  if (var4 != null) {
                     var2 = true;
                  }

                  Iterator var5 = this.realmsServers.iterator();

                  while(var5.hasNext()) {
                     RealmsServer var6 = (RealmsServer)var5.next();
                     if (var4.id == var6.id) {
                        var4.latestStatFrom(var6);
                        break;
                     }
                  }
               }

               this.realmsServers = var1;
               if (!regionsPinged && var2) {
                  regionsPinged = true;
                  this.pingRegions();
               }
               break;
            }
         }

         if (realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
            this.numberOfPendingInvites = realmsDataFetcher.getPendingInvitesCount();
         }

         realmsDataFetcher.markClean();
      }
   }

   private void pingRegions() {
      (new Thread(this) {
         final RealmsMainScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            List var1 = Ping.pingAllRegions();
            RealmsClient var2 = RealmsClient.createRealmsClient();
            PingResult var3 = new PingResult();
            var3.pingResults = var1;
            var3.worldIds = RealmsMainScreen.access$000(this.this$0);

            try {
               var2.sendPingResults(var3);
            } catch (Throwable var5) {
               RealmsMainScreen.access$100().warn("Could not send ping result to Realms: ", var5);
            }

         }
      }).start();
   }

   private List getOwnedNonExpiredWorldIds() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.realmsServers.iterator();

      while(var2.hasNext()) {
         RealmsServer var3 = (RealmsServer)var2.next();
         if (var3 != null) {
            var1.add(var3.id);
         }
      }

      return var1;
   }

   private boolean isMcoEnabled() {
      return mcoEnabled;
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            this.play(this.selectedServerId);
         } else if (var1.id() == 3) {
            this.configureClicked();
         } else if (var1.id() == 5) {
            this.leaveClicked();
         } else if (var1.id() == 0) {
            this.stopRealmsFetcherAndPinger();
            Realms.setScreen(this.lastScreen);
         } else if (var1.id() == 2) {
            this.saveListScrollPosition();
            this.stopRealmsFetcherAndPinger();
            Realms.setScreen(new BuyRealmsScreen(this));
         } else if (var1.id() == 4) {
            this.moreInfoButtonClicked();
         }

      }
   }

   private void checkIfMcoEnabled() {
      if (!checkedMcoAvailability) {
         checkedMcoAvailability = true;
         (new Thread(this, "MCO Availability Checker #1") {
            final RealmsMainScreen this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               RealmsClient var1 = RealmsClient.createRealmsClient();

               try {
                  if (var1.clientOutdated()) {
                     Realms.setScreen(RealmsMainScreen.access$202(new RealmsClientOutdatedScreen(RealmsMainScreen.access$300(this.this$0))));
                     return;
                  }
               } catch (RealmsServiceException var9) {
                  RealmsMainScreen.access$100().error("Couldn't connect to realms: ", var9.toString());
                  if (var9.httpResultCode == 401) {
                     RealmsMainScreen.access$202(new RealmsGenericErrorScreen(var9, RealmsMainScreen.access$300(this.this$0)));
                  }

                  Realms.setScreen(new RealmsGenericErrorScreen(var9, RealmsMainScreen.access$300(this.this$0)));
                  return;
               } catch (IOException var10) {
                  RealmsMainScreen.access$100().error("Couldn't connect to realms: ", var10.getMessage());
                  Realms.setScreen(new RealmsGenericErrorScreen(var10.getMessage(), RealmsMainScreen.access$300(this.this$0)));
                  return;
               }

               boolean var2 = false;

               for(int var3 = 0; var3 < 3; ++var3) {
                  try {
                     Boolean var4 = var1.mcoEnabled();
                     RealmsMainScreen.access$402(true);
                     if (var4) {
                        RealmsMainScreen.access$100().info("Realms is available for this user");
                        RealmsMainScreen.access$502(true);
                     } else {
                        RealmsMainScreen.access$100().info("Realms is not available for this user");
                        RealmsMainScreen.access$502(false);
                     }
                  } catch (RetryCallException var6) {
                     var2 = true;
                  } catch (RealmsServiceException var7) {
                     RealmsMainScreen.access$100().error("Couldn't connect to Realms: " + var7.toString());
                  } catch (IOException var8) {
                     RealmsMainScreen.access$100().error("Couldn't parse response connecting to Realms: " + var8.getMessage());
                  }

                  if (!var2) {
                     break;
                  }

                  try {
                     Thread.sleep(5000L);
                  } catch (InterruptedException var5) {
                     Thread.currentThread().interrupt();
                  }
               }

            }
         }).start();
      }

   }

   private void switchToStage() {
      if (!stageEnabled) {
         (new Thread(this, "MCO Stage Availability Checker #1") {
            final RealmsMainScreen this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               RealmsClient var1 = RealmsClient.createRealmsClient();

               try {
                  Boolean var2 = var1.stageAvailable();
                  if (var2) {
                     RealmsClient.switchToStage();
                     RealmsMainScreen.access$100().info("Switched to stage");
                     RealmsMainScreen.access$602(true);
                  } else {
                     RealmsMainScreen.access$602(false);
                  }
               } catch (RealmsServiceException var3) {
                  RealmsMainScreen.access$100().error("Couldn't connect to Realms: " + var3.toString());
               } catch (IOException var4) {
                  RealmsMainScreen.access$100().error("Couldn't parse response connecting to Realms: " + var4.getMessage());
               }

            }
         }).start();
      }

   }

   private void switchToProd() {
      if (stageEnabled) {
         stageEnabled = false;
         RealmsClient.switchToProd();
      }

   }

   private void stopRealmsFetcherAndPinger() {
      if (this.isMcoEnabled()) {
         realmsDataFetcher.stop();
         statusPinger.removeAll();
      }

   }

   private void moreInfoButtonClicked() {
      String var1 = getLocalizedString("mco.more.info.question.line1");
      String var2 = getLocalizedString("mco.more.info.question.line2");
      Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var1, var2, 4));
   }

   private void configureClicked() {
      RealmsServer var1 = this.findServer(this.selectedServerId);
      if (var1 != null && (Realms.getName().equals(var1.owner) || overrideConfigure)) {
         this.stopRealmsFetcherAndPinger();
         this.saveListScrollPosition();
         Realms.setScreen(new RealmsConfigureWorldScreen(this, var1.id));
      }

   }

   private void leaveClicked() {
      RealmsServer var1 = this.findServer(this.selectedServerId);
      if (var1 != null && !Realms.getName().equals(var1.owner)) {
         this.saveListScrollPosition();
         String var2 = getLocalizedString("mco.configure.world.leave.question.line1");
         String var3 = getLocalizedString("mco.configure.world.leave.question.line2");
         Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var2, var3, 5));
      }

   }

   private void saveListScrollPosition() {
      lastScrollYPosition = this.serverSelectionList.getScroll();
   }

   private RealmsServer findServer(long var1) {
      Iterator var3 = this.realmsServers.iterator();

      RealmsServer var4;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         var4 = (RealmsServer)var3.next();
      } while(var4.id != var1);

      return var4;
   }

   private int findIndex(long var1) {
      for(int var3 = 0; var3 < this.realmsServers.size(); ++var3) {
         if (((RealmsServer)this.realmsServers.get(var3)).id == var1) {
            return var3;
         }
      }

      return -1;
   }

   public void confirmResult(boolean var1, int var2) {
      if (var2 == 5 && var1) {
         (new Thread(this, "Realms-leave-server") {
            final RealmsMainScreen this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               try {
                  RealmsServer var1 = RealmsMainScreen.access$800(this.this$0, RealmsMainScreen.access$700(this.this$0));
                  if (var1 != null) {
                     RealmsClient var2 = RealmsClient.createRealmsClient();
                     RealmsMainScreen.access$900().removeItem(var1);
                     RealmsMainScreen.access$1000(this.this$0).remove(var1);
                     var2.uninviteMyselfFrom(var1.id);
                     RealmsMainScreen.access$900().removeItem(var1);
                     RealmsMainScreen.access$1000(this.this$0).remove(var1);
                     RealmsMainScreen.access$1100(this.this$0);
                  }
               } catch (RealmsServiceException var3) {
                  RealmsMainScreen.access$100().error("Couldn't configure world");
                  Realms.setScreen(new RealmsGenericErrorScreen(var3, this.this$0));
               }

            }
         }).start();
      }

      Realms.setScreen(this);
   }

   private void updateSelectedItemPointer() {
      int var1 = this.findIndex(this.selectedServerId);
      if (this.realmsServers.size() - 1 == var1) {
         --var1;
      }

      if (this.realmsServers.size() == 0) {
         var1 = -1;
      }

      if (var1 >= 0 && var1 < this.realmsServers.size()) {
         this.selectedServerId = ((RealmsServer)this.realmsServers.get(var1)).id;
      }

   }

   public void removeSelection() {
      this.selectedServerId = -1L;
   }

   public void keyPressed(char var1, int var2) {
      if (var2 != 28 && var2 != 156) {
         if (var2 == 1) {
            this.mindex = 0;
            this.sindex = 0;
            this.stopRealmsFetcherAndPinger();
            Realms.setScreen(this.lastScreen);
         } else {
            if (this.mchars[this.mindex] == var1) {
               ++this.mindex;
               if (this.mindex == this.mchars.length) {
                  this.mindex = 0;
                  overrideConfigure = true;
               }
            } else {
               this.mindex = 0;
            }

            if (this.schars[this.sindex] == var1) {
               ++this.sindex;
               if (this.sindex == this.schars.length) {
                  this.sindex = 0;
                  if (!stageEnabled) {
                     this.switchToStage();
                  } else {
                     this.switchToProd();
                  }
               }

               return;
            }

            this.sindex = 0;
         }
      } else {
         this.mindex = 0;
         this.sindex = 0;
         this.buttonClicked(this.playButton);
      }

   }

   public void render(int var1, int var2, float var3) {
      this.toolTip = null;
      this.renderBackground();
      this.serverSelectionList.render(var1, var2, var3);
      this.drawRealmsLogo(this.width() / 2 - 50, 7);
      this.renderLink(var1, var2);
      if (this.toolTip != null) {
         this.renderMousehoverTooltip(this.toolTip, var1, var2);
      }

      this.drawInvitationPendingIcon(var1, var2);
      if (stageEnabled) {
         this.renderStage();
      }

      super.render(var1, var2, var3);
   }

   private void drawRealmsLogo(int var1, int var2) {
      RealmsScreen.bind("realms:textures/gui/title/realms.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2 - 5, 0.0F, 0.0F, 200, 50, 200.0F, 50.0F);
      GL11.glPopMatrix();
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (var1 <= var2 && this.numberOfPendingInvites != 0) {
         this.stopRealmsFetcherAndPinger();
         PendingInvitationScreen var4 = new PendingInvitationScreen(this.lastScreen);
         Realms.setScreen(var4);
      }

      if (this.onLink) {
         this.browseTo("https://minecraft.net/realms");
      }

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

   private void drawInvitationPendingIcon(int var1, int var2) {
      int var3 = this.numberOfPendingInvites;
      boolean var4 = this.inPendingInvitationArea(var1, var2);
      int var5 = this.width() / 2 + 50;
      boolean var6 = true;
      int var8;
      if (var3 != 0) {
         float var7 = 0.25F + (1.0F + RealmsMth.sin((float)this.animTick * 0.5F)) * 0.25F;
         var8 = -16777216 | (int)(var7 * 64.0F) << 16 | (int)(var7 * 64.0F) << 8 | (int)(var7 * 64.0F) << 0;
         this.fillGradient(var5 - 2, 10, var5 + 18, 30, var8, var8);
         var8 = -16777216 | (int)(var7 * 255.0F) << 16 | (int)(var7 * 255.0F) << 8 | (int)(var7 * 255.0F) << 0;
         this.fillGradient(var5 - 2, 10, var5 + 18, 11, var8, var8);
         this.fillGradient(var5 - 2, 10, var5 - 1, 30, var8, var8);
         this.fillGradient(var5 + 17, 10, var5 + 18, 30, var8, var8);
         this.fillGradient(var5 - 2, 29, var5 + 18, 30, var8, var8);
      }

      RealmsScreen.bind("realms:textures/gui/realms/invite_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      RealmsScreen.blit(var5, 6, var4 ? 16.0F : 0.0F, 0.0F, 15, 25, 31.0F, 25.0F);
      GL11.glPopMatrix();
      if (var3 != 0) {
         int var12 = (Math.min(var3, 6) - 1) * 8;
         var8 = (int)(Math.max(0.0F, Math.max(RealmsMth.sin((float)(10 + this.animTick) * 0.57F), RealmsMth.cos((float)this.animTick * 0.35F))) * -6.0F);
         RealmsScreen.bind("realms:textures/gui/realms/invitation_icons.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         RealmsScreen.blit(var5 + 4, 16 + var8, (float)var12, 0.0F, 8, 8, 48.0F, 8.0F);
         GL11.glPopMatrix();
      }

      boolean var13 = var4 || var3 != 0;
      if (var13) {
         var8 = var1 + 12;
         int var9 = var2 - 12;
         if (!var4) {
            var8 = var5 + 22;
            var9 = 12;
         }

         String var10;
         if (var3 != 0) {
            var10 = getLocalizedString("mco.invites.pending");
         } else {
            var10 = getLocalizedString("mco.invites.nopending");
         }

         int var11 = this.fontWidth(var10);
         this.fillGradient(var8 - 3, var9 - 3, var8 + var11 + 3, var9 + 8 + 3, -1073741824, -1073741824);
         this.fontDrawShadow(var10, var8, var9, -1);
      }

   }

   private void play(long var1) {
      RealmsServer var3 = this.findServer(var1);
      if (var3 != null) {
         this.stopRealmsFetcherAndPinger();
         LongRunningMcoTaskScreen var4 = new LongRunningMcoTaskScreen(this, new RealmsConnectTask(this, var3));
         var4.start();
         Realms.setScreen(var4);
      }

   }

   private boolean isSelfOwnedServer(RealmsServer var1) {
      return var1.owner != null && var1.owner.equals(Realms.getName());
   }

   private void drawExpired(int var1, int var2, int var3, int var4) {
      RealmsScreen.bind("realms:textures/gui/realms/expired_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.height() - 64 && var4 > 32) {
         this.toolTip = getLocalizedString("mco.selectServer.expired");
      }

   }

   private void drawExpiring(int var1, int var2, int var3, int var4, int var5) {
      if (this.animTick % 20 < 10) {
         RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glScalef(0.5F, 0.5F, 0.5F);
         RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
         GL11.glPopMatrix();
      }

      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.height() - 64 && var4 > 32) {
         if (var5 == 0) {
            this.toolTip = getLocalizedString("mco.selectServer.expires.soon");
         } else if (var5 == 1) {
            this.toolTip = getLocalizedString("mco.selectServer.expires.day");
         } else {
            this.toolTip = getLocalizedString("mco.selectServer.expires.days", new Object[]{var5});
         }
      }

   }

   private void drawOpen(int var1, int var2, int var3, int var4) {
      RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.height() - 64 && var4 > 32) {
         this.toolTip = getLocalizedString("mco.selectServer.open");
      }

   }

   private void drawClose(int var1, int var2, int var3, int var4) {
      RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.height() - 64 && var4 > 32) {
         this.toolTip = getLocalizedString("mco.selectServer.closed");
      }

   }

   private void drawLocked(int var1, int var2, int var3, int var4) {
      RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9 && var4 < this.height() - 64 && var4 > 32) {
         this.toolTip = getLocalizedString("mco.selectServer.locked");
      }

   }

   protected void renderMousehoverTooltip(String var1, int var2, int var3) {
      if (var1 != null) {
         int var4 = var2 + 12;
         int var5 = var3 - 12;
         int var6 = this.fontWidth(var1);
         this.fillGradient(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
         this.fontDrawShadow(var1, var4, var5, -1);
      }
   }

   private void renderLink(int var1, int var2) {
      String var3 = getLocalizedString("mco.selectServer.whatisrealms");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      int var4 = 3368635;
      int var5 = 7107012;
      int var6 = this.fontWidth(var3);
      byte var7 = 10;
      byte var8 = 12;
      int var10 = var7 + var6 + 1;
      int var12 = var8 + this.fontLineHeight();
      GL11.glTranslatef((float)var7, (float)var8, 0.0F);
      if (var7 <= var1 && var1 <= var10 && var8 <= var2 && var2 <= var12) {
         this.onLink = true;
         this.drawString(var3, 0, 0, var5);
      } else {
         this.onLink = false;
         this.drawString(var3, 0, 0, var4);
      }

      GL11.glPopMatrix();
   }

   private void renderStage() {
      String var1 = "STAGE!";
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)(this.width() / 2 - 25), 20.0F, 0.0F);
      GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
      GL11.glScalef(1.5F, 1.5F, 1.5F);
      this.drawString(var1, 0, 0, -256);
      GL11.glPopMatrix();
   }

   static List access$000(RealmsMainScreen var0) {
      return var0.getOwnedNonExpiredWorldIds();
   }

   static Logger access$100() {
      return LOGGER;
   }

   static RealmsScreen access$202(RealmsScreen var0) {
      realmsGenericErrorScreen = var0;
      return var0;
   }

   static RealmsScreen access$300(RealmsMainScreen var0) {
      return var0.lastScreen;
   }

   static boolean access$402(boolean var0) {
      mcoEnabledCheck = var0;
      return var0;
   }

   static boolean access$502(boolean var0) {
      mcoEnabled = var0;
      return var0;
   }

   static boolean access$602(boolean var0) {
      stageEnabled = var0;
      return var0;
   }

   static long access$700(RealmsMainScreen var0) {
      return var0.selectedServerId;
   }

   static RealmsServer access$800(RealmsMainScreen var0, long var1) {
      return var0.findServer(var1);
   }

   static RealmsDataFetcher access$900() {
      return realmsDataFetcher;
   }

   static List access$1000(RealmsMainScreen var0) {
      return var0.realmsServers;
   }

   static void access$1100(RealmsMainScreen var0) {
      var0.updateSelectedItemPointer();
   }

   static long access$702(RealmsMainScreen var0, long var1) {
      return var0.selectedServerId = var1;
   }

   static void access$1200(RealmsMainScreen var0) {
      var0.stopRealmsFetcherAndPinger();
   }

   static boolean access$1300() {
      return overrideConfigure;
   }

   static boolean access$1400(RealmsMainScreen var0, RealmsServer var1) {
      return var0.isSelfOwnedServer(var1);
   }

   static RealmsButton access$1500(RealmsMainScreen var0) {
      return var0.configureButton;
   }

   static RealmsButton access$1600(RealmsMainScreen var0) {
      return var0.leaveButton;
   }

   static RealmsButton access$1700(RealmsMainScreen var0) {
      return var0.playButton;
   }

   static void access$1800(RealmsMainScreen var0, long var1) {
      var0.play(var1);
   }

   static int access$1900(RealmsMainScreen var0, long var1) {
      return var0.findIndex(var1);
   }

   static int access$2000(RealmsMainScreen var0) {
      return var0.animTick;
   }

   static RealmsServerStatusPinger access$2100() {
      return statusPinger;
   }

   static ThreadPoolExecutor access$2200() {
      return THREAD_POOL;
   }

   static void access$2300(RealmsMainScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawExpired(var1, var2, var3, var4);
   }

   static void access$2400(RealmsMainScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawClose(var1, var2, var3, var4);
   }

   static void access$2500(RealmsMainScreen var0, int var1, int var2, int var3, int var4, int var5) {
      var0.drawExpiring(var1, var2, var3, var4, var5);
   }

   static void access$2600(RealmsMainScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawOpen(var1, var2, var3, var4);
   }

   static void access$2700(RealmsMainScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawLocked(var1, var2, var3, var4);
   }

   static {
      String var0 = RealmsVersion.getVersion();
      if (var0 != null) {
         LOGGER.info("Realms library version == " + var0);
      }

   }

   private class ServerSelectionList extends RealmsScrolledSelectionList {
      final RealmsMainScreen this$0;

      public ServerSelectionList(RealmsMainScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return RealmsMainScreen.access$1000(this.this$0).size() + 1;
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
         if (var1 < RealmsMainScreen.access$1000(this.this$0).size()) {
            RealmsServer var5 = (RealmsServer)RealmsMainScreen.access$1000(this.this$0).get(var1);
            RealmsMainScreen.access$702(this.this$0, var5.id);
            if (var5.state == RealmsServer.State.UNINITIALIZED) {
               RealmsMainScreen.access$1200(this.this$0);
               Realms.setScreen(new CreateRealmsWorldScreen(var5.id, this.this$0));
            }

            RealmsMainScreen.access$1500(this.this$0).active(RealmsMainScreen.access$1300() || RealmsMainScreen.access$1400(this.this$0, var5) && var5.state != RealmsServer.State.ADMIN_LOCK);
            RealmsMainScreen.access$1600(this.this$0).active(!RealmsMainScreen.access$1400(this.this$0, var5));
            RealmsMainScreen.access$1700(this.this$0).active(var5.state == RealmsServer.State.OPEN && !var5.expired);
            if (var2 && RealmsMainScreen.access$1700(this.this$0).active()) {
               RealmsMainScreen.access$1800(this.this$0, RealmsMainScreen.access$700(this.this$0));
            }

         }
      }

      public boolean isSelectedItem(int var1) {
         return var1 == RealmsMainScreen.access$1900(this.this$0, RealmsMainScreen.access$700(this.this$0));
      }

      public int getMaxPosition() {
         return this.getItemCount() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
         if (var1 < RealmsMainScreen.access$1000(this.this$0).size()) {
            this.renderMcoServerItem(var1, var2, var3);
         }

      }

      private void renderMcoServerItem(int var1, int var2, int var3) {
         RealmsServer var4 = (RealmsServer)RealmsMainScreen.access$1000(this.this$0).get(var1);
         int var5 = -1;
         if (RealmsMainScreen.access$1400(this.this$0, var4)) {
            var5 = -8388737;
         }

         if (var4.state == RealmsServer.State.UNINITIALIZED) {
            RealmsScreen.bind("realms:textures/gui/realms/world_icon.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(3008);
            GL11.glPushMatrix();
            RealmsScreen.blit(var2 + 10, var3 + 6, 0.0F, 0.0F, 40, 20, 40.0F, 20.0F);
            GL11.glPopMatrix();
            float var12 = 0.5F + (1.0F + RealmsMth.sin((float)RealmsMainScreen.access$2000(this.this$0) * 0.25F)) * 0.25F;
            int var13 = -16777216 | (int)(127.0F * var12) << 16 | (int)(255.0F * var12) << 8 | (int)(127.0F * var12);
            this.this$0.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.uninitialized"), var2 + 10 + 40 + 75, var3 + 12, var13);
         } else {
            if (var4.shouldPing(Realms.currentTimeMillis())) {
               var4.serverPing.lastPingSnapshot = Realms.currentTimeMillis();
               RealmsMainScreen.access$2200().submit(new Runnable(this, var4) {
                  final RealmsServer val$serverData;
                  final RealmsMainScreen.ServerSelectionList this$1;

                  {
                     this.this$1 = var1;
                     this.val$serverData = var2;
                  }

                  public void run() {
                     try {
                        RealmsMainScreen.access$2100().pingServer(this.val$serverData.ip, this.val$serverData.serverPing);
                     } catch (UnknownHostException var2) {
                        RealmsMainScreen.access$100().error("Pinger: Could not resolve host");
                     }

                  }
               });
            }

            this.this$0.drawString(var4.getName(), var2 + 2, var3 + 1, var5);
            short var6 = 207;
            byte var7 = 1;
            if (var4.expired) {
               RealmsMainScreen.access$2300(this.this$0, var2 + var6, var3 + var7, this.xm(), this.ym());
            } else if (var4.state == RealmsServer.State.CLOSED) {
               RealmsMainScreen.access$2400(this.this$0, var2 + var6, var3 + var7, this.xm(), this.ym());
            } else if (RealmsMainScreen.access$1400(this.this$0, var4) && var4.daysLeft < 7) {
               this.showStatus(var2 - 14, var3, var4);
               RealmsMainScreen.access$2500(this.this$0, var2 + var6, var3 + var7, this.xm(), this.ym(), var4.daysLeft);
            } else if (var4.state == RealmsServer.State.OPEN) {
               RealmsMainScreen.access$2600(this.this$0, var2 + var6, var3 + var7, this.xm(), this.ym());
               this.showStatus(var2 - 14, var3, var4);
            } else if (var4.state == RealmsServer.State.ADMIN_LOCK) {
               RealmsMainScreen.access$2700(this.this$0, var2 + var6, var3 + var7, this.xm(), this.ym());
            }

            String var8 = "0";
            if (!var4.serverPing.nrOfPlayers.equals(var8)) {
               String var9 = ChatFormatting.GRAY + "" + var4.serverPing.nrOfPlayers;
               this.this$0.drawString(var9, var2 + 200 - this.this$0.fontWidth(var9), var3 + 1, 8421504);
            }

            if (var4.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
               int var14 = 9206892;
               if (RealmsMainScreen.access$2000(this.this$0) % 10 < 5) {
                  var14 = 13413468;
               }

               String var10 = RealmsScreen.getLocalizedString("mco.selectServer.minigame") + " ";
               int var11 = this.this$0.fontWidth(var10);
               this.this$0.drawString(var10, var2 + 2, var3 + 12, var14);
               this.this$0.drawString(var4.getMotd(), var2 + 2 + var11, var3 + 12, 7105644);
            } else {
               this.this$0.drawString(var4.getMotd(), var2 + 2, var3 + 12, 7105644);
            }

            this.this$0.drawString(var4.owner, var2 + 2, var3 + 12 + 11, 5000268);
            RealmsScreen.bindFace(var4.ownerUUID, var4.owner);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RealmsScreen.blit(var2 - 36, var3, 8.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
            RealmsScreen.blit(var2 - 36, var3, 40.0F, 8.0F, 8, 8, 32, 32, 64.0F, 64.0F);
         }
      }

      private void showStatus(int var1, int var2, RealmsServer var3) {
         if (var3.ip != null) {
            if (var3.status != null) {
               this.this$0.drawString(var3.status, var1 + 215 - this.this$0.fontWidth(var3.status), var2 + 1, 8421504);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            RealmsScreen.bind("textures/gui/icons.png");
         }
      }
   }
}
