/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AtAllProvider", description="Automatically mentions everyone on the server when using '@a' in your message.", category=ModuleCategory.MISC)
public final class AtAllProvider
extends Module {
    private final LinkedBlockingQueue sendQueue;
    private long delay;
    private final MSTimer msTimer;
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 500, 0, 20000){
        final AtAllProvider this$0;

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)AtAllProvider.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (n3 < n2) {
                this.set((Object)n3);
            }
        }
        {
            this.this$0 = atAllProvider;
            super(string, n, n2, n3);
        }

        static {
        }
    };
    private final BoolValue retryValue;
    private final List retryQueue;
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 1000, 0, 20000){
        final AtAllProvider this$0;

        protected void onChanged(int n, int n2) {
            int n3 = ((Number)AtAllProvider.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (n3 > n2) {
                this.set((Object)n3);
            }
        }

        static {
        }

        public void onChanged(Object object, Object object2) {
            this.onChanged(((Number)object).intValue(), ((Number)object2).intValue());
        }
        {
            this.this$0 = atAllProvider;
            super(string, n, n2, n3);
        }
    };

    public AtAllProvider() {
        this.retryValue = new BoolValue("Retry", false);
        this.sendQueue = new LinkedBlockingQueue();
        this.retryQueue = new ArrayList();
        this.msTimer = new MSTimer();
        this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
    }

    @Override
    public void onDisable() {
        boolean bl;
        Collection collection = this.sendQueue;
        boolean bl2 = false;
        boolean bl3 = false;
        synchronized (collection) {
            bl = false;
            this.sendQueue.clear();
            Unit unit = Unit.INSTANCE;
        }
        collection = this.retryQueue;
        bl2 = false;
        boolean bl4 = false;
        synchronized (collection) {
            bl = false;
            this.retryQueue.clear();
            Unit unit = Unit.INSTANCE;
        }
        super.onDisable();
    }

    public static final IntegerValue access$getMinDelayValue$p(AtAllProvider atAllProvider) {
        return atAllProvider.minDelayValue;
    }

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        if (!this.msTimer.hasTimePassed(this.delay)) {
            return;
        }
        LinkedBlockingQueue linkedBlockingQueue = this.sendQueue;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedBlockingQueue) {
            boolean bl3 = false;
            if (this.sendQueue.isEmpty()) {
                if (!((Boolean)this.retryValue.get()).booleanValue() || this.retryQueue.isEmpty()) {
                    return;
                }
                this.sendQueue.addAll(this.retryQueue);
            }
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            iEntityPlayerSP.sendChatMessage((String)this.sendQueue.take());
            this.msTimer.reset();
            this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
            Unit unit = Unit.INSTANCE;
        }
    }

    @EventTarget
    public final void onPacket(PacketEvent packetEvent) {
        ICPacketChatMessage iCPacketChatMessage;
        String string;
        if (MinecraftInstance.classProvider.isCPacketChatMessage(packetEvent.getPacket()) && (string = (iCPacketChatMessage = packetEvent.getPacket().asCPacketChatMessage()).getMessage()).equals("@a")) {
            LinkedBlockingQueue linkedBlockingQueue = this.sendQueue;
            boolean bl = false;
            boolean bl2 = false;
            synchronized (linkedBlockingQueue) {
                boolean bl3 = false;
                for (Object object : MinecraftInstance.mc.getNetHandler().getPlayerInfoMap()) {
                    String string2 = object.getGameProfile().getName();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (string2.equals(iEntityPlayerSP.getName())) continue;
                    this.sendQueue.add(StringsKt.replace$default((String)string, (String)"@a", (String)string2, (boolean)false, (int)4, null));
                }
                if (((Boolean)this.retryValue.get()).booleanValue()) {
                    Object object;
                    object = this.retryQueue;
                    boolean bl4 = false;
                    boolean bl5 = false;
                    synchronized (object) {
                        boolean bl6 = false;
                        this.retryQueue.clear();
                        Collection collection = this.sendQueue;
                        List list = this.retryQueue;
                        boolean bl7 = false;
                        Collection collection2 = collection;
                        String[] stringArray = collection2.toArray(new String[0]);
                        if (stringArray == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        String[] stringArray2 = stringArray;
                        bl5 = list.addAll(CollectionsKt.listOf((Object[])Arrays.copyOf(stringArray2, stringArray2.length)));
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
            packetEvent.cancelEvent();
        }
    }

    public static final IntegerValue access$getMaxDelayValue$p(AtAllProvider atAllProvider) {
        return atAllProvider.maxDelayValue;
    }
}

