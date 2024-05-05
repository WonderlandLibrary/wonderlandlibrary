/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.INetHandler
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayClient
 *  net.minecraft.network.play.client.CPacketAnimation
 *  net.minecraft.network.play.client.CPacketEntityAction
 *  net.minecraft.network.play.client.CPacketPlayer
 *  net.minecraft.network.play.client.CPacketPlayer$Position
 *  net.minecraft.network.play.client.CPacketPlayer$PositionRotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.network.play.server.SPacketConfirmTransaction
 *  net.minecraft.network.play.server.SPacketEntityVelocity
 *  net.minecraft.network.play.server.SPacketPlayerPosLook
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketConfirmTransaction;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

@ModuleInfo(name="GrimFull", description="GrimFull", category=ModuleCategory.COMBAT)
public final class GrimFull
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"FDP", "New"}, "New");
    private final BoolValue disable = new BoolValue("S08Disable", true);
    private final BoolValue reEnable = new BoolValue("ReEnable", true);
    private int cancelPacket = 6;
    private int resetPersecFDP = 8;
    private int grimTCancel;
    private int updatesFDP;
    private int cancelPackets;
    private int resetPersec = 8;
    private int updates;
    private final LinkedBlockingQueue<Packet<?>> packets = new LinkedBlockingQueue();
    private boolean disableLogger;
    private final LinkedList<Packet<INetHandlerPlayClient>> inBus = new LinkedList();

    @Override
    public void onEnable() {
        this.grimTCancel = 0;
        this.cancelPackets = 0;
    }

    @Override
    public void onDisable() {
        if (((String)this.modeValue.get()).equals("New")) {
            this.closeblink();
        }
    }

    /*
     * Unable to fully structure code
     */
    @EventTarget
    public final void onPacket(PacketEvent event) {
        v0 = MinecraftInstance.mc.getThePlayer();
        if (v0 == null) {
            return;
        }
        thePlayer = v0;
        packet = event.getPacket();
        $this$unwrap$iv = event.getPacket();
        $i$f$unwrap = false;
        packet1 = ((PacketImpl)$this$unwrap$iv).getWrapped();
        $fun$startBlink$1 = new Function0<Unit>(this, event){
            final /* synthetic */ GrimFull this$0;
            final /* synthetic */ PacketEvent $event;

            public final void invoke() {
                IPacket $this$unwrap$iv = this.$event.getPacket();
                boolean $i$f$unwrap = false;
                T packet2 = ((PacketImpl)$this$unwrap$iv).getWrapped();
                if (MinecraftInstance.mc.getThePlayer() == null || GrimFull.access$getDisableLogger$p(this.this$0)) {
                    return;
                }
                if (packet2 instanceof CPacketPlayer) {
                    this.$event.cancelEvent();
                }
                if (packet2 instanceof CPacketPlayer.Position || packet2 instanceof CPacketPlayer.PositionRotation || packet2 instanceof CPacketPlayerTryUseItemOnBlock || packet2 instanceof CPacketAnimation || packet2 instanceof CPacketEntityAction || packet2 instanceof CPacketUseEntity || StringsKt.startsWith((String)packet2.getClass().getSimpleName(), (String)"C", (boolean)true)) {
                    this.$event.cancelEvent();
                    GrimFull.access$getPackets$p(this.this$0).add(packet2);
                }
                if (StringsKt.startsWith((String)packet2.getClass().getSimpleName(), (String)"S", (boolean)true)) {
                    if (packet2 instanceof SPacketEntityVelocity) {
                        Object object = MinecraftInstance.mc.getTheWorld();
                        if (object == null || (object = object.getEntityByID(((SPacketEntityVelocity)packet2).func_149412_c())) == null) {
                            return;
                        }
                        if (object.equals(MinecraftInstance.mc.getThePlayer())) {
                            return;
                        }
                    }
                    this.$event.cancelEvent();
                    T t = packet2;
                    if (t == null) {
                        throw new TypeCastException("null cannot be cast to non-null type net.minecraft.network.Packet<net.minecraft.network.play.INetHandlerPlayClient>");
                    }
                    GrimFull.access$getInBus$p(this.this$0).add(t);
                }
            }
            {
                this.this$0 = grimFull;
                this.$event = packetEvent;
                super(0);
            }
        };
        $i$f$unwrap = (String)this.modeValue.get();
        switch ($i$f$unwrap.hashCode()) {
            case 78208: {
                if (!$i$f$unwrap.equals("New")) ** break;
                break;
            }
            case 69458: {
                if (!$i$f$unwrap.equals("FDP") || !MovementUtils.isMoving()) ** break;
                if (MinecraftInstance.classProvider.isSPacketEntityVelocity(packet)) {
                    packetEntityVelocity = packet.asSPacketEntityVelocity();
                    v1 = MinecraftInstance.mc.getTheWorld();
                    if (v1 == null || (v1 = v1.getEntityByID(packetEntityVelocity.getEntityID())) == null) {
                        return;
                    }
                    if (v1.equals(thePlayer) ^ true) {
                        return;
                    }
                    event.cancelEvent();
                    this.grimTCancel = this.cancelPacket;
                }
                if (!(packet1 instanceof SPacketConfirmTransaction) || this.grimTCancel <= 0) ** break;
                event.cancelEvent();
                packetEntityVelocity = this.grimTCancel;
                this.grimTCancel = packetEntityVelocity + -1;
                ** break;
            }
        }
        if (packet1 instanceof SPacketEntityVelocity) {
            packetEntityVelocity = packet.asSPacketEntityVelocity();
            v2 = MinecraftInstance.mc.getTheWorld();
            if (v2 == null || (v2 = v2.getEntityByID(packetEntityVelocity.getEntityID())) == null) {
                return;
            }
            if (v2.equals(thePlayer) ^ true) {
                return;
            }
            event.cancelEvent();
            this.cancelPackets = 3;
        }
        if (this.cancelPackets <= 0) ** break;
        $fun$startBlink$1.invoke();
lbl42:
        // 7 sources

        if (packet1 instanceof SPacketPlayerPosLook && ((Boolean)this.disable.get()).booleanValue()) {
            v3 = LiquidBounce.INSTANCE.getModuleManager().getModule(GrimFull.class);
            if (v3 == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.GrimFull");
            }
            velocity = (GrimFull)v3;
            velocity.setState(false);
            if (((Boolean)this.reEnable.get()).booleanValue()) {
                new Thread(new Runnable(velocity){
                    final /* synthetic */ GrimFull $velocity;

                    public final void run() {
                        try {
                            Thread.sleep(1000L);
                            this.$velocity.setState(true);
                        }
                        catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    {
                        this.$velocity = grimFull;
                    }
                }).start();
            }
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent event) {
        int n = this.updatesFDP;
        this.updatesFDP = n + 1;
        if (this.resetPersecFDP > 0 && (this.updatesFDP >= 0 || this.updatesFDP >= this.resetPersecFDP)) {
            this.updatesFDP = 0;
            if (this.grimTCancel > 0) {
                n = this.grimTCancel;
                this.grimTCancel = n + -1;
            }
        }
        n = this.updates;
        this.updates = n + 1;
        if (this.resetPersec > 0 && (this.updates >= 0 || this.updates >= this.resetPersec)) {
            this.updates = 0;
            if (this.cancelPackets > 0) {
                n = this.cancelPackets;
                this.cancelPackets = n + -1;
            }
        }
        if (this.cancelPackets == 0 && ((String)this.modeValue.get()).equals("New")) {
            this.closeblink();
        }
    }

    private final void closeblink() {
        try {
            this.disableLogger = true;
            while (!this.packets.isEmpty()) {
                NetHandlerPlayClient netHandlerPlayClient = MinecraftInstance.mc2.func_147114_u();
                if (netHandlerPlayClient == null) {
                    Intrinsics.throwNpe();
                }
                netHandlerPlayClient.func_147298_b().func_179290_a(this.packets.take());
            }
            while (!this.inBus.isEmpty()) {
                Packet<INetHandlerPlayClient> packet = this.inBus.poll();
                if (packet == null) continue;
                Minecraft minecraft = MinecraftInstance.mc2;
                if (minecraft == null) {
                    Intrinsics.throwNpe();
                }
                packet.func_148833_a((INetHandler)minecraft.func_147114_u());
            }
            this.disableLogger = false;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.disableLogger = false;
        }
    }

    public static final /* synthetic */ boolean access$getDisableLogger$p(GrimFull $this) {
        return $this.disableLogger;
    }

    public static final /* synthetic */ void access$setDisableLogger$p(GrimFull $this, boolean bl) {
        $this.disableLogger = bl;
    }

    public static final /* synthetic */ LinkedBlockingQueue access$getPackets$p(GrimFull $this) {
        return $this.packets;
    }

    public static final /* synthetic */ LinkedList access$getInBus$p(GrimFull $this) {
        return $this.inBus;
    }
}

