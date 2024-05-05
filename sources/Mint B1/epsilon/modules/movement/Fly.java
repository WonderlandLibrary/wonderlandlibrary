package epsilon.modules.movement;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventRenderGUI;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventReceivePacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.MoveUtil;
import epsilon.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Fly extends Module{
	
	private float gradualTimer = 1;

	private final Queue<Packet> packetQueue = new ConcurrentLinkedDeque<>();
	
	private boolean beginVulcan, watchdogDamaged, watchdogRecievedVelo, latinoSwitch, verus3Switch;
	
	private double oldX, oldY, oldZ;
    private int funni, minelatinoBalanceTicks, hycraftGround;
    private double[] pos;

	EntityOtherPlayerMP spartanEntity;
	
	public ModeSetting mode = new ModeSetting("Mode", 
	"Vanilla", "Vanilla", "Watchdog","NCP","NCPEdit","BMCSlime","Test", "Test2", "Test3","Matrix", 
	"MineMoraConsistent","Vulcan","OldVulcan","Hycraft","VerusVanilla","Verus", "Verus3", "MorganBasic","Kauri", "KauriInfinite","MineLatino",
	"CancelY", "Collide", "Hawk" ,"Basic", 
	"CatchMotion", "Ground", "Clip", "Funcraft", "Hades", "Spartan");
	
	public NumberSetting matrixBaseFactor = new NumberSetting ("MatrixBaseFactor", 20, 1, 50, 0.5);

	public NumberSetting matrixReduce = new NumberSetting ("MatrixReduce", 0.15, 0.0, 1, 0.05);
	
	public ModeSetting matrixFactorMode = new ModeSetting ("MatrixdFactor", "Gradual", "Gradual", "Static");
	
	public NumberSetting fly = new NumberSetting("FlySpeedH", 1.0, 0.25, 10.0, 0.25);
	public NumberSetting flyv = new NumberSetting("FlySpeedV", 1.0, 0.25, 10.0, 0.25);
	public NumberSetting glide = new NumberSetting("Glide", 0.1, 0.0, 1, 0.05);
	public BooleanSetting wcl = new BooleanSetting("WatchdogClip", false);
	public BooleanSetting wdmg = new BooleanSetting("WatchdogDamage", false);
	public NumberSetting yd = new NumberSetting("YCancelDelay", 200, 50, 1000, 50);
	public NumberSetting cd = new NumberSetting("ClipDelay", 200, 50, 1000, 50);
	public NumberSetting clipdi = new NumberSetting("ClipDistanceXZ", 2, -8, 8, 1);
	public NumberSetting clipdiy = new NumberSetting("ClipDistanceY", 2, -8, 8, 1);
	public BooleanSetting clipGsp = new BooleanSetting("ClipGroundSpoof", false);
	public BooleanSetting clipF = new BooleanSetting("ClipFreeze", false);
	public BooleanSetting vSilent = new BooleanSetting("SilentVerusVanilla", false);
	public Timer timer = new Timer();
	
	int watchdogint;
	
	public double layery, layerx, layerz;
	public double othery;
	float spanishtimer;
	private float matrixtimer;
	private int slot;
	public double px;
	public double py;
	public double pz;
	private int i, ticks = 0;
	private boolean jumped,dmged, beingDmged;
	public MoveUtil move = new MoveUtil();
	boolean Hehehahatime = false;
	double speed = 0.4;
	long lastMatrixMS=0;
	int kauristage = 1;
	int time = 0;
	int bsh;
	int stopwhen = 0;
	int spanishL = 0;
	double y = 0;
	boolean youMayBeginRapingSpartan = false;
	boolean aBoolForBWP = false;
	public static boolean spartanishehas = false;
	public Fly(){
		super("Fly", Keyboard.KEY_G, Category.MOVEMENT, "Mono que puede volar");
		this.addSettings(mode, glide, fly,flyv, wcl, wdmg,vSilent,yd, cd, clipdi, clipdiy, clipGsp, clipF, matrixFactorMode, matrixBaseFactor, matrixReduce);
	}
	public static double roundToOnGround(final double posY) {
        return posY - (posY % 0.015625);
    }
	public void onDisable(){
		aBoolForBWP = false;

		mc.thePlayer.capabilities.isFlying = false;
		beginVulcan = false;
		watchdogDamaged = verus3Switch = false;
		
		
		if(mode.getMode()=="MorganPosition") {
			mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));

		}
		
		move.SetMomentum(0, mc.thePlayer.motionY, 0);
		
		for(Packet packet : packetQueue) {
			mc.getNetHandler().sendPacketNoEvent(packet);
		}
		packetQueue.clear();
		
		spanishL = 0;
		spartanishehas = false;
		youMayBeginRapingSpartan = false;

		mc.thePlayer.capabilities.isFlying = false;
		mc.thePlayer.capabilities.allowFlying = false;
		

		
        if((mode.getMode() == "Hawk" || mode.getMode()=="AstroBow") && slot != mc.thePlayer.inventory.currentItem) 
        	mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
        
		if(mode.getMode() == "Vanilla")
			mc.thePlayer.capabilities.isFlying = false;
		
		Hehehahatime = false;
		mc.timer.timerSpeed = 1;
		kauristage = 1;
		if(mode.getMode()=="Test3") {
			move.SetMomentum(0, 0, 0);
		}
		if(mode.getMode()=="OldVulcan")
			move.SetMomentum(0, 0, 0);
		
	}
	
	
	 
	public void onEnable() {
		watchdogDamaged = false;
	watchdogRecievedVelo = false;
	time = 0;
		if(mode.getMode()=="Test") 
			gradualTimer = 5;
		
		if(mode.getMode()=="MorganPosition") {
			time = 0;
			slot = mc.thePlayer.inventory.currentItem;
			if(getBowSlot()!=-1)
			mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(getBowSlot()));aBoolForBWP = false;

		}
		
		if(mode.getMode() == "Test2") {
			if(mc.thePlayer.onGround)
			move.packetComedy(0, 0.42);
			mc.timer.timerSpeed = 1.5f;
			
		}		
		beginVulcan = false;
		
		timer.reset();
		
		switch(mode.getMode()) {
		
		case "Vulcan":
			beginVulcan = false;
			break;
		
		case "Hycraft":
			if(!mc.thePlayer.onGround && this.toggled) {
				Epsilon.addChatMessage("Must be onground");
				this.toggle();
			}
			break;
		
		case "Hades":
	        for(int i=0;i<8;i++) {
	        	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42f, mc.thePlayer.posZ, false));
	        	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
	        }
	    	mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
			break;
		
		case "Matrix":
			
			matrixtimer =(float) matrixBaseFactor.getValue();
			if(mc.thePlayer.onGround)
				mc.thePlayer.jump();
			
			break;
		
		case "MorganBasic":
			
			
			mc.timer.timerSpeed = 0.5f;
			
			if(mc.thePlayer.onGround)
				mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.42f, mc.thePlayer.posZ);
			
			break;
			
		}
		
		watchdogint = minelatinoBalanceTicks = hycraftGround=  0;
		latinoSwitch = false;
		if(wdmg.isEnabled())
			watchdogDamaged = false;
		else 
			watchdogDamaged = true;
		
		
		if(mode.getMode()=="Test3" && mc.thePlayer.onGround) {
			Epsilon.addChatMessage("Dont spam, this is 1000x better then regular vanilla fly, but if you spam like 6+ in a row for 100s of blocks, it will still ban.");
		}
		
		
		aBoolForBWP=false;
		spanishtimer=0;
		if((mode.getMode() == "CatchMotion"||mode.getMode()=="Funcraft") && mc.thePlayer.onGround) {
			mc.thePlayer.jump();
		}
		spanishL = 0;
		spartanishehas = false;
		youMayBeginRapingSpartan = false;
		if(mode.getMode() == "Astro") {
			//mc.thePlayer.motionY = 0.42f;
			stopwhen = 0;
		}
		if(mode.getMode() == "MineLatino") {
			mc.thePlayer.motionY = 0.42f;
			stopwhen = 0;
		}
		if(mode.getMode() == "MineLatinoGood") {
			mc.thePlayer.jump();
			spanishL++;
		}

		slot = mc.thePlayer.inventory.currentItem;
		bsh = 0;
		time = 0;
		dmged = false;
		jumped = false;
		py = mc.thePlayer.posY;
		kauristage = 1;
		layery = mc.thePlayer.posY;
		layerx = mc.thePlayer.posX;
		layerz = mc.thePlayer.posZ;
		speed = 1;
		if(mode.getMode() == "Kauri" || mode.getMode() == "KauriInfinite") {
			Epsilon.addChatMessage("Kauri Nofall is reccomended with this fly.");
		}
		if(mode.getMode() == "Hawk" || mode.getMode() == "AstroBow") {
			mc.thePlayer.motionY = 0.42f;
				Epsilon.addChatMessage("Toggle Velocity for the best experience");
			
		}
		if(mode.getMode()=="MMCGlide") {
			mc.thePlayer.jump();
			gradualTimer = (float) 0.01;
		}


	}
	public void onEvent(Event e){
		
		
		if(mc.thePlayer !=null) {
			if ( mc.thePlayer.hurtTime>0 ) {
	
				dmged = true;
			}else {
				dmged = false;
			}
		}
		
		
		if(e instanceof EventSendPacket && mc.getNetHandler()!=null) {

    		Packet p = e.getPacket();
    		
			switch(mode.getMode()) {
			
			case "Vulcan":
				
				if(beginVulcan) {

				
					packetQueue.add(p);
					e.setCancelled();
				
				}else if (!packetQueue.isEmpty()) {

					for(Packet packet : packetQueue) {
						mc.getNetHandler().sendPacketNoEvent(packet);
					}
					packetQueue.clear();
				}
				
				break;
			
			case "Hades":
			case "Matrix":
			case "Redesky":
				
				e.setCancelled();
				packetQueue.add(p);
				
				break;
			
			
			
			case "MorganBasic":
				
				if(p instanceof C03PacketPlayer.C05PacketPlayerLook || p instanceof C03PacketPlayer.C04PacketPlayerPosition || p instanceof C03PacketPlayer.C06PacketPlayerPosLook || p instanceof C02PacketUseEntity || p instanceof C0APacketAnimation || p instanceof C01PacketChatMessage) {
    				
					packetQueue.add(p);
    			}
				e.setCancelled();
				
				if(timer.hasTimeElapsed(1000, true)) {
					for(Packet packet : packetQueue) {
						mc.getNetHandler().sendPacketNoEvent(packet);
					}
					mc.timer.timerSpeed=0.5f;
					packetQueue.clear();
					Epsilon.addChatMessage("Release");
				}else {
					mc.timer.timerSpeed+=0.1f;
				}
				
				
				break;
			
			case "MineLatino":
				
				if(minelatinoBalanceTicks<20 && !(p instanceof C00PacketKeepAlive)&& !(p instanceof C0FPacketConfirmTransaction)) {
					e.setCancelled();
				}
				
				
				break;
				
			case "Watchdog":
				if(watchdogRecievedVelo) {
					e.setCancelled();
					packetQueue.add(p);
				}
				break;
			
			}
			
		}
		
		if(e instanceof EventReceivePacket && mc.thePlayer!=null) {
			Packet p = e.getPacket();
			switch(mode.getMode()) {
			
			case "NCPEdit":
				if(p instanceof S08PacketPlayerPosLook) {
					
					final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
					
					packet.yaw = mc.thePlayer.cameraYaw;
					packet.pitch = mc.thePlayer.cameraPitch;
					
					if(mc.thePlayer.ticksExisted%2==0) {
	        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
						e.setCancelled();
					}
					
					
				}
				
				break;
			
			case "Minelatino":
				if(p instanceof S12PacketEntityVelocity) {
					e.setCancelled();
				}
				break;
			
			case"Hycraft":
				if(p instanceof S08PacketPlayerPosLook && hycraftGround<10) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Responded accurately to S08");
					e.setCancelled();
				}
				break;
			
			case "Watchdog":
				

				if(p instanceof S12PacketEntityVelocity) {
					watchdogRecievedVelo = true;
					Epsilon.addChatMessage("begin");
				}
				
				break;
			
			case "MorganPosition":
				
				if(p instanceof S12PacketEntityVelocity) {
					if(mc.thePlayer.hurtTime>0)
						aBoolForBWP = true;
					e.setCancelled();
				}
				
				break;
				
			case "Redesky":
			
				if(p instanceof S08PacketPlayerPosLook) {
					S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) p;
        			mc.getNetHandler().sendPacketNoEvent(new C06PacketPlayerPosLook(packet.x, packet.y, packet.z, packet.yaw, packet.pitch, mc.thePlayer.onGround));
        			Epsilon.addChatMessage("Responded accurately to S08");
        			e.setCancelled();
        			
        			
    			}
				if(p instanceof S12PacketEntityVelocity) {
					e.setCancelled();
				}
				
				break;
				
			case "NCP":
				break;
			}
		}
		
		if(e instanceof EventUpdate) {
			minelatinoBalanceTicks++;hycraftGround++;
			this.displayName = mode.getMode();
			
			
			this.displayInfo = mode.getMode();
			
			if(mode.getMode()=="MatrixEgg");
				time++;
				
			switch(mode.getMode()) {
			
			case "MorganPosition":


				time++;
				if(time<10)
				mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventoryContainer.getSlot(getBowSlot() + 36).getStack(), 0, 0, 0));
				
				if(time>11 && time<13) {
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, -90, mc.thePlayer.onGround));
					mc.getNetHandler().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				}
				
				if(aBoolForBWP) {
					move.strafe(move.getBaseMoveSpeed()+Math.random()/50);
					mc.thePlayer.motionY+=0.15;
					if(mc.thePlayer.onGround)
						mc.thePlayer.motionY+=0.42f;
					
					if(timer.hasTimeElapsed2(650, true) && mc.thePlayer.hurtTime<=0) {
						aBoolForBWP = false;
						mc.thePlayer.motionY = 0;
					}
				}
				
				
				break;
			
			case "Redesky":

				move.strafe(move.getBaseMoveSpeed());
				mc.thePlayer.capabilities.isFlying = true;
				mc.getNetHandler().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer,C0BPacketEntityAction.Action.STOP_SPRINTING));

				mc.getNetHandler().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer,C0BPacketEntityAction.Action.START_SPRINTING));
				break;
			
			case"Watchdog":
				
				
				
				
				break;
			}
			
		}
		if(e instanceof EventMotion){
			if(e.isPost()) {
				EventMotion event = (EventMotion)e;
			}
			if(e.isPre()){
				EventMotion event = (EventMotion)e;
				switch(mode.getMode()) {
				
				case "NCPEdit":

					final double yawwww = Math.toRadians(mc.thePlayer.rotationYaw);
					
					mc.thePlayer.motionY = 0;
					MoveUtil moveeUtil = new MoveUtil();
					if(!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.42, mc.thePlayer.posZ)).getBlock() instanceof BlockAir))

						mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.42, mc.thePlayer.posZ);
					
					
					double expectedXXX;
					double expectedYYY;
				    double expectedZZZ;
					
					double xxx = mc.thePlayer.posX;
			        double yyy = mc.thePlayer.posY;
			        double zzz = mc.thePlayer.posZ;
			        

			        expectedXXX = xxx + (-Math.sin(yawwww) * 9);
			        expectedZZZ = zzz + (Math.cos(yawwww) * 9);
			        if(mc.gameSettings.keyBindJump.getIsKeyPressed())  
				        yyy = 5;
			        
					else if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
						yyy = -5;
					else
						yyy = 0;
			        
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXXX, mc.thePlayer.posY + yyy, expectedZZZ, false));
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXXX, mc.thePlayer.posY, expectedZZZ, false));
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXXX, mc.thePlayer.posY, expectedZZZ, true));
			        
					
					
					break;
				
				case"Hycraft":

					if(hycraftGround<10) {
						mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
						event.setY(mc.thePlayer.posY-0.5);
						mc.thePlayer.motionY = -0.1;
					}else if(hycraftGround>11 && hycraftGround<=12){
						
						mc.thePlayer.jump();
					}else {
						mc.getNetHandler().sendPacketNoEvent(new C0CPacketInput(55,55,true,true));
						mc.thePlayer.motionY = 0;
						mc.timer.timerSpeed = 0.3f;
						move.strafe(move.getBaseMoveSpeed()+1);
					}
					break;
				
				case "Watchdog":
					
					
					if(!watchdogDamaged) {
						move.timerDamageHypixel();
						watchdogDamaged = true;
					}else if (mc.thePlayer.onGround)
						mc.thePlayer.jump();
					
					if(watchdogRecievedVelo) {
						mc.thePlayer.motionY = 0;
						move.strafe(move.getBaseMoveSpeed()+0.2);
					}
					
					break;
				
				case "BMCSlime":
					
					if(mc.thePlayer.onGround)
						y = mc.thePlayer.posY;
					//if(move.getBlockRelativeToPlayer(0, 0, 0) instanceof BlockAir) {
						//mc.thePlayer.swingItem();
					if(!mc.thePlayer.onGround)
						move.place(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY - y,mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
					//}
					if(!mc.thePlayer.onGround&&move.getBlockRelativeToPlayer(0, -0.1, 0) instanceof BlockSlime) {
						beginVulcan = true;
						mc.thePlayer.setPosition(mc.thePlayer.posX, Math.round(mc.thePlayer.posY), mc.thePlayer.posZ);
					}
					if(beginVulcan) {

						mc.thePlayer.motionY = 0;
						move.strafe();
					}
					
					break;
				
				case "Hades":
					mc.thePlayer.motionY = 0;if(mc.thePlayer.ticksExisted%2==0)
						event.setOnGround(true);
					
					move.strafe(move.getBaseMoveSpeed()+2);
					
					/*mc.thePlayer.motionY = 0;
					
					if(mc.gameSettings.keyBindJump.getIsKeyPressed())
						mc.thePlayer.motionY = 0.42;
					else if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
						mc.thePlayer.motionY = -1;

					if(mc.thePlayer.ticksExisted%2==0) {
						event.setOnGround(true);
						move.strafe(move.getBaseMoveSpeed());
						mc.timer.timerSpeed = 1.1f;
					}else {
						move.strafe(move.getBaseMoveSpeed()+0.05);

						mc.timer.timerSpeed = 1.0f;
					}*/
					
					break;
				
				
				case "Redesky":
					
					event.setOnGround(true);
					
					break;
				
				case "MineMoraConsistent":
					
					mc.timer.timerSpeed = (float) (1.3f - Math.random()/10);
					gradualTimer-=0.01;
					if(mc.thePlayer.fallDistance>0)  {
						if(mc.thePlayer.fallDistance>0 && mc.thePlayer.fallDistance<0.1)
							move.strafe(move.getBaseMoveSpeed());
						
						if(mc.thePlayer.fallDistance<1.24)
							mc.thePlayer.motionY = gradualTimer;
						else {
							event.setOnGround(true);mc.thePlayer.fallDistance = 0;
						}
					}
					if(mc.thePlayer.onGround) {
						mc.thePlayer.jump();
						gradualTimer = 0.0f;
					}
					
					
					break;
				
				case "MorganPosition":

					if(time>11) {
					event.setPitch(-90);
					Epsilon.addChatMessage(event.getPitch());
					}
					
					break;
				
				case "MorganBasic":
					
	    			mc.thePlayer.motionY=0;
					
					break;
				
				case "MMCGlide":
						
					
					break;
				
				
				case "NCP":
					
					final double yawww = Math.toRadians(mc.thePlayer.rotationYaw);
					
					mc.thePlayer.motionY = 0;
					MoveUtil moveUtil = new MoveUtil();
					if(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX - (-Math.sin(yawww) * 1), mc.thePlayer.posY - 1, mc.thePlayer.posZ - (Math.cos(yawww) * 1))).getBlock() instanceof BlockAir)
					moveUtil.strafe(moveUtil.getBaseMoveSpeed()+1);
					
					
					double expectedXX;
					double expectedYY;
				    double expectedZZ;
					
					double xx = mc.thePlayer.posX;
			        double yy = mc.thePlayer.posY;
			        double zz = mc.thePlayer.posZ;
			        

			        expectedXX = xx + (-Math.sin(yawww) * 9);
			        expectedZZ = zz + (Math.cos(yawww) * 9);
			        if(mc.gameSettings.keyBindJump.getIsKeyPressed())  
				        yy = 5;
			        
					else if (mc.gameSettings.keyBindSneak.getIsKeyPressed())
						yy = -5;
					else
						yy = 0;
			        
			        if(move.isMoving() || yy!=0) {
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXX, mc.thePlayer.posY + yy, expectedZZ, false));
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXX, mc.thePlayer.posY, expectedZZ, false));
					mc.getNetHandler().addToSendQueue(new C04PacketPlayerPosition(expectedXX, mc.thePlayer.posY, expectedZZ, true));
					mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+(yy/5), zz);
			        }
					
					break;
				
				case "WatchdogVamp":
					
					if(watchdogDamaged) {
					
						if(mc.thePlayer.onGround && wcl.isEnabled())
							py = mc.thePlayer.posY;
						
						else {
							if(wcl.isEnabled())
								mc.thePlayer.setPosition(mc.thePlayer.posX, py + 0.42f, mc.thePlayer.posZ);
							
							mc.thePlayer.motionY = 0;
							move.strafe();
							
						}
						
					}else if (mc.thePlayer.onGround && watchdogint<3) {
						
						event.setOnGround(false);
						watchdogint++;
						mc.thePlayer.jump();
						
					}else if (mc.thePlayer.onGround && watchdogint>2) {
						
						event.setOnGround(true);
						
					}	
					if(mc.thePlayer.hurtTime > 0 && !watchdogDamaged) {
						
						watchdogDamaged = true;
						
					}
					if(watchdogDamaged && mc.thePlayer.onGround && wcl.isEnabled())
						mc.thePlayer.jump();
					
					break;
				
				case "OldVulcan":
					
					mc.timer.timerSpeed = 0.5f;
					
					if(mc.thePlayer.ticksExisted%2==0)
						event.setOnGround(true);
					
					if(mc.thePlayer.onGround)
						move.packetComedy(0, 0.42f);
					
					
					move.strafe(move.getBaseMoveSpeed()+2.1);
					mc.thePlayer.motionY=0;
					
					break;
				


				
				case "MatrixEgg":
					//Not only for egg but eh
					if(mc.thePlayer.hurtTime>0)
						aBoolForBWP = true;
					if(time>30) { 
						aBoolForBWP = false;
						time = 0;
					}	
					
					if(!mc.thePlayer.onGround) {
						layery = mc.thePlayer.posY;
					}	
					
					if(mc.thePlayer.hurtTime>0) 
						mc.thePlayer.motionY = 0.42f;
					
					if(aBoolForBWP) {
						double yaw = mc.thePlayer.cameraYaw;
						speed = 1;
						speed += 0.01;
						if(speed>1.1)
							speed = 1;
						mc.thePlayer.motionX *= speed;
						mc.thePlayer.motionZ *= speed;
					}	
					
					break;
				
				
				
				case "MineLatino":
					
					if(minelatinoBalanceTicks==20 && mc.thePlayer.hurtTime<=0) {
						move.verusDamage();
					}
					if(minelatinoBalanceTicks>20) {
						mc.thePlayer.motionY = 0;
						if(minelatinoBalanceTicks<41)
						move.strafe(move.getBaseMoveSpeed()+0.5);
						else if (move.getSpeed()>move.getBaseMoveSpeed()) {
							mc.thePlayer.motionX*= 0.24;
							mc.thePlayer.motionZ*= 0.24;
						}
					}
					
					
					
					if(minelatinoBalanceTicks<20) {
						mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
					}
					
					if(mc.thePlayer.ticksExisted%2==0) {
						event.setOnGround(true);
						if(mc.thePlayer.ticksExisted%4==0 && mc.gameSettings.keyBindJump.getIsKeyPressed()) {
							mc.thePlayer.jump();
						}
					}
					
					if(minelatinoBalanceTicks>35) {
						Epsilon.addChatMessage("Disabling soon");
					}
					if(minelatinoBalanceTicks>40 && this.toggled) {
						mc.thePlayer.jump();
						event.setOnGround(true);
						this.toggle();
					}
					
					
					break;
				
				case "VerusVanilla":
					mc.thePlayer.motionY = 0;
					
					
					
					if(mc.thePlayer.ticksExisted%2==0) {

						move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
								new ItemStack(Blocks.stone.getItem
									(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
						event.setOnGround(true);
						event.setY(Math.floor(mc.thePlayer.posY));
						move.strafe(move.getSpeed()+0.28);
					}else {
						move.strafe(move.getBaseMoveSpeed()+0.10);
						move.place(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY - y,mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
						
					}
					
					if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {
						mc.thePlayer.motionY = 0.5;
						mc.thePlayer.motionX*=0.75;
						mc.thePlayer.motionZ*=0.75;
					}else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
						mc.thePlayer.motionY = -0.5;
						mc.thePlayer.motionX*=0.5;
						mc.thePlayer.motionZ*=0.5;
					}
					
					if(mc.thePlayer.onGround) {
						mc.thePlayer.motionX*=0.5;
						mc.thePlayer.motionZ*=0.5;
					}
					if(mc.gameSettings.keyBindBack.getIsKeyPressed()) {
						mc.thePlayer.motionX*=0.5f;
						mc.thePlayer.motionZ*=0.5f;
					}else if (mc.gameSettings.keyBindLeft.getIsKeyPressed() || mc.gameSettings.keyBindRight.getIsKeyPressed()) {

						mc.thePlayer.motionX*=0.7f;
						mc.thePlayer.motionZ*=0.7f;
					}
					
					
					break;
					
					
				case "Verus3":
					

					
					if(mc.thePlayer.onGround) {
						y = mc.thePlayer.posY;
						mc.thePlayer.jump();
						verus3Switch = true;
					}	
					else {

						if(mc.thePlayer.ticksExisted%4==0) {
							verus3Switch = !verus3Switch;
						}
						
						if(mc.thePlayer.ticksExisted%2==0) {
							move.place(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ), 1,
									new ItemStack(Blocks.stone.getItem
										(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
							event.setOnGround(true);
							event.setY(Math.round(mc.thePlayer.posY));
							move.strafe(move.getSpeed()+0.28);
							if(verus3Switch) {
								mc.thePlayer.motionY=0.5;
							}
						}else {
							move.strafe(move.getBaseMoveSpeed()+0.08);
							move.place(new BlockPos(mc.thePlayer.posX,mc.thePlayer.posY - y,mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(mc.theWorld, new BlockPos(0, 0, 0))), 0.5f, 0.5f, 0.5f);
							if(!verus3Switch) {
								mc.thePlayer.motionY=-0.5;
							}
						}
					}
					
					
					break;
				
				case "Test3":
					if(fly.getValue() > 1)
						speed = 1;
					else if (fly.getValue() == 0.25)
						speed = move.getBaseMoveSpeed();
					else
						speed = fly.getValue();
					
					if(mc.gameSettings.keyBindJump.pressed) {
						mc.thePlayer.motionY = speed/2;
					}else if (mc.gameSettings.keyBindSneak.pressed) {
						mc.thePlayer.motionY = (-1*speed)/2;
					}else {
						mc.thePlayer.motionY = 0;
					}
					
					move.setMoveSpeed(speed);
					if(mc.thePlayer.ticksExisted % 10 ==0) {
						layerx = mc.thePlayer.posX;
						layery = mc.thePlayer.posY;
						layerz = mc.thePlayer.posZ;
						//mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(999,999, true, true));
						event.setX(mc.thePlayer.lastTickPosX);
						event.setY(mc.thePlayer.lastTickPosY);
						event.setZ(mc.thePlayer.lastTickPosZ);
						aBoolForBWP = true;
					}else if (aBoolForBWP){
						event.setX(layerx);
						event.setY(layery);
						event.setZ(layerz);
					}
					
					
					
					

					/*if(fly.getValue() > 1)
						speed = 1;
					else if (fly.getValue() == 0.25)
						speed = move.getBaseMoveSpeed();
					else
						speed = fly.getValue();
					
					if(mc.gameSettings.keyBindJump.pressed) {
						mc.thePlayer.motionY = speed/2;
					}else if (mc.gameSettings.keyBindSneak.pressed) {
						mc.thePlayer.motionY = (-1*speed)/2;
					}else {
						mc.thePlayer.motionY = 0;
					}
					
					move.setMoveSpeed(speed);
					event.setOnGround(true);
					if(mc.thePlayer.ticksExisted % 10 ==0) {
						layerx = mc.thePlayer.posX;
						layery = mc.thePlayer.posY;
						layerz = mc.thePlayer.posZ;
						//mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput(999,999, true, true));
						event.setX(mc.thePlayer.lastTickPosX);
						event.setY(mc.thePlayer.lastTickPosY);
						event.setZ(mc.thePlayer.lastTickPosZ);
						aBoolForBWP = true;
					}else if (aBoolForBWP){
						event.setX(layerx);
						event.setY(layery);
						event.setZ(layerz);
					}*/
					
					
					break;
				
				case "Test2":
					
					mc.thePlayer.motionY = 0;
					move.strafe(move.getBaseMoveSpeed());
					
					
					break;
				
				
					case "Funcraft":
						/*
						 * Dont have vers spoof yet dont know if this actually works  :stare:
						 * I literally just skidded the Funcraft mode from my old "FunniFlies" script on LB
						*/
						if(mc.thePlayer.posY > py+0.5) {
							mc.thePlayer.motionY = 0;
							mc.thePlayer.jumpMovementFactor = 0;
							if(!mc.thePlayer.onGround)
								mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.000008, mc.thePlayer.posZ);
							
							if(!mc.thePlayer.isCollidedHorizontally) {
								move.strafe(speed);
							}
							if (speed > 0.30)
								speed -= speed / 250;
						
						}
						
						
						break;
				
					case "Test":
						
						if(mc.thePlayer.onGround) {
							mc.thePlayer.jump();
							move.strafe(move.getBaseMoveSpeed()+0.4);
							gradualTimer = 25;
							timer.reset();
						}else {
							mc.thePlayer.motionY = 0;
							if(gradualTimer>3)
							gradualTimer-=0.16;
							else
								gradualTimer = 3;
							if(gradualTimer>3) 
							mc.timer.timerSpeed = (float) (gradualTimer - Math.random()/10);
							else
								mc.timer.timerSpeed = (float) (3 - Math.random()/10);
							
							
							
						}
						move.strafe();
						
						
						
					break;
					
					
					case "Clip":
						if(clipF.isEnabled()) {
							mc.thePlayer.motionY = 0; mc.thePlayer.motionX =0; mc.thePlayer.motionZ = 0;
						}
						if(clipGsp.isEnabled()) 
							((EventMotion) e).setOnGround(true);
						if(timer.hasTimeElapsed((long) cd.getValue() * 10, true))
							move.packetComedy(clipdi.getValue(), clipdiy.getValue());
						break;
						
						
					case "Ground":
						event.setOnGround(true);
						mc.thePlayer.motionY =0;
						break;
					case "Basic":
						if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {
							mc.thePlayer.motionY = fly.getValue();
						}else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
							mc.thePlayer.motionY = -fly.getValue();
						}else {
							mc.thePlayer.motionY =0;
						}
						break;
							
					
					case "CatchMotion":
						if(py+1.245f < mc.thePlayer.posY) {
							mc.thePlayer.motionY =0;
							move.strafe(fly.getValue());
						}
						break;
					
					case "MineLatinoGood":
						if(mc.thePlayer.onGround && spanishL < 5) {
							move.setMoveSpeed(0);
							move.strafe(0);
							mc.thePlayer.jump();
							event.setOnGround(false);
							spanishL++;
						
						}
						if(spanishL == 5) {
							mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
							event.setOnGround(true);
							spanishtimer =3;
							if(mc.thePlayer.hurtTime>0)
								spanishL++;
						}
						if(spanishL ==6) {
							
								if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {
									mc.thePlayer.motionY = 0.3;
								}else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
									
								}else {
									mc.thePlayer.motionY = 0;
								}
							if(mc.thePlayer.ticksExisted % 20 == 0 && !mc.thePlayer.onGround) {
								mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.1, mc.thePlayer.posZ, false));
					            mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
					            mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
							}else {
								event.setOnGround(true);
							}
							if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime<7) {
								mc.timer.timerSpeed =spanishtimer;
								spanishtimer-=0.0001;
								if(spanishtimer<1) {
									spanishtimer=1;
								}
							}else
								mc.timer.timerSpeed=1;
							
							if(mc.thePlayer.hurtTime>0 && mc.thePlayer.hurtTime <20) {
								move.strafe(0.7);
							}else {
								event.setY(mc.thePlayer.posY - 0.1);
							}
						}
						break;
					case "SpartanPacket1.9":
						spartanishehas = true;
						double expectedX;
					    double expectedY;
					    double expectedZ;
						double x = mc.thePlayer.posX;
				        double y = mc.thePlayer.posY;
				        double z = mc.thePlayer.posZ;
						if(timer.hasTimeElapsed3(150, true)) {
							event.setY(move.getRandomPositiveBoundsY(mc.thePlayer.posY, -10000));
							youMayBeginRapingSpartan = true;
						}

						if(youMayBeginRapingSpartan) {
							event.setOnGround(true);
							if(mc.gameSettings.keyBindForward.pressed ) {
								/*final double yaw = Math.toRadians(mc.thePlayer.rotationYaw);
								expectedX = (x-0.01) + -Math.sin(yaw);
								expectedZ = (z-0.01) + -Math.sin(yaw);
								event.setX(mc.thePlayer.lastTickPosX + expectedX);
								event.setZ(mc.thePlayer.lastTickPosX + expectedZ);*/
								move.packetComedy(0.1, 0);
								move.setMoveSpeed(0);
							}
							if(mc.gameSettings.keyBindJump.pressed) {
								mc.thePlayer.motionY = 0;
								event.setY(mc.thePlayer.posY+5);
								move.packetComedy(0, 2);
							}else if (mc.gameSettings.keyBindSneak.pressed) {
								event.setY(mc.thePlayer.posY - 5);
								move.packetComedy(0, -2);
							}	else if(timer.hasTimeElapsed(500, true)){
								event.setY(move.getRandomPositiveBoundsY(mc.thePlayer.posY, -10000));
								mc.thePlayer.motionY = 0;
							}
							
						}else {
							mc.thePlayer.setPosition(mc.thePlayer.lastTickPosX, mc.thePlayer.lastTickPosY, mc.thePlayer.lastTickPosZ);
							mc.thePlayer.motionY = 0;
							move.setMoveSpeed(0);
						}
						mc.thePlayer.motionY = 0;
						/*event.setOnGround(false);
						if(timer.hasTimeElapsed2(150, true)) {
							event.setY(mc.thePlayer.lastTickPosY + 142);
							mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
							
						}else {
							mc.thePlayer.motionY = 0;
							if(timer.hasTimeElapsed(50, true))
							move.packetComedy(0.1, 6);
						}
						move.packetComedy(0.0,-0.1);
	                    */
	                
						
						break;
					case "Astro":
						if(mc.thePlayer.fallDistance>0 && stopwhen <= 1) {
							mc.thePlayer.motionY = 0.42f;
							mc.thePlayer.fallDistance = 0;
							stopwhen++;
						}
						if(mc.thePlayer.fallDistance>3.5 && !mc.thePlayer.onGround) {
							event.setOnGround(true);
							mc.thePlayer.motionY += 1.3;
							move.setMoveSpeed(0.3);
							mc.thePlayer.fallDistance = 0;
						}
						/*if(stopwhen>10 && mc.thePlayer.onGround) {
							stopwhen = 0;
						}*/
						break;
					
					case "Vulcan":

						if(mc.thePlayer.onGround) {
							py = mc.thePlayer.posY;
							mc.thePlayer.jump();
						}
						
						if(!beginVulcan && time<21) {
							if(mc.thePlayer.posY-1.24>py) {
								beginVulcan = true;
							}
						}
						
						if(beginVulcan && mc.thePlayer.fallDistance<=0) {
							mc.thePlayer.motionY = 0;
							time++;
							event.setOnGround(mc.thePlayer.ticksExisted%2==0);
							
							if(time>20) {
								beginVulcan = false;
								move.SetMomentum(0, 0, 0);
							}
						}
						
						break;
					
					case "AstroBow":
						
						if(bsh == 0) {
							time++;
							int bowSlot= getBowSlot();
							if (bowSlot == -1) {
								Epsilon.addChatMessage("No bow found :(");
								return;
							}
							if (mc.thePlayer.inventory.currentItem != bowSlot) {
		                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bowSlot));
		                    }
		                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventoryContainer.getSlot(bowSlot + 36).getStack(), 0, 0, 0));
		                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ, mc.thePlayer.onGround));
		                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ, mc.thePlayer.onGround));
			                bsh =1;
	
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, -90, mc.thePlayer.onGround));
			                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			                if (mc.thePlayer.inventory.currentItem != bowSlot) {
		                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bowSlot));
		                    }
						} 
						if(bsh == 1) {
							if(mc.thePlayer.hurtTime > 0) {
								bsh = 2;
							}
						}
						if(bsh == 2 && mc.thePlayer.hurtTime >0) {
							mc.thePlayer.motionY = 0.56;
							event.setOnGround(true);
							move.strafe(0.5);
						}
						if(mc.thePlayer.hurtTime<=0 && bsh < 2 ) {
							move.setMoveSpeed(-0.0);
						}
						break;
					
					case "Hawk":
						if(bsh == 0) {
							time++;
							int bowSlot= getBowSlot();
							if (bowSlot == -1) {
								Epsilon.addChatMessage("No bow found :(");
								return;
							}
							if (mc.thePlayer.inventory.currentItem != bowSlot) {
		                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bowSlot));
		                    }
		                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventoryContainer.getSlot(bowSlot + 36).getStack(), 0, 0, 0));
		                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ, mc.thePlayer.onGround));
		                    mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(mc.thePlayer.posX,mc.thePlayer.posY,mc.thePlayer.posZ, mc.thePlayer.onGround));
			                bsh =1;
	
							mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, -90, mc.thePlayer.onGround));
			                mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			                if (mc.thePlayer.inventory.currentItem != bowSlot) {
		                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(bowSlot));
		                    }
						} 
						if(bsh == 1) {
							if(mc.thePlayer.hurtTime > 0) {
								bsh = 2;
							}
						}
						if(bsh == 2 && mc.thePlayer.hurtTime >0) {
							mc.thePlayer.motionY = 0.56;
							move.setMoveSpeed(1.5);
						}
						if(mc.thePlayer.hurtTime<=0 && bsh < 2 ) {
							move.setMoveSpeed(-0.0);
						}
						break;
					case "KauriInfinite":
						if(mc.thePlayer.ticksExisted % 2==0) {
							kauristage++;
							
						}
						if(!mc.thePlayer.onGround) {
							if(kauristage ==1) {
								if(layery > mc.thePlayer.posY) {mc.thePlayer.jump();}
								move.setMoveSpeed(0.3);
							}else if(kauristage == 2){
								if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {
									mc.thePlayer.motionY += 0.2;
									layery = mc.thePlayer.posY;
								}else if (mc.gameSettings.keyBindSneak.getIsKeyPressed()){
								mc.thePlayer.motionY -= 0.2;
								layery = mc.thePlayer.posY;
								}else {
									mc.thePlayer.motionY = 0;
								}
							}else if (kauristage ==3) {
								if(layery < mc.thePlayer.posY) {
									mc.thePlayer.motionY = -0.6;
								}
								mc.thePlayer.motionY = 0.0;
								
							}else {
								kauristage = 1;
							}
						}else {
							mc.thePlayer.jump();
							move.setMoveSpeed(0.3);
						}
						break;
					case "Kauri":
						if(mc.thePlayer.ticksExisted % 2 ==0) kauristage++;
						
						if(kauristage == 1) {
							mc.thePlayer.motionY = 0.1f;
						}
						if(kauristage == 2) {
							mc.thePlayer.motionY = 0;
							move.setMoveSpeed(0.2);
						}
						if(kauristage == 3) {
							if(mc.gameSettings.keyBindJump.pressed) {
							mc.thePlayer.jump();
							move.setMoveSpeed(0.2);
							}else if (mc.gameSettings.keyBindSneak.pressed) {
								mc.thePlayer.motionY -= 0.1;
							}else {
								mc.thePlayer.motionY -= 0.0;
							}
						}
						
						if(kauristage ==4) {
							move.setMoveSpeed(0.9);
							mc.thePlayer.motionY = 0.1;
						}	
						if(kauristage ==5) {
							mc.thePlayer.motionY += 0.01;
						}
						if(kauristage >= 6) {
							kauristage = 1;
						}
						break;
						
					case "Collide":
						
						
						if (!mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
		                    
	
		                    if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
		                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY + 0.2));
		                    } else {
		                        mc.thePlayer.motionY = -(mc.thePlayer.posY - roundToOnGround(mc.thePlayer.posY));
		                    }
	
		                    if (mc.thePlayer.posY % (1.0F / 64.0F) < 0.005) {
								event.setOnGround(true);
		                    }
		                }
						break;
					case "CancelY": 
						if(mc.thePlayer.fallDistance > 0)
							if(timer.hasTimeElapsed2((long) yd.getValue(), true)) 
								mc.thePlayer.motionY = 0;
						break;
					case "Vanilla":
							if(mc.gameSettings.keyBindJump.pressed) {
								mc.thePlayer.motionY = flyv.getValue()/2;
							}else if (mc.gameSettings.keyBindSneak.pressed) {
								mc.thePlayer.motionY = (-1*flyv.getValue())/2;
							}else {
								mc.thePlayer.motionY = 0;
							}
							move.setMoveSpeed(fly.getValue());
							if(!mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.pressed && !mc.gameSettings.keyBindSneak.pressed) {
								mc.thePlayer.motionY = -glide.getValue();
							}	
							
							if(!move.isMoving())
								move.SetMomentum(0, mc.thePlayer.motionY, 0);
							
							break;
					
					case "Matrix":
						
						mc.thePlayer.motionY = 0;

						if(matrixFactorMode.getMode() == "Gradual") {
							if(matrixtimer>0.9)
							matrixtimer -= matrixReduce.getValue();
							
							if(matrixtimer<=1)
								mc.timer.timerSpeed = 1;
							else
								mc.timer.timerSpeed = matrixtimer;
						}else 
							mc.timer.timerSpeed = matrixtimer;
						
						
						
						
						
						
						
						break;
					case "Verus":
						move.strafe();
		                if (!mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
		                    if (mc.gameSettings.keyBindJump.getIsKeyPressed()) {
		                        if (mc.thePlayer.ticksExisted%2==0)
		                            mc.thePlayer.motionY = 0.42f;
		                    } else {
		                        if (mc.thePlayer.onGround) {
		                            mc.thePlayer.jump();
		                            //if(!mc.gameSettings.keyBindBack.getIsKeyPressed())
			                           // move.strafe(move.getBaseMoveSpeed()+0.25); removed cuz even tho faster it fucks up when u hold space and forward
		                        }

		                        if (mc.thePlayer.fallDistance > 1) {
		                            mc.thePlayer.motionY = -((mc.thePlayer.posY) - Math.floor(mc.thePlayer.posY));
		                        }

		                        if (mc.thePlayer.motionY == 0) {
		                            mc.thePlayer.jump();
		                            //if(!mc.gameSettings.keyBindBack.getIsKeyPressed())
		                            	//move.strafe(move.getBaseMoveSpeed()+0.25);

		                            mc.thePlayer.onGround = true;
		                            mc.thePlayer.fallDistance = 0;
		                            event.setOnGround(true);
		                        }
		                    }
		                }
						break;
				}	
			}
		}
		if(e instanceof EventUpdate) {
			
		}
	}
	private int getBowSlot() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBow) {
                return i - 36;
            }
        }
        return -1;
    }
	private int getBlockSlot() {
        for(int i = 36; i < 45; ++i) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                return i - 36;
            }
        }
        return -1;
    }

	

}
