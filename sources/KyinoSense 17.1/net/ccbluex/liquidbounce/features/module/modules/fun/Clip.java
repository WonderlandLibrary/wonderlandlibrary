/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name="Clip", description="Allows you to clip through blocks.", category=ModuleCategory.FUN, canEnable=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/Clip;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "horizontalValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "verticalValue", "onEnable", "", "KyinoClient"})
public final class Clip
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"Teleport", "Flag"}, "Teleport");
    private final FloatValue horizontalValue = new FloatValue("Horizontal", 0.0f, -10.0f, 10.0f);
    private final FloatValue verticalValue = new FloatValue("Vertical", 5.0f, -10.0f, 10.0f);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onEnable() {
        if (Clip.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        double yaw = Math.toRadians(Clip.access$getMc$p$s1046033730().field_71439_g.field_70177_z);
        boolean bl = false;
        double x = -Math.sin(yaw) * ((Number)this.horizontalValue.get()).doubleValue();
        boolean bl2 = false;
        double z = Math.cos(yaw) * ((Number)this.horizontalValue.get()).doubleValue();
        String string = (String)this.modeValue.get();
        boolean bl3 = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 3145580: {
                if (!string.equals("flag")) return;
                break;
            }
            case -1360201941: {
                if (!string.equals("teleport")) return;
                Clip.access$getMc$p$s1046033730().field_71439_g.func_70107_b(Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x, Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u + ((Number)this.verticalValue.get()).doubleValue(), Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z);
                return;
            }
        }
        Minecraft minecraft = Clip.access$getMc$p$s1046033730();
        Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
        NetHandlerPlayClient netHandler = minecraft.func_147114_u();
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t, Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v, true));
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t + x, Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u + ((Number)this.verticalValue.get()).doubleValue(), Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v + z, true));
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(0.5, 0.0, 0.5, true));
        netHandler.func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t + 0.5, Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u, Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v + 0.5, true));
        double d = Clip.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
        EntityPlayerSP entityPlayerSP = Clip.access$getMc$p$s1046033730().field_71439_g;
        boolean bl4 = false;
        double d2 = Math.sin(yaw);
        double d3 = d + -d2 * 0.04;
        double d4 = Clip.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
        d2 = Clip.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
        d = d3;
        bl4 = false;
        double d5 = Math.cos(yaw);
        entityPlayerSP.func_70107_b(d, d2, d4 + d5 * 0.04);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

