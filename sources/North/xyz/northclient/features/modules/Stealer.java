package xyz.northclient.features.modules;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import xyz.northclient.features.AbstractModule;
import xyz.northclient.features.Category;
import xyz.northclient.features.EventLink;
import xyz.northclient.features.ModuleInfo;
import xyz.northclient.features.events.UpdateEvent;
import xyz.northclient.features.values.DoubleValue;
import xyz.northclient.util.Stopwatch;

import java.util.Random;

@ModuleInfo(name = "Stealer",description = "",category = Category.WORLD, keyCode = Keyboard.KEY_N)
public class Stealer extends AbstractModule {
    public DoubleValue delay = new DoubleValue("Delay",this)
            .setDefault(1)
            .setMin(0)
            .setMax(100)
            .enableOnlyInt();
    public Stopwatch timer = new Stopwatch();
    private long nextClick;
    private int lastClick;
    private int lastSteal;
    @EventLink
    public void onUpdate(UpdateEvent event) {

        if (mc.currentScreen instanceof GuiChest) {
            final ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;

            if (!this.timer.elapsed2(this.nextClick)) {
                return;
            }

            this.lastSteal++;

            for (int i = 0; i < container.inventorySlots.size(); i++) {
                final ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);

                if (stack == null || this.lastSteal <= 1) {
                    continue;
                }

                this.nextClick = Math.round(getRandomInRange(delay.get().intValue(),delay.get().intValue()+10));
                mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                this.timer.reset();
                this.lastClick = 0;
                if (this.nextClick > 0) return;
            }

            this.lastClick++;

            if (this.lastClick > 1) {
                mc.thePlayer.closeScreen();
            }
        } else {
            this.lastClick = 0;
            this.lastSteal = 0;
        }
    }

    public static double getRandomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted;
    }
}
