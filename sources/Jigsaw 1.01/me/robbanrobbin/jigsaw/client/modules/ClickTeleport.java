package me.robbanrobbin.jigsaw.client.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.TeleportResult;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.utils.GuiUtils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class ClickTeleport extends Module {

	private BlockPos hover;
	private boolean tp = false;

	public ClickTeleport() {
		super("ClickTeleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleports you forward when you right-click. Max 200 blocks.");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		hover = mc.player.rayTrace(200, mc.timer.renderPartialTicks).getBlockPos();
		if(tp) {
			event.cancel();
			tp = false;
		}
		super.onUpdate(event);
	}
	
	ArrayList<Vec3d> positions = new ArrayList<Vec3d>();

	@Override
	public void onRightClick() {
		if(mc.currentScreen != null) {
			return;
		}
		positions.clear();
		RayTraceResult rayTrace = mc.player.rayTrace(200, mc.timer.renderPartialTicks);
		BlockPos blockPos = rayTrace.getBlockPos();
		if (blockPos == null) {
			return;
		}
		if(isMode("Vanilla")) {
			TeleportResult result = Utils.pathFinderTeleportTo(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), Utils.getVec3d(rayTrace.getBlockPos().up()));
			if(!result.foundPath) {
				return;
			}
			positions = result.positions;
			mc.player.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
			tp = true;
		}
		if(isMode("Mineplex Improved")) {
			TeleportResult result = Utils.pathFinderTeleportTo_MINEPLEX(new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ), Utils.getVec3d(rayTrace.getBlockPos().up()));
			if(!result.foundPath) {
				return;
			}
			positions = result.positions;
			mc.player.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
			tp = true;
		}
		if(isMode("Mineplex Old")) {
			if(blockPos.getY() + 1 != mc.player.posY) {
				return;
			}
			double distance = mc.player.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
			double stepSize = 0.20;
			double step = stepSize / distance;
			double steps = 0;
			for (int i = 0; i < distance; i++) {
				steps += 1 / stepSize;
				if (stepSize * steps > distance) {
					break;
				}
			}
			
			double targetX = blockPos.getX() + 0.5;
			double targetY = blockPos.getY();
			double targetZ = blockPos.getZ() + 0.5;
			
			int item = mc.player.inventory.currentItem;
			
			for(int i = 0; i < steps; i++) {
				
				double difX = mc.player.posX - targetX;
				double difY = mc.player.posY - targetY;
				double difZ = mc.player.posZ - targetZ;
				
				double divider = step * i;
				
				double posX = mc.player.posX - (difX * divider);
				double posY = mc.player.posY;
				double posZ = mc.player.posZ - (difZ * divider);
				
				Vec3d position = new Vec3d(posX, posY, posZ);

				positions.add(position);
				
//				List<AxisAlignedBB> lst = mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.contract(0.01).set(posX, posY, posZ));
//				
//				if(lst.size() > 1) {
////					Jigsaw.chatMessage("Collision! Size: " + lst.size() + ", Index: " + i);
////					return;
//				}
			}

			sendPacket(new CPacketHeldItemChange(7));
			for(Vec3d vec : positions) {
				
				double posX = vec.x;
				double posY = vec.y;
				double posZ = vec.z;
				
				sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(posX, posY - 1, posZ), EnumFacing.DOWN, EnumHand.OFF_HAND, 0f, 1f, 0f));
				sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
				
			}
			sendPacket(new CPacketHeldItemChange(item));
			
			mc.player.setPosition(targetX, targetY + 1, targetZ);
			tp = true;
			
			
		}
		if(isMode("Mineplex Old")) {
			if(blockPos.getY() + 1 != mc.player.posY) {
				return;
			}
			double distance = mc.player.getDistance(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
			double stepSize = 0.20;
			double step = stepSize / distance;
			double steps = 0;
			for (int i = 0; i < distance; i++) {
				steps += 1 / stepSize;
				if (stepSize * steps > distance) {
					break;
				}
			}
			
			double targetX = blockPos.getX() + 0.5;
			double targetY = blockPos.getY();
			double targetZ = blockPos.getZ() + 0.5;
			
			int item = mc.player.inventory.currentItem;
			
			for(int i = 0; i < steps; i++) {
				
				double difX = mc.player.posX - targetX;
				double difY = mc.player.posY - targetY;
				double difZ = mc.player.posZ - targetZ;
				
				double divider = step * i;
				
				double posX = mc.player.posX - (difX * divider);
				double posY = mc.player.posY;
				double posZ = mc.player.posZ - (difZ * divider);
				
				Vec3d position = new Vec3d(posX, posY, posZ);

				positions.add(position);
				
//				List<AxisAlignedBB> lst = mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.contract(0.01).set(posX, posY, posZ));
//				
//				if(lst.size() > 1) {
////					Jigsaw.chatMessage("Collision! Size: " + lst.size() + ", Index: " + i);
////					return;
//				}
			}

			sendPacket(new CPacketHeldItemChange(7));
			for(Vec3d vec : positions) {
				
				double posX = vec.x;
				double posY = vec.y;
				double posZ = vec.z;
				
				sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(posX, posY - 1, posZ), EnumFacing.DOWN, EnumHand.OFF_HAND, 0f, 1f, 0f));
				sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
				
			}
			sendPacket(new CPacketHeldItemChange(item));
			
			mc.player.setPosition(targetX, targetY + 1, targetZ);
			tp = true;
			
			
		}
		super.onRightClick();
	}

	@Override
	public void onRender() {
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		RenderTools.lineWidth(2);
		GuiUtils.setColor(ClientSettings.getForeGroundGuiColor());
		RenderTools.glBegin(3);
		for (Vec3d vec : positions) {
			RenderTools.putVertex3d(RenderTools.getRenderPos(vec.x, vec.y, vec.z));
		}
		RenderTools.glEnd();
		RenderTools.color4f(0.3f, 0.3f, 1f, 1f);
		RenderTools.glBegin(3);
		RenderTools.glEnd();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();
		
//		RenderTools.lineWidth(3);
//		for (Vec3d vec : positions) {
//			drawESP(1f, 0.3f, 0.3f, 1f, vec.x, vec.y, vec.z);
//		}
		
		if (hover == null) {
			return;
		}
		double x = hover.getX() + 0.5 - mc.getRenderManager().renderPosX;
		double y = hover.getY() + 1 - mc.getRenderManager().renderPosY;
		double z = hover.getZ() + 0.5 - mc.getRenderManager().renderPosZ;
		
		RenderTools.drawSolidEntityESP(x, y, z, 1 * 0.65, 0.1, 0.6f, 0.2f, 0.2f, 0.3f);
		RenderTools.drawOutlinedEntityESP(x, y, z, 1 * 0.65, 0.1, 1f, 1f, 1f, 1f);
		
		
		
		super.onRender();
	}
	
	public void drawESP(float red, float green, float blue, float alpha, double x, double y, double z) {
		double xPos = x - mc.getRenderManager().renderPosX;
		double yPos = y - mc.getRenderManager().renderPosY;
		double zPos = z - mc.getRenderManager().renderPosZ;
		RenderTools.drawOutlinedEntityESP(xPos, yPos, zPos, mc.player.width / 2, mc.player.height, red, green,
				blue, alpha);
	}
	
	@Override
	public String[] getModes() {
		return new String[] {"Vanilla", "Mineplex Old", "Mineplex Improved"};
	}

}
