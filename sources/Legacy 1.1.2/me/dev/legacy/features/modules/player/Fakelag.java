package me.dev.legacy.features.modules.player;

import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.MathUtil;
import me.dev.legacy.util.Timer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketTabComplete;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Fakelag extends Module {
    public Setting<Boolean> cPacketPlayer = this.register(new Setting<Boolean>("CPacketPlayer", true));
    public Setting<Mode> autoOff = this.register(new Setting<Mode>("AutoOff", Mode.MANUAL));
    public Setting<Integer> timeLimit = this.register(new Setting<Object>("Time", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> this.autoOff.getValue() == Mode.TIME));
    public Setting<Integer> packetLimit = this.register(new Setting<Object>("Packets", Integer.valueOf(20), Integer.valueOf(1), Integer.valueOf(500), v -> this.autoOff.getValue() == Mode.PACKETS));
    public Setting<Float> distance = this.register(new Setting<Object>("Distance", Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(100.0f), v -> this.autoOff.getValue() == Mode.DISTANCE));
    private Timer timer = new Timer();
    private Queue<Packet<?>> packets = new ConcurrentLinkedQueue();
    private EntityOtherPlayerMP entity;
    private int packetsCanceled = 0;
    private BlockPos startPos = null;
    private static Fakelag INSTANCE = new Fakelag();

    public Fakelag() {
        super("Fakelag", "Fakelag.", Module.Category.PLAYER, true, false, false);
        this.setInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    public static Fakelag getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Fakelag();
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        if (!Fakelag.fullNullCheck()) {
            this.entity = new EntityOtherPlayerMP((World) Fakelag.mc.world, Fakelag.mc.session.getProfile());
            this.entity.copyLocationAndAnglesFrom((Entity) Fakelag.mc.player);
            this.entity.rotationYaw = Fakelag.mc.player.rotationYaw;
            this.entity.rotationYawHead = Fakelag.mc.player.rotationYawHead;
            this.entity.inventory.copyInventory(Fakelag.mc.player.inventory);
            Fakelag.mc.world.addEntityToWorld(6942069, (Entity) this.entity);
            this.startPos = Fakelag.mc.player.getPosition();
        } else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
    }

    @Override
    public void onUpdate() {
        if (Fakelag.nullCheck() || this.autoOff.getValue() == Mode.TIME && this.timer.passedS(this.timeLimit.getValue().intValue()) || this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && Fakelag.mc.player.getDistanceSq(this.startPos) >= MathUtil.square(this.distance.getValue().floatValue()) || this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= this.packetLimit.getValue()) {
            this.disable();
        }
    }

    @Override
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event) {
        if (event.getStage() == 0 && Fakelag.mc.world != null && !mc.isSingleplayer()) {
            Object packet = event.getPacket();
            if (this.cPacketPlayer.getValue().booleanValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!this.cPacketPlayer.getValue().booleanValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }

    @Override
    public void onDisable() {
        if (!Fakelag.fullNullCheck()) {
            Fakelag.mc.world.removeEntity((Entity) this.entity);
            while (!this.packets.isEmpty()) {
                Fakelag.mc.player.connection.sendPacket(this.packets.poll());
            }
        }
        this.startPos = null;
    }

    public static enum Mode {
        MANUAL,
        TIME,
        DISTANCE,
        PACKETS;

    }
}
