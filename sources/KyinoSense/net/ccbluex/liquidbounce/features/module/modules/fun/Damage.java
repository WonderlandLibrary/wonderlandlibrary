/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
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
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name="Damage", description="Deals damage to yourself.", category=ModuleCategory.FUN, canEnable=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/fun/Damage;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "damageValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onEnable", "", "KyinoClient"})
public final class Damage
extends Module {
    private final ListValue modeValue = new ListValue("Mode", new String[]{"NCP", "AAC"}, "NCP");
    private final IntegerValue damageValue = new IntegerValue("Damage", 1, 1, 20);

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onEnable() {
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        string = string3;
        switch (string.hashCode()) {
            case 96323: {
                if (!string.equals("aac")) return;
                break;
            }
            case 108891: {
                if (!string.equals("ncp")) return;
                double x = Damage.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
                double y = Damage.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
                double z = Damage.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
                int n = 65 * ((Number)this.damageValue.get()).intValue();
                boolean bl2 = false;
                int n2 = 0;
                n2 = 0;
                int n3 = n;
                while (n2 < n3) {
                    int it = n2++;
                    boolean bl3 = false;
                    Minecraft minecraft = Damage.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                    minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.049, z, false));
                    Minecraft minecraft2 = Damage.access$getMc$p$s1046033730();
                    Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                    minecraft2.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                }
                Minecraft minecraft = Damage.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));
                return;
            }
        }
        Damage.access$getMc$p$s1046033730().field_71439_g.field_70181_x = (double)5 * (double)((Number)this.damageValue.get()).intValue();
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

