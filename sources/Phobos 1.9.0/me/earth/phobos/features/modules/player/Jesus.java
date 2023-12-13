package me.earth.phobos.features.modules.player;

import me.earth.phobos.event.events.JesusEvent;
import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.EntityUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Jesus
        extends Module {
    public static AxisAlignedBB offset = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9999, 1.0);
    private static Jesus INSTANCE = new Jesus();
    public Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NORMAL));
    public Setting<Boolean> cancelVehicle = this.register(new Setting<Boolean>("NoVehicle", false));
    public Setting<EventMode> eventMode = this.register(new Setting<Object>("Jump", EventMode.PRE, v -> this.mode.getValue() == Mode.TRAMPOLINE));
    public Setting<Boolean> fall = this.register(new Setting<Object>("NoFall", Boolean.valueOf(false), v -> this.mode.getValue() == Mode.TRAMPOLINE));
    private boolean grounded = false;

    public Jesus() {
        super("Jesus", "Allows you to walk on water", Module.Category.PLAYER, true, false, false);
        INSTANCE = this;
    }

    public static Jesus getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Jesus();
        }
        return INSTANCE;
    }

    @SubscribeEvent
    public void updateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (Jesus.fullNullCheck() || Freecam.getInstance().isOn()) {
            return;
        }
        if (!(event.getStage() != 0 || this.mode.getValue() != Mode.BOUNCE && this.mode.getValue() != Mode.VANILLA && this.mode.getValue() != Mode.NORMAL || Jesus.mc.player.isSneaking() || Jesus.mc.player.noClip || Jesus.mc.gameSettings.keyBindJump.isKeyDown() || !EntityUtil.isInLiquid())) {
            Jesus.mc.player.motionY = 0.1f;
        }
        if (event.getStage() == 0 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.PRE)) {
            this.doTrampoline();
        } else if (event.getStage() == 1 && this.mode.getValue() == Mode.TRAMPOLINE && (this.eventMode.getValue() == EventMode.ALL || this.eventMode.getValue() == EventMode.POST)) {
            this.doTrampoline();
        }
    }

    @SubscribeEvent
    public void sendPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer && Freecam.getInstance().isOff() && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.NORMAL) && Jesus.mc.player.getRidingEntity() == null && !Jesus.mc.gameSettings.keyBindJump.isKeyDown()) {
            CPacketPlayer packet = event.getPacket();
            if (!EntityUtil.isInLiquid() && EntityUtil.isOnLiquid(0.05f) && EntityUtil.checkCollide() && Jesus.mc.player.ticksExisted % 3 == 0) {
                packet.y -= 0.05f;
            }
        }
    }

    @SubscribeEvent
    public void onLiquidCollision(JesusEvent event) {
        if (Jesus.fullNullCheck() || Freecam.getInstance().isOn()) {
            return;
        }
        if (event.getStage() == 0 && (this.mode.getValue() == Mode.BOUNCE || this.mode.getValue() == Mode.VANILLA || this.mode.getValue() == Mode.NORMAL) && Jesus.mc.world != null && Jesus.mc.player != null && EntityUtil.checkCollide() && !(Jesus.mc.player.motionY >= (double) 0.1f) && (double) event.getPos().getY() < Jesus.mc.player.posY - (double) 0.05f) {
            if (Jesus.mc.player.getRidingEntity() != null) {
                event.setBoundingBox(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.95f, 1.0));
            } else {
                event.setBoundingBox(Block.FULL_BLOCK_AABB);
            }
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPacketReceived(PacketEvent.Receive event) {
        if (this.cancelVehicle.getValue().booleanValue() && event.getPacket() instanceof SPacketMoveVehicle) {
            event.setCanceled(true);
        }
    }

    @Override
    public String getDisplayInfo() {
        if (this.mode.getValue() == Mode.NORMAL) {
            return null;
        }
        return this.mode.currentEnumName();
    }

    private void doTrampoline() {
        if (Jesus.mc.player.isSneaking()) {
            return;
        }
        if (EntityUtil.isAboveLiquid(Jesus.mc.player) && !Jesus.mc.player.isSneaking() && !Jesus.mc.gameSettings.keyBindJump.pressed) {
            Jesus.mc.player.motionY = 0.1;
            return;
        }
        if (Jesus.mc.player.onGround || Jesus.mc.player.isOnLadder()) {
            this.grounded = false;
        }
        if (Jesus.mc.player.motionY > 0.0) {
            if (Jesus.mc.player.motionY < 0.03 && this.grounded) {
                Jesus.mc.player.motionY += 0.06713;
            } else if (Jesus.mc.player.motionY <= 0.05 && this.grounded) {
                Jesus.mc.player.motionY *= 1.20000000999;
                Jesus.mc.player.motionY += 0.06;
            } else if (Jesus.mc.player.motionY <= 0.08 && this.grounded) {
                Jesus.mc.player.motionY *= 1.20000003;
                Jesus.mc.player.motionY += 0.055;
            } else if (Jesus.mc.player.motionY <= 0.112 && this.grounded) {
                Jesus.mc.player.motionY += 0.0535;
            } else if (this.grounded) {
                Jesus.mc.player.motionY *= 1.000000000002;
                Jesus.mc.player.motionY += 0.0517;
            }
        }
        if (this.grounded && Jesus.mc.player.motionY < 0.0 && Jesus.mc.player.motionY > -0.3) {
            Jesus.mc.player.motionY += 0.045835;
        }
        if (!this.fall.getValue().booleanValue()) {
            Jesus.mc.player.fallDistance = 0.0f;
        }
        if (!EntityUtil.checkForLiquid(Jesus.mc.player, true)) {
            return;
        }
        if (EntityUtil.checkForLiquid(Jesus.mc.player, true)) {
            Jesus.mc.player.motionY = 0.5;
        }
        this.grounded = true;
    }

    public enum Mode {
        TRAMPOLINE,
        BOUNCE,
        VANILLA,
        NORMAL

    }

    public enum EventMode {
        PRE,
        POST,
        ALL

    }
}

