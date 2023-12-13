package vestige.anticheat;

import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import vestige.util.IMinecraft;

@Getter
public class PlayerData implements IMinecraft {

    private final EntityPlayer player;

    private double x, y, z;
    private double lastX, lastY, lastZ;

    private double motionX, motionY, motionZ;
    private double lastMotionX, lastMotionY, lastMotionZ;

    private double dist, lastDist;

    private boolean onGround, lastGround, lastLastGround;
    private boolean mathGround, lastMathGround, lastLastMathGround;

    private float yaw, pitch;
    private float lastYaw, lastPitch;

    private float yawChange, pitchChange;

    private boolean sprinting, sneaking;
    private boolean lastSprinting, lastSneaking;

    private boolean collidedHorizontally, lastCollidedHorizontally;

    private boolean closeToGround;
    private int closeToGroundTicks;

    public PlayerData(EntityPlayer player) {
        this.player = player;
    }

    public void updateInfos() {
        updatePosition();
        updateRotation();
        updateActions();
    }

    private void updatePosition() {
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

        this.x = player.posX;
        this.y = player.posY;
        this.z = player.posZ;

        this.lastLastGround = lastGround;
        this.lastGround = onGround;
        this.onGround = player.onGround;

        this.lastLastMathGround = lastMathGround;
        this.lastMathGround = mathGround;
        this.mathGround = y % 0.0625 == 0.0;

        this.lastMotionX = motionX;
        this.lastMotionY = motionY;
        this.lastMotionZ = motionZ;

        this.motionX = x - lastX;
        this.motionY = y - lastY;
        this.motionZ = z - lastZ;

        this.lastDist = dist;
        this.dist = Math.hypot(motionX, motionZ);

        this.lastCollidedHorizontally = collidedHorizontally;
        this.collidedHorizontally = player.isCollidedHorizontally;

        findBlock();
    }

    private void findBlock() {
        boolean foundBlock = false;

        this.closeToGroundTicks++;

        for(double x2 = x - 1; x2 <= x + 1; x2++) {
            for(double y2 = y - 1; y2 <= y + 1; y2++) {
                for(double z2 = z - 1; z2 <= z + 1; z2++) {
                    BlockPos pos = new BlockPos(x2, y2, z2);

                    Block block = mc.theWorld.getBlockState(pos).getBlock();

                    if(block != null) {
                        Material material = block.getMaterial();

                        if(material != Material.air && material != Material.water && material != Material.lava) {
                            foundBlock = true;
                        }
                    }
                }
            }
        }

        if(foundBlock != closeToGround) {
            this.closeToGroundTicks = 0;
        }

        this.closeToGround = foundBlock;
    }

    private void updateRotation() {
        this.lastYaw = yaw;
        this.lastPitch = pitch;

        this.yaw = player.rotationYaw;
        this.pitch = player.rotationPitch;

        this.yawChange = yaw - lastYaw;
        this.pitchChange = pitch - lastPitch;
    }

    private void updateActions() {
        this.lastSprinting = sprinting;
        this.lastSneaking = sneaking;

        this.sprinting = player.isSprinting();
        this.sneaking = player.isSneaking();
    }

}
