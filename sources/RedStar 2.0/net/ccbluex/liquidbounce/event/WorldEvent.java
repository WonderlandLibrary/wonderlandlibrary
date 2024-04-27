package net.ccbluex.liquidbounce.event;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.Event;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\u000020B\b0¢R0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/event/WorldEvent;", "Lnet/ccbluex/liquidbounce/event/Event;", "worldClient", "Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "(Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;)V", "getWorldClient", "()Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IWorldClient;", "Pride"})
public final class WorldEvent
extends Event {
    @Nullable
    private final IWorldClient worldClient;

    @Nullable
    public final IWorldClient getWorldClient() {
        return this.worldClient;
    }

    public WorldEvent(@Nullable IWorldClient worldClient) {
        this.worldClient = worldClient;
    }
}
