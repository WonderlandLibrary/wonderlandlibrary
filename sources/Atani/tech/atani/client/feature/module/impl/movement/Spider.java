package tech.atani.client.feature.module.impl.movement;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import tech.atani.client.listener.event.minecraft.world.CollisionBoxesEvent;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateMotionEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.player.movement.MoveUtil;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.StringBoxValue;

@Native
@ModuleData(name = "Spider", description = "Climb up walls", category = Category.MOVEMENT)
public class Spider extends Module {
    private final StringBoxValue mode = new StringBoxValue("Mode", "Which mode will the module use?", this, new String[] {"Jump", "Collision", "Vulcan", "Verus"});
    private final CheckBoxValue jumpOnly = new CheckBoxValue("Jump Only", "Should the module only work when pressing the jump key?", this, false);

    @Override
    public String getSuffix() {
    	return mode.getValue();
    }

    @Listen
    public void onPacket(PacketEvent packetEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        if(canClimbWall() && mode.is("Vulcan")) {
            if(packetEvent.getPacket() instanceof C03PacketPlayer) {
                C03PacketPlayer packet = (C03PacketPlayer) packetEvent.getPacket();

                if(mc.thePlayer.ticksExisted % 3 == 0) {
                    float yaw = MoveUtil.getDirection();
                    double random = (Math.random() * 0.03 + 0.16);

                    packet.setY(packet.getY() - 0.015);

                    float f = yaw * 0.017453292F;
                    packet.setX(packet.getX() + (MathHelper.sin(f) * random));
                    packet.setZ(packet.getZ() - (MathHelper.cos(f) * random));
                }

                if(mc.thePlayer.ticksExisted % 2 == 0) {
                    packet.setOnGround(true);
                }
            }
        }
    }

    @Listen
    public void onCollisionBoxes(CollisionBoxesEvent collisionBoxesEvent) {
        if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
            return;

        if(canClimbWall()) {
            switch(mode.getValue()) {
            case "Collision":
                if (mc.thePlayer.motionY > 0) {
                    return;
                }

                BlockPos blockPos = collisionBoxesEvent.getBlockPos();

                collisionBoxesEvent.setBoundingBox(new AxisAlignedBB(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos.getX() + 1, 1, blockPos.getZ() + 1));
                break;
            }
        }
    }
    
    @Listen
    public void onMotion(UpdateMotionEvent updateMotionEvent) {
        if(updateMotionEvent.getType() == UpdateMotionEvent.Type.MID) {
            if(jumpOnly.getValue() && !isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                return;
            }

            if(this.canClimbWall()) {
                switch (mode.getValue()) {
                case "Jump":
                case "Vulcan":
                    mc.thePlayer.jump();
                    break;

                case "Verus":
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.motionY = 0.42f;
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    private boolean canClimbWall() {
        return mc.thePlayer != null && mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && mc.thePlayer.fallDistance < 1.0F;
    }

}
