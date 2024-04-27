package me.AquaVit.liquidSense.modules.movement;

import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.*;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.movement.Speed;
import net.ccbluex.liquidbounce.features.module.modules.player.Blink;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.utils.ChatUtil;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TickTimer;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;

@ModuleInfo(name = "Flight1", description = "Kill Watchdog", category = ModuleCategory.MOVEMENT)
public class Flight1 extends Module {
    private final ListValue ModeValue = new ListValue("Mode", new String[] {"Hypixel","Motion"}, "Hypixel");
    private final FloatValue motionSpeedValue = new FloatValue("MotionSpeed", 5F, 0F, 5F);
    private final FloatValue BoostHypixel = new FloatValue("MovementSpeed", 2.5F, 0.3F, 3.0F);
    private final FloatValue timerValue = new FloatValue("Timer",1.2F,0.5F,2F);
    private final IntegerValue timertickValue = new IntegerValue("TimerTick", 10,1,20);
    private final BoolValue uhcdmgValue = new BoolValue("UHC", false);
    private ArrayList<Packet> packets = new ArrayList<>();
    Speed speed = (Speed) LiquidBounce.moduleManager.getModule(Speed.class);
    private int boostStage;
    private double lastDistance;
    private boolean failedStart;
    double moveSpeed, bypass, lastDist;
    private int boostHypixelState = 1;
    private int posYStage;
    private double y;
    private final TickTimer hypixelTimer = new TickTimer();
    private int timerTicks = 0;
    private boolean hypvan;
    HUD hd = (HUD)LiquidBounce.moduleManager.getModule("HUD");
    @Override
    public void onEnable() {
        if(mc.thePlayer == null)
            return;
        switch(ModeValue.get().toLowerCase()) {
            case "hypixel":
                if (mc.thePlayer.fallDistance > 3) {
                    ModeValue.set("Motion");
                    hypvan = true;
                }

                if(!mc.thePlayer.onGround) return;

                if (this.uhcdmgValue.get()) {
                    this.damagePlayerUHC();
                } else {
                    this.damagePlayer();
                }

                final PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                playerCapabilities.isFlying = true;


                mc.thePlayer.jump();
                mc.thePlayer.posY += 0.419999999812688697815;
                this.boostStage = 1;
                this.moveSpeed = 0.1;
                this.lastDistance = 0.0;
                this.failedStart = false;
                posYStage = 0;
                this.y = 0.0;

                boostHypixelState = 1;
                break;
        }


        super.onEnable();
    }

    public void damagePlayerUHC() {
        for (int i = 0; i <= 64; ++i) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.0514865, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.0618865, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-12, mc.thePlayer.posZ, false));
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    public void damagePlayer() {
        for (int i = 0; i <= 48; ++i) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.0514865, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.0618865, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-12, mc.thePlayer.posZ, false));
        }
        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }

    @EventTarget
    public void onUpdate(final UpdateEvent event){
        if(speed.getState() == true){
            if(hd.no.get()) {
                ChatUtil.sendClientMessage("Maybe u need onDisable Speed", Notificationsn.Type.WARNING);
            }
            LiquidBounce.moduleManager.getModule(Speed.class).setState(false);
        }
        switch (ModeValue.get().toLowerCase()) {
            case "motion":
                mc.thePlayer.onGround = false;
                if (mc.thePlayer.movementInput.jump) {
                    mc.thePlayer.motionY = 1.5;
                } else if (mc.thePlayer.movementInput.sneak) {
                    mc.thePlayer.motionY = -1.5;
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null)
            return;

        if(hypvan){
            ModeValue.set("hypixel");
        }

        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionZ = 0;
        mc.thePlayer.capabilities.isFlying = false;

        mc.timer.timerSpeed = 1F;
        mc.thePlayer.speedInAir = 0.02F;
    }

    @EventTarget
    public void onMotion(final MotionEvent event) {
        if(ModeValue.get().equalsIgnoreCase("Hypixel")) {
            switch(event.getEventState()) {
                case PRE:
                    this.hypixelTimer.update();
                    if (this.hypixelTimer.hasTimePassed(2)) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.00001002, mc.thePlayer.posZ);
                        this.hypixelTimer.reset();
                    }
                    if (!this.failedStart) {
                        mc.thePlayer.motionY = 0.0;
                        break;
                    }
                    break;

                case POST:
                    double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    lastDistance = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
            }
            if (this.timerTicks < timertickValue.get())  {
                this.timerTicks++;
                mc.timer.timerSpeed = timerValue.get();
            } else mc.timer.timerSpeed = 1.0F;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {

        final Packet<?> packet = event.getPacket();
        if(packet instanceof C03PacketPlayer) {
            final C03PacketPlayer packetPlayer = (C03PacketPlayer) packet;
            final String mode = ModeValue.get();
            if (mode.equalsIgnoreCase("hypixel"))
                packetPlayer.onGround = false;
        }
    }

    @EventTarget
    public void onMove(final MoveEvent event) {
        switch(ModeValue.get().toLowerCase()) {
            case"motion":
                double speed = motionSpeedValue.get();
                double forward = mc.thePlayer.movementInput.moveForward;
                double strafe = mc.thePlayer.movementInput.moveStrafe;
                float yawm = mc.thePlayer.rotationYaw;
                if ((forward == 0.0D) && (strafe == 0.0D)) {
                    event.setX(0.0D);
                    event.setZ(0.0D);
                } else {
                    if (forward != 0.0D) {
                        if (strafe > 0.0D) {
                            yawm += (forward > 0.0D ? -45 : 45);
                        } else if (strafe < 0.0D) {
                            yawm += (forward > 0.0D ? 45 : -45);
                        }
                        strafe = 0.0D;
                        if (forward > 0.0D) {
                            forward = 1;
                        } else if (forward < 0.0D) {
                            forward = -1;
                        }
                    }
                    event.setX(forward * speed * Math.cos(Math.toRadians(yawm + 90.0F))
                            + strafe * speed * Math.sin(Math.toRadians(yawm + 90.0F)));
                    event.setZ(forward * speed * Math.sin(Math.toRadians(yawm + 90.0F))
                            - strafe * speed * Math.cos(Math.toRadians(yawm + 90.0F)));
                }
                break;
            case "hypixel":
                if (!MovementUtils.isMoving()) {
                    event.setX(0D);
                    event.setZ(0D);
                    break;
                }

                if (failedStart)
                    return;

                final double amplifier = 1 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.2 *
                        (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 0);
                final double baseSpeed = 0.29D * amplifier;

                switch (boostHypixelState) {
                    case 1:
                        moveSpeed = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * baseSpeed;
                        boostHypixelState = 2;
                        break;
                    case 2:
                        moveSpeed *= BoostHypixel.get();
                        boostHypixelState = 3;
                        break;
                    case 3:
                        moveSpeed = lastDistance - (mc.thePlayer.ticksExisted % 3 == 0 ? 0.0103D : 0.0123D) * (lastDistance - baseSpeed);

                        boostHypixelState = 4;
                        break;
                    default:
                        moveSpeed = lastDistance - lastDistance / 159.8D;
                        break;
                }
                moveSpeed = Math.max(moveSpeed, 0.3D);
                final double yaw = MovementUtils.getDirection();
                event.setX(-Math.sin(yaw) * moveSpeed);
                event.setZ(Math.cos(yaw) * moveSpeed);
                mc.thePlayer.motionX = event.getX();
                mc.thePlayer.motionZ = event.getZ();
                break;

        }
    }

    @EventTarget
    public void onBB(final BlockBBEvent event) {
        if (mc.thePlayer == null) return;

        final String mode = ModeValue.get();

        if (event.getBlock() instanceof BlockAir && (mode.equalsIgnoreCase("Hypixel")
                && mc.thePlayer.inventory.getCurrentItem() == null) && event.getY() < mc.thePlayer.posY)
            event.setBoundingBox(AxisAlignedBB.fromBounds(event.getX(), event.getY(), event.getZ(), event.getX() + 1, mc.thePlayer.posY, event.getZ() + 1));
    }

    @EventTarget
    public void onJump(final JumpEvent e) {
        final String mode = ModeValue.get();
        if (mode.equalsIgnoreCase("Hypixel"))
            e.cancelEvent();
    }

    @EventTarget
    public void onStep(final StepEvent e) {
        final String mode = ModeValue.get();
        if (mode.equalsIgnoreCase("Hypixel"))
            e.setStepHeight(0F);
    }

    @Override
    public String getTag() {
        return ModeValue.get();
    }
}
