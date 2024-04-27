/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoRotateSet", description="Prevents the server from rotating your head.", category=ModuleCategory.MISC)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/misc/NoRotateSet;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "confirmValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "illegalRotationValue", "noZeroValue", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "LiKingSense"})
public final class NoRotateSet
extends Module {
    public final BoolValue confirmValue = new BoolValue("Confirm", true);
    public final BoolValue illegalRotationValue = new BoolValue("ConfirmIllegalRotation", false);
    public final BoolValue noZeroValue = new BoolValue("NoZero", false);

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP thePlayer = iEntityPlayerSP;
        if (MinecraftInstance.classProvider.isSPacketPlayerPosLook(event.getPacket())) {
            ISPacketPosLook packet = event.getPacket().asSPacketPosLook();
            if (((Boolean)this.noZeroValue.get()).booleanValue() && packet.getYaw() == 0.0f && packet.getPitch() == 0.0f) {
                return;
            }
            if ((((Boolean)this.illegalRotationValue.get()).booleanValue() || packet.getPitch() <= (float)90 && packet.getPitch() >= (float)-90 && RotationUtils.serverRotation != null && packet.getYaw() != RotationUtils.serverRotation.getYaw() && packet.getPitch() != RotationUtils.serverRotation.getPitch()) && ((Boolean)this.confirmValue.get()).booleanValue()) {
                MinecraftInstance.mc.getNetHandler().addToSendQueue(MinecraftInstance.classProvider.createCPacketPlayerLook(packet.getYaw(), packet.getPitch(), thePlayer.getOnGround()));
            }
            packet.setYaw(thePlayer.getRotationYaw());
            packet.setPitch(thePlayer.getRotationPitch());
        }
    }
}

