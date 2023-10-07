package sudo.module.render;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.utils.render.QuadColor;
import sudo.utils.render.RenderUtils;

public class HoleESP extends Mod {

    public ModeSetting mode = new ModeSetting("Mode", "Flat", "Flat", "Cube");
    
    public BooleanSetting hide = new BooleanSetting("Hide", false);

    public BooleanSetting bedrock = new BooleanSetting("Bedrock", true);
    public BooleanSetting mixed = new BooleanSetting("Mixed", true);
    public BooleanSetting obi = new BooleanSetting("Obsidian", true);

    public BooleanSetting out = new BooleanSetting("Outline", true);
    public BooleanSetting fill = new BooleanSetting("Fill", true);

    public ColorSetting bedrockColor = new ColorSetting("Bedrock", new Color(0xffca7af8));
    public ColorSetting obsidianColor = new ColorSetting("Obsidian", new Color(0xff9168f8));
    public ColorSetting mixedColor = new ColorSetting("Mixed", new Color(0xff686bf8));

    private Map<BlockPos, float[]> holes = new HashMap<>();

    public HoleESP() {
        super("HoleESP", "Highlights safe holes", Category.RENDER,0);
        addSettings(mode, hide, bedrock, mixed, obi, out, fill, bedrockColor, obsidianColor, mixedColor);
    }

    @Override
    public void onTick() {
        if (mc.player.age % 14 == 0) {
            holes.clear();

            int dist = 10;

            //MAKE THE float[] {}; HERE WITH SLIDERS COLOR PICKER THING

            for (BlockPos pos : BlockPos.iterateOutwards(mc.player.getBlockPos(), dist, dist, dist)) {
                if (!mc.world.isInBuildLimit(pos.down())
                        || (mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK
                        && mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN)
                        || !mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).isEmpty()
                        || !mc.world.getBlockState(pos.up(1)).getCollisionShape(mc.world, pos.up(1)).isEmpty()
                        || !mc.world.getBlockState(pos.up(2)).getCollisionShape(mc.world, pos.up(2)).isEmpty()) {
                    continue;
                }

                if (hide.isEnabled()
                        && mc.player.getBoundingBox().getCenter().x > pos.getX() + 0.1
                        && mc.player.getBoundingBox().getCenter().x < pos.getX() + 0.9
                        && mc.player.getBoundingBox().getCenter().z > pos.getZ() + 0.1
                        && mc.player.getBoundingBox().getCenter().z < pos.getZ() + 0.9
                ) {
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
                    holes.put(pos.toImmutable(), bedrockColor.getRGBFloat());
                } else if (obsidianCounter == 5 && obi.isEnabled()) {
                    holes.put(pos.toImmutable(), obsidianColor.getRGBFloat());
                } else if (bedrockCounter >= 1 && obsidianCounter >= 1
                        && bedrockCounter + obsidianCounter == 5 && mixed.isEnabled()) {
                    holes.put(pos.toImmutable(), mixedColor.getRGBFloat());
                }
            }
        }
        super.onTick();
    }
    @Override
    public void onWorldRender(MatrixStack matrices) {
        holes.forEach((pos, color) -> {
        	if (mode.is("Cube")) {
	            if (out.isEnabled()) {
	                RenderUtils.drawBoxOutline(
	                        new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0), QuadColor.single(color[0], color[1], color[2], 1f), 2);
	            }
	            if (fill.isEnabled()) {
	                RenderUtils.drawBoxFill(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0), QuadColor.single(color[0], color[1], color[2], 0.5f));
	            }
            } else if (mode.is("Flat")) {
	            if (out.isEnabled()) {
	                RenderUtils.drawBottomOutlineRect(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0), QuadColor.single(color[0], color[1], color[2], 1f), 2);
	            }
	            if (fill.isEnabled()) {
	                RenderUtils.drawBottomFilledRect(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0), QuadColor.single(color[0], color[1], color[2], 0.5f));
	            }
            }
        });
    }

    private BlockPos[] neighbours(BlockPos pos) {
        return new BlockPos[] {
                pos.west(), pos.east(), pos.south(), pos.north(), pos.down()
        };
    }
}
