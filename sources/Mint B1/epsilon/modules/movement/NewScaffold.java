package epsilon.modules.movement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.events.Event;
import epsilon.events.listeners.EventMotion;
import epsilon.events.listeners.EventUpdate;
import epsilon.events.listeners.packet.EventAttackPacket;
import epsilon.events.listeners.packet.EventSendPacket;
import epsilon.modules.Module;
import epsilon.modules.skyblock.SkyblockNuker;
import epsilon.settings.setting.BooleanSetting;
import epsilon.settings.setting.ModeSetting;
import epsilon.settings.setting.NumberSetting;
import epsilon.util.BlockUtils;
import epsilon.util.MoveUtil;
import epsilon.util.NewMath;
import epsilon.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class NewScaffold extends Module{


	public ModeSetting config = new ModeSetting ("Mode", "Custom", "Custom", "Hypixel", "NCP","Matrix", "Verus", "Redesky","Legit");
	
	public ModeSetting scaffPlaceEvent = new ModeSetting ("ScaffoldPlaceEvent", "Pre", "Pre", "Post");
	
	public ModeSetting rotation = new ModeSetting ("Rotation", "Normal", "Normal", "Snap", "Legit", "Watchdog", "Center","VulcanExpand", "Custom");
	
	public ModeSetting tower = new ModeSetting ("Tower", "Vanilla", "Vanilla", "Hypixel", "NCP", "OldVerus", "LatestVerus", "Vulcan", "Matrix", "Hades", "Spartan", "Redesky");
	public ModeSetting towerPlaceEvent = new ModeSetting ("TowerPlaceEvent", "Pre", "Pre", "Post");

	public ModeSetting sprint = new ModeSetting ("Sprint", "Always", "Always","InAir", "Legit", "OnGround", "Never");
	
	public ModeSetting clicktype = new ModeSetting ("Click", "Custom", "Custom","Legit", "Jitter", "Butterfly", "Spam", "Drag");
	
	public ModeSetting placewhen = new ModeSetting ("OnlyPlaceWhen", "Always", "Always","Falling", "Rising", "OnGround", "OffGround");
	
	
	public BooleanSetting eagle = new BooleanSetting ("Eagle", true);

	public BooleanSetting legit = new BooleanSetting ("LegitPlace", true);

	public BooleanSetting telly = new BooleanSetting ("AutoJump", true);

	public BooleanSetting pitchChange = new BooleanSetting ("PitchSwitch", true);

	public BooleanSetting sprintbipas = new BooleanSetting ("SprintDisabler", false);
	
	public BooleanSetting swing = new BooleanSetting ("Swing", false);
	
	public BooleanSetting expandsionAllowed = new BooleanSetting ("Expand", false);
	
	public BooleanSetting autojump = new BooleanSetting ("Allow Consumption", true);
	
	public BooleanSetting sameY = new BooleanSetting ("SameY", true);
	
	public BooleanSetting tmove = new BooleanSetting ("TowerMove", true);
	
	public BooleanSetting spoof = new BooleanSetting ("BlockSpoof", true);
	
	public BooleanSetting sword = new BooleanSetting ("AllowSwordHit", true);

	public BooleanSetting consumable = new BooleanSetting ("Allow Consumption", true);

	public BooleanSetting keeprot = new BooleanSetting ("Keep rot", true);
	

	public NumberSetting reach = new NumberSetting ("Reach", 3, 1, 6, 0.5);

	public NumberSetting towerSpeed = new NumberSetting ("VanillaTower", 1, 0.1, 1.5, 0.1);
	
	public NumberSetting minCPS = new NumberSetting ("ClickDelayMS", 50, 1, 250, 10);
	
	public NumberSetting expand = new NumberSetting ("ExpandDist", 0.0, 0.0, 6, 0.2);
	
	public NumberSetting scaffTimer = new NumberSetting ("ScaffoldTimer", 1, 0.1, 10, 0.1);
	
	public NumberSetting towerTimer = new NumberSetting ("Towertimer", 1, 0.1, 10, 0.1);
	
	public NumberSetting failrate = new NumberSetting ("Failrate", 1, 0, 25, 1);
	
	private boolean sprinting, sneaking, spoofingSlotAttack, shouldGoDown, towering;
	
	private int slot, airTicks, fallDist, blockSlot, disableSlot;
	
	private double groundpos, startY, keepy = 0, y;
	
	private float targetYaw, targetPitch, yaw, lastYaw, pitch, lastPitch;
	
	private BlockPos blockPos, blockData, blockFace;
	private BlockData blockdata;
	private final Timer timer = new Timer();
	private final MoveUtil move = new MoveUtil();
	
	
    private List<Vec3> valid = new ArrayList<>();
    private Vec3 targetPos;
	
	List<Block> blacklistedBlocks = Arrays.asList(Blocks.acacia_door, Blocks.acacia_fence, Blocks.acacia_fence_gate, Blocks.acacia_stairs, Blocks.activator_rail,
			Blocks.anvil, Blocks.bed, Blocks.birch_door, Blocks.birch_fence, Blocks.birch_stairs, Blocks.birch_fence_gate, Blocks.brewing_stand, Blocks.brick_stairs,
			Blocks.cactus, Blocks.cake, Blocks.carpet, Blocks.carrots, Blocks.cauldron, Blocks.chest, Blocks.cobblestone_wall, Blocks.cocoa, Blocks.command_block,
			Blocks.crafting_table, Blocks.dark_oak_door, Blocks.dark_oak_fence, Blocks.dark_oak_fence_gate, Blocks.dark_oak_stairs, Blocks.daylight_detector,
			Blocks.daylight_detector_inverted, Blocks.deadbush, Blocks.detector_rail, Blocks.dispenser, Blocks.double_plant, Blocks.double_stone_slab,
			Blocks.double_stone_slab2, Blocks.dragon_egg, Blocks.dropper, Blocks.enchanting_table, Blocks.ender_chest, Blocks.flower_pot, Blocks.furnace,
			Blocks.glass, Blocks.glass_pane, Blocks.golden_rail, Blocks.grass, Blocks.heavy_weighted_pressure_plate, Blocks.iron_bars, Blocks.iron_door,
			Blocks.iron_trapdoor, Blocks.jukebox, Blocks.jungle_door, Blocks.jungle_fence, Blocks.jungle_fence_gate, Blocks.jungle_stairs, Blocks.ladder,
			Blocks.lever, Blocks.light_weighted_pressure_plate, Blocks.noteblock, Blocks.oak_door, Blocks.oak_fence, Blocks.oak_fence_gate, Blocks.oak_stairs,
			Blocks.rail //Finish rest later i gtg
			);
	
	public NewScaffold() {
		super("NewScaffold", Keyboard.KEY_R, Category.PLAYER, "Scaffold but its not utterly shit");
		this.addSettings(config, rotation, tower, towerPlaceEvent,sprint, clicktype, placewhen,
				eagle, legit, telly, pitchChange, sprintbipas, swing, expandsionAllowed, autojump, sameY, tmove, spoof, sword, consumable, keeprot, 
				reach, towerSpeed ,minCPS, expand, scaffTimer, towerTimer, failrate);
	}
	

	public void onEnable() {
		shouldGoDown = mc.gameSettings.keyBindSneak.getIsKeyPressed();
        y = mc.thePlayer.posY;
		valid.clear();
		startY = mc.thePlayer.posY;
		
		if(mc.thePlayer.inventory.currentItem!=getBlockSlot() && getBlockSlot() !=-1) {
			packetSwitch(getBlockSlot());
			mc.thePlayer.inventory.currentItem = getBlockSlot();
		}
		
		if(sprintbipas.isEnabled())
			mc.getNetHandler().sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
		
	}
	public void onDisable() {
		
		mc.timer.timerSpeed = 1;
		
		if(!mc.gameSettings.keyBindSneak.getIsKeyPressed() && sneaking) {
			
			sneaking = false;
			mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
			
		}
		
		if(!mc.gameSettings.keyBindSprint.getIsKeyPressed() && sprinting)
			
			mc.thePlayer.setSprinting(false);
	
		if(getBlockSlot() != disableSlot) {
			
			slotChange(disableSlot, false);
			slot = -1;
			
		}	
		
			
			
		
	}
	
	
	private boolean canPlaceLegit() {
        return ((mc.theWorld.getBlockState(new BlockPos(move.getForwardXZ(+0.25, 0)[2], mc.thePlayer.posY - 1, move.getForwardXZ(+0.25, 0)[3])).getBlock() instanceof BlockAir) && mc.thePlayer.onGround) || !mc.thePlayer.onGround;
	}
	
	public void onEvent(Event e){
		this.displayInfo = config.getMode() + " | " + tower.getMode();
		
		
		if(e instanceof EventAttackPacket) {
			if(e.isPre() ) {
				spoofingSlotAttack = true;
				packetSwitch(mc.thePlayer.inventory.currentItem);
			}
			
			if(e.isPost()) {
    			packetSwitch(getBlockSlot());
			}
			
		}
		
		if(e instanceof EventSendPacket) {

    		Packet p = e.getPacket();
    		
    		if(p instanceof C0BPacketEntityAction) {
    			C0BPacketEntityAction act = (C0BPacketEntityAction) p;
    			
    			if(act.getAction() == C0BPacketEntityAction.Action.START_SPRINTING && sprintbipas.isEnabled())
    				e.setCancelled();
    		}
    		
    		if(p instanceof C07PacketPlayerDigging) {

    			C07PacketPlayerDigging d = (C07PacketPlayerDigging)p;
    			
    		}
    		
    		if(p instanceof C08PacketPlayerBlockPlacement) {
    		}
    		
    		if(p instanceof C09PacketHeldItemChange) {
    			C09PacketHeldItemChange slot = (C09PacketHeldItemChange)p;
    			disableSlot = slot.getSlotId();

				
				if(!spoofingSlotAttack)
					e.setCancelled();
    		}
    		
    		if(p instanceof C02PacketUseEntity) {

    			spoofingSlotAttack = false;
    		}
    		
		}
		
		if(e instanceof EventUpdate) {
			
			
			if(!mc.thePlayer.onGround) 
				airTicks++;
			else {
				airTicks = 0;
				groundpos = mc.thePlayer.posY;
			}	

			if(e instanceof EventUpdate){
				if(e.isPre()){
					switch(sprint.getMode()) {
					
					case "Always":
						
						mc.thePlayer.setSprinting(true);
						
						break;
						
					case "InAir":
						
						mc.thePlayer.setSprinting(!mc.thePlayer.onGround);
						
						break;
						
					case "OnGround":
						
						mc.thePlayer.setSprinting(mc.thePlayer.onGround);
						
						break;
						
					case "Never":
						
						mc.thePlayer.setSprinting(false);
						
						break;
						
					case "Legit":
						
							mc.thePlayer.setSprinting(mc.thePlayer.fallDistance>0 && !mc.thePlayer.onGround);
						
						break;
					
					}
				}
			}
		
			
		}
		if(e instanceof EventMotion){
			EventMotion event = (EventMotion)e;
		
			blockSlot = getBlockSlot();
			final ItemStack c = mc.thePlayer.inventory.getCurrentItem();

            final float Yaw;
            BlockPos postData = null;
			
            if(e.isPost()) {
	            double x = mc.thePlayer.posX;
	            double z = mc.thePlayer.posZ;
	            double y = mc.thePlayer.posY;
	            postData = new BlockPos(x, y-1, z);
            	
            	
            }
	        
            
            	
            	
            
			if((e.isPre() && scaffPlaceEvent.getMode()=="Pre") || (e.isPost() && scaffPlaceEvent.getMode()=="Post")){

				if(mc.thePlayer.inventory.currentItem!=getBlockSlot() && getBlockSlot() !=-1) {
					packetSwitch(getBlockSlot());
					mc.thePlayer.inventory.currentItem = getBlockSlot();
				}
				
				if(eagle.isEnabled() && config.getMode() == "Custom") {
					if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && mc.thePlayer.onGround) {
						mc.gameSettings.keyBindSneak.setKeyPressed(true);
					}else
						mc.gameSettings.keyBindSneak.setKeyPressed(false);
				}
				
	            double x = mc.thePlayer.posX;
	            double z = mc.thePlayer.posZ;
	            

	            if(sameY.isEnabled()){
	            	if((mc.thePlayer.fallDistance > 1.5 + 1* move.getJumpMotion()) || (mc.gameSettings.keyBindJump.getIsKeyPressed() && !(move.getBlockRelativeToPlayer(0, -0.1, 0) instanceof BlockAir)) || (mc.thePlayer.posY-y>2)){
	            		y = mc.thePlayer.posY;
	            	}
	            	for(Module m : Epsilon.modules) {
	            		if(m.name == "Fly" && m.toggled) y = mc.thePlayer.posY;
	            	}
	            }else y = mc.thePlayer.posY;

	            BlockPos target = new BlockPos(x, y-1, z);
	            
	            Block underBlock = mc.theWorld.getBlockState(target).getBlock();

	            BlockData d = getBlockData(target);
	            float[] rot = null;
	            
	            
	            if(d!=null) {
		            
	            	rot = SkyblockNuker.getBlockRotationsP(target, rotation.getMode() != "Snap");
	            	
	            }
	            
	            
	            if(keeprot.isEnabled() && d!=null && rot!=null && !spoofingSlotAttack) {
	            	double random = 0;
	            	if(postData!=null && pitchChange.isEnabled()) {
		            	if((Math.round(target.x) != Math.round(postData.x) && Math.round(target.z) != Math.round(postData.z)) || !mc.thePlayer.onGround) {
		            		random = Math.random();
		            		if(!mc.thePlayer.onGround)
		            			random+=(mc.thePlayer.posY-target.y)/10;
		            	}else
		            		random = 0;
	            	}
	            	
	            	yaw = mc.thePlayer.rotationYawHead;
	            	

            		if(mc.gameSettings.thirdPersonView!=0) {
                	mc.thePlayer.rotationYawHead = rot[0];
    				mc.thePlayer.renderYawOffset = rot[0];
    				mc.thePlayer.rotationPitchHead = rot[1];
	    				if(towering && rotation.getMode()=="Snap") {
	    					rot[1] = 90;
	    				}
            		}
    				event.setYaw(rot[1]);
    				event.setPitch((float) (rot[1] + random));
            	}

	            if (isAirBlock(underBlock) && d != null) {

                	blockdata = d;
                	
                	
                	
                	if(!spoofingSlotAttack) {
                		if(mc.gameSettings.thirdPersonView!=0) {
	                	mc.thePlayer.rotationYawHead = rot[0];
	    				mc.thePlayer.renderYawOffset = rot[0];
	    				mc.thePlayer.rotationPitchHead = rot[1];
                		}
	    				event.setYaw(rot[1]);
	    				event.setPitch(rot[1]);
                	}
    			

                	if(timer.hasTimeElapsed((long) minCPS.getValue(), true) && (legit.isEnabled() && canPlaceLegit()) || !legit.isEnabled()) {
	    				
                		switch(placewhen.getMode()) {
                		
                		case "Always":
                			
                			break;
                			
                		case "Falling":
                			
                			if(mc.thePlayer.fallDistance<=0)
                			return;
                			break;
                			
                		case "Rising":
                			if(mc.thePlayer.fallDistance>0)
                				return;
                			break;
                			
                		case "OnGround":
                			
                			if(!mc.thePlayer.onGround)
                				return;
                			
                			break;
                		
                		case "OffGround":
                			if(mc.thePlayer.onGround)
                				return;
                		
                		break;
                			
                		}
                		if(swing.isEnabled())
                		mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
	                	mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, c, d.position, d.face, getVec3(d.position, d.face));
	                    
	                	if(mc.thePlayer.getHeldItem() == null && getBlockSlot() !=-1) {
	                		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(getBlockSlot()).getStack();
	                		packetSwitch(getBlockSlot());
	                		mc.thePlayer.inventory.setItemStack(itemStack);
	                	}
                	}
                	
                	
	            }
	            
			}
			

            if((e.isPre()&&towerPlaceEvent.getMode() == "Pre") || (e.isPost() && towerPlaceEvent.getMode() == "Post") ) {
            	
            	if(mc.thePlayer.onGround&&telly.isEnabled()&&!mc.gameSettings.keyBindJump.getIsKeyPressed() && move.isMoving()) {
            		mc.thePlayer.jump();
            		
            	}
            	final double x = mc.thePlayer.posX;
	            final double z = mc.thePlayer.posZ;

	            
	            final BlockPos target = new BlockPos(x, y-1, z);

            	if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {

            		y = mc.thePlayer.posY;
            	}
	            if(isAirBlock(mc.theWorld.getBlockState(new BlockPos(x, y-1, z)).getBlock()) && getBlockData(target) != null) {
	            	
	            	if(mc.gameSettings.keyBindJump.getIsKeyPressed()) {

	            		y = mc.thePlayer.posY;
	            		
	            		if((!move.isMoving() && !tmove.isEnabled()) || tmove.isEnabled()) {
	            			if((tower.getMode()=="Hades" || tower.getMode()=="LatestVerus") && mc.thePlayer.ticksExisted%2==0)
	            				event.setOnGround(true);
	            		
	            		performTower();
	            		towering = true;
	            		}
	            	}
	            	else {
	            		towering = false;
	            		mc.timer.timerSpeed = (float) scaffTimer.getValue();
	            	}
	            }
            }
			
		}
	}	
	
	private void packetSwitch(int blockSlot) {
		
		mc.getNetHandler().sendPacketNoEvent(new C09PacketHeldItemChange(blockSlot));
		
	}

	private void placePacket(BlockPos b, int placeDirect, ItemStack itemStack, float faceX, float faceY, float faceZ) {
		if(swing.isEnabled())
			mc.thePlayer.swingItem();
		else 
			mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
		mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(b, placeDirect, itemStack, faceX, faceY, faceZ));
	}
	
	public boolean isAirBlock(Block block) {
        if (block.getMaterial().isReplaceable()) {
        	return !(block instanceof BlockSnow) && block.getBlockBoundsMaxX()>0.125;
        }

        return false;
    }
	
	private void slotChange(final int slot, final boolean packet) {
		if(packet)
			mc.getNetHandler().addToSendQueueWithoutEvent(new C09PacketHeldItemChange(slot));
		else 
			mc.thePlayer.inventory.currentItem = slot;
			
	}
	
	public static int getBlockSlot() {
        for(int i = 36; i < 45; ++i) {
        	
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            
            
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                return i - 36;
            }
        }
        return -1;
    }
	
    
    private Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
	
	public static double randomNumber(double max, double min) {
        return (Math.random() * (max - min)) + min;
    }
	
	
	private Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.5;
        
        double y = pos.getY() + 0.5;
        
        double z = pos.getZ() + 0.5;
        
        x += (double) face.getFrontOffsetX() / 2;
        
        z += (double) face.getFrontOffsetZ() / 2;
        
        y += (double) face.getFrontOffsetY() / 2;
        
        
        return new Vec3(x, y, z);
    }
	
	
	
	/*private class BlockData {

        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }

    }*/
	
	private boolean isPosSolid(final BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if ((block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockSnow)
                && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
            return true;
        }
        return false;
    }

    private BlockData getBlockData(final BlockPos pos) {
    	
        if (isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos1 = pos.add(-1, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos2 = pos.add(1, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos3 = pos.add(0, 0, 1);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos4 = pos.add(0, 0, -1);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos19 = pos.add(-2, 0, 0);
        if (isPosSolid(pos1.add(0, -1, 0))) {
            return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos1.add(-1, 0, 0))) {
            return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos1.add(1, 0, 0))) {
            return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos1.add(0, 0, 1))) {
            return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos1.add(0, 0, -1))) {
            return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos29 = pos.add(2, 0, 0);
        if (isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos39 = pos.add(0, 0, 2);
        if (isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos49 = pos.add(0, 0, -2);
        if (isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos5 = pos.add(0, -1, 0);
        if (isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos6 = pos5.add(1, 0, 0);
        if (isPosSolid(pos6.add(0, -1, 0))) {
            return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos6.add(-1, 0, 0))) {
            return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos6.add(1, 0, 0))) {
            return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos6.add(0, 0, 1))) {
            return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos6.add(0, 0, -1))) {
            return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos7 = pos5.add(-1, 0, 0);
        if (isPosSolid(pos7.add(0, -1, 0))) {
            return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos7.add(-1, 0, 0))) {
            return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos7.add(1, 0, 0))) {
            return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos7.add(0, 0, 1))) {
            return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos7.add(0, 0, -1))) {
            return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos8 = pos5.add(0, 0, 1);
        if (isPosSolid(pos8.add(0, -1, 0))) {
            return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos8.add(-1, 0, 0))) {
            return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos8.add(1, 0, 0))) {
            return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos8.add(0, 0, 1))) {
            return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos8.add(0, 0, -1))) {
            return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
        }
        BlockPos pos9 = pos5.add(0, 0, -1);
        if (isPosSolid(pos9.add(0, -1, 0))) {
            return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
        }
        if (isPosSolid(pos9.add(-1, 0, 0))) {
            return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
        }
        if (isPosSolid(pos9.add(1, 0, 0))) {
            return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
        }
        if (isPosSolid(pos9.add(0, 0, 1))) {
            return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
        }
        if (isPosSolid(pos9.add(0, 0, -1))) {
            return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
        }
        return null;
    }
	
	
	
    
    private void performTower() {
    	
        BlockPos targetPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
        BlockData data = getBlockData(targetPos);

		if(getBlockSlot() == -1)
			return;
		ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(getBlockSlot()).getStack();
		
        
		
		
        if(!(move.getBlockRelativeToPlayer(0, -1, 0) instanceof BlockAir && move.getBlockRelativeToPlayer(0, -2, 0) instanceof BlockAir)) {


    		if(swing.isEnabled())
    		mc.getNetHandler().sendPacketNoEvent(new C0APacketAnimation());
            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, itemStack, targetPos.add(0, -1, 0), EnumFacing.UP, new Vec3(0, 0, 0));

	    	
	    	y = mc.thePlayer.posY;
	    	switch(tower.getMode()) {
	    	
	    	case "Spartan":
	    			mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY+0.9, mc.thePlayer.posZ);
	    		
	    		break;
	    	
	    	case "Vulcan":

                mc.thePlayer.motionY = 0.3F;
	    		break;
	    	
	    	case "Vanilla":
	    		
	    		mc.thePlayer.motionY = towerSpeed.getValue();
	    		
	    		
	    		break;
	    		
	    	case "NCP":
	    		
	    		mc.thePlayer.motionY = 0.42;
                mc.thePlayer.motionX *= 0.55;
                mc.thePlayer.motionZ *= 0.55;
	    		
	    		break;
	    		
	    	case "Hypixel":
	    		
	    		if(!(move.getBlockRelativeToPlayer(0, -0.1, 0) instanceof BlockAir)) {
	    			mc.thePlayer.motionY = 0.42F;
	    		}else {
	    			
	    			mc.thePlayer.jump();
	    			mc.thePlayer.motionX*=0.25;
	    			mc.thePlayer.motionZ*=0.25;
	    			
	    		}
	    		
                
	    		break;
	    		
	    	case "Redesky":

                mc.thePlayer.motionX *= 0.75;
                mc.thePlayer.motionZ *= 0.75;
	    		if(!(move.getBlockRelativeToPlayer(0, 0, 0) instanceof BlockAir)) {
	    			mc.thePlayer.motionY = 0.42;
	    		}else if (!(move.getBlockRelativeToPlayer(0, -1.2, 0) instanceof BlockAir)) {
	    			mc.thePlayer.motionY = -0;
	    			mc.timer.timerSpeed = (float) (towerTimer.getValue()+1);
	    		}
	    		
                
	    		break;

		    	
	    	case "Matrix":
	    		
	    		if(!(move.getBlockRelativeToPlayer(0, 0, 0) instanceof BlockAir)) {
	    			mc.thePlayer.motionY = 0.42;
	    		}
	    		
	    		break;
	    		
	    	case "Hades":
	    		
	    		if(mc.thePlayer.ticksExisted%2==0)
	    			mc.thePlayer.motionY = 0.7;
	    		else
	    			mc.thePlayer.motionY = 0;
	    		
	    		break;
	    		
	    	case "OldVerus":
	    		
	    		if(mc.thePlayer.ticksExisted%2==0)
	    			mc.thePlayer.motionY = 0.42f;
	    		
	    		break;
	    		
	    	case "LatestVerus":
	    		
	    		mc.thePlayer.motionY = 0.5;
	    		
	    		break;
	    	
	    	}
	    	mc.timer.timerSpeed = (float) towerTimer.getValue();
        }else {

	    switch(tower.getMode()) {
	    	case "Spartan":
	    		

	    		break;
	    	}
        }
    	
    }

    private class BlockData {

        public BlockPos position;
        public EnumFacing face;

        private BlockData(BlockPos position, EnumFacing face) {
            this.position = position;
            this.face = face;
        }

    }
    
    
}
