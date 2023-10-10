package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PlayerInfo;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.RealmsConfirmResultListener;
import java.io.IOException;
import java.util.Set;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.Tezzelator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class ConfigureWorldScreen extends RealmsScreen implements RealmsConfirmResultListener {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final String TOGGLE_ON_ICON_LOCATION = "realms:textures/gui/realms/toggle_on_icon.png";
   private static final String TOGGLE_OFF_ICON_LOCATION = "realms:textures/gui/realms/toggle_off_icon.png";
   private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
   private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
   private static final String OP_ICON_LOCATION = "realms:textures/gui/realms/op_icon.png";
   private static final String USER_ICON_LOCATION = "realms:textures/gui/realms/user_icon.png";
   private static final String CROSS_ICON_LOCATION = "realms:textures/gui/realms/cross_icon.png";
   private String toolTip;
   private final RealmsScreen lastScreen;
   private RealmsServer serverData;
   private volatile long serverId;
   private ConfigureWorldScreen.InvitedSelectionList invitedSelectionList;
   private int column1_x;
   private int column_width;
   private int column2_x;
   private static final int BUTTON_OPEN_ID = 0;
   private static final int BUTTON_CLOSE_ID = 1;
   private static final int BUTTON_UNINVITE_ID = 3;
   private static final int BUTTON_EDIT_ID = 5;
   private static final int BUTTON_SUBSCRIPTION_ID = 7;
   private static final int BUTTON_MINIGAME_ID = 8;
   private static final int BUTTON_BACK_ID = 10;
   private static final int BUTTON_ACTIVITY_ID = 11;
   private static final int BUTTON_EDIT_WORLD_ID = 12;
   private static final int BUTTON_INVITE_ID = 13;
   private int selectedInvitedIndex = -1;
   private String selectedInvited;
   private RealmsButton editButton;
   private RealmsButton minigameButton;
   private RealmsButton subscriptionButton;
   private RealmsButton editWorldButton;
   private RealmsButton activityButton;
   private RealmsButton inviteButton;
   private boolean stateChanged;
   private boolean openButtonHovered = false;
   private boolean closeButtonHovered = false;
   private volatile Set ops;

   public ConfigureWorldScreen(RealmsScreen var1, long var2) {
      this.lastScreen = var1;
      this.serverId = var2;
   }

   public void mouseEvent() {
      super.mouseEvent();
      if (this.invitedSelectionList != null) {
         this.invitedSelectionList.mouseEvent();
      }

   }

   public void tick() {
      super.tick();
   }

   public void init() {
      this.getOwnWorld(this.serverId);
      this.column1_x = this.width() / 2 - 160;
      this.column_width = 150;
      this.column2_x = this.width() / 2 + 12;
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.buttonsAdd(this.editButton = newButton(5, this.column2_x, this.row(6), this.column_width, 20, getLocalizedString("mco.configure.world.buttons.edit")));
      this.buttonsAdd(this.editWorldButton = newButton(12, this.column2_x, this.row(4), this.column_width, 20, getLocalizedString("mco.configure.world.buttons.editworld")));
      this.buttonsAdd(this.minigameButton = newButton(8, this.column2_x, this.row(2), this.column_width, 20, getLocalizedString("mco.configure.world.buttons.startMiniGame")));
      this.buttonsAdd(this.subscriptionButton = newButton(7, this.column2_x, this.row(8), this.column_width, 20, getLocalizedString("mco.configure.world.buttons.subscription")));
      this.buttonsAdd(this.activityButton = newButton(11, this.column1_x, this.row(12), this.column_width + 10, 20, getLocalizedString("mco.configure.world.buttons.activity")));
      this.buttonsAdd(this.inviteButton = newButton(13, this.column1_x, this.row(10), this.column_width + 10, 20, "+"));
      this.buttonsAdd(newButton(10, this.column2_x + this.column_width / 2 + 2, this.row(12), this.column_width / 2 - 2, 20, getLocalizedString("gui.back")));
      this.invitedSelectionList = new ConfigureWorldScreen.InvitedSelectionList(this);
      this.invitedSelectionList.setLeftPos(this.column1_x);
      this.editButton.active(false);
      this.editWorldButton.active(false);
      this.minigameButton.active(false);
      this.subscriptionButton.active(false);
      this.activityButton.active(false);
      this.inviteButton.active(false);
   }

   private void fetchOps() {
      (new Thread(this) {
         final ConfigureWorldScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               ConfigureWorldScreen.access$002(this.this$0, var1.getOpsFor(ConfigureWorldScreen.access$100(this.this$0)).ops);
            } catch (RealmsServiceException var3) {
               ConfigureWorldScreen.access$200().error("Couldn't fetch ops");
            } catch (Exception var4) {
               ConfigureWorldScreen.access$200().error("Couldn't parse response of fetching ops");
            }

         }
      }).start();
   }

   private int row(int var1) {
      return 40 + var1 * 13;
   }

   public void removed() {
      Keyboard.enableRepeatEvents(false);
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 10) {
            this.backButtonClicked();
         } else if (var1.id() == 5) {
            Realms.setScreen(new EditRealmsWorldScreen(this, this.lastScreen, this.serverData.clone()));
         } else if (var1.id() == 1) {
            String var2 = getLocalizedString("mco.configure.world.close.question.line1");
            String var3 = getLocalizedString("mco.configure.world.close.question.line2");
            Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var2, var3, 1));
         } else if (var1.id() == 0) {
            this.openTheWorld();
         } else if (var1.id() == 8) {
            if (this.serverData.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
               Realms.setScreen(new ModifyMinigameWorldScreen(this, this.serverData));
            } else {
               Realms.setScreen(new StartMinigameWorldScreen(this, this.serverData));
            }
         } else if (var1.id() == 7) {
            Realms.setScreen(new SubscriptionScreen(this, this.serverData));
         } else if (var1.id() == 11) {
            Realms.setScreen(new ActivityScreen(this, this.serverData.id));
         } else if (var1.id() == 12) {
            Realms.setScreen(new WorldManagementScreen(this, this.lastScreen, this.serverData.clone()));
         } else if (var1.id() == 13) {
            Realms.setScreen(new InviteScreen(this.lastScreen, this, this.serverData));
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         this.backButtonClicked();
      }

   }

   private void backButtonClicked() {
      if (this.stateChanged) {
         ((RealmsMainScreen)this.lastScreen).removeSelection();
      }

      Realms.setScreen(this.lastScreen);
   }

   private void getOwnWorld(long var1) {
      (new Thread(this, var1) {
         final long val$worldId;
         final ConfigureWorldScreen this$0;

         {
            this.this$0 = var1;
            this.val$worldId = var2;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               ConfigureWorldScreen.access$302(this.this$0, var1.getOwnWorld(this.val$worldId));
               ConfigureWorldScreen.access$400(this.this$0);
               ConfigureWorldScreen.access$500(this.this$0).active(!ConfigureWorldScreen.access$300(this.this$0).expired);
               ConfigureWorldScreen.access$600(this.this$0).active(!ConfigureWorldScreen.access$300(this.this$0).expired);
               ConfigureWorldScreen.access$700(this.this$0).active(true);
               ConfigureWorldScreen.access$800(this.this$0).active(true);
               ConfigureWorldScreen.access$900(this.this$0).active(true);
               boolean var2 = ConfigureWorldScreen.access$300(this.this$0).worldType.equals(RealmsServer.WorldType.MINIGAME);
               if (var2) {
                  ConfigureWorldScreen.access$600(this.this$0).msg(RealmsScreen.getLocalizedString("mco.configure.world.buttons.modifyMiniGame"));
                  ConfigureWorldScreen.access$500(this.this$0).active(false);
                  ConfigureWorldScreen.access$900(this.this$0).active(false);
               }
            } catch (RealmsServiceException var3) {
               ConfigureWorldScreen.access$200().error("Couldn't get own world");
            } catch (IOException var4) {
               ConfigureWorldScreen.access$200().error("Couldn't parse response getting own world");
            }

         }
      }).start();
   }

   private void op(int var1) {
      RealmsClient var2 = RealmsClient.createRealmsClient();
      String var3 = ((PlayerInfo)this.serverData.players.get(var1)).getName();

      try {
         var2.op(this.serverData.id, var3);
         this.fetchOps();
      } catch (RealmsServiceException var5) {
         LOGGER.error("Couldn't op the user");
      }

   }

   private void deop(int var1) {
      RealmsClient var2 = RealmsClient.createRealmsClient();
      String var3 = ((PlayerInfo)this.serverData.players.get(var1)).getName();

      try {
         var2.deop(this.serverData.id, var3);
         this.fetchOps();
      } catch (RealmsServiceException var5) {
         LOGGER.error("Couldn't deop the user");
      }

   }

   private void openTheWorld() {
      RealmsClient var1 = RealmsClient.createRealmsClient();

      try {
         Boolean var2 = var1.open(this.serverData.id);
         if (var2) {
            this.stateChanged = true;
            this.serverData.state = RealmsServer.State.OPEN;
            this.init();
         }
      } catch (RealmsServiceException var3) {
         LOGGER.error("Couldn't open world");
      } catch (IOException var4) {
         LOGGER.error("Could not parse response opening world");
      }

   }

   private void closeTheWorld() {
      RealmsClient var1 = RealmsClient.createRealmsClient();

      try {
         boolean var2 = var1.close(this.serverData.id);
         if (var2) {
            this.stateChanged = true;
            this.serverData.state = RealmsServer.State.CLOSED;
            this.init();
         }
      } catch (RealmsServiceException var3) {
         LOGGER.error("Couldn't close world");
      } catch (IOException var4) {
         LOGGER.error("Could not parse response closing world");
      }

   }

   private void uninvite(int var1) {
      if (var1 >= 0 && var1 < this.serverData.players.size()) {
         PlayerInfo var2 = (PlayerInfo)this.serverData.players.get(var1);
         this.selectedInvited = var2.getUuid();
         this.selectedInvitedIndex = var1;
         RealmsConfirmScreen var3 = new RealmsConfirmScreen(this, "Question", getLocalizedString("mco.configure.world.uninvite.question") + " '" + var2.getName() + "'", 3);
         Realms.setScreen(var3);
      }

   }

   public void confirmResult(boolean var1, int var2) {
      if (var2 == 3) {
         if (var1) {
            RealmsClient var3 = RealmsClient.createRealmsClient();

            try {
               var3.uninvite(this.serverData.id, this.selectedInvited);
            } catch (RealmsServiceException var5) {
               LOGGER.error("Couldn't uninvite user");
            }

            this.deleteFromInvitedList(this.selectedInvitedIndex);
         }

         Realms.setScreen(new ConfigureWorldScreen(this.lastScreen, this.serverData.id));
      } else if (var2 == 1) {
         if (var1) {
            this.closeTheWorld();
         }

         Realms.setScreen(this);
      }

   }

   private void deleteFromInvitedList(int var1) {
      this.serverData.players.remove(var1);
   }

   public void mouseClicked(int var1, int var2, int var3) {
      if (this.closeButtonHovered && !this.serverData.expired) {
         this.openTheWorld();
      } else if (this.openButtonHovered && !this.serverData.expired) {
         String var4 = getLocalizedString("mco.configure.world.close.question.line1");
         String var5 = getLocalizedString("mco.configure.world.close.question.line2");
         Realms.setScreen(new LongConfirmationScreen(this, LongConfirmationScreen.Type.Info, var4, var5, 1));
      }

      super.mouseClicked(var1, var2, var3);
   }

   public void render(int var1, int var2, float var3) {
      this.toolTip = null;
      this.closeButtonHovered = false;
      this.openButtonHovered = false;
      this.renderBackground();
      if (this.invitedSelectionList != null) {
         this.invitedSelectionList.render(var1, var2, var3);
      }

      GL11.glDisable(2896);
      GL11.glDisable(2912);
      Tezzelator var4 = Tezzelator.instance;
      bind("textures/gui/options_background.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var5 = 32.0F;
      var4.begin();
      var4.color(4210752);
      var4.vertexUV(0.0D, (double)this.height(), 0.0D, 0.0D, (double)((float)(this.height() - this.row(10)) / var5 + 0.0F));
      var4.vertexUV((double)this.width(), (double)this.height(), 0.0D, (double)((float)this.width() / var5), (double)((float)(this.height() - this.row(10)) / var5 + 0.0F));
      var4.vertexUV((double)this.width(), (double)this.row(10), 0.0D, (double)((float)this.width() / var5), 0.0D);
      var4.vertexUV(0.0D, (double)this.row(10), 0.0D, 0.0D, 0.0D);
      var4.end();
      this.drawCenteredString(getLocalizedString("mco.configure.world.title"), this.width() / 2, 17, 16777215);
      if (this.serverData != null && this.serverData.players != null) {
         this.drawString(getLocalizedString("mco.configure.world.invited") + " (" + this.serverData.players.size() + "/20)", this.column1_x, this.row(1), 10526880);
         this.inviteButton.active(this.serverData.players.size() < 20);
      } else {
         this.drawString(getLocalizedString("mco.configure.world.invited"), this.column1_x, this.row(1), 10526880);
         this.inviteButton.active(false);
      }

      super.render(var1, var2, var3);
      if (this.serverData != null) {
         String var6 = this.serverData.getName();
         int var7 = this.fontWidth(var6);
         if (this.serverData.state == RealmsServer.State.OPEN) {
            this.drawCenteredString(var6, this.width() / 2, 30, 8388479);
         } else if (this.serverData.state == RealmsServer.State.CLOSED) {
            this.drawCenteredString(var6, this.width() / 2, 30, 13421772);
         } else {
            this.drawCenteredString(var6, this.width() / 2, 30, 8388479);
         }

         int var8 = this.width() / 2 - var7 / 2 - 13;
         this.drawServerStatus(var8, 30, var1, var2);
         if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, var1, var2);
         }

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

   private void drawServerStatus(int var1, int var2, int var3, int var4) {
      if (this.serverData.expired) {
         this.drawExpired(var1, var2, var3, var4);
      } else if (this.serverData.state == RealmsServer.State.CLOSED) {
         this.drawClose(var1, var2, var3, var4);
      } else if (this.serverData.state == RealmsServer.State.OPEN) {
         this.drawOpen(var1, var2, var3, var4);
      } else if (this.serverData.state == RealmsServer.State.ADMIN_LOCK) {
         this.drawLocked(var1, var2, var3, var4);
      }

   }

   private void drawRemoveIcon(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/cross_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      RealmsScreen.blit(var1, var2, 0.0F, 0.0F, 8, 7, 8.0F, 7.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.toolTip = getLocalizedString("mco.configure.world.invites.remove.tooltip");
      }

   }

   private void drawOpped(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/op_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      RealmsScreen.blit(var1, var2, 0.0F, 0.0F, 8, 8, 8.0F, 8.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.toolTip = getLocalizedString("mco.configure.world.invites.ops.tooltip");
      }

   }

   private void drawNormal(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/user_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      RealmsScreen.blit(var1, var2, 0.0F, 0.0F, 8, 8, 8.0F, 8.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.toolTip = getLocalizedString("mco.configure.world.invites.normal.tooltip");
      }

   }

   private void drawExpired(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/expired_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.toolTip = getLocalizedString("mco.selectServer.expired");
      }

   }

   private void drawOpen(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/toggle_on_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      int var5 = var1 - 12;
      RealmsScreen.blit(var5 * 2, var2 * 2, 0.0F, 0.0F, 32, 16, 32.0F, 16.0F);
      GL11.glPopMatrix();
      if (var3 >= var5 && var3 <= var5 + 16 && var4 >= var2 && var4 <= var2 + 8) {
         this.toolTip = getLocalizedString("mco.selectServer.closeserver");
         this.openButtonHovered = true;
      }

   }

   private void drawClose(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/toggle_off_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      int var5 = var1 - 12;
      RealmsScreen.blit(var5 * 2, var2 * 2, 0.0F, 0.0F, 32, 16, 32.0F, 16.0F);
      GL11.glPopMatrix();
      if (var3 >= var5 && var3 <= var5 + 16 && var4 >= var2 && var4 <= var2 + 8) {
         this.toolTip = getLocalizedString("mco.selectServer.openserver");
         this.closeButtonHovered = true;
      }

   }

   private void drawLocked(int var1, int var2, int var3, int var4) {
      bind("realms:textures/gui/realms/off_icon.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPushMatrix();
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      RealmsScreen.blit(var1 * 2, var2 * 2, 0.0F, 0.0F, 15, 15, 15.0F, 15.0F);
      GL11.glPopMatrix();
      if (var3 >= var1 && var3 <= var1 + 9 && var4 >= var2 && var4 <= var2 + 9) {
         this.toolTip = getLocalizedString("mco.selectServer.locked");
      }

   }

   static Set access$002(ConfigureWorldScreen var0, Set var1) {
      return var0.ops = var1;
   }

   static long access$100(ConfigureWorldScreen var0) {
      return var0.serverId;
   }

   static Logger access$200() {
      return LOGGER;
   }

   static RealmsServer access$302(ConfigureWorldScreen var0, RealmsServer var1) {
      return var0.serverData = var1;
   }

   static void access$400(ConfigureWorldScreen var0) {
      var0.fetchOps();
   }

   static RealmsServer access$300(ConfigureWorldScreen var0) {
      return var0.serverData;
   }

   static RealmsButton access$500(ConfigureWorldScreen var0) {
      return var0.editButton;
   }

   static RealmsButton access$600(ConfigureWorldScreen var0) {
      return var0.minigameButton;
   }

   static RealmsButton access$700(ConfigureWorldScreen var0) {
      return var0.subscriptionButton;
   }

   static RealmsButton access$800(ConfigureWorldScreen var0) {
      return var0.activityButton;
   }

   static RealmsButton access$900(ConfigureWorldScreen var0) {
      return var0.editWorldButton;
   }

   static int access$1000(ConfigureWorldScreen var0) {
      return var0.column_width;
   }

   static int access$1100(ConfigureWorldScreen var0, int var1) {
      return var0.row(var1);
   }

   static int access$1200(ConfigureWorldScreen var0) {
      return var0.column1_x;
   }

   static Set access$000(ConfigureWorldScreen var0) {
      return var0.ops;
   }

   static void access$1300(ConfigureWorldScreen var0, int var1) {
      var0.deop(var1);
   }

   static void access$1400(ConfigureWorldScreen var0, int var1) {
      var0.op(var1);
   }

   static void access$1500(ConfigureWorldScreen var0, int var1) {
      var0.uninvite(var1);
   }

   static void access$1600(ConfigureWorldScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawOpped(var1, var2, var3, var4);
   }

   static void access$1700(ConfigureWorldScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawNormal(var1, var2, var3, var4);
   }

   static void access$1800(ConfigureWorldScreen var0, int var1, int var2, int var3, int var4) {
      var0.drawRemoveIcon(var1, var2, var3, var4);
   }

   private class InvitedSelectionList extends RealmsClickableScrolledSelectionList {
      final ConfigureWorldScreen this$0;

      public InvitedSelectionList(ConfigureWorldScreen var1) {
         super(ConfigureWorldScreen.access$1000(var1) + 10, ConfigureWorldScreen.access$1100(var1, 10), ConfigureWorldScreen.access$1100(var1, 2), ConfigureWorldScreen.access$1100(var1, 10), 13);
         this.this$0 = var1;
      }

      public void customMouseEvent(int var1, int var2, int var3, float var4, int var5) {
         if (Mouse.isButtonDown(0) && this.ym() >= var1 && this.ym() <= var2) {
            int var6 = ConfigureWorldScreen.access$1200(this.this$0);
            int var7 = ConfigureWorldScreen.access$1200(this.this$0) + ConfigureWorldScreen.access$1000(this.this$0);
            int var8 = this.ym() - var1 - var3 + (int)var4 - 4;
            int var9 = var8 / var5;
            if (this.xm() >= var6 && this.xm() <= var7 && var9 >= 0 && var8 >= 0 && var9 < this.getItemCount()) {
               this.itemClicked(var8, var9, this.xm(), this.ym(), this.width());
            }
         }

      }

      public void itemClicked(int var1, int var2, int var3, int var4, int var5) {
         int var6 = ConfigureWorldScreen.access$1200(this.this$0) + ConfigureWorldScreen.access$1000(this.this$0) - 22;
         int var7 = var1 + 70 - this.getScroll();
         int var8 = var6 + 10;
         int var9 = var7 - 3;
         System.out.println("xm: " + var3 + " ym: " + var4);
         System.out.println("removeX: " + var6 + " removey: " + var7);
         if (var3 >= var8 && var3 <= var8 + 9 && var4 >= var9 && var4 <= var9 + 9) {
            if (var2 >= 0 && var2 < ConfigureWorldScreen.access$300(this.this$0).players.size()) {
               String var10 = ((PlayerInfo)ConfigureWorldScreen.access$300(this.this$0).players.get(var2)).getName();
               if (ConfigureWorldScreen.access$000(this.this$0) != null) {
                  if (ConfigureWorldScreen.access$000(this.this$0).contains(var10)) {
                     ConfigureWorldScreen.access$1300(this.this$0, var2);
                  } else {
                     ConfigureWorldScreen.access$1400(this.this$0, var2);
                  }
               }
            }
         } else if (var3 >= var6 && var3 <= var6 + 9 && var4 >= var7 && var4 <= var7 + 9 && var2 >= 0 && var2 < ConfigureWorldScreen.access$300(this.this$0).players.size()) {
            ConfigureWorldScreen.access$1500(this.this$0, var2);
         }

      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      public int getScrollbarPosition() {
         return ConfigureWorldScreen.access$1200(this.this$0) + this.width() - 5;
      }

      public int getItemCount() {
         return ConfigureWorldScreen.access$300(this.this$0) == null ? 1 : ConfigureWorldScreen.access$300(this.this$0).players.size();
      }

      public int getMaxPosition() {
         return this.getItemCount() * 13;
      }

      protected void renderItem(int var1, int var2, int var3, int var4, Tezzelator var5, int var6, int var7) {
         if (ConfigureWorldScreen.access$300(this.this$0) != null) {
            if (var1 < ConfigureWorldScreen.access$300(this.this$0).players.size()) {
               this.renderInvitedItem(var1, var2, var3, var4);
            }

         }
      }

      private void renderInvitedItem(int var1, int var2, int var3, int var4) {
         PlayerInfo var5 = (PlayerInfo)ConfigureWorldScreen.access$300(this.this$0).players.get(var1);
         this.this$0.drawString(var5.getName(), ConfigureWorldScreen.access$1200(this.this$0) + 3 + 12, var3 + 1, 16777215);
         if (ConfigureWorldScreen.access$000(this.this$0) != null && ConfigureWorldScreen.access$000(this.this$0).contains(var5.getName())) {
            ConfigureWorldScreen.access$1600(this.this$0, ConfigureWorldScreen.access$1200(this.this$0) + ConfigureWorldScreen.access$1000(this.this$0) - 10, var3 + 1, this.xm(), this.ym());
         } else {
            ConfigureWorldScreen.access$1700(this.this$0, ConfigureWorldScreen.access$1200(this.this$0) + ConfigureWorldScreen.access$1000(this.this$0) - 10, var3 + 1, this.xm(), this.ym());
         }

         ConfigureWorldScreen.access$1800(this.this$0, ConfigureWorldScreen.access$1200(this.this$0) + ConfigureWorldScreen.access$1000(this.this$0) - 22, var3 + 2, this.xm(), this.ym());
         RealmsScreen.bindFace(var5.getUuid(), var5.getName());
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         RealmsScreen.blit(ConfigureWorldScreen.access$1200(this.this$0) + 2 + 2, var3 + 1, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
         RealmsScreen.blit(ConfigureWorldScreen.access$1200(this.this$0) + 2 + 2, var3 + 1, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
      }
   }
}
