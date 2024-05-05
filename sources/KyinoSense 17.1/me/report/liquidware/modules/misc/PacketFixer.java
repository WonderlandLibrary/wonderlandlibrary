/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 */
package me.report.liquidware.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.PacketUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@ModuleInfo(name="PacketFixer", spacedName="Packet Fixer", description="Fix some weird packet issues.", category=ModuleCategory.FUN)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2={"Lme/report/liquidware/modules/misc/PacketFixer;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "fixBlinkAndFreecam", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "fixGround", "fixIdleFly", "fixInvalidPlace", "fixItemSwap", "fixPacketPlayer", "jam", "", "packetCount", "pitch", "", "prevSlot", "x", "", "y", "yaw", "z", "onEnable", "", "onPacket", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class PacketFixer
extends Module {
    private final BoolValue fixBlinkAndFreecam = new BoolValue("Blink-3Y", false);
    private final BoolValue fixPacketPlayer = new BoolValue("Timer-3A", false);
    private final BoolValue fixItemSwap = new BoolValue("Scaffold-14D", true);
    private final BoolValue fixInvalidPlace = new BoolValue("Scaffold-14E", true);
    private final BoolValue fixGround = new BoolValue("Fly-4I", true);
    private final BoolValue fixIdleFly = new BoolValue("Fly-4C", true);
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private int jam;
    private int packetCount;
    private int prevSlot = -1;

    @Override
    public void onEnable() {
        this.jam = 0;
        this.packetCount = 0;
        this.prevSlot = -1;
        if (PacketFixer.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        this.x = PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70165_t;
        this.y = PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70163_u;
        this.z = PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70161_v;
        this.yaw = PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70177_z;
        this.pitch = PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70125_A;
    }

    private final void onPacket(PacketEvent event) {
        int n;
        Packet<?> packet;
        block22: {
            block23: {
                if (PacketFixer.access$getMc$p$s1046033730().field_71439_g == null || PacketFixer.access$getMc$p$s1046033730().field_71441_e == null || event.isCancelled()) {
                    return;
                }
                packet = event.getPacket();
                if (((Boolean)this.fixGround.get()).booleanValue() && packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) && (PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70181_x == 0.0 || PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70122_E && PacketFixer.access$getMc$p$s1046033730().field_71439_g.field_70124_G) && !((C03PacketPlayer)packet).field_149474_g) {
                    ((C03PacketPlayer)packet).field_149474_g = true;
                }
                if (packet instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                    this.x = ((C03PacketPlayer.C04PacketPlayerPosition)packet).field_149479_a;
                    this.y = ((C03PacketPlayer.C04PacketPlayerPosition)packet).field_149477_b;
                    this.z = ((C03PacketPlayer.C04PacketPlayerPosition)packet).field_149478_c;
                    this.jam = 0;
                }
                if (packet instanceof C03PacketPlayer.C05PacketPlayerLook) {
                    this.yaw = ((C03PacketPlayer.C05PacketPlayerLook)packet).field_149476_e;
                    this.pitch = ((C03PacketPlayer.C05PacketPlayerLook)packet).field_149473_f;
                }
                if (packet instanceof C03PacketPlayer.C06PacketPlayerPosLook) {
                    this.x = ((C03PacketPlayer.C06PacketPlayerPosLook)packet).field_149479_a;
                    this.y = ((C03PacketPlayer.C06PacketPlayerPosLook)packet).field_149477_b;
                    this.z = ((C03PacketPlayer.C06PacketPlayerPosLook)packet).field_149478_c;
                    this.jam = 0;
                    this.yaw = ((C03PacketPlayer.C06PacketPlayerPosLook)packet).field_149476_e;
                    this.pitch = ((C03PacketPlayer.C06PacketPlayerPosLook)packet).field_149473_f;
                }
                if (((Boolean)this.fixPacketPlayer.get()).booleanValue() && packet instanceof C03PacketPlayer && !(packet instanceof C03PacketPlayer.C04PacketPlayerPosition) && !(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                    n = this.jam;
                    this.jam = n + 1;
                    if (this.jam > 20) {
                        this.jam = 0;
                        event.cancelEvent();
                        PacketUtils.sendPacketNoEvent((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(this.x, this.y, this.z, this.yaw, this.pitch, ((C03PacketPlayer)packet).field_149474_g));
                    }
                }
                if (!PacketFixer.access$getMc$p$s1046033730().func_71356_B() && ((Boolean)this.fixItemSwap.get()).booleanValue() && packet instanceof C09PacketHeldItemChange) {
                    if (((C09PacketHeldItemChange)packet).func_149614_c() == this.prevSlot) {
                        event.cancelEvent();
                    } else {
                        this.prevSlot = ((C09PacketHeldItemChange)packet).func_149614_c();
                    }
                }
                if (((Boolean)this.fixInvalidPlace.get()).booleanValue() && packet instanceof C08PacketPlayerBlockPlacement) {
                    RangesKt.coerceIn(((C08PacketPlayerBlockPlacement)packet).func_149573_h(), -1.0f, 1.0f);
                    RangesKt.coerceIn(((C08PacketPlayerBlockPlacement)packet).func_149569_i(), -1.0f, 1.0f);
                    RangesKt.coerceIn(((C08PacketPlayerBlockPlacement)packet).func_149575_j(), -1.0f, 1.0f);
                }
                if (!((Boolean)this.fixBlinkAndFreecam.get()).booleanValue()) break block22;
                Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(Blink.class);
                if (module == null) {
                    Intrinsics.throwNpe();
                }
                if (module.getState()) break block23;
                Module module2 = LiquidBounce.INSTANCE.getModuleManager().getModule(FreeCam.class);
                if (module2 == null) {
                    Intrinsics.throwNpe();
                }
                if (!module2.getState()) break block22;
            }
            if (packet instanceof C00PacketKeepAlive) {
                event.cancelEvent();
            }
        }
        if (((Boolean)this.fixIdleFly.get()).booleanValue() && packet instanceof C03PacketPlayer && !((C03PacketPlayer)packet).field_149474_g) {
            if (!(packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C05PacketPlayerLook || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                n = this.packetCount;
                this.packetCount = n + 1;
                if (this.packetCount >= 2) {
                    event.cancelEvent();
                }
            } else {
                this.packetCount = 0;
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

