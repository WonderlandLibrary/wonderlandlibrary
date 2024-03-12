// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.event;

import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.event.impl.render.CustomBlockRenderEvent;
import dev.tenacity.event.impl.render.RenderModelEvent;
import dev.tenacity.event.impl.render.RendererLivingEntityEvent;
import dev.tenacity.event.impl.render.RenderChestEvent;
import dev.tenacity.event.impl.render.Render3DEvent;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.PreRenderEvent;
import dev.tenacity.event.impl.render.NametagRenderEvent;
import dev.tenacity.event.impl.render.HurtCamEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import dev.tenacity.event.impl.player.StepConfirmEvent;
import dev.tenacity.event.impl.player.StrafeEvent;
import dev.tenacity.event.impl.player.SlowDownEvent;
import dev.tenacity.event.impl.player.SafeWalkEvent;
import dev.tenacity.event.impl.player.PushOutOfBlockEvent;
import dev.tenacity.event.impl.player.PlayerSendMessageEvent;
import dev.tenacity.event.impl.player.PlayerMoveUpdateEvent;
import dev.tenacity.event.impl.player.MoveEvent;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.event.impl.player.LivingDeathEvent;
import dev.tenacity.event.impl.player.KeepSprintEvent;
import dev.tenacity.event.impl.player.JumpFixEvent;
import dev.tenacity.event.impl.player.JumpEvent;
import dev.tenacity.event.impl.player.ClickEventRight;
import dev.tenacity.event.impl.player.ClickEvent;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.event.impl.player.BoundingBoxEvent;
import dev.tenacity.event.impl.player.BlockPlaceableEvent;
import dev.tenacity.event.impl.player.BlockEvent;
import dev.tenacity.event.impl.player.AttackEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.event.impl.game.TickEvent;
import dev.tenacity.event.impl.game.RenderTickEvent;
import dev.tenacity.event.impl.game.KeyPressEvent;
import dev.tenacity.event.impl.game.GameCloseEvent;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.Map;

public abstract class ListenerAdapter implements EventListener
{
    private final Map<Class<? extends Event>, Consumer<Event>> methods;
    private boolean registered;
    
    public ListenerAdapter() {
        this.methods = new HashMap<Class<? extends Event>, Consumer<Event>>();
        this.registered = false;
    }
    
    public void onGameCloseEvent(final GameCloseEvent event) {
    }
    
    public void onKeyPressEvent(final KeyPressEvent event) {
    }
    
    public void onRenderTickEvent(final RenderTickEvent event) {
    }
    
    public void onTickEvent(final TickEvent event) {
    }
    
    public void onWorldEvent(final WorldEvent event) {
    }
    
    public void onPacketReceiveEvent(final PacketReceiveEvent event) {
    }
    
    public void onPacketSendEvent(final PacketSendEvent event) {
    }
    
    public void onAttackEvent(final AttackEvent event) {
    }
    
    public void onBlockEvent(final BlockEvent event) {
    }
    
    public void onBlockPlaceable(final BlockPlaceableEvent event) {
    }
    
    public void onBoundingBoxEvent(final BoundingBoxEvent event) {
    }
    
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
    }
    
    public void onClickEvent(final ClickEvent event) {
    }
    
    public void onClickEventRight(final ClickEventRight event) {
    }
    
    public void onJumpEvent(final JumpEvent event) {
    }
    
    public void onJumpFixEvent(final JumpFixEvent event) {
    }
    
    public void onKeepSprintEvent(final KeepSprintEvent event) {
    }
    
    public void onLivingDeathEvent(final LivingDeathEvent event) {
    }
    
    public void onMotionEvent(final MotionEvent event) {
    }
    
    public void onMoveEvent(final MoveEvent event) {
    }
    
    public void onPlayerMoveUpdateEvent(final PlayerMoveUpdateEvent event) {
    }
    
    public void onPlayerSendMessageEvent(final PlayerSendMessageEvent event) {
    }
    
    public void onPushOutOfBlockEvent(final PushOutOfBlockEvent event) {
    }
    
    public void onSafeWalkEvent(final SafeWalkEvent event) {
    }
    
    public void onSlowDownEvent(final SlowDownEvent event) {
    }
    
    public void onStrafeEvent(final StrafeEvent event) {
    }
    
    public void onStepConfirmEvent(final StepConfirmEvent event) {
    }
    
    public void onUpdateEvent(final UpdateEvent event) {
    }
    
    public void onHurtCamEvent(final HurtCamEvent event) {
    }
    
    public void onNametagRenderEvent(final NametagRenderEvent event) {
    }
    
    public void onPreRenderEvent(final PreRenderEvent event) {
    }
    
    public void onRender2DEvent(final Render2DEvent event) {
    }
    
    public void onRender3DEvent(final Render3DEvent event) {
    }
    
    public void onRenderChestEvent(final RenderChestEvent event) {
    }
    
    public void onRendererLivingEntityEvent(final RendererLivingEntityEvent event) {
    }
    
    public void onRenderModelEvent(final RenderModelEvent event) {
    }
    
    public void onCustomBlockRender(final CustomBlockRenderEvent event) {
    }
    
    public void onShaderEvent(final ShaderEvent event) {
    }
    
    @Override
    public void onEvent(final Event event) {
        if (!this.registered) {
            this.start();
            this.registered = true;
        }
        this.methods.get(event.getClass()).accept(event);
    }
    
    private <T> void registerEvent(final Class<T> clazz, final Consumer<T> consumer) {
        this.methods.put((Class<? extends Event>)clazz, (Consumer<Event>)consumer);
    }
    
    private void start() {
        this.registerEvent(GameCloseEvent.class, this::onGameCloseEvent);
        this.registerEvent(KeyPressEvent.class, this::onKeyPressEvent);
        this.registerEvent(RenderTickEvent.class, this::onRenderTickEvent);
        this.registerEvent(TickEvent.class, this::onTickEvent);
        this.registerEvent((Class<WorldEvent>)WorldEvent.Load.class, this::onWorldEvent);
        this.registerEvent((Class<WorldEvent>)WorldEvent.Unload.class, this::onWorldEvent);
        this.registerEvent(WorldEvent.class, this::onWorldEvent);
        this.registerEvent(PacketReceiveEvent.class, this::onPacketReceiveEvent);
        this.registerEvent(PacketSendEvent.class, this::onPacketSendEvent);
        this.registerEvent(AttackEvent.class, this::onAttackEvent);
        this.registerEvent(BlockEvent.class, this::onBlockEvent);
        this.registerEvent(BlockPlaceableEvent.class, this::onBlockPlaceable);
        this.registerEvent(BoundingBoxEvent.class, this::onBoundingBoxEvent);
        this.registerEvent(ChatReceivedEvent.class, this::onChatReceivedEvent);
        this.registerEvent(ClickEvent.class, this::onClickEvent);
        this.registerEvent(ClickEventRight.class, this::onClickEventRight);
        this.registerEvent(JumpEvent.class, this::onJumpEvent);
        this.registerEvent(JumpFixEvent.class, this::onJumpFixEvent);
        this.registerEvent(KeepSprintEvent.class, this::onKeepSprintEvent);
        this.registerEvent(LivingDeathEvent.class, this::onLivingDeathEvent);
        this.registerEvent(MotionEvent.class, this::onMotionEvent);
        this.registerEvent(MoveEvent.class, this::onMoveEvent);
        this.registerEvent(PlayerMoveUpdateEvent.class, this::onPlayerMoveUpdateEvent);
        this.registerEvent(PlayerSendMessageEvent.class, this::onPlayerSendMessageEvent);
        this.registerEvent(PushOutOfBlockEvent.class, this::onPushOutOfBlockEvent);
        this.registerEvent(SafeWalkEvent.class, this::onSafeWalkEvent);
        this.registerEvent(SlowDownEvent.class, this::onSlowDownEvent);
        this.registerEvent(StrafeEvent.class, this::onStrafeEvent);
        this.registerEvent(StepConfirmEvent.class, this::onStepConfirmEvent);
        this.registerEvent(UpdateEvent.class, this::onUpdateEvent);
        this.registerEvent(HurtCamEvent.class, this::onHurtCamEvent);
        this.registerEvent(NametagRenderEvent.class, this::onNametagRenderEvent);
        this.registerEvent(PreRenderEvent.class, this::onPreRenderEvent);
        this.registerEvent(Render2DEvent.class, this::onRender2DEvent);
        this.registerEvent(Render3DEvent.class, this::onRender3DEvent);
        this.registerEvent(RenderChestEvent.class, this::onRenderChestEvent);
        this.registerEvent(RendererLivingEntityEvent.class, this::onRendererLivingEntityEvent);
        this.registerEvent(RenderModelEvent.class, this::onRenderModelEvent);
        this.registerEvent(CustomBlockRenderEvent.class, this::onCustomBlockRender);
        this.registerEvent(ShaderEvent.class, this::onShaderEvent);
    }
}
