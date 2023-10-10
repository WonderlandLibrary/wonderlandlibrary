/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.module.specials.speed.VanillaBhop;
import tk.rektsky.module.specials.speed.VerusBHop;
import tk.rektsky.module.specials.speed.VerusLowHop;
import tk.rektsky.utils.MovementUtil;

public class Speed
extends Module {
    public ListSetting mode = new ListSetting("Mode", new String[]{"VerusAirBruher", "Verus LowHop", "Vanilla", "Vanilla OnGround", "Verus Flatten", "Verus OnGround"}, "VerusAirBruher");
    public BooleanSetting boost = new BooleanSetting("DmgBoost", true);
    public DoubleSetting vanillaSpeed = new DoubleSetting("Vanilla Speed", 0.33, 9.7, 1.0);

    public Speed() {
        super("Speed", "Walks Faster", 0, Category.MOVEMENT);
        this.mode.setOnChange((oldValue, newValue) -> {
            if (this.isToggled()) {
                if (oldValue.equalsIgnoreCase("VerusAirBruher")) {
                    VerusBHop.onDisable(this);
                }
                if (newValue.equalsIgnoreCase("VerusAirBruher")) {
                    VerusBHop.onEnable(this);
                }
            }
        });
    }

    @Override
    public void onEnable() {
        if (this.mode.getValue().equalsIgnoreCase("VerusAirBruher")) {
            VerusBHop.onEnable(this);
        }
        if (this.mode.getValue().equalsIgnoreCase("Verus LowHop")) {
            VerusLowHop.onEnable();
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
            VanillaBhop.onEnable(this);
        }
        if (this.mode.getValue().equalsIgnoreCase("Verus Flatten")) {
            this.mc.thePlayer.jump();
            if (!this.mc.thePlayer.isCollidedHorizontally && !this.mc.gameSettings.keyBindJump.pressed) {
                this.mc.thePlayer.motionY = 0.0;
                this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688698, this.mc.thePlayer.posZ);
                this.mc.thePlayer.onGround = false;
                MovementUtil.strafe(0.35);
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        double landedTicks;
        if (this.mode.getValue().equalsIgnoreCase("VerusAirBruher")) {
            VerusBHop.onEvent(this, event);
        }
        if (this.mode.getValue().equalsIgnoreCase("Verus LowHop")) {
            VerusLowHop.onEvent(event);
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
            VanillaBhop.onEvent(this, event);
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla OnGround")) {
            MovementUtil.strafe(1.5);
        }
        if (event instanceof UpdateSprintingEvent && this.mode.getValue().equalsIgnoreCase("Verus Flatten")) {
            ((UpdateSprintingEvent)event).setSprinting(true);
        }
        if (event instanceof PacketSentEvent && this.mode.getValue().equalsIgnoreCase("Verus Flatten") && ((PacketSentEvent)event).getPacket() instanceof C03PacketPlayer && (landedTicks = (double)(this.mc.thePlayer.lastGroundTick - this.mc.thePlayer.lastAirTick)) >= 10.0) {
            ((PacketSentEvent)event).setCanceled(true);
        }
        if (event instanceof PacketSentEvent && this.mode.getValue().equalsIgnoreCase("Verus OnGround") && ((PacketSentEvent)event).getPacket() instanceof C0BPacketEntityAction && ((C0BPacketEntityAction)((PacketSentEvent)event).getPacket()).getAction() == C0BPacketEntityAction.Action.STOP_SPRINTING) {
            ((PacketSentEvent)event).setCanceled(true);
        }
        if (event instanceof WorldTickEvent) {
            if (this.mode.getValue().equalsIgnoreCase("Verus Flatten")) {
                landedTicks = this.mc.thePlayer.lastGroundTick - this.mc.thePlayer.lastAirTick;
                if ((landedTicks += 1.0) <= 9.0 && landedTicks > 0.0) {
                    this.mc.thePlayer.setSpeed(Math.min((float)(landedTicks * (double)0.15f), 1.0f));
                } else if (landedTicks <= 10.0) {
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                }
                if (landedTicks == 11.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.41999998688698, this.mc.thePlayer.posZ, false));
                }
                if (landedTicks == 12.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.3415999853611, this.mc.thePlayer.posZ, false));
                }
                if (landedTicks == 13.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1863679808445, this.mc.thePlayer.posZ, false));
                }
                if (landedTicks == 14.0) {
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                    this.mc.thePlayer.lastGroundTick = this.mc.thePlayer.ticksExisted;
                    this.mc.thePlayer.lastAirTick = this.mc.thePlayer.ticksExisted;
                }
            }
            if (this.mode.getValue().equalsIgnoreCase("Verus OnGround")) {
                if (this.mc.thePlayer.onGround) {
                    MovementUtil.strafe(0.48);
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + 0.1, this.mc.thePlayer.posY + 0.0030162615090425504, this.mc.thePlayer.posZ + 0.1, false));
                    this.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                } else {
                    MovementUtil.strafe(0.3);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.mode.getValue().equalsIgnoreCase("VerusAirBruher")) {
            VerusBHop.onDisable(this);
        }
        if (this.mode.getValue().equalsIgnoreCase("Verus LowHop")) {
            VerusLowHop.onDisable();
        }
        if (this.mode.getValue().equalsIgnoreCase("Vanilla")) {
            VanillaBhop.onDisable(this);
        }
    }

    @Override
    public String getSuffix() {
        return this.mode.getValue();
    }

    @Override
    public void unregisterListeners() {
        VerusBHop.onDisable(this);
        VerusLowHop.onDisable();
        VanillaBhop.onDisable(this);
    }
}

