/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.event.listener;

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

public interface IGameEventListener {
    public void onTick(TickEvent var1);

    public void onPlayerUpdate(PlayerUpdateEvent var1);

    public void onSendChatMessage(ChatEvent var1);

    public void onPreTabComplete(TabCompleteEvent var1);

    public void onChunkEvent(ChunkEvent var1);

    public void onRenderPass(RenderEvent var1);

    public void onWorldEvent(WorldEvent var1);

    public void onSendPacket(PacketEvent var1);

    public void onReceivePacket(PacketEvent var1);

    public void onPlayerRotationMove(RotationMoveEvent var1);

    public void onPlayerSprintState(SprintStateEvent var1);

    public void onBlockInteract(BlockInteractEvent var1);

    public void onPlayerDeath();

    public void onPathEvent(PathEvent var1);
}

