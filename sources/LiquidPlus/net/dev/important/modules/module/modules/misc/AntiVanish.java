/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S14PacketEntity
 *  net.minecraft.network.play.server.S1DPacketEntityEffect
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.value.IntegerValue;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Info(name="AntiVanish", spacedName="Anti Vanish", description="Anti player vanish", category=Category.MISC, cnName="\u53cd\u73a9\u5bb6\u9690\u85cf")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\b\u0010\u000b\u001a\u00020\bH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2={"Lnet/dev/important/modules/module/modules/misc/AntiVanish;", "Lnet/dev/important/modules/module/Module;", "()V", "lastNotify", "", "notifyLast", "Lnet/dev/important/value/IntegerValue;", "onPacket", "", "event", "Lnet/dev/important/event/PacketEvent;", "vanish", "LiquidBounce"})
public final class AntiVanish
extends Module {
    private long lastNotify = -1L;
    @NotNull
    private final IntegerValue notifyLast = new IntegerValue("Notification-Seconds", 2, 1, 30);

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (MinecraftInstance.mc.field_71441_e == null || MinecraftInstance.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof S1DPacketEntityEffect) {
            if (MinecraftInstance.mc.field_71441_e.func_73045_a(((S1DPacketEntityEffect)event.getPacket()).func_149426_d()) == null) {
                this.vanish();
            }
        } else if (event.getPacket() instanceof S14PacketEntity && ((S14PacketEntity)event.getPacket()).func_149065_a((World)MinecraftInstance.mc.field_71441_e) == null) {
            this.vanish();
        }
    }

    private final void vanish() {
        if (System.currentTimeMillis() - this.lastNotify > 5000L) {
            Client.INSTANCE.getHud().addNotification(new Notification("Found a vanished entity!", Notification.Type.WARNING, (long)((Number)this.notifyLast.get()).intValue() * 1000L));
        }
        this.lastNotify = System.currentTimeMillis();
    }
}

