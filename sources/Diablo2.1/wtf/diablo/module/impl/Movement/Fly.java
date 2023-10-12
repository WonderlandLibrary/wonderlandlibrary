package wtf.diablo.module.impl.Movement;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import wtf.diablo.events.EventType;
import wtf.diablo.events.impl.CollideEvent;
import wtf.diablo.events.impl.MoveEvent;
import wtf.diablo.events.impl.PacketEvent;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.ModeSetting;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.math.Stopwatch;
import wtf.diablo.utils.packet.PacketUtil;
import wtf.diablo.utils.player.MoveUtil;
import wtf.diablo.utils.world.EntityUtil;

@Setter
@Getter
public class Fly extends Module {
    public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Watchdog", "Verus", "Vulcan", "Ghostly", "MMC", "MMC2", "Invaded");
    public NumberSetting speed = new NumberSetting("Speed", 0.6, 0.05, 0.2, 7);
    public NumberSetting timer = new NumberSetting("Timer", 1, 0.05, 1, 2);
    Stopwatch sw = new Stopwatch();
    int state, state2;
    int mmcstate;

    public Fly() {
        super("Fly", "Allows flight", Category.MOVEMENT, ServerType.All);
        speed.setParent(mode, "Vanilla", "Verus", "Ghostly","Invaded");
        timer.setParent(mode, "Watchdog", "WatchdogInfinite");
        addSettings(mode, speed, timer);
    }

    double vDist = 0;
    int stage = 0;
    boolean funny;
    double startY = 0;


    @Subscribe
    public void onUpdate(UpdateEvent e) {

        this.setSuffix(mode.getMode());
        switch (mode.getMode()) {
            case "Vanilla":
                MoveUtil.setMotion(speed.getValue());
                if (mc.thePlayer.movementInput.jump) {
                    mc.thePlayer.motionY = 0.6f;
                } else if (mc.thePlayer.movementInput.sneak) {
                    mc.thePlayer.motionY = -0.6f;
                } else {
                    mc.thePlayer.motionY = 0;
                }
                break;

            case "MMC2":

                if (mc.thePlayer.hurtTime <= 4 && mc.thePlayer.hurtTime != 0) {
                    mmcstate = 1;
                }
                //System.out.println(mc.thePlayer.hurtTime);
                if (mmcstate == 1) {
                    MoveUtil.setMotion(speed.getValue() * 2);
                    if (mc.thePlayer.movementInput.jump) {
                        mc.thePlayer.motionY = 0.6f;
                    } else if (mc.thePlayer.movementInput.sneak) {
                        mc.thePlayer.motionY = -0.6f;
                    } else {
                        mc.thePlayer.motionY = 0;
                    }
                }

                break;

            case "WatchdogNew":
                MoveUtil.setMotion(0.10);
                final double C20PacketAntiJudaism = 0.025000000000000012;
                mc.thePlayer.motionY = 0;
                if (!funny) {
                    if (mc.thePlayer.posY > startY + 0.3)
                        funny = true;
                } else {
                    if (mc.thePlayer.posY < startY + 0.1)
                        funny = false;
                }
                mc.timer.timerSpeed = 0.5f;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + (funny ? -C20PacketAntiJudaism : C20PacketAntiJudaism), mc.thePlayer.posZ);
                System.out.println(mc.thePlayer.posY + C20PacketAntiJudaism);
                break;

            case "Verus":

                MoveUtil.setMotion(speed.getValue());
                /*
                if (mc.thePlayer.hurtTime > 0 && stage == 0) {
                    sw.reset();
                    state = 1;
                }
                if (mc.thePlayer.lastPosY < startY + 0.1) {
                    mc.thePlayer.jump();
                    MoveUtil.setMotion(0.28f);
                } else {
                    if (mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.motionY < 0.1) {
                        mc.thePlayer.motionY = -0.098;
                    }
                }
                mc.thePlayer.renderOffsetY = 0;
                ///mc.thePlayer.posY = startY + 0.1;
                mc.timer.timerSpeed = 1.0f;
                if (state == 1 && !sw.hasReached(200)) {
                    MoveUtil.setMotion(speed.getValue());
                }

                 */
                break;
            case "Ghostly":
                MoveUtil.setMotion(speed.getValue());
                break;

            case "Watchdog":
            case "WatchdogInfinite":
                if (!funny) {
                    /*
                    EntityUtil.setRotations(e, mc.thePlayer.rotationYaw, 90);

                    if (mc.thePlayer.posY < mc.thePlayer.lastTickPosY) {
                        if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.2, mc.thePlayer.posZ), EnumFacing.UP, Entity.getVectorForRotation(90, mc.thePlayer.rotationYaw))) {
                            mc.thePlayer.swingItem();
                        }
                    }

                    if (mc.thePlayer.onGround) {
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.05, mc.thePlayer.posZ, false));
                        PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.12, mc.thePlayer.posZ, false));

                        this.funny = true;
                    }

                     */
                } else {
                    if (e.getType() == EventType.Pre) {
                        mc.timer.timerSpeed = timer.getFloatValue();
                        mc.thePlayer.cameraYaw = (float) .1;
                        mc.thePlayer.motionY = 0;
                    }
                }
                break;

            case "Vulcan":
                if (mc.thePlayer.lastPosY < startY + 0.1) {
                    mc.thePlayer.jump();
                    MoveUtil.setMotion(0.28f);
                } else {
                    if (mc.thePlayer.ticksExisted % 2 == 0 && mc.thePlayer.motionY < 0.1) {
                        mc.thePlayer.motionY = -0.098;
                    }
                }
                mc.thePlayer.prevRenderArmPitch = 0;
                mc.thePlayer.posY = startY + 0.1;
                mc.timer.timerSpeed = 1.0f;
                break;
            case "MMC":
                break;
            case "Invaded":
                if(funny){
                    if(mc.thePlayer.hurtTime != 0) {
                        MoveUtil.strafe(speed.getValue());
                    }
                }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent e) {
        //Forces nofall on verus fly

        switch (mode.getMode()) {
            case "Watchdog":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    this.funny = true;
                }
            case "Invaded":
                if (e.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) e.getPacket();
                    if (packet.entityID == mc.thePlayer.getEntityId()) {
                        this.funny = true;
                        //packet.motionX = (int) (packet.getMotionX() * 4);
                        ///mc.thePlayer.motionY = packet.getMotionY() * 2;
                        //packet.motionZ = (int) (packet.getMotionZ() * 4);
                    }
                }
                break;
        }
    }

    @Subscribe
    public void onMoveEvent(MoveEvent e) {
        switch (mode.getMode()) {
            case "MMC":
                if (mc.thePlayer.hurtTime != 0) {
                    e.setMoveSpeed(0.53727623540770172);
                } else {
                    this.toggle();
                }
                break;

            case "Watchdog":
                if (funny) {
                    e.setMoveSpeed(0.2027623540770172);
                } else {
                    e.setX(0);
                    e.setZ(0);
                }
                break;
            case "WatchdogInfinite":
                if (funny) {
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        e.setMoveSpeed(0.4027623540770172);
                    }
                } else {
                    e.setX(0);
                    e.setZ(0);
                }
                break;
            case "Invaded":
                if (funny) {
                    //e.setMoveSpeed(0.4027623540770172);
                    e.setY(-0.01);
                } else {
                    e.setX(0);
                    e.setZ(0);
                }
                break;
        }
    }

    @Subscribe
    public void onCollide(CollideEvent e) {
        final double x = e.getX();
        final double y = e.getY();
        final double z = e.getZ();

        switch (mode.getMode()) {
            case "WatchdogInfinite":
            case "Ghostly":
            case "Verus":
            case "MMC":
                e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y + 0.05, z));
                break;
            case "Watchdog":
            case "Invaded":
                if (funny) {
                    e.setBoundingBox(AxisAlignedBB.fromBounds(15.0, 1.0, 15.0, -15.0, -1.0, -15.0).offset(x, y + 0.05, z));
                }
                break;
        }
    }

    @Override
    public void onEnable() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY;
        double z = mc.thePlayer.posZ;

        startY = mc.thePlayer.posY;
        mmcstate = 0;

        this.funny = false;

        switch (mode.getMode()) {
            case "Verus":
                EntityUtil.damageVerus();
                break;
            case "Ghostly":
            case "MMC":
                if (mc.thePlayer.onGround)
                    EntityUtil.damageVerus();
                break;
            case "MMC2":
                PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 2.5, mc.thePlayer.posZ, false));
                break;
            case "Watchdog":
                /*
                for (int i = 0; i < 49; i++) {
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.0625D, z, false));
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));

                }
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));

                 */
                this.funny = true;
                break;
            case "Invaded":
                /*
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y - 0.125D, z, false));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.325D, z, false));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.325D, z, false));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.325D, z, false));

                 */
                break;
                /*
            case "Watchdog":
            case "WatchdogInfinite":
                int slot = -1;
                int blockCount = 0;
                for (int i = 0; i < 9; ++i) {
                    ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (itemStack != null) {
                        final int stackSize = itemStack.stackSize;
                        if (stackSize > blockCount) {
                            blockCount = stackSize;
                            slot = i;
                        }
                    }
                }

                mc.thePlayer.inventory.currentItem = slot;

                mc.thePlayer.motionY = 0.42F;
                vDist = MathUtil.round(mc.thePlayer.posY, 1);
                funny = false;
                break;

                 */
            case "Vulcan":
                mc.timer.timerSpeed = 0.2f;
                //EntityUtil.damageVerus();
                break;
        }


        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
        MoveUtil.setMotion(0);
        mmcstate = 0;
        super.onDisable();
    }

}
