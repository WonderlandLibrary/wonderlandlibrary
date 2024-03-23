package dev.zprestige.prestige.client.module.impl.movement;

import dev.zprestige.prestige.client.Prestige;
import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.Priority;
import dev.zprestige.prestige.client.event.impl.PacketReceiveEvent;
import dev.zprestige.prestige.client.event.impl.PacketSendEvent;
import dev.zprestige.prestige.client.event.impl.Render3DEvent;
import dev.zprestige.prestige.client.event.impl.TickEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.FloatSetting;
import dev.zprestige.prestige.client.util.impl.PacketUtil;
import dev.zprestige.prestige.client.util.impl.RenderHelper;
import dev.zprestige.prestige.client.util.impl.RenderUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;

public class FakeLag extends Module {

    public DragSetting choke;
    public BooleanSetting holdOnKB;
    public FloatSetting kbHoldTime;
    public BooleanSetting render;
    public TimerUtil timer;
    public Vec3d pos;
    public ArrayList unusedList;
    public Queue<Packet<?>> packets;
    public boolean bruh;

    public FakeLag() {
        super("Fake Lag", Category.Movement, "Lags your packets to make it seem like you are lagging");
        choke = setting("Choke", 100, 150, 0, 1000).description("Interval between packet bursts");
        holdOnKB = setting("Hold On KB", false).description("When to hold packets (Can be blatant)");
        kbHoldTime = setting("KB Hold Time", 100f, 0f, 1000f).invokeVisibility(arg_0 -> holdOnKB.getObject()).description("How long to hold packets for when you get knocked back");
        render = setting("Render", true).description("Renders a circle to show how long have been holding packets for");
        packets = new ConcurrentLinkedQueue();
        timer = new TimerUtil();
        pos = Vec3d.ZERO;
        unusedList = new ArrayList();
    }

    @Override
    public void onEnable() {
        timer.reset();
        pos = getMc().player.getPos();
    }

    @Override
    public void onDisable() {
        reset();
    }

    @EventListener
    public void event(PacketReceiveEvent event) {
        if (getMc().world == null || getMc().player == null) {
            return;
        }
        if (event.getPacket() instanceof ExplosionS2CPacket) {
            reset();
            choke.setValue();
        }
        if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet) {
            if (packet.getId() == getMc().player.getId() && unusedList.isEmpty() && holdOnKB.getObject()) {
                reset();
                choke.setValue(kbHoldTime.getObject());
            }
        }
    }

    @EventListener(getPriority=Priority.LOWEST)
    public void event(PacketSendEvent event) {
        if (getMc().world == null || getMc().player == null) {
            return;
        }
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket || event.getPacket() instanceof HandSwingC2SPacket || event.getPacket() instanceof PlayerInteractBlockC2SPacket || event.getPacket() instanceof PlayerInteractItemC2SPacket || event.getPacket() instanceof ClickSlotC2SPacket) {
            reset();
            return;
        }
        if (!bruh) {
            packets.add(event.getPacket());
            event.setCancelled();
        }
    }

    @EventListener
    public void event(Render3DEvent event) {
        if (render.getObject()) {
            RenderHelper.setMatrixStack(event.getMatrixStack());
            RenderUtil.setCameraAction();
            RenderUtil.renderColoredEllipse3D((float) pos.x, (float) pos.y, (float) pos.z, Math.min(0.5f, timer.getElapsedTime() / 500), Prestige.Companion.getModuleManager().getMenu().getColor().getObject());
            event.getMatrixStack().pop();
        }
    }

    @EventListener
    public void event(TickEvent event) {
        if (timer.delay(choke)) {
            if (getMc().player != null && !getMc().player.isUsingItem()) {
                reset();
                choke.setValue();
            }
        }
    }

    private void reset() {
        if (getMc().player == null || getMc().world == null) {
            return;
        }
        bruh = true;
        while (!packets.isEmpty()) {
            PacketUtil.INSTANCE.sendPacket(packets.poll());
        }
        bruh = false;
        timer.reset();
        pos = getMc().player.getPos();
    }


    @Override
    public String method224() {
        return (float)Math.ceil(choke.getValue()) + "ms";
    }
}
