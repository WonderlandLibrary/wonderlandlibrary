package ravenweave.client.module.modules.player;

import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.ComboSetting;
import ravenweave.client.module.setting.impl.DescriptionSetting;

public class NoFall extends Module {
    public static DescriptionSetting warning;
    public static ComboSetting mode;

    int ticks;
    double dist;
    boolean spoofing;

    public NoFall() {
        super("NoFall", ModuleCategory.player);

        this.registerSetting(warning = new DescriptionSetting("HypixelSpoof silent flags."));
        this.registerSetting(mode = new ComboSetting("Mode", Mode.Spoof));
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        switch ((Mode) mode.getMode()) {
            case Spoof -> {
                if ((double) mc.thePlayer.fallDistance > 2.5D) {
                    mc.thePlayer.onGround = true;
                }
            }
            case HypixelSpoof -> {
                if (mc.thePlayer.onGround) {
                    ticks = 0;
                    dist = 0;
                    spoofing = false;
                } else {
                    if (mc.thePlayer.fallDistance > 2) {
                        if (spoofing) {
                            ticks++;
                            mc.thePlayer.onGround = true;

                            if (ticks >= 2) {
                                spoofing = false;
                                ticks = 0;
                                dist = mc.thePlayer.fallDistance;
                            }
                        } else {
                            if (mc.thePlayer.fallDistance - dist > 2) {
                                spoofing = true;
                            }
                        }
                    }
                }
            }
            case Verus -> {
                if (mc.thePlayer.onGround) {
                    dist = 0;
                    spoofing = false;
                } else {
                    if (mc.thePlayer.fallDistance > 2) {
                        if (spoofing) {
                            mc.thePlayer.onGround = true;
                            mc.thePlayer.motionY = 0;
                            spoofing = false;
                            dist = mc.thePlayer.fallDistance;
                        } else {
                            if (mc.thePlayer.fallDistance - dist > 2) {
                                spoofing = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public enum Mode {
        Spoof, HypixelSpoof, Verus
    }
}
