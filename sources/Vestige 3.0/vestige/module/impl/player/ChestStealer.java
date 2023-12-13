package vestige.module.impl.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import vestige.Vestige;
import vestige.event.Listener;
import vestige.event.impl.UpdateEvent;
import vestige.module.Category;
import vestige.module.Module;
import vestige.setting.impl.BooleanSetting;
import vestige.setting.impl.IntegerSetting;

public class ChestStealer extends Module {

    private final IntegerSetting delay = new IntegerSetting("Delay", 1, 0, 10, 1);
    private final BooleanSetting filter = new BooleanSetting("Filter", true);
    private final BooleanSetting autoClose = new BooleanSetting("Autoclose", true);
    private final BooleanSetting guiDetect = new BooleanSetting("Gui detect", true);

    private int counter;

    private InventoryManager invManager;

    public ChestStealer() {
        super("Chest Stealer", Category.PLAYER);
        this.addSettings(delay, filter, autoClose, guiDetect);
    }

    @Override
    public void onClientStarted() {
        invManager = Vestige.instance.getModuleManager().getModule(InventoryManager.class);
    }

    @Listener
    public void onUpdate(UpdateEvent event) {
        if(mc.thePlayer.openContainer != null && mc.thePlayer.openContainer instanceof ContainerChest && (!isGUI() || !guiDetect.isEnabled())) {
            ContainerChest container = (ContainerChest) mc.thePlayer.openContainer;

            for(int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
                ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);
                if(stack != null && !isUseless(stack)) {
                    if(++counter > delay.getValue()) {
                        mc.playerController.windowClick(container.windowId, i, 1, 1, mc.thePlayer);
                        counter = 0;
                        return;
                    }
                }
            }

            if(autoClose.isEnabled() && isChestEmpty(container)) {
                mc.thePlayer.closeScreen();
            }
        }
    }

    private boolean isChestEmpty(ContainerChest container) {
        for(int i = 0; i < container.getLowerChestInventory().getSizeInventory(); i++) {
            ItemStack stack = container.getLowerChestInventory().getStackInSlot(i);

            if(stack != null && !isUseless(stack)) {
                return false;
            }
        }

        return true;
    }

    private boolean isUseless(ItemStack stack) {
        if(!filter.isEnabled()) {
            return false;
        }

        return invManager.isUseless(stack);
    }

    private boolean isGUI() {
        for(double x = mc.thePlayer.posX - 5; x <= mc.thePlayer.posX + 5; x++) {
            for(double y = mc.thePlayer.posY - 5; y <= mc.thePlayer.posY + 5; y++) {
                for(double z = mc.thePlayer.posZ - 5; z <= mc.thePlayer.posZ + 5; z++) {

                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();

                    if(block instanceof BlockChest || block instanceof BlockEnderChest) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
