package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
import me.robbanrobbin.jigsaw.pathfinding.JigsawPathfinder;
import me.robbanrobbin.jigsaw.pathfinding.MiningController;
import me.robbanrobbin.jigsaw.pathfinding.MiningNodeProcessor;
import me.robbanrobbin.jigsaw.pathfinding.PathfinderResult;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class MinerBot extends Module {
	
	private final static int DEFAULT_SEARCH_AREA = 20;
	
	private EnumFacing facing = EnumFacing.EAST;
	private boolean escaping = false;
	private WaitTimer escapeTimer = new WaitTimer();
	private WaitTimer changeFacingTimer = new WaitTimer();
	private WaitTimer inWaterTimer = new WaitTimer();
	private WaitTimer increaseSearchAreaTimer = new WaitTimer();
	
	private JigsawPathfinder pathFinderMiner = new JigsawPathfinder(mc, 1500, new MiningNodeProcessor());
	
	private MiningController miningController_ITEM;
	private MiningController miningController_XRAY;
	
	private EntityItem currentFoundItem;
	
	private boolean goingUp;
	
	private ArrayList<BlockPos> xrayBlocks = new ArrayList<BlockPos>();
	private BlockPos xrayBlockPos;
	
	private int searchArea = DEFAULT_SEARCH_AREA;
	
	public MinerBot() {
		super("MinerBot", 0, Category.AI, "Mines automatically at a desired block height so you can take a shit and when you come back you got tons of diamonds and you have hopefuly not fallen in lava.");
	}
	
	@Override
	public ModSetting[] getModSettings() {
		double max = (mc.world == null ? 256 : mc.world.getHeight());
		
		return new ModSetting[]{
				new CheckBtnSetting("Emerald", "autoMineEmeralds") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Diamond", "autoMineDiamonds") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Gold", "autoMineGold") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Lapis Lazuli", "autoMineLapisLazuli") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Iron", "autoMineIron") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Redstone", "autoMineRedstone") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new CheckBtnSetting("Coal", "autoMineCoal") {
					@Override
					public void onValueChanged() {
						super.onValueChanged();
						resetXRayPathfinderAndSearchArea();
					}
				},
				new SliderSetting("Mining Height", "autoMineBlockLimit", 0.0, max, 0.0, ValueFormat.DECIMAL),
				new CheckBtnSetting("Randomly Change Direction", "autoMineChangeFacingRandomly"),
				new CheckBtnSetting("Use XRay (Blatant)", "autoMineUseXRay")};
	}
	
	private void resetXRayPathfinderAndSearchArea() {
		miningController_XRAY = null;
		searchArea = DEFAULT_SEARCH_AREA;
		xrayBlockPos = null;
	}
	
	@Override
	public void onEnable() {
		facing = EnumFacing.fromAngle(mc.player.rotationYaw);
		escapeTimer.time = 10000;
		inWaterTimer.time = 10000;
		escaping = false;
		changeFacingTimer.reset();
		currentFoundItem = null;

		xrayBlockPos = null;
		miningController_ITEM = null;
		miningController_XRAY = null;
		increaseSearchAreaTimer.time = 10000;
		super.onEnable();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if (!ClientSettings.autoMineCoal && !ClientSettings.autoMineDiamonds && !ClientSettings.autoMineEmeralds
				&& !ClientSettings.autoMineGold && !ClientSettings.autoMineIron && !ClientSettings.autoMineRedstone
				&& !ClientSettings.autoMineLapisLazuli) {
			return;
		}
//		InventoryCleaner.cleanItem(Item.getItemFromBlock(Blocks.COBBLESTONE)); //TODO fix
		boolean foundOre = false;
		if(ClientSettings.autoMineUseXRay && xrayBlockPos != null) {
			if(isBlockSelected(Utils.getBlock(xrayBlockPos))) {
				foundOre = true;
			}
			else {
				foundOre = false;
				xrayBlockPos = null;
			}
		}
		if(!ClientSettings.autoMineUseXRay) {
			miningController_XRAY = null;
		}
		
		BlockPos toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
				mc.player.posZ).offset(facing);
		
		if(ClientSettings.smoothAim) {
			ClientSettings.smoothAim = false; //disable smooth aiming to not fuck up the looking and stuff
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.WARNING, this.getName() + " disabled Smooth Aiming, because it is not compatible with mining blocks."));
		}
		
		if(mc.player.isInWater()) { //enable escape if in water for some reason
			currentFoundItem = null;
			inWaterTimer.reset();
		}
		
		if(escapeTimer.hasTimeElapsed(2000, false) && inWaterTimer.hasTimeElapsed(5000, false)) {
			if(AutoEat.isEating()) {
				return;
			}
			if(escaping) {
				boolean right = rand.nextBoolean();
				facing = facing.fromAngle(mc.player.rotationYaw + (right ? 90 : -90));
				escaping = false;
			}
			else {
				if(ClientSettings.autoMineChangeFacingRandomly && changeFacingTimer.hasTimeElapsed(60000, true) && rand.nextBoolean()) {
					boolean right = rand.nextBoolean();
					facing = facing.fromAngle(mc.player.rotationYaw + (right ? 90 : -90));
				}
			}
			IBlockState stateUnder = Utils.getBlockState(Utils.getBlockPosRelativeToEntity(mc.player, -0.01d));
			
			if(currentFoundItem == null) {
				for (EntityItem item : Utils.getNearbyItems(6)) {
					if (item.ticksExisted > 30 && item.ticksExisted < 20 * 10) {
						PathfinderResult result = pathFinderMiner.findPath(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3d(item.posX, item.posY, item.posZ)); //is it possible to get to the item?
						if(result == null) {
							continue;
						}
						currentFoundItem = item;
						miningController_ITEM = new MiningController(result) {
							@Override
							public void onFinish() {
								currentFoundItem = null;
							}

							@Override
							protected void onIncrement() {
								
							}
						};
					}
				}
			}
			if (currentFoundItem != null) {
				if(mc.world.getEntityByID(currentFoundItem.getEntityId()) == null || currentFoundItem.ticksExisted > 20 * 10) {
					currentFoundItem = null;
					return;
				}
				if(ClientSettings.autoMineUseXRay) {
					xrayBlockPos = null;
				}
				miningController_ITEM.update(); //update the mining AI
			}
			else {
				if(ClientSettings.autoMineUseXRay && xrayBlockPos == null && increaseSearchAreaTimer.hasTimeElapsed(searchArea * 8, false)) { //scan for an ore to go to using xray mode
					increaseSearchAreaTimer.reset();
					xrayBlocks.clear();
					miningController_XRAY = null;
					
					int scanRange = searchArea;
					
					int alreadyScannedRange = -(DEFAULT_SEARCH_AREA - scanRange);
					
					boolean foundAnyBlocks = false;
					for(int x = -scanRange / 2; x < scanRange / 2; x++) {
						for(int y = -scanRange / 2; y < scanRange / 2; y++) {
							for(int z = -scanRange / 2; z < scanRange / 2; z++) {
								
//								if(x > (-alreadyScannedRange) / 2 && x < (alreadyScannedRange) / 2) {
//									continue;
//								}
//								if(y > (-alreadyScannedRange) / 2 && y < (alreadyScannedRange) / 2) {
//									continue;
//								}
//								if(z > (-alreadyScannedRange) / 2 && z < (alreadyScannedRange) / 2) {
//									continue;
//								}
								
								BlockPos blockPos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
								Block block = Utils.getBlock(blockPos);
								
								if(isBlockSelected(block)) {
									foundAnyBlocks = true;
									xrayBlocks.add(blockPos);
								}
								
							}
						}
					}
					if(!foundAnyBlocks) { //if did not find any blocks at all
						increaseSearchAreaTimer.reset();
						searchArea += DEFAULT_SEARCH_AREA;
						if(searchArea > 100) {
							searchArea = DEFAULT_SEARCH_AREA;
							Jigsaw.getNotificationManager().addNotification(new Notification(Level.ERROR, "MinerBot could not find any matching blocks!"));
						}
						return;
					}
					increaseSearchAreaTimer.reset();
					searchArea = DEFAULT_SEARCH_AREA;
					if(xrayBlocks.isEmpty()) { //if did not find a path to blocks
						return;
					}
					xrayBlocks.sort(new Comparator<BlockPos>() {
						@Override
						public int compare(BlockPos o1, BlockPos o2) {
							double range1 = mc.player.getDistanceSq(o1.getX(), o1.getY(), o1.getZ());
							double range2 = mc.player.getDistanceSq(o2.getX(), o2.getY(), o2.getZ());
							if(range1 > range2) {
								return 1;
							}
							if(range1 < range2) {
								return -1;
							}
							return 0;
						}
					});
					for(BlockPos blockPos : xrayBlocks) {
						PathfinderResult result = pathFinderMiner.findPath(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), Utils.getVec3d(blockPos).addVector(0.5, 0, 0.5));
						if(!result.didFindPath()) {
							continue;
						}
						xrayBlockPos = blockPos;
						break;
					}
					foundOre = true;
				}
				else {
					for (int x = -4; x <= 4; x++) { //find close and raytraced blocks
						for (int y = -3; y <= 5; y++) {
							for (int z = -4; z <= 4; z++) {
								BlockPos blockPos = new BlockPos(mc.player.posX + x, mc.player.posY + y,
										mc.player.posZ + z);
								Block block = Utils.getBlock(blockPos);
								IBlockState state = Utils.getBlockState(blockPos);
								if (state.getMaterial() == Material.AIR) {
									continue;
								}
								double xDist = mc.player.posX - (blockPos.getX() + 0.5);
								double yDist = mc.player.posY - (blockPos.getY() + 0.5);
								double zDist = mc.player.posZ - (blockPos.getZ() + 0.5);
								double distanceToPlayer = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
								if (isBlockSelected(block)) {
									if(distanceToPlayer > 1.2) {
										RayTraceResult trace0 = mc.world.rayTraceBlocks(
												new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
												Utils.getVec3d(blockPos).addVector(0.5, 0.5, 0.5), true,
												false, true);
										if (trace0.getBlockPos() == null) {
											continue;
										}
										BlockPos blockPosTrace = trace0.getBlockPos();
										Block blockTrace = Utils.getBlock(blockPosTrace);
										double dist = Utils.getVec3d(blockPos).distanceTo(Utils.getVec3d(blockPosTrace));
										double dist0 = Math.sqrt(x * x + y * y + z * z);
										if(dist0 >= 1.5) {
											float yaw = Utils.getFacePos(Utils.getVec3d(blockPosTrace))[0];
											yaw = Utils.normalizeAngle(yaw);
											if (yaw == 45) {
												continue;
											}
											if (yaw == -45) {
												continue;
											}
											if (yaw == 135) {
												continue;
											}
											if (yaw == -135) {
												continue;
											}
										}
										if (dist <= 0) {
											toFace = blockPos;
											foundOre = true;
											break;
										}
									}
									else {
										RayTraceResult trace0 = mc.world.rayTraceBlocks(
												new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
												Utils.getVec3d(blockPos).addVector(0.5, 0.5, 0.5), true,
												false, true);
										if (trace0.getBlockPos() == null) {
											continue;
										}
										BlockPos blockPosTrace = trace0.getBlockPos();
										Block blockTrace = Utils.getBlock(blockPosTrace);
										double dist = Utils.getVec3d(blockPos).distanceTo(Utils.getVec3d(blockPosTrace));
										double dist0 = Math.sqrt(x * x + y * y + z * z);
										if(dist0 >= 1.5) {
											float yaw = Utils.getFacePos(Utils.getVec3d(blockPosTrace))[0];
											yaw = Utils.normalizeAngle(yaw);
											if (yaw == 45) {
												continue;
											}
											if (yaw == -45) {
												continue;
											}
											if (yaw == 135) {
												continue;
											}
											if (yaw == -135) {
												continue;
											}
										}
										toFace = blockPosTrace;
										foundOre = true;
										break;
									}
								}
							}
						}
					}
				}
				if((!ClientSettings.autoMineUseXRay && !mc.player.isInWater()) || (ClientSettings.autoMineUseXRay && !mc.player.isInWater() && xrayBlockPos == null)) {
					for(int x = -1; x <= 1; x++) {
						for(int z = -1; z <= 1; z++) {
							for(int y = 0; y <= 1; y++) {
								
								BlockPos blockPos = new BlockPos(mc.player.posX + x, mc.player.posY + y,
										mc.player.posZ + z);
								Block block = Utils.getBlock(blockPos);
								IBlockState state = Utils.getBlockState(blockPos);
								
								if(block instanceof BlockLiquid) {
									foundOre = false;
									
									escapeTimer.reset();
									escaping = true;
									
									
									facing = facing.getOpposite();
									return;
								}
								
							}
						}
					}
				}
				
				if (foundOre && (mc.player.onGround || xrayBlockPos != null)) {
					if(ClientSettings.autoMineUseXRay) { //use pathfinder to go to the found ore
						BlockPos playerBlockPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
						if(xrayBlockPos == null) {
							return;
						}
						if(miningController_XRAY == null) {
							PathfinderResult result = pathFinderMiner.findPath(Utils.getVec3d(playerBlockPos), Utils.getVec3d(xrayBlockPos).addVector(0.5, 0, 0.5));
							if(!result.didFindPath()) {
								xrayBlockPos = null;
								return;
							}
							miningController_XRAY = new MiningController(result) {
								@Override
								protected void onFinish() {
									xrayBlockPos = null;
								}

								@Override
								protected void onIncrement() {
									PathfinderResult result = pathFinderMiner.findPath(Utils.getVec3d(playerBlockPos), Utils.getVec3d(xrayBlockPos).addVector(0.5, 0, 0.5));
									if(!result.didFindPath()) {
										xrayBlockPos = null;
										return;
									}
									miningController_XRAY.update(result);
									increaseSearchAreaTimer.reset();
								}
							};
						}
						miningController_XRAY.update();
					}
					else {
						if (!Utils.isBlockPosAir(toFace)) { //mine close block with normal mode
							mineBlock(toFace);
						}
					}
				} 
				else {
					if (mc.player.posY > ClientSettings.autoMineBlockLimit + 1) {
						if (stateUnder.getMaterial() != Material.AIR) {
							mineBlockUnderPlayer();
						} 
						else if (mc.player.onGround) {
							mc.player.moveFlying(0, 0, 0.5f, 0.1f);
						}
					}
					else {
						if(mc.player.posY < ClientSettings.autoMineBlockLimit - 1) { //get back up, build stairs
							goingUp = true;
							toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ).offset(facing); //block in front of player
							if (Utils.isBlockPosAir(toFace)) { 
								mc.player.moveFlying(0, 0, 0.5f, 0.1f);
								if (mc.player.onGround) {
									toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight() + 1, mc.player.posZ); //block over player
									if (Utils.isBlockPosAir(toFace)) {
										toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight() + 1, mc.player.posZ).offset(facing); //block in front of player, offset 1 Y
										if (Utils.isBlockPosAir(toFace)) {
											if(Utils.isBlockPosAir(new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ).offset(facing))) {
												mc.player.jump();
											}
										}
									}
								}
							}
							Utils.faceBlock(toFace);
							if (!Utils.isBlockPosAir(toFace)) {
								mineBlock(toFace);
							}
						}
						else {
							if(goingUp) {
								if(!mc.player.onGround) {
									mc.player.moveFlying(0, 0, 0.5f, 0.1f);
									return;
								}
								else {
									goingUp = false;
								}
							}
							if (Utils.isBlockPosAir(toFace)) {
								toFace = toFace.down();
								if (isBlockPosSafe(toFace)) {
									if (Utils.isBlockPosAir(toFace) && mc.player.onGround) { //if there is no block to mine in front of the player
										mc.player.moveFlying(0, 0, 1f, 0.1f);
									}
								} 
								else {
									facing = facing.fromAngle(mc.player.rotationYaw + 90);
									toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
											mc.player.posZ).offset(facing);
									if (!isBlockPosSafe(toFace)) {
										facing = facing.fromAngle(mc.player.rotationYaw - 90);
										toFace = new BlockPos(mc.player.posX,
												mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ)
														.offset(facing);
										if (!isBlockPosSafe(toFace.down())) {
											facing = facing.fromAngle(mc.player.rotationYaw + 180);
											toFace = new BlockPos(mc.player.posX,
													mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ)
															.offset(facing);
										}
									}
								}
							}
							if (Utils.isBlockPosAir(toFace)) { //Stop bobbing (up and down is irritating)
								toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
										mc.player.posZ).offset(facing);
							}
							Utils.faceBlock(toFace);
							if (!Utils.isBlockPosAir(toFace)) {
								mineBlock(toFace);
							}
							else {
								if (!Utils.isBlockPosAir(toFace.offset(facing))) {
									mineBlock(toFace.offset(facing));
								}
							}
						}
						
					}
				}
			}
		}
		else {

			miningController_XRAY = null;
			xrayBlockPos = null;
			
			if (mc.player.onGround) {
				if(mc.player.isInWater()) {
					mc.player.moveFlying(0, 0, 0.2f, 0.1f);
				}
				else {
					mc.player.moveFlying(0, 0, 1f, 0.1f);
				}
			}
			if (Utils.isBlockPosAir(toFace)) {
				toFace = toFace.down();
				if (isBlockPosSafe(toFace)) {
					if (Utils.isBlockPosAir(toFace) && mc.player.onGround) { //if there is no block to mine in front of the player
						
					}
				} 
				else {
					facing = facing.fromAngle(mc.player.rotationYaw + 90);
					toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
							mc.player.posZ).offset(facing);
					if (!isBlockPosSafe(toFace)) {
						facing = facing.fromAngle(mc.player.rotationYaw - 90);
						toFace = new BlockPos(mc.player.posX,
								mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ)
										.offset(facing);
						if (!isBlockPosSafe(toFace.down())) {
							facing = facing.fromAngle(mc.player.rotationYaw + 180);
							toFace = new BlockPos(mc.player.posX,
									mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ)
											.offset(facing);
						}
					}
				}
			}
			if (Utils.isBlockPosAir(toFace)) { //Stop bobbing (up and down is irritating)
				toFace = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
						mc.player.posZ).offset(facing);
			}
			Utils.faceBlock(toFace);
			if (!Utils.isBlockPosAir(toFace)) {
				mineBlock(toFace);
			}
			else {
				if (!Utils.isBlockPosAir(toFace.offset(facing))) {
					mineBlock(toFace.offset(facing));
				}
			}
		}
		
		super.onUpdate(event);
	}
	
	public void mineBlockUnderPlayer() {
		BlockPos pos = Utils.getBlockPosRelativeToEntity(mc.player, -0.01d);
		mineBlock(pos);
	}
	
	public void mineBlock(BlockPos pos) {
		Block block = Utils.getBlock(pos);
		if(block instanceof BlockLiquid) {
			return;
		}
		Utils.faceBlock(pos);
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
	}
	
	public boolean isBlockPosSafe(BlockPos pos) {
		return checkBlockPos(pos, 10);
	}
	
	public boolean isBlockSelected(Block block) {
		return  (block == Blocks.EMERALD_ORE && ClientSettings.autoMineEmeralds) ||
				(block == Blocks.DIAMOND_ORE && ClientSettings.autoMineDiamonds) ||
				(block == Blocks.GOLD_ORE && ClientSettings.autoMineGold) ||
				(block == Blocks.LAPIS_ORE && ClientSettings.autoMineLapisLazuli) ||
				(block == Blocks.IRON_ORE && ClientSettings.autoMineIron) ||
				(block instanceof BlockRedstoneOre && ClientSettings.autoMineRedstone) ||
				(block == Blocks.COAL_ORE && ClientSettings.autoMineCoal);
	}
	
	public boolean checkBlockPos(BlockPos pos, int checkHeight) {
		boolean safe = true;
		boolean blockInWay = false;
		int fallDist = 0;
		if(Utils.getBlockState(pos).getMaterial() == Material.LAVA
				|| Utils.getBlockState(pos).getMaterial() == Material.WATER) {
			return false;
		}
		if(Utils.getBlockState(pos.up(1)).getMaterial() == Material.LAVA
				|| Utils.getBlockState(pos.up(1)).getMaterial() == Material.WATER) {
			return false;
		}
		if(Utils.getBlockState(pos.up(2)).getMaterial() == Material.LAVA
				|| Utils.getBlockState(pos.up(2)).getMaterial() == Material.WATER) {
			return false;
		}
		for(int i = 1; i < checkHeight + 1; i++) {
			BlockPos pos2 = pos.down(i);
			IBlockState blockState = Utils.getBlockState(pos2);
			if(blockState.getMaterial() == Material.AIR) {
				if(!blockInWay) {
					fallDist++;
				}
				continue;
			}
			if(!blockInWay) {
				if(blockState.getMaterial() == Material.LAVA
						|| blockState.getMaterial() == Material.WATER) {
					return false;
				}
			}
			if(!blockInWay) {
				blockInWay = true;
			}
		}
		if(fallDist > 2) {
			safe = false;
		}
		return safe;
	}
	
	@Override
	public void onRender() {
		super.onRender();
		
		if(miningController_XRAY == null) {
			return;
		}
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		RenderTools.lineWidth(2);
		RenderTools.color4f(0.3f, 1f, 0.3f, 1f);
		RenderTools.glBegin(3);
		int i = 0;
		for (Vec3d vec : miningController_XRAY.getPositions()) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.x + 0.5, vec.y, vec.z + 0.5));
			i++;
		}
		RenderTools.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		RenderTools.lineWidth(3);
		for (Vec3d vec : miningController_XRAY.getPositions()) {
			drawESP(1f, 0.3f, 0.3f, 1f, vec.x + 0.5, vec.y, vec.z + 0.5);
		}
		Vec3d vec = miningController_XRAY.getPositions().get(miningController_XRAY.getPositionIndex());
		drawESP(1f, 1f, 1f, 1f, vec.x + 0.5, vec.y, vec.z + 0.5);
	}
	
	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.player.width / 2, mc.player.height, red, green,
				blue, alpha);
	}
	
}
