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
import baritone.api.event.listener.IGameEventListener;

public interface AbstractGameEventListener
extends IGameEventListener {
    @Override
    default public void onTick(TickEvent event) {
    }

    @Override
    default public void onPlayerUpdate(PlayerUpdateEvent event) {
    }

    @Override
    default public void onSendChatMessage(ChatEvent event) {
    }

    @Override
    default public void onPreTabComplete(TabCompleteEvent event) {
    }

    @Override
    default public void onChunkEvent(ChunkEvent event) {
    }

    @Override
    default public void onRenderPass(RenderEvent event) {
    }

    @Override
    default public void onWorldEvent(WorldEvent event) {
    }

    @Override
    default public void onSendPacket(PacketEvent event) {
    }

    @Override
    default public void onReceivePacket(PacketEvent event) {
    }

    @Override
    default public void onPlayerRotationMove(RotationMoveEvent event) {
    }

    @Override
    default public void onPlayerSprintState(SprintStateEvent event) {
    }

    @Override
    default public void onBlockInteract(BlockInteractEvent event) {
    }

    @Override
    default public void onPlayerDeath() {
    }

    @Override
    default public void onPathEvent(PathEvent event) {
    }
}

