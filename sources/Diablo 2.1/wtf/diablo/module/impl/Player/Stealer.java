package wtf.diablo.module.impl.Player;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;
import wtf.diablo.events.impl.UpdateEvent;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.NumberSetting;
import wtf.diablo.utils.chat.ChatUtil;
import wtf.diablo.utils.math.MathUtil;
import wtf.diablo.utils.math.Stopwatch;

@Getter
@Setter
public class Stealer extends Module {
    public static boolean isStealing;
    private final Stopwatch timer = new Stopwatch();
    public NumberSetting delay = new NumberSetting("Delay", 50, 1, 1, 150);

    public Stealer() {
        super("Stealer", "Steal items from chests", Category.PLAYER, ServerType.All);
        addSettings(delay);
    }

    @Override
    public void onEnable() {
        isStealing = false;
        super.onEnable();
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        long delay = (long) (this.delay.getValue() + MathUtil.getRandInt(0, 5));

        this.setSuffix(String.valueOf(this.delay.getValue()));
        if (mc.thePlayer.openContainer instanceof ContainerChest) {
            final ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;
            for (int i = 0; i < container.getLowerChestInventory().getSizeInventory(); ++i) {
                if (container.getLowerChestInventory().getStackInSlot(i) != null && this.timer.hasReached(delay)) {
                    if (container.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Chest") || container.getLowerChestInventory().getDisplayName().getUnformattedText().contains("Storage")) {
                        isStealing = true;
                        mc.playerController.windowClick(container.windowId, i, 0, 1, mc.thePlayer);
                    }
                    this.timer.reset();
                }
            }
            if (this.isContainerEmpty(container)) {
                mc.thePlayer.closeScreen();
            }
        } else {
            isStealing = false;
        }
    }

    public boolean isValid(Item item) {
        if (item instanceof ItemSword) {
            return true;
        }
        if (item instanceof ItemAxe) {
            return true;
        }
        if (item instanceof ItemFood) {
            return true;
        }
        if (item instanceof ItemArmor) {
            return true;
        } else {
            return item instanceof ItemPotion;
        }
    }

    public boolean isContainerEmpty(Container container) {
        boolean temp = true;
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                temp = false;
            }
        }
        return temp;
    }
}
