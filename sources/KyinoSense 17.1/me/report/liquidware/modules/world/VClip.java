/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 */
package me.report.liquidware.modules.world;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

@ModuleInfo(name="VClip", description="Clip by Report.1337", category=ModuleCategory.WORLD)
public class VClip
extends Module {
    public final IntegerValue clips = new IntegerValue("Down-Value", 4, 1, 10);

    @EventTarget
    public void onUpdate(UpdateEvent e) {
        mc.func_147114_u().func_147297_a((Packet)new C03PacketPlayer.C04PacketPlayerPosition(VClip.mc.field_71439_g.field_70165_t, VClip.mc.field_71439_g.field_70163_u - (double)((Integer)this.clips.get()).intValue(), VClip.mc.field_71439_g.field_70161_v, true));
        VClip.mc.field_71439_g.func_70107_b(VClip.mc.field_71439_g.field_70165_t, VClip.mc.field_71439_g.field_70163_u - (double)((Integer)this.clips.get()).intValue(), VClip.mc.field_71439_g.field_70161_v);
    }
}

