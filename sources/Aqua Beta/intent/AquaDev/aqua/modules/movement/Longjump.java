package intent.AquaDev.aqua.modules.movement;

import de.Hero.settings.Setting;
import events.Event;
import events.listeners.EventClick;
import events.listeners.EventPreMotion;
import events.listeners.EventTick;
import events.listeners.EventTimerDisabler;
import events.listeners.EventUpdate;
import intent.AquaDev.aqua.Aqua;
import intent.AquaDev.aqua.modules.Category;
import intent.AquaDev.aqua.modules.Module;
import intent.AquaDev.aqua.utils.PlayerUtil;
import intent.AquaDev.aqua.utils.Test;
import intent.AquaDev.aqua.utils.TimeUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class Longjump
extends Module {
    public static double Packets;
    public static boolean jump;
    public static boolean startUP;
    public double posY;
    public int stage;
    public int jumps;
    public boolean dmg;
    public int groundTicks;
    TimeUtil timer = new TimeUtil();
    TimeUtil timeUtil = new TimeUtil();
    public boolean hittet = false;
    Test test = new Test();

    public Longjump() {
        super("Longjump", "Longjump", 0, Category.Movement);
        Aqua.setmgr.register(new Setting("Mode", (Module)this, "Watchdog", new String[]{"Watchdog", "WatchdogBow", "Gamster"}));
    }

    public void onEnable() {
        this.hittet = false;
        PlayerUtil.setSpeed((double)0.0);
        jump = false;
        startUP = true;
        Longjump.mc.gameSettings.keyBindUseItem.pressed = Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("WatchdogBow");
        Aqua.moduleManager.getModuleByName("Killaura").setState(false);
        if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
            if (Longjump.mc.thePlayer.onGround) {
                Longjump.mc.thePlayer.jump();
            }
            Longjump.mc.timer.timerSpeed = 0.05f;
        }
        if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Watchdog")) {
            PlayerUtil.setSpeed((double)0.0);
            Longjump.mc.gameSettings.keyBindForward.pressed = false;
            Longjump.mc.thePlayer.motionX = 0.0;
            Longjump.mc.thePlayer.motionZ = 0.0;
            this.dmg = false;
            this.posY = Longjump.mc.thePlayer.posY;
            jump = false;
            this.stage = 0;
            this.jumps = 0;
            this.groundTicks = 0;
        }
        super.onEnable();
    }

    public void onDisable() {
        this.hittet = false;
        this.timeUtil.reset();
        jump = false;
        Longjump.mc.thePlayer.jumpMovementFactor = 0.0f;
        Longjump.mc.thePlayer.capabilities.isFlying = false;
        Longjump.mc.timer.timerSpeed = 1.0f;
        Longjump.mc.thePlayer.capabilities.allowFlying = false;
        startUP = false;
        Aqua.moduleManager.getModuleByName("Killaura").setState(true);
        if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
            // empty if block
        }
        if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Watchdog")) {
            Longjump.mc.gameSettings.keyBindJump.pressed = false;
            this.dmg = false;
            this.jumps = 0;
            this.stage = 0;
            this.groundTicks = 0;
            this.timer.reset();
            jump = false;
        }
        Longjump.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }

    public void onEvent(Event e) {
        Packet packet;
        if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("WatchdogBow") && Longjump.mc.thePlayer.ticksExisted % 7 == 0) {
            Longjump.mc.gameSettings.keyBindUseItem.pressed = false;
        }
        if (e instanceof EventTick && Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
            if (Longjump.mc.thePlayer.ticksExisted % 5 == 0) {
                Longjump.mc.gameSettings.keyBindUseItem.pressed = false;
                Longjump.mc.timer.timerSpeed = 1.0f;
            } else {
                Longjump.mc.timer.timerSpeed = 0.1f;
                Longjump.mc.gameSettings.keyBindUseItem.pressed = true;
            }
        }
        if (e instanceof EventUpdate) {
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("WatchdogBow")) {
                if (Longjump.mc.thePlayer.hurtTime != 0) {
                    jump = true;
                }
                if (!jump) {
                    Longjump.mc.gameSettings.keyBindForward.pressed = false;
                }
                if (jump) {
                    Longjump.mc.gameSettings.keyBindForward.pressed = true;
                }
                if (Longjump.mc.thePlayer.hurtTime == 0) {
                    Longjump.mc.timer.timerSpeed = 0.1f;
                }
                if (Longjump.mc.thePlayer.hurtTime != 0) {
                    PlayerUtil.setSpeed((double)0.45);
                }
                Longjump.mc.thePlayer.jumpMovementFactor = 0.025f;
                if ((Longjump.mc.gameSettings.keyBindForward.pressed || Longjump.mc.gameSettings.keyBindLeft.pressed || Longjump.mc.gameSettings.keyBindRight.pressed || Longjump.mc.gameSettings.keyBindBack.pressed) && Longjump.mc.thePlayer.onGround) {
                    Longjump.mc.thePlayer.motionY = 0.42f;
                    Longjump.mc.timer.timerSpeed = 0.1f;
                    Longjump.mc.thePlayer.setSprinting(true);
                } else {
                    Longjump.mc.thePlayer.setSprinting(true);
                    Longjump.mc.timer.timerSpeed = 1.0f;
                    if (Longjump.mc.thePlayer.hurtTime != 0) {
                        Longjump.mc.thePlayer.motionY += (double)0.028f;
                        if ((double)Longjump.mc.thePlayer.fallDistance > 0.1) {
                            Longjump.mc.thePlayer.motionY = 0.42f;
                        }
                        if (Longjump.mc.thePlayer.fallDistance > 0.0f) {
                            Longjump.mc.thePlayer.motionY = 0.25;
                        }
                    }
                }
            }
            if (!Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster") || Longjump.mc.thePlayer.hurtTime != 0) {
                // empty if block
            }
        }
        if (e instanceof EventPreMotion) {
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
                if (Longjump.mc.thePlayer.hurtTime != 0) {
                    this.hittet = true;
                    ((EventPreMotion)e).setPitch(-40.0f);
                } else {
                    if (Longjump.mc.thePlayer.onGround) {
                        Longjump.mc.thePlayer.jump();
                    }
                    if (!this.hittet) {
                        ((EventPreMotion)e).setPitch(-85.0f);
                    } else {
                        ((EventPreMotion)e).setPitch(-40.0f);
                    }
                }
            }
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("WatchdogBow")) {
                ((EventPreMotion)e).setPitch(-90.0f);
            }
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Watchdog")) {
                if (this.jumps < 3) {
                    Longjump.mc.thePlayer.posY = this.posY;
                } else {
                    Longjump.mc.timer.timerSpeed = 1.0f;
                }
            }
        }
        if (e instanceof EventTimerDisabler && Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Watchdog") && (packet = EventTimerDisabler.getPacket()) instanceof C03PacketPlayer) {
            C03PacketPlayer packetPlayer = (C03PacketPlayer)packet;
            if (this.jumps < 3) {
                packetPlayer.onGround = false;
            }
        }
        if (e instanceof EventClick) {
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Watchdog")) {
                if (Longjump.mc.thePlayer.onGround && this.jumps < 3) {
                    Longjump.mc.thePlayer.jump();
                    ++this.jumps;
                    Longjump.mc.gameSettings.keyBindForward.pressed = false;
                }
                if (Longjump.mc.thePlayer.hurtTime != 0) {
                    jump = true;
                    if (Longjump.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed) != null) {
                        PlayerUtil.setSpeed((double)0.525);
                    } else {
                        PlayerUtil.setSpeed((double)0.4);
                    }
                    Longjump.mc.timer.timerSpeed = 1.0f;
                } else {
                    Longjump.mc.gameSettings.keyBindForward.pressed = false;
                }
                if (jump) {
                    Longjump.mc.gameSettings.keyBindForward.pressed = true;
                    Longjump.mc.gameSettings.keyBindJump.pressed = true;
                    if (!Longjump.mc.thePlayer.onGround) {
                        Longjump.mc.thePlayer.motionY += 0.025;
                    }
                }
            }
            if (Aqua.setmgr.getSetting("LongjumpMode").getCurrentMode().equalsIgnoreCase("Gamster")) {
                // empty if block
            }
        }
    }
}
