package sudo.module.combat;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;

public class HoleTP extends Mod {
	
	public BooleanSetting bedrock = new BooleanSetting("Bedrock", true);
	public BooleanSetting mixed = new BooleanSetting("Mixed", true);
	public BooleanSetting obi = new BooleanSetting("Obsidian", true);
	public NumberSetting range = new NumberSetting("Range", 1, 4, 2, 1);
	private Map<BlockPos, float[]> holes = new HashMap<>();
	
	float[] bedrockColor = new float[] {30f, 235f, 30f};
	float[] obsidianColor = new float[] {30f, 235f, 235f};
	float[] mixedColor  = new float[] {127f, 0f, 127f};
	
	public HoleTP() {
		super("HoleTP", "TP the players to the nearest safe hole (shift to TP)", Category.COMBAT, 0);
		addSettings(bedrock, mixed, obi, range);
	}
	
	@Override
	public void onTick() {
		if (mc.player.age % 14 == 0) {
			holes.clear();

			int dist = 10;
			
			for (BlockPos pos : BlockPos.iterateOutwards(mc.player.getBlockPos(), dist, dist, dist)) {
				if (!mc.world.isInBuildLimit(pos.down())
						|| (mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK
						&& mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN)
						|| !mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).isEmpty()
						|| !mc.world.getBlockState(pos.up(1)).getCollisionShape(mc.world, pos.up(1)).isEmpty()
						|| !mc.world.getBlockState(pos.up(2)).getCollisionShape(mc.world, pos.up(2)).isEmpty()) {
					continue;
				}
				if (mc.player.getBoundingBox().getCenter().x > pos.getX() + 0.1
				&& mc.player.getBoundingBox().getCenter().x < pos.getX() + 0.9
				&& mc.player.getBoundingBox().getCenter().z > pos.getZ() + 0.1
				&& mc.player.getBoundingBox().getCenter().z < pos.getZ() + 0.9){
					continue;
				}
				
				int bedrockCounter = 0;
				int obsidianCounter = 0;
				for (BlockPos pos1 : neighbours(pos)) {
					if (mc.world.getBlockState(pos1).getBlock() == Blocks.BEDROCK) {
						bedrockCounter++;
					} else if (mc.world.getBlockState(pos1).getBlock() == Blocks.OBSIDIAN) {
						obsidianCounter++;
					} else {
						break;
					}
				}

				if (bedrockCounter == 5 && bedrock.isEnabled()) {
					holes.put(pos.toImmutable(), bedrockColor);
				} else if (obsidianCounter == 5 && obi.isEnabled()) {
					holes.put(pos.toImmutable(), obsidianColor);
				} else if (bedrockCounter >= 1 && obsidianCounter >= 1
						&& bedrockCounter + obsidianCounter == 5 && mixed.isEnabled()) {
					holes.put(pos.toImmutable(), mixedColor);
				}
			}
		}
		super.onTick();
	}
	int cooldown = 30;
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		holes.forEach((pos, color) -> {
			if (pos.getSquaredDistance(mc.player.getPos())<range.getValue()) {
				if (mc.options != null && mc.options.sneakKey.isPressed()) {
					mc.player.updatePosition(pos.getX()+0.5, pos.getY(), pos.getZ()+0.5);
				}
			}
		});
		cooldown--;
	}
	@Override
	public void onEnable() {
		mc.inGameHud.getChatHud().addMessage(Text.literal("[Sudo] Press the sneak key to TP to the nearest safe hole"));
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
	private BlockPos[] neighbours(BlockPos pos) {
		return new BlockPos[] {
				pos.west(), pos.east(), pos.south(), pos.north(), pos.down()
		};
	}
}