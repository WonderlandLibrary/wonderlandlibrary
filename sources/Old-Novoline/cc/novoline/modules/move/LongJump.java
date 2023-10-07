package cc.novoline.modules.move;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.*;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import cc.novoline.utils.notifications.NotificationType;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LongJump extends AbstractModule {

/*    @Property("speed")
    private DoubleProperty speed = PropertyFactory.createDouble(5.0).minimum(2.0).maximum(6.0);
    @Property("jump-boost")
    private IntProperty jump_boost = PropertyFactory.createInt(2).minimum(0).maximum(4);*/

    public LongJump(@NotNull ModuleManager novoline) {
        super(novoline, EnumModuleType.MOVEMENT, "LongJump", "Long Jump");
/*        Manager.put(new Setting("LJ_SPEED", "Speed", SettingType.SLIDER, this, speed, 0.2));
        Manager.put(new Setting("LJ_JUMP_BOOST", "Jump Boost", SettingType.SLIDER, this, jump_boost, 1));*/
    }

    private int tick, stage;
    private boolean shouldBoost, wait;
    private double lastDist, baseSpeed, moveSpeed;
    private List<Packet> packetList = new CopyOnWriteArrayList();

    private int bowSlot() {
        return mc.player.getSlotByItem(Items.bow);
    }

    @Override
    public void onEnable() {
        if (bowSlot() == -1 || !mc.player.inventory.hasItem(Items.arrow)) {
            novoline.getNotificationManager().pop(getDisplayName(), "You need bow in your hotbar and arrows", NotificationType.WARNING);
            toggle();
            return;
        }

        wait = true;
        setSuffix("Hypixel");
        checkModule(Speed.class, Scaffold.class, Flight.class);
    }

    @Override
    public void onDisable() {
        tick = 0;
        mc.timer.timerSpeed = 1;
    }

    @EventTarget
    public void onTick(TickUpdateEvent event) {
        setSuffix("Hypixel");
        tick++;
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        lastDist = mc.player.getLastTickDistance();
        baseSpeed = mc.player.getBaseMoveSpeed(0.16);

        if (mc.player.hurtResistantTime == 19) {
            wait = false;
            stage = 1;
        }
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            checkModule(getClass());
        }

        if (wait) {
            if (event.getPacket() instanceof C03PacketPlayer || event.getPacket() instanceof C08PacketPlayerBlockPlacement
                    || event.getPacket() instanceof C07PacketPlayerDigging || event.getPacket() instanceof C09PacketHeldItemChange) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onMotion(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (tick == 1) {
                sendPacketNoEvent(new C03PacketPlayer());
                sendPacketNoEvent(new C09PacketHeldItemChange(bowSlot()));
                sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.player.inventory.getStackInSlot(bowSlot())));
            } else if (tick == 2) {
                sendPacketNoEvent(new C03PacketPlayer());
            } else if (tick == 3) {
                sendPacketNoEvent(new C03PacketPlayer());
            } else if (tick == 4) {
                sendPacketNoEvent(new C06PacketPlayerPosLook(event.getX(), event.getY(), event.getZ(), event.getYaw(), -90, event.isOnGround()));
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                sendPacketNoEvent(new C09PacketHeldItemChange(mc.player.inventory.currentItem));
            }
        }

        if (tick > 35) {
            if (mc.player.onGround) {
                mc.timer.timerSpeed = 1;
                checkModule(getClass());
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        double baseAmplifier = mc.player.isPotionActive(Potion.moveSpeed) ? mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1 ? 1.86 : 1.34 : 1.34;
        double jumpAmplifier = mc.player.isPotionActive(Potion.moveSpeed) ? mc.player.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1 ? 1.69 : 2.35 : 2.2;
        if (wait) {
            event.setMoveSpeed(0);
        } else if (mc.player.isMoving()) {
            if (mc.player.onGround) {
                if (shouldBoost) {
                    event.setY(mc.player.motionY = mc.player.getBaseMotionY() + 0.1 * (4 - (mc.player.isPotionActive(Potion.jump) ?
                            mc.player.getActivePotionEffect(Potion.jump).getAmplifier() + 1 : 0)));
                    moveSpeed *= 0.9;
                } else {
                    moveSpeed = mc.player.getBySprinting() * 2;
                }
            } else {
                print(mc.player.motionY);
                if(mc.player.motionY == 0.03196443960654492){
                    mc.player.motionY = -0.13;
                }
                moveSpeed = lastDist - lastDist / 55;
            }

            event.setMoveSpeed(Math.max(moveSpeed, baseSpeed));
            shouldBoost = mc.player.onGround;
        }
    }
}
