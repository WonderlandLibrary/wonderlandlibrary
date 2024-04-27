package me.aquavit.liquidsense.module.modules.player;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.event.EventTarget;
import me.aquavit.liquidsense.event.events.PacketEvent;
import me.aquavit.liquidsense.event.events.Render3DEvent;
import me.aquavit.liquidsense.event.events.UpdateEvent;
import me.aquavit.liquidsense.module.Module;
import me.aquavit.liquidsense.module.ModuleCategory;
import me.aquavit.liquidsense.module.ModuleInfo;
import me.aquavit.liquidsense.module.modules.render.Breadcrumbs;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import me.aquavit.liquidsense.utils.timer.MSTimer;
import me.aquavit.liquidsense.value.BoolValue;
import me.aquavit.liquidsense.value.IntegerValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@ModuleInfo(name = "Blink", description = "Suspends all movement packets.", category = ModuleCategory.PLAYER)
public class Blink extends Module {

    private final List<Packet> packets = new ArrayList<>();
    private EntityOtherPlayerMP fakePlayer = null;
    private boolean disableLogger;
    private final LinkedList<double[]> positions = new LinkedList<>();

    private final BoolValue pulseValue = new BoolValue("Pulse", false);
    private final IntegerValue pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000);

    private final MSTimer pulseTimer = new MSTimer();

    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;

        fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
        fakePlayer.clonePlayer(mc.thePlayer, true);
        fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
        fakePlayer.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(-9100, fakePlayer);

        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + (mc.thePlayer.getEyeHeight() / 2), mc.thePlayer.posZ});
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        pulseTimer.reset();
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer == null || fakePlayer == null)
            return;

        blink();
        mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
        fakePlayer = null;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();

        if (mc.thePlayer == null || disableLogger)
            return;

        if (packet instanceof C03PacketPlayer) // Cancel all movement stuff
            event.cancelEvent();

        if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook ||
                packet instanceof C08PacketPlayerBlockPlacement ||
                packet instanceof C0APacketAnimation ||
                packet instanceof C0BPacketEntityAction || packet instanceof C02PacketUseEntity) {
            event.cancelEvent();

            packets.add(packet);
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        synchronized(positions) {
            positions.add(new double[] {mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ});
        }

        if(pulseValue.get() && pulseTimer.hasTimePassed(pulseDelayValue.get())) {
            blink();
            pulseTimer.reset();
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {
        final Breadcrumbs breadcrumbs = (Breadcrumbs) LiquidSense.moduleManager.getModule(Breadcrumbs.class);
        final Color color = breadcrumbs.colorRainbow.get() ? ColorUtils.rainbow() : new Color(breadcrumbs.colorRedValue.get(), breadcrumbs.colorGreenValue.get(), breadcrumbs.colorBlueValue.get());

        synchronized(positions) {
            glPushMatrix();

            glDisable(GL_TEXTURE_2D);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glEnable(GL_LINE_SMOOTH);
            glEnable(GL_BLEND);
            glDisable(GL_DEPTH_TEST);
            mc.entityRenderer.disableLightmap();
            glBegin(GL_LINE_STRIP);
            RenderUtils.glColor(color);
            final double renderPosX = mc.getRenderManager().viewerPosX;
            final double renderPosY = mc.getRenderManager().viewerPosY;
            final double renderPosZ = mc.getRenderManager().viewerPosZ;

            for(final double[] pos : positions)
                glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);

            glColor4d(1, 1, 1, 1);
            glEnd();
            glEnable(GL_DEPTH_TEST);
            glDisable(GL_LINE_SMOOTH);
            glDisable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glPopMatrix();
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(packets.size());
    }

    private void blink() {
        try {
            disableLogger = true;

            final Iterator<Packet> packetIterator = packets.iterator();
            for(; packetIterator.hasNext(); ) {
                mc.getNetHandler().addToSendQueue(packetIterator.next());
                packetIterator.remove();
            }

            disableLogger = false;
        }catch(final Exception e) {
            e.printStackTrace();
            disableLogger = false;
        }

        synchronized(positions) {
            positions.clear();
        }
    }
}
