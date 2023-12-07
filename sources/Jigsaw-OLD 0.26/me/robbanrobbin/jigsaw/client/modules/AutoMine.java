package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AutoMine extends Module {
	
	private EnumFacing facing = EnumFacing.EAST;
	private boolean dugDown = false;

	public AutoMine() {
		super("AutoMine", 0, Category.WORLD, "Mines automatically so you can take a shit and when you come back you got tons of diamonds and you have hopefuly not fallen in lava.");
	}
	
	@Override
	public Component[] getModSettings() {
		double max = (mc.theWorld == null ? 256 : mc.theWorld.getHeight());
		final Slider killaruarangeslider = new BasicSlider("Block Height", ClientSettings.autoMineBlockLimit, 0.0, max, 0.0,
				ValueDisplay.DECIMAL);

		killaruarangeslider.addSliderListener(new SliderListener() {

			@Override
			public void onSliderValueChanged(Slider slider) {

				ClientSettings.autoMineBlockLimit = slider.getValue();
			}
		});
		return new Component[]{killaruarangeslider};
	}
	
	@Override
	public void onEnable() {
		facing = EnumFacing.fromAngle(mc.thePlayer.rotationYaw);
		super.onEnable();
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		Block under = Utils.getBlockRelativeToEntity(mc.thePlayer, -0.01d);
		boolean foundItem = false;
		EntityItem theItem = null;
		for (EntityItem item : Utils.getNearbyItems(4)) {
			if (mc.thePlayer.canEntityBeSeen(item) && item.ticksExisted > 20 && item.ticksExisted < 150) {
				foundItem = true;
				theItem = item;
			}
		}
		if (foundItem) {
			Utils.faceEntity(theItem);
			mc.thePlayer.moveFlying(0, 1f, 0.1f);
			if (theItem.posY > mc.thePlayer.posY && mc.thePlayer.onGround) {
				mc.thePlayer.jump();
			}
		} 
		else {
			BlockPos toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
					mc.thePlayer.posZ).offset(facing);
			boolean foundOre = false;
			for (int x = -3; x <= 3; x++) {
				for (int y = -3; y <= 5; y++) {
					for (int z = -3; z <= 3; z++) {
						BlockPos blockPos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y,
								mc.thePlayer.posZ + z);
						Block block = Utils.getBlock(blockPos);
						if (block.getMaterial() == Material.air) {
							continue;
						}
						if (block instanceof BlockOre) {
							MovingObjectPosition trace0 = mc.theWorld.rayTraceBlocks(
									new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
									new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5), true,
									false, true);
							if (trace0.getBlockPos() == null) {
								continue;
							}
							BlockPos blockPosTrace = trace0.getBlockPos();
							Block blockTrace = Utils.getBlock(blockPosTrace);
							double dist = Utils.getVec3(blockPos).distanceTo(Utils.getVec3(blockPosTrace));
							if (dist <= 1) {
								toFace = blockPosTrace;
								foundOre = true;
								break;
							}
						}
					}
				}
			}
			if (foundOre) {
				Utils.faceBlock(toFace);
				if (!Utils.isBlockPosAir(toFace)) {
					mineBlock(toFace);
				}
			} 
			else {
				if (mc.thePlayer.posY > ClientSettings.autoMineBlockLimit) {
					if (under.getMaterial() != Material.air) {
						mineBlockUnderPlayer();
					} 
					else if (mc.thePlayer.onGround) {
						mc.thePlayer.moveFlying(0, 0.5f, 0.1f);
					}
				} 
				else {
					if(Utils.getBlock(toFace).getMaterial() == Material.lava || Utils.getBlock(toFace).getMaterial() == Material.water) {
						facing = facing.getOpposite();
						toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
								mc.thePlayer.posZ).offset(facing);
					}
					if (Utils.isBlockPosAir(toFace) || !isBlockPosSafe(toFace)) {
						toFace = toFace.down();
						if (isBlockPosSafe(toFace)) {
							if (Utils.isBlockPosAir(toFace) && mc.thePlayer.onGround) {
								mc.thePlayer.moveFlying(0, 1f, 0.1f);
							}
						} 
						else {
							facing = facing.fromAngle(mc.thePlayer.rotationYaw + 90);
							toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
									mc.thePlayer.posZ).offset(facing);
							if (!isBlockPosSafe(toFace)) {
								facing = facing.fromAngle(mc.thePlayer.rotationYaw - 90);
								toFace = new BlockPos(mc.thePlayer.posX,
										mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
												.offset(facing);
								if (!isBlockPosSafe(toFace.down())) {
									facing = facing.fromAngle(mc.thePlayer.rotationYaw + 180);
									toFace = new BlockPos(mc.thePlayer.posX,
											mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ)
													.offset(facing);
								}
							}
						}
					}
					if (Utils.isBlockPosAir(toFace)) {
						toFace = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
								mc.thePlayer.posZ).offset(facing);
					}
					Utils.faceBlock(toFace);
					if (!Utils.isBlockPosAir(toFace)) {
						mineBlock(toFace);
					}
				}
			}
		}
		super.onUpdate(event);
	}
	
	public void mineBlockUnderPlayer() {
		BlockPos pos = Utils.getBlockPosRelativeToEntity(mc.thePlayer, -0.01d);
		mineBlock(pos);
	}
	
	public void mineBlock(BlockPos pos) {
		Block block = Utils.getBlock(pos);
		Utils.faceBlock(pos);
		mc.thePlayer.swingItem();
		mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
	}
	
	public boolean isBlockPosSafe(BlockPos pos) {
		return checkBlockPos(pos, 10);
	}
	
	public boolean checkBlockPos(BlockPos pos, int checkHeight) {
		boolean safe = true;
		boolean blockInWay = false;
		int fallDist = 0;
		if(Utils.getBlock(pos).getMaterial() == Material.lava
				|| Utils.getBlock(pos).getMaterial() == Material.water) {
			return false;
		}
		if(Utils.getBlock(pos.up(1)).getMaterial() == Material.lava
				|| Utils.getBlock(pos.up(1)).getMaterial() == Material.water) {
			return false;
		}
		if(Utils.getBlock(pos.up(2)).getMaterial() == Material.lava
				|| Utils.getBlock(pos.up(2)).getMaterial() == Material.water) {
			return false;
		}
		for(int i = 1; i < checkHeight + 1; i++) {
			BlockPos pos2 = pos.down(i);
			Block block = Utils.getBlock(pos2);
			if(block.getMaterial() == Material.air) {
				if(!blockInWay) {
					fallDist++;
				}
				continue;
			}
			if(!blockInWay) {
				if(block.getMaterial() == Material.lava
						|| block.getMaterial() == Material.water) {
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
	
}
