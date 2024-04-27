package net.ccbluex.liquidbounce.features.module.modules.misc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AtAllProvider", description="Automatically mentions everyone on the server when using '@a' in your message.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000J\n\n\n\b\n\t\n\u0000\n\n\b\n\n\u0000\n!\n\n\u0000\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J\b0HJ020HJ02\b0HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n\b0\f0X¢\n\u0000R\r0X¢\n\u0000R\b0\f0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/AtAllProvider;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delay", "", "maxDelayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "minDelayValue", "msTimer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "retryQueue", "", "", "retryValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "sendQueue", "Ljava/util/concurrent/LinkedBlockingQueue;", "onDisable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class AtAllProvider
extends Module {
    private final IntegerValue minDelayValue = new IntegerValue(this, "MinDelay", 500, 0, 20000){
        final AtAllProvider this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)AtAllProvider.access$getMaxDelayValue$p(this.this$0).get()).intValue();
            if (i < newValue) {
                this.set(i);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final IntegerValue maxDelayValue = new IntegerValue(this, "MaxDelay", 1000, 0, 20000){
        final AtAllProvider this$0;

        protected void onChanged(int oldValue, int newValue) {
            int i = ((Number)AtAllProvider.access$getMinDelayValue$p(this.this$0).get()).intValue();
            if (i > newValue) {
                this.set(i);
            }
        }
        {
            this.this$0 = $outer;
            super($super_call_param$1, $super_call_param$2, $super_call_param$3, $super_call_param$4);
        }
    };
    private final BoolValue retryValue = new BoolValue("Retry", false);
    private final LinkedBlockingQueue<String> sendQueue = new LinkedBlockingQueue();
    private final List<String> retryQueue = new ArrayList();
    private final MSTimer msTimer = new MSTimer();
    private long delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        Collection<String> collection = this.sendQueue;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (collection) {
            boolean bl3 = false;
            this.sendQueue.clear();
            Unit unit = Unit.INSTANCE;
        }
        collection = this.retryQueue;
        bl = false;
        boolean bl4 = false;
        synchronized (collection) {
            boolean bl5 = false;
            this.retryQueue.clear();
            Unit unit = Unit.INSTANCE;
        }
        super.onDisable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent event) {
        if (!this.msTimer.hasTimePassed(this.delay)) {
            return;
        }
        try {
            LinkedBlockingQueue<String> linkedBlockingQueue = this.sendQueue;
            boolean bl = false;
            boolean bl2 = false;
            synchronized (linkedBlockingQueue) {
                boolean bl3 = false;
                if (this.sendQueue.isEmpty()) {
                    if (!((Boolean)this.retryValue.get()).booleanValue() || this.retryQueue.isEmpty()) {
                        return;
                    }
                    this.sendQueue.addAll((Collection<String>)this.retryQueue);
                }
                IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                if (iEntityPlayerSP == null) {
                    Intrinsics.throwNpe();
                }
                String string = this.sendQueue.take();
                Intrinsics.checkExpressionValueIsNotNull(string, "sendQueue.take()");
                iEntityPlayerSP.sendChatMessage(string);
                this.msTimer.reset();
                this.delay = TimeUtils.randomDelay(((Number)this.minDelayValue.get()).intValue(), ((Number)this.maxDelayValue.get()).intValue());
                Unit unit = Unit.INSTANCE;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * WARNING - void declaration
     */
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        ICPacketChatMessage packetChatMessage;
        String message;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (MinecraftInstance.classProvider.isCPacketChatMessage(event.getPacket()) && StringsKt.contains$default((CharSequence)(message = (packetChatMessage = event.getPacket().asCPacketChatMessage()).getMessage()), "@a", false, 2, null)) {
            LinkedBlockingQueue<String> linkedBlockingQueue = this.sendQueue;
            boolean bl = false;
            boolean bl2 = false;
            synchronized (linkedBlockingQueue) {
                boolean bl3 = false;
                for (INetworkPlayerInfo playerInfo : MinecraftInstance.mc.getNetHandler().getPlayerInfoMap()) {
                    String playerName = playerInfo.getGameProfile().getName();
                    IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
                    if (iEntityPlayerSP == null) {
                        Intrinsics.throwNpe();
                    }
                    if (Intrinsics.areEqual(playerName, iEntityPlayerSP.getName())) continue;
                    String string = playerName;
                    Intrinsics.checkExpressionValueIsNotNull(string, "playerName");
                    this.sendQueue.add(StringsKt.replace$default(message, "@a", string, false, 4, null));
                }
                if (((Boolean)this.retryValue.get()).booleanValue()) {
                    List<String> list = this.retryQueue;
                    boolean bl4 = false;
                    boolean bl5 = false;
                    synchronized (list) {
                        void $this$toTypedArray$iv;
                        boolean bl6 = false;
                        this.retryQueue.clear();
                        Collection collection = this.sendQueue;
                        List<String> list2 = this.retryQueue;
                        boolean $i$f$toTypedArray = false;
                        void thisCollection$iv = $this$toTypedArray$iv;
                        String[] stringArray = thisCollection$iv.toArray(new String[0]);
                        if (stringArray == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<T>");
                        }
                        String[] stringArray2 = stringArray;
                        bl5 = list2.addAll((Collection<String>)CollectionsKt.listOf(Arrays.copyOf(stringArray2, stringArray2.length)));
                    }
                }
                Unit unit = Unit.INSTANCE;
            }
            event.cancelEvent();
        }
    }

    public static final IntegerValue access$getMaxDelayValue$p(AtAllProvider $this) {
        return $this.maxDelayValue;
    }

    public static final IntegerValue access$getMinDelayValue$p(AtAllProvider $this) {
        return $this.minDelayValue;
    }
}
