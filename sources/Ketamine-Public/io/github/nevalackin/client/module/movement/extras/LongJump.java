package io.github.nevalackin.client.module.movement.extras;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.event.player.MoveEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.util.movement.JumpUtil;
import io.github.nevalackin.client.util.movement.MovementUtil;
import io.github.nevalackin.client.util.player.InventoryUtil;
import io.github.nevalackin.client.util.player.WindowClickRequest;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public final class LongJump extends Module {

    private boolean wasOnGround;
    private int ticksOnGround;

    private Vec3 velocity;
    private int serverSideHeldItem;
    private boolean isBowBoost;

    private boolean hasFiredArrow;
    private boolean isCharging;
    private int chargedTicks;

    public LongJump() {
        super("Long Jump", Category.MOVEMENT, Category.SubCategory.MOVEMENT_EXTRAS);
    }

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity velocityPacket = (S12PacketEntityVelocity) packet;

            if (velocityPacket.getEntityID() == this.mc.thePlayer.getEntityId()) {
                this.velocity = new Vec3(velocityPacket.getMotionX() / 8000.0,
                                         velocityPacket.getMotionY() / 8000.0,
                                         velocityPacket.getMotionZ() / 8000.0);
                this.mc.thePlayer.addChatMessage(new ChatComponentText("Received velo"));
            }
        } else if (packet instanceof S27PacketExplosion) {
            final S27PacketExplosion explosion = (S27PacketExplosion) packet;

            this.velocity = new Vec3(explosion.getMotionX(), explosion.getMotionY(), explosion.getMotionZ());

            this.mc.thePlayer.addChatMessage(new ChatComponentText("Received velo"));
        }
    };

    @EventLink
    private final Listener<MoveEvent> moveEventListener = event -> {
        if (this.velocity == null && this.isBowBoost) {
            event.setX(0);
            event.setZ(0);
        }
    };

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre()) {
            if (this.isBowBoost) {
                if (!this.hasFiredArrow) {
                    this.isCharging = true;
                    this.chargedTicks = 0;
                    // Start charging bow
                    this.mc.thePlayer.sendQueue.sendPacket(new C08PacketPlayerBlockPlacement(null));

                    if (this.isCharging) {
                        ++this.chargedTicks;

                        switch (this.chargedTicks) {
                            case 2:
                                // Rotate to hit optimal part of bb
                                event.setYaw(Math.round(this.mc.thePlayer.rotationYaw / 90.0F) * 90.0F + 45.0F);
                                event.setPitch(-86.0F);
                                break;
                            case 3:
                                // Release bow
                                this.mc.thePlayer.sendQueue.sendPacket(new C07PacketPlayerDigging(
                                    C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                this.isCharging = false;
                                this.hasFiredArrow = true;
                                break;
                        }

                        return;
                    }
                }

                if (this.velocity == null) return;
            }

            if (this.mc.thePlayer.onGround) {
                this.ticksOnGround++;
            } else {
                this.ticksOnGround = 0;
            }

            if (this.ticksOnGround > 1) {
                this.toggle();
                return;
            }

            if (this.mc.thePlayer.fallDistance < 1.0F) {
                if (this.mc.thePlayer.motionY < 0.0F) {
                    this.mc.thePlayer.motionY *= 0.875;
                }
                this.mc.thePlayer.motionY += 0.005;
            }

            if (MovementUtil.isMoving(this.mc.thePlayer)) {
                final double baseMoveSpeed = MovementUtil.getBaseMoveSpeed(this.mc.thePlayer);

                final double xDist = event.getLastTickPosX() - event.getPosX();
                final double zDist = event.getLastTickPosZ() - event.getPosZ();

                final double lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

                double moveSpeed;

                if (this.mc.thePlayer.onGround && !this.wasOnGround) {
                    if (this.isBowBoost) {
                        final double velocity = Math.sqrt(this.velocity.xCoord * this.velocity.xCoord + this.velocity.zCoord * this.velocity.zCoord);
                        moveSpeed = baseMoveSpeed + velocity;
                    } else {
                        moveSpeed = baseMoveSpeed * 2.149;
                    }

                    this.mc.thePlayer.motionY = JumpUtil.getJumpHeight(this.mc.thePlayer) + 0.005;

                    this.wasOnGround = true;
                } else if (this.wasOnGround) {
                    this.wasOnGround = false;

                    final double bunnySlope = 0.92 * (lastDist - baseMoveSpeed);
                    moveSpeed = lastDist - bunnySlope;
                } else {
                    moveSpeed = MovementUtil.getMotion(mc.thePlayer);
                }

                MovementUtil.setMotion(this.mc.thePlayer, targetStrafeInstance, moveSpeed);
            }
        }
    };

    @Override
    public void onEnable() {
        this.wasOnGround = false;
        this.isCharging = false;
        this.chargedTicks = 0;
        this.velocity = null;
        this.isBowBoost = false;
        this.hasFiredArrow = false;

        if (this.mc.thePlayer != null) {
            this.serverSideHeldItem = this.mc.thePlayer.inventory.currentItem;

            final int bowSlot = InventoryUtil.findSlotMatching(this.mc.thePlayer, itemStack -> itemStack != null && itemStack.getItem() instanceof ItemBow);

            if (bowSlot != -1) {
                final int arrowSlot = InventoryUtil.findSlotMatching(this.mc.thePlayer, itemStack -> itemStack != null &&
                    itemStack.getItem() == Items.arrow);

                if (arrowSlot != -1) {
                    if (bowSlot < InventoryUtil.ONLY_HOT_BAR_BEGIN) {
                        // Switch bow into hot bar
                        InventoryUtil.queueClickRequest(new WindowClickRequest() {
                            @Override
                            public void performRequest() {
                                InventoryUtil.windowClick(mc, bowSlot, 6, InventoryUtil.ClickType.SWAP_WITH_HOT_BAR_SLOT);
                            }
                        });
                        // Switch to bow hot bar slot
                    } else {
                        // Switch to bow slot
                    }
                    this.isBowBoost = true;
                }
            }
        }
    }

    @Override
    public void onDisable() {

    }
}
