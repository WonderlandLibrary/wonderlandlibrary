package me.earth.phobos.features.modules.movement;

import io.netty.util.internal.ConcurrentSet;
import me.earth.phobos.event.events.MoveEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.PushEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TestPhase
        extends Module {
    private static TestPhase instance;
    private final Set<CPacketPlayer> packets = new ConcurrentSet();
    private final Map<Integer, IDtime> teleportmap = new ConcurrentHashMap<Integer, IDtime>();
    public Setting<Boolean> flight = this.register(new Setting<Boolean>("Flight", true));
    public Setting<Integer> flightMode = this.register(new Setting<Integer>("FMode", 0, 0, 1));
    public Setting<Boolean> doAntiFactor = this.register(new Setting<Boolean>("Factorize", true));
    public Setting<Double> antiFactor = this.register(new Setting<Double>("AntiFactor", 2.5, 0.1, 3.0));
    public Setting<Double> extraFactor = this.register(new Setting<Double>("ExtraFactor", 1.0, 0.1, 3.0));
    public Setting<Boolean> strafeFactor = this.register(new Setting<Boolean>("StrafeFactor", true));
    public Setting<Integer> loops = this.register(new Setting<Integer>("Loops", 1, 1, 10));
    public Setting<Boolean> clearTeleMap = this.register(new Setting<Boolean>("ClearMap", true));
    public Setting<Integer> mapTime = this.register(new Setting<Integer>("ClearTime", 30, 1, 500));
    public Setting<Boolean> clearIDs = this.register(new Setting<Boolean>("ClearIDs", true));
    public Setting<Boolean> setYaw = this.register(new Setting<Boolean>("SetYaw", true));
    public Setting<Boolean> setID = this.register(new Setting<Boolean>("SetID", true));
    public Setting<Boolean> setMove = this.register(new Setting<Boolean>("SetMove", false));
    public Setting<Boolean> nocliperino = this.register(new Setting<Boolean>("NoClip", false));
    public Setting<Boolean> sendTeleport = this.register(new Setting<Boolean>("Teleport", true));
    public Setting<Boolean> resetID = this.register(new Setting<Boolean>("ResetID", true));
    public Setting<Boolean> setPos = this.register(new Setting<Boolean>("SetPos", false));
    public Setting<Boolean> invalidPacket = this.register(new Setting<Boolean>("InvalidPacket", true));
    private int flightCounter = 0;
    private int teleportID = 0;

    public TestPhase() {
        super("Packetfly", "Uses packets to fly!", Module.Category.MOVEMENT, true, false, false);
        instance = this;
    }

    public static TestPhase getInstance() {
        if (instance == null) {
            instance = new TestPhase();
        }
        return instance;
    }

    @Override
    public void onToggle() {
    }

    @Override
    public void onTick() {
        this.teleportmap.entrySet().removeIf(idTime -> this.clearTeleMap.getValue() != false && idTime.getValue().getTimer().passedS(this.mapTime.getValue().intValue()));
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 1) {
            return;
        }
        TestPhase.mc.player.setVelocity(0.0, 0.0, 0.0);
        double speed = 0.0;
        boolean checkCollisionBoxes = this.checkHitBoxes();
        speed = TestPhase.mc.player.movementInput.jump && (checkCollisionBoxes || !EntityUtil.isMoving()) ? (this.flight.getValue().booleanValue() && !checkCollisionBoxes ? (this.flightMode.getValue() == 0 ? (this.resetCounter(10) ? -0.032 : 0.062) : (this.resetCounter(20) ? -0.032 : 0.062)) : 0.062) : (TestPhase.mc.player.movementInput.sneak ? -0.062 : (!checkCollisionBoxes ? (this.resetCounter(4) ? (this.flight.getValue().booleanValue() ? -0.04 : 0.0) : 0.0) : 0.0));
        if (this.doAntiFactor.getValue().booleanValue() && checkCollisionBoxes && EntityUtil.isMoving() && speed != 0.0) {
            speed /= this.antiFactor.getValue().doubleValue();
        }
        double[] strafing = this.getMotion(this.strafeFactor.getValue() != false && checkCollisionBoxes ? 0.031 : 0.26);
        for (int i = 1; i < this.loops.getValue() + 1; ++i) {
            TestPhase.mc.player.motionX = strafing[0] * (double) i * this.extraFactor.getValue();
            TestPhase.mc.player.motionY = speed * (double) i;
            TestPhase.mc.player.motionZ = strafing[1] * (double) i * this.extraFactor.getValue();
            this.sendPackets(TestPhase.mc.player.motionX, TestPhase.mc.player.motionY, TestPhase.mc.player.motionZ, this.sendTeleport.getValue());
        }
    }

    @SubscribeEvent
    public void onMove(MoveEvent event) {
        if (this.setMove.getValue().booleanValue() && this.flightCounter != 0) {
            event.setX(TestPhase.mc.player.motionX);
            event.setY(TestPhase.mc.player.motionY);
            event.setZ(TestPhase.mc.player.motionZ);
            if (this.nocliperino.getValue().booleanValue() && this.checkHitBoxes()) {
                TestPhase.mc.player.noClip = true;
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        CPacketPlayer packet;
        if (event.getPacket() instanceof CPacketPlayer && !this.packets.remove(packet = event.getPacket())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPushOutOfBlocks(PushEvent event) {
        if (event.getStage() == 1) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && !TestPhase.fullNullCheck()) {
            BlockPos pos;
            SPacketPlayerPosLook packet = event.getPacket();
            if (TestPhase.mc.player.isEntityAlive() && TestPhase.mc.world.isBlockLoaded(pos = new BlockPos(TestPhase.mc.player.posX, TestPhase.mc.player.posY, TestPhase.mc.player.posZ), false) && !(TestPhase.mc.currentScreen instanceof GuiDownloadTerrain) && this.clearIDs.getValue().booleanValue()) {
                this.teleportmap.remove(packet.getTeleportId());
            }
            if (this.setYaw.getValue().booleanValue()) {
                packet.yaw = TestPhase.mc.player.rotationYaw;
                packet.pitch = TestPhase.mc.player.rotationPitch;
            }
            if (this.setID.getValue().booleanValue()) {
                this.teleportID = packet.getTeleportId();
            }
        }
    }

    private boolean checkHitBoxes() {
        return !TestPhase.mc.world.getCollisionBoxes(TestPhase.mc.player, TestPhase.mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    private boolean resetCounter(int counter) {
        if (++this.flightCounter >= counter) {
            this.flightCounter = 0;
            return true;
        }
        return false;
    }

    private double[] getMotion(double speed) {
        float moveForward = TestPhase.mc.player.movementInput.moveForward;
        float moveStrafe = TestPhase.mc.player.movementInput.moveStrafe;
        float rotationYaw = TestPhase.mc.player.prevRotationYaw + (TestPhase.mc.player.rotationYaw - TestPhase.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double) moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double) moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double) moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double) moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private void sendPackets(double x, double y, double z, boolean teleport) {
        Vec3d vec = new Vec3d(x, y, z);
        Vec3d position = TestPhase.mc.player.getPositionVector().add(vec);
        Vec3d outOfBoundsVec = this.outOfBoundsVec(vec, position);
        this.packetSender(new CPacketPlayer.Position(position.x, position.y, position.z, TestPhase.mc.player.onGround));
        if (this.invalidPacket.getValue().booleanValue()) {
            this.packetSender(new CPacketPlayer.Position(outOfBoundsVec.x, outOfBoundsVec.y, outOfBoundsVec.z, TestPhase.mc.player.onGround));
        }
        if (this.setPos.getValue().booleanValue()) {
            TestPhase.mc.player.setPosition(position.x, position.y, position.z);
        }
        this.teleportPacket(position, teleport);
    }

    private void teleportPacket(Vec3d pos, boolean shouldTeleport) {
        if (shouldTeleport) {
            TestPhase.mc.player.connection.sendPacket(new CPacketConfirmTeleport(++this.teleportID));
            this.teleportmap.put(this.teleportID, new IDtime(pos, new Timer()));
        }
    }

    private Vec3d outOfBoundsVec(Vec3d offset, Vec3d position) {
        return position.add(0.0, 1337.0, 0.0);
    }

    private void packetSender(CPacketPlayer packet) {
        this.packets.add(packet);
        TestPhase.mc.player.connection.sendPacket(packet);
    }

    private void clean() {
        this.teleportmap.clear();
        this.flightCounter = 0;
        if (this.resetID.getValue().booleanValue()) {
            this.teleportID = 0;
        }
        this.packets.clear();
    }

    public static class IDtime {
        private final Vec3d pos;
        private final Timer timer;

        public IDtime(Vec3d pos, Timer timer) {
            this.pos = pos;
            this.timer = timer;
            this.timer.reset();
        }

        public Vec3d getPos() {
            return this.pos;
        }

        public Timer getTimer() {
            return this.timer;
        }
    }
}

