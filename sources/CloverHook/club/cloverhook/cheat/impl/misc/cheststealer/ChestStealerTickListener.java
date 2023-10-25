package club.cloverhook.cheat.impl.misc.cheststealer;

import club.cloverhook.Cloverhook;
import club.cloverhook.cheat.Cheat;
import club.cloverhook.event.minecraft.RunTickEvent;
import net.minecraft.client.Minecraft;

/**
 * @author antja03
 */
public class ChestStealerTickListener {
    private ChestStealer chestStealer;

    public ChestStealerTickListener(ChestStealer chestStealer) {
        this.chestStealer = chestStealer;
        Cloverhook.eventBus.register(this);
    }

    public void onRunTick(RunTickEvent runTickEvent) {
        if (Minecraft.getMinecraft().theWorld == null) {
            chestStealer.getLootedChestPositions().clear();
        }
    }
}
