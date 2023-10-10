package vestige.module.impl.combat;

import net.minecraft.network.play.server.S12PacketEntityVelocity;
import vestige.Vestige;
import vestige.util.player.KeyboardUtil;
import vestige.event.Listener;
import vestige.event.impl.PacketReceiveEvent;
import vestige.event.impl.PostMotionEvent;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.module.impl.movement.Blink;
import vestige.module.impl.movement.Longjump;
import vestige.module.impl.movement.Speed;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;

public class Velocity extends Module {

    public final ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Hypixel", "Packet loss", "Legit");

    private final IntegerSetting horizontal = new IntegerSetting("Horizontal", () -> mode.is("Packet"), 0, 0, 100, 2);
    private final IntegerSetting vertical = new IntegerSetting("Vertical", () -> mode.is("Packet"), 0, 0, 100, 2);

    private boolean reducing;

    private boolean pendingVelocity;

    private double motionY;

    private int ticks;

    private int offGroundTicks;

    private boolean wasVelocityEffective;

    private Blink blinkModule;
    private Backtrack backtrackModule;
    private Longjump longjumpModule;
    private Speed speedModule;

    public Velocity() {
        super("Velocity", Category.COMBAT);
        this.addSettings(mode, horizontal, vertical);
    }

    @Override
    public void onEnable() {
        reducing = false;
        offGroundTicks = 0;

        wasVelocityEffective = false;
    }

    @Override
    public void onDisable() {
        if(mode.is("Hypixel") && pendingVelocity) {
            pendingVelocity = false;
            mc.thePlayer.motionY = motionY;
            Vestige.instance.getPacketBlinkHandler().stopBlinkingPing();
        }

        if(mode.is("Packet loss")) {
            Vestige.instance.getPacketBlinkHandler().stopAll();
        }
    }

    @Override
    public void onClientStarted() {
        blinkModule = Vestige.instance.getModuleManager().getModule(Blink.class);
        backtrackModule = Vestige.instance.getModuleManager().getModule(Backtrack.class);
        longjumpModule = Vestige.instance.getModuleManager().getModule(Longjump.class);
        speedModule = Vestige.instance.getModuleManager().getModule(Speed.class);
    }

    @Listener
    public void onReceive(PacketReceiveEvent event) {
        if(canEditVelocity()) {
            if(event.getPacket() instanceof S12PacketEntityVelocity) {
                S12PacketEntityVelocity packet = event.getPacket();

                if(packet.getEntityID() == mc.thePlayer.getEntityId()) {
                    switch (mode.getMode()) {
                        case "Packet":
                            double horizontalMult = horizontal.getValue() / 100.0;
                            double verticalMult = vertical.getValue() / 100.0;

                            if(horizontalMult == 0) {
                                event.setCancelled(true);

                                if(verticalMult > 0) {
                                    mc.thePlayer.motionY = (packet.getMotionY() * verticalMult) / 8000.0;
                                }
                            } else {
                                packet.setMotionX((int) (packet.getMotionX() * horizontalMult));
                                packet.setMotionZ((int) (packet.getMotionZ() * horizontalMult));

                                packet.setMotionY((int) (packet.getMotionY() * verticalMult));
                            }
                            break;
                        case "Hypixel":
                            event.setCancelled(true);

                            if(offGroundTicks == 1 || !speedModule.isEnabled()) {
                                mc.thePlayer.motionY = packet.getMotionY() / 8000.0;
                            } else {
                                pendingVelocity = true;

                                motionY = packet.getMotionY() / 8000.0;

                                Vestige.instance.getPacketBlinkHandler().startBlinkingPing();

                                ticks = 12;
                            }
                            break;
                        case "Packet loss":
                            event.setCancelled(true);
                            pendingVelocity = true;
                            break;
                        case "Legit":
                            if(mc.currentScreen == null) {
                                mc.gameSettings.keyBindSprint.pressed = true;
                                mc.gameSettings.keyBindForward.pressed = true;
                                mc.gameSettings.keyBindJump.pressed = true;
                                mc.gameSettings.keyBindBack.pressed = false;

                                reducing = true;
                            }
                            break;
                    }
                }
            }
        } else {
            switch (mode.getMode()) {
                case "Hypixel":
                    if(pendingVelocity) {
                        pendingVelocity = false;
                        mc.thePlayer.motionY = motionY;
                        Vestige.instance.getPacketBlinkHandler().stopBlinkingPing();
                    }
                    break;
            }
        }
    }

    private boolean canEditVelocity() {
        boolean usingSelfDamageLongjump = longjumpModule.isEnabled() && longjumpModule.mode.is("Self damage");

        return !blinkModule.isEnabled() && !backtrackModule.isDelaying() && !usingSelfDamageLongjump;
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if(mc.thePlayer.onGround) {
            offGroundTicks = 0;
        } else {
            offGroundTicks++;
        }

        switch (mode.getMode()) {
            case "Hypixel":
                --ticks;

                if(pendingVelocity) {
                    if(offGroundTicks == 1 || !Vestige.instance.getPacketBlinkHandler().isBlinkingPing() || ticks == 1) {
                        pendingVelocity = false;
                        mc.thePlayer.motionY = motionY;
                        Vestige.instance.getPacketBlinkHandler().stopBlinkingPing();
                    }

                    mc.gameSettings.keyBindJump.pressed = false;
                }
                break;
            case "Packet loss":
                Vestige.instance.getPacketBlinkHandler().startBlinkingPing();

                if(pendingVelocity) {
                    Vestige.instance.getPacketBlinkHandler().clearPing();
                    pendingVelocity = false;
                } else {
                    Vestige.instance.getPacketBlinkHandler().releasePing();
                }
                break;
        }
    }

    @Listener
    public void onPostMotion(PostMotionEvent event) {
        switch (mode.getMode()) {
            case "Legit":
                if(reducing) {
                    if(mc.currentScreen == null) {
                        KeyboardUtil.resetKeybindings(mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindForward,
                                mc.gameSettings.keyBindJump, mc.gameSettings.keyBindBack);
                    }

                    reducing = false;
                }
                break;
        }
    }

    @Override
    public String getSuffix() {
        return mode.getMode();
    }

}