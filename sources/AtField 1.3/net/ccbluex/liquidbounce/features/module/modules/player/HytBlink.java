/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.play.client.CPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import java.awt.Color;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
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
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="HytBlink", description="Suspends all movement packets.", category=ModuleCategory.PLAYER)
public final class HytBlink
extends Module {
    private final LinkedBlockingQueue packets = new LinkedBlockingQueue();
    private final MSTimer pulseTimer;
    private boolean disableLogger;
    private final IntegerValue pulseDelayValue;
    private final BoolValue hytnewvalue;
    private final BoolValue pulseValue;
    private final LinkedList positions = new LinkedList();
    private IEntityOtherPlayerMP fakePlayer;

    @Override
    public void onDisable() {
        if (MinecraftInstance.mc.getThePlayer() == null) {
            return;
        }
        this.blink();
        IEntityOtherPlayerMP iEntityOtherPlayerMP = this.fakePlayer;
        if (iEntityOtherPlayerMP != null) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient != null) {
                iWorldClient.removeEntityFromWorld(iEntityOtherPlayerMP.getEntityId());
            }
            this.fakePlayer = null;
        }
    }

    private final void blink() {
        Object object;
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                object = MinecraftInstance.mc.getNetHandler().getNetworkManager();
                IPacket iPacket = (IPacket)this.packets.take();
                object.sendPacket(iPacket);
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
        Object object2 = object;
        synchronized (object2) {
            boolean bl3 = false;
            this.positions.clear();
            Unit unit = Unit.INSTANCE;
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (MinecraftInstance.mc.getThePlayer() == null || this.disableLogger) {
            return;
        }
        if (((Boolean)this.hytnewvalue.get()).booleanValue() && iPacket instanceof CPacketConfirmTransaction) {
            packetEvent.cancelEvent();
        }
        if (MinecraftInstance.classProvider.isCPacketPlayer(iPacket)) {
            packetEvent.cancelEvent();
        }
        if (iPacket instanceof SPacketEntityVelocity) {
            packetEvent.cancelEvent();
        }
        if (MinecraftInstance.classProvider.isCPacketPlayerPosition(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerPosLook(iPacket) || MinecraftInstance.classProvider.isCPacketPlayerBlockPlacement(iPacket) || MinecraftInstance.classProvider.isCPacketAnimation(iPacket) || MinecraftInstance.classProvider.isCPacketEntityAction(iPacket) || MinecraftInstance.classProvider.isCPacketUseEntity(iPacket)) {
            packetEvent.cancelEvent();
            this.packets.add(iPacket);
        }
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        Breadcrumbs breadcrumbs = (Breadcrumbs)LiquidBounce.INSTANCE.getModuleManager().getModule(Breadcrumbs.class);
        Breadcrumbs breadcrumbs2 = breadcrumbs;
        if (breadcrumbs2 == null) {
            Intrinsics.throwNpe();
        }
        Color color = (Boolean)breadcrumbs2.getColorRainbow().get() != false ? ColorUtils.rainbow() : new Color(((Number)breadcrumbs.getColorRedValue().get()).intValue(), ((Number)breadcrumbs.getColorGreenValue().get()).intValue(), ((Number)breadcrumbs.getColorBlueValue().get()).intValue());
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        LinkedList linkedList2 = linkedList;
        synchronized (linkedList2) {
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

    public HytBlink() {
        this.hytnewvalue = new BoolValue("HytBlink", false);
        this.pulseValue = new BoolValue("Pulse", false);
        this.pulseDelayValue = new IntegerValue("PulseDelay", 1000, 500, 5000);
        this.pulseTimer = new MSTimer();
    }

    @Override
    public String getTag() {
        return String.valueOf(this.packets.size());
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
        LinkedList linkedList2 = linkedList;
        synchronized (linkedList2) {
            boolean bl3 = false;
            bl2 = this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY(), iEntityPlayerSP2.getPosZ()});
        }
        if (!((Boolean)this.pulseValue.get()).booleanValue() || !this.pulseTimer.hasTimePassed(((Number)this.pulseDelayValue.get()).intValue())) return;
        this.blink();
        this.pulseTimer.reset();
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
        Object object2 = object;
        synchronized (object2) {
            boolean bl3 = false;
            this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY() + (double)(iEntityPlayerSP2.getEyeHeight() / 2.0f), iEntityPlayerSP2.getPosZ()});
            bl2 = this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY(), iEntityPlayerSP2.getPosZ()});
        }
        this.pulseTimer.reset();
    }
}

