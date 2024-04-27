/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.server.S08PacketPlayerPosLook
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.module.modules.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.event.EventTarget;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.WorldEvent;
import net.dev.important.gui.client.hud.HUD;
import net.dev.important.gui.client.hud.element.elements.Notification;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.jetbrains.annotations.NotNull;

@Info(name="AutoDisable", spacedName="Auto Disable", description="Automatically disable modules for you on flag or world respawn.", category=Category.MISC, array=false, cnName="\u81ea\u52a8\u5173\u95ed\u529f\u80fd")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \f2\u00020\u0001:\u0002\f\rB\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\tH\u0007J\u0010\u0010\n\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\u000bH\u0007\u00a8\u0006\u000e"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoDisable;", "Lnet/dev/important/modules/module/Module;", "()V", "disableModules", "", "enumDisable", "Lnet/dev/important/modules/module/modules/misc/AutoDisable$DisableEvent;", "onPacket", "event", "Lnet/dev/important/event/PacketEvent;", "onWorld", "Lnet/dev/important/event/WorldEvent;", "Companion", "DisableEvent", "LiquidBounce"})
public final class AutoDisable
extends Module {
    @NotNull
    public static final Companion Companion = new Companion(null);

    @EventTarget
    public final void onPacket(@NotNull PacketEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            this.disableModules(DisableEvent.FLAG);
        }
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.disableModules(DisableEvent.WORLD_CHANGE);
    }

    /*
     * WARNING - void declaration
     */
    public final void disableModules(@NotNull DisableEvent enumDisable) {
        String string;
        void $this$filterTo$iv$iv;
        Intrinsics.checkNotNullParameter((Object)enumDisable, "enumDisable");
        int moduleNames = 0;
        Iterable $this$filter$iv = Client.INSTANCE.getModuleManager().getModules();
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Module it = (Module)element$iv$iv;
            boolean bl = false;
            if (!(it.getAutoDisables().contains((Object)enumDisable) && it.getState())) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        Iterable $this$forEach$iv = (List)destination$iv$iv;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            Module it = (Module)element$iv;
            boolean bl = false;
            it.toggle();
            int n = moduleNames;
            moduleNames = n + 1;
        }
        if (moduleNames <= 0) {
            return;
        }
        HUD hUD = Client.INSTANCE.getHud();
        StringBuilder stringBuilder = new StringBuilder().append("Disabled ").append(moduleNames).append(" modules due to ");
        switch (WhenMappings.$EnumSwitchMapping$0[enumDisable.ordinal()]) {
            case 1: {
                string = "unexpected teleport";
                break;
            }
            case 2: {
                string = "world change";
                break;
            }
            default: {
                string = "game ended";
            }
        }
        hUD.addNotification(new Notification(stringBuilder.append(string).append('.').toString(), Notification.Type.INFO, 1000L));
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoDisable$Companion;", "", "()V", "handleGameEnd", "", "LiquidBounce"})
    public static final class Companion {
        private Companion() {
        }

        public final void handleGameEnd() {
            Module module2 = Client.INSTANCE.getModuleManager().get(AutoDisable.class);
            Intrinsics.checkNotNull(module2);
            AutoDisable autoDisableModule = (AutoDisable)module2;
            autoDisableModule.disableModules(DisableEvent.GAME_END);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lnet/dev/important/modules/module/modules/misc/AutoDisable$DisableEvent;", "", "(Ljava/lang/String;I)V", "FLAG", "WORLD_CHANGE", "GAME_END", "LiquidBounce"})
    public static final class DisableEvent
    extends Enum<DisableEvent> {
        public static final /* enum */ DisableEvent FLAG = new DisableEvent();
        public static final /* enum */ DisableEvent WORLD_CHANGE = new DisableEvent();
        public static final /* enum */ DisableEvent GAME_END = new DisableEvent();
        private static final /* synthetic */ DisableEvent[] $VALUES;

        public static DisableEvent[] values() {
            return (DisableEvent[])$VALUES.clone();
        }

        public static DisableEvent valueOf(String value) {
            return Enum.valueOf(DisableEvent.class, value);
        }

        static {
            $VALUES = disableEventArray = new DisableEvent[]{DisableEvent.FLAG, DisableEvent.WORLD_CHANGE, DisableEvent.GAME_END};
        }
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[DisableEvent.values().length];
            nArray[DisableEvent.FLAG.ordinal()] = 1;
            nArray[DisableEvent.WORLD_CHANGE.ordinal()] = 2;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

