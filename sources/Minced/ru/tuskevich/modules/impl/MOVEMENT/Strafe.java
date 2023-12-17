// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import ru.tuskevich.event.events.impl.EventAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.MobEffects;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import java.util.Iterator;
import ru.tuskevich.util.movement.MoveUtility;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.event.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.util.math.TimerUtility;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Strafe", desc = "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0441\u0442\u0440\u0435\u0439\u0444\u0438\u0442\u044c", type = Type.MOVEMENT)
public class Strafe extends Module
{
    private static boolean damageboolean;
    private float waterTicks;
    public static BooleanSetting elytra;
    public static BooleanSetting damage;
    public ModeSetting mode;
    private final SliderSetting boost;
    public SliderSetting setSpeed;
    TimerUtility timerUtility;
    public static double oldSpeed;
    public static double contextFriction;
    public static boolean needSwap;
    public static boolean prevSprint;
    public static boolean needSprintState;
    public static int counter;
    public static int noSlowTicks;
    public int ticks2;
    public boolean a;
    private boolean boosted;
    private boolean disabled;
    
    public Strafe() {
        this.waterTicks = 0.0f;
        this.mode = new ModeSetting("Boost mode", "Matrix Safe", () -> Strafe.elytra.get(), new String[] { "Matrix Safe", "Matrix Hard" });
        this.boost = new SliderSetting("Boost Multiplier", 0.25f, 0.1f, 0.2f, 0.01f, () -> Strafe.damage.get());
        this.setSpeed = new SliderSetting("Speed", 1.5f, 0.5f, 2.5f, 0.1f, () -> Strafe.elytra.get());
        this.timerUtility = new TimerUtility();
        this.add(this.mode, Strafe.elytra, Strafe.damage, this.boost, this.setSpeed);
    }
    
    @EventTarget
    public void onPacket(final EventPacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            Strafe.oldSpeed = 0.0;
        }
    }
    
    @EventTarget
    public void onUpdate(final EventMotion e) {
        if (Strafe.damage.get()) {
            Strafe.damageboolean = true;
            final Minecraft mc = Strafe.mc;
            if (Minecraft.player.hurtTime > 0) {
                this.a = true;
            }
            if (this.a) {
                final Minecraft mc2 = Strafe.mc;
                if (Minecraft.player.onGround) {
                    final Minecraft mc3 = Strafe.mc;
                    Minecraft.player.jump();
                }
                final Minecraft mc4 = Strafe.mc;
                if (Minecraft.player.ticksExisted % 2 == 0) {
                    this.ticks2 += (int)10.0f;
                }
                final Minecraft mc5 = Strafe.mc;
                Minecraft.player.speedInAir = this.boost.getFloatValue();
                final Minecraft mc6 = Strafe.mc;
                if (Minecraft.player.onGround) {
                    this.a = false;
                }
            }
            else {
                final Minecraft mc7 = Strafe.mc;
                Minecraft.player.speedInAir = 0.02f;
            }
        }
        else {
            Strafe.damageboolean = false;
        }
        final int elytraSlot = this.getHotbarSlotOfItem();
        Label_0486: {
            if (Strafe.elytra.get()) {
                final Minecraft mc8 = Strafe.mc;
                for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
                    if (stack.getItem() == Items.ELYTRA) {
                        swapElytraToChestplate();
                    }
                }
                final int elytra = this.getHotbarSlotOfItem();
                final Minecraft mc9 = Strafe.mc;
                if (!Minecraft.player.isInWater()) {
                    final Minecraft mc10 = Strafe.mc;
                    if (!Minecraft.player.isInLava() && this.waterTicks <= 0.0f && elytra != -1) {
                        final Minecraft mc11 = Strafe.mc;
                        if (!Minecraft.player.isInWeb) {
                            final Minecraft mc12 = Strafe.mc;
                            if (Minecraft.player.fallDistance == 0.0f) {
                                break Label_0486;
                            }
                            final Minecraft mc13 = Strafe.mc;
                            if (Minecraft.player.fallDistance >= 0.1) {
                                break Label_0486;
                            }
                            final Minecraft mc14 = Strafe.mc;
                            if (Minecraft.player.motionY >= -0.1) {
                                break Label_0486;
                            }
                            if (elytra != -2) {
                                final PlayerControllerMP playerController = Strafe.mc.playerController;
                                final int windowId = 0;
                                final int slotId = elytra;
                                final int mouseButton = 1;
                                final ClickType pickup = ClickType.PICKUP;
                                final Minecraft mc15 = Strafe.mc;
                                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                                final PlayerControllerMP playerController2 = Strafe.mc.playerController;
                                final int windowId2 = 0;
                                final int slotId2 = 6;
                                final int mouseButton2 = 1;
                                final ClickType pickup2 = ClickType.PICKUP;
                                final Minecraft mc16 = Strafe.mc;
                                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                            }
                            final NetHandlerPlayClient connection = Strafe.mc.getConnection();
                            final Minecraft mc17 = Strafe.mc;
                            connection.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            this.disabled = true;
                            final NetHandlerPlayClient connection2 = Strafe.mc.getConnection();
                            final Minecraft mc18 = Strafe.mc;
                            connection2.sendPacket(new CPacketEntityAction(Minecraft.player, CPacketEntityAction.Action.START_FALL_FLYING));
                            if (elytra != -2) {
                                final PlayerControllerMP playerController3 = Strafe.mc.playerController;
                                final int windowId3 = 0;
                                final int slotId3 = 6;
                                final int mouseButton3 = 1;
                                final ClickType pickup3 = ClickType.PICKUP;
                                final Minecraft mc19 = Strafe.mc;
                                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
                                final PlayerControllerMP playerController4 = Strafe.mc.playerController;
                                final int windowId4 = 0;
                                final int slotId4 = elytra;
                                final int mouseButton4 = 1;
                                final ClickType pickup4 = ClickType.PICKUP;
                                final Minecraft mc20 = Strafe.mc;
                                playerController4.windowClick(windowId4, slotId4, mouseButton4, pickup4, Minecraft.player);
                            }
                            break Label_0486;
                        }
                    }
                }
                return;
            }
        }
        Label_1004: {
            if (Strafe.elytra.get() && elytraSlot != -1) {
                Label_0869: {
                    if (this.mode.is("Matrix Hard")) {
                        final Minecraft mc21 = Strafe.mc;
                        Label_0746: {
                            if (!Minecraft.player.onGround) {
                                final WorldClient world = Strafe.mc.world;
                                final Minecraft mc22 = Strafe.mc;
                                final EntityPlayerSP player = Minecraft.player;
                                final Minecraft mc23 = Strafe.mc;
                                if (!world.getCollisionBoxes(player, Minecraft.player.getEntityBoundingBox().offset(0.0, -1.0, 0.0).expand(0.0, 10.0, 0.0)).isEmpty()) {
                                    if (MoveUtility.isMoving() && this.disabled) {
                                        final Minecraft mc24 = Strafe.mc;
                                        if (Minecraft.player.isAirBorne) {
                                            final Minecraft mc25 = Strafe.mc;
                                            if (Minecraft.player.ticksExisted % 5 == 0) {
                                                this.boosted = true;
                                            }
                                        }
                                        else {
                                            final Minecraft mc26 = Strafe.mc;
                                            if (Minecraft.player.ticksExisted % 6 == 0) {
                                                this.boosted = false;
                                                this.disabled = false;
                                            }
                                        }
                                        if (this.boosted) {
                                            MoveUtility.setMotion(this.setSpeed.getFloatValue() - 0.1f);
                                            Strafe.oldSpeed = (this.setSpeed.getFloatValue() - 0.1f) / 1.06;
                                        }
                                    }
                                    final Minecraft mc27 = Strafe.mc;
                                    if (Minecraft.player.collidedVertically) {
                                        this.boosted = false;
                                    }
                                    final Minecraft mc28 = Strafe.mc;
                                    if (Minecraft.player.onGround && !MoveUtility.isMoving()) {
                                        this.boosted = false;
                                    }
                                    break Label_0746;
                                }
                            }
                            this.boosted = false;
                            Strafe.oldSpeed = MoveUtility.getSpeed();
                        }
                        final Minecraft mc29 = Strafe.mc;
                        if (!Minecraft.player.onGround) {
                            final WorldClient world2 = Strafe.mc.world;
                            final Minecraft mc30 = Strafe.mc;
                            final EntityPlayerSP player2 = Minecraft.player;
                            final Minecraft mc31 = Strafe.mc;
                            if (!world2.getCollisionBoxes(player2, Minecraft.player.getEntityBoundingBox().offset(0.0, -1.0, 0.0).expand(0.0, 0.2, 0.0)).isEmpty()) {
                                final Minecraft mc32 = Strafe.mc;
                                if (Minecraft.player.fallDistance >= 0.15) {
                                    if (MoveUtility.isMoving()) {
                                        MoveUtility.setMotion(this.setSpeed.getFloatValue());
                                        Strafe.oldSpeed = this.setSpeed.getFloatValue() / 1.06;
                                    }
                                    break Label_0869;
                                }
                            }
                        }
                        Strafe.oldSpeed = MoveUtility.getSpeed();
                    }
                }
                if (this.mode.is("Matrix Safe")) {
                    final Minecraft mc33 = Strafe.mc;
                    if (!Minecraft.player.onGround) {
                        final WorldClient world3 = Strafe.mc.world;
                        final Minecraft mc34 = Strafe.mc;
                        final EntityPlayerSP player3 = Minecraft.player;
                        final Minecraft mc35 = Strafe.mc;
                        if (!world3.getCollisionBoxes(player3, Minecraft.player.getEntityBoundingBox().offset(0.0, -1.0, 0.0).expand(0.0, 0.2, 0.0)).isEmpty()) {
                            final Minecraft mc36 = Strafe.mc;
                            if (Minecraft.player.fallDistance >= 0.15) {
                                if (MoveUtility.isMoving()) {
                                    MoveUtility.setMotion(this.setSpeed.getFloatValue());
                                    Strafe.oldSpeed = this.setSpeed.getFloatValue() / 1.06;
                                }
                                break Label_1004;
                            }
                        }
                    }
                    Strafe.oldSpeed = MoveUtility.getSpeed();
                }
            }
        }
        final Minecraft mc37 = Strafe.mc;
        if (Minecraft.player.isInWater()) {
            this.waterTicks = 10.0f;
        }
        else {
            --this.waterTicks;
        }
        if (this.strafes()) {
            final Minecraft mc38 = Strafe.mc;
            double forward = Minecraft.player.movementInput.moveForward;
            final Minecraft mc39 = Strafe.mc;
            double strafe = Minecraft.player.movementInput.moveStrafe;
            final Minecraft mc40 = Strafe.mc;
            float yaw = Minecraft.player.rotationYaw;
            if (forward == 0.0 && strafe == 0.0) {
                Strafe.oldSpeed = 0.0;
                final Minecraft mc41 = Strafe.mc;
                Minecraft.player.motionX = 0.0;
                final Minecraft mc42 = Strafe.mc;
                Minecraft.player.motionZ = 0.0;
            }
            else {
                if (forward != 0.0) {
                    if (strafe > 0.0) {
                        yaw += ((forward > 0.0) ? -45 : 45);
                    }
                    else if (strafe < 0.0) {
                        yaw += ((forward > 0.0) ? 45 : -45);
                    }
                    strafe = 0.0;
                    if (forward > 0.0) {
                        forward = 1.0;
                    }
                    else if (forward < 0.0) {
                        forward = -1.0;
                    }
                }
                final double speed = calculateSpeed();
                final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
                final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
                final Minecraft mc43 = Strafe.mc;
                Minecraft.player.motionX = forward * speed * cos + strafe * speed * sin;
                final Minecraft mc44 = Strafe.mc;
                Minecraft.player.motionZ = forward * speed * sin - strafe * speed * cos;
            }
        }
        else {
            Strafe.oldSpeed = 0.0;
        }
    }
    
    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            final Minecraft mc = Strafe.mc;
            final ItemStack itemStack = Minecraft.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void swapElytraToChestplate() {
        final Minecraft mc = Strafe.mc;
        for (final ItemStack stack : Minecraft.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {
                final int slot = (getSlowWithArmor() < 9) ? (getSlowWithArmor() + 36) : getSlowWithArmor();
                if (getSlowWithArmor() == -1) {
                    continue;
                }
                final PlayerControllerMP playerController = Strafe.mc.playerController;
                final int windowId = 0;
                final int slotId = slot;
                final int mouseButton = 1;
                final ClickType pickup = ClickType.PICKUP;
                final Minecraft mc2 = Strafe.mc;
                playerController.windowClick(windowId, slotId, mouseButton, pickup, Minecraft.player);
                final PlayerControllerMP playerController2 = Strafe.mc.playerController;
                final int windowId2 = 0;
                final int slotId2 = 6;
                final int mouseButton2 = 0;
                final ClickType pickup2 = ClickType.PICKUP;
                final Minecraft mc3 = Strafe.mc;
                playerController2.windowClick(windowId2, slotId2, mouseButton2, pickup2, Minecraft.player);
                final PlayerControllerMP playerController3 = Strafe.mc.playerController;
                final int windowId3 = 0;
                final int slotId3 = slot;
                final int mouseButton3 = 1;
                final ClickType pickup3 = ClickType.PICKUP;
                final Minecraft mc4 = Strafe.mc;
                playerController3.windowClick(windowId3, slotId3, mouseButton3, pickup3, Minecraft.player);
            }
        }
    }
    
    @Override
    public void onEnable() {
        Strafe.oldSpeed = 0.0;
        this.boosted = false;
        this.disabled = false;
        this.timerUtility.reset();
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.a = false;
        this.ticks2 = 0;
        final Minecraft mc = Strafe.mc;
        Minecraft.player.speedInAir = 0.02f;
        Strafe.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    public boolean strafes() {
        final Minecraft mc = Strafe.mc;
        if (Minecraft.player.isSneaking()) {
            return false;
        }
        final Minecraft mc2 = Strafe.mc;
        if (Minecraft.player.isInLava()) {
            return false;
        }
        final Minecraft mc3 = Strafe.mc;
        if (Minecraft.player.isInWater()) {
            return false;
        }
        final Minecraft mc4 = Strafe.mc;
        return !Minecraft.player.capabilities.isFlying;
    }
    
    public static double calculateSpeed() {
        final Minecraft mc = Minecraft.getMinecraft();
        final boolean fromGround = Minecraft.player.onGround;
        final boolean toGround = !Minecraft.player.onGround;
        final boolean jump = Minecraft.player.motionY > 0.0;
        final float speedAttributes = getAIMoveSpeed(Minecraft.player);
        final float frictionFactor = getFrictionFactor(Minecraft.player);
        float n6 = (Minecraft.player.isPotionActive(MobEffects.JUMP_BOOST) && Minecraft.player.isHandActive()) ? 0.88f : ((float)((Strafe.oldSpeed > 0.32 && Minecraft.player.isHandActive()) ? 0.88 : 0.9100000262260437));
        if (fromGround) {
            n6 = frictionFactor;
        }
        final float n7 = (float)(0.16277135908603668 / Math.pow(n6, 3.01));
        final boolean noslow = false;
        double max2 = Strafe.oldSpeed;
        final double max3 = 0.0;
        if (!Minecraft.player.isHandActive() || jump) {
            Strafe.noSlowTicks = 0;
        }
        if (Strafe.noSlowTicks > 3) {
            max2 = max3 - 0.019;
        }
        else if (Strafe.elytra.get()) {
            if (MoveUtility.isMoving() && !Minecraft.player.onGround && Minecraft.player.fallDistance >= 0.15) {
                max2 = Math.max(noslow ? 0.0 : 0.25, max2) - ((Strafe.counter++ % 2 == 0) ? 0.001 : 0.002);
            }
            else {
                max2 = MoveUtility.getSpeed();
            }
        }
        else {
            max2 = MoveUtility.getSpeed();
        }
        Strafe.contextFriction = n6;
        if (!fromGround) {
            Strafe.needSwap = true;
        }
        else {
            Strafe.prevSprint = false;
        }
        if (toGround && fromGround) {
            Strafe.needSprintState = false;
        }
        return max2;
    }
    
    public static void postMove(final double horizontal) {
        Strafe.oldSpeed = horizontal * Strafe.contextFriction;
    }
    
    public static float getAIMoveSpeed(final EntityPlayer contextPlayer) {
        final boolean prevSprinting = contextPlayer.isSprinting();
        contextPlayer.setSprinting(false);
        final float speed = contextPlayer.getAIMoveSpeed() * 1.3f;
        contextPlayer.setSprinting(prevSprinting);
        return speed;
    }
    
    private static float getFrictionFactor(final EntityPlayer contextPlayer) {
        final Minecraft mc = Strafe.mc;
        final double motionX = Minecraft.player.motionX;
        final Minecraft mc2 = Strafe.mc;
        final double yIn = Minecraft.player.motionY - 1.0;
        final Minecraft mc3 = Strafe.mc;
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(motionX, yIn, Minecraft.player.motionZ);
        return contextPlayer.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91f;
    }
    
    private int getHotbarSlotOfItem() {
        final Minecraft mc = Strafe.mc;
        for (final ItemStack stack : Minecraft.player.getArmorInventoryList()) {
            if (stack.getItem() == Items.ELYTRA) {
                return -2;
            }
        }
        int slot = -1;
        for (int i = 0; i < 36; ++i) {
            final Minecraft mc2 = Strafe.mc;
            final ItemStack s = Minecraft.player.inventory.getStackInSlot(i);
            if (s.getItem() == Items.ELYTRA) {
                slot = i;
                break;
            }
        }
        if (slot < 9 && slot != -1) {
            slot += 36;
        }
        return slot;
    }
    
    @EventTarget
    public void onAction(final EventAction eventAction) {
        if (Strafe.needSwap) {
            final Minecraft mc = Strafe.mc;
            eventAction.sprintState = !Minecraft.player.serverSprintState;
            Strafe.needSwap = false;
        }
    }
    
    static {
        Strafe.elytra = new BooleanSetting("Elytra Boost", false, () -> !Strafe.damageboolean);
        Strafe.damage = new BooleanSetting("Damage Boost", false, () -> !Strafe.elytra.get());
    }
}
