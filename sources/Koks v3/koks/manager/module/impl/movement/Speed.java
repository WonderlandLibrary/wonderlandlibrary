package koks.manager.module.impl.movement;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.manager.event.Event;
import koks.manager.event.impl.EventMotion;
import koks.manager.event.impl.EventMoveWithHeading;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.gen.structure.StructureVillagePieces;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:17
 */

@ModuleInfo(name = "Speed", description = "In germany we call it rasant", category = Module.Category.MOVEMENT)
public class Speed extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave", "Hypixel", "MCCentral", "Mineplex", "Mineplex FAST", "Mineplex Ground", "AAC4", "AAC3.3.12", "NCPBhop", "NCPGround", "Tired", "Legit"}, "Intave", this);

    public float mineplexMotion, mineplexSpeed;

    public int curSlot;

    public int aacSpeed;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventMoveWithHeading) {
            switch (mode.getCurrentMode()) {
                case "Hypixel":
                    //STANDART 0.91F
                    ((EventMoveWithHeading) event).setF4(0.932f);
                    break;
            }
        }

        if (event instanceof EventMotion) {
            if(((EventMotion) event).getType().equals(EventMotion.Type.POST)) {
                if (getPlayer().onGround && isMoving()) {
                    getPlayer().jump();
                    getPlayer().motionY = 0.41;
                    float f = getPlayer().rotationYaw * 0.017453292F;
                    getPlayer().motionX -= (double) (MathHelper.sin(f) * 0.065F);
                    getPlayer().motionZ += (double) (MathHelper.cos(f) * 0.065F);
                } else {
                    getPlayer().motionY -= 0.004f;
                }
            }
        }

        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Legit":
                    if (getPlayer().onGround && isMoving())
                        getPlayer().jump();
                    getPlayer().setSprinting(true);
                    break;
                case "NCPBhop":
                    getPlayer().setSprinting(true);
                    movementUtil.setSpeed(0.26, true);
                    if (movementUtil.isMoving()) {
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                        } else {
                        }
                    }
                    break;
                case "NCPGround":
                    if (getPlayer().onGround) {
                        getPlayer().motionY = randomUtil.getRandomFloat(0.01F, 0.02F);
                        getPlayer().addExhaustion(0.8F);
                    } else {
                        float dir = movementUtil.getDirection(getPlayer().rotationYaw) * 0.017453292F;
                        float speed = randomUtil.getRandomFloat(0.05F, 0.057F);
                        getPlayer().motionX -= (double) (MathHelper.sin(dir) * speed);
                        getPlayer().motionZ += (double) (MathHelper.cos(dir) * speed);
                    }
                    break;
                case "AAC4":
                    if (getPlayer().onGround) {
                        aacSpeed++;
                        getPlayer().jump();
                        if (aacSpeed <= 3)
                            getTimer().timerSpeed = 25;
                        else
                            getTimer().timerSpeed = 0.1F;
                    } else {
                        getTimer().timerSpeed = 1.0F;
                    }

                    if (aacSpeed >= 4)
                        aacSpeed = 0;
                    break;
                case "Intave":
                    getPlayer().setSprinting(true);
                    getPlayer().addExhaustion(0.8F);
                    if (getPlayer().onGround && isMoving()) {
                        getPlayer().jump();
                    } else {
                        if (getPlayer().fallDistance >= 0.7)
                            getPlayer().motionY -= 0.01955;
                    }
                    break;
                case "Tired":
                    getGameSettings().keyBindSprint.pressed = false;
                    if (getPlayer().onGround && isMoving()) {
                        getPlayer().jump();
                        getPlayer().setSprinting(false);
                        movementUtil.setSpeed(0.06, true);
                        getTimer().timerSpeed = 5F;
                    } else {
                        getPlayer().setSprinting(true);
                        getPlayer().speedInAir = 0.23F;
                    }
                    if (getPlayer().motionY >= 0.3) {
                        getPlayer().motionY = 0;
                    }
                    break;
                case "AAC3.3.12":
                    if (isMoving()) {
                        if (getPlayer().onGround) {
                            getPlayer().jump();
                        } else {
                            getPlayer().motionY -= 0.022;
                            getPlayer().jumpMovementFactor = 0.032F;
                        }
                    }
                    break;
                case "Mineplex FAST":
                    if (!mc.thePlayer.isInWeb) {

                        if (getPlayer().isCollidedHorizontally) {
                            setMotion(0);
                            mineplexMotion = 0.02F;
                        }

                        if (isMoving()) {
                            if (getPlayer().onGround) {
                                getPlayer().motionX = getPlayer().motionZ = 0;
                                mineplexMotion += 0.25F;
                                getPlayer().jump();
                            } else {

                                /*if(getPlayer().fallDistance >= 0.41 && getPlayer().fallDistance <= 0.43) {
                                    getPlayer().motionY += 0.42F;
                                }*/
                                mineplexMotion -= mineplexMotion / 64;
                                movementUtil.setSpeed(mineplexMotion, false);
                            }
                        } else {
                            mineplexMotion = 0.02F;
                        }
                    }
                    break;
                case "Mineplex Ground":
                    if (inventoryUtil.hasAir()) {
                        if (getPlayer().onGround && isMoving()) {
                            int slot = inventoryUtil.getAir();
                            ItemStack stack = getPlayer().inventory.getStackInSlot(slot);
                            BlockPos blockPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
                            Vec3 vec = new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            sendPacket(new C09PacketHeldItemChange(slot));
                            getPlayerController().onPlayerRightClick(mc.thePlayer, mc.theWorld, null, blockPos, EnumFacing.UP, new Vec3(vec.xCoord * 0.4F, vec.yCoord * 0.4F, vec.zCoord * 0.4F));
                            float targetSpeed = 0.8F;
                            if (targetSpeed > mineplexSpeed)
                                mineplexSpeed += targetSpeed / 8;
                            if (mineplexSpeed >= targetSpeed)
                                mineplexSpeed = targetSpeed;
                            movementUtil.setSpeed(mineplexSpeed, true);
                        } else {
                            mineplexSpeed = 0;
                        }
                    } else {
                        sendmsg("§cYou must have a Empty Slot!", true);
                        this.setToggled(false);
                    }

                    break;
                case "Mineplex":
                    if (!mc.thePlayer.isInWeb) {
                        if (getPlayer().onGround && isMoving())
                            getPlayer().jump();

                        if (isMoving()) {
                            getPlayer().setSprinting(false);
                            getGameSettings().keyBindSprint.pressed = false;
                            movementUtil.setSpeed(0.35D, true);
                            getPlayer().speedInAir = 0.044F;
                        }
                    }
                    break;
                case "MCCentral":
                    if (mc.thePlayer.onGround && isMoving()) {
                        mc.thePlayer.jump();
                    } else {
                        if (isMoving())
                            movementUtil.setSpeed(0.7, true);
                    }
                    break;
            }
        }

    }

    @Override
    public void onEnable() {
        mineplexMotion = 0.2F;
        curSlot = getPlayer().inventory.currentItem;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.speedInAir = 0.02F;
        mineplexSpeed = 0;
        if (mode.getCurrentMode().equalsIgnoreCase("Mineplex Ground"))
            sendPacket(new C09PacketHeldItemChange(curSlot));
    }

}