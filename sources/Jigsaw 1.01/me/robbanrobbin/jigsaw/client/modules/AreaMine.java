package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;

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
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AreaMine extends Module {
	
	public BlockPos pos1;
	public BlockPos pos2;
	public ArrayList<BlockPos> blockPoses = new ArrayList<BlockPos>();
	public BlockPos currentPos;

	public AreaMine() {
		super("AreaMine", 0, Category.AI, "Select two blocks and it will mine it for you!");
	}
	
	@Override
	public ModSetting[] getModSettings() {
		CheckBtnSetting box2 = new CheckBtnSetting("FastBreak", "areaMineFastBreak");
		return new ModSetting[]{box2};
	}
	
	@Override
	public void onEnable() {
		pos1 = null;
		pos2 = null;
		blockPoses.clear();
		currentPos = null;
		super.onEnable();
	}
	
	public float[] getRots(BlockPos pos) {
		float[] rots = Utils.getFacePos(Utils.getVec3d(pos));
		
		rots = Utils.getFacePos(Utils.getVec3d(pos));
		
		float yaw = rots[0];
		float pitch = rots[1];
		
		return new float[]{yaw, pitch};
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(currentPos != null && Utils.getBlockState(currentPos).getMaterial() != Material.AIR) {
			float[] rots = getRots(currentPos);
			event.yaw = rots[0];
			event.pitch = rots[1];
			return;
		}
		currentPos = null;
		if(pos1 == null || pos2 == null) {
			return;
		}
		if(blockPoses.isEmpty()) {
			for(BlockPos pos : BlockPos.getAllInBox(pos1, pos2)) {
				blockPoses.add(pos);
			}
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Loaded " + blockPoses.size() + " blocks!"));
		}
		int valid = 0;
		for(BlockPos pos : blockPoses) {
			if(Utils.getBlockState(pos).getMaterial() != Material.AIR) {
				valid++;
			}
		}
		if(valid == 0) {
			pos1 = null;
			pos2 = null;
			blockPoses.clear();
			currentPos = null;
		}
		for(BlockPos pos : blockPoses) {
			if(Utils.getBlockState(pos).getMaterial() == Material.AIR) {
				continue;
			}
			double x = mc.player.posX - (pos.getX() + 0.5);
			double y = mc.player.posY - (pos.getY() + 0.5 - mc.player.getEyeHeight());
			double z = mc.player.posZ - (pos.getZ() + 0.5);
			double dist = Math.sqrt(x * x + y * y + z * z);
			if(dist < 5) {
				RayTraceResult trace = mc.world.rayTraceBlocks(
						new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
						new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), true,
						false, true);
				if(trace.getBlockPos().equals(pos)) {
					if(dist >= 1.5) {
						float yaw = getRots(trace.getBlockPos())[0];
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
					currentPos = pos;
					float[] rots = getRots(currentPos);
					event.yaw = rots[0];
					event.pitch = rots[1];
					break;
				}
			}
		}
		super.onUpdate(event);
	}
	
	@Override
	public void onLateUpdate() {
		if(currentPos != null) {
			double x = mc.player.posX - (currentPos.getX() + 0.5);
			double y = mc.player.posY - (currentPos.getY() + 0.5 - mc.player.getEyeHeight());
			double z = mc.player.posZ - (currentPos.getZ() + 0.5);
			double dist = Math.sqrt(x * x + y * y + z * z);
			if(dist >= 5) {
				currentPos = null;
				return;
			}
			mineBlock(currentPos);
		}
		super.onLateUpdate();
	}
	
	@Override
	public void onRender() {
		if(pos1 != null) {
			Vec3d pos = RenderTools.getRenderPos(pos1.getX(), pos1.getY(), pos1.getZ());
			RenderTools.drawBlockESP(pos.x, pos.y, pos.z, 0.5f, 1f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		if(pos2 != null) {
			Vec3d pos = RenderTools.getRenderPos(pos2.getX(), pos2.getY(), pos2.getZ());
			RenderTools.drawBlockESP(pos.x, pos.y, pos.z, 1f, 0.5f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		if(pos1 != null && pos2 != null) {
			for(BlockPos blockPos : blockPoses) {
				if(Utils.getBlockState(blockPos).getMaterial() == Material.AIR) {
					continue;
				}
				Vec3d pos = RenderTools.getRenderPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
				RenderTools.drawOutlinedBlockESP(pos.x, pos.y, pos.z, 1, 1, 1, 0.2f, 1f);
			}
		}
		if(currentPos != null) {
			Vec3d pos = RenderTools.getRenderPos(currentPos.getX(), currentPos.getY(), currentPos.getZ());
			RenderTools.drawBlockESP(pos.x, pos.y, pos.z, 0.5f, 0.5f, 0.5f, 0.2f, 1f, 1f, 1f, 0.7f, 0.5f);
		}
		super.onRender();
	}
	
	@Override
	public void onRightClick() {
		if(pos1 == null) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Set first position!"));
			pos1 = mc.objectMouseOver.getBlockPos();
		}
		else if(pos2 == null) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Set second position!"));
			pos2 = mc.objectMouseOver.getBlockPos();
		}
		super.onRightClick();
	}
	
	public void mineBlock(BlockPos pos) {
		if(ClientSettings.areaMineFastBreak) {
			mc.player.swingArm(EnumHand.MAIN_HAND);
			mc.player.connection.sendPacket(
					new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.NORTH));
			mc.player.connection.sendPacket(
					new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.NORTH));
		}
		else {
			mc.player.swingArm(EnumHand.MAIN_HAND);
			if(mc.player.capabilities.isCreativeMode) {
				mc.playerController.clickBlock(pos, EnumFacing.UP);
			}
			else {
				mc.playerController.onPlayerDamageBlock(pos, EnumFacing.UP);
			}
		}
	}
	
}
