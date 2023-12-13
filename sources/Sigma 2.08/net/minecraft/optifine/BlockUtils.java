package net.minecraft.optifine;

import net.minecraft.block.Block;

public class BlockUtils {
	private static ReflectorClass ForgeBlock = new ReflectorClass(Block.class);
	private static ReflectorMethod ForgeBlock_setLightOpacity = new ReflectorMethod(BlockUtils.ForgeBlock, "setLightOpacity");
	private static boolean directAccessValid = true;

	public static void setLightOpacity(Block block, int opacity) {
		if (BlockUtils.directAccessValid) {
			try {
				block.setLightOpacity(opacity);
				return;
			} catch (IllegalAccessError var3) {
				BlockUtils.directAccessValid = false;

				if (!BlockUtils.ForgeBlock_setLightOpacity.exists()) {
					throw var3;
				}
			}
		}

		Reflector.callVoid(block, BlockUtils.ForgeBlock_setLightOpacity, new Object[] { Integer.valueOf(opacity) });
	}
	
}
