package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.EntityUtil;
import net.minecraft.block.material.Material;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HoleTP
        extends Module {
    private static HoleTP INSTANCE = new HoleTP();
    private final double[] oneblockPositions = new double[]{0.42, 0.75};
    private int packets;
    private boolean jumped = false;

    public HoleTP() {
        super("HoleTP", "Teleports you in a hole.", Module.Category.MOVEMENT, true, false, false);
        this.setInstance();
    }

    public static HoleTP getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HoleTP();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1 && (Speed.getInstance().isOff() || Speed.getInstance().mode.getValue() == Speed.Mode.INSTANT) && Strafe.getInstance().isOff() && LagBlock.getInstance().isOff()) {
            if (!HoleTP.mc.player.onGround) {
                if (HoleTP.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.jumped = true;
                }
            } else {
                this.jumped = false;
            }
            if (!this.jumped && (double) HoleTP.mc.player.fallDistance < 0.5 && BlockUtil.isInHole() && HoleTP.mc.player.posY - BlockUtil.getNearestBlockBelow() <= 1.125 && HoleTP.mc.player.posY - BlockUtil.getNearestBlockBelow() <= 0.95 && !EntityUtil.isOnLiquid() && !EntityUtil.isInLiquid()) {
                if (!HoleTP.mc.player.onGround) {
                    ++this.packets;
                }
                if (!(HoleTP.mc.player.onGround || HoleTP.mc.player.isInsideOfMaterial(Material.WATER) || HoleTP.mc.player.isInsideOfMaterial(Material.LAVA) || HoleTP.mc.gameSettings.keyBindJump.isKeyDown() || HoleTP.mc.player.isOnLadder() || this.packets <= 0)) {
                    BlockPos blockPos = new BlockPos(HoleTP.mc.player.posX, HoleTP.mc.player.posY, HoleTP.mc.player.posZ);
                    for (double position : this.oneblockPositions) {
                        HoleTP.mc.player.connection.sendPacket(new CPacketPlayer.Position((float) blockPos.getX() + 0.5f, HoleTP.mc.player.posY - position, (float) blockPos.getZ() + 0.5f, true));
                    }
                    HoleTP.mc.player.setPosition((float) blockPos.getX() + 0.5f, BlockUtil.getNearestBlockBelow() + 0.1, (float) blockPos.getZ() + 0.5f);
                    this.packets = 0;
                }
            }
        }
    }
}

