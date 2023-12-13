package com.mojang.realmsclient.gui.screens;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.PendingInvite;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.List;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.realms.RealmsScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class PendingInvitationScreen extends RealmsScreen {
   private static final Logger LOGGER = LogManager.getLogger();
   private static final int BACK_BUTTON_ID = 0;
   private static final int ACCEPT_BUTTON_ID = 1;
   private static final int REJECT_BUTTON_ID = 2;
   private final RealmsScreen onlineScreenLastScreen;
   private PendingInvitationScreen.PendingInvitationList pendingList;
   private List pendingInvites = Lists.newArrayList();
   private int selectedItem = -1;

   public PendingInvitationScreen(RealmsScreen var1) {
      this.onlineScreenLastScreen = var1;
   }

   public void mouseEvent() {
      super.mouseEvent();
      this.pendingList.mouseEvent();
   }

   public void init() {
      Keyboard.enableRepeatEvents(true);
      this.buttonsClear();
      this.pendingList = new PendingInvitationScreen.PendingInvitationList(this);
      (new Thread(this, "Realms-pending-invitations-fetcher") {
         final PendingInvitationScreen this$0;

         {
            this.this$0 = var1;
         }

         public void run() {
            RealmsClient var1 = RealmsClient.createRealmsClient();

            try {
               PendingInvitationScreen.access$002(this.this$0, var1.pendingInvites().pendingInvites);
            } catch (RealmsServiceException var3) {
               PendingInvitationScreen.access$100().error("Couldn't list invites");
            }

         }
      }).start();
      this.postInit();
   }

   private void postInit() {
      this.buttonsAdd(newButton(1, this.width() / 2 - 154, this.height() - 52, 153, 20, getLocalizedString("mco.invites.button.accept")));
      this.buttonsAdd(newButton(2, this.width() / 2 + 6, this.height() - 52, 153, 20, getLocalizedString("mco.invites.button.reject")));
      this.buttonsAdd(newButton(0, this.width() / 2 - 75, this.height() - 28, 153, 20, getLocalizedString("gui.back")));
   }

   public void tick() {
      super.tick();
   }

   public void buttonClicked(RealmsButton var1) {
      if (var1.active()) {
         if (var1.id() == 1) {
            this.accept();
         } else if (var1.id() == 0) {
            Realms.setScreen(new RealmsMainScreen(this.onlineScreenLastScreen));
         } else if (var1.id() == 2) {
            this.reject();
         }

      }
   }

   public void keyPressed(char var1, int var2) {
      if (var2 == 1) {
         Realms.setScreen(this.onlineScreenLastScreen);
      }

   }

   private void reject() {
      if (this.selectedItem >= 0 && this.selectedItem < this.pendingInvites.size()) {
         (new Thread(this, "Realms-reject-invitation") {
            final PendingInvitationScreen this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               try {
                  RealmsClient var1 = RealmsClient.createRealmsClient();
                  var1.rejectInvitation(((PendingInvite)PendingInvitationScreen.access$000(this.this$0).get(PendingInvitationScreen.access$200(this.this$0))).invitationId);
                  PendingInvitationScreen.access$300(this.this$0);
               } catch (RealmsServiceException var2) {
                  PendingInvitationScreen.access$100().error("Couldn't reject invite");
               }

            }
         }).start();
      }

   }

   private void accept() {
      if (this.selectedItem >= 0 && this.selectedItem < this.pendingInvites.size()) {
         (new Thread(this, "Realms-accept-invitation") {
            final PendingInvitationScreen this$0;

            {
               this.this$0 = var1;
            }

            public void run() {
               try {
                  RealmsClient var1 = RealmsClient.createRealmsClient();
                  var1.acceptInvitation(((PendingInvite)PendingInvitationScreen.access$000(this.this$0).get(PendingInvitationScreen.access$200(this.this$0))).invitationId);
                  PendingInvitationScreen.access$300(this.this$0);
               } catch (RealmsServiceException var2) {
                  PendingInvitationScreen.access$100().error("Couldn't accept invite");
               }

            }
         }).start();
      }

   }

   private void updateSelectedItemPointer() {
      int var1 = this.selectedItem;
      if (this.pendingInvites.size() - 1 == this.selectedItem) {
         --this.selectedItem;
      }

      this.pendingInvites.remove(var1);
      if (this.pendingInvites.size() == 0) {
         this.selectedItem = -1;
         Realms.setScreen(new RealmsMainScreen(this.onlineScreenLastScreen));
      }

   }

   public void render(int var1, int var2, float var3) {
      this.renderBackground();
      this.pendingList.render(var1, var2, var3);
      this.drawCenteredString(getLocalizedString("mco.invites.title"), this.width() / 2, 20, 16777215);
      super.render(var1, var2, var3);
   }

   static List access$002(PendingInvitationScreen var0, List var1) {
      return var0.pendingInvites = var1;
   }

   static Logger access$100() {
      return LOGGER;
   }

   static int access$200(PendingInvitationScreen var0) {
      return var0.selectedItem;
   }

   static List access$000(PendingInvitationScreen var0) {
      return var0.pendingInvites;
   }

   static void access$300(PendingInvitationScreen var0) {
      var0.updateSelectedItemPointer();
   }

   static int access$202(PendingInvitationScreen var0, int var1) {
      return var0.selectedItem = var1;
   }

   private class PendingInvitationList extends RealmsScrolledSelectionList {
      final PendingInvitationScreen this$0;

      public PendingInvitationList(PendingInvitationScreen var1) {
         super(var1.width(), var1.height(), 32, var1.height() - 64, 36);
         this.this$0 = var1;
      }

      public int getItemCount() {
         return PendingInvitationScreen.access$000(this.this$0).size() + 1;
      }

      public void selectItem(int var1, boolean var2, int var3, int var4) {
         if (var1 < PendingInvitationScreen.access$000(this.this$0).size()) {
            PendingInvitationScreen.access$202(this.this$0, var1);
         }
      }

      public boolean isSelectedItem(int var1) {
         return var1 == PendingInvitationScreen.access$200(this.this$0);
      }

      public int getMaxPosition() {
         return this.getItemCount() * 36;
      }

      public void renderBackground() {
         this.this$0.renderBackground();
      }

      public void renderItem(int var1, int var2, int var3, int var4, int var5, int var6) {
         if (var1 < PendingInvitationScreen.access$000(this.this$0).size()) {
            this.renderPendingInvitationItem(var1, var2, var3, var4);
         }

      }

      private void renderPendingInvitationItem(int var1, int var2, int var3, int var4) {
         PendingInvite var5 = (PendingInvite)PendingInvitationScreen.access$000(this.this$0).get(var1);
         this.this$0.drawString(var5.worldName, var2 + 2, var3 + 1, 16777215);
         this.this$0.drawString(var5.worldOwnerName, var2 + 2, var3 + 12, 7105644);
      }
   }
}
