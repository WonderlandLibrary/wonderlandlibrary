/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.MoveEvent;
import dev.rise.event.impl.other.StrafeEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.event.impl.packet.PacketSendEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.player.DamageUtil;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import dev.rise.util.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.ConcurrentLinkedQueue;


@ModuleInfo(name = "LongJump", description = "Does a long jump", category = Category.MOVEMENT)
public final class LongJump extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", this, "Redesky", "Redesky", "Redesky 2", "Cubecraft", "Hypixel", "NCP", "Minemenclub", "Minemenclub Safe", "Vulcan", "Taka");
    private final ModeSetting ncpMode = new ModeSetting("NCP Mode", this, "Normal", "Normal", "Damage");
    private final BooleanSetting smoothCamera = new BooleanSetting("Smooth Camera", this, false);
    private final BooleanSetting autoDisable = new BooleanSetting("Auto Disable", this, true);
    private final NumberSetting onGroundSpeed = new NumberSetting("On Ground Speed", this, 0.4, 0.1, 3, 0.1);
    private final NumberSetting offGroundSpeed = new NumberSetting("Off Ground Speed", this, 1.4, 0.1, 3, 0.1);
    private final NumberSetting timer = new NumberSetting("Timer", this, 1, 0.1, 2, 0.1);
    private final NumberSetting viewBobbing = new NumberSetting("View Bobbing", this, 0, 0, 0.1, 0.01);

    private boolean beingDmged, jumped, reset, glide, receivedDamage, boosted;
    private int offGroundTicks, stage;
    private int ticks, i;
    private float oPositionY;
    private double moveSpeed, lastDist;
    private final ConcurrentLinkedQueue<Packet<?>> packetList = new ConcurrentLinkedQueue<>();

    @Override
    public void onUpdateAlwaysInGui() {
        offGroundSpeed.hidden = onGroundSpeed.hidden = !(mode.is("NCP") && ncpMode.is("Normal"));

        timer.hidden = !(mode.is("NCP") || mode.is("Hypixel"));

        ncpMode.hidden = !mode.is("NCP");
    }

    @Override
    protected void onEnable() {
        offGroundTicks = 0;
        reset = glide = receivedDamage = boosted = false;
        ticks = stage = i = 0;

        switch (mode.getMode()) {
            case "Taka":
                mc.timer.timerSpeed = 0.2F;

                for (int i = 0; i < 8; i++) {
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42F, mc.thePlayer.posZ, false));
                    PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                }
                break;

            case "Vulcan":
                if (!mc.thePlayer.onGround) {
                    this.registerNotification("Cannot enable " + this.getModuleInfo().name() + " in air.");
                    this.toggleModule();
                }
                break;
        }

        oPositionY = (float) mc.thePlayer.posY;
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.speedInAir = 0.02f;
        mc.thePlayer.jumpMovementFactor = 0.02F;
        mc.timer.timerSpeed = 1;
        EntityPlayer.enableCameraYOffset = false;
        moveSpeed = lastDist = 0;
        beingDmged = false;
        jumped = false;

        switch (mode.getMode()) {
            case "NCP":
                if (ncpMode.is("Damage") && mc.thePlayer.inventory.currentItem != i)
                    PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                break;
        }

        if (!mc.isIntegratedServerRunning()) {
            packetList.forEach(PacketUtil::sendPacketWithoutEvent);
            packetList.clear();
        }

        MoveUtil.stop();
    }

    @Override
    public void onMove(final MoveEvent event) {
        switch (mode.getMode()) {
            case "NCP":
                if (ncpMode.is("Damage")) {
                    if (mc.thePlayer.hurtTime > 0)
                        jumped = true;

                    if (!jumped)
                        return;

                    if (MoveUtil.isMoving() && !mc.thePlayer.isCollidedHorizontally) {
                        mc.timer.timerSpeed = (float) timer.getValue();
                        if (mc.thePlayer.onGround) {
                            moveSpeed = Math.max(0.2873 * 2.15, moveSpeed);
                            reset = true;
                        } else if (reset) {
                            moveSpeed += 0.15 * (moveSpeed - 0.2873);
                            reset = false;
                        } else {
                            moveSpeed = lastDist - lastDist / 159.998;
                        }
                    } else {
                        mc.timer.timerSpeed = 1;
                        moveSpeed = 0;
                    }

                    MoveUtil.setMoveEventSpeed(event, Math.max(moveSpeed, 0.2873));

                    switch (stage) {
                        case 0:
                            if (mc.thePlayer.onGround) {
                                event.setY(mc.thePlayer.motionY = (0.424 - Math.random() / 500));
                                stage++;
                            }
                            break;

                        case 1:
                            event.setY(mc.thePlayer.motionY = 0.42F);
                            stage++;
                            break;

                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            stage++;
                            break;
                    }

                    if (glide) {
                        event.setY(mc.thePlayer.motionY += (0.045 - Math.random() / 500));
                        stage++;
                    }
                }
                break;

            case "Hypixel":
                switch (offGroundTicks) {
                    case 0:
                        moveSpeed = MoveUtil.getBaseMoveSpeed() * 1.7;
                        event.setY(mc.thePlayer.motionY = MoveUtil.getJumpMotion(0.424F));
                        break;

                    case 1:
                        mc.thePlayer.motionY += 0.02;
                        break;

                    case 2:
                    case 3:
                        moveSpeed = lastDist * 0.98;
                        break;

                    default:
                        moveSpeed = lastDist * (mc.thePlayer.fallDistance < 0.25 ? 0.95 : 0.91);

                        if (!(PlayerUtil.getBlockRelativeToPlayer(0, -0.1, 0) instanceof BlockAir))
                            moveSpeed = lastDist * 0.5;
                        break;
                }

                if (mc.thePlayer.fallDistance > 0) {
                    if (mc.thePlayer.fallDistance < 0.25)
                        mc.thePlayer.motionY = -0.0025;
                    else
                        mc.thePlayer.motionY += 0.02;
                }

                mc.timer.timerSpeed = (float) (timer.getValue() == 1 ? 1 : timer.getValue() - (Math.random() / 100));

                MoveUtil.setMoveEventSpeed(event, moveSpeed - (Math.random() / 500));
                break;
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> p = event.getPacket();
        if (mode.is("Vulcan") && (p instanceof S12PacketEntityVelocity || p instanceof S27PacketExplosion))
            event.setCancelled(true);
    }

    @Override
    public void onPacketSend(final PacketSendEvent event) {
        final Packet<?> p = event.getPacket();

        switch (mode.getMode()) {
            case "Vulcan":
                if (!receivedDamage && p instanceof C03PacketPlayer)
                    event.setCancelled(true);

                if (p instanceof C0FPacketConfirmTransaction || p instanceof C00PacketKeepAlive) {
                    packetList.add(p);
                    event.setCancelled(true);
                }
                break;
        }
    }

    @Override
    public void onStrafe(final StrafeEvent event) {
        switch (mode.getMode()) {
            case "Minemenclub":
            case "Minemenclub Safe":
                if (!jumped)
                    event.setCancelled(true);
                break;

            case "NCP":
                if (ncpMode.is("Damage") && !jumped)
                    event.setCancelled(true);
                break;

            case "Vulcan":
                if (!receivedDamage)
                    event.setCancelled(true);
                break;
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        //Utils
        ++ticks;

        if (mc.thePlayer.ticksExisted == 1) {
            toggleModule();
        }

        if (mc.thePlayer.onGround) {
            if (autoDisable.isEnabled() && (!(mode.is("Minemenclub") || mode.is("Minemenclub Safe")) ? offGroundTicks > 0 : offGroundTicks > 5)) {
                this.toggleModule();
                return;
            }

            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }

        final double x = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        final double z = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.hypot(x, z);

        EntityPlayer.enableCameraYOffset = false;

        if (smoothCamera.isEnabled()) {
            if (mc.thePlayer.posY > oPositionY) {
                EntityPlayer.enableCameraYOffset = true;
                EntityPlayer.cameraYPosition = oPositionY;
            }
        }

        mc.thePlayer.cameraYaw = (float) viewBobbing.getValue();

        switch (mode.getMode()) {
            case "Redesky":

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                    mc.thePlayer.motionZ *= 1.2;
                    mc.thePlayer.motionX *= 1.2;
                }

                if (offGroundTicks == 1) {
                    mc.thePlayer.motionZ *= 1.2;
                    mc.thePlayer.motionX *= 1.2;
                }

                break;

            case "Redesky 2":

                if (mc.thePlayer.isCollidedHorizontally) {
                    toggleModule();
                    return;
                }

                final Block block2 = mc.thePlayer.worldObj.getBlockState(new BlockPos(
                        MathHelper.floor_double(mc.thePlayer.posX),
                        MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minY - 0.5) - 1,
                        MathHelper.floor_double(mc.thePlayer.posZ))).getBlock();

                if (ticks > 10 && !(block2 instanceof BlockAir)) {
                    toggleModule();
                    return;
                }

                mc.timer.timerSpeed = (float) (0.5 + Math.random() / 10);

                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump();
                }

                if (!mc.thePlayer.onGround) {
                    mc.thePlayer.jumpMovementFactor = 0.18f;

                    mc.thePlayer.motionY += mc.thePlayer.motionY > 0.0 ? 0.055 : mc.thePlayer.motionY < 0.3 ? 0.01 : 0.03;
                }


                break;

            case "Minemenclub Safe":
                if (MoveUtil.isMoving() && (PlayerUtil.isOnServer("minemen") || PlayerUtil.isOnServer("mineman"))) {
                    if (mc.thePlayer.onGround) {
                        if (!beingDmged) {
                            event.setY(event.getY() - (2.5 - Math.random() / 100));
                            jumped = false;
                            moveSpeed = 0;
                        } else {
                            jumped = true;
                            moveSpeed = MoveUtil.getBaseMoveSpeed() + (0.32 - Math.random() / 100);
                            mc.thePlayer.jump();
                            mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.6 - Math.random() / 100));
                        }
                    } else
                        MoveUtil.strafe();
                } else
                    moveSpeed = 0;

                beingDmged = mc.thePlayer.onGround && mc.thePlayer.hurtTime > 0;

                MoveUtil.strafe(moveSpeed);
                break;

            case "Minemenclub":
                if (MoveUtil.isMoving() && (PlayerUtil.isOnServer("minemen") || PlayerUtil.isOnServer("mineman"))) {
                    if (mc.thePlayer.onGround) {
                        if (!beingDmged) {
                            event.setY(event.getY() - (2.5 - Math.random() / 100));
                            jumped = false;
                            moveSpeed = 0;
                        } else {
                            jumped = true;
                            moveSpeed = MoveUtil.getBaseMoveSpeed() + (1 - Math.random() / 100);
                            mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.6 - Math.random() / 100));
                        }
                    } else
                        MoveUtil.strafe();
                } else
                    moveSpeed = 0;

                beingDmged = mc.thePlayer.onGround && mc.thePlayer.hurtTime > 0;

                MoveUtil.strafe(moveSpeed);
                break;

            case "Cubecraft":
                if (MoveUtil.isMoving()) {
                    switch (offGroundTicks) {
                        case 0:
                            DamageUtil.damagePlayer(DamageUtil.DamageType.POSITION, 0.0465, false, true);

                            mc.thePlayer.jump();
                            mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.6 + (Math.random() / 100)));
                            MoveUtil.strafe(0.4 - (Math.random() / 500));
                            break;

                        case 1:
                            MoveUtil.strafe(0.6 - (Math.random() / 500));
                            break;
                    }

                    if (mc.thePlayer.fallDistance > 0 && mc.thePlayer.fallDistance < 3)
                        mc.thePlayer.motionY += 0.0425 + (Math.random() / 500);
                } else
                    MoveUtil.stop();
                MoveUtil.strafe();
                break;

            case "NCP":
                switch (ncpMode.getMode()) {
                    case "Normal": {
                        if (MoveUtil.isMoving()) {
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.jump();
                                mc.thePlayer.motionY = MoveUtil.getJumpMotion((float) (0.424 - Math.random() / 500));
                                MoveUtil.strafe(onGroundSpeed.getValue());
                            }

                            if (offGroundTicks == 1)
                                MoveUtil.strafe(offGroundSpeed.getValue());

                            if (mc.thePlayer.fallDistance > 0 && mc.thePlayer.fallDistance < 3)
                                mc.thePlayer.motionY += 0.03 + Math.random() / 500;

                            mc.timer.timerSpeed = (float) timer.getValue();
                        } else {
                            MoveUtil.stop();
                            mc.timer.timerSpeed = 1;
                        }
                        MoveUtil.strafe();
                        break;
                    }

                    case "Damage": {
                        ItemStack itemStack;

                        if (!jumped)
                            event.setPitch(-RandomUtils.nextFloat(89, 90));

                        int sexySlot = -1;
                        int shitSlot = -1;

                        if (!beingDmged) {
                            for (i = 0; i < 9; ++i) {
                                itemStack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();

                                if (itemStack != null) {
                                    final Item item = itemStack.getItem();

                                    if (itemStack.getItem() instanceof ItemBow) {
                                        for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                                            final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

                                            if (stack != null) {
                                                if (stack.getItem().getUnlocalizedName().contains("arrow")) {
                                                    sexySlot = this.i;
                                                }
                                            }
                                        }
                                    }

                                    if (item instanceof ItemFishingRod || item instanceof ItemSnowball || item instanceof ItemEgg) {
                                        shitSlot = i;
                                    }
                                }
                            }

                            if (sexySlot != -1) {
                                itemStack = mc.thePlayer.inventoryContainer.getSlot(sexySlot + 36).getStack();

                                if (mc.thePlayer.inventory.currentItem != sexySlot && ticks == 1) {
                                    PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(sexySlot));
                                }

                                if (ticks == 5 + randomInt(0, 1)) {
                                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));
                                }

                                ticks++;

                                if (ticks == 11 + randomInt(1, 2)) {
                                    PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                                    beingDmged = true;
                                }
                            } else if (shitSlot != -1) {
                                itemStack = mc.thePlayer.inventoryContainer.getSlot(shitSlot + 36).getStack();

                                if (mc.thePlayer.inventory.currentItem != shitSlot)
                                    PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(shitSlot));

                                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));

                                beingDmged = true;
                            }
                        }
                        break;
                    }
                }
                break;

            case "Vulcan": {
                final double[] jumpValues = new double[]{0.42F, 0.33319999363422365, 0.24813599859094576, 0.16477328182606651, 0.08307781780646721, 0.0030162615090425808};

                if (mc.thePlayer.onGround && ticks == 5 && !beingDmged) {
                    for (int i = 0; i < 6; i++) {
                        double position = mc.thePlayer.posY;
                        for (final double value : jumpValues) {
                            position += value;

                            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, position, mc.thePlayer.posZ, false));
                        }
                    }
                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer(true));
                    event.setGround(true);

                    beingDmged = true;
                }

                if (mc.thePlayer.hurtTime > 0)
                    receivedDamage = true;

                if (receivedDamage) {
                    mc.timer.timerSpeed = 1;

                    if (!boosted) {
                        float motion = 0.6F;

                        if (PlayerUtil.getBlockRelativeToPlayer(0, -0.5, 0).getUnlocalizedName().contains("bed"))
                            motion = 1.5F;

                        mc.thePlayer.motionY = motion - (Math.random() / 100);
                        MoveUtil.strafe(9.5 - (Math.random() / 500));
                        boosted = true;
                    } else if (offGroundTicks == 1)
                        MoveUtil.strafe(0.5 - (Math.random() / 500));

                    if (mc.thePlayer.fallDistance > 0)
                        mc.thePlayer.motionY += 0.02 - (Math.random() / 10000);
                }
                break;
            }

            case "Taka": {
                if (mc.thePlayer.hurtTime == 9) {
                    ticks = 0;

                    mc.timer.timerSpeed = 1.0F;

                    MoveUtil.strafe(9.85F);
                    mc.thePlayer.motionY = 0.42F;
                }

                if (ticks < 13) {
                    MoveUtil.strafe(9.85F);
                }

                if (ticks == 13) {
                    MoveUtil.strafe(0.25F);
                }

                break;
            }
        }

    }
}