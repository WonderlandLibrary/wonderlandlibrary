/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.init.Items
 *  net.minecraft.inventory.Slot
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.InventoryUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="KeepAlive", description="Tries to prevent you from dying.", category=ModuleCategory.PLAYER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/player/KeepAlive;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "modeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "getModeValue", "()Lnet/ccbluex/liquidbounce/value/ListValue;", "runOnce", "", "onMotion", "", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "KyinoClient"})
public final class KeepAlive
extends Module {
    @NotNull
    private final ListValue modeValue = new ListValue("Mode", new String[]{"/heal", "Soup"}, "/heal");
    private boolean runOnce;

    @NotNull
    public final ListValue getModeValue() {
        return this.modeValue;
    }

    /*
     * Enabled aggressive block sorting
     */
    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (!KeepAlive.access$getMc$p$s1046033730().field_71439_g.field_70128_L) {
            EntityPlayerSP entityPlayerSP = KeepAlive.access$getMc$p$s1046033730().field_71439_g;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
            if (!(entityPlayerSP.func_110143_aJ() <= 0.0f)) {
                this.runOnce = false;
                return;
            }
        }
        if (this.runOnce) {
            return;
        }
        String string = (String)this.modeValue.get();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        switch (string3) {
            case "/heal": {
                KeepAlive.access$getMc$p$s1046033730().field_71439_g.func_71165_d("/heal");
                break;
            }
            case "soup": {
                int soupInHotbar = InventoryUtils.findItem(36, 45, Items.field_151009_A);
                if (soupInHotbar == -1) break;
                Minecraft minecraft = KeepAlive.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
                minecraft.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(soupInHotbar - 36));
                Minecraft minecraft2 = KeepAlive.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
                NetHandlerPlayClient netHandlerPlayClient = minecraft2.func_147114_u();
                Slot slot = KeepAlive.access$getMc$p$s1046033730().field_71439_g.field_71069_bz.func_75139_a(soupInHotbar);
                Intrinsics.checkExpressionValueIsNotNull(slot, "mc.thePlayer.inventoryCo\u2026ner.getSlot(soupInHotbar)");
                netHandlerPlayClient.func_147297_a((Packet)new C08PacketPlayerBlockPlacement(slot.func_75211_c()));
                Minecraft minecraft3 = KeepAlive.access$getMc$p$s1046033730();
                Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
                minecraft3.func_147114_u().func_147297_a((Packet)new C09PacketHeldItemChange(KeepAlive.access$getMc$p$s1046033730().field_71439_g.field_71071_by.field_70461_c));
                break;
            }
        }
        this.runOnce = true;
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

