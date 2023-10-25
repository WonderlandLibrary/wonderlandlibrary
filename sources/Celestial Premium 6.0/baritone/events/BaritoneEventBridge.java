/*
 * Decompiled with CFR 0.150.
 */
package baritone.events;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.event.events.BlockInteractEvent;
import baritone.api.event.events.ChunkEvent;
import baritone.api.event.events.PacketEvent;
import baritone.api.event.events.PlayerUpdateEvent;
import baritone.api.event.events.RenderEvent;
import baritone.api.event.events.SprintStateEvent;
import baritone.api.event.events.TickEvent;
import baritone.api.event.events.WorldEvent;
import baritone.api.event.events.type.EventState;
import baritone.events.events.baritoneOnly.EventBarAChunkPost;
import baritone.events.events.baritoneOnly.EventBarAChunkPre;
import baritone.events.events.baritoneOnly.EventBarBlockBreak;
import baritone.events.events.baritoneOnly.EventBarBlockUse;
import baritone.events.events.baritoneOnly.EventBarChunkPost;
import baritone.events.events.baritoneOnly.EventBarChunkPre;
import baritone.events.events.baritoneOnly.EventBarDeath;
import baritone.events.events.baritoneOnly.EventBarPostLoadWorld;
import baritone.events.events.baritoneOnly.EventBarPreLoadWorld;
import baritone.events.events.baritoneOnly.EventBarSprintState;
import baritone.events.events.game.EventVolatileTick;
import baritone.events.events.network.EventPostReceivePacket;
import baritone.events.events.network.EventPostSendPacket;
import baritone.events.events.player.EventPreUpdate;
import baritone.events.events.render.EventPostRender3D;
import java.util.function.BiFunction;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.packet.EventSendPacket;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.impl.misc.Baritone;
import org.celestial.client.helpers.math.MathematicHelper;
import org.celestial.client.helpers.player.InventoryHelper;

public class BaritoneEventBridge {
    private Minecraft mc;

    public void init() {
        this.mc = Minecraft.getMinecraft();
        BaritoneAPI.getProvider().getPrimaryBaritone();
        EventManager.register(this);
    }

    @EventTarget
    public void onTick(EventVolatileTick myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        BiFunction<EventState, TickEvent.Type, TickEvent> tickProvider = TickEvent.createNextProvider();
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
                ibaritone.getPathingBehavior().forceCancel();
                ibaritone.getPathingBehavior().cancelEverything();
            }
            TickEvent.Type type = ibaritone.getPlayerContext().player() != null && ibaritone.getPlayerContext().world() != null ? TickEvent.Type.IN : TickEvent.Type.OUT;
            ibaritone.getGameEventHandler().onTick(tickProvider.apply(EventState.PRE, type));
        }
    }

    @EventTarget
    public void onPreUpdate(EventPreUpdate myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this.mc.player);
        if (baritone != null) {
            baritone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.PRE));
        }
    }

    @EventTarget
    public void onPostUpdate(EventUpdate myEvent) {
        IBaritone baritone;
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        if (BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() || BaritoneAPI.getProvider().getPrimaryBaritone().getBuilderProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getGetToBlockProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getGetToBlockProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getExploreProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getFollowProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getMineProcess().isActive() || BaritoneAPI.getProvider().getPrimaryBaritone().getFarmProcess().isActive()) {
            if (Baritone.autoDropTrash.getCurrentValue()) {
                for (int i = 0; i < 45; ++i) {
                    ItemStack stack = this.mc.player.openContainer.getSlot(i).getStack();
                    if (!stack.getItem().getUnlocalizedName().equals(Block.getBlockById(4).getUnlocalizedName()) && !stack.getItem().getUnlocalizedName().equals(Block.getBlockById(1).getUnlocalizedName()) || stack.getCount() <= 10) continue;
                    this.mc.playerController.windowClick(this.mc.player.inventoryContainer.windowId, i, 1, ClickType.THROW, this.mc.player);
                }
            }
            if (Baritone.inventoryFullLeave.getCurrentValue() && !InventoryHelper.inventoryHasAir()) {
                this.mc.world.sendFullInventoryLeavePacket();
            }
            if (Baritone.randomizeRotations.getCurrentValue()) {
                float randomValue = Baritone.randomizeValue.getCurrentValue();
                Minecraft.getMinecraft().player.rotationYaw -= MathematicHelper.randomizeFloat(-randomValue, randomValue);
                Minecraft.getMinecraft().player.rotationYaw += MathematicHelper.randomizeFloat(-randomValue, randomValue);
                Minecraft.getMinecraft().player.rotationPitch -= MathematicHelper.randomizeFloat(-randomValue, randomValue);
                Minecraft.getMinecraft().player.rotationPitch += MathematicHelper.randomizeFloat(-randomValue, randomValue);
            }
        }
        if ((baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this.mc.player)) != null) {
            baritone.getGameEventHandler().onPlayerUpdate(new PlayerUpdateEvent(EventState.POST));
        }
    }

    @EventTarget
    public void preChunkData(EventBarChunkPre myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player().connection != this.mc.player.connection) continue;
            ibaritone.getGameEventHandler().onChunkEvent(new ChunkEvent(EventState.PRE, myEvent.packet.doChunkLoad() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL, myEvent.packet.getChunkX(), myEvent.packet.getChunkZ()));
        }
    }

    @EventTarget
    public void postChunkData(EventBarChunkPost myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player().connection != this.mc.player.connection) continue;
            ibaritone.getGameEventHandler().onChunkEvent(new ChunkEvent(EventState.POST, myEvent.packet.doChunkLoad() ? ChunkEvent.Type.POPULATE_FULL : ChunkEvent.Type.POPULATE_PARTIAL, myEvent.packet.getChunkX(), myEvent.packet.getChunkZ()));
        }
    }

    @EventTarget
    public void preChunk(EventBarAChunkPre myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().world() != this.mc.world) continue;
            ibaritone.getGameEventHandler().onChunkEvent(new ChunkEvent(EventState.PRE, myEvent.loadChunk ? ChunkEvent.Type.LOAD : ChunkEvent.Type.UNLOAD, myEvent.chunkX, myEvent.chunkZ));
        }
    }

    @EventTarget
    public void postChunk(EventBarAChunkPost myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().world() != this.mc.world) continue;
            ibaritone.getGameEventHandler().onChunkEvent(new ChunkEvent(EventState.POST, myEvent.loadChunk ? ChunkEvent.Type.LOAD : ChunkEvent.Type.UNLOAD, myEvent.chunkX, myEvent.chunkZ));
        }
    }

    @EventTarget
    public void renderWorldPass(EventPostRender3D myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            ibaritone.getGameEventHandler().onRenderPass(new RenderEvent(this.mc.timer.renderPartialTicks));
        }
    }

    @EventTarget
    public void onPreLoadWorld(EventBarPreLoadWorld myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        if (this.mc.world == null && myEvent.world == null) {
            return;
        }
        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(new WorldEvent(myEvent.world, EventState.PRE));
    }

    @EventTarget
    public void onPostLoadWorld(EventBarPostLoadWorld myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onWorldEvent(new WorldEvent(myEvent.world, EventState.POST));
    }

    @EventTarget
    public void onPreSendPacket(EventSendPacket myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() == null) continue;
            ibaritone.getGameEventHandler().onSendPacket(new PacketEvent(this.mc.myNetworkManager, EventState.PRE, myEvent.getPacket()));
        }
    }

    @EventTarget
    public void onPostSendPacket(EventPostSendPacket myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() == null) continue;
            ibaritone.getGameEventHandler().onSendPacket(new PacketEvent(this.mc.myNetworkManager, EventState.POST, myEvent.getPacket()));
        }
    }

    @EventTarget
    public void onPreReceivePacket(EventReceivePacket myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() == null) continue;
            ibaritone.getGameEventHandler().onReceivePacket(new PacketEvent(this.mc.myNetworkManager, EventState.PRE, myEvent.getPacket()));
        }
    }

    @EventTarget
    public void onPostReceivePacket(EventPostReceivePacket myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player() == null) continue;
            ibaritone.getGameEventHandler().onReceivePacket(new PacketEvent(this.mc.myNetworkManager, EventState.POST, myEvent.getPacket()));
        }
    }

    @EventTarget
    public void onSprintState(EventBarSprintState myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        IBaritone baritone = BaritoneAPI.getProvider().getBaritoneForPlayer(this.mc.player);
        SprintStateEvent event = new SprintStateEvent();
        baritone.getGameEventHandler().onPlayerSprintState(event);
        if (event.getState() != null) {
            myEvent.state = event.getState();
        }
        if (baritone != BaritoneAPI.getProvider().getPrimaryBaritone()) {
            myEvent.state = false;
        }
    }

    @EventTarget
    public void onBlockBreak(EventBarBlockBreak myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(myEvent.position, BlockInteractEvent.Type.START_BREAK));
    }

    @EventTarget
    public void onBlockUse(EventBarBlockUse myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().onBlockInteract(new BlockInteractEvent(myEvent.position, BlockInteractEvent.Type.USE));
    }

    @EventTarget
    public void onDeath(EventBarDeath myEvent) {
        if (!Celestial.instance.featureManager.getFeatureByClass(Baritone.class).getState()) {
            return;
        }
        for (IBaritone ibaritone : BaritoneAPI.getProvider().getAllBaritones()) {
            if (ibaritone.getPlayerContext().player().connection != this.mc.player.connection) continue;
            ibaritone.getGameEventHandler().onPlayerDeath();
        }
    }
}

