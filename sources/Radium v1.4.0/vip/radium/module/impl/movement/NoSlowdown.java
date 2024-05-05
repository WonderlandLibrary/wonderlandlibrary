package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import vip.radium.event.CancellableEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.event.impl.player.UseItemEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.module.impl.combat.KillAura;
import vip.radium.property.Property;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "No Slowdown", category = ModuleCategory.MOVEMENT)
public final class NoSlowdown extends Module {

    private static final C07PacketPlayerDigging PLAYER_DIGGING = new C07PacketPlayerDigging(
            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
    private static final C08PacketPlayerBlockPlacement BLOCK_PLACEMENT = new C08PacketPlayerBlockPlacement(
            new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);

    private final Property<Boolean> ncpProperty = new Property<>("NCP", true);

    public static boolean isNoSlowdownEnabled() {
        return ModuleManager.getInstance(NoSlowdown.class).isEnabled();
    }

    @EventLink
    public final Listener<UseItemEvent> onUseItemEvent = CancellableEvent::setCancelled;

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = e -> {
        if (ncpProperty.getValue() && MovementUtils.isMoving() &&
                !KillAura.isAutoBlocking() && Wrapper.getPlayer().isBlocking()) {
            if (e.isPre()) {
                Wrapper.sendPacketDirect(PLAYER_DIGGING);
            } else {
                Wrapper.sendPacketDirect(BLOCK_PLACEMENT);
            }
        }
    };
}
