package club.cloverhook.cheat.impl.movement;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.player.Phase;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerJumpEvent;
import club.cloverhook.event.minecraft.PlayerMoveEvent;
import club.cloverhook.event.minecraft.PlayerSlowdownEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.SendPacketEvent;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;


public class Longjump extends Cheat {

    protected StringsProperty prop_mode = new StringsProperty("Mode", "Wonder what the fuck this does", null, false, true, new String[]{"Hypixel"}, new Boolean[]{true});
    private float air;
    private int counter;
    private int stage;

    public Longjump() {
        super("LongJump", "Weeeeeeeeeee!", CheatCategory.MOVEMENT);
        registerProperties(prop_mode);
    }

    public void onDisable() {
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
        mc.thePlayer.setSpeed(0);
        mc.timer.timerSpeed = 1.0f;
    }

    public void onEnable() {
        air = 0;
        stage = 0;
    }

    public int getSpeedEffect() {
        return mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
    }

    @Collect
    public void onMove(PlayerMoveEvent e) {
        if (!mc.thePlayer.onGround) {
            if (mc.thePlayer.isCollidedVertically)
                stage = 0;
            mc.thePlayer.motionY = getMotion(stage);
            if (stage > 0)
                stage++;
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        setMode(prop_mode.getSelectedStrings().get(0));
        if (e.isPre() && prop_mode.getValue().get("Hypixel")) {
            float x2 = 0.3f + getSpeedEffect() * 0.45f;
            if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
                setMotion(.15);
                stage = 1;
                mc.thePlayer.jump();
            }
            if(counter > 10)
                air = x2 * 20;
            if (mc.thePlayer.onGround) {
                air = 0;
            } else {
                double speed = (0.95 + getSpeedEffect() * 0.2) - air / 30;
                if (speed < mc.thePlayer.getBaseMoveSpeed() - 0.05)  // + (0.025*MoveUtils.getSpeedEffect())
                    speed = mc.thePlayer.getBaseMoveSpeed() - 0.05;
                setMotion(speed);
                air += x2;
                counter++;
            }
        }
    }

    public void setMotion(double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.thePlayer.motionX = (0.0);
            mc.thePlayer.motionZ = (0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) yaw += ((forward > 0.0) ? -45 : 45);
                else if (strafe < 0.0) yaw += ((forward > 0.0) ? 45 : -45);
                strafe = 0.0;
                if (forward > 0.0) forward = 1.0;
                else if (forward < 0.0) forward = -1.0;
            }
            mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }

    double getMotion(int stage) {
        double[] mot = {0.359, 0.273, 0.201, 0.129, 0.057, -0.015, -0.087, -0.159};
        stage--;
        if (stage >= 0 && stage < mot.length) {
            return mot[stage];
        } else {
            return mc.thePlayer.motionY;
        }
    }
}
