package summer.cheat.cheats.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import summer.base.manager.Selection;
import summer.base.manager.config.Cheats;
import summer.base.utilities.MoveUtils;
import summer.cheat.eventsystem.Event;
import summer.cheat.eventsystem.EventTarget;
import summer.cheat.eventsystem.events.player.EventMotion;
import summer.cheat.eventsystem.events.player.EventUpdate;

public class Phase extends Cheats{

	public Phase() {
		super("Phase", "", Selection.PLAYER);
		// TODO Auto-generated constructor stub
	}
	
	@EventTarget
	public void onMove(EventMotion em) {
		if (mc.thePlayer.isCollidedHorizontally && MoveUtils.isMoving() && mc.thePlayer.onGround) {
			if (mc.timer.timerSpeed == 0.2F) {
				final float var2 = getDirection();
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (mc.thePlayer.motionX * 0.3925), mc.thePlayer.posY, mc.thePlayer.posZ + (mc.thePlayer.motionZ * 0.3925), mc.thePlayer.onGround));
				this.mc.thePlayer.setPosition(mc.thePlayer.posX + 0.8420 * Math.cos(Math.toRadians(var2 + 90.0f)), mc.thePlayer.posY, mc.thePlayer.posZ + 0.8420 * Math.sin(Math.toRadians(var2 + 90.0f)));
				for (int i = 0; i < 2; i++) {
                	if (this.isInsideBlock()) {
                		mc.timer.timerSpeed = 1F;
                		this.mc.thePlayer.setPosition(mc.thePlayer.posX + 0.3520 * Math.cos(Math.toRadians(var2 + 90.0f)), mc.thePlayer.posY, mc.thePlayer.posZ + 0.3520 * Math.sin(Math.toRadians(var2 + 90.0f)));
                	}
                }
				if (mc.timer.timerSpeed == 1F) {
					//mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (mc.thePlayer.motionX * 0.3925), mc.thePlayer.posY, mc.thePlayer.posZ + (mc.thePlayer.motionZ * 0.3925), mc.thePlayer.onGround));
				}
				em.setY(0);
			} else {
				mc.thePlayer.moveForward *= 0.2F;
				mc.thePlayer.moveStrafing *= 0.2F;
				mc.timer.timerSpeed = 0.2F;
				mc.thePlayer.cameraPitch += 18;
				mc.thePlayer.cameraYaw += 1;
			}
		} else {
			mc.timer.timerSpeed = 1F;
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate eu){
		eu.setPitch(90.0F);
	}

	public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

	public static float getDirection() {
        float direction = mc.thePlayer.rotationYaw;
        boolean back =mc.gameSettings.keyBindBack.isKeyDown() && !mc.gameSettings.keyBindForward.isKeyDown();
        boolean forward =!mc.gameSettings.keyBindBack.isKeyDown() && mc.gameSettings.keyBindForward.isKeyDown();
        if (mc.gameSettings.keyBindLeft.isKeyDown()) {
        	direction -= (back ? 135 : (forward ? 45 : 90));
        } if (mc.gameSettings.keyBindRight.isKeyDown()) {
        	direction += (back ? 135 : (forward ? 45 : 90));
        }
        if (back && direction == mc.thePlayer.rotationYaw) {
        	direction += 180F;
        }
        return direction;
    }

}
