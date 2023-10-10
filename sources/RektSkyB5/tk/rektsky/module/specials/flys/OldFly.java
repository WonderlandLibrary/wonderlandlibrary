/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.specials.flys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.Client;
import tk.rektsky.event.impl.ClientTickEvent;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.event.impl.WorldTickPostEvent;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.module.impl.exploits.Disabler;
import tk.rektsky.module.impl.movement.Fly;
import tk.rektsky.module.impl.render.Notification;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.DoubleSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.utils.MovementUtil;
import tk.rektsky.utils.Timer;
import tk.rektsky.utils.display.ColorUtil;

public class OldFly {
    static Minecraft mc = Minecraft.getMinecraft();
    public double oldX = 0.0;
    public double oldY = 0.0;
    public double oldZ = 0.0;
    public static long disable = 0L;
    public float oldYaw = 0.0f;
    public int lastFlag = 0;
    public float oldPitch = 0.0f;
    public EntityOtherPlayerMP player;
    Timer flyTimer = new Timer();

    public void onEnable() {
        BooleanSetting oldDisabler = ModulesManager.getModuleByClass(Fly.class).oldDisabler;
        if (oldDisabler.getValue().booleanValue()) {
            Client.notify(new Notification.PopupMessage("Final Dad Exploit", "Enabling Final Dad exploit... Please wait...", ColorUtil.NotificationColors.YELLOW, 40));
        }
        this.flyTimer.reset();
        this.lastFlag = 0;
        this.oldX = OldFly.mc.thePlayer.posX;
        this.oldY = OldFly.mc.thePlayer.posY;
        this.oldZ = OldFly.mc.thePlayer.posZ;
        this.oldYaw = OldFly.mc.thePlayer.rotationYaw;
        this.oldPitch = OldFly.mc.thePlayer.rotationPitch;
        this.player = new EntityOtherPlayerMP(OldFly.mc.theWorld, OldFly.mc.thePlayer.getGameProfile());
        this.player.copyLocationAndAnglesFrom(OldFly.mc.thePlayer);
        this.player.rotationYawHead = OldFly.mc.thePlayer.rotationYawHead;
        this.player.renderYawOffset = OldFly.mc.thePlayer.renderYawOffset;
        OldFly.mc.thePlayer.noClip = true;
    }

    public void onDisable() {
        BooleanSetting oldDisabler = ModulesManager.getModuleByClass(Fly.class).oldDisabler;
        if (oldDisabler.getValue().booleanValue()) {
            OldFly.mc.thePlayer.setPosition(this.oldX, this.oldY, this.oldZ);
            OldFly.mc.thePlayer.rotationYaw = this.oldYaw;
            OldFly.mc.thePlayer.rotationPitch = this.oldPitch;
        }
        OldFly.mc.thePlayer.motionX = 0.0;
        OldFly.mc.thePlayer.motionY = 0.0;
        OldFly.mc.thePlayer.motionZ = 0.0;
        this.player = null;
        OldFly.mc.timer.timerSpeed = 1.0;
        OldFly.mc.thePlayer.capabilities.allowFlying = false;
        OldFly.mc.thePlayer.capabilities.setFlySpeed(0.05f);
        OldFly.mc.thePlayer.capabilities.isFlying = false;
        OldFly.mc.thePlayer.speedInAir = 0.02f;
    }

    @Subscribe
    public void onRecvPacket(PacketReceiveEvent event) {
        BooleanSetting oldDisabler = ModulesManager.getModuleByClass(Fly.class).oldDisabler;
        if (oldDisabler.getValue().booleanValue()) {
            int enabledTicks = ModulesManager.getModuleByClass(Fly.class).enabledTicks;
            if (event.getPacket() instanceof S12PacketEntityVelocity) {
                event.setCanceled(true);
                this.lastFlag = enabledTicks;
            }
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                this.oldX = ((S08PacketPlayerPosLook)event.getPacket()).getX();
                this.oldY = ((S08PacketPlayerPosLook)event.getPacket()).getY();
                this.oldZ = ((S08PacketPlayerPosLook)event.getPacket()).getZ();
                this.oldYaw = ((S08PacketPlayerPosLook)event.getPacket()).yaw;
                this.oldPitch = ((S08PacketPlayerPosLook)event.getPacket()).pitch;
                this.player.posX = this.oldX;
                this.player.posY = this.oldY;
                this.player.posZ = this.oldZ;
                this.player.rotationYaw = this.oldYaw;
                this.player.rotationPitch = this.oldPitch;
                event.setCanceled(true);
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(this.oldX, this.oldY, this.oldZ, this.oldYaw, this.oldPitch, true));
            }
            if (event.getPacket() instanceof S07PacketRespawn) {
                ModulesManager.getModuleByClass(Fly.class).setToggled(false);
            }
        }
    }

    @Subscribe
    public void onSendPacket(PacketSentEvent event) {
        Packet<?> p2;
        BooleanSetting oldDisabler = ModulesManager.getModuleByClass(Fly.class).oldDisabler;
        ListSetting mode = ModulesManager.getModuleByClass(Fly.class).mode;
        if (oldDisabler.getValue().booleanValue() && event.getPacket() instanceof C03PacketPlayer) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof C03PacketPlayer) {
            // empty if block
        }
        if (mode.getValue().equals("Minemora") && ((p2 = event.getPacket()) instanceof C00PacketKeepAlive || p2.getClass().getName().equals(C03PacketPlayer.class.getName()))) {
            event.setCanceled(true);
        }
    }

    @Subscribe
    public void onWorldTickPOST(WorldTickPostEvent event) {
        BooleanSetting oldDisabler = ModulesManager.getModuleByClass(Fly.class).oldDisabler;
        ListSetting mode = ModulesManager.getModuleByClass(Fly.class).mode;
        DoubleSetting flySpeed = ModulesManager.getModuleByClass(Fly.class).flySpeed;
        if (oldDisabler.getValue().booleanValue()) {
            DoubleSetting timer = ModulesManager.getModuleByClass(Fly.class).timer;
            int enabledTicks = ModulesManager.getModuleByClass(Fly.class).enabledTicks;
            if (enabledTicks == 3) {
                Client.notify(new Notification.PopupMessage("Final Dad", "Enabled Final Dad! You can fly freely until you disable fly", ColorUtil.NotificationColors.GREEN, 40));
            }
            if (enabledTicks <= 2) {
                OldFly.mc.timer.timerSpeed = (float)timer.getValue().doubleValue();
                OldFly.mc.thePlayer.motionX = 0.0;
                OldFly.mc.thePlayer.motionY = 0.05;
                OldFly.mc.thePlayer.motionZ = 0.0;
                OldFly.mc.thePlayer.speedOnGround = 0.0f;
                OldFly.mc.thePlayer.speedInAir = 0.0f;
                return;
            }
            OldFly.mc.timer.timerSpeed = (float)timer.getValue().doubleValue();
            if (enabledTicks % 2 != 0) {
                return;
            }
            if (OldFly.mc.thePlayer.posY - this.player.posY > 10.0) {
                if (enabledTicks % 20 == 0) {
                    OldFly.mc.thePlayer.setLocationAndAngles(this.player.posX, this.player.posY, this.player.posZ, OldFly.mc.thePlayer.rotationYaw, OldFly.mc.thePlayer.rotationPitch);
                    Client.notify(new Notification.PopupMessage("Final Dad Exploit", "You got lag backed! Please land on the ground if it keeps happening.", ColorUtil.NotificationColors.RED, 40));
                }
                return;
            }
            if (enabledTicks % 10 == 0 && (enabledTicks - this.lastFlag >= 6 || OldFly.mc.thePlayer.posY - this.player.posY > 4.0) && OldFly.mc.thePlayer.getDistanceToEntity(this.player) >= 12.0f) {
                OldFly.mc.thePlayer.setLocationAndAngles(this.player.posX, this.player.posY, this.player.posZ, OldFly.mc.thePlayer.rotationYaw, OldFly.mc.thePlayer.rotationPitch);
                Client.notify(new Notification.PopupMessage("Final Dad Exploit", "You got lag backed! Please land on the ground if it keeps happening.", ColorUtil.NotificationColors.RED, 40));
            }
            OldFly.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C06PacketPlayerPosLook(OldFly.mc.thePlayer.posX, OldFly.mc.thePlayer.posY, OldFly.mc.thePlayer.posZ, OldFly.mc.thePlayer.rotationYaw, OldFly.mc.thePlayer.rotationPitch, true));
            OldFly.mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(OldFly.mc.thePlayer.posX, OldFly.mc.thePlayer.posY + 50.0, OldFly.mc.thePlayer.posZ, true));
        }
        if (mode.getValue().equals("Motion")) {
            OldFly.mc.thePlayer.fallDistance = 0.0f;
            OldFly.mc.thePlayer.motionY = 0.0;
            double speed = ModulesManager.getModuleByClass(Fly.class).flySpeed.getValue();
            double ySpeed = speed / 2.0;
            MovementUtil.strafe(speed);
            if (ModulesManager.getModuleByClass(Fly.class).vanillaBypass.getValue().booleanValue()) {
                AxisAlignedBB axisalignedbb = OldFly.mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625).addCoord(0.0, -0.55, 0.0);
                if (!OldFly.mc.theWorld.checkBlockCollision(axisalignedbb)) {
                    OldFly.mc.thePlayer.setPosition(OldFly.mc.thePlayer.posX, OldFly.mc.thePlayer.posY - 0.05, OldFly.mc.thePlayer.posZ);
                }
                if (OldFly.mc.thePlayer.floatingTickCount < 70.0 && OldFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    OldFly.mc.thePlayer.motionY += ySpeed;
                }
                if (OldFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    OldFly.mc.thePlayer.motionY -= ySpeed;
                }
            } else {
                if (OldFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                    OldFly.mc.thePlayer.motionY += ySpeed;
                }
                if (OldFly.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    OldFly.mc.thePlayer.motionY -= ySpeed;
                }
            }
            if (!(OldFly.mc.gameSettings.keyBindJump.isKeyDown() || OldFly.mc.gameSettings.keyBindSneak.isKeyDown() || OldFly.mc.gameSettings.keyBindForward.isKeyDown() || OldFly.mc.gameSettings.keyBindLeft.isKeyDown() || OldFly.mc.gameSettings.keyBindRight.isKeyDown() || OldFly.mc.gameSettings.keyBindBack.isKeyDown())) {
                OldFly.mc.thePlayer.motionX = 0.0;
                OldFly.mc.thePlayer.motionY = 0.0;
                OldFly.mc.thePlayer.motionZ = 0.0;
            }
        }
        if (mode.getValue().equals("Stable")) {
            OldFly.mc.thePlayer.fallDistance = 0.0f;
            double speed = ModulesManager.getModuleByClass(Fly.class).stableFlySpeed.getValue();
            if (!ModulesManager.getModuleByClass(Disabler.class).isToggled() || ModulesManager.getModuleByClass(Disabler.class).mode.getValue().equalsIgnoreCase("Verus")) {
                // empty if block
            }
            OldFly.mc.thePlayer.motionX = 0.0;
            OldFly.mc.thePlayer.motionY = 0.0;
            OldFly.mc.thePlayer.motionZ = 0.0;
            if (!MovementUtil.isMoving()) {
                OldFly.mc.thePlayer.motionX = 0.0;
                OldFly.mc.thePlayer.motionZ = 0.0;
            }
            MovementUtil.strafe(speed);
            if (ModulesManager.getModuleByClass(Fly.class).vanillaBypass.getValue().booleanValue()) {
                AxisAlignedBB axisalignedbb = OldFly.mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625).addCoord(0.0, -0.55, 0.0);
                if (!OldFly.mc.theWorld.checkBlockCollision(axisalignedbb)) {
                    OldFly.mc.thePlayer.setPosition(OldFly.mc.thePlayer.posX, OldFly.mc.thePlayer.posY - 0.05, OldFly.mc.thePlayer.posZ);
                }
                if (OldFly.mc.thePlayer.floatingTickCount < 70.0) {
                    if (OldFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                        OldFly.mc.thePlayer.motionY = speed / (double)1.4f;
                        MovementUtil.strafe(speed / (double)1.4f);
                    } else if (OldFly.mc.thePlayer.isSneaking()) {
                        OldFly.mc.thePlayer.motionY = -speed / (double)1.4f;
                        MovementUtil.strafe(speed / (double)1.4f);
                    }
                } else if (OldFly.mc.thePlayer.isSneaking()) {
                    OldFly.mc.thePlayer.motionY = -speed / (double)1.4f;
                    MovementUtil.strafe(speed / (double)1.4f);
                }
            } else if (OldFly.mc.gameSettings.keyBindJump.isKeyDown()) {
                OldFly.mc.thePlayer.motionY = speed / (double)1.4f;
                MovementUtil.strafe(speed / (double)1.4f);
            } else if (OldFly.mc.thePlayer.isSneaking()) {
                OldFly.mc.thePlayer.motionY = -speed / (double)1.4f;
                MovementUtil.strafe(speed / (double)1.4f);
            }
        }
        if (mode.getValue().equals("Vanilla")) {
            OldFly.mc.thePlayer.setSprinting(true);
            OldFly.mc.thePlayer.noClip = true;
            OldFly.mc.thePlayer.capabilities.allowFlying = true;
            OldFly.mc.thePlayer.capabilities.setFlySpeed(0.05f);
            OldFly.mc.thePlayer.capabilities.isFlying = true;
            OldFly.mc.thePlayer.onGround = false;
        }
    }

    @Subscribe
    public void onClientTick(ClientTickEvent event) {
        if (OldFly.mc.theWorld == null || !mc.isInGame()) {
            ModulesManager.getModuleByClass(Fly.class).setToggled(false);
        }
    }
}

