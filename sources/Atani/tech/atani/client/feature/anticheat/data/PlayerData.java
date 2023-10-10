package tech.atani.client.feature.anticheat.data;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;

import net.minecraft.network.play.server.S18PacketEntityTeleport;
import tech.atani.client.feature.anticheat.check.Check;
import tech.atani.client.feature.anticheat.check.CheckStorage;
import tech.atani.client.feature.anticheat.data.tracker.impl.AimTracker;
import tech.atani.client.feature.anticheat.data.tracker.impl.MovementTracker;

import java.util.List;

public class PlayerData {

    private final EntityOtherPlayerMP player;

    private final MovementTracker movementTracker;
    private final AimTracker aimTracker;

    private final List<Check> checks;

    private int ticksExisted;

    private long lastTeleport;

    public PlayerData(EntityOtherPlayerMP player) {
        this.player = player;

        this.movementTracker = new MovementTracker(this);
        this.aimTracker = new AimTracker(this);
        this.checks = CheckStorage.getInstance().loadChecks(this);
    }

    public void handle(Packet<?> packet) {
        if(packet instanceof S18PacketEntityTeleport) {
            lastTeleport = System.currentTimeMillis();
        }

        movementTracker.handle(packet);
        aimTracker.handle(packet);
        checks.forEach(c -> c.handle(packet));
    }

    public void updateTicks() {
        ticksExisted++;
    }

    public EntityOtherPlayerMP getPlayer() {
        return player;
    }

    public MovementTracker getMovementTracker() {
        return movementTracker;
    }

    public AimTracker getAimTracker() {
        return aimTracker;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public int getTicksExisted() {
        return ticksExisted;
    }

    public long getLastTeleport() {
        return lastTeleport;
    }
}