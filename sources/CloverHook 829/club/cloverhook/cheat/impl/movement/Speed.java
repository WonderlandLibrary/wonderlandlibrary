package club.cloverhook.cheat.impl.movement;

import club.cloverhook.utils.property.impl.BooleanProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.player.Phase;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerJumpEvent;
import club.cloverhook.event.minecraft.PlayerMoveEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.utils.BlockUtils;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import org.apache.commons.lang3.time.StopWatch;

/*
 * HOLY GRAEL OF DIRTY/MESSY CODE - FEEL FREE TO CLEAN THIS BITCH UP
 *
 *
 *  DONT REMOVE SHIT FROM IT - JUST ADD ONTO IT!
 *
 *  Notes:
 *  using setspeed with the ``speed`` double property needs to be done on pre, else it seems to not work!
 *
 *
 * */
public class Speed extends Cheat {

    private boolean slowDownHop;
    private double less, stair;

    private StringsProperty prop_mode = new StringsProperty("Mode", "change the mode.", null, false, true, new String[]{"Hypixel"}, new Boolean[]{true});

    public Speed() {
        super("Speed", "Makes you move faster.", CheatCategory.MOVEMENT);
        registerProperties(prop_mode);
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        if (prop_mode.getValue().get("Hypixel")) {
            mc.thePlayer.motionZ = 0;
            mc.thePlayer.motionX = 0;

        }
    }

    public void onEnable() {
        if (prop_mode.getValue().get("Hypixel")) {
            speed = mc.thePlayer.getBaseMoveSpeed();
            less = 0;
            stage = 2;
            mc.timer.timerSpeed = 1;
        }
    }

    private double speed;
    private int stage;

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent e) {
        setMode(prop_mode.getSelectedStrings().get(0));
    }

    @Collect
    public void onPlayerMove(PlayerMoveEvent e) {
        if (prop_mode.getValue().get("Hypixel")) {
            boolean collided = mc.thePlayer.isCollidedHorizontally;
            if (collided)
                stage = -1;
            if (stair > 0)
                stair -= 0.25;
            less -= less > 1 ? 0.12 : 0.11;
            if (less < 0)
                less = 0;
            if (!mc.thePlayer.isInWater() && mc.thePlayer.onGround && mc.thePlayer.isMoving()) {
                collided = mc.thePlayer.isCollidedHorizontally;
                if (stage >= 0 || collided) {
                    stage = 0;

                    double motY = 0.407 + mc.thePlayer.getJumpEffect() * 0.1;
                    if (stair == 0) {
                        mc.thePlayer.motionY = motY;
                        e.setY(mc.thePlayer.motionY = motY);
                    }

                    less++;
                    if (less > 1 && !slowDownHop)
                        slowDownHop = true;
                    else
                        slowDownHop = false;
                    if (less > 1.12)
                        less = 1.12;
                }
            }
            speed = getCurrentSpeed(stage) + 0.0312;
            speed *= 0.952;

            if (stair > 0)
                speed *= 0.72 - mc.thePlayer.getSpeedEffect() * 0.206;
            if (stage < 0)
                speed = mc.thePlayer.getBaseMoveSpeed();
            if (slowDownHop)
                speed *= 0.815;
            if (mc.thePlayer.isCollidedHorizontally)
                mc.thePlayer.setMotion(speed / 10);


            if (mc.thePlayer.isInWater())
                speed = 0.12;
            if (mc.thePlayer.isMoving()) {
                mc.thePlayer.setMoveSpeedAris(e, speed);
                ++stage;
            } else {
                mc.thePlayer.motionZ = 0;
                mc.thePlayer.motionX = 0;
            }
        }
    }

    private double getCurrentSpeed(int stage) {
        double speed = mc.thePlayer.getBaseMoveSpeed() + (0.028 * mc.thePlayer.getSpeedEffect()) + (double) mc.thePlayer.getSpeedEffect() / 15;
        double initSpeed = 0.4145 + mc.thePlayer.getSpeedEffect() / 12.5;
        double decrease = (((double) stage / 500) * 1.87);


        if (stage == 0)
            speed = 0.64 + (mc.thePlayer.getSpeedEffect() + (0.028 * mc.thePlayer.getSpeedEffect())) * 0.134;
        else if (stage == 1)
            speed = initSpeed;
        else if (stage >= 2)
            speed = initSpeed - decrease;

        return Math.max(speed, slowDownHop ? speed : mc.thePlayer.getBaseMoveSpeed() + (0.028 * mc.thePlayer.getSpeedEffect()));
    }
}