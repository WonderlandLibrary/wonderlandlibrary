package vestige.impl.module.world;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.util.BlockPos;
import vestige.api.event.Listener;
import vestige.api.event.impl.UpdateEvent;
import vestige.api.module.Category;
import vestige.api.module.Module;
import vestige.api.module.ModuleInfo;
import vestige.api.setting.impl.BooleanSetting;
import vestige.api.setting.impl.NumberSetting;
import vestige.util.misc.TimerUtil;

@ModuleInfo(name = "ChestStealer", category = Category.WORLD, key = Keyboard.KEY_N)
public class ChestStealer extends Module {
	
	private final TimerUtil timer = new TimerUtil();
	
	private final NumberSetting delay = new NumberSetting("Delay", this, 25, 0, 300, 10, true);
	private final BooleanSetting autoClose = new BooleanSetting("Auto close", this, true);
	private final BooleanSetting GuiDetect = new BooleanSetting("Gui Detect", this, true);

	public ChestStealer() {
		this.registerSettings(delay, autoClose, GuiDetect);
	}

	@Listener
	public void onUpdate(UpdateEvent e) {
		if((mc.thePlayer.openContainer != null) && (mc.thePlayer.openContainer instanceof ContainerChest)) {
			if(GuiDetect.isEnabled() && isGui()) {
				return;
			}

			ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;

			for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
				if((chest.getLowerChestInventory().getStackInSlot(i) != null)) {
					if(timer.getTimeElapsed() > delay.getCurrentValue()) {
						mc.playerController.windowClick(chest.windowId, i, 0, 1, mc.thePlayer);
						timer.reset();
					}

				}

			}

			if(isChestEmpty(chest) && autoClose.isEnabled() && timer.getTimeElapsed() > delay.getCurrentValue()) {
				mc.thePlayer.closeScreen();
				timer.reset();
			}

		}
	}
	
	public boolean isChestEmpty(final Container container) {
        boolean isEmpty = true;
        int maxSlot = (container.inventorySlots.size() == 90) ? 54 : 27;
        for (int i = 0; i < maxSlot; ++i) {
            if (container.getSlot(i).getHasStack()) {
                isEmpty = false;
            }
        }
        return isEmpty;
    }
	
	public boolean isGui() {
		int range = 5;
		for(int x = -range; x < range; ++x) {
			for(int y = range; y > -range; --y) {
				for(int z = -range; z < range; ++z) {
					int blockX = (int) mc.thePlayer.posX + x;
					int blockY = (int) mc.thePlayer.posY + y;
					int blockZ = (int) mc.thePlayer.posZ + z;
					BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
					Block block = mc.theWorld.getBlockState(blockPos).getBlock();
					if(block instanceof BlockChest || block instanceof BlockEnderChest) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
