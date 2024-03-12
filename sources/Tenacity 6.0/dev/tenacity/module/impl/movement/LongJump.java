// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.movement;

import dev.tenacity.module.settings.Setting;
import java.util.ArrayList;
import dev.tenacity.module.Category;
import java.util.function.Consumer;
import dev.tenacity.utils.misc.MathUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import dev.tenacity.event.impl.player.MoveEvent;
import dev.tenacity.event.impl.network.PacketSendEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.player.MotionEvent;
import net.minecraft.network.Packet;
import java.util.List;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class LongJump extends Module
{
    private final ModeSetting mode;
    private final ModeSetting watchdogMode;
    private final NumberSetting damageSpeed;
    private final BooleanSetting spoofY;
    private int movementTicks;
    private double speed;
    private float pitch;
    private int prevSlot;
    private int ticks;
    private boolean damagedBow;
    private final TimerUtil jumpTimer;
    private boolean damaged;
    private double x;
    private double y;
    private double z;
    private final List<Packet> packets;
    private int stage;
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        this.setSuffix(this.mode.getMode());
        if (this.spoofY.isEnabled()) {
            LongJump.mc.thePlayer.posY = this.y;
        }
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Vanilla": {
                if (MovementUtils.isMoving() && LongJump.mc.thePlayer.onGround) {
                    MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 2.0);
                    LongJump.mc.thePlayer.jump();
                    break;
                }
                break;
            }
            case "Watchdog": {
                if (event.isPre()) {
                    final String mode2 = this.watchdogMode.getMode();
                    switch (mode2) {
                        case "Damage": {
                            if (LongJump.mc.thePlayer.onGround) {
                                ++this.stage;
                                if (this.stage <= 3) {
                                    LongJump.mc.thePlayer.jump();
                                }
                                if (this.stage > 5 && this.damaged) {
                                    this.toggle();
                                }
                            }
                            if (this.stage <= 3) {
                                event.setOnGround(false);
                                LongJump.mc.thePlayer.posY = this.y;
                                LongJump.mc.timer.timerSpeed = this.damageSpeed.getValue().floatValue();
                                this.speed = 1.2;
                            }
                            if (LongJump.mc.thePlayer.hurtTime > 0) {
                                this.damaged = true;
                                ++this.ticks;
                                if (this.ticks < 2) {
                                    LongJump.mc.thePlayer.motionY = 0.41999998688698;
                                }
                                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * this.speed);
                                this.speed -= 0.01;
                                LongJump.mc.timer.timerSpeed = 1.0f;
                            }
                            if (this.damaged) {
                                final EntityPlayerSP thePlayer = LongJump.mc.thePlayer;
                                thePlayer.motionY += 0.0049;
                                break;
                            }
                            break;
                        }
                        case "Damageless": {
                            ++this.stage;
                            if (this.stage == 1 && LongJump.mc.thePlayer.onGround) {
                                LongJump.mc.thePlayer.motionY = 0.42;
                                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.2);
                                this.speed = 1.4500000476837158;
                            }
                            if (this.stage > 1) {
                                MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * this.speed);
                                this.speed -= 0.015;
                            }
                            if (LongJump.mc.thePlayer.onGround && this.stage > 1) {
                                this.toggle();
                                break;
                            }
                            break;
                        }
                    }
                    break;
                }
                break;
            }
            case "NCP": {
                if (MovementUtils.isMoving()) {
                    if (MovementUtils.isOnGround(2.3E-4)) {
                        LongJump.mc.thePlayer.motionY = 0.41;
                    }
                    switch (this.movementTicks) {
                        case 1: {
                            this.speed = MovementUtils.getBaseMoveSpeed();
                            break;
                        }
                        case 2: {
                            this.speed = MovementUtils.getBaseMoveSpeed() + 0.132535 * Math.random();
                            break;
                        }
                        case 3: {
                            this.speed = MovementUtils.getBaseMoveSpeed() / 2.0;
                            break;
                        }
                    }
                    MovementUtils.setSpeed(Math.max(this.speed, MovementUtils.getBaseMoveSpeed()));
                    ++this.movementTicks;
                    break;
                }
                break;
            }
            case "AGC": {
                final int bow = this.getBowSlot();
                if (this.damagedBow) {
                    if (LongJump.mc.thePlayer.onGround && this.jumpTimer.hasTimeElapsed(1000L)) {
                        this.toggle();
                    }
                    if (LongJump.mc.thePlayer.onGround && LongJump.mc.thePlayer.motionY > 0.003) {
                        LongJump.mc.thePlayer.motionY = 0.574999988079071;
                    }
                    else {
                        MovementUtils.setSpeed(MovementUtils.getBaseMoveSpeed() * 1.8);
                    }
                }
                if (this.damagedBow) {
                    break;
                }
                switch (this.ticks) {
                    case 0: {
                        if (this.prevSlot != bow) {
                            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(bow));
                        }
                        PacketUtils.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(LongJump.mc.thePlayer.inventory.getStackInSlot(bow)));
                        break;
                    }
                    case 3: {
                        event.setPitch(-89.93f);
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), this.pitch, event.isOnGround()));
                        PacketUtils.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));
                        if (this.prevSlot != bow) {
                            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(this.prevSlot));
                            break;
                        }
                        break;
                    }
                }
                if (LongJump.mc.thePlayer.hurtTime != 0) {
                    this.damagedBow = true;
                    break;
                }
                break;
            }
        }
        if (!this.mode.is("Watchdog")) {
            ++this.ticks;
        }
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
    }
    
    @Override
    public void onMoveEvent(final MoveEvent event) {
        if (!this.damagedBow && this.mode.is("AGC")) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        if (!this.damaged && this.mode.is("Watchdog") && this.watchdogMode.is("Damage")) {
            event.setSpeed(0.0);
        }
    }
    
    public int getBowSlot() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack is = LongJump.mc.thePlayer.inventory.getStackInSlot(i);
            if (is != null && is.getItem() == Items.bow) {
                return i;
            }
        }
        return -1;
    }
    
    public int getItemCount(final Item item) {
        int count = 0;
        for (int i = 9; i < 45; ++i) {
            final ItemStack stack = LongJump.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null && stack.getItem() == item) {
                count += stack.stackSize;
            }
        }
        return count;
    }
    
    @Override
    public void onEnable() {
        if (this.mode.is("AGC")) {
            this.prevSlot = LongJump.mc.thePlayer.inventory.currentItem;
            this.pitch = MathUtils.getRandomFloat(-89.2f, -89.99f);
            if (this.getBowSlot() == -1) {
                this.toggleSilent();
                return;
            }
            if (this.getItemCount(Items.arrow) == 0) {
                this.toggleSilent();
                return;
            }
        }
        this.ticks = 0;
        this.damagedBow = false;
        this.damaged = false;
        this.jumpTimer.reset();
        this.x = LongJump.mc.thePlayer.posX;
        this.y = LongJump.mc.thePlayer.posY;
        this.z = LongJump.mc.thePlayer.posZ;
        this.packets.clear();
        this.stage = 0;
        this.speed = 1.399999976158142;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        LongJump.mc.timer.timerSpeed = 1.0f;
        this.packets.forEach(PacketUtils::sendPacketNoEvent);
        this.packets.clear();
        super.onDisable();
    }
    
    public LongJump() {
        super("LongJump", "Long Jump", Category.MOVEMENT, "jump further");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Watchdog", "NCP", "AGC" });
        this.watchdogMode = new ModeSetting("Watchdog Mode", "Damage", new String[] { "Damage", "Damageless" });
        this.damageSpeed = new NumberSetting("Damage Speed", 1.0, 20.0, 1.0, 0.5);
        this.spoofY = new BooleanSetting("Spoof Y", false);
        this.movementTicks = 0;
        this.ticks = 0;
        this.jumpTimer = new TimerUtil();
        this.packets = new ArrayList<Packet>();
        this.watchdogMode.addParent(this.mode, m -> m.is("Watchdog"));
        this.damageSpeed.addParent(this.mode, m -> m.is("Watchdog") && this.watchdogMode.is("Damage"));
        this.addSettings(this.mode, this.watchdogMode, this.damageSpeed, this.spoofY);
    }
}
