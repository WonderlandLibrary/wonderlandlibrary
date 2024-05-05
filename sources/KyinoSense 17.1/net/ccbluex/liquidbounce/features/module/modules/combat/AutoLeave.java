/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="AutoLeave", description="Automatically makes you leave the server whenever your health is low.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/AutoLeave;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "healthValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onUpdate", "", "event", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class AutoLeave
extends Module {
    private final FloatValue healthValue = new FloatValue("Health", 8.0f, 0.0f, 20.0f);
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Quit", "InvalidPacket", "SelfHurt", "IllegalChat"}, "Quit");

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        EntityPlayerSP entityPlayerSP = AutoLeave.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        if (entityPlayerSP.func_110143_aJ() <= ((Number)this.healthValue.get()).floatValue() && !AutoLeave.access$getMc$p$s1046033730().field_71439_g.field_71075_bZ.field_75098_d) {
            Minecraft minecraft = AutoLeave.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            if (!minecraft.func_71387_A()) {
                String string = (String)this.modeValue.get();
                boolean bl = false;
                String string2 = string;
                if (string2 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                }
                String string3 = string2.toLowerCase();
                Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
                switch (string3) {
                    case "quit": {
                        AutoLeave.access$getMc$p$s1046033730().field_71441_e.func_72882_A();
                        break;
                    }
                    case "invalidpacket": {
                        Minecraft minecraft2 = AutoLeave.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                        minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(DoubleCompanionObject.INSTANCE.getNaN(), DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY(), DoubleCompanionObject.INSTANCE.getPOSITIVE_INFINITY(), !AutoLeave.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                        break;
                    }
                    case "selfhurt": {
                        Minecraft minecraft3 = AutoLeave.access$getMc$p$s1046033730();
                        Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                        minecraft3.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)AutoLeave.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                        break;
                    }
                    case "illegalchat": {
                        AutoLeave.access$getMc$p$s1046033730().field_71439_g.func_71165_d(String.valueOf(new Random().nextInt()) + "\u00a7\u00a7\u00a7" + new Random().nextInt());
                    }
                }
                this.setState(false);
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

