package dev.tenacity.event;

import dev.tenacity.event.impl.game.*;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.player.*;
import dev.tenacity.event.impl.render.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author cedo
 * @since 04/18/2022
 */
public abstract class ListenerAdapter implements EventListener {

    // Game
    public void onGameCloseEvent(GameCloseEvent event) {}
    public void onKeyPressEvent(KeyPressEvent event) {}
    public void onRenderTickEvent(RenderTickEvent event) {}
    public void onTickEvent(TickEvent event) {}
    public void onWorldEvent(WorldEvent event) {}

    // Network
    public void onPacketReceiveEvent(PacketReceiveEvent event) {}
    public void onPacketSendEvent(PacketSendEvent event) {}

    // Player
    public void onAttackEvent(AttackEvent event) {}
    public void onBlockEvent(BlockEvent event) {}
    public void onBlockPlaceable(BlockPlaceableEvent event) {}
    public void onBoundingBoxEvent(BoundingBoxEvent event) {}
    public void onChatReceivedEvent(ChatReceivedEvent event) {}
    public void onClickEvent(ClickEvent event) {}
    public void onClickEventRight(ClickEventRight event) {}
    public void onJumpEvent(JumpEvent event) {}
    public void onJumpFixEvent(JumpFixEvent event) {}
    public void onKeepSprintEvent(KeepSprintEvent event) {}
    public void onLivingDeathEvent(LivingDeathEvent event) {}
    public void onMotionEvent(MotionEvent event) {}
    public void onMoveEvent(MoveEvent event) {}
    public void onPlayerMoveUpdateEvent(PlayerMoveUpdateEvent event) {}
    public void onPlayerSendMessageEvent(PlayerSendMessageEvent event) {}
    public void onPushOutOfBlockEvent(PushOutOfBlockEvent event) {}
    public void onSafeWalkEvent(SafeWalkEvent event) {}
    public void onSlowDownEvent(SlowDownEvent event) {}
    public void onStepConfirmEvent(StepConfirmEvent event) {}
    public void onUpdateEvent(UpdateEvent event) {}

    // Render
    public void onHurtCamEvent(HurtCamEvent event) {}
    public void onNametagRenderEvent(NametagRenderEvent event) {}
    public void onPreRenderEvent(PreRenderEvent event) {}
    public void onRender2DEvent(Render2DEvent event) {}
    public void onRender3DEvent(Render3DEvent event) {}
    public void onRenderChestEvent(RenderChestEvent event) {}
    public void onRendererLivingEntityEvent(RendererLivingEntityEvent event) {}
    public void onRenderModelEvent(RenderModelEvent event) {}
    public void onCustomBlockRender(CustomBlockRenderEvent event) {}
    public void onShaderEvent(ShaderEvent event) {}

    private final Map<Class<? extends Event>, Consumer<Event>> methods = new HashMap<>();

    private boolean registered = false;

    @Override
    public void onEvent(Event event) {
        if (!registered) {
            start();
            registered = true;
        }
        methods.get(event.getClass()).accept(event);
    }

    private <T> void registerEvent(Class<T> clazz, Consumer<T> consumer) {
        methods.put((Class<? extends Event>) clazz, (Consumer<Event>) consumer);
    }

    private void start() {
        // Game
        registerEvent(GameCloseEvent.class, this::onGameCloseEvent);
        registerEvent(KeyPressEvent.class, this::onKeyPressEvent);
        registerEvent(RenderTickEvent.class, this::onRenderTickEvent);
        registerEvent(TickEvent.class, this::onTickEvent);

        registerEvent(WorldEvent.Load.class, this::onWorldEvent);
        registerEvent(WorldEvent.Unload.class, this::onWorldEvent);
        registerEvent(WorldEvent.class, this::onWorldEvent);

        registerEvent(PacketReceiveEvent.class, this::onPacketReceiveEvent);
        registerEvent(PacketSendEvent.class, this::onPacketSendEvent);

        // Player
        registerEvent(AttackEvent.class, this::onAttackEvent);
        registerEvent(BlockEvent.class, this::onBlockEvent);
        registerEvent(BlockPlaceableEvent.class, this::onBlockPlaceable);
        registerEvent(BoundingBoxEvent.class, this::onBoundingBoxEvent);
        registerEvent(ChatReceivedEvent.class, this::onChatReceivedEvent);
        registerEvent(ClickEvent.class, this::onClickEvent);
        registerEvent(ClickEventRight.class, this::onClickEventRight);
        registerEvent(JumpEvent.class, this::onJumpEvent);
        registerEvent(JumpFixEvent.class, this::onJumpFixEvent);
        registerEvent(KeepSprintEvent.class, this::onKeepSprintEvent);
        registerEvent(LivingDeathEvent.class, this::onLivingDeathEvent);
        registerEvent(MotionEvent.class, this::onMotionEvent);
        registerEvent(MoveEvent.class, this::onMoveEvent);
        registerEvent(PlayerMoveUpdateEvent.class, this::onPlayerMoveUpdateEvent);
        registerEvent(PlayerSendMessageEvent.class, this::onPlayerSendMessageEvent);
        registerEvent(PushOutOfBlockEvent.class, this::onPushOutOfBlockEvent);
        registerEvent(SafeWalkEvent.class, this::onSafeWalkEvent);
        registerEvent(SlowDownEvent.class, this::onSlowDownEvent);
        registerEvent(StepConfirmEvent.class, this::onStepConfirmEvent);
        registerEvent(UpdateEvent.class, this::onUpdateEvent);

        // Render
        registerEvent(HurtCamEvent.class, this::onHurtCamEvent);
        registerEvent(NametagRenderEvent.class, this::onNametagRenderEvent);
        registerEvent(PreRenderEvent.class, this::onPreRenderEvent);
        registerEvent(Render2DEvent.class, this::onRender2DEvent);
        registerEvent(Render3DEvent.class, this::onRender3DEvent);
        registerEvent(RenderChestEvent.class, this::onRenderChestEvent);
        registerEvent(RendererLivingEntityEvent.class, this::onRendererLivingEntityEvent);
        registerEvent(RenderModelEvent.class, this::onRenderModelEvent);
        registerEvent(CustomBlockRenderEvent.class, this::onCustomBlockRender);
        registerEvent(ShaderEvent.class, this::onShaderEvent);
    }

}
