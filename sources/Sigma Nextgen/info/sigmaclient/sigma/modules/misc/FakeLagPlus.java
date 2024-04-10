package info.sigmaclient.sigma.modules.misc;

import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.EventManager;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.event.player.WindowUpdateEvent;
import info.sigmaclient.sigma.event.player.WorldEvent;
import info.sigmaclient.sigma.event.render.Render3DEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.combat.Killaura;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SDestroyEntitiesPacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.network.play.server.SUpdateHealthPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.ࡅ揩柿괠竁頉;
import static info.sigmaclient.sigma.sigma5.utils.BoxOutlineESP.骰괠啖㕠挐酋;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class FakeLagPlus extends Module {
    NumberValue range = new NumberValue("Range", 3, 0.1,8, NumberValue.NUMBER_TYPE.FLOAT);
    NumberValue duration = new NumberValue("Duration", 0.5, 0.1,3, NumberValue.NUMBER_TYPE.FLOAT);
    BooleanValue hurt = new BooleanValue("Stop On Hurt", true);
    BooleanValue hurtTarget = new BooleanValue("No Hurt Reset", true);
    public FakeLagPlus() {
        super("FakeLag", Category.Misc, "Very nice");
        registerValue(range);
        registerValue(duration);
        registerValue(hurt);
        registerValue(hurtTarget);
    }
    CopyOnWriteArrayList<IPacket<?>> packet = new CopyOnWriteArrayList<>();
    boolean stopPackets = false, lastISOK = false;
    int durationTicks = 0;
    Vector3d lastPosition = null;
    void stop(boolean flashPacket) {
        if (flashPacket)
            for (IPacket<?> packet1 : packet) {
                PacketEvent packetEvent = new PacketEvent(packet1).setRev();
                EventManager.call(packetEvent);
                if (!packetEvent.cancelable)
                    ((IPacket<INetHandler>) packet1).processPacket(mc.getConnection());
            }
        packet.clear();
        stopPackets = false;
        lastISOK = false;
        durationTicks = 0;
        lastPosition = null;
    }

    @Override
    public void onWindowUpdateEvent(WindowUpdateEvent event) {
        LivingEntity target = Killaura.attackTarget;
        if (target == null || (hurt.isEnable() && mc.player.hurtTime > 0)) {
            stop(true);
            return;
        }
        // duration
        if (durationTicks > 0) {
            // stopping...
            durationTicks--;
            stopPackets = true;
            if ((hurtTarget.isEnable() && target.hurtTime == 0)) {
                stop(true);
                durationTicks = 0;
            }
        } else {
            stop(true);
        }
        int calcDuration = (int) (duration.getValue().floatValue() * 20);
        boolean ok = mc.player.getDistanceNearest3(lastPosition == null ? mc.player.getPositionVec() : lastPosition, target) <= range.getValue().floatValue();
        if (ok && durationTicks == 0) {
            durationTicks = calcDuration;
            lastPosition = mc.player.getPositionVec();
        }
        lastISOK = ok;
        super.onWindowUpdateEvent(event);
    }

    @Override
    public void onWorldEvent(WorldEvent event) {
        stop(true);
        super.onWorldEvent(event);
    }
    @Override
    public void onEnable() {
        stop(true);
        super.onEnable();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if (event.isPre()) return;
    }
    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.isSend()) return;
        IPacket<?> packet = event.getPacket();
        if(stopPackets && !event.cancelable && !(event.packet instanceof SUpdateHealthPacket || event.packet instanceof SEntityStatusPacket || event.packet instanceof SDestroyEntitiesPacket)){
            this.packet.add(packet);
            event.cancelable = true;
        }
    }

    @Override
    public void onRender3DEvent(Render3DEvent event) {
        if(lastPosition != null){
            GL11.glPushMatrix();
            GL11.glDisable(2929);
            RenderUtils.renderPos r = RenderUtils.getRenderPos();
            final double n = lastPosition.getX() - r.renderPosX;
            final double n2 = lastPosition.getY() - r.renderPosY;
            final double n3 = lastPosition.getZ() - r.renderPosZ;
            float halfWidth = mc.player.getWidth() / 2.0F;
            骰괠啖㕠挐酋(new AxisAlignedBB(n - halfWidth, n2, n3 - halfWidth, n + halfWidth, n2 + mc.player.getHeight(), n3 + halfWidth),1, new Color(255,255,255,100).getRGB());
            GL11.glEnable(2929);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onDisable() {
        stop(true);
        super.onDisable();
    }

}
