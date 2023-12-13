package dev.rise.anticheat.data;

import dev.rise.anticheat.check.Check;
import dev.rise.anticheat.check.manager.CheckManager;
import dev.rise.util.player.PlayerUtil;
import lombok.Getter;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;

import java.util.List;

@Getter
public final class PlayerData {

    private final EntityOtherPlayerMP player;

    private final List<Check> checks;

    private int serverPosX, serverPosY, serverPosZ;

    private int ticksSinceLastTeleport,
            ticksSinceLastVelocity;

    private double x, y, z,
            lastX, lastY, lastZ,
            deltaX, deltaY, deltaZ,
            lastDeltaX, lastDeltaY, lastDeltaZ;

    private double groundX, groundY, groundZ,
            lastGroundX, lastGroundY, lastGroundZ;

    private boolean onGround, lastOnGround;
    private boolean isRoughlyGround, lastIsRoughlyGround;

    public PlayerData(final EntityOtherPlayerMP player) {
        this.player = player;

        this.serverPosX = player.serverPosX;
        this.serverPosY = player.serverPosY;
        this.serverPosZ = player.serverPosZ;

        this.checks = CheckManager.loadChecks(this);
    }

    public void handle(final Packet<?> packet) {
        if (this.player.bot)
            return;

        if (packet instanceof S14PacketEntity.S15PacketEntityRelMove
                || packet instanceof S14PacketEntity.S17PacketEntityLookMove) {
            final S14PacketEntity wrapper = ((S14PacketEntity) packet);

            if (wrapper.entityId == this.player.getEntityId()) {
                this.serverPosX += wrapper.posX;
                this.serverPosY += wrapper.posY;
                this.serverPosZ += wrapper.posZ;

                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;

                this.x = (double) this.serverPosX / 32.0D;
                this.y = (double) this.serverPosY / 32.0D;
                this.z = (double) this.serverPosZ / 32.0D;

                this.lastDeltaX = deltaX;
                this.lastDeltaY = deltaY;
                this.lastDeltaZ = deltaZ;
                this.deltaY = this.y - this.lastY;
                this.deltaZ = this.z - this.lastZ;
                this.deltaX = this.x - this.lastX;

                this.lastOnGround = this.onGround;
                this.onGround = !(PlayerUtil.getBlock(this.x - 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x + 0.5, this.y - 0.43, this.z - 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x + 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x - 0.5, this.y - 0.43, this.z + 0.5) instanceof BlockAir);

                this.lastIsRoughlyGround = this.isRoughlyGround;
                this.isRoughlyGround = !(PlayerUtil.getBlock(this.x - 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x + 0.5, this.y - 0.99, this.z - 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x + 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir) ||
                        !(PlayerUtil.getBlock(this.x - 0.5, this.y - 0.99, this.z + 0.5) instanceof BlockAir);

                if (this.onGround) {
                    this.lastGroundX = this.groundX;
                    this.lastGroundY = this.groundY;
                    this.lastGroundZ = this.groundZ;
                    this.groundX = this.x;
                    this.groundY = this.y;
                    this.groundZ = this.z;
                }
            }
        } else if (packet instanceof S18PacketEntityTeleport) {
            final S18PacketEntityTeleport wrapper = ((S18PacketEntityTeleport) packet);

            if (wrapper.getEntityId() == this.player.getEntityId()) {
                this.serverPosX = wrapper.getX();
                this.serverPosY = wrapper.getY();
                this.serverPosZ = wrapper.getZ();

                this.lastX = this.x;
                this.lastY = this.y;
                this.lastZ = this.z;

                this.x = (double) this.serverPosX / 32.0D;
                this.y = (double) this.serverPosY / 32.0D;
                this.z = (double) this.serverPosZ / 32.0D;

                this.ticksSinceLastTeleport = 0;
            }
        } else if (packet instanceof C03PacketPlayer) {
            this.ticksSinceLastTeleport++;
            this.ticksSinceLastVelocity++;
        } else if (packet instanceof S12PacketEntityVelocity && ((S12PacketEntityVelocity) packet).getEntityID() == this.player.getEntityId())
            this.ticksSinceLastVelocity = 0;

        this.checks.forEach(check -> check.handle(packet));
    }
}
