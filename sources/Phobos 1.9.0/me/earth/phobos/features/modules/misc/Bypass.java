package me.earth.phobos.features.modules.misc;

import me.earth.phobos.event.events.PacketEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketCloseWindow;
import net.minecraft.network.play.server.SPacketEntityMetadata;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Bypass
        extends Module {
    private static Bypass instance;
    private final Timer timer = new Timer();
    public Setting<Boolean> illegals = this.register(new Setting<Boolean>("Illegals", false));
    public Setting<Boolean> secretClose = this.register(new Setting<Object>("SecretClose", Boolean.valueOf(false), v -> this.illegals.getValue()));
    public Setting<Boolean> rotation = this.register(new Setting<Object>("Rotation", Boolean.valueOf(false), v -> this.secretClose.getValue() != false && this.illegals.getValue() != false));
    public Setting<Boolean> elytra = this.register(new Setting<Boolean>("Elytra", false));
    public Setting<Boolean> reopen = this.register(new Setting<Object>("Reopen", Boolean.valueOf(false), v -> this.elytra.getValue()));
    public Setting<Integer> reopen_interval = this.register(new Setting<Object>("ReopenDelay", Integer.valueOf(1000), Integer.valueOf(0), Integer.valueOf(5000), v -> this.elytra.getValue()));
    public Setting<Integer> delay = this.register(new Setting<Object>("Delay", Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(1000), v -> this.elytra.getValue()));
    public Setting<Boolean> allow_ghost = this.register(new Setting<Object>("Ghost", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> cancel_close = this.register(new Setting<Object>("Cancel", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> discreet = this.register(new Setting<Object>("Secret", Boolean.valueOf(true), v -> this.elytra.getValue()));
    public Setting<Boolean> packets = this.register(new Setting<Boolean>("Packets", false));
    public Setting<Boolean> limitSwing = this.register(new Setting<Object>("LimitSwing", Boolean.valueOf(false), v -> this.packets.getValue()));
    public Setting<Integer> swingPackets = this.register(new Setting<Object>("SwingPackets", Integer.valueOf(1), Integer.valueOf(0), Integer.valueOf(100), v -> this.packets.getValue()));
    public Setting<Boolean> noLimit = this.register(new Setting<Object>("NoCompression", Boolean.valueOf(false), v -> this.packets.getValue()));
    int cooldown = 0;
    private float yaw;
    private float pitch;
    private boolean rotate;
    private BlockPos pos;
    private final Timer swingTimer = new Timer();
    private int swingPacket = 0;

    public Bypass() {
        super("Bypass", "Bypass for stuff", Module.Category.MISC, true, false, false);
        instance = this;
    }

    public static Bypass getInstance() {
        if (instance == null) {
            instance = new Bypass();
        }
        return instance;
    }

    @Override
    public void onToggle() {
        this.swingPacket = 0;
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() == null && this.secretClose.getValue().booleanValue() && this.rotation.getValue().booleanValue()) {
            this.pos = new BlockPos(Bypass.mc.player.getPositionVector());
            this.yaw = Bypass.mc.player.rotationYaw;
            this.pitch = Bypass.mc.player.rotationPitch;
            this.rotate = true;
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (this.illegals.getValue().booleanValue() && this.secretClose.getValue().booleanValue()) {
            if (event.getPacket() instanceof CPacketCloseWindow) {
                event.setCanceled(true);
            } else if (event.getPacket() instanceof CPacketPlayer && this.rotation.getValue().booleanValue() && this.rotate) {
                CPacketPlayer packet = event.getPacket();
                packet.yaw = this.yaw;
                packet.pitch = this.pitch;
            }
        }
        if (this.packets.getValue().booleanValue() && this.limitSwing.getValue().booleanValue() && event.getPacket() instanceof CPacketAnimation) {
            if (this.swingPacket > this.swingPackets.getValue()) {
                event.setCanceled(true);
            }
            ++this.swingPacket;
        }
    }

    @SubscribeEvent
    public void onIncomingPacket(PacketEvent.Receive event) {
        if (!Bypass.fullNullCheck() && this.elytra.getValue().booleanValue()) {
            SPacketEntityMetadata MetadataPacket;
            if (event.getPacket() instanceof SPacketSetSlot) {
                SPacketSetSlot packet = event.getPacket();
                if (packet.getSlot() == 6) {
                    event.setCanceled(true);
                }
                if (!this.allow_ghost.getValue().booleanValue() && packet.getStack().getItem().equals(Items.ELYTRA)) {
                    event.setCanceled(true);
                }
            }
            if (this.cancel_close.getValue().booleanValue() && Bypass.mc.player.isElytraFlying() && event.getPacket() instanceof SPacketEntityMetadata && (MetadataPacket = event.getPacket()).getEntityId() == Bypass.mc.player.getEntityId()) {
                event.setCanceled(true);
            }
        }
        if (event.getPacket() instanceof SPacketCloseWindow) {
            this.rotate = false;
        }
    }

    @Override
    public void onTick() {
        if (this.secretClose.getValue().booleanValue() && this.rotation.getValue().booleanValue() && this.rotate && this.pos != null && Bypass.mc.player != null && Bypass.mc.player.getDistanceSq(this.pos) > 400.0) {
            this.rotate = false;
        }
        if (this.elytra.getValue().booleanValue()) {
            if (this.cooldown > 0) {
                --this.cooldown;
            } else if (!(Bypass.mc.player == null || Bypass.mc.currentScreen instanceof GuiInventory || Bypass.mc.player.onGround && this.discreet.getValue().booleanValue())) {
                for (int i = 0; i < 36; ++i) {
                    ItemStack item = Bypass.mc.player.inventory.getStackInSlot(i);
                    if (!item.getItem().equals(Items.ELYTRA)) continue;
                    Bypass.mc.playerController.windowClick(0, i < 9 ? i + 36 : i, 0, ClickType.QUICK_MOVE, Bypass.mc.player);
                    this.cooldown = this.delay.getValue();
                    return;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        this.swingPacket = 0;
        if (this.elytra.getValue().booleanValue() && this.timer.passedMs(this.reopen_interval.getValue().intValue()) && this.reopen.getValue().booleanValue() && !Bypass.mc.player.isElytraFlying() && Bypass.mc.player.fallDistance > 0.0f) {
            Bypass.mc.player.connection.sendPacket(new CPacketEntityAction(Bypass.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }
}

