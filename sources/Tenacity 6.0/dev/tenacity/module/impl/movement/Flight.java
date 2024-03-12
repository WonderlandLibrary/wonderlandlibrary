// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import java.util.HashSet;
import java.util.function.Consumer;
import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.utils.player.InventoryUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.util.AxisAlignedBB;
import dev.tenacity.event.impl.player.BoundingBoxEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.event.impl.player.UpdateEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Vec3i;
import org.apache.commons.lang3.RandomUtils;
import dev.tenacity.utils.player.DamageUtils;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.init.Blocks;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.module.impl.combat.TargetStrafe;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MoveEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.settings.ParentAttribute;
import dev.tenacity.module.Category;
import net.minecraft.util.BlockPos;
import java.util.Set;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.network.Packet;
import java.util.concurrent.CopyOnWriteArrayList;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class Flight extends Module
{
    private final ModeSetting mode;
    private final NumberSetting teleportDelay;
    private final NumberSetting teleportLength;
    private final NumberSetting timerAmount;
    private final NumberSetting horizontalSpeed;
    private final NumberSetting verticalSpeed;
    private final BooleanSetting viewBobbing;
    private final BooleanSetting antiKick;
    private int stage;
    private int ticks;
    private int mospixelTicks;
    private boolean doFly;
    private double x;
    private double y;
    private double z;
    private double lastX;
    private double lastY;
    private double lastZ;
    private final CopyOnWriteArrayList<Packet> packets;
    private boolean hasClipped;
    private int slot;
    private double speedStage;
    private float clip;
    private double moveSpeed;
    private int stage2;
    private final TimerUtil timer;
    public static final Set<BlockPos> hiddenBlocks;
    private boolean hasS08;
    private boolean hasDamaged;
    private boolean up;
    private boolean jumped;
    private int airTicks;
    private boolean adjustSpeed;
    private boolean canSpeed;
    private boolean hasBeenDamaged;
    public double moveSpeed2;
    public double lastDist;
    public int stage3;
    private final BooleanSetting damage;
    private final ModeSetting damageMode;
    private final NumberSetting motionY;
    private final BooleanSetting speed;
    private final NumberSetting speedAmount;
    
    public Flight() {
        super("Flight", "Flight", Category.MOVEMENT, "Makes you hover in the air");
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Zonecraft", "Watchdog", "Vanilla", "AirWalk", "Viper", "Verus", "Minemen", "Old NCP", "Mospixel", "Slime", "Custom", "Packet", "Minemora", "Vulcan" });
        this.teleportDelay = new NumberSetting("Teleport Delay", 5.0, 20.0, 1.0, 1.0);
        this.teleportLength = new NumberSetting("Teleport Length", 5.0, 20.0, 1.0, 1.0);
        this.timerAmount = new NumberSetting("Timer Amount", 1.0, 3.0, 0.1, 0.1);
        this.horizontalSpeed = new NumberSetting("Horizontal Speed", 2.0, 5.0, 0.0, 0.1);
        this.verticalSpeed = new NumberSetting("Vertical Speed", 1.0, 5.0, 0.0, 0.1);
        this.viewBobbing = new BooleanSetting("View Bobbing", true);
        this.antiKick = new BooleanSetting("Anti-kick", false);
        this.packets = new CopyOnWriteArrayList<Packet>();
        this.slot = 0;
        this.timer = new TimerUtil();
        this.damage = new BooleanSetting("Damage", false);
        this.damageMode = new ModeSetting("Damage Mode", "Vanilla", new String[] { "Vanilla", "Suffocate", "NCP" });
        this.motionY = new NumberSetting("Motion Y", 0.0, 0.3, -0.3, 0.01);
        this.speed = new BooleanSetting("Speed", false);
        this.speedAmount = new NumberSetting("Speed Amount", 0.2, 9.0, 0.05, 0.01);
        this.horizontalSpeed.addParent(this.mode, m -> m.is("Vanilla"));
        this.verticalSpeed.addParent(this.mode, m -> m.is("Vanilla"));
        this.antiKick.addParent(this.mode, m -> m.is("Vanilla"));
        this.damage.addParent(this.mode, m -> m.is("Custom"));
        this.damageMode.addParent(this.damage, ParentAttribute.BOOLEAN_CONDITION);
        this.motionY.addParent(this.mode, m -> m.is("Custom"));
        this.speed.addParent(this.mode, m -> m.is("Custom"));
        this.speedAmount.addParent(this.speed, ParentAttribute.BOOLEAN_CONDITION);
        this.teleportDelay.addParent(this.mode, m -> m.is("Packet"));
        this.teleportLength.addParent(this.mode, m -> m.is("Packet"));
        this.addSettings(this.mode, this.teleportDelay, this.teleportLength, this.motionY, this.damage, this.damageMode, this.speed, this.speedAmount, this.timerAmount, this.horizontalSpeed, this.verticalSpeed, this.viewBobbing, this.antiKick);
    }
    
    @Override
    public void onMoveEvent(final MoveEvent e) {
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vanilla": {
                e.setSpeed(MovementUtils.isMoving() ? ((double)this.horizontalSpeed.getValue().floatValue()) : 0.0);
                TargetStrafe.strafe(e, this.horizontalSpeed.getValue().floatValue());
                break;
            }
            case "Watchdog": {
                e.setSpeed(0.0);
                break;
            }
            case "Slime": {
                if (this.stage < 8) {
                    e.setSpeed(0.0);
                    break;
                }
                break;
            }
            case "Packet": {
                e.setSpeed(0.0);
                break;
            }
            case "Minemora": {
                if (!this.hasDamaged) {
                    e.setSpeed(0.0);
                    break;
                }
                break;
            }
            case "Mospixel": {
                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed());
                break;
            }
            default: {
                TargetStrafe.strafe(e);
                break;
            }
        }
    }
    
    @Override
    public void onMotionEvent(final MotionEvent e) {
        this.setSuffix(this.mode.getMode());
        if (this.viewBobbing.isEnabled()) {
            final EntityPlayerSP thePlayer = Flight.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = Flight.mc.thePlayer;
            final float n = 0.08f;
            thePlayer2.cameraPitch = n;
            thePlayer.cameraYaw = n;
        }
        Flight.mc.timer.timerSpeed = this.timerAmount.getValue().floatValue();
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Watchdog": {
                if (!e.isPre()) {
                    break;
                }
                Flight.mc.thePlayer.motionY = 0.0;
                ++this.stage;
                if (this.stage == 1) {
                    final double x = e.getX() + -Math.sin(Math.toRadians(Flight.mc.thePlayer.rotationYaw)) * 7.99;
                    final double y = e.getY() - 1.75;
                    final double z = e.getZ() + Math.cos(Math.toRadians(Flight.mc.thePlayer.rotationYaw)) * 7.99;
                    if (Flight.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock() == Blocks.air) {
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        Flight.mc.thePlayer.setPosition(x, y, z);
                    }
                    break;
                }
                break;
            }
            case "Vulcan": {
                if (e.isPre()) {
                    Flight.mc.thePlayer.motionY = 0.0;
                    e.setOnGround(true);
                    Flight.mc.playerController.onPlayerRightClick(Flight.mc.thePlayer, Flight.mc.theWorld, Flight.mc.thePlayer.getHeldItem(), new BlockPos(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY - 2.0, Flight.mc.thePlayer.posZ), EnumFacing.UP, new Vec3(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY - 2.0, Flight.mc.thePlayer.posZ));
                    break;
                }
                break;
            }
            case "Minemora": {
                if (!e.isPre()) {
                    break;
                }
                if (this.stage < 3) {
                    e.setOnGround(false);
                    if (Flight.mc.thePlayer.onGround) {
                        Flight.mc.thePlayer.jump();
                        ++this.stage;
                        break;
                    }
                    break;
                }
                else {
                    if (Flight.mc.thePlayer.hurtTime > 0 && !this.hasDamaged) {
                        this.hasDamaged = true;
                    }
                    if (this.hasDamaged) {
                        Flight.mc.thePlayer.motionY = -MathUtils.getRandomInRange(0.005, 0.0051);
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.5);
                        break;
                    }
                    break;
                }
                break;
            }
            case "Zonecraft": {
                if (e.isPre()) {
                    switch (++this.stage) {
                        case 1: {
                            e.setOnGround(true);
                            MovementUtils.setSpeed(0.55);
                            break;
                        }
                    }
                    Flight.mc.thePlayer.motionY = 0.0;
                    e.setY(Flight.mc.thePlayer.posY + 0.1);
                    break;
                }
                break;
            }
            case "Verus": {
                if (e.isPre()) {
                    if (!Flight.mc.gameSettings.keyBindJump.isKeyDown()) {
                        if (Flight.mc.thePlayer.onGround) {
                            Flight.mc.thePlayer.motionY = 0.41999998688697815;
                            this.up = true;
                        }
                        else if (this.up) {
                            if (!Flight.mc.thePlayer.isCollidedHorizontally) {
                                Flight.mc.thePlayer.motionY = -0.0784000015258789;
                            }
                            this.up = false;
                        }
                    }
                    else if (Flight.mc.thePlayer.ticksExisted % 3 == 0) {
                        Flight.mc.thePlayer.motionY = 0.41999998688697815;
                    }
                    MovementUtils.setSpeed(Flight.mc.gameSettings.keyBindJump.isKeyDown() ? 0.0 : 0.33);
                    break;
                }
                break;
            }
            case "Vanilla": {
                if (TargetStrafe.canStrafe()) {
                    Flight.mc.thePlayer.motionY = (this.antiKick.isEnabled() ? -0.0625 : 0.0);
                    break;
                }
                Flight.mc.thePlayer.motionY = (Flight.mc.gameSettings.keyBindJump.isKeyDown() ? this.verticalSpeed.getValue() : (Flight.mc.gameSettings.keyBindSneak.isKeyDown() ? (-this.verticalSpeed.getValue()) : (this.antiKick.isEnabled() ? -0.0625 : 0.0)));
                break;
            }
            case "Viper": {
                Flight.mc.thePlayer.motionY = (Flight.mc.gameSettings.keyBindJump.isKeyDown() ? 1.0 : (Flight.mc.gameSettings.keyBindSneak.isKeyDown() ? -1.0 : 0.0));
                e.setOnGround(true);
                MovementUtils.setSpeed(0.3);
                break;
            }
            case "Old NCP": {
                if (this.hasDamaged) {
                    e.setOnGround(true);
                    final double baseSpeed = MovementUtils.getBaseMoveSpeed();
                    if (!MovementUtils.isMoving() || Flight.mc.thePlayer.isCollidedHorizontally) {
                        this.moveSpeed = baseSpeed;
                    }
                    if (this.moveSpeed > baseSpeed) {
                        this.moveSpeed -= this.moveSpeed / 159.0;
                    }
                    this.moveSpeed = Math.max(baseSpeed, this.moveSpeed);
                    if (e.isPre()) {
                        Flight.mc.timer.timerSpeed = 1.0f;
                        if (MovementUtils.isMoving()) {
                            MovementUtils.setSpeed(this.moveSpeed);
                        }
                        Flight.mc.thePlayer.motionY = 0.0;
                        final double y = 1.0E-10;
                        e.setY(e.getY() - y);
                    }
                    break;
                }
                if (Flight.mc.thePlayer.onGround) {
                    DamageUtils.damage(DamageUtils.DamageType.WATCHDOGUP);
                    Flight.mc.thePlayer.jump();
                    this.hasDamaged = true;
                    break;
                }
                break;
            }
            case "Mospixel": {
                if (this.hasDamaged) {
                    if (e.isPre()) {
                        if (Flight.mc.thePlayer.isCollidedHorizontally || !MovementUtils.isMoving() || (this.stage >= 15 && !this.jumped && !this.damage.isEnabled())) {
                            this.moveSpeed = MovementUtils.getBaseMoveSpeed();
                            Flight.mc.timer.timerSpeed = 1.0f;
                        }
                        double value = RandomUtils.nextDouble(1.0E-7, 1.2E-7);
                        if (Flight.mc.thePlayer.ticksExisted % 2 == 0) {
                            value = -value;
                        }
                        if (!Flight.mc.thePlayer.onGround) {
                            Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + value, Flight.mc.thePlayer.posZ);
                        }
                        Flight.mc.thePlayer.motionY = 0.0;
                        break;
                    }
                    break;
                }
                else {
                    if (Flight.mc.thePlayer.onGround) {
                        Flight.mc.thePlayer.jump();
                        this.hasDamaged = true;
                        break;
                    }
                    break;
                }
                break;
            }
            case "Slime": {
                if (!e.isPre()) {
                    break;
                }
                switch (++this.stage) {
                    case 1: {
                        if (Flight.mc.thePlayer.onGround) {
                            Flight.mc.thePlayer.jump();
                            break;
                        }
                        break;
                    }
                    case 7: {
                        final BlockPos pos = new BlockPos(e.getX(), e.getY() - 2.0, e.getZ());
                        e.setPitch(Flight.mc.thePlayer.rotationPitchHead = 90.0f);
                        if (Flight.mc.playerController.onPlayerRightClick(Flight.mc.thePlayer, Flight.mc.theWorld, Flight.mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos))) {
                            Flight.mc.thePlayer.swingItem();
                            break;
                        }
                        break;
                    }
                }
                if (this.stage > 8) {
                    e.setOnGround(true);
                    MovementUtils.setSpeed(0.3);
                    Flight.mc.thePlayer.motionY = 0.0;
                    break;
                }
                break;
            }
            case "Custom": {
                if (!e.isPre()) {
                    break;
                }
                switch (++this.stage) {
                    case 1: {
                        if (this.damage.isEnabled()) {
                            DamageUtils.damage(DamageUtils.DamageType.valueOf(this.damageMode.getMode().toUpperCase()));
                            break;
                        }
                        break;
                    }
                }
                Flight.mc.thePlayer.motionY = this.motionY.getValue();
                if (this.speed.isEnabled()) {
                    MovementUtils.setSpeed(this.speedAmount.getValue());
                    break;
                }
                break;
            }
            case "Packet": {
                if (!e.isPre()) {
                    break;
                }
                Flight.mc.thePlayer.motionY = 0.0;
                if (MovementUtils.isMoving() && Flight.mc.thePlayer.ticksExisted % this.teleportDelay.getValue().intValue() == 0) {
                    final double x = e.getX() + -Math.sin(Math.toRadians(Flight.mc.thePlayer.rotationYaw)) * this.teleportLength.getValue().intValue();
                    final double z2 = e.getZ() + Math.cos(Math.toRadians(Flight.mc.thePlayer.rotationYaw)) * this.teleportLength.getValue().intValue();
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(x, Flight.mc.thePlayer.posY, z2, false));
                    Flight.mc.thePlayer.setPosition(x, Flight.mc.thePlayer.posY, z2);
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onUpdateEvent(final UpdateEvent event) {
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (Flight.mc.isSingleplayer() || Flight.mc.thePlayer == null) {
            return;
        }
        if (this.mode.is("Slime") && this.stage > 7 && PacketUtils.isPacketValid(event.getPacket())) {
            event.cancel();
            this.packets.add(event.getPacket());
        }
        if (this.mode.is("Watchdog") && event.getPacket() instanceof C03PacketPlayer) {
            event.cancel();
        }
    }
    
    @Override
    public void onBoundingBoxEvent(final BoundingBoxEvent event) {
        if (this.mode.is("AirWalk") || this.mode.is("Verus")) {
            final AxisAlignedBB axisAlignedBB = AxisAlignedBB.fromBounds(-5.0, -1.0, -5.0, 5.0, 1.0, 5.0).offset(event.getBlockPos().getX(), event.getBlockPos().getY(), event.getBlockPos().getZ());
            event.setBoundingBox(axisAlignedBB);
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook && !this.hasS08) {
            final S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook)e.getPacket();
            this.hasS08 = true;
        }
    }
    
    @Override
    public void onEnable() {
        this.hasDamaged = false;
        this.doFly = false;
        this.mospixelTicks = 0;
        this.jumped = false;
        this.ticks = 0;
        this.stage = 0;
        this.stage3 = 0;
        this.clip = 0.0f;
        this.hasClipped = false;
        this.packets.clear();
        this.timer.reset();
        this.moveSpeed = 0.0;
        this.stage2 = 0;
        this.hasS08 = false;
        if (Flight.mc.thePlayer != null) {
            this.lastX = Flight.mc.thePlayer.posX;
            this.lastY = Flight.mc.thePlayer.posY;
            this.lastZ = Flight.mc.thePlayer.posZ;
            this.y = 0.0;
            this.slot = Flight.mc.thePlayer.inventory.currentItem;
            if (this.mode.is("Old NCP")) {
                this.moveSpeed = 1.6;
            }
            else if (this.mode.is("Slime")) {
                final int slimeBlockSlot = InventoryUtils.getBlockSlot(Blocks.slime_block);
                if (slimeBlockSlot == -1) {
                    NotificationManager.post(NotificationType.DISABLE, "Flight", "No slime block found in hotbar!");
                    this.toggleSilent();
                    return;
                }
                Flight.mc.thePlayer.inventory.currentItem = slimeBlockSlot;
            }
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        if (this.mode.is("Vanilla") || this.mode.is("Minemen") || this.mode.is("Old NCP") || this.mode.is("Watchdog")) {
            final EntityPlayerSP thePlayer = Flight.mc.thePlayer;
            final EntityPlayerSP thePlayer2 = Flight.mc.thePlayer;
            final double n = 0.0;
            thePlayer2.motionZ = n;
            thePlayer.motionX = n;
        }
        else if (this.mode.is("Slime")) {
            Flight.mc.thePlayer.inventory.currentItem = this.slot;
        }
        Flight.mc.timer.timerSpeed = 1.0f;
        this.packets.forEach(PacketUtils::sendPacketNoEvent);
        this.packets.clear();
        super.onDisable();
    }
    
    static {
        hiddenBlocks = new HashSet<BlockPos>();
    }
}
