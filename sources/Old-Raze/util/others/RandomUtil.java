package markgg.util.others;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

public class RandomUtil {

	private static final int USERNAME_LENGTH = 9;
	private static final int TEXT_LENGTH = 7;
	
	public String generateUsername() {
		StringBuilder username = new StringBuilder();
		Random rand = new Random();

		for (int i = 0; i < USERNAME_LENGTH; i++) {
			int character = rand.nextInt(128);
			if (Character.isLetterOrDigit(character)) {
				username.append((char) character);
			} else {
				i--;
			}
		}

		return username.toString();
	}
	
	public static String generateRandomLine() {
		StringBuilder username = new StringBuilder();
		Random rand = new Random();

		for (int i = 0; i < TEXT_LENGTH; i++) {
			int character = rand.nextInt(128);
			if (Character.isLetterOrDigit(character)) {
				username.append((char) character);
			} else {
				i--;
			}
		}

		return username.toString();
	}
	
	public static boolean hasBlocksInInventory() {
	    for (ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
	        if (stack != null && stack.getItem() instanceof ItemBlock) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public static int getCurrentPing() {
	    final NetworkPlayerInfo networkPlayerInfo = Minecraft.getMinecraft().getNetHandler().func_175102_a(Minecraft.getMinecraft().thePlayer.getUniqueID());

	    return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
	}
	
	public Block getBlock(BlockPos blockPos) {
	    if (Minecraft.getMinecraft().theWorld != null) {
	        IBlockState blockState = Minecraft.getMinecraft().theWorld.getBlockState(blockPos);
	        if (blockState != null) {
	            return blockState.getBlock();
	        }
	    }
	    return null;
	}

}
