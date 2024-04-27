/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.features.module.modules.player;

import me.AquaVit.liquidSense.modules.movement.Flight;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.render.FreeCam;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.VecRotation;
import net.ccbluex.liquidbounce.utils.block.BlockUtils;
import net.ccbluex.liquidbounce.utils.misc.FallingPlayer;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(name = "NoFall", description = "Prevents you from taking fall damage.", category = ModuleCategory.PLAYER)
public class NoFall extends Module {

    public final ListValue modeValue = new ListValue("Mode", new String[]{"NewPacket","SpoofGround", "NoGround", "Packet", "AAC", "LAAC", "AAC3.3.11", "AAC3.3.15", "Spartan", "CubeCraft", "Hypixel","MLG","AAC4.4.0"}, "SpoofGround");
    private float lastFall;
    private int state;
    private boolean jumped;
    private final TickTimer spartanTimer = new TickTimer();
    private final FloatValue minFallDistance = new FloatValue("MinMLGHeight", 5F, 2F, 50F);
    private final TickTimer mlgTimer = new TickTimer();
    private VecRotation currentMlgRotation;
    private int currentMlgItemIndex;
    private BlockPos currentMlgBlock;
    private int hypixel;
    private List<Packet<?>> packets = new ArrayList<>();
    private boolean fakelag = false;
    public double fallDist = 0;
    private boolean packetmodify = false;
    Flight flight = (Flight) LiquidBounce.moduleManager.getModule(Flight.class);
    Fly fly = (Fly) LiquidBounce.moduleManager.getModule(Fly.class);

    private final BoolValue noSound = new BoolValue("NoSound", true);

    @Override
    public void onEnable() {
        if (modeValue.get().equalsIgnoreCase("AAC4.4.0")) {
            fakelag = false;
            packetmodify = false;
        }

    }

    @EventTarget(ignoreCondition = true)
    public void onUpdate(UpdateEvent event) {
        if(mc.thePlayer.onGround)
            jumped = false;

        if(mc.thePlayer.motionY > 0)
            jumped = true;

        if (!getState() || LiquidBounce.moduleManager.getModule(FreeCam.class).getState())
            return;

        if(BlockUtils.collideBlock(mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) ||
                BlockUtils.collideBlock(new AxisAlignedBB(mc.thePlayer.getEntityBoundingBox().maxX, mc.thePlayer.getEntityBoundingBox().maxY, mc.thePlayer.getEntityBoundingBox().maxZ, mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY - 0.01D, mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid))
            return;

        switch(modeValue.get().toLowerCase()) {
            case "hypixel":
                if (fallDist > mc.thePlayer.fallDistance)
                    fallDist = 0;

                if (mc.thePlayer.motionY < 0 && mc.thePlayer.fallDistance > 2.124 && !checkVoid(mc.thePlayer) && isBlockUnder() && !mc.thePlayer.isSpectator() && !mc.thePlayer.capabilities.allowFlying) {
                    double motionY = mc.thePlayer.motionY;
                    double fallingDist = mc.thePlayer.fallDistance - fallDist;
                    double realDist = fallingDist + -((motionY - 0.08D) * 0.9800000190734863D);
                    if (realDist >= 3) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                        fallDist = mc.thePlayer.fallDistance;
                        if(noSound.get()) mc.thePlayer.fallDistance = 0f;
                    }
                }
                break;
            case "packet":
                if(mc.thePlayer.fallDistance > 2F)
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                break;
            case "cubecraft":
                if(mc.thePlayer.fallDistance > 2F) {
                    mc.thePlayer.onGround = false;
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            case "aac":
                if(mc.thePlayer.fallDistance > 2F) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    state = 2;
                }else if(state == 2 && mc.thePlayer.fallDistance < 2) {
                    mc.thePlayer.motionY = 0.1D;
                    state = 3;
                    return;
                }

                switch(state) {
                    case 3:
                        mc.thePlayer.motionY = 0.1D;
                        state = 4;
                        break;
                    case 4:
                        mc.thePlayer.motionY = 0.1D;
                        state = 5;
                        break;
                    case 5:
                        mc.thePlayer.motionY = 0.1D;
                        state = 1;
                        break;
                }
                break;
            case "laac":
                if(!jumped && mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()
                        && !mc.thePlayer.isInWeb)
                    mc.thePlayer.motionY = -6;
                break;
            case "aac3.3.11":
                if(mc.thePlayer.fallDistance > 2) {
                    mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY - 10E-4D, mc.thePlayer.posZ, mc.thePlayer.onGround));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
                break;
            case "aac3.3.15":
                if(mc.thePlayer.fallDistance > 2) {
                    if(!mc.isIntegratedServerRunning())
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                Double.NaN, mc.thePlayer.posZ, false));
                    mc.thePlayer.fallDistance = -9999;
                }
                break;
            case "spartan":
                spartanTimer.update();

                if(mc.thePlayer.fallDistance > 1.5 && spartanTimer.hasTimePassed(10)) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 10, mc.thePlayer.posZ, true));
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY - 10, mc.thePlayer.posZ, true));
                    spartanTimer.reset();
                }
                break;
            case "newpacket":
                if (!MovementUtils.isOnGround(0.001)) {
                    if (mc.thePlayer.motionY < -0.08)
                        mc.thePlayer.fallDistance -= mc.thePlayer.motionY;
                    if ((mc.thePlayer.fallDistance > 2F) && (!mc.thePlayer.onGround)) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    }
                } else
                    mc.thePlayer.fallDistance = 0;
                break;
        }
    }
    @EventTarget
    private void onMotionUpdate(MotionEvent event) {
        if (modeValue.get().equalsIgnoreCase("NewPacket")) {
            if (mc.thePlayer.onGround) {
                fallDist = 0;
            } else if (mc.thePlayer.motionY < 0) {
                fallDist -= mc.thePlayer.motionY;
            }
        }
        if (modeValue.get().equalsIgnoreCase("AAC4.4.0")) {
            final EventState eventState = event.getEventState();
            if (eventState == EventState.PRE) {
                if (!inVoid()) {
                    if (fakelag) {
                        fakelag = false;
                        if (packets.size() > 0) {
                            for (Packet<?> packet : packets) {
                                mc.thePlayer.sendQueue.addToSendQueue(packet);
                            }
                            packets.clear();
                        }
                    }
                    return;
                }
                if (mc.thePlayer.onGround && fakelag) {
                    fakelag = false;
                    if (packets.size() > 0) {
                        for (Packet<?> packet : packets) {
                            mc.thePlayer.sendQueue.addToSendQueue(packet);
                        }
                        packets.clear();
                    }
                    return;
                }
                if (mc.thePlayer.fallDistance > 3 && fakelag) {
                    packetmodify = true;
                    mc.thePlayer.fallDistance = 0;
                }
                if (inAir(4.0, 1.0)) {
                    return;
                }
                if (!fakelag) {
                    fakelag = true;
                }
            }
        }

        if (modeValue.get().equalsIgnoreCase("MLG")){
            if (event.getEventState() == EventState.PRE) {
                currentMlgRotation = null;
                mlgTimer.update();

                if (!mlgTimer.hasTimePassed(10))
                    return;

                if (mc.thePlayer.fallDistance > minFallDistance.get()) {
                    FallingPlayer fallingPlayer = new FallingPlayer(
                            mc.thePlayer.posX,
                            mc.thePlayer.posY,
                            mc.thePlayer.posZ,
                            mc.thePlayer.motionX,
                            mc.thePlayer.motionY,
                            mc.thePlayer.motionZ,
                            mc.thePlayer.rotationYaw,
                            mc.thePlayer.moveStrafing,
                            mc.thePlayer.moveForward
                    );

                    double maxDist = mc.playerController.getBlockReachDistance() + 1.5;

                    FallingPlayer.CollisionResult collision = fallingPlayer.findCollision((int) Math.ceil((1.0 / mc.thePlayer.motionY) * (-maxDist)));

                    if (collision == null)
                        return;

                    boolean ok = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.eyeHeight, mc.thePlayer.posZ).distanceTo(new Vec3(collision.getPos()).addVector(0.5, 0.5, 0.5)) < mc.playerController.getBlockReachDistance() + Math.sqrt(0.75);

                    if (mc.thePlayer.motionY < (collision.getPos().getY() + 1) - mc.thePlayer.posY) {
                        ok = true;
                    }

                    if (!ok)
                        return;

                    int index = -1;

                    for (int i = 36; i < 45; i++) {
                        ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

                        if (itemStack != null && (itemStack.getItem() == Items.water_bucket || itemStack.getItem() instanceof ItemBlock && ((ItemBlock) itemStack.getItem()).getBlock() == Blocks.web)) {
                            index = i - 36;

                            if (mc.thePlayer.inventory.currentItem == index)
                                break;
                        }
                    }

                    if (index == -1)
                        return;

                    currentMlgItemIndex = index;
                    currentMlgBlock = collision.getPos();

                    if (mc.thePlayer.inventory.currentItem != index) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(index));
                    }

                    currentMlgRotation = RotationUtils.faceBlock(collision.getPos());
                    currentMlgRotation.getRotation().toPlayer(mc.thePlayer);
                }
            } else if (currentMlgRotation != null) {
                ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(currentMlgItemIndex + 36).getStack();

                if (stack.getItem() instanceof ItemBucket) {
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, stack);
                } else {
                    Vec3i dirVec = EnumFacing.UP.getDirectionVec();

                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, stack, currentMlgBlock, EnumFacing.UP, new Vec3(dirVec.getX() * 0.5, dirVec.getY() * 0.5, dirVec.getZ() * 0.5).add(new Vec3(currentMlgBlock)))) {
                        mlgTimer.reset();
                    }
                }

                if (mc.thePlayer.inventory.currentItem != currentMlgItemIndex)
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
            }
        }


    }

    public static boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0.0D) {
            return false;
        } else {
            int off = 0;

            while(true) {
                if (off >= (int)mc.thePlayer.posY + 2) {
                    return false;
                }

                AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0.0D, (double)(-off), 0.0D);
                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }

                off += 2;
            }
        }
    }

    public static int getJumpEffect() {
        return mc.thePlayer.isPotionActive(Potion.jump)?mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1:0;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        final Packet<?> packet = event.getPacket();
        final String mode = modeValue.get();

        if (mode.equalsIgnoreCase("AAC4.4.0")) {
            if (packet instanceof C03PacketPlayer && fakelag) {
                event.cancelEvent();
                if (packetmodify) {
                    ((C03PacketPlayer) packet).onGround = true;
                    packetmodify = false;
                }
                packets.add(packet);
            }
        }

        if(packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("SpoofGround")){
            ((C03PacketPlayer) packet).onGround = true;
        }


        if(packet instanceof C03PacketPlayer && mode.equalsIgnoreCase("NoGround")){
            ((C03PacketPlayer) packet).onGround = true;
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if(BlockUtils.collideBlock(mc.thePlayer.getEntityBoundingBox(), block -> block instanceof BlockLiquid) || BlockUtils.collideBlock(new AxisAlignedBB(mc.thePlayer.getEntityBoundingBox().maxX, mc.thePlayer.getEntityBoundingBox().maxY, mc.thePlayer.getEntityBoundingBox().maxZ, mc.thePlayer.getEntityBoundingBox().minX, mc.thePlayer.getEntityBoundingBox().minY - 0.01D, mc.thePlayer.getEntityBoundingBox().minZ), block -> block instanceof BlockLiquid))
            return;

        if (modeValue.get().equalsIgnoreCase("laac")) {
            if (!jumped && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isInWeb && mc.thePlayer.motionY < 0D) {
                event.setX(0);
                event.setZ(0);
            }
        }
    }
    public boolean inVoid() {
        if (mc.thePlayer.posY < 0) {
            return false;
        }

        for (int off = 0; off < mc.thePlayer.posY + 2; off += 2) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private boolean isVoid(int X, int Z, EntityLivingBase entity){
        Fly fly = (Fly) LiquidBounce.moduleManager.getModule(Fly.class);

        if (fly.getState()) {
            return false;
        }
        if (mc.thePlayer.posY < 0.0) {
            return true;
        }
        for (int off = 0; off < entity.posY + 2; off += 2) {
            AxisAlignedBB bb = entity.getEntityBoundingBox().offset(X,-off,Z);
            if (mc.theWorld.getCollidingBoundingBoxes(entity, bb).isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    private boolean checkVoid(EntityLivingBase entity) {
        for(int x = -1; x <= 0; ++x) {
            for(int z = -1; z <= 0; ++z) {
                if (this.isVoid(x, z, entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inAir(double height, double plus) {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int off = 0; off < height; off += plus) {
            AxisAlignedBB bb = new AxisAlignedBB(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.posX, mc.thePlayer.posY - off, mc.thePlayer.posZ);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    @EventTarget(ignoreCondition = true)
    public void onJump(final JumpEvent event) {
        jumped = true;
    }

    @Override
    public String getTag() {
        return modeValue.get();
    }
}
