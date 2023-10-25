package intent.AquaDev.aqua.modules.movement;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventPacket;
import events.listeners.EventPlayerMove;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.PlayerUtil;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoubleStoneSlab;
import net.minecraft.block.BlockDoubleStoneSlabNew;
import net.minecraft.block.BlockDoubleWoodSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;

public class Speed
extends Module {
    public Speed() {
        super("Speed", "Speed", 0, Category.Movement);
        Aqua.setmgr.register(new Setting("MotionReset", (Module)this, true));
        Aqua.setmgr.register(new Setting("AutoDisable", (Module)this, true));
        Aqua.setmgr.register(new Setting("WatchdogBoost", (Module)this, false));
        Aqua.setmgr.register(new Setting("Speed", (Module)this, 1.0, 0.3, 9.0, false));
        Aqua.setmgr.register(new Setting("YMotion", (Module)this, 0.42, 0.0, 0.9, false));
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Watchdog", new String[]{"Watchdog", "Watchdog2", "WatchdogNew", "WatchdogSave", "Vanilla", "Strafe", "AAC3", "Intave14"}));
    }

    public void onEnable() {
        if (Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState()) {
            // empty if block
        }
        super.onEnable();
    }

    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        Speed.mc.gameSettings.keyBindJump.pressed = false;
        PlayerUtil.setSpeed((double)0.0);
        super.onDisable();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onEvent(Event event) {
        block47: {
            block50: {
                block51: {
                    block48: {
                        block49: {
                            Packet packet;
                            if (event instanceof EventPacket && (packet = EventPacket.getPacket()) instanceof S08PacketPlayerPosLook && Aqua.setmgr.getSetting("SpeedAutoDisable").isState()) {
                                Aqua.moduleManager.getModuleByName("Speed").setState(false);
                            }
                            if (!(event instanceof EventUpdate)) break block47;
                            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("Vanilla")) {
                                Speed.mc.timer.timerSpeed = 2.0f;
                            }
                            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("Intave14")) {
                                float strafe1;
                                boolean flag;
                                boolean bl = flag = this.getBlockUnderPlayer(0.1f) instanceof BlockStairs || this.getBlockUnderPlayer(0.1f) instanceof BlockSlab && !(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleWoodSlab) && (!(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlab) || this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlabNew);
                                if (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.moduleManager.getModuleByName("Speed").isToggled() && Speed.mc.thePlayer.onGround) {
                                    float t = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.186, (double)0.1861);
                                    if (!Speed.mc.gameSettings.keyBindJump.pressed) {
                                        Speed.mc.thePlayer.jump();
                                    }
                                    if (!flag) {
                                        // empty if block
                                    }
                                }
                                if (Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null && !Speed.mc.gameSettings.keyBindJump.pressed && Speed.mc.thePlayer.isMoving()) {
                                    Speed.mc.thePlayer.jump();
                                }
                                if (!Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null) {
                                    strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.00283f, (double)0.002832f);
                                    if (Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState() && !flag) {
                                        Speed.mc.thePlayer.motionY -= (double)strafe1;
                                    }
                                }
                                if (!Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState()) {
                                    strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.0019f, (double)0.002f);
                                    if (!flag) {
                                        Speed.mc.thePlayer.motionY -= (double)strafe1;
                                    }
                                }
                            }
                            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("AAC3")) {
                                boolean boost;
                                boolean bl = boost = Math.abs((float)(Speed.mc.thePlayer.rotationYawHead - Speed.mc.thePlayer.rotationYaw)) < 90.0f;
                                if (Speed.mc.thePlayer.isMoving() && Speed.mc.thePlayer.hurtTime < 5) {
                                    if (Speed.mc.thePlayer.onGround) {
                                        Speed.mc.timer.timerSpeed = 1.0f;
                                        if (!Aqua.moduleManager.getModuleByName("Scaffold").isToggled()) {
                                            Speed.mc.thePlayer.jump();
                                        } else {
                                            Speed.mc.thePlayer.motionY = 0.4;
                                        }
                                        float f = Speed.getDirection();
                                        if (Aqua.moduleManager.getModuleByName("Scaffold").isToggled()) {
                                            // empty if block
                                        }
                                    } else {
                                        Speed.mc.timer.timerSpeed = 1.0f;
                                        Speed.mc.thePlayer.speedInAir = 0.021f;
                                        double currentSpeed = Math.sqrt((double)(Speed.mc.thePlayer.motionX * Speed.mc.thePlayer.motionX + Speed.mc.thePlayer.motionZ * Speed.mc.thePlayer.motionZ));
                                        double speed1 = 1.0074;
                                        double direction = Speed.getDirection();
                                        Speed.mc.thePlayer.motionX = -Math.sin((double)direction) * speed1 * currentSpeed;
                                        Speed.mc.thePlayer.motionZ = Math.cos((double)direction) * speed1 * currentSpeed;
                                    }
                                }
                            }
                            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("Strafe")) {
                                if (Speed.mc.thePlayer.onGround && !Speed.mc.gameSettings.keyBindJump.pressed) {
                                    Speed.mc.thePlayer.jump();
                                } else {
                                    PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                                }
                            }
                            if (!Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("WatchdogNew")) break block48;
                            if (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null) break block49;
                            float boost = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.186, (double)0.18601);
                            if (Speed.mc.thePlayer.onGround) {
                                boolean flag;
                                if (!Speed.mc.gameSettings.keyBindJump.pressed) {
                                    Speed.mc.thePlayer.jump();
                                }
                                boolean bl = flag = this.getBlockUnderPlayer(0.1f) instanceof BlockStairs || this.getBlockUnderPlayer(0.1f) instanceof BlockSlab && !(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleWoodSlab) && (!(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlab) || this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlabNew);
                                if (!flag) {
                                    PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + (double)boost));
                                }
                                break block48;
                            } else {
                                PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                            }
                            break block48;
                        }
                        if (Speed.mc.thePlayer.onGround && !Speed.mc.gameSettings.keyBindJump.pressed) {
                            Speed.mc.thePlayer.jump();
                        } else {
                            PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                        }
                    }
                    if (!Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("WatchdogSave")) break block50;
                    if (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null) break block51;
                    float boost = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.186, (double)0.18601);
                    if (Speed.mc.thePlayer.onGround) {
                        boolean flag;
                        if (!Speed.mc.gameSettings.keyBindJump.pressed) {
                            Speed.mc.thePlayer.jump();
                        }
                        boolean bl = flag = this.getBlockUnderPlayer(0.1f) instanceof BlockStairs || this.getBlockUnderPlayer(0.1f) instanceof BlockSlab && !(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleWoodSlab) && (!(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlab) || this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlabNew);
                        if (!flag) {
                            PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + (double)boost));
                        }
                    }
                    break block50;
                }
                if (Speed.mc.thePlayer.onGround && !Speed.mc.gameSettings.keyBindJump.pressed) {
                    Speed.mc.thePlayer.jump();
                    PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                }
            }
            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("Watchdog2")) {
                if (Speed.mc.thePlayer.onGround && !Speed.mc.gameSettings.keyBindJump.pressed && Speed.mc.thePlayer.isMoving()) {
                    Speed.mc.thePlayer.motionY += (double)0.15f;
                    if (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null) {
                        PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + 0.105));
                    } else {
                        PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + 0.06));
                    }
                    Speed.mc.timer.timerSpeed = 1.0f;
                    Speed.mc.thePlayer.jumpMovementFactor = 0.04f;
                }
                float strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.0023f, (double)0.00234f);
                if (Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState()) {
                    Speed.mc.thePlayer.motionY -= (double)strafe1;
                }
            }
            if (Aqua.setmgr.getSetting("SpeedMode").getCurrentMode().equalsIgnoreCase("Watchdog")) {
                boolean flag;
                boolean bl = flag = this.getBlockUnderPlayer(0.1f) instanceof BlockStairs || this.getBlockUnderPlayer(0.1f) instanceof BlockSlab && !(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleWoodSlab) && (!(this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlab) || this.getBlockUnderPlayer(0.1f) instanceof BlockDoubleStoneSlabNew);
                if (Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.moduleManager.getModuleByName("Speed").isToggled()) {
                    if (Speed.mc.thePlayer.onGround) {
                        float t = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.186, (double)0.1861);
                        if (!Speed.mc.gameSettings.keyBindJump.pressed) {
                            Speed.mc.thePlayer.jump();
                        }
                        if (!flag) {
                            PlayerUtil.setSpeed((double)(PlayerUtil.getSpeed() + (double)t));
                        }
                    } else {
                        PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
                    }
                }
                if (Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null && !Speed.mc.gameSettings.keyBindJump.pressed && Speed.mc.thePlayer.isMoving()) {
                    Speed.mc.thePlayer.jump();
                }
                if (!Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) == null) {
                    float strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.00283f, (double)0.00284f);
                    if (Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState() && !flag) {
                        Speed.mc.thePlayer.motionY -= (double)strafe1;
                    }
                }
                if (!Speed.mc.thePlayer.onGround && Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null && Aqua.setmgr.getSetting("SpeedWatchdogBoost").isState()) {
                    float strafe1 = (float)MathHelper.getRandomDoubleInRange((Random)new Random(), (double)0.0019f, (double)0.002f);
                    if (!flag) {
                        Speed.mc.thePlayer.motionY -= (double)strafe1;
                    }
                }
                PlayerUtil.setSpeed((double)PlayerUtil.getSpeed());
            }
        }
    }

    public static void setSpeed1(EventPlayerMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0) {
            if (strafe > 0.0) {
                yaw += (float)(forward > 0.0 ? -45 : 45);
            } else if (strafe < 0.0) {
                yaw += (float)(forward > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (forward > 0.0) {
                forward = 1.0;
            } else if (forward < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double mx = Math.cos((double)Math.toRadians((double)(yaw + 90.0f)));
        double mz = Math.sin((double)Math.toRadians((double)(yaw + 90.0f)));
        moveEvent.setX(forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.setZ(forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }

    Block getBlockUnderPlayer(float offsetY) {
        return this.getBlockUnderPlayer((EntityPlayer)Speed.mc.thePlayer, offsetY);
    }

    Block getBlockUnderPlayer(EntityPlayer player, float offsetY) {
        return this.getWorld().getBlockState(new BlockPos(player.posX, player.posY - (double)offsetY, player.posZ)).getBlock();
    }

    WorldClient getWorld() {
        return Speed.mc.theWorld;
    }

    public static float getDirection() {
        float var1 = Speed.mc.thePlayer.rotationYaw;
        if (Speed.mc.thePlayer.moveForward < 0.0f) {
            var1 += 180.0f;
        }
        float forward = 1.0f;
        if (Speed.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Speed.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Speed.mc.thePlayer.moveStrafing > 0.0f) {
            var1 -= 90.0f * forward;
        }
        if (Speed.mc.thePlayer.moveStrafing < 0.0f) {
            var1 += 90.0f * forward;
        }
        return var1 *= (float)Math.PI / 180;
    }
}
