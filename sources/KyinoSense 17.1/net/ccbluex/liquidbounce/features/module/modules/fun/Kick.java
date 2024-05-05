/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import java.util.Random;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name="Kick", description="Allows you to kick yourself from a server.", category=ModuleCategory.FUN, canEnable=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/Kick;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onEnable", "", "KyinoClient"})
public final class Kick
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Quit", "InvalidPacket", "SelfHurt", "IllegalChat", "PacketSpam"}, "Quit");

    @Override
    public void onEnable() {
        Minecraft minecraft = Kick.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        if (minecraft.func_71387_A()) {
            ClientUtils.displayChatMessage("\u00a7c\u00a7lError: \u00a7aYou can't enable \u00a7c\u00a7l'Kick' \u00a7ain SinglePlayer.");
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
        switch (string3) {
            case "quit": {
                Kick.access$getMc$p$s1046033730().field_71441_e.func_72882_A();
                break;
            }
            case "invalidpacket": {
                Minecraft minecraft2 = Kick.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(DoubleCompanionObject.INSTANCE.getNaN(), DoubleCompanionObject.INSTANCE.getNEGATIVE_INFINITY(), DoubleCompanionObject.INSTANCE.getPOSITIVE_INFINITY(), !Kick.access$getMc$p$s1046033730().field_71439_g.field_70122_E));
                break;
            }
            case "selfhurt": {
                Minecraft minecraft3 = Kick.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C02PacketUseEntity((Entity)Kick.access$getMc$p$s1046033730().field_71439_g, C02PacketUseEntity.Action.ATTACK));
                break;
            }
            case "illegalchat": {
                Kick.access$getMc$p$s1046033730().field_71439_g.func_71165_d(String.valueOf(new Random().nextInt()) + "\u00a7\u00a7\u00a7" + new Random().nextInt());
                break;
            }
            case "packetspam": {
                n = 9999;
                boolean bl = false;
                int n2 = 0;
                n2 = 0;
                int n3 = n;
                while (n2 < n3) {
                    int it = n2++;
                    boolean bl2 = false;
                    Minecraft minecraft4 = Kick.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft4, "mc");
                    minecraft4.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition((double)it, (double)it, (double)it, new Random().nextBoolean()));
                }
                break;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

