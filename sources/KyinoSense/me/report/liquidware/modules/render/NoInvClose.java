/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoInvClose", spacedName="NoInv Close", category=ModuleCategory.RENDER, array=false, description="Closes the GuiInventory.")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lme/report/liquidware/modules/render/NoInvClose;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "onPacket", "", "event", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class NoInvClose
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (NoInvClose.access$getMc$p$s1046033730().field_71441_e == null || NoInvClose.access$getMc$p$s1046033730().field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof S2EPacketCloseWindow && NoInvClose.access$getMc$p$s1046033730().field_71462_r instanceof GuiInventory) {
            event.cancelEvent();
        }
    }

    public NoInvClose() {
        this.setState(true);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

