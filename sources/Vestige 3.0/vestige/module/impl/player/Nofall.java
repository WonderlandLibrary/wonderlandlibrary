package vestige.module.impl.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.MotionEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.IntegerSetting;
import vestige.setting.impl.ModeSetting;
import vestige.util.network.PacketUtil;

public class Nofall extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Spoof", "Packet", "Spoof", "Blink");

    private final IntegerSetting blinkTicks = new IntegerSetting("Blink ticks", () -> mode.is("Blink"), 6, 2, 14, 1);
    private final BooleanSetting suspendAll = new BooleanSetting("Suspend all packets", () -> mode.is("Blink"), true);

    private double lastY;
    private double fallDistance;

    private int ticks;

    private boolean blinking;

    public Nofall() {
        super("Nofall", Category.PLAYER);
        this.addSettings(mode, blinkTicks, suspendAll);
    }

    @Override
    public void onEnable() {
        ticks = 0;

        lastY = mc.thePlayer.posY;
        fallDistance = mc.thePlayer.fallDistance;
    }

    @Override
    public void onDisable() {
        Vestige.instance.getPacketBlinkHandler().stopAll();
    }

    @Listener
    public void onMotion(MotionEvent event) {
        double y = event.getY();

        double motionY = y - lastY;

        if(mc.thePlayer.onGround) {
            fallDistance = 0;
        } else {
            if(motionY < 0) {
                fallDistance -= motionY;
            }
        }

        switch (mode.getMode()) {
            case "Packet":
                if(fallDistance >= 3) {
                    PacketUtil.sendPacket(new C03PacketPlayer(true));
                    fallDistance = 0;
                }
                break;
            case "Spoof":
                if(fallDistance >= 3) {
                    event.setOnGround(true);
                    fallDistance = 0;
                }
                break;
            case "Blink":
                if(fallDistance >= 3) {
                    if(!blinking) {
                        if(suspendAll.isEnabled()) {
                            Vestige.instance.getPacketBlinkHandler().startBlinkingAll();
                        } else {
                            Vestige.instance.getPacketBlinkHandler().startBlinkingMove();
                            Vestige.instance.getPacketBlinkHandler().startBlinkingOther();
                        }

                        ticks = 0;

                        blinking = true;
                    }
                    event.setOnGround(true);
                    fallDistance = 0;
                }

                if(blinking) {
                    if(ticks >= blinkTicks.getValue()) {
                        ticks = 0;
                        blinking = false;

                        Vestige.instance.getPacketBlinkHandler().stopAll();
                    } else {
                        ticks++;
                    }
                }
                break;
        }

        lastY = event.getY();
    }

}
