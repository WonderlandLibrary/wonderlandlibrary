/*
 * Decompiled with CFR 0.150.
 */
package baritone.event;

import baritone.Baritone;
import baritone.api.event.events.BlockInteractEvent;
import baritone.api.event.events.ChatEvent;
import baritone.api.event.events.ChunkEvent;
import baritone.api.event.events.PacketEvent;
import baritone.api.event.events.PathEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.RenderEvent;
import baritone.api.event.events.RotationMoveEvent;
import baritone.api.event.events.SprintStateEvent;
import baritone.api.event.events.TabCompleteEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.event.events.WorldEvent;
import baritone.api.event.events.type.EventState;
import baritone.api.event.listener.IEventBus;
import baritone.api.event.listener.IGameEventListener;
import baritone.api.utils.Helper;
import baritone.cache.WorldProvider;
import baritone.utils.BlockStateInterface;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public final class GameEventHandler
implements IEventBus,
Helper {
    private final Baritone baritone;
    private final List<IGameEventListener> listeners = new CopyOnWriteArrayList<IGameEventListener>();

    public GameEventHandler(Baritone baritone) {
        this.baritone = baritone;
    }

    @Override
    public final void onTick(TickEvent event) {
        if (event.getType() == TickEvent.Type.IN) {
            try {
                this.baritone.bsi = new BlockStateInterface(this.baritone.getPlayerContext(), true);
            }
            catch (Exception ex) {
                this.baritone.bsi = null;
            }
        } else {
            this.baritone.bsi = null;
        }
        this.listeners.forEach(l -> l.onTick(event));
    }

    @Override
    public final void onPlayerUpdate(PlayerUpdateEvent event) {
        this.listeners.forEach(l -> l.onPlayerUpdate(event));
    }

    @Override
    public final void onSendChatMessage(ChatEvent event) {
        this.listeners.forEach(l -> l.onSendChatMessage(event));
    }

    @Override
    public void onPreTabComplete(TabCompleteEvent event) {
        this.listeners.forEach(l -> l.onPreTabComplete(event));
    }

    @Override
    public final void onChunkEvent(ChunkEvent event) {
        boolean isPreUnload;
        EventState state = event.getState();
        ChunkEvent.Type type = event.getType();
        boolean isPostPopulate = state == EventState.POST && (type == ChunkEvent.Type.POPULATE_FULL || type == ChunkEvent.Type.POPULATE_PARTIAL);
        World world = this.baritone.getPlayerContext().world();
        boolean bl = isPreUnload = state == EventState.PRE && type == ChunkEvent.Type.UNLOAD && world.getChunkProvider().isChunkGeneratedAt(event.getX(), event.getZ());
        if (isPostPopulate || isPreUnload) {
            this.baritone.getWorldProvider().ifWorldLoaded(worldData -> {
                Chunk chunk = world.getChunk(event.getX(), event.getZ());
                worldData.getCachedWorld().queueForPacking(chunk);
            });
        }
        this.listeners.forEach(l -> l.onChunkEvent(event));
    }

    @Override
    public final void onRenderPass(RenderEvent event) {
        this.listeners.forEach(l -> l.onRenderPass(event));
    }

    @Override
    public final void onWorldEvent(WorldEvent event) {
        WorldProvider cache = this.baritone.getWorldProvider();
        if (event.getState() == EventState.POST) {
            cache.closeWorld();
            if (event.getWorld() != null) {
                cache.initWorld(event.getWorld().provider.getDimensionType().getId());
            }
        }
        this.listeners.forEach(l -> l.onWorldEvent(event));
    }

    @Override
    public final void onSendPacket(PacketEvent event) {
        this.listeners.forEach(l -> l.onSendPacket(event));
    }

    @Override
    public final void onReceivePacket(PacketEvent event) {
        this.listeners.forEach(l -> l.onReceivePacket(event));
    }

    @Override
    public void onPlayerRotationMove(RotationMoveEvent event) {
        this.listeners.forEach(l -> l.onPlayerRotationMove(event));
    }

    @Override
    public void onPlayerSprintState(SprintStateEvent event) {
        this.listeners.forEach(l -> l.onPlayerSprintState(event));
    }

    @Override
    public void onBlockInteract(BlockInteractEvent event) {
        this.listeners.forEach(l -> l.onBlockInteract(event));
    }

    @Override
    public void onPlayerDeath() {
        this.listeners.forEach(IGameEventListener::onPlayerDeath);
    }

    @Override
    public void onPathEvent(PathEvent event) {
        this.listeners.forEach(l -> l.onPathEvent(event));
    }

    @Override
    public final void registerEventListener(IGameEventListener listener) {
        this.listeners.add(listener);
    }
}

