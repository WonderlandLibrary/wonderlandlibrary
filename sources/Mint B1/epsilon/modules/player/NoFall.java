package epsilon.modules.player;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.ModeSetting;
import epsilon.util.MoveUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class NoFall extends Module{
	

	private final Queue<Packet> packetQueue = new ConcurrentLinkedDeque<>();
	
	private float dist;
	private int matrixSpoofCount;
	private boolean matrixDamaged, matrixSpoof;
	private double[] savedMotion = {0,0,0};
	
	public final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla","Packet","PacketBypass","Hypixel","WatchdogTest","NCP","Edit","Test", "Air","Ground","Redesky","Matrix", "Verus","Verus2", "Vulcan", "Zonecraft","Spartan1.8", "Spartan1.9+","Block","BlockSilent");
	public NoFall(){
		super("NoFall", Keyboard.KEY_F, Category.PLAYER, "Will remove or reduce fall damage");
		this.addSettings(mode);
	}
	public void onEnable() {
		matrixSpoofCount = 0;matrixSpoof=false;
		dist = mc.thePlayer.fallDistance;
		if(mode.getMode() == "SpartanFunniPacket") {
			mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX + 1.5,mc.thePlayer.posY, mc.thePlayer.posZ +1.5, true));
		}
		matrixDamaged = false;
	}
	
	public void onDisable() {

		mc.timer.timerSpeed = 1;
		
		packetQueue.forEach(packets -> mc.getNetHandler().sendPacketNoEvent(packets));
		packetQueue.clear();
	}
	
	public void onEvent(final Event e) {
		
		if(e instanceof EventSendPacket) {
			

    		Packet p = e.getPacket();
    		
			switch(mode.getMode()) {
			
			case "Matrix":
				
				if(((p instanceof C03PacketPlayer.C04PacketPlayerPosition || p instanceof C03PacketPlayer.C05PacketPlayerLook || p instanceof C03PacketPlayer.C06PacketPlayerPosLook))&& mc.thePlayer.fallDistance>2) {
					matrixSpoofCount++;

					final C03PacketPlayer packet = (C03PacketPlayer) p;
					
					switch(matrixSpoofCount) {
					
					case 1:
						
						savedMotion[0] = mc.thePlayer.motionX;
						savedMotion[1] = mc.thePlayer.motionY;
						savedMotion[2] = mc.thePlayer.motionZ;
						mc.timer.timerSpeed = 1;
						break;
						
					case 2:
						savedMotion[0] = mc.thePlayer.motionX;
						savedMotion[1] = mc.thePlayer.motionY;
						savedMotion[2] = mc.thePlayer.motionZ;
						
						break;
						
					case 3:
						packet.onGround= true;
						
						break;
						
					case 4:
						mc.timer.timerSpeed = 0.4f;
						break;
						
					case 5:
						mc.thePlayer.fallDistance = 0;
						matrixSpoofCount = 0;
						mc.timer.timerSpeed = 1;

						break;
					
					}
					
				}
				
				break;
				
			case "Edit":
				
				if(p instanceof C03PacketPlayer) {
					C03PacketPlayer packet = (C03PacketPlayer) p;
					packet.setOnGround(true);
				}
				
				break;
			
			}
			
		}
		
		if(e instanceof EventUpdate) {
			
			this.displayInfo = mode.getMode();
			
			if(e.isPre()) {
				
				switch(mode.getMode()) {
				
				
				case "Watchdog":
					if (mc.thePlayer.fallDistance > 3) {
	                    mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer(true));
	                }
	                if (mc.thePlayer.fallDistance > 4 && mc.thePlayer.motionY < -0.1) {
	                    if (mc.thePlayer.isSprinting())
	                        mc.thePlayer.setSprinting(false);
	                    mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.posY, mc.thePlayer.lastTickPosZ);
	                }
	                
	                break;
	                
				
	                
	                
				} 
			}
		}
		
		if(e instanceof EventMotion) {

			final EventMotion event = (EventMotion)e;
			
			
			if(e.isPre()) {
    			this.displayInfo = mode.getMode();
				
    			
    			
				switch(mode.getMode()) {
				
				case "Hypixel":
					
					if(mc.thePlayer.fallDistance>2.5) event.setOnGround(true);
					
					break;
				
				
				case "NCP":

					
					break;

				case "PacketBypass":
					if(mc.thePlayer.fallDistance>0) {
						if(mc.thePlayer.fallDistance>1.5f) 
						event.setOnGround(true);
						if(mc.thePlayer.fallDistance>2) {
						mc.getNetHandler().sendPacketNoEvent(new C03PacketPlayer(false));

						mc.thePlayer.fallDistance = 0;
						}
					}
					
					break;
				
				case "Edit":
					if(mc.thePlayer.fallDistance>0)
						event.setOnGround(true);
					break;
				case "Matrix":
					
					if(mc.thePlayer.onGround && mc.timer.timerSpeed!=0 && matrixSpoofCount!=0) mc.timer.timerSpeed =1;
					
					if(matrixSpoofCount > 1) {
						event.setOnGround(true);
						event.setY(mc.thePlayer.posY-0.1E-15);
					}
					if(mc.thePlayer.onGround) matrixSpoofCount = 0;
					
					break;
				
				
				case "WatchdogTest":
					
					if(mc.thePlayer.fallDistance>2) {
						
						event.setOnGround(true);
						
						
					}
					
					break; 	
				
				case "Verus":
				case "Zonecraft":
				case "BlockSilent":
					
					if(mc.thePlayer.fallDistance>2) {
						mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement
								(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
								(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f));
						event.setOnGround(true);
						event.setY(Math.round(mc.thePlayer.posY));
						mc.thePlayer.fallDistance = 0;
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					}
					
					break;
				
				case "Verus2":
					boolean damaged = false;
					if(!damaged && mc.thePlayer.fallDistance>3.5) {
						event.setOnGround(true);
						damaged = true;
					}	
					if(mc.thePlayer.hurtTime>0)
						damaged = true;
					if(mc.thePlayer.fallDistance>3 && damaged) {
						event.setOnGround(true);
						
						mc.thePlayer.fallDistance = 0;
					}
					if(mc.thePlayer.onGround)
						damaged = false;
					break;
					
				case "Block":
					if(mc.thePlayer.fallDistance>3) {
						mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement
							(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
								(mc.theWorld, new BlockPos(1, 1, 1))), 0, 0f, 0));
						event.setOnGround(true);
						MoveUtil.SetMomentum(0,0,0);
						event.setY(Math.round(mc.thePlayer.posY));
						mc.thePlayer.fallDistance = 0;
					}
					break;
					
				case "Test":
					if(mc.thePlayer.fallDistance>3) {
						mc.getNetHandler().addToSendQueue(new C03PacketPlayer(false));
						mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement
								(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
								(mc.theWorld, new BlockPos(1, 1, 1))), 0, 0f, 0));
						event.setOnGround(true);
						event.setY(Math.round(mc.thePlayer.posY));
						mc.thePlayer.fallDistance = 0;
					}
					break;
					
				case "Vulcan":
					if(mc.thePlayer.fallDistance>3.1) {
						//mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement
							//	(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								//new ItemStack(Blocks.stone.getItem
								//(mc.theWorld, new BlockPos(1, 1, 1))), 0, 0f, 0));
						event.setOnGround(true);
						MoveUtil.SetMomentum(mc.thePlayer.motionX,0,mc.thePlayer.motionZ);
						mc.thePlayer.fallDistance = 0;
					}
					break;
					
				case "Air":

					event.setOnGround(false);
					event.setY(event.getY() + 0.00125);
					break;
					
				case "Ground":

					event.setOnGround(true);
					break;
					
				case "Packet":

					if (mc.thePlayer.fallDistance >= 2.9) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
						
						mc.thePlayer.fallDistance = 0;
					}	
					break;
					
				case "Vanilla":
				case "Redesky":

					if(mc.thePlayer.fallDistance>3) {
						event.setOnGround(true);
						
						mc.thePlayer.fallDistance = 0;
					}
					break;
					
				case "Spartan1.8":
					if (mc.thePlayer.fallDistance >= 2.8) {
						event.setOnGround(true);
	                   mc.thePlayer.fallDistance = 0;  
	                }
					break;
					
				case "Spartan1.9+":
					if (mc.thePlayer.fallDistance >= 2) {
		                   mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		            }if(mc.thePlayer.fallDistance > 7) {
		                   mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
		                   mc.thePlayer.fallDistance = 0;
		            }
					break;
				}
			}	
			
		}
		
		if(e instanceof EventReceivePacket) {
			

    		Packet p = e.getPacket();
    		
			switch(mode.getMode()) {
			
			
				
			}
			
		}
	}
}