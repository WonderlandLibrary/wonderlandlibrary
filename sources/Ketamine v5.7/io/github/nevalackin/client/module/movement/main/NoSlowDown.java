package io.github.nevalackin.client.module.movement.main;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.event.player.UseSlowDownEvent;
import io.github.nevalackin.client.module.combat.rage.Aura;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public final class NoSlowDown extends Module {

    private static final C07PacketPlayerDigging UNBLOCK_PACKET = new C07PacketPlayerDigging(
        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);

    private static final C08PacketPlayerBlockPlacement BLOCK_PACKET = new C08PacketPlayerBlockPlacement(
        new BlockPos(-1, -1, -1), 255, null, 0.0f, 0.0f, 0.0f);

    private final BooleanProperty ncpProperty = new BooleanProperty("NCP", true);
    private final BooleanProperty interactProperty = new BooleanProperty("Interact", true, this.ncpProperty::getValue);

    private Aura aura;

    public NoSlowDown() {
        super("No Slow", Category.MOVEMENT, Category.SubCategory.MOVEMENT_MAIN);

        this.register(this.ncpProperty, this.interactProperty);
    }

    @EventLink
    private final Listener<UseSlowDownEvent> onUseSlowDown = event -> {
        event.setCancelled();
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (this.ncpProperty.getValue() &&
            this.mc.thePlayer.isBlocking() &&
            !this.aura.canAutoBlock() &&
            MovementUtil.isMoving(this.mc.thePlayer)) {

            if (event.isPre()) {
                this.mc.thePlayer.sendQueue.sendPacket(UNBLOCK_PACKET);
            } else {
                this.mc.thePlayer.sendQueue.sendPacket(BLOCK_PACKET);

                if (this.interactProperty.getValue() && this.mc.objectMouseOver != null &&
                    this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY &&
                    this.mc.objectMouseOver.entityHit != null) {

                    this.mc.playerController.interactWithEntitySendPacket(this.mc.thePlayer, this.mc.objectMouseOver.entityHit);
                }
            }
        }
    };

    @Override
    public void onEnable() {
        if (this.aura == null) {
            this.aura = KetamineClient.getInstance().getModuleManager().getModule(Aura.class);
        }
    }

    @Override
    public void onDisable() {

    }
}
