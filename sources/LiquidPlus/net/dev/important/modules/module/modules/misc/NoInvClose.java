/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.network.play.server.S2EPacketCloseWindow
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import org.jetbrains.annotations.NotNull;

@Info(name="NoInvClose", spacedName="No Inv Close", description="Stops server from closing your Inventory.", category=Category.MISC, cnName="\u4fdd\u6301\u6e38\u620f\u754c\u9762")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2={"Lnet/dev/important/modules/module/modules/misc/NoInvClose;", "Lnet/dev/important/modules/module/Module;", "()V", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "LiquidBounce"})
public final class NoInvClose
extends Module {
    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71441_e == null || MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof S2EPacketCloseWindow && MinecraftInstance.mc.field_71462_r instanceof GuiInventory) {
            event.cancelEvent();
        }
    }
}

