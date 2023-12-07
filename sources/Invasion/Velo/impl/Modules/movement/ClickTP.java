package Velo.impl.Modules.movement;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import Velo.api.Module.Module;
import Velo.api.Util.Render.RenderUtil;
import Velo.impl.Event.EventGetBlockReach;
import Velo.impl.Event.EventRender3D;
import Velo.impl.Event.EventSendPacket;
import Velo.impl.Event.EventUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class ClickTP extends Module {
	
	public ClickTP() {
		super("ClickTP", "ClickTP", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	public static transient BlockPos lockPos = null;
	
	@Override
	public void onEnable() {
		lockPos = null;
	}
	
	public void onUpdate(EventUpdate event) {
		

		if (mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
			lockPos = mc.objectMouseOver.getBlockPos();
		}

		if (mc.thePlayer.capabilities.isCreativeMode)
			return;

		mc.playerController.curBlockDamageMP = 0;
	}
	
	
	@Override
	public void onEventSendPacket(EventSendPacket event) {
		if (event.packet instanceof C08PacketPlayerBlockPlacement) {
			event.setCancelled(true);
			try {
				C08PacketPlayerBlockPlacement packet = (C08PacketPlayerBlockPlacement)event.packet;
				
				if (packet.getPosition().getY() + 1 <= mc.thePlayer.posY - 3) {
					
					if (mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
						packet.position = mc.objectMouseOver.getBlockPos();
					}
					
					double x, y, z;
					
					x = packet.getPosition().getX() + (new Random().nextDouble() - 0.5);
					y = packet.getPosition().getY() + 1;
					z = packet.getPosition().getZ() + (new Random().nextDouble() - 0.5);
					
					mc.thePlayer.setPosition(x, y, z);
					mc.theWorld.setBlockToAir(new BlockPos(x, y, z));
					
				}
				
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		super.onEventSendPacket(event);
	}
	
	
	
	@Override
	public void onRender3DUpdate(EventRender3D event) {

		
		
		if (mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && lockPos != null) {
			
			double xPos = ((lockPos.getX() + 0.5)
					+ ((lockPos.getX() + 0.5) - (lockPos.getX() + 0.5)) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosX;
			double yPos = ((lockPos.getY() + 0.5)
					+ ((lockPos.getY() + 0.5) - (lockPos.getY() + 0.5)) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosY + 0;
			double zPos = ((lockPos.getZ() + 0.5)
					+ ((lockPos.getZ() + 0.5) - (lockPos.getZ() + 0.5)) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosZ;

			GL11.glPushMatrix();
			GL11.glLoadIdentity();
			mc.entityRenderer.orientCamera(mc.timer.renderPartialTicks);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glLineWidth(2.0f);
			GL11.glBegin(2);
			GL11.glVertex3d(0.0D, 0.0D + Minecraft.getMinecraft().thePlayer.getEyeHeight(), 0.0D);
			GL11.glVertex3d(xPos, yPos, zPos);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();

			BlockPos below = lockPos;

			for (short i = 0; i < 5; i++) {

				RenderUtil.drawLine(below.getX(), below.getY(), below.getZ(), below.getX() + 1, below.getY(),
						below.getZ());
				RenderUtil.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX() + 1,
						below.getY() + 1, below.getZ());
				RenderUtil.drawLine(below.getX(), below.getY(), below.getZ(), below.getX(), below.getY(),
						below.getZ() + 1);
				RenderUtil.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX(), below.getY() + 1,
						below.getZ() + 1);
				RenderUtil.drawLine(below.getX(), below.getY(), below.getZ(), below.getX(), below.getY() + 1,
						below.getZ());
				RenderUtil.drawLine(below.getX(), below.getY() + 1, below.getZ(), below.getX(), below.getY() + 1,
						below.getZ());
				RenderUtil.drawLine(below.getX() + 1, below.getY(), below.getZ(), below.getX() + 1,
						below.getY() + 1, below.getZ());
				RenderUtil.drawLine(below.getX() + 1, below.getY() + 1, below.getZ(), below.getX() + 1,
						below.getY() + 1, below.getZ());
				RenderUtil.drawLine(below.getX(), below.getY(), below.getZ() + 1, below.getX(), below.getY() + 1,
						below.getZ() + 1);
				RenderUtil.drawLine(below.getX(), below.getY() + 1, below.getZ() + 1, below.getX(),
						below.getY() + 1, below.getZ() + 1);
				RenderUtil.drawLine(below.getX() + 1, below.getY(), below.getZ() + 1, below.getX(), below.getY(),
						below.getZ() + 1);
				RenderUtil.drawLine(below.getX() + 1, below.getY() + 1, below.getZ() + 1, below.getX(),
						below.getY() + 1, below.getZ() + 1);
				RenderUtil.drawLine(below.getX() + 1, below.getY(), below.getZ() + 1, below.getX() + 1,
						below.getY() + 1, below.getZ() + 1);
				RenderUtil.drawLine(below.getX() + 1, below.getY() + 1, below.getZ(), below.getX() + 1,
						below.getY() + 1, below.getZ() + 1);
				RenderUtil.drawLine(below.getX() + 1, below.getY(), below.getZ(), below.getX() + 1, below.getY(),
						below.getZ() + 1);

			}

			// RenderUtil.drawLine(mc.thePlayer.posX, mc.thePlayer.posY +
			// mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ,
			// mc.objectMouseOver.getBlockPos().getX() + 0.5,
			// mc.objectMouseOver.getBlockPos().getY(),
			// mc.objectMouseOver.getBlockPos().getZ() + 0.5);

		}
		super.onRender3DUpdate(event);
	}
	
	
	@Override
	public void onEventGetReach(EventGetBlockReach event) {

		if (mc.thePlayer.capabilities.isCreativeMode)
			return;

	

		event.setCancelled(true);
		event.reach = 200f;
		
		super.onEventGetReach(event);
	}
	
	
	
}
