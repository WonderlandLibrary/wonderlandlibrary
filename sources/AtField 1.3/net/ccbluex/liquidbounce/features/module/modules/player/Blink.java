/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.awt.Color;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.player.IEntityOtherPlayerMP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.render.Breadcrumbs;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Blink", description="Suspends all movement packets.", category=ModuleCategory.PLAYER)
public final class Blink
extends Module {
    private final LinkedList positions;
    private final LinkedBlockingQueue packets = new LinkedBlockingQueue();
    private boolean disableLogger;
    private final MSTimer pulseTimer;
    private IEntityOtherPlayerMP fakePlayer;
    private final BoolValue pulseValue;
    private final IntegerValue pulseDelayValue;

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        this.blink();
        IEntityOtherPlayerMP iEntityOtherPlayerMP = this.fakePlayer;
        if (iEntityOtherPlayerMP != null) {
            IWorldClient iWorldClient;
            IWorldClient iWorldClient2 = iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient2 != null) {
                iWorldClient2.removeEntityFromWorld(iEntityOtherPlayerMP.getEntityId());
            }
            this.fakePlayer = null;
        }
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        Breadcrumbs breadcrumbs;
        Breadcrumbs breadcrumbs2 = breadcrumbs = (Breadcrumbs)LiquidBounce.INSTANCE.getModuleManager().getModule(Breadcrumbs.class);
        if (breadcrumbs2 == null) {
            Intrinsics.throwNpe();
        }
        Color color = (Boolean)breadcrumbs2.getColorRainbow().get() != false ? ColorUtils.rainbow() : new Color(((Number)breadcrumbs.getColorRedValue().get()).intValue(), ((Number)breadcrumbs.getColorGreenValue().get()).intValue(), ((Number)breadcrumbs.getColorBlueValue().get()).intValue());
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedList) {
            boolean bl3 = false;
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            MinecraftInstance.mc.getEntityRenderer().disableLightmap();
            GL11.glBegin((int)3);
            RenderUtils.glColor(color);
            double d = MinecraftInstance.mc.getRenderManager().getViewerPosX();
            double d2 = MinecraftInstance.mc.getRenderManager().getViewerPosY();
            double d3 = MinecraftInstance.mc.getRenderManager().getViewerPosZ();
            for (double[] dArray : this.positions) {
                GL11.glVertex3d((double)(dArray[0] - d), (double)(dArray[1] - d2), (double)(dArray[2] - d3));
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
            Unit unit = Unit.INSTANCE;
        }
    }

    @Override
    public String getTag() {
        return String.valueOf(this.packets.size());
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.mc.getThePlayer() == null || this.disableLogger) {
            return;
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket)) {
            packetEvent.cancelEvent();
        }
        if (MinecraftInstance.classProvider.isCPacketPlayerPosition(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerPosLook(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerBlockPlacement(iPacket) || MinecraftInstance.classProvider.isCPacketAnimation(iPacket) || MinecraftInstance.classProvider.isCPacketEntityAction(iPacket) || MinecraftInstance.classProvider.isCPacketUseEntity(iPacket)) {
            packetEvent.cancelEvent();
            this.packets.add(iPacket);
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        synchronized (linkedList) {
            boolean bl5 = false;
            boolean bl6 = false;
            bl2 = this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY(), iEntityPlayerSP2.getPosZ()});
            Unit unit = Unit.INSTANCE;
        }
        if (!((Boolean)this.pulseValue.get()).booleanValue()) return;
        Object object = this.pulseDelayValue.get();
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Number");
        }
        if (!this.pulseTimer.hasTimePassed(((Number)object).intValue())) return;
        this.blink();
        this.pulseTimer.reset();
    }

    public Blink() {
        this.positions = new LinkedList();
        this.pulseValue = new BoolValue("Pulse", false);
        this.pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000);
        this.pulseTimer = new MSTimer();
    }

    @Override
    public void onEnable() {
        Object object;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        if (!((Boolean)this.pulseValue.get()).booleanValue()) {
            object = MinecraftInstance.mc.getTheWorld();
            if (object == null) {
                Intrinsics.throwNpe();
            }
            IEntityOtherPlayerMP iEntityOtherPlayerMP = MinecraftInstance.classProvider.createEntityOtherPlayerMP((IWorldClient)object, iEntityPlayerSP2.getGameProfile());
            iEntityOtherPlayerMP.setRotationYawHead(iEntityPlayerSP2.getRotationYawHead());
            iEntityOtherPlayerMP.setRenderYawOffset(iEntityPlayerSP2.getRenderYawOffset());
            iEntityOtherPlayerMP.copyLocationAndAnglesFrom(iEntityPlayerSP2);
            iEntityOtherPlayerMP.setRotationYawHead(iEntityPlayerSP2.getRotationYawHead());
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            iWorldClient.addEntityToWorld(-1337, iEntityOtherPlayerMP);
            this.fakePlayer = iEntityOtherPlayerMP;
        }
        object = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        synchronized (object) {
            boolean bl5 = false;
            boolean bl6 = false;
            this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY() + (double)(iEntityPlayerSP2.getEyeHeight() / 2.0f), iEntityPlayerSP2.getPosZ()});
            bl2 = this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY(), iEntityPlayerSP2.getPosZ()});
            Unit unit = Unit.INSTANCE;
        }
        this.pulseTimer.reset();
    }

    private final void blink() {
        Object object;
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                object = MinecraftInstance.mc.getNetHandler().getNetworkManager();
                Object e = this.packets.take();
                object.sendPacket((IPacket)e);
            }
            this.disableLogger = false;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.disableLogger = false;
        }
        object = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        synchronized (object) {
            boolean bl5 = false;
            boolean bl6 = false;
            this.positions.clear();
            Unit unit = Unit.INSTANCE;
            Unit unit2 = Unit.INSTANCE;
        }
    }
}

