package net.minecraft.network.play.server;

import net.minecraft.network.play.server.S42PacketCombatEvent;

/*
 * Exception performing whole class analysis ignored.
 */
static class S42PacketCombatEvent.1 {
    static final /* synthetic */ int[] $SwitchMap$net$minecraft$network$play$server$S42PacketCombatEvent$Event;

    static {
        $SwitchMap$net$minecraft$network$play$server$S42PacketCombatEvent$Event = new int[S42PacketCombatEvent.Event.values().length];
        try {
            S42PacketCombatEvent.1.$SwitchMap$net$minecraft$network$play$server$S42PacketCombatEvent$Event[S42PacketCombatEvent.Event.END_COMBAT.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        try {
            S42PacketCombatEvent.1.$SwitchMap$net$minecraft$network$play$server$S42PacketCombatEvent$Event[S42PacketCombatEvent.Event.ENTITY_DIED.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
    }
}
