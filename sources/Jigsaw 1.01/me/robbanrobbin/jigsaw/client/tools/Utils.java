package me.robbanrobbin.jigsaw.client.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.AutoBlock;
import me.robbanrobbin.jigsaw.client.modules.Criticals;
import me.robbanrobbin.jigsaw.client.modules.target.AuraUtils;
import me.robbanrobbin.jigsaw.client.modules.target.Team;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.pathfinding.FlyingNodeProcessor;
import me.robbanrobbin.jigsaw.pathfinding.JigsawPathfinder;
import me.robbanrobbin.jigsaw.pathfinding.MineplexTpAuraNodeProcessor;
import me.robbanrobbin.jigsaw.pathfinding.Node;
import me.robbanrobbin.jigsaw.pathfinding.NodeProcessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialTransparent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.src.Reflector;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Utils {

	private static Minecraft mc = Minecraft.getMinecraft();
	private static Random rand = new Random();
	
	public static boolean spectator;
	
	public static ArrayList<Entity> blackList = new ArrayList<Entity>();
	
	static double x;
	static double y;
	static double z;
	static double xPreEn;
	static double yPreEn;
	static double zPreEn;
	static double xPre;
	static double yPre;
	static double zPre;
	
//	static ArrayList<Vec3> positions = new ArrayList<Vec3>();
//	static ArrayList<Vec3> positionsBack = new ArrayList<Vec3>();
	
	public static TeleportResult pathFinderTeleportTo_MINEPLEX(Vec3d from, Vec3d to) {

		
		NodeProcessor processor = new MineplexTpAuraNodeProcessor();
		
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
		ArrayList<Node> triedPaths = new ArrayList<Node>();
//		System.out.println(to.toString());
		BlockPos targetBlockPos = new BlockPos(Utils.getBlockPos(to));
		BlockPos fromBlockPos = Utils.getBlockPos(from);
		
		BlockPos finalBlockPos = targetBlockPos;
//		boolean passable = true;
//		if(!processor.isPassable(Utils.getBlockState(targetBlockPos))) {
//			finalBlockPos = targetBlockPos.up();
//			boolean lastIsPassable;
//			if(!(lastIsPassable = processor.isPassable(Utils.getBlockState(targetBlockPos.up())))) {
//				finalBlockPos = targetBlockPos.up(2);
//				if(!lastIsPassable) {
//					passable = false;
//				}
//			}
//		}
		
		processor.getPath(new BlockPos(from.x, from.y, from.z), finalBlockPos, ClientSettings.pathfinderMaxComputations);
		triedPaths = processor.triedPaths;
		if(processor.path.isEmpty()) {
			return new TeleportResult(positions, null, triedPaths, null, null, false);
		}
		Vec3d lastPos = null;
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		Node preNode = null;
		for(Node node : processor.path) {
			if(preNode == null) {
				preNode = new Node(true, Utils.getBlockPos(from));
			}
			for(float i = 0; i < 1; i += 0.07) {
				BlockPos prePos = preNode.getBlockpos();
				BlockPos thisPos = node.getBlockpos();
				
				double x = (thisPos.getX() - prePos.getX()) * i;
				double y = (thisPos.getY() - prePos.getY()) * i;
				double z = (thisPos.getZ() - prePos.getZ()) * i;
				y = Math.ceil(y);
				
				double posX = prePos.getX() + 0.5 + x;
				double posY = prePos.getY() + y;
				double posZ = prePos.getZ() + 0.5 + z;
				
				sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(posX, posY - 1, posZ), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0f, 1f, 0f));
				sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
				positions.add((lastPos = new Vec3d(posX, posY, posZ)));
			}
			preNode = node;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, null, triedPaths, processor.path, lastPos, true);
	}
	
	public static TeleportResult pathFinderTeleportBack_MINEPLEX(ArrayList<Vec3d> positions) {
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		ArrayList<Vec3d> positionsBack = new ArrayList<Vec3d>();
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = positions.size() - 1; i > -1; i--) {
			sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(positions.get(i).x, positions.get(i).y - 1, positions.get(i).z), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0f, 1f, 0f));
			sendPacket(new CPacketPlayer.Position(positions.get(i).x, positions.get(i).y, positions.get(i).z, true));
			positionsBack.add(positions.get(i));
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, positionsBack, null, null, null, false);
	}

	public static TeleportResult pathFinderTeleportTo(Vec3d from, Vec3d to) {

		
		NodeProcessor processor = new FlyingNodeProcessor();
		
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
		ArrayList<Node> triedPaths = new ArrayList<Node>();
//		System.out.println(to.toString());
		BlockPos targetBlockPos = new BlockPos(Utils.getBlockPos(to));
		BlockPos fromBlockPos = Utils.getBlockPos(from);
		
		BlockPos finalBlockPos = targetBlockPos;
		boolean passable = true;
		if(!processor.isPassable(Utils.getBlockState(targetBlockPos))) {
			finalBlockPos = targetBlockPos.up();
			boolean lastIsPassable;
			if(!(lastIsPassable = processor.isPassable(Utils.getBlockState(targetBlockPos.up())))) {
				finalBlockPos = targetBlockPos.up(2);
				if(!lastIsPassable) {
					passable = false;
				}
			}
		}
		
		processor.getPath(new BlockPos(from.x, from.y, from.z), finalBlockPos, ClientSettings.pathfinderMaxComputations);
		triedPaths = processor.triedPaths;
		if(processor.path.isEmpty()) {
			return new TeleportResult(positions, null, triedPaths, null, null, false);
		}
		Vec3d lastPos = null;
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for(Node node : processor.path) {
			BlockPos pos = node.getBlockpos();
			sendPacket(new CPacketPlayer.Position(node.getBlockpos().getX() + 0.5, node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
			positions.add((lastPos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)));
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, null, triedPaths, processor.path, lastPos, true);
	}
	
	public static TeleportResult pathFinderTeleportBack(ArrayList<Vec3d> positions) {
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		ArrayList<Vec3d> positionsBack = new ArrayList<Vec3d>();
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = positions.size() - 1; i > -1; i--) {
			sendPacket(new CPacketPlayer.Position(positions.get(i).x, positions.get(i).y, positions.get(i).z, true));
			positionsBack.add(positions.get(i));
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, positionsBack, null, null, null, false);
	}
	
	private static void sendPacket(Packet packet) {
		mc.getConnection().getNetworkManager().sendPacket(packet);
	}
	
	public static boolean infiniteReach(double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3d> positionsBack, ArrayList<Vec3d> positions, EntityLivingBase en) {
		
		int ind = 0;
		xPreEn = en.posX;
		yPreEn = en.posY;
		zPreEn = en.posZ;
		xPre = mc.player.posX;
		yPre = mc.player.posY;
		zPre = mc.player.posZ;
		boolean attack = true;
		boolean up = false;
		boolean tpUpOneBlock = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();

		positions.clear();
		positionsBack.clear();
		
		//preInfiniteReach(range, maxXZTP, maxYTP, positionsBack, positions, new Vec3(en.posX, en.posY, en.posZ), tpStraight, up, attack, tpUpOneBlock, sneaking);
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		RayTraceResult rayTrace = null;
		RayTraceResult rayTrace1 = null;
		RayTraceResult rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
				new Vec3d(en.posX, en.posY, en.posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
						new Vec3d(en.posX, en.posY + mc.player.getEyeHeight(), en.posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
					new Vec3d(en.posX, mc.player.posY, en.posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
									mc.player.posZ),
							new Vec3d(en.posX, mc.player.posY + mc.player.getEyeHeight(), en.posZ), false, false,
							true)) != null) {
				RayTraceResult trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.player.posY;
					// yPreEn = mc.player.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							RayTraceResult tr = rayTracePos(
									new Vec3d(mc.player.posX, target.getY() + i, mc.player.posZ),
									new Vec3d(en.posX, target.getY() + i, en.posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							IBlockState blockState = getBlockState(blockPos);
							Block block = blockState.getBlock();
							if (blockState.getMaterial() != Material.AIR) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.player.posX - xPreEn;
						double difZ = mc.player.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				RayTraceResult ent = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
						new Vec3d(en.posX, en.posY, en.posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.player.posY;
					yPreEn = mc.player.posY;
				} else {
					y = mc.player.posY;
					yPreEn = en.posY;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.player.posX;
				y = yPreEn;
				z = mc.player.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - en.posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP && mc.player.getDistanceToEntity(en) >= 4) {
					x = xPreEn;
					y = en.posY;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					attackInf(en);
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).x;
				y = positions.get(i).y;
				z = positions.get(i).z;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.player.posX;
		y = mc.player.posY;
		z = mc.player.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}
	
	public static boolean infiniteReach(double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3d> positionsBack, ArrayList<Vec3d> positions, BlockPos targetBlockPos) {
		positions.clear();
		positionsBack.clear();
		boolean tpUpOneBlock = false;
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		int ind = 0;
		double posX = ((double)targetBlockPos.getX()) + 0.5;
		double posY = ((double)targetBlockPos.getY()) + 1.0;
		double posZ = ((double)targetBlockPos.getZ()) + 0.5;
		xPreEn = posX;
		yPreEn = posY;
		zPreEn = posZ;
		xPre = mc.player.posX;
		yPre = mc.player.posY;
		zPre = mc.player.posZ;
		boolean attack = true;
		boolean up = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		RayTraceResult rayTrace = null;
		RayTraceResult rayTrace1 = null;
		RayTraceResult rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
				new Vec3d(posX, posY, posZ), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
						new Vec3d(posX, posY + mc.player.getEyeHeight(), posZ), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
					new Vec3d(posX, mc.player.posY, posZ), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
									mc.player.posZ),
							new Vec3d(posX, mc.player.posY + mc.player.getEyeHeight(), posZ), false, false,
							true)) != null) {
				RayTraceResult trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.player.posY;
					// yPreEn = mc.player.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							RayTraceResult tr = rayTracePos(
									new Vec3d(mc.player.posX, target.getY() + i, mc.player.posZ),
									new Vec3d(posX, target.getY() + i, posZ), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							IBlockState blockState = getBlockState(blockPos);
							Block block = mc.world.getBlockState(blockPos).getBlock();
							if (blockState.getMaterial() != Material.AIR) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.player.posX - xPreEn;
						double difZ = mc.player.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				RayTraceResult ent = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
						new Vec3d(posX, posY, posZ), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.player.posY;
					yPreEn = mc.player.posY;
				} else {
					y = mc.player.posY;
					yPreEn = posY;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.player.posX;
				y = yPreEn;
				z = mc.player.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - posY;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP && mc.player.getDistance(posX, posY, posZ) >= 4) {
					x = xPreEn;
					y = posY;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					mc.player.swingArm(EnumHand.MAIN_HAND);
					mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
					mc.getConnection().getNetworkManager().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).x;
				y = positions.get(i).y;
				z = positions.get(i).z;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.player.posX;
		y = mc.player.posY;
		z = mc.player.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}

	public static boolean infiniteReach(Vec3d src, Vec3d dest, double range, double maxXZTP, double maxYTP, 
			ArrayList<Vec3d> positionsBack, ArrayList<Vec3d> positions) {
		positions.clear();
		positionsBack.clear();
		boolean tpUpOneBlock = false;
		double step = maxXZTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			// Jigsaw.chatMessage(maxXZTP * steps);
			if (maxXZTP * steps > range) {
				break;
			}
		}
		int ind = 0;
		xPreEn = dest.x;
		yPreEn = dest.y;
		zPreEn = dest.z;
		xPre = mc.player.posX;
		yPre = mc.player.posY;
		zPre = mc.player.posZ;
		boolean attack = true;
		boolean up = false;

		// If something in the way
		boolean hit = false;
		boolean tpStraight = false;
		boolean sneaking = mc.player.isSneaking() || Jigsaw.getModuleByName("AutoSneak").isToggled();
		RayTraceResult rayTrace = null;
		RayTraceResult rayTrace1 = null;
		RayTraceResult rayTraceCarpet = null;
		if ((rayTraceWide(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
				new Vec3d(dest.x, dest.y, dest.z), false, false, true))
				|| (rayTrace1 = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
						new Vec3d(dest.x, dest.y + mc.player.getEyeHeight(), dest.z), false, false,
						true)) != null) {
			if ((rayTrace = rayTracePos(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
					new Vec3d(dest.x, mc.player.posY, dest.z), false, false, true)) != null
					|| (rayTrace1 = rayTracePos(
							new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(),
									mc.player.posZ),
							new Vec3d(dest.x, mc.player.posY + mc.player.getEyeHeight(), dest.z), false, false,
							true)) != null) {
				RayTraceResult trace = null;
				if (rayTrace == null) {
					trace = rayTrace1;
				}
				if (rayTrace1 == null) {
					trace = rayTrace;
				}
				if (trace == null) {
					// y = mc.player.posY;
					// yPreEn = mc.player.posY;
				} else {
					if (trace.getBlockPos() != null) {
						boolean fence = false;
						BlockPos target = trace.getBlockPos();
						// positions.add(BlockTools.getVec3(target));
						up = true;
						y = target.up().getY();
						yPreEn = target.up().getY();
						Block lastBlock = null;
						Boolean found = false;
						for (int i = 0; i < maxYTP; i++) {
							RayTraceResult tr = rayTracePos(
									new Vec3d(mc.player.posX, target.getY() + i, mc.player.posZ),
									new Vec3d(dest.x, target.getY() + i, dest.z), false, false, true);
							if (tr == null) {
								continue;
							}
							if (tr.getBlockPos() == null) {
								continue;
							}

							BlockPos blockPos = tr.getBlockPos();
							IBlockState blockState = getBlockState(blockPos);
							Block block = blockState.getBlock();
							if (blockState.getMaterial() != Material.AIR) {
								lastBlock = block;
								continue;
							}
							fence = lastBlock instanceof BlockFence;
							y = target.getY() + i;
							yPreEn = target.getY() + i;
							if (fence) {
								y += 1;
								yPreEn += 1;
								if (i + 1 > maxYTP) {
									found = false;
									break;
								}
							}
							found = true;
							break;
						}
						double difX = mc.player.posX - xPreEn;
						double difZ = mc.player.posZ - zPreEn;
						double divider = step * 0;
						if (!found) {
							attack = false;
							return false;
						}
					} else {
						attack = false;
						return false;
					}
				}
			} else {
				RayTraceResult ent = rayTracePos(
						new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ),
						new Vec3d(dest.x, dest.y, dest.z), false, false, false);
				if (ent != null && ent.entityHit == null) {
					y = mc.player.posY;
					yPreEn = mc.player.posY;
				} else {
					y = mc.player.posY;
					yPreEn = dest.y;
				}

			}
		}
		if (!attack) {
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = 0; i < steps; i++) {
			ind++;
			if (i == 1 && up) {
				x = mc.player.posX;
				y = yPreEn;
				z = mc.player.posZ;
				sendPacket(false, positionsBack, positions);
			}
			if (i != steps - 1) {
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
			} else {
				// if last teleport
				{
					double difX = mc.player.posX - xPreEn;
					double difY = mc.player.posY - yPreEn;
					double difZ = mc.player.posZ - zPreEn;
					double divider = step * i;
					x = mc.player.posX - difX * divider;
					y = mc.player.posY - difY * (up ? 1 : divider);
					z = mc.player.posZ - difZ * divider;
				}
				sendPacket(false, positionsBack, positions);
				double xDist = x - xPreEn;
				double zDist = z - zPreEn;
				double yDist = y - dest.y;
				double dist = Math.sqrt(xDist * xDist + zDist * zDist);
				if (dist > 4) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				} else if (dist > 0.05 && up) {
					x = xPreEn;
					y = yPreEn;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
				}
				if (Math.abs(yDist) < maxYTP) {
					x = xPreEn;
					y = dest.y;
					z = zPreEn;
					sendPacket(false, positionsBack, positions);
					//Attack / interact
					
				} else {
					attack = false;
				}
			}
		}

		// Go back!
		for (int i = positions.size() - 2; i > -1; i--) {
			{
				x = positions.get(i).x;
				y = positions.get(i).y;
				z = positions.get(i).z;
			}
			sendPacket(false, positionsBack, positions);
		}
		x = mc.player.posX;
		y = mc.player.posY;
		z = mc.player.posZ;
		sendPacket(false, positionsBack, positions);
		if (!attack) {
			if (sneaking) {
				mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			}
			positions.clear();
			positionsBack.clear();
			return false;
		}
		if (sneaking) {
			mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return true;
	}
	
	private static void attackInf(EntityLivingBase en) {
		AutoBlock.stopBlock();
		mc.player.swingArm(EnumHand.MAIN_HAND);
		Criticals.crit(x, y, z);
		Criticals.disable = true;
		mc.getConnection().getNetworkManager().sendPacketFinal(new CPacketUseEntity(en));
		Criticals.disable = false;
		AutoBlock.startBlock();

		float sharpLevel = EnchantmentHelper.getModifierForCreature(mc.player.getHeldItem(EnumHand.MAIN_HAND), en.getCreatureAttribute());
		boolean vanillaCrit = (mc.player.fallDistance > 0.0F) && (!mc.player.onGround)
				&& (!mc.player.isOnLadder()) && (!mc.player.isInWater())
				&& (!mc.player.isPotionActive(MobEffects.BLINDNESS)) && (mc.player.ridingEntity == null);
		if ((Jigsaw.getModuleByName("Criticals").isToggled()) || (vanillaCrit)) {
			mc.player.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.player.onEnchantmentCritical(en);
		}
	}
	
	public static void sendPacket(boolean goingBack, ArrayList<Vec3d> positionsBack, ArrayList<Vec3d> positions) {
		CPacketPlayer.Position playerPacket = new CPacketPlayer.Position(x, y, z, true);
		sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(x, y - 1, z), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0f, 1f, 0f));
		mc.getConnection().getNetworkManager().sendPacketFinal(playerPacket);
		if (goingBack) {
			positionsBack.add(new Vec3d(x, y, z));
			return;
		}
		positions.add(new Vec3d(x, y, z));
	}
	
	@SuppressWarnings("unused")
	public static RayTraceResult rayTracePos(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid,
			boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
		float[] rots = getFacePosRemote(vec32, vec31);
		float yaw = rots[0];
		double angleA = Math.toRadians(Utils.normalizeAngle(yaw));
		double angleB = Math.toRadians(Utils.normalizeAngle(yaw) + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3d left = new Vec3d(vec31.x + Math.cos(angleA) * size, vec31.y,
				vec31.z + Math.sin(angleA) * size);
		Vec3d right = new Vec3d(vec31.x + Math.cos(angleB) * size, vec31.y,
				vec31.z + Math.sin(angleB) * size);
		Vec3d left2 = new Vec3d(vec32.x + Math.cos(angleA) * size, vec32.y,
				vec32.z + Math.sin(angleA) * size);
		Vec3d right2 = new Vec3d(vec32.x + Math.cos(angleB) * size, vec32.y,
				vec32.z + Math.sin(angleB) * size);
		Vec3d leftA = new Vec3d(vec31.x + Math.cos(angleA) * size2, vec31.y,
				vec31.z + Math.sin(angleA) * size2);
		Vec3d rightA = new Vec3d(vec31.x + Math.cos(angleB) * size2, vec31.y,
				vec31.z + Math.sin(angleB) * size2);
		Vec3d left2A = new Vec3d(vec32.x + Math.cos(angleA) * size2, vec32.y,
				vec32.z + Math.sin(angleA) * size2);
		Vec3d right2A = new Vec3d(vec32.x + Math.cos(angleB) * size2, vec32.y,
				vec32.z + Math.sin(angleB) * size2);
		if (false) {
			RayTraceResult trace2 = mc.world.rayTraceBlocks(vec31, vec32, stopOnLiquid,
					ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
			return trace2;
		}
		// RayTraceResult trace4 = mc.world.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		RayTraceResult trace1 = mc.world.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		RayTraceResult trace2 = mc.world.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		RayTraceResult trace3 = mc.world.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// RayTraceResult trace5 = mc.world.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		// positionsBack.add(rightA);
		// positionsBack.add(right2A);
		// positionsBack.add(leftA);
		// positionsBack.add(left2A);
		RayTraceResult trace4 = null;
		RayTraceResult trace5 = null;
		if (trace2 != null || trace1 != null || trace3 != null || trace4 != null || trace5 != null) {
			if (returnLastUncollidableBlock) {
				if (trace5 != null && (Utils.getBlockState(trace5.getBlockPos()).getMaterial() != Material.AIR
						|| trace5.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace5;
				}
				if (trace4 != null && (Utils.getBlockState(trace4.getBlockPos()).getMaterial() != Material.AIR
						|| trace4.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace4;
				}
				if (trace3 != null && (Utils.getBlockState(trace3.getBlockPos()).getMaterial() != Material.AIR
						|| trace3.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null && (Utils.getBlockState(trace1.getBlockPos()).getMaterial() != Material.AIR
						|| trace1.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null && (Utils.getBlockState(trace2.getBlockPos()).getMaterial() != Material.AIR
						|| trace2.entityHit != null)) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			} else {
				if (trace5 != null) {
					return trace5;
				}
				if (trace4 != null) {
					return trace4;
				}
				if (trace3 != null) {
					// positions.add(BlockTools.getVec3(trace3.getBlockPos()));
					return trace3;
				}
				if (trace1 != null) {
					// positions.add(BlockTools.getVec3(trace1.getBlockPos()));
					return trace1;
				}
				if (trace2 != null) {
					// positions.add(BlockTools.getVec3(trace2.getBlockPos()));
					return trace2;
				}
			}
		}
		if (trace2 == null) {
			if (trace3 == null) {
				if (trace1 == null) {
					if (trace5 == null) {
						if (trace4 == null) {
							return null;
						}
						return trace4;
					}
					return trace5;
				}
				return trace1;
			}
			return trace3;
		}
		return trace2;
	}
	
	public static boolean rayTraceWide(Vec3d vec31, Vec3d vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox,
			boolean returnLastUncollidableBlock) {
		float yaw = getFacePosRemote(vec32, vec31)[0];
		yaw = Utils.normalizeAngle(yaw);
		yaw += 180;
		yaw = MathHelper.wrapDegrees(yaw);
		double angleA = Math.toRadians(yaw);
		double angleB = Math.toRadians(yaw + 180);
		double size = 2.1;
		double size2 = 2.1;
		Vec3d left = new Vec3d(vec31.x + Math.cos(angleA) * size, vec31.y,
				vec31.z + Math.sin(angleA) * size);
		Vec3d right = new Vec3d(vec31.x + Math.cos(angleB) * size, vec31.y,
				vec31.z + Math.sin(angleB) * size);
		Vec3d left2 = new Vec3d(vec32.x + Math.cos(angleA) * size, vec32.y,
				vec32.z + Math.sin(angleA) * size);
		Vec3d right2 = new Vec3d(vec32.x + Math.cos(angleB) * size, vec32.y,
				vec32.z + Math.sin(angleB) * size);
		Vec3d leftA = new Vec3d(vec31.x + Math.cos(angleA) * size2, vec31.y,
				vec31.z + Math.sin(angleA) * size2);
		Vec3d rightA = new Vec3d(vec31.x + Math.cos(angleB) * size2, vec31.y,
				vec31.z + Math.sin(angleB) * size2);
		Vec3d left2A = new Vec3d(vec32.x + Math.cos(angleA) * size2, vec32.y,
				vec32.z + Math.sin(angleA) * size2);
		Vec3d right2A = new Vec3d(vec32.x + Math.cos(angleB) * size2, vec32.y,
				vec32.z + Math.sin(angleB) * size2);
		// RayTraceResult trace4 = mc.world.rayTraceBlocks(leftA,
		// left2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		RayTraceResult trace1 = mc.world.rayTraceBlocks(left, left2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		RayTraceResult trace2 = mc.world.rayTraceBlocks(vec31, vec32, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		RayTraceResult trace3 = mc.world.rayTraceBlocks(right, right2, stopOnLiquid,
				ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
		// RayTraceResult trace5 = mc.world.rayTraceBlocks(rightA,
		// right2A, stopOnLiquid, ignoreBlockWithoutBoundingBox,
		// returnLastUncollidableBlock);
		RayTraceResult trace4 = null;
		RayTraceResult trace5 = null;
		if (returnLastUncollidableBlock) {
			return (trace1 != null && Utils.getBlockState(trace1.getBlockPos()).getMaterial() != Material.AIR)
					|| (trace2 != null && Utils.getBlockState(trace2.getBlockPos()).getMaterial() != Material.AIR)
					|| (trace3 != null && Utils.getBlockState(trace3.getBlockPos()).getMaterial() != Material.AIR)
					|| (trace4 != null && Utils.getBlockState(trace4.getBlockPos()).getMaterial() != Material.AIR)
					|| (trace5 != null && Utils.getBlockState(trace5.getBlockPos()).getMaterial() != Material.AIR);
		} else {
			return trace1 != null || trace2 != null || trace3 != null || trace5 != null || trace4 != null;
		}

	}
	
	public static void blinkToPosFromPos(Vec3d src, Vec3d dest, double maxTP) {
		double range = 0;
		double xDist = src.x - dest.x;
		double yDist = src.y - dest.y;
		double zDist = src.z - dest.z;
		double x1 = src.x;
		double y1 = src.y;
		double z1 = src.z;
		double x2 = dest.x;
		double y2 = dest.y;
		double z2 = dest.z;
		range = Math.sqrt(xDist * xDist + yDist * yDist + zDist * zDist);
		double step = maxTP / range;
		int steps = 0;
		for (int i = 0; i < range; i++) {
			steps++;
			if (maxTP * steps > range) {
				break;
			}
		}
		for (int i = 0; i < steps; i++) {
			double difX = x1 - x2;
			double difY = y1 - y2;
			double difZ = z1 - z2;
			double divider = step * i;
			double x = x1 - difX * divider;
			double y = y1 - difY * divider;
			double z = z1 - difZ * divider;
			//Jigsaw.chatMessage(y);
			mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
		}
		mc.player.connection.sendPacket(new CPacketPlayer.Position(x2, y2, z2, true));
	}
	
	public static boolean isBlacklisted(Entity en) {
		for(Entity i : blackList) {
			if(i.isEntityEqual(en)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<EntityLivingBase> getClosestEntitiesToEntity(float range, Entity ent) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (Utils.isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (ent.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
	/**
	 * Returns the distance to the entity. Args: entity
	 */
	public float getDistanceToEntityFromEntity(Entity entityIn, Entity entityIn2) {
		float f = (float) (entityIn.posX - entityIn2.posX);
		float f1 = (float) (entityIn.posY - entityIn2.posY);
		float f2 = (float) (entityIn.posZ - entityIn2.posZ);
		return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
	}

	public static ArrayList<EntityLivingBase> getClosestEntities(float range) {
		ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().player.getDistanceToEntity(en) < range) {
					entities.add(en);
				}
			}
		}
		return entities;
	}
	
//	public static boolean checkEntity(boolean friends, boolean invisible, boolean players) {
//		if (en.isEntityEqual(Minecraft.getMinecraft().player)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().player.getName())) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
//				&& en.getName().equals(Minecraft.getMinecraft().player.getName())) {
//			return false;
//		}
//		if (en.isDead) {
//			return false;
//		}
//		if (en.getHealth() <= 0) {
//			return false;
//		}
//		if (!(en instanceof EntityLivingBase)) {
//			return false;
//		}
//		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
//			if (!Jigsaw.getModuleByName("Friends").isToggled()) {
//				return false;
//			}
//		}
//		if (en.isInvisible()) {
//			if (!Jigsaw.getModuleByName("Invisible").isToggled()) {
//				return false;
//			}
//		}
//		if (en instanceof EntityPlayer) {
//			if (!Jigsaw.getModuleByName("Players").isToggled() || en.height < 0.21f) {
//				return false;
//			}
//		}
//		if (Team.isOnTeam(en)) {
//			if (!Jigsaw.getModuleByName("Team").isToggled()) {
//				return false;
//			}
//		}
//		if (!(en instanceof EntityPlayer)) {
//			if (!Jigsaw.getModuleByName("NonPlayers").isToggled()) {
//				return false;
//			}
//		}
//		if ((en instanceof EntityPlayer)) {
//			if (Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiGwen")) {
//				if (!((EntityPlayer) en).didSwingItem) {
//					if (en.onGround) {
//						if (en.isSprinting()) {
//							return true;
//						}
//					} else {
//						if (en.hurtResistantTime == 0) {
//							return false;
//						}
//					}
//				}
//			}
//			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog")) {
//				if(en.ticksExisted < 139) {
//					return false;
//				}
//			}
//		}
//		if(isBlacklisted(en)) {
//			return false;
//		}
//		// if(en.hurtTime > 12 &&
//		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
//		// return false;
//		// }
//		return true;
//	}

	public static boolean validEntity(EntityLivingBase en) {
		return validEntity(
				en, 
				Jigsaw.getModuleByName("Friends").isToggled(),
				Jigsaw.getModuleByName("Invisible").isToggled(),
				Jigsaw.getModuleByName("Players").isToggled(),
				Jigsaw.getModuleByName("Team").isToggled(),
				Jigsaw.getModuleByName("NonPlayers").isToggled(),
				Jigsaw.getModuleByName("Skip Unarmored Players").isToggled()
		);
	}
	
	public static boolean validEntity(EntityLivingBase en, boolean friends, boolean invisible, boolean players, boolean team, boolean nonplayers, boolean skipUnarmoredPlayers) {
		if (en.isEntityEqual(Minecraft.getMinecraft().player)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Freecam").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().player.getName())) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getModuleByName("Blink").isToggled()
				&& en.getName().equals(Minecraft.getMinecraft().player.getName())) {
			return false;
		}
		if (en.isDead) {
			return false;
		}
		if (en.getHealth() <= 0) {
			return false;
		}
		if (!(en instanceof EntityLivingBase)) {
			return false;
		}
		if (en instanceof EntityPlayer && Jigsaw.getFriendsMananger().isFriend((EntityPlayer) en)) {
			if (!friends) {
				return false;
			}
		}
		if (en.isInvisible()) {
			if (!invisible) {
				return false;
			}
		}
		if (en instanceof EntityPlayer) {
			if (!players || en.height < 0.21f) {
				return false;
			}
		}
		if (Team.isOnTeam(en)) {
			if (!team) {
				return false;
			}
		}
		if (!(en instanceof EntityPlayer)) {
			if (!nonplayers) {
				return false;
			}
		}
		if ((en instanceof EntityPlayer) && skipUnarmoredPlayers) {
			EntityPlayer living = (EntityPlayer)en;
			boolean armor = false;
			if(!armor && living.inventory.armorInventory.get(0) != null && living.inventory.armorInventory.get(0).getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory.get(1) != null && living.inventory.armorInventory.get(1).getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory.get(2) != null && living.inventory.armorInventory.get(2).getItem() != null) {
				armor = true;
			}
			if(!armor && living.inventory.armorInventory.get(3) != null && living.inventory.armorInventory.get(3).getItem() != null) {
				armor = true;
			}
			if(armor == false) {
				return false;
			}
		}

		if(isBlacklisted(en)) {
			return false;
		}
		
		if ((en instanceof EntityPlayer)) {
			if (Jigsaw.getModuleByName("AntiBot").isToggled()) {
				if(Jigsaw.getModuleByName("AntiBot").getCurrentMode().equals("Mineplex")) {
					if (!((EntityPlayer) en).didSwingItem) {
						if (en.onGround) {
							if (en.isSprinting()) {
								return true;
							}
						} else {
							if (en.hurtResistantTime == 0) {
								return false;
							}
						}
					}
				}
				if(Jigsaw.getModuleByName("AntiBot").getCurrentMode().equals("Ground")) {
					if(!en.onGround) {
						return false;
					}
				}
				if(Jigsaw.getModuleByName("AntiBot").getCurrentMode().equals("TicksExisted")) {
					if(en.ticksExisted < 200) {
						return false;
					}
				}
			}
//			if(Jigsaw.getBypassManager().getEnabledBypass() != null && Jigsaw.getBypassManager().getEnabledBypass().getName().equals("AntiWatchdog") || Jigsaw.getModuleByName("AntiBot(Watchdog)").isToggled()) {
//				if(en.ticksExisted < 139) {
//					return false;
//				}
//			}
		}
		// if(en.hurtTime > 12 &&
		// !Jigsaw.getModuleByName("HurtResistant").isToggled()) {
		// return false;
		// }
		return true;
	}

	public static EntityLivingBase getClosestEntity(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en)) {
					continue;
				}
				if (Minecraft.getMinecraft().player.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().player.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}
	
	public static EntityLivingBase getClosestEntity(float range, boolean friends, boolean invisible, boolean players, boolean team, boolean nonplayers, boolean skipUnarmoredPlayers) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!validEntity(en, friends, invisible, players, team, nonplayers, skipUnarmoredPlayers)) {
					continue;
				}
				if (Minecraft.getMinecraft().player.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().player.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}
	
	public static EntityLivingBase getClosestEntitySkipValidCheck(float range) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (Minecraft.getMinecraft().player.getDistanceToEntity(en) < mindistance) {
					mindistance = Minecraft.getMinecraft().player.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static EntityLivingBase getClosestEntityToEntity(float range, Entity ent) {
		EntityLivingBase closestEntity = null;
		float mindistance = range;
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
				EntityLivingBase en = (EntityLivingBase) o;
				if(en.isInvisible()) {
					continue;
				}
				if (ent.getDistanceToEntity(en) < mindistance) {
					mindistance = ent.getDistanceToEntity(en);
					closestEntity = en;
				}
			}
		}
		return closestEntity;
	}

	public static boolean isNotItem(Object o) {
		if (!(o instanceof EntityLivingBase)) {
			return false;
		}
		return true;
	}

	public static void faceEntity(Entity en) {
		facePos(new Vec3d(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));

	}

	public static void faceBlock(BlockPos blockPos) {
		facePos(getVec3d(blockPos));
	}

	public static Vec3i getVec3i(BlockPos blockPos) {
		return new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static Vec3d getVec3d(BlockPos blockPos) {
		return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3i vec) {
		return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3d vec) {
		return new BlockPos(vec.x, vec.y, vec.z);
	}

	public static void facePos(Vec3d vec) {
		if (ClientSettings.smoothAim) {
			smoothFacePos(vec);
			return;
		}
		double diffX = vec.x + 0.5 - Minecraft.getMinecraft().player.posX;
		double diffY = vec.y + 0.5
				- (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight());
		double diffZ = vec.z + 0.5 - Minecraft.getMinecraft().player.posZ;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		Minecraft.getMinecraft().player.rotationYaw = Minecraft.getMinecraft().player.rotationYaw
				+ MathHelper.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw);
		Minecraft.getMinecraft().player.rotationPitch = Minecraft.getMinecraft().player.rotationPitch
				+ MathHelper.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch);
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePos(Vec3d vec) {
		double diffX = vec.x + 0.5 - Minecraft.getMinecraft().player.posX;
		double diffY = vec.y + 0.5 - (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight());
		double diffZ = vec.z + 0.5 - Minecraft.getMinecraft().player.posZ;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {
				Minecraft.getMinecraft().player.rotationYaw
						+ MathHelper.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw),
				Minecraft.getMinecraft().player.rotationPitch
						+ MathHelper.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch) };
	}

	/**
	 * 
	 * 
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosRemote(Vec3d src, Vec3d dest) {
		double diffX = dest.x - src.x;
		double diffY = dest.y - (src.y);
		double diffZ = dest.z - src.z;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		return new float[] {MathHelper.wrapDegrees(yaw),
				MathHelper.wrapDegrees(pitch) };
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntity(Entity en) {
		if (en == null) {
			return new float[] { Minecraft.getMinecraft().player.rotationYawHead,
					Minecraft.getMinecraft().player.rotationPitch };
		}
		return getFacePos(new Vec3d(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));
	}

	/**
	 * 
	 * @param vec
	 * @return index 0 = yaw | index 1 = pitch
	 */
	public static float[] getFacePosEntityRemote(EntityLivingBase facing, Entity en) {
		if (en == null) {
			return new float[] { facing.rotationYawHead, facing.rotationPitch };
		}
		return getFacePosRemote(new Vec3d(facing.posX, facing.posY + en.getEyeHeight(), facing.posZ),
				new Vec3d(en.posX, en.posY + en.getEyeHeight(), en.posZ));
	}

	public static void smoothFacePos(Vec3d vec) {
		double diffX = vec.x + 0.5 - Minecraft.getMinecraft().player.posX;
		double diffY = vec.y + 0.5
				- (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight());
		double diffZ = vec.z + 0.5 - Minecraft.getMinecraft().player.posZ;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		boolean aim = false;
		float max = 5;
		float yawChange = 0;
		if ((MathHelper.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw)) > max * 2) {
			aim = true;
			yawChange = max;
		} else if ((MathHelper.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw)) < -max * 2) {
			aim = true;
			yawChange = -max;
		}
		float pitchChange = 0;
		if ((MathHelper.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch)) > max * 4) {
			aim = true;
			pitchChange = max;
		} else if ((MathHelper.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch)) < -max
				* 4) {
			aim = true;
			pitchChange = -max;
		}
		// Minecraft.getMinecraft().player.rotationYaw += yawChange;
		// Minecraft.getMinecraft().player.rotationPitch += pitchChange;
		if (aim) {
			Minecraft.getMinecraft().player.rotationYaw += (MathHelper
					.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
			Minecraft.getMinecraft().player.rotationPitch += (MathHelper
					.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch))
					/ (AuraUtils.getSmoothAimSpeed() * (rand.nextDouble() * 2 + 1));
		}

	}

	public static void smoothFacePos(Vec3d vec, double addSmoothing) {
		double diffX = vec.x + 0.5 - Minecraft.getMinecraft().player.posX;
		double diffY = vec.y + 0.5
				- (Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight());
		double diffZ = vec.z + 0.5 - Minecraft.getMinecraft().player.posZ;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;

		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

		Minecraft.getMinecraft().player.rotationYaw += (MathHelper
				.wrapDegrees(yaw - Minecraft.getMinecraft().player.rotationYaw))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
		Minecraft.getMinecraft().player.rotationPitch += (MathHelper
				.wrapDegrees(pitch - Minecraft.getMinecraft().player.rotationPitch))
				/ (AuraUtils.getSmoothAimSpeed() * addSmoothing);
	}

	// public static int getDistanceFromMouse(Entity entity)
	// {
	// float[] neededRotations = getRotationsNeeded(entity);
	// if(neededRotations != null)
	// {
	// float neededYaw =
	// Minecraft.getMinecraft().player.rotationYaw
	// - neededRotations[0], neededPitch =
	// Minecraft.getMinecraft().player.rotationPitch
	// - neededRotations[1];
	// float distanceFromMouse =
	// MathHelper.sqrt(neededYaw * neededYaw + neededPitch
	// * neededPitch);
	// return (int)distanceFromMouse;
	// }
	// return -1;
	// }
	// public static float[] getRotationsNeeded(Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - Minecraft.getMinecraft().player.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (Minecraft.getMinecraft().player.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (Minecraft.getMinecraft().player.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// double diffZ = entity.posZ - Minecraft.getMinecraft().player.posZ;
	// double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapDegrees(yaw
	// - Minecraft.getMinecraft().player.rotationYaw) /
	// AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapDegrees(pitch
	// - Minecraft.getMinecraft().player.rotationPitch) /
	// AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// Minecraft.getMinecraft().player.rotationYaw
	// + MathHelper.wrapDegrees(yaw
	// - Minecraft.getMinecraft().player.rotationYaw),
	// Minecraft.getMinecraft().player.rotationPitch
	// + MathHelper.wrapDegrees(pitch
	// - Minecraft.getMinecraft().player.rotationPitch)};
	//
	// }
	// public static float[] getRotationsNeededRemote(EntityLivingBase remote,
	// Entity entity)
	// {
	// if(entity == null)
	// return null;
	// double diffX = entity.posX - remote.posX;
	// double diffY;
	// if(entity instanceof EntityLivingBase)
	// {
	// EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
	// diffY =
	// entityLivingBase.posY
	// + entityLivingBase.getEyeHeight()
	// * 0.9
	// - (remote.posY + Minecraft
	// .getMinecraft().thePlayer.getEyeHeight());
	// }else
	// diffY =
	// (entity.boundingBox.minY + entity.boundingBox.maxY)
	// / 2.0D
	// - (remote.posY + remote.getEyeHeight());
	// double diffZ = entity.posZ - remote.posZ;
	// double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
	// float yaw =
	// (float)(Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
	// float pitch = (float)-(Math.atan2(diffY, dist) * 180.0D / Math.PI);
	// if(AuraUtils.getSmoothAim()) {
	// return new float[]{
	// (float) (MathHelper.wrapDegrees(yaw
	// - remote.rotationYaw) / AuraUtils.getSmoothAimSpeed()),
	// (float) (MathHelper.wrapDegrees(pitch
	// - remote.rotationPitch) / AuraUtils.getSmoothAimSpeed())};
	// }
	// return new float[]{
	// remote.rotationYaw
	// + MathHelper.wrapDegrees(yaw
	// - remote.rotationYaw),
	// remote.rotationPitch
	// + MathHelper.wrapDegrees(pitch
	// - remote.rotationPitch)};
	//
	// }
	public static float getPlayerBlockDistance(BlockPos blockPos) {
		return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public static float getPlayerBlockDistance(double posX, double posY, double posZ) {
		float xDiff = (float) (Minecraft.getMinecraft().player.posX - posX);
		float yDiff = (float) (Minecraft.getMinecraft().player.posY - posY);
		float zDiff = (float) (Minecraft.getMinecraft().player.posZ - posZ);
		return getBlockDistance(xDiff, yDiff, zDiff);
	}

	public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
		return MathHelper.sqrt(
				(xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}

	public static ArrayList<EntityItem> getNearbyItems(int range) {
		ArrayList<EntityItem> eList = new ArrayList<EntityItem>();
		for (Object o : Minecraft.getMinecraft().world.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().player.getDistanceToEntity(e) >= range) {
				continue;
			}

			eList.add(e);
		}
		return eList;
	}

	public static EntityItem getClosestItem(float range) {
		float mindistance = range;
		EntityItem ee = null;
		for (Object o : Minecraft.getMinecraft().world.getLoadedEntityList()) {
			if (!(o instanceof EntityItem)) {
				continue;
			}
			EntityItem e = (EntityItem) o;
			if (Minecraft.getMinecraft().player.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	public static Entity getClosestItemOrXPOrb(float range) {
		float mindistance = range;
		Entity ee = null;
		for (Object o : Minecraft.getMinecraft().world.getLoadedEntityList()) {
			if (!(o instanceof EntityItem) && !(o instanceof EntityXPOrb)) {
				continue;
			}
			Entity e = (Entity) o;
			if (Minecraft.getMinecraft().player.getDistanceToEntity(e) >= mindistance) {
				continue;
			}
			ee = e;
		}
		return ee;
	}

	private final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange)
			change = maxChange;
		else if (change < -maxChange)
			change = -maxChange;
		return current + change;
	}

	public static double normalizeAngle(double angle) {
		return (angle + 360) % 360;
	}

	public static float normalizeAngle(float angle) {
		return (angle + 360) % 360;
	}

	public static int getItemIndexHotbar(int itemID) {
		for (int i = 0; i < 9; i++) {
			ItemStack stackInSlot = mc.player.inventory.getStackInSlot(i);
			if (stackInSlot == null) {
				continue;
			}
			if (itemID == Item.getIdFromItem(stackInSlot.getItem())) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isBlockPosAir(BlockPos blockPos) {
		return mc.world.getBlockState(blockPos).getMaterial() == Material.AIR;
	}

	public static Block getBlockRelativeToEntity(Entity en, double d) {
		return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static IBlockState getBlockStateRelativeToEntity(Entity en, double d) {
		return getBlockState(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static BlockPos getBlockPosRelativeToEntity(Entity en, double d) {
		return new BlockPos(en.posX, en.posY + d, en.posZ);
	}
	
	public static Block getBlock(BlockPos pos) {
		return mc.world.getBlockState(pos).getBlock();
	}
	
	private static Vec3d lastLoc = null;
	
	public static Vec3d getLastGroundLocation() {
		return lastLoc;
		
	}
	
	public static void updateLastGroundLocation() {
		if(mc.player.onGround) {
			lastLoc = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
		}
	}
	
	public static double getXZDist(Vec3d loc1, Vec3d loc2) {
		double xDist = loc1.x - loc2.x;
		double zDist = loc1.z - loc2.z;
		return Math.abs(Math.sqrt(xDist * xDist + zDist * zDist));
	}
	
	public static IBlockState getBlockState(BlockPos blockPos) {
		return mc.world.getBlockState(blockPos);
	}
	
	public static ArrayList<BlockPos> getBlockPosesPlayerIsStandingOn() {
		BlockPos pos1 = new BlockPos(mc.player.boundingBox.minX, mc.player.boundingBox.minY - 0.01, mc.player.boundingBox.minZ);
		
		BlockPos pos2 = new BlockPos(mc.player.boundingBox.maxX, mc.player.boundingBox.minY - 0.01, mc.player.boundingBox.maxZ);
		
		Iterable<BlockPos> collisionBlocks = BlockPos.getAllInBox(pos1, pos2);
		ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
		for(BlockPos pos : collisionBlocks) {
			returnList.add(pos);
		}
		return returnList;
	}
	
	public static ArrayList<BlockPos> getBlockPosesEntityIsStandingOn(Entity en) {
		BlockPos pos1 = new BlockPos(en.boundingBox.minX, en.boundingBox.minY - 0.01, en.boundingBox.minZ);
		
		BlockPos pos2 = new BlockPos(en.boundingBox.maxX, en.boundingBox.minY - 0.01, en.boundingBox.maxZ);
		
		Iterable<BlockPos> collisionBlocks = BlockPos.getAllInBox(pos1, pos2);
		ArrayList<BlockPos> returnList = new ArrayList<BlockPos>();
		for(BlockPos pos : collisionBlocks) {
			returnList.add(pos);
		}
		return returnList;
	}
	
	public static boolean isEntityOnGround(Entity en) {
		ArrayList<BlockPos> poses = getBlockPosesEntityIsStandingOn(en);
		
		
		for(BlockPos pos : poses) {
			IBlockState blockState = getBlockState(pos);
			Block block = blockState.getBlock();
			if(!(blockState.getMaterial() instanceof MaterialTransparent) && blockState.getMaterial() != Material.AIR
					&& !(block instanceof BlockLiquid) && blockState.isFullCube()) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isPlayerOnLiqud() {
		ArrayList<BlockPos> poses = getBlockPosesPlayerIsStandingOn();
		boolean liquid = false;
		for(BlockPos pos : poses) {
			IBlockState blockState = getBlockState(pos);
			Block block = blockState.getBlock();
			if(block instanceof BlockLiquid) {
				liquid = true;
			}
			if(!(block instanceof BlockLiquid) && !JigsawPathfinder.NodeProcessors.FLYING.isPassable(blockState)) {
				return false;
			}
		}
		return liquid;
	}
	
	public static boolean isPlayerHoldingItem(Item item) {
		if(mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem().equals(item)) {
			return true;
		}
		if(mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemOffhand().getItem().equals(item)) {
			return true;
		}
		return false;
	}
	
	public static ItemStack returnItemStackIfPlayerHoldingItem(Item item) {
		if(mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem().equals(item)) {
			return mc.player.getHeldItemMainhand();
		}
		if(mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemOffhand().getItem().equals(item)) {
			return mc.player.getHeldItemOffhand();
		}
		return null;
	}
	
	public static Vec3d getPlayerVec3d() {
		return new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
	}
	
	public static Vec3d getPlayerVec3dEyeHeight() {
		return getPlayerVec3d().addVector(0, mc.player.getEyeHeight(), 0);
	}
	
	public static RayTraceResult doRightClick(BlockPos pos) {
		RayTraceResult rayTrace = mc.world.rayTraceBlocks(Utils.getPlayerVec3dEyeHeight(), Utils.getVec3d(pos.add(0.5, 0.5, 0.5)), false, false, true);
		if(rayTrace == null) {
			return null;
		}
		mc.playerController.processRightClickBlock(mc.player, mc.world, pos,
				rayTrace.sideHit, rayTrace.hitVec, EnumHand.OFF_HAND);
		
		return rayTrace;
	}
	
	public static void resetMcTimerTPS() {
		mc.timer.tickLength = 1000f/20f;
	}
	
	public static boolean willCrit(boolean criticalsEnabled, Entity targetEntity) {
		
		if(criticalsEnabled) {
			boolean willCrit;
			if(mc.player.onGround) {
				willCrit = !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(MobEffects.BLINDNESS) && !mc.player.isRiding() && targetEntity instanceof EntityLivingBase;
			}
			else {
				willCrit = mc.player.fallDistance > 0.0f && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(MobEffects.BLINDNESS) && !mc.player.isRiding() && targetEntity instanceof EntityLivingBase;
			}
			
	        
			if(!ClientSettings.sprintCrits) {
				willCrit = willCrit && !mc.player.isSprinting();
	        }
			
	        return willCrit;
		}
		else {
			boolean willCrit = mc.player.fallDistance > 0.0F && !mc.player.onGround && !mc.player.isOnLadder() && !mc.player.isInWater() && !mc.player.isPotionActive(MobEffects.BLINDNESS) && !mc.player.isRiding() && targetEntity instanceof EntityLivingBase;
	        
			if(!ClientSettings.sprintCrits) {
				willCrit = willCrit && !mc.player.isSprinting();
	        }
			
	        return willCrit;
		}
		
		
        
	}

	public static void createHitParticles(boolean criticalsEnabled, EntityLivingBase en) {
		float sharpLevel = EnchantmentHelper.getModifierForCreature(mc.player.getHeldItemMainhand(), en.getCreatureAttribute());
		if (Utils.willCrit(criticalsEnabled, en)) {
			mc.player.onCriticalHit(en);
		}
		if (sharpLevel > 0.0F) {
			mc.player.onEnchantmentCritical(en);
		}
	}
	
	public static RayTraceResult rayTraceAllFacesOnBlock(BlockPos blockPos) {
		for(int i = 0; i < EnumFacing.values().length; i++) {
			EnumFacing facing = EnumFacing.values()[i];
			double offsetAmount = 0.5;
			Vec3d rayTraceEnd = new Vec3d(
					blockPos.getX() + facing.getFrontOffsetX() * offsetAmount,
					blockPos.getY() + facing.getFrontOffsetY() * offsetAmount,
					blockPos.getZ() + facing.getFrontOffsetZ() * offsetAmount);
			
			RayTraceResult result = mc.world.rayTraceBlocks(
					new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
					rayTraceEnd.addVector(0.5, 0.5, 0.5), true,
					false, true);
			
			if(result == null) {
				continue;
			}
			
			if(result.getBlockPos() == null) {
				continue;
			}
			
			if(result.getBlockPos().getX() == blockPos.getX() && result.getBlockPos().getY() == blockPos.getY() && result.getBlockPos().getZ() == blockPos.getZ()) {
				return result;
			}
		}
		return null;
	}
	
	public static RayTraceResult rayTraceBlocksAndEntities(double entityTraceDistance, Vec3d start, Vec3d end) {
		Entity entity = mc.getRenderViewEntity();
		Entity hitEntity = null;

        if (entity != null && mc.world != null)
        {
            
            Vec3d vec3d = entity.getPositionEyes(1.0f);
            
            int i = 3;

            Vec3d src = start;
            Vec3d dest = end;
            Vec3d hitVec = null;

            hitEntity = null;
            
            List<Entity> list = mc.world.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(entityTraceDistance, entityTraceDistance, entityTraceDistance), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>()
            {
                public boolean apply(@Nullable Entity p_apply_1_)
                {
                    return p_apply_1_ != null && p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = entityTraceDistance;

            for (int j = 0; j < list.size(); ++j)
            {
                Entity entity1 = list.get(j);
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expandXyz((double)entity1.getCollisionBorderSize());
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(vec3d, dest);

                if (axisalignedbb.isVecInside(vec3d))
                {
                    if (d2 >= 0.0D)
                    {
                    	hitEntity = entity1;
                        hitVec = raytraceresult == null ? vec3d : raytraceresult.hitVec;
                        d2 = 0.0D;
                    }
                }
                else if (raytraceresult != null)
                {
                    double d3 = vec3d.distanceTo(raytraceresult.hitVec);

                    if (d3 < d2 || d2 == 0.0D)
                    {
                        boolean flag1 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists())
                        {
                            flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract);
                        }

                        if (!flag1 && entity1.getLowestRidingEntity() == entity.getLowestRidingEntity())
                        {
                            if (d2 == 0.0D)
                            {
                            	hitEntity = entity1;
                                hitVec = raytraceresult.hitVec;
                            }
                        }
                        else
                        {
                        	hitEntity = entity1;
                            hitVec = raytraceresult.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (hitEntity != null)
            {
            	return new RayTraceResult(hitEntity, hitVec);
            }
            
        }
        return mc.world.rayTraceBlocks(start, end, false, true, true);
	}
	
	public static String overideNameProtect(String string) {
		return string.charAt(0) + "§r" + string.substring(1);
	}
	
}