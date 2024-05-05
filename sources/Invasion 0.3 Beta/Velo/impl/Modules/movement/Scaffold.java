package Velo.impl.Modules.movement;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.Other.BlockUtil;
import Velo.api.Util.Other.ChatUtil;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.Other.MovementUtil;
import Velo.api.Util.Other.Timer;
import Velo.api.Util.Render.RenderUtil;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.impl.Event.EventPostMotion;
import Velo.impl.Event.EventPreMotion;
import Velo.impl.Event.EventRender;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.ModeSetting;
import Velo.impl.Settings.NumberSetting;
import Velo.impl.Modules.visuals.ChestESP;
import Velo.impl.Modules.visuals.CustomVelo;
import Velo.impl.Modules.visuals.hud.HUD;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {
	
	public static ModeSetting mode = new ModeSetting("Mode", "Ncp", "Ncp", "Watchdog", "Dev", "Dev2", "Dev3", "Dev4", "Sneak", "Legit", "Cubecraft", "Aac", "Aac2", "Jump");
	public static ModeSetting towermode = new ModeSetting("Tower", "Ncp", "Ncp", "Aac", "Hypixel", "Off");
	public static ModeSetting swingmode = new ModeSetting("SwingMode", "Server", "Client", "Server");
	public static BooleanSetting keepsprint = new BooleanSetting("KeepSprint", true);
	public static BooleanSetting downwards = new BooleanSetting("Downwards", false);
	public static BooleanSetting sprintBypass = new BooleanSetting("Hypixel Sprint Bypass", false);
	public static BooleanSetting slotChnage = new BooleanSetting("Hypixel Slot Change", false);
	public static BooleanSetting esp = new BooleanSetting("ESP", false);
	public static BooleanSetting diagonal = new BooleanSetting("Diagonal", true);
	public static BooleanSetting snap = new BooleanSetting("Snap", false);
	public static BooleanSetting spoofitem = new BooleanSetting("SpoofItem", true);
	public static BooleanSetting safewalk = new BooleanSetting("Safewalk", false);
	public static BooleanSetting extend = new BooleanSetting("Extend", false);
	public static NumberSetting aactimer = new NumberSetting("AacTimer", 1, 0.1, 10, 0.01);
	public static NumberSetting delay = new NumberSetting("Delay", 0, 0, 1000, 1);
	public static NumberSetting turnspeed = new NumberSetting("Hypixel Rotation Turn Amount", 5, 0, 30, 1);
	public static NumberSetting expandamount = new NumberSetting("Expand Amount", 1, 0, 15, 1);
	public Fonts font1 = new Fonts();
	public int ticks = 0;
	public int ticks2 = 0;
	
	
	
	 private List<Block> blacklisted = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava,
	            Blocks.flowing_lava, Blocks.enchanting_table, Blocks.ender_chest, Blocks.yellow_flower, Blocks.carpet,
	            Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.crafting_table, Blocks.snow_layer,
	            Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.torch,
	            Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore,
	            Blocks.lit_redstone_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate,
	            Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button,
	            Blocks.wooden_button, Blocks.cactus, Blocks.lever, Blocks.activator_rail, Blocks.rail, Blocks.detector_rail,
	            Blocks.golden_rail, Blocks.furnace, Blocks.ladder, Blocks.oak_fence, Blocks.redstone_torch,
	            Blocks.iron_trapdoor, Blocks.trapdoor, Blocks.tripwire_hook, Blocks.hopper, Blocks.acacia_fence_gate,
	            Blocks.birch_fence_gate, Blocks.dark_oak_fence_gate, Blocks.jungle_fence_gate, Blocks.spruce_fence_gate,
	            Blocks.oak_fence_gate, Blocks.dispenser, Blocks.sapling, Blocks.tallgrass, Blocks.deadbush, Blocks.web,
	            Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.nether_brick_fence, Blocks.vine,
	            Blocks.double_plant, Blocks.flower_pot, Blocks.beacon, Blocks.pumpkin, Blocks.lit_pumpkin);
	    public static List<Block> blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water,
	            Blocks.lava, Blocks.flowing_lava, Blocks.ender_chest, Blocks.enchanting_table, Blocks.stone_button,
	            Blocks.wooden_button, Blocks.crafting_table, Blocks.beacon);
	//Scaffold variables needed
	
	int slot = 0;
	
 
    
	public float keepYaw;
	public float keepPitch;
	public float animationYaw;
	public float animationPitch;
	
	public static boolean isEnabled = false;
	
	public static boolean SafewalkEnabled;
	
	public Timer timer = new Timer();
	
	public BlockPos blockPos;
	public BlockPos blockPos2;
	public EnumFacing enumFacing;
	
	public double jumpPosY = 0;
	
	public Scaffold() {
		super("Scaffold", "Scaffold " + mode.getMode(), Keyboard.KEY_NONE, Category.MOVEMENT);
		this.loadSettings(mode, towermode, swingmode, diagonal, keepsprint, snap, spoofitem, aactimer, delay, safewalk);
	}
	
	public void onEnable() {
		jumpPosY = mc.thePlayer.posY;
		keepYaw = mc.thePlayer.rotationYaw;
		keepPitch = mc.thePlayer.rotationPitch;
		animationYaw = mc.thePlayer.rotationYaw;
		animationPitch = mc.thePlayer.rotationPitch;
	}
	
	public void onDisable() {
		ticks = 0;
		ticks2 = 0;
		jumpPosY = 0;
		isEnabled = false;
		mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
		mc.playerController.updateController();
		slot = 0;
		keepYaw = mc.thePlayer.rotationYaw;
		keepPitch = mc.thePlayer.rotationPitch;
		animationYaw = mc.thePlayer.rotationYaw;
		animationPitch = mc.thePlayer.rotationPitch;
		mc.timer.timerSpeed = 1;
		SafewalkEnabled = false;
	}
	
	public void onUpdate(EventUpdate event) {
		isEnabled = true;
		this.setDisplayName("Scaffold");
		
		if(safewalk.isEnabled()) {
			SafewalkEnabled = true;
		}
		if(!keepsprint.isEnabled()) {
            mc.thePlayer.setSprinting(false);
            mc.gameSettings.keyBindSprint.pressed = false;
			if(mc.thePlayer.isSprinting()) {
				mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
			}
		}
		

		
		
		if(towermode.equalsIgnorecase("Hypixel")) {
			  if (mc.gameSettings.keyBindJump.isKeyDown()) {
                  if(mc.gameSettings.keyBindJump.pressed) {
                      // if (!mc.thePlayer.isMoving()) {
                      if (!MovementUtil.isOnGround(0.79) || mc.thePlayer.onGround) {
                          mc.thePlayer.motionY = 0.41985;
                      }
                      if(mc.thePlayer.ticksExisted % 75 == 0) {
                          // mc.thePlayer.motionY = -1L;
                          mc.thePlayer.motionY = mc.thePlayer.ticksExisted % 75 == 0 ? -0.019429035780923 : 0.52f;
                          timer.reset();
                      }
                  }
			  }
		}
	}
	
	
@Override
public void onRender3DUpdate(EventRender3D event) {
	
}
	
	public void onPreMotionUpdate(EventPreMotion event) {
		
		
		
		BlockPos pos = null;
		BlockPos pos2 = null;
		
		double x = -Math.sin(mc.thePlayer.getDirection()) * 0;
		double z = Math.cos(mc.thePlayer.getDirection()) * 0;
		
		if(mc.thePlayer.onGround) {
			jumpPosY = mc.thePlayer.posY;
		} else {
			
		}
		
		
	    if (MovementUtil.isMoving()) {
            if (mc.thePlayer.onGround) {
                          }
        } else {
        	  mc.timer.timerSpeed = 1;
        }
	    
	    
	    pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - 1, mc.thePlayer.posZ + z);
		
		blockPos = null;
		enumFacing = null;
		
		if(mc.theWorld.getBlockState(pos).getBlock() instanceof Block) {
			setBlockFacing(pos);
			try {
				if(blockPos != null) {
					if(mode.equalsIgnorecase("Ncp")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						if(snap.isEnabled()) {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								event.setYaw((float) (facing[0]));
								event.setPitch(facing[1] + 9);
							}
						} else {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								keepPitch = facing[1];
								keepYaw = facing[0];
							} else {
								
							}
							
							if (animationYaw > facing[0]) {
								animationYaw = ((animationYaw) - ((animationYaw - facing[0]) / 5));
							}
							else if (animationYaw < facing[0]) {
								animationYaw = ((animationYaw) + ((facing[0] - animationYaw) / 5));
							}
							
							event.setYaw((float) (keepYaw + randomNumber(3, -3)));
							event.setPitch((float) (keepPitch));
							
							mc.thePlayer.renderYawOffset = animationYaw;
							mc.thePlayer.rotationYawHead = animationYaw;
						}
					}
					if(mode.equalsIgnorecase("Dev4")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						if(snap.isEnabled()) {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								event.setYaw((float) (facing[0]));
								event.setPitch(facing[1] + 9);
							}
						} else {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								keepPitch = facing[1];
								keepYaw = facing[0];
							} else {
								
							}
							
							if (animationYaw > facing[0]) {
								animationYaw = ((animationYaw) - ((animationYaw - facing[0]) / 5));
							}
							else if (animationYaw < facing[0]) {
								animationYaw = ((animationYaw) + ((facing[0] - animationYaw) / 5));
							}
							
							event.setYaw((float) (animationYaw + randomNumber(3, -3)));
							event.setPitch((float) (keepPitch));
							
							mc.thePlayer.renderYawOffset = animationYaw;
							mc.thePlayer.rotationYawHead = animationYaw;
						}
					}
					if(mode.equalsIgnorecase("Aac")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(animationPitch);
						
						double diffYaw = 0;
						
						if(mc.thePlayer.isMoving()) {
							if(mc.thePlayer.movementInput.moveForward > 0) diffYaw = 180;
							if(mc.thePlayer.movementInput.moveForward < 0) diffYaw = 0;
						}
						
						if(snap.isEnabled()) {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								event.setYaw((float) (mc.thePlayer.rotationYaw + diffYaw));
								event.setPitch(facing[1]);
							}
						} else {
							event.setYaw((float) (mc.thePlayer.rotationYaw + diffYaw));
							event.setPitch(facing[1]);
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
					}
					
					if(mode.equalsIgnorecase("Aac2")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						
						double diffYaw = 0;
						
						diffYaw = 180;
						if(snap.isEnabled()) {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								event.setYaw((float) (mc.thePlayer.rotationYaw + diffYaw));
								event.setPitch(facing[1]);
							}
						} else {
							event.setYaw((float) (mc.thePlayer.rotationYaw + diffYaw));
							event.setPitch(facing[1]);
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
					}
					
					if(mode.equalsIgnorecase("Aac") || mode.equalsIgnorecase("Aac2")) {
						mc.timer.timerSpeed = (float) aactimer.getValue();
					}
					
					if(mode.equalsIgnorecase("Legit")) {
						
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						
						if(enumFacing == EnumFacing.WEST) {
							keepYaw = -90;
						}
						if(enumFacing == EnumFacing.EAST) {
							keepYaw = 90;
						}
						if(enumFacing == EnumFacing.SOUTH) {
							keepYaw = 180;
						}
						if(enumFacing == EnumFacing.NORTH) {
							keepYaw = 360;
						}
						
						if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
							keepPitch = facing[1];
						}
						
				        float f = mc.gameSettings.mouseSensitivity/2F * 0.6F + 0.2F;
				        float gcd = f * f * f * 1.2F;

				        keepYaw -= keepYaw % gcd;
				        keepPitch -= keepPitch % gcd;
				        
				        event.setYaw(keepYaw);
				        event.setPitch(keepPitch);
				        
				        mc.thePlayer.renderYawOffset = event.getYaw();
				        mc.thePlayer.rotationYawHead = event.getYaw();
					}
					
					if(mode.equalsIgnorecase("Dev")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(facing[1]);
					
						
						if(!snap.isEnabled()) {
							if(enumFacing == EnumFacing.WEST) {
								event.setYaw(-90);
							}
							if(enumFacing == EnumFacing.EAST) {
								event.setYaw(90);
							}
							if(enumFacing == EnumFacing.SOUTH) {
								event.setYaw(180);
							}
							if(enumFacing == EnumFacing.NORTH) {
								event.setYaw(360);
							}
						} else {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								if(enumFacing == EnumFacing.WEST) {
									event.setYaw(-90);
								}
								if(enumFacing == EnumFacing.EAST) {
									event.setYaw(90);
								}
								if(enumFacing == EnumFacing.SOUTH) {
									event.setYaw(180);
								}
								if(enumFacing == EnumFacing.NORTH) {
									event.setYaw(360);
								}
							}
						}
						
						keepYaw = event.getYaw();
						
						if (animationYaw > keepYaw) {
							animationYaw = ((animationYaw) - ((animationYaw - keepYaw) / 5));
						}
						else if (animationYaw < keepYaw) {
							animationYaw = ((animationYaw) + ((keepYaw - animationYaw) / 5));
						}
						
						mc.thePlayer.renderYawOffset = animationYaw;
						mc.thePlayer.rotationYawHead = animationYaw;
					}
					if(mode.equalsIgnorecase("Dev3")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(facing[1]);
					//	event.setYaw(facing[0]);
						
						if(mc.thePlayer.isMovingWhenOnGround() && mc.gameSettings.keyBindJump.isPressed() == false) {
						mc.thePlayer.motionY += 0.44;
						//mc.thePlayer.fakeJump();
					
						mc.timer.timerSpeed = 1.05f;
						}
						if(mc.thePlayer.isMoving()) {
					mc.thePlayer.setSpeed(MovementUtil.getBaseMoveSpeed() + 0.06);
					mc.thePlayer.setSneaking(true);
					
				//		mc.thePlayer.cameraYaw = -1;
				//		mc.thePlayer.cameraPitch = 0.7f;
						}
						if(!snap.isEnabled()) {
							if(enumFacing == EnumFacing.WEST) {
								event.setYaw(-90);
							}
							if(enumFacing == EnumFacing.EAST) {
								event.setYaw(90);
							}
							if(enumFacing == EnumFacing.SOUTH) {
								event.setYaw(180);
							}
							if(enumFacing == EnumFacing.NORTH) {
								event.setYaw(360);
							}
						} else {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								if(enumFacing == EnumFacing.WEST) {
									event.setYaw(-90);
								}
								if(enumFacing == EnumFacing.EAST) {
									event.setYaw(90);
								}
								if(enumFacing == EnumFacing.SOUTH) {
									event.setYaw(180);
								}
								if(enumFacing == EnumFacing.NORTH) {
									event.setYaw(360);
								}
							}
							
							
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
					}
					
					if(mode.equalsIgnorecase("Sneak")) {
						if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
							mc.gameSettings.keyBindSneak.pressed = true;
						} else {
							mc.gameSettings.keyBindSneak.pressed = false;
						}
						
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(facing[1]);
						
						if(!snap.isEnabled()) {
							if(enumFacing == EnumFacing.WEST) {
								event.setYaw(-90);
							}
							if(enumFacing == EnumFacing.EAST) {
								event.setYaw(90);
							}
							if(enumFacing == EnumFacing.SOUTH) {
								event.setYaw(180);
							}
							if(enumFacing == EnumFacing.NORTH) {
								event.setYaw(360);
							}
						} else {
							if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
								if(enumFacing == EnumFacing.WEST) {
									event.setYaw(-90);
								}
								if(enumFacing == EnumFacing.EAST) {
									event.setYaw(90);
								}
								if(enumFacing == EnumFacing.SOUTH) {
									event.setYaw(180);
								}
								if(enumFacing == EnumFacing.NORTH) {
									event.setYaw(360);
								}
							}
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
					}
					if(mode.equalsIgnorecase("Dev2")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(facing[1]);
						
						keepYaw += 180;
						
						if(enumFacing == EnumFacing.WEST) {
							event.setYaw(-90 - keepYaw);
						}
						if(enumFacing == EnumFacing.EAST) {
							event.setYaw(90 - keepYaw);
						}
						if(enumFacing == EnumFacing.SOUTH) {
							event.setYaw(180 - keepYaw);
						}
						if(enumFacing == EnumFacing.NORTH) {
							event.setYaw(360 - keepYaw);
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
						
					}
					
					if(mode.equalsIgnorecase("Cubecraft")) {
						float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
						event.setPitch(facing[1]);
						
						event.setYaw((float) (facing[0]));
					}
					if(mode.equalsIgnorecase("Watchdog")) {
						float facing[] = BlockUtil.getWatchdogRotations(blockPos, enumFacing);
						
						mc.timer.timerSpeed = 0.8f;
						if(mc.theWorld.getBlockState(pos).getBlock() instanceof BlockAir) {
							keepPitch = facing[1];
							keepYaw = facing[0];
							event.setPitch(keepPitch);					
							event.setYaw(keepYaw);
						}
						
						mc.thePlayer.renderYawOffset = event.getYaw();
						mc.thePlayer.rotationYawHead = event.getYaw();
					}
					CustomVelo.lastTickPitch = CustomVelo.pitch;
					CustomVelo.pitch = event.getPitch();
				}
			} catch(NullPointerException e) {
				
			}
		}
	}
	
	public void onPostMotionUpdate(EventPostMotion event) {
		if(blockPos != null) {
			if(mode.equalsIgnorecase("Cubecraft")) {
				float facing[] = BlockUtil.getDirectionToBlock(blockPos.getX(), blockPos.getY(), blockPos.getZ(), enumFacing);
				BlockPos pos = Velo.api.Util.Other.RaycastUtil.rayCast(1, facing[0], facing[1]);
				
				if(pos != null) {
					System.out.println(pos);
				}
			}
			try {
				if(spoofitem.isEnabled()) {
					 if (this.isHotbarEmpty()) {
			                for (slot = 9; slot < 36; ++slot) {
			                    Item item;
			                    if (!mc.thePlayer.inventoryContainer.getSlot(slot).getHasStack() || !((item = mc.thePlayer.inventoryContainer.getSlot(slot).getStack().getItem()) instanceof ItemBlock)) continue;
			                    this.swap(slot, 7);
			                    break;
			                }
			         }
					 slot = 36;
			            while (slot < 45) {
			                Item item;
			                ItemStack is;
			                if (mc.thePlayer.inventoryContainer.getSlot(slot).getHasStack() && (item = (is = mc.thePlayer.inventoryContainer.getSlot(slot).getStack()).getItem()) instanceof ItemBlock) {
			                    int last = mc.thePlayer.inventory.currentItem;
			                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot - 36));
			                    mc.thePlayer.inventory.currentItem = slot - 36;
			                    mc.playerController.updateController();
								if(timer.hasTimedElapsed((long) delay.getValue(), false)) {
									if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, enumFacing, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))) {
										if(swingmode.equalsIgnorecase("Client")) {
											mc.thePlayer.swingItem();
										}
										if(swingmode.equalsIgnorecase("Server")) {
											mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
										}
										if(towermode.equalsIgnorecase("Ncp")) {
											if(mc.gameSettings.keyBindJump.pressed) {
												mc.thePlayer.fakeJump();
												//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ, false));
												if(!mc.thePlayer.isMoving()) {
													mc.thePlayer.motionY = 0.42;
												} else {
													if(mc.thePlayer.onGround) {
														jumpPosY = mc.thePlayer.posY;
													}
													mc.thePlayer.onGround = false;
													mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.STOP_SPRINTING));
													mc.thePlayer.motionY = 0.42;
													if(mc.thePlayer.posY > jumpPosY + 0.05) {
														mc.thePlayer.motionY = -0.42;
													}
												}
												//jumpPosY = mc.thePlayer.posY - 1;
											} else if(!mc.gameSettings.keyBindJump.pressed) {
												if(!Speed.isEnabled) {
													mc.thePlayer.motionY = -0.42;
												}
											}
										}
										if(towermode.equalsIgnorecase("Aac")) {
											if(mc.gameSettings.keyBindJump.pressed) {
												mc.thePlayer.fakeJump();
												if(mc.thePlayer.onGround) {
													mc.thePlayer.motionY = 0.41;
												} else if(mc.thePlayer.motionY > 0.1) {
													mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
													mc.thePlayer.motionY = 0.42f;
												}
											} else if(!mc.gameSettings.keyBindJump.pressed) {
												
											}
										}
										timer.reset();
									}
								} else {
									
								}
			                    mc.thePlayer.inventory.currentItem = last;
			                    mc.playerController.updateController();
			                    return;
			                }
			                ++slot;
			            }
			            return;
				} else {
					if(timer.hasTimedElapsed((long) delay.getValue(), false)) {
						if(mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), blockPos, enumFacing, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))) {
							if(swingmode.equalsIgnorecase("Client")) {
								mc.thePlayer.swingItem();
							}
							if(swingmode.equalsIgnorecase("Server")) {
								mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
							}
							if(towermode.equalsIgnorecase("Ncp")) {
								if(mc.gameSettings.keyBindJump.pressed) {
									mc.thePlayer.fakeJump();
									mc.thePlayer.motionY = 0.42;
								} else if(!mc.gameSettings.keyBindJump.pressed) {
									if(!Speed.isEnabled) {
										mc.thePlayer.motionY = -0.42;
									}
								}
							}
							if(towermode.equalsIgnorecase("Aac")) {
								if(mc.gameSettings.keyBindJump.pressed) {
									mc.thePlayer.fakeJump();
									if(mc.thePlayer.onGround) {
										mc.thePlayer.motionY = 0.41;
									} else if(mc.thePlayer.motionY > 0.1) {
										mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
										mc.thePlayer.motionY = 0.41f;
									}
								} else if(!mc.gameSettings.keyBindJump.pressed) {
									
								}
							}
							timer.reset();
						}
					} else {
						
					}
				}
			} catch(NullPointerException e) {
				
			}
		}
	}
	
	public void onRenderUpdate(EventRender event) {
		
	}
	
	public void setBlockFacing(BlockPos pos) {
		if(diagonal.isEnabled()) {
			if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, -1, 0);
				enumFacing = EnumFacing.UP;
			} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(-1, 0, 0);
				enumFacing = EnumFacing.EAST;
			} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(1, 0, 0);
				enumFacing = EnumFacing.WEST;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, 0, -1);
				enumFacing = EnumFacing.SOUTH;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, 0, 1);
				enumFacing = EnumFacing.NORTH;
			}
			else if (mc.theWorld.getBlockState(pos.add(-1, 0, -1)).getBlock() != Blocks.air) {
				enumFacing = EnumFacing.EAST;
				this.blockPos = pos.add(-1, 0, -1);
			} else if (mc.theWorld.getBlockState(pos.add(1, 0, 1)).getBlock() != Blocks.air) {
				enumFacing = EnumFacing.WEST;
				this.blockPos = pos.add(1, 0, 1);
			} else if (mc.theWorld.getBlockState(pos.add(1, 0, -1)).getBlock() != Blocks.air) {
				enumFacing = EnumFacing.SOUTH;
				this.blockPos = pos.add(1, 0, -1);
			} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 1)).getBlock() != Blocks.air) {
				enumFacing = EnumFacing.NORTH;
				this.blockPos = pos.add(-1, 0, 1);
			}
			else if (mc.theWorld.getBlockState(pos.add(0, -1, 1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, -1, 1);
				enumFacing = EnumFacing.UP;
			}
			else if (mc.theWorld.getBlockState(pos.add(0, -1, -1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, -1, -1);
				enumFacing = EnumFacing.UP;
			}
			else if (mc.theWorld.getBlockState(pos.add(1, -1, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(1, -1, 0);
				enumFacing = EnumFacing.UP;
			}
			else if (mc.theWorld.getBlockState(pos.add(-1, -1, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(-1, -1, 0);
				enumFacing = EnumFacing.UP;
			}
			else {
				blockPos = null;
				enumFacing = null;
			}
		} else {
			if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, -1, 0);
				enumFacing = EnumFacing.UP;
			} else if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(-1, 0, 0);
				enumFacing = EnumFacing.EAST;
			} else if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(1, 0, 0);
				enumFacing = EnumFacing.WEST;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, 0, -1);
				enumFacing = EnumFacing.SOUTH;
			} else if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air) {
				this.blockPos = pos.add(0, 0, 1);
				enumFacing = EnumFacing.NORTH;
			} else {
				blockPos = null;
				enumFacing = null;
			}
		}
	}
	
	public static boolean isPosSolid(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
        if ((block.getMaterial().isSolid() || !block.isTranslucent() || block instanceof BlockLadder || block instanceof BlockCarpet
                || block instanceof BlockSnow || block instanceof BlockSkull)
                && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
            return true;
        }
        return false;
    }
	
	public void onEventSendPacket(EventSendPacket event) {
		
		

			if(keepsprint.isEnabled()) {
				if(this.sprintBypass.isEnabled()) {
					 if(event.getPacket() instanceof S08PacketPlayerPosLook) {
	                    }
	                    if (event.getPacket() instanceof C0BPacketEntityAction) {
	                        event.setCancelled(true);
	                        mc.getNetHandler().addToSendQueueSilent(event.getPacket());
	                    }
				}
			}
		
		
	}
	   public static int randomNumber(int max, int min) {
		      return Math.round(min + (float)Math.random() * (max - min));
		    }
	
	protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private boolean isHotbarEmpty() {
        for (int i = 36; i < 45; ++i) {
            Item item;
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem()) instanceof ItemBlock)) continue;
            return false;
        }
        return true;
    }
    
    public int getBlockCount() {
        int blockCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) continue;
            ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            Item item = is.getItem();
            if (!(is.getItem() instanceof ItemBlock)) continue;
            blockCount += is.stackSize;
        }
        return blockCount;
    }
}
