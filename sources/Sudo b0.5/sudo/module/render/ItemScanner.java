package sudo.module.render;

import org.lwjgl.glfw.GLFW;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.utils.world.WorldUtils;

public class ItemScanner extends Mod {

	public ItemScanner() {
		super("ItemScanner", "Find items in nearby chests", Category.RENDER, GLFW.GLFW_KEY_Y);
	}
	
    private static final Formatting Gray = Formatting.GRAY;
	@Override
	public void onTick() {
		this.setDisplayName("ItemScanner " + Gray + "[" + item + "] ");
		super.onTick();
	}
	
	Item item = Items.DIAMOND;
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		for (BlockEntity block : WorldUtils.blockEntities()) {
		    if (block instanceof ChestBlockEntity) {
		        @SuppressWarnings("unused")
				ChestBlockEntity chest = (ChestBlockEntity) block;
		        
		    }
		}
		super.onWorldRender(matrices);
	}
	
	@Override
	public void onEnable() {
		if (mc.player.getMainHandStack().getItem() != Items.AIR) {
			item = mc.player.getMainHandStack().getItem();
			mc.inGameHud.getChatHud().addMessage(Text.literal("Looking for: " + item));
		} else if (mc.player.getMainHandStack().getItem() == Items.AIR) {
			mc.inGameHud.getChatHud().addMessage(Text.literal("Please hold an item to look for"));			
			this.setEnabled(false);
		}
		super.onEnable();
	}
}
