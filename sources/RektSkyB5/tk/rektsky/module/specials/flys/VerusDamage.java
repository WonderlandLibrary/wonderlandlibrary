/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.EventManager;
import tk.rektsky.event.impl.BlockBBEvent;
import tk.rektsky.event.impl.ClientTickEvent;
import tk.rektsky.event.impl.ClientTickPostEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.UpdateSprintingEvent;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.exploits.SelfDamage;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.specials.flys.VerusFly;
import tk.rektsky.utils.MovementUtil;
import tk.rektsky.utils.display.ColorUtil;

public class VerusDamage {
    private int ticks = 0;
    private final Minecraft mc = Minecraft.getMinecraft();
    public double launchY;
    public static String sign = "AAAAAA_0_AAAAAA_0_AAAAAA_0";
    private boolean damaged = false;
    private final List<Packet> packetBuffer = new ArrayList<Packet>();
    private int undamagedTicks = 0;
    private int packetsModifiedForDamage = 0;
    private final List<C03PacketPlayer> packetsQueue = new ArrayList<C03PacketPlayer>();
    public long startTime = 0L;
    private boolean registered = false;
    private VerusFly verusFly = new VerusFly();

    public void onEnable() {
        this.ticks = 0;
        this.damaged = false;
        this.undamagedTicks = 0;
        this.packetsQueue.clear();
        this.mc.timer.timerSpeed = ModulesManager.getModuleByClass(Fly.class).timer.getValue();
        this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
        this.packetsModifiedForDamage = 0;
        this.registered = false;
        this.modifyPacket();
    }

    public void onDisable() {
        EventManager.EVENT_BUS.unregister(this.verusFly);
        this.verusFly.onDisable();
        this.startTime = System.currentTimeMillis();
        this.mc.timer.timerSpeed = 1.0;
        try {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionY = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private void modifyPacket() {
        SelfDamage.damage();
    }

    public void onEvent(Event event) {
    }

    @Subscribe
    public void onMotionUpdate(WorldTickEvent event) {
        this.mc.thePlayer.setSprinting(true);
        if (this.packetsModifiedForDamage < 1) {
            this.mc.thePlayer.motionY = 0.0;
            ++this.packetsModifiedForDamage;
            return;
        }
        if (this.ticks > 0 || !this.damaged) {
            this.mc.thePlayer.fallDistance = 0.0f;
            if (!(this.mc.thePlayer.hurtTime <= 0 && ModulesManager.getModuleByClass(Fly.class).damage.getValue().booleanValue() || this.damaged)) {
                this.ticks = (int)ModulesManager.getModuleByClass(Fly.class).zoomDuration.getValue().floatValue();
                this.damaged = true;
            }
            --this.ticks;
            if (this.ticks <= 0) {
                if (this.damaged) {
                    return;
                }
                if (this.undamagedTicks > 40) {
                    this.damaged = true;
                    this.launchY = this.mc.thePlayer.posY;
                    Client.notify(new Notification.PopupMessage("Verus Damage Fly", "Your ping is too high or you can't take damage here. Fly Disabled.", ColorUtil.NotificationColors.RED, 30));
                }
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionY = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
                ++this.undamagedTicks;
            } else {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionY = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.timer.timerSpeed = ModulesManager.getModuleByClass(Fly.class).timer.getValue();
                MovementUtil.strafe(ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue());
                if (this.mc.gameSettings.keyBindJump.isKeyDown()) {
                    this.mc.thePlayer.motionY = ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue() / (double)1.4f;
                    MovementUtil.strafe(ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue() / (double)1.4f);
                } else if (this.mc.thePlayer.isSneaking()) {
                    this.mc.thePlayer.motionY = -ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue().doubleValue() / (double)1.4f;
                    MovementUtil.strafe(ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue() / (double)1.4f);
                }
                this.damaged = true;
                this.launchY = this.mc.thePlayer.posY;
            }
        } else if (!this.registered) {
            ModulesManager.getModuleByClass(Fly.class).setToggled(false);
            this.registered = true;
        }
    }

    @Subscribe
    public void onTick(ClientTickEvent event) {
        if (this.mc.thePlayer == null) {
            return;
        }
        if (this.mc.theWorld == null) {
            ModulesManager.getModuleByClass(Fly.class).setToggled(false);
            return;
        }
    }

    @Subscribe
    public void onClientPostTick(ClientTickPostEvent event) {
    }

    @Subscribe
    public void onPacketSend(PacketSentEvent event) {
        if ((this.ticks > 0 || !this.damaged) && event.getPacket() instanceof C03PacketPlayer && this.packetsModifiedForDamage == 1) {
            if (this.mc.thePlayer.fallDistance > 3.0f) {
                ((C03PacketPlayer)event.getPacket()).onGround = true;
                this.mc.thePlayer.fallDistance = 0.0f;
            } else {
                ((C03PacketPlayer)event.getPacket()).onGround = false;
            }
        }
    }

    @Subscribe
    public void onSprint(UpdateSprintingEvent event) {
        event.setSprinting(true);
    }

    @Subscribe
    public void onBB(BlockBBEvent event) {
    }
}

