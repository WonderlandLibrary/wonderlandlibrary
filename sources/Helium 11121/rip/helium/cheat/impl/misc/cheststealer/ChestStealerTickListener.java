package rip.helium.cheat.impl.misc.cheststealer;

import net.minecraft.client.Minecraft;
import rip.helium.Helium;
import rip.helium.event.minecraft.RunTickEvent;

/**
 * @author antja03
 */
public class ChestStealerTickListener {
    private final ChestStealer chestStealer;

    public ChestStealerTickListener(ChestStealer chestStealer) {
        this.chestStealer = chestStealer;
        Helium.eventBus.register(this);
    }

    public void onRunTick(RunTickEvent runTickEvent) {
        if (Minecraft.getMinecraft().theWorld == null) {
            chestStealer.getLootedChestPositions().clear();
        }
    }
}
