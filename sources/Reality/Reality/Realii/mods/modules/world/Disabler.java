package Reality.Realii.mods.modules.world;
//C0BPacketEntityAction

import net.minecraft.network.play.client.C00PacketKeepAlive;
import Reality.Realii.mods.modules.movement.Fly;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.*;
import net.minecraft.util.MathHelper;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.network.PacketBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import org.apache.commons.lang3.RandomUtils;

import Moses.server.packets12.PacketUtil;
import net.minecraft.util.Vec3;
//import Reality.Realii.util.pathfinding.MainPathFinder;
//import Reality.Realii.pathfinding.Vec3;
import net.minecraft.world.World;
import Reality.Realii.Client;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPacketRecieve;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Numbers;
import Reality.Realii.event.value.Option;
import Reality.Realii.guis.notification.Notification;
import Reality.Realii.guis.notification.NotificationsManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.mods.modules.combat.Killaura;
import Reality.Realii.mods.modules.movement.LagBackChecker;
import Reality.Realii.mods.modules.movement.Speed;
import Reality.Realii.mods.modules.render.ArrayList2;
import Reality.Realii.utils.cheats.player.Helper;
import Reality.Realii.utils.cheats.player.PlayerUtils;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventMove;
import Reality.Realii.event.events.world.EventPreUpdate;
import Reality.Realii.event.value.Mode;
import Reality.Realii.managers.ModuleManager;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import Reality.Realii.utils.math.MathUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import libraries.optifine.MathUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.server.MinecraftServer;
import Reality.Realii.event.EventHandler;
import Reality.Realii.event.events.world.EventPacketSend;
import Reality.Realii.mods.Module;
import Reality.Realii.mods.ModuleType;
import Reality.Realii.utils.cheats.world.TimerUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;


public class Disabler extends Module{
	private Mode mode = new Mode("Mode", "Mode", new String[]{"BlocksMcLessFlags","VulcanMove","Sentinel2","SentinelNew","Polar","OldAgc","VulcanScaffold","PikaKit","MineLand","koksTp","Watchdog","VerusCombat","Sentinel","NcpTp","Sparky","Grim2","Vulcan","Taco","GrimCombat","Hac","Burito","WatchFrog","Respawn","BMC","Morgan2","Morgan","MushMc","NoRule","MmcVelo","VoidTp","OmniSprint","Lunar","VulcanCombat", "VerusCevata","PingSpoof","VerusFull"}, "BlocksMcLessFlags");
	public static Numbers<Number>  Ping = new Numbers<>("PingSpoofAmount", 100, 100, 10089, 823);
	  private List<Packet> packetList = new CopyOnWriteArrayList<Packet>();
	    private TimerUtil timer = new TimerUtil();
		public boolean Ducagay = false;
	    private double LastKniowPosX;
	    private double LastKniowPosY;
	    private double LastKniowPosZ;
	    private boolean lagback;
	    //make option to chose if you want keepsrint disabller or not
	    private int counter;
	    private boolean teleported;

	public Disabler(){
		super("Disabler", ModuleType.World);
		  this.addValues(this.mode, Ping);

	}


    @Override
    public void onEnable() {
        super.onEnable();
		Ducagay = false;
		timer.reset();
        if (this.mode.getValue().equals("VerusFull")) {


        	Helper.sendMessage("Disabler");
        	 mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
             mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        }

    }

    @Override
    public void onDisable() {
        super.onDisable();
		Ducagay = false;
		timer.reset();
        	 LastKniowPosX = mc.thePlayer.posX;
             LastKniowPosY = mc.thePlayer.posY;
             LastKniowPosZ = mc.thePlayer.posZ;


    }

    @EventHandler
    public void onMove(EventMove e) {
    	if (this.mode.getValue().equals("Morgan2")) {

    		//  if (mc.thePlayer.ticksExisted % 5 == 0) {
    		    //    mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));

    		  //  }
    	}
    	if (this.mode.getValue().equals("Vulcan")) {



     // if (mc.thePlayer.isSwingInProgress && Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled()) {
    //	  if (mc.thePlayer.ticksExisted % 1 == 0) {
    //	  mc.gameSettings.keyBindSprint.pressed = false;
    //	  this.mc.thePlayer.setSprinting(false);
    //	  }

    	 // if (mc.thePlayer.ticksExisted % 2 == 0) {
        	 // mc.gameSettings.keyBindSprint.pressed = true;
        	//  this.mc.thePlayer.setSprinting(true);
        	//  }
    		 // }

  		  if (mc.thePlayer.ticksExisted % 5 == 0) {
  		        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));

  		    }

  		//   mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(mc.thePlayer), EnumFacing.UP));

  		//if (mc.thePlayer.ticksExisted % 7 == 0) {
		//       mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
		//
		//    }


  		    }
    }

	private static boolean selector;

	public static boolean selector() {
		return selector;
	}
    @EventHandler
    public void onWorldChange(EventPacketRecieve event) {

    	if (this.mode.getValue().equals("BMC")) {
        if (event.getPacket() instanceof S07PacketRespawn || event.getPacket() instanceof S01PacketJoinGame) {
        	  NotificationsManager.addNotification(new Notification("Waring! Using Speed before the game starts might result in a ban!", Notification.Type.Alert,5));
        }
        }
    }

    @EventHandler
    private void onPacketSend(EventPacketSend e) {
		if (this.mode.getValue().equals("Polar")) {
			if (e.getPacket() instanceof S05PacketSpawnPosition) {
				e.setCancelled(true);
			}
		}

		if (this.mode.getValue().equals("VulcanMove")) {
			if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
				e.setCancelled(true);
			}
		}
		if (this.mode.getValue().equals("Sentinel2")) {
			if (e.getPacket() instanceof C03PacketPlayer && !selector() && mc.currentScreen == null) {
				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).down(5),
						EnumFacing.UP.getIndex(), null, 0.0F, 1.0F, 0.0F));
				//BadPackets.reset();
			} else if (e.getPacket() instanceof C00PacketKeepAlive) {
				e.setCancelled(true);
			}



		}
    	if (this.mode.getValue().equals("VulcanScaffold")) {
    	//	if(Client.instance.getModuleManager().getModuleByClass(Scaffold.class).isEnabled()){
    			//mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
    		 if (e.getPacket() instanceof C0BPacketEntityAction) {

  			   e.setCancelled(true);
  		   }
    	//	}

    	}
    	if (this.mode.getValue().equals("MineLand")) {
    		if(e.getPacket() instanceof C0FPacketConfirmTransaction ) {
    			e.setCancelled(true);

    		}


    	}

    	if (this.mode.getValue().equals("OldAgc")) {
    		if(e.getPacket() instanceof C0FPacketConfirmTransaction ) {
    			e.setCancelled(true);

    		}

    		 if (e.getPacket() instanceof C0BPacketEntityAction) {

    	   		   final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
    	   		      if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
    	   		    	    if (mc.thePlayer.serverSprintState) {
    	   		    	    	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
    	                           mc.thePlayer.serverSprintState = false;
    	                       }
    	   		    	    e.setCancelled(true);
    	   		      }
    	   		       if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
    	                      e.setCancelled(true);
    	   		       }

    	   		 }


    	}
    	if (this.mode.getValue().equals("koksTp")) {

           //     e.setCancelled(true);




    		// mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX - 5, mc.thePlayer.posY + 9, mc.thePlayer.posZ + 12, false));

	        //}
    	}

    	if (this.mode.getValue().equals("VerusCombat")) {
    		 if (e.getPacket() instanceof C0BPacketEntityAction) {

    			   e.setCancelled(true);
    		   }
    	}
		if (this.mode.getValue().equals("SentinelNew")) {
			if (e.getPacket() instanceof C0BPacketEntityAction) {
				e.setCancelled(true);

			}

		}
     	if (this.mode.getValue().equals("Sentinel")) {

			if (e.getPacket() instanceof C0BPacketEntityAction) {

				final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
				if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {

					e.setCancelled(true);
				}


			}

     		//mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(null);
     		// mc.thePlayer.sendQueue.addToSendQueueWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

     //	if(Killaura.target !=null) {
			//e.getPacket() instanceof C13PacketPlayerAbilities || e.getPacket() instanceof C0FPacketConfirmTransaction ||
			//if (e.getPacket() instanceof C0BPacketEntityAction) {

		//		e.setCancelled(true);
		//	}
		//}


     	}
    	if (this.mode.getValue().equals("Grim")) {
    		if(e.getPacket() instanceof C0APacketAnimation || e.getPacket() instanceof C0CPacketInput ) {
    			e.setCancelled(true);
    			//t && e.getPacket() instanceof S32PacketConfirmTransaction
    		}

    	}
    	if (this.mode.getValue().equals("Hac")) {
    		if (e.getPacket() instanceof S08PacketPlayerPosLook && lagback) {

                S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
                e.setCancelled(true);;

                lagback = false;
                mc.thePlayer.sendQueue.addToSendQueue(new C06PacketPlayerPosLook(
                        packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
            }
    	}

    	if (this.mode.getValue().equals("NcpTp")) {
    		if(mc.gameSettings.keyBindJump.isPressed()) {
    		  mc.thePlayer.setPosition(LastKniowPosX, LastKniowPosY, LastKniowPosZ);
    		// mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.0001, mc.thePlayer.posZ, false));
            // mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
           //  mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    		}

    		   if (e.getPacket() instanceof C0BPacketEntityAction || e.getPacket() instanceof C03PacketPlayer) {
    			  e.setCancelled(true);

    		  }
    	}
    	if (this.mode.getValue().equals("Taco")) {

    		   if (e.getPacket() instanceof C0APacketAnimation || e.getPacket() instanceof C0CPacketInput && e.getPacket() instanceof S32PacketConfirmTransaction) {
    			   //if (mc.thePlayer.ticksExisted % 70 == 0) {
    	 			 //  Helper.sendMessage("I Like taco bell!");
    	 			//	}
    			   e.setCancelled(true);
    		   }
    		//C08
    	}



    	if (this.mode.getValue().equals("WatchFrog")) {

    		 if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                 e.setCancelled(true);

   	       }

    		// mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));









    	}
    	if (this.mode.getValue().equals("Respawn")) {




    	}
    	if (this.mode.getValue().equals("BMC")) {

    		// if (e.getPacket() instanceof C0BPacketEntityAction) {

      		 //  final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
      		    //  if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
      		    	 //   if (mc.thePlayer.serverSprintState) {
      		    	  //  	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                       //       mc.thePlayer.serverSprintState = false;
                     //     }
      		    //	    e.setCancelled(true);
      		  //    }
      		    //   if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                //         e.setCancelled(true);
      		    //   }

      	// }




    	}

    	if (this.mode.getValue().equals("Morgan2")) {
    		 //if (e.getPacket() instanceof C0FPacketConfirmTransaction ||e.getPacket() instanceof C00PacketKeepAlive )  {
 	          //  e.setCancelled(true);
     		 // }
    		  if (mc.thePlayer == null || mc.theWorld == null) return;
  	        if (e.getPacket() instanceof C0BPacketEntityAction)
  	            e.setCancelled(true);
    	}
    	if (this.mode.getValue().equals("Morgan")) {
    		if (mc.thePlayer.ticksExisted % 2 == 0) {
    		 if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                 e.setCancelled(true);
                 Helper.sendMessage("Hahahahha No More ac lol");
                 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
             }
    		}


    		  if (mc.thePlayer == null || mc.theWorld == null) return;
    	        if (e.getPacket() instanceof C0BPacketEntityAction)
    	            e.setCancelled(true);


    	    	//mc.timer.timerSpeed = 0.7f;
    	    	//mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("40413eb", new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{8, 52, 48, 51, 101, 98, 49}))));
   	        if(e.getPacket() instanceof S08PacketPlayerPosLook) {
    	            final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();

   	            if(mc.thePlayer.getDistance(packet.getX(), packet.getY(), packet.getZ()) > 8) {
   	                return;
    	            }
    	            e.setCancelled(true);

    	            mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
    	            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
    	            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
    	           // mc.getNetHandler().getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.ACCEPTED));
    	            mc.getNetHandler().getNetworkManager().sendPacket(new C19PacketResourcePackStatus("", C19PacketResourcePackStatus.Action.ACCEPTED));
    	            mc.getNetHandler().getNetworkManager().sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));

    	        }
    	    }








    	//mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("40413eb1", new PacketBuffer(Unpooled.wrappedBuffer(new byte[]{52, 48, 51, 101, 98, 49}))));
    	//byte[] payloadData = new byte[]{52, 48, 51, 101, 98, 49}; // The data to send in the payload
    	//C17PacketCustomPayload packet123 = new C17PacketCustomPayload("40413eb1", new PacketBuffer(Unpooled.wrappedBuffer(payloadData)));
    	//mc.getNetHandler().addToSendQueue(packet123);
    	if (this.mode.getValue().equals("NoRule")) {


    	if(e.getPacket() instanceof C06PacketPlayerPosLook) {
    			  e.setCancelled(true);

    		 }


    		if(e.getPacket() instanceof S08PacketPlayerPosLook && mc.getNetHandler().doneLoadingTerrain) {
    			  final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) e.getPacket();
                  double dist = mc.thePlayer.getDistance(s08.getX(), s08.getY(), s08.getZ());
                  if(dist > 8) {
                	  return;
                  }
                  e.setCancelled(true);
    		}




    	}

    	if (this.mode.getValue().equals("MushMc")) {
    		 if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                 e.setCancelled(true);
                 Helper.sendMessage("Spoofed the first packet successfully");
             }


   		 if(e.getPacket() instanceof C03PacketPlayer) {

                final C03PacketPlayer c03 = (C03PacketPlayer) e.getPacket();
                mc.thePlayer.sendQueue.addToSendQueue(new C18PacketSpectate(mc.thePlayer.getUniqueID()));
                if (mc.thePlayer.ticksExisted % 100 == 0) {
                Helper.sendMessage("Spoofed the second packet successfully");
                }
            }
   		}

    	if (this.mode.getValue().equals("MmcVelo")) {
    		  if (mc.thePlayer.ticksExisted % 2 == 0) {
    			  if (e.getPacket() instanceof S12PacketEntityVelocity) {
    	 	            e.setCancelled(true);
    	 	        }
    		  }
    	}


    	if (this.mode.getValue().equals("VulcanCombat")) {

    		if (e.getPacket() instanceof C0BPacketEntityAction) {
  	            e.setCancelled(true);
    		}

    		  if (e.getPacket() instanceof C00PacketKeepAlive || e.getPacket() instanceof C0FPacketConfirmTransaction && this.mc.thePlayer.isEntityAlive()) {
  	            this.packetList.add(e.getPacket());
  	            e.setCancelled(true);
  	        }
  	        if (this.timer.hasReached(750.0)) {
  	            if (!this.packetList.isEmpty()) {
  	                int i = 0;
  	                double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
  	                for (Packet packet1 : this.packetList) {
  	                    if ((double)i >= totalPackets) continue;
  	                    ++i;
  	                    this.mc.getNetHandler().getNetworkManager().sendPacket(packet1);
  	                    this.packetList.remove(packet1);
  	                }
  	            }
  	            this.mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(10000));

  	            this.timer.reset();
  	        }
    	}

     	if (this.mode.getValue().equals("Lunar")) {

     		if (e.getPacket() instanceof C03PacketPlayer) {
     			mc.thePlayer.sendPlayerAbilities();
     		}

     		 if (e.getPacket() instanceof C0BPacketEntityAction) {
     			 e.setCancelled(true);
     		 }



                // if (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                   //  final C03PacketPlayer.C04PacketPlayerPosition packetPlayerPosition = (C03PacketPlayer.C04PacketPlayerPosition) p;
                  //   e.setPacket(new C03PacketPlayer.C06PacketPlayerPosLook(packetPlayerPosition.getX(), packetPlayerPosition.getY(), packetPlayerPosition.getZ(), mc.thePlayer.rotationYaw + (mc.thePlayer.ticksExisted % 2 == 0 ? RandomUtils.nextFloat(0.05F, 0.1F) : -RandomUtils.nextFloat(0.05F, 0.1F)), mc.thePlayer.rotationPitch, packetPlayerPosition.isOnGround()));
                // }





     	}

     	if (this.mode.getValue().equals("OmniSprint")) {

   		 if (e.getPacket() instanceof C0BPacketEntityAction) {

   		   final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
   		      if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
   		    	    if (mc.thePlayer.serverSprintState) {
   		    	    	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                           mc.thePlayer.serverSprintState = false;
                       }
   		    	    e.setCancelled(true);
   		      }
   		       if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                      e.setCancelled(true);
   		       }

   		 }

     	}

    	if (this.mode.getValue().equals("Vulcan")) {

    		 if (e.getPacket() instanceof C0BPacketEntityAction) {

    		  final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
    		      if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {
    		    	    if (mc.thePlayer.serverSprintState) {
    		    	    	mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                           mc.thePlayer.serverSprintState = false;
                        }
    		    	    e.setCancelled(true);
    		      }
    		       if (c0B.getAction().equals(C0BPacketEntityAction.Action.STOP_SPRINTING)) {
                       e.setCancelled(true);
    		       }

    		 }

    		// if (e.getPacket() instanceof C17PacketCustomPayload) {
    			// e.setCancelled(true);

    		//}



    	}

    	  if (this.mode.getValue().equals("VerusFull")) {
    		  mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));

    		  if (e.getPacket() instanceof C00PacketKeepAlive && this.mc.thePlayer.isEntityAlive()) {
  	            this.packetList.add(e.getPacket());
  	            e.setCancelled(true);
  	        }
  	        if (this.timer.hasReached(750.0)) {
  	            if (!this.packetList.isEmpty()) {
  	                int i = 0;
  	                double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
  	                for (Packet packet1 : this.packetList) {
  	                    if ((double)i >= totalPackets) continue;
  	                    ++i;
  	                    this.mc.getNetHandler().getNetworkManager().sendPacket(packet1);
  	                    this.packetList.remove(packet1);
  	                }
  	            }
  	            this.mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(2000));
  	            this.timer.reset();
  	        }


    	  }
    	  if (this.mode.getValue().equals("VerusCevata")) {
    	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    	mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(null));
    	this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.42, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
		this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.753, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
    }
    	  if (this.mode.getValue().equals("BlocksMcLessFlags")) {
    	        if (e.getPacket() instanceof C00PacketKeepAlive && this.mc.thePlayer.isEntityAlive()) {
    	            this.packetList.add(e.getPacket());
    	            e.setCancelled(true);
    	        }
    	        if (this.timer.hasReached(750.0)) {
    	            if (!this.packetList.isEmpty()) {
    	                int i = 0;
    	                double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
    	                for (Packet packet1 : this.packetList) {
    	                    if ((double)i >= totalPackets) continue;
    	                    ++i;
    	                    this.mc.getNetHandler().getNetworkManager().sendPacket(packet1);
    	                    this.packetList.remove(packet1);
    	                }
    	            }
    	            this.mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(10000));
    	            this.timer.reset();
    	        }

    	  }

    	  if (this.mode.getValue().equals("PingSpoof")) {
  	        if (e.getPacket() instanceof C00PacketKeepAlive && this.mc.thePlayer.isEntityAlive()) {
  	            this.packetList.add(e.getPacket());
  	            e.setCancelled(true);
  	        }
  	        if (this.timer.hasReached(750.0)) {
  	            if (!this.packetList.isEmpty()) {
  	                int i = 0;
  	                double totalPackets = MathUtils.getIncremental(Math.random() * 10.0, 1.0);
  	                for (Packet packet1 : this.packetList) {
  	                    if ((double)i >= totalPackets) continue;
  	                    ++i;
  	                    this.mc.getNetHandler().getNetworkManager().sendPacket(packet1);
  	                    this.packetList.remove(packet1);
  	                }
  	            }
  	            this.mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive(Ping.getValue().intValue()));
  	            this.timer.reset();
  	        }

  	  }

    	  if (this.mode.getValue().equals("Watchdog")) {



    		  if(Killaura.target != null) {
    		  if (e.getPacket() instanceof C0BPacketEntityAction) {

    	   		   final C0BPacketEntityAction c0B = (C0BPacketEntityAction) e.getPacket();
    	   		      if (c0B.getAction().equals(C0BPacketEntityAction.Action.START_SPRINTING)) {

    	   		    	    e.setCancelled(true);
    	   		      }
    		  }
    		  }


    	    }







    }



	@EventHandler
    public void onUpdate(EventPreUpdate e) {
		if (this.mode.getValue().equals("Polar")) {

			if(Ducagay) {
				e.setOnground(false);
				e.setOnground(true);
				Helper.sendMessage("Best tp fr");
			}
			///3000

			if(mc.thePlayer.isInvisible() && timer.hasReached(3000)) {

				Ducagay = true;

			}
			if(!mc.thePlayer.isInvisible()) {
				Ducagay = false;
			}


		}
		if (this.mode.getValue().equals("GrimCombat")) {
		if(Killaura.target == null) {
			mc.thePlayer.isDead = false;
		}
    		if(mc.thePlayer.onGround && Killaura.target != null) {
    			mc.thePlayer.isDead = true;

    		} else {
    			mc.thePlayer.isDead = false;
    		}
    	}
		if (this.mode.getValue().equals("Hac")) {
    		if (mc.thePlayer.ticksExisted % 20 == 0) {
    			 lagback = true;
                mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, -13, mc.thePlayer.posZ, false));
            }




    	}
    	 if (ArrayList2.Sufix.getValue().equals("On")) {


     		this.setSuffix(mode.getModeAsString());
     	}


     	 if (ArrayList2.Sufix.getValue().equals("Off")) {


      		this.setSuffix("");
      	}


		if (this.mode.getValue().equals("Sentinel2")) {
			//if(Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled() && !mc.thePlayer.onGround) {
				//e.setOnground(true);


		//	}

		}
		if (this.mode.getValue().equals("SentinelNew")) {
	//	e.setOnground(false);
			if (Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
				//e.setOnground(true);
				//mc.thePlayer.onGround
				boolean playerNearby =
						mc.currentScreen != null;

				if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY, mc.thePlayer.lastReportedPosZ)
						<= (playerNearby ? 5 : 10) - Fly.speed.getValue().doubleValue() - 0.15 && mc.thePlayer.swingProgressInt != 3) {
					e.setCancelled(true);
				}


			}
		}
     	if (this.mode.getValue().equals("Sentinel")) {

     	//	if(Killaura.target == null) {
     		//fix disabler
     		//or just straight up set the ground on true
     		//maybe use an in air check
     		//that should work
     //	if(!mc.thePlayer.onGround) {
     		//maybe remove the air check
     		//maybe dont use this

    // 	if(!Client.instance.getModuleManager().getModuleByClass(Killaura.class).isEnabled()) {

     	//e.setOnground(true);

     		//}
     	//}
     	//	}


     		//if(Killaura.target !=null) {
     	//	if(!mc.thePlayer.onGround) {
     		//if(PlayerUtils.isMoving()) {
     	//	if(Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled() && !mc.thePlayer.onGround && PlayerUtils.isMoving()) {
     		//	e.setOnground(true);
     		//}

     		if(Client.instance.getModuleManager().getModuleByClass(Fly.class).isEnabled()) {
     			//e.setOnground(true);
			//mc.thePlayer.onGround
				boolean playerNearby =
						mc.currentScreen != null;

				if (mc.thePlayer.getDistance(mc.thePlayer.lastReportedPosX, mc.thePlayer.lastReportedPosY,mc.thePlayer.lastReportedPosZ)
						<= (playerNearby ? 5 : 10) - Fly.speed.getValue().doubleValue() - 0.15 && mc.thePlayer.swingProgressInt != 3) {
					e.setCancelled(true);
				}


     		}

     	//	e.setOnground(true);
     		 //mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
     		//if (mc.thePlayer.ticksExisted % 20  == 0) {
    		//	 counter++;
    		 	//Helper.sendMessage("Sentinel on top " + counter);
    		//}

     		//}
     			//mc.thePlayer.onGround = true;
     		//}

     		//}

     		if (mc.thePlayer.ticksExisted % 120  == 0) {
     			 counter++;
     		 	//Helper.sendMessage("did zucu's mom " + counter + " times" + "(zucu if you see this go back to sucking allan's dick)" + " also Dont Break blocks nigga");
     		}

     	}

     	if (this.mode.getValue().equals("Sparky")) {

        		 mc.thePlayer.sendQueue.addToSendQueue(new C13PacketPlayerAbilities());
        		 mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());


     	 if (mc.thePlayer.ticksExisted % 3 == 0) {
   		  counter++;

			   Helper.sendMessage("C0FPacketConfirmTransaction " + counter);
   		 }
     	}


    	if (this.mode.getValue().equals("VoidTp")) {
    		if (mc.thePlayer.ticksExisted % 20 == 0) {
            double x = mc.thePlayer.posX;
            double y = mc.thePlayer.posY;
            double z = mc.thePlayer.posZ;
    		//double x = e.getYaw();
          //  double y = e.getY();
           // double z = e.getPitch();

            //int min = 1024;
           // int max = 2040;
            Random random = new Random();
             int min = 1024;
             int max = 2040;
             int randomNumber = random.nextInt(max - min + 1) + min;

            Helper.sendMessage("Did the funny");
            y -= randomNumber;

            	  mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));


            //true
    		}


    	}








    }
}








