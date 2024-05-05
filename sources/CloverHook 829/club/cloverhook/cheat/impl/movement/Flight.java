package club.cloverhook.cheat.impl.movement;

import java.util.Arrays;
import java.util.List;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.cheat.impl.combat.aura.Aura;
import club.cloverhook.cheat.impl.player.Phase;
import club.cloverhook.event.Stage;
import club.cloverhook.event.minecraft.PlayerMoveEvent;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.ProcessPacketEvent;
import club.cloverhook.utils.Mafs;
import club.cloverhook.utils.Stopwatch;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.DoubleProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.Sys;

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

public class Flight extends Cheat {

    boolean canPacketBoost;
    private int level;
    private long lastboost;
    private boolean initialboost, secondaryboost, theogdamageboost;
    public static long disabled;
    private int delay, counter;
    public double lastreporteddistance, movementSpeed, oldX, oldY, oldZ;
    private final StringsProperty prop_mode = new StringsProperty("Fly", "How this cheat will function.", null, false, false, new String[]{"HypixelFast"}, new Boolean[]{true});
    private BooleanProperty prop_bobbing = new BooleanProperty("View Bobbing", "bippity boppity", null, false);
    private BooleanProperty prop_damage = new BooleanProperty("Damage", "Sanic niggas!", null, true);

    public Flight() {
        super("Fly", "Fuck that little faggot!", CheatCategory.MOVEMENT);
        registerProperties(prop_mode, prop_bobbing, prop_damage);
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindJump.pressed = false;
        getPlayer().stepHeight = 0.6F;
        getPlayer().setSpeed(0);

        if (prop_mode.getValue().get("HypixelFast")) {
            initialboost = true;
            level = 1;
        }
        lastboost = System.currentTimeMillis();
        disabled = System.currentTimeMillis();
    }

    @Override
    public void onEnable() {
        oldY = getPlayer().posY;
        canPacketBoost = false;
        getPlayer().stepHeight = 0;
        if (mc.thePlayer.fallDistance > 3 || System.currentTimeMillis() - lastboost >= 500 && prop_mode.getValue().get("HypixelFast") && getPlayer().onGround && getPlayer().isMoving()) {
            if (!prop_damage.getValue()) {
                getPlayer().motionY = 0.412f;
                theogdamageboost = false;
                initialboost = true;
                secondaryboost = false;
                delay = getPlayer().isPotionActive(Potion.moveSpeed) ? 40 : 50;
                movementSpeed = 1;
            } else {
                for (int i = 0; i <= 70; i++) {
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 0.05, Minecraft.getMinecraft().thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 0.00001, Minecraft.getMinecraft().thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                }
                mc.thePlayer.setMotion(0);
                mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                getPlayer().motionY = 0.41999998688698f;
                theogdamageboost = true;
                secondaryboost = false;
                initialboost = false;
            }


            lastboost = System.currentTimeMillis();
        } else {
            mc.thePlayer.setSpeed(0);
            theogdamageboost = false;
            secondaryboost = false;
            initialboost = false;
        }
        level = 1;

        mc.thePlayer.packets = 0;
        mc.thePlayer.packets2 = 0;

        mc.thePlayer.hurtTime = 0;
    }

    @Collect
    public void onPlayerMove(PlayerMoveEvent playerMoveEvent) {
        if (!canPacketBoost) {
            if (prop_mode.getValue().get("HypixelFast")) {
                List collidingList = mc.theWorld.getCollidingBoundingBoxes(getPlayer(), getPlayer().getEntityBoundingBox().offset(0, 0, 0));
                if (theogdamageboost) {
                    if (level != 1 || !getPlayer().isMoving()) {
                        if (level == 2) {
                            level = 3;
                            movementSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.14 : 2.22;
                        } else if (level == 3) {
                            level = 4;
                            double difference = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? .0079 : .0002) * (lastreporteddistance - mc.thePlayer.getBaseMoveSpeed());
                            movementSpeed = lastreporteddistance - difference;
                        } else {
                            if (collidingList.size() > 0 || getPlayer().isCollidedVertically) {
                                level = 1;
                            }
                            movementSpeed = lastreporteddistance - (lastreporteddistance / 95000);
                        }
                    } else {
                        level = 2;
                        double boost = getPlayer().isPotionActive(Potion.moveSpeed) ? 1.89 : 2.24;
                        movementSpeed = boost * mc.thePlayer.getBaseMoveSpeed();

                    }
                    mc.thePlayer.setMoveSpeedAris(playerMoveEvent, movementSpeed = Math.max(mc.thePlayer.getBaseMoveSpeed(), movementSpeed));
                } else if (initialboost) {
                    if (level != 1 || !getPlayer().isMoving()) {
                        mc.timer.timerSpeed = 1.0F;
                        if (level == 2) {
                            level = 3;
                            movementSpeed *= getPlayer().isPotionActive(Potion.moveSpeed) ? 2.14 : 2.2;
                        } else if (level == 3) {
                            level = 4;
                            double difference = (getPlayer().isPotionActive(Potion.moveSpeed) ? .7 : .6) * (lastreporteddistance - mc.thePlayer.getBaseMoveSpeed());
                            movementSpeed = lastreporteddistance - difference;
                        } else {
                            if (collidingList.size() > 0 || getPlayer().isCollidedVertically) {
                                level = 1;
                            }
                            movementSpeed = lastreporteddistance - lastreporteddistance / (120 + delay);
                        }
                    } else {
                        level = 2;
                        //mc.timer.timerSpeed = 1.42F;
                        double boost = getPlayer().isPotionActive(Potion.moveSpeed) ? 1.85 : 2.13;
                        movementSpeed = boost * mc.thePlayer.getBaseMoveSpeed();
                    }
                    mc.thePlayer.setMoveSpeedAris(playerMoveEvent, movementSpeed = Math.max(mc.thePlayer.getBaseMoveSpeed(), movementSpeed));
                }
            }
        } else {
            mc.thePlayer.setMoveSpeedAris(playerMoveEvent,  4.4);
        }
    }

    @Collect
    public void onProcessPacket(ProcessPacketEvent processPacketEvent) {
        if (processPacketEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) processPacketEvent.getPacket();
            canPacketBoost = true;
            mc.thePlayer.packets = 0;
            mc.thePlayer.packets2 = 0;
            if (packet.getYaw() == 0.0 && packet.getPitch() == 0.0 && packet.getX() == 0.0 && packet.getZ() == 0.0 && packet.getY() == 0.0) {
                processPacketEvent.setCancelled(true);
            }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent event) {
        setMode(prop_mode.getSelectedStrings().get(0));
        if (event.isPre()) {
            if (prop_mode.getValue().get("HypixelFast")) {
                if (event.isPre()) {
                    if (getPlayer().movementInput.jump) {
                        getPlayer().motionY += 0.26622;
                    } else if (getPlayer().movementInput.sneak) {
                        getPlayer().motionY -= 0.26622;
                    } else {
                        lastreporteddistance = Math.hypot(getPlayer().posX - getPlayer().prevPosX, getPlayer().posZ - getPlayer().prevPosZ) * .99;
                        if (initialboost || secondaryboost) {
                            delay -= 1;
                            setMode("HypixelFast " + delay);
                            if (delay <= 0) {

                                initialboost = false;
                                secondaryboost = true;

                            }
                            if (delay <= -10) {
                                mc.timer.timerSpeed = 1.0F;
                                secondaryboost = false;
                            }
                        } else {
                            setMode("HypixelFast");
                        }
                        getPlayer().motionY = 0;
                        getPlayer().onGround = true;
                        counter++;
                        if (prop_bobbing.getValue())
                            getPlayer().cameraYaw = .105f;

                        if (counter == 1) {
                            getPlayer().setPosition(getPlayer().posX, getPlayer().posY + 1.0E-13D, getPlayer().posZ);
                        } else if (counter == 2) {
                            getPlayer().setPosition(getPlayer().posX, getPlayer().posY + 1.0E-123D, getPlayer().posZ);
                            counter = 0;
                        }
                    }
                }
            }
        }
    }
}