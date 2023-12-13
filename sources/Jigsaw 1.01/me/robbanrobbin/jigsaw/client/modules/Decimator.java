package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.WaitTimer;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class Decimator extends Module {

	RenderManager renderManager = mc.getRenderManager();
	WaitTimer timer = new WaitTimer();

	public Decimator() {
		super("Decimator", Keyboard.KEY_NONE, Category.WORLD, "idk");
	}
	@Override
	public void onLeftClick() {
		if(!mc.player.capabilities.isCreativeMode) {
			Jigsaw.chatMessage("§cYou must be in creativemode!");
			return;
		}
		if(false) {
			ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
			int tpCount = 999;
			ArrayList<Double> xPoses = new ArrayList<Double>();
			ArrayList<Double> zPoses = new ArrayList<Double>();
			double angleA = Math.toRadians(Utils.normalizeAngle(mc.player.rotationYawHead + (90)));
			double valX = Math.cos(angleA);
			double valZ = Math.sin(angleA);
			for (int i = 6; i < 150; i += 2) {
				double x = valX * (double) i;
				double z = valZ * (double) i;
				x += mc.player.posX;
				z += mc.player.posZ;
				for (int xx = -7; xx < 7; xx++) {
					for (int yy = 7; yy > -7; yy--) {
						for (int zz = -7; zz < 7; zz++) {
							double xBlock = (xx + x);
							double yBlock = (mc.player.posY + yy);
							double zBlock = (zz + z);

							BlockPos blockPos1 = new BlockPos(xBlock, yBlock, zBlock);
							IBlockState blockState1 = Utils.getBlockState(blockPos1);

							if (blockState1.getMaterial() != Material.AIR) {
								mineBlock(blockPos1, hitBlocks);
							}

						}
					}
				}
				if (i > 0 && tpCount > 1) {
					double xr = valX * ((double) i - 6.0);
					double zr = valZ * ((double) i - 6.0);
					xr += mc.player.posX;
					zr += mc.player.posZ;
					xPoses.add(xr);
					zPoses.add(zr);
					sendPacket(new CPacketPlayer.Position(xr, mc.player.posY, zr, false));
					tpCount = 0;
				}
				tpCount++;
			}
			for (int i = xPoses.size() - 2; i > -1; i -= 2) {
				if (i < 0) {
					sendPacket(new CPacketPlayer.Position(xPoses.get(0), mc.player.posY,
							zPoses.get(0), false));
					break;
				}
				sendPacket(new CPacketPlayer.Position(xPoses.get(i), mc.player.posY, zPoses.get(i),
						false));
			}
		}
		else {
			ArrayList<BlockPos> hitBlocks = new ArrayList<BlockPos>();
			int tpCount = 999;
			ArrayList<Double> xPoses = new ArrayList<Double>();
			ArrayList<Double> yPoses = new ArrayList<Double>();
			ArrayList<Double> zPoses = new ArrayList<Double>();
			double valX = mc.player.getLookVec().x;
			double valZ = mc.player.getLookVec().z;
			double valY = mc.player.getLookVec().y;
			for (int i = 6; i < 150; i += 2) {
				double x = valX * (double) i;
				double z = valZ * (double) i;
				double y = valY * (double) i;
				x += mc.player.posX;
				z += mc.player.posZ;
				y += mc.player.posY;
				for (int xx = -7; xx < 7; xx++) {
					for (int yy = 8; yy > -6; yy--) {
						for (int zz = -7; zz < 7; zz++) {
							double xBlock = (xx + x);
							double yBlock = (yy + y);
							double zBlock = (zz + z);

							BlockPos blockPos1 = new BlockPos(xBlock, yBlock, zBlock);
							IBlockState blockState1 = Utils.getBlockState(blockPos1);

							if (blockState1.getMaterial() != Material.AIR) {
								mineBlock(blockPos1, hitBlocks);
							}

						}
					}
				}
				if (i > 0 && tpCount > 1) {
					double xr = valX * ((double) i - 6.0);
					double zr = valZ * ((double) i - 6.0);
					double yr = valY * ((double) i - 6.0);
					xr += mc.player.posX;
					zr += mc.player.posZ;
					yr += mc.player.posY;
					xPoses.add(xr);
					yPoses.add(yr);
					zPoses.add(zr);
					sendPacket(new CPacketPlayer.Position(xr, yr, zr, false));
					tpCount = 0;
				}
				tpCount++;
			}
			for (int i = xPoses.size() - 2; i > -1; i -= 2) {
				if (i < 0) {
					sendPacket(new CPacketPlayer.Position(xPoses.get(0), yPoses.get(0),
							zPoses.get(0), false));
					break;
				}
				sendPacket(new CPacketPlayer.Position(xPoses.get(i), yPoses.get(i), zPoses.get(i),
						false));
			}
		}
		
		super.onLeftClick();
	}
	private void mineBlock(BlockPos pos, ArrayList<BlockPos> minedblocks) {
//		for(BlockPos pos1 : minedblocks) {
//			if(pos.getX() == pos1.getX() && pos.getY() == pos1.getY() && pos.getZ() == pos1.getZ()) {
//				return;
//			}
//		}
		mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK,
				pos, EnumFacing.NORTH));
		mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK,
				pos, EnumFacing.NORTH));
		//minedblocks.add(pos);
	}
}
