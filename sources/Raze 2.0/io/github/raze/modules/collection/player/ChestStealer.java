package io.github.raze.modules.collection.player;

import io.github.raze.Raze;
import io.github.raze.events.collection.motion.EventMotion;
import io.github.raze.events.system.Event;
import io.github.nevalackin.radbus.Listen;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.math.TimeUtil;
import net.minecraft.inventory.ContainerChest;

public class ChestStealer extends AbstractModule {

    private final BooleanSetting autoClose;
    private final NumberSetting delay;

    private final TimeUtil timer;

    private int ticksInChest;

    public ChestStealer() {
        super("ChestStealer", "Automatically steals items from chests, really useful in survival games.", ModuleCategory.PLAYER);

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                autoClose = new BooleanSetting(this, "Auto Close", true),
                delay = new NumberSetting(this, "Delay", 1, 25, 1)

        );

        timer = new TimeUtil();
    }

    @Listen
    public void onMotion(EventMotion eventMotion) {
        if (eventMotion.getState() == Event.State.PRE) {
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
                ticksInChest++;
                for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i += 1) {
                    if (container.getLowerChestInventory().getStackInSlot(i) != null) {
                        if (timer.elapsed((long) (delay.get().doubleValue() * 10), true)) {
                            mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                            timer.reset();
                        }
                    }
                }
            }
            if(autoClose.get() && ticksInChest > 10) {
                if (mc.thePlayer.openContainer instanceof ContainerChest) {
                    ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;

                    for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i += 1) {
                        if (container.getLowerChestInventory().getStackInSlot(i) == null) {
                            if (timer.elapsed((long) (delay.get().doubleValue() * 10), true)) {
                                mc.thePlayer.closeScreen();
                                ticksInChest = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        ticksInChest = 0;
    }
}
