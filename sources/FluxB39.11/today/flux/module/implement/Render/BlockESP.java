package today.flux.module.implement.Render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.material.Material;
import today.flux.event.WorldRenderEvent;
import today.flux.module.value.FloatValue;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.value.BooleanValue;


import org.lwjgl.opengl.GL11;

import java.util.*;

public class BlockESP extends Module {
    public BooleanValue dia = new BooleanValue("BlockESP", "Diamond", true);
    public BooleanValue iron = new BooleanValue("BlockESP", "Iron", true);
    public BooleanValue lapis = new BooleanValue("BlockESP", "Lapis", true);
    public BooleanValue emerald = new BooleanValue("BlockESP", "Emerald", true);
    public BooleanValue gold = new BooleanValue("BlockESP", "Gold", true);
    public BooleanValue coal = new BooleanValue("BlockESP", "Coal", true);
    public BooleanValue redstone = new BooleanValue("BlockESP", "Red Stone", true);
    public BooleanValue prompt = new BooleanValue("BlockESP", "prompt",true);
    public FloatValue depth = new FloatValue("BlockESP", "Test Depth", 2f, 1f, 5f, 1f);
    public FloatValue range = new FloatValue("BlockESP", "Range", 32f, 20f, 100f, 1f);

    public OreFinder closeFinder;
    public OreFinder farFinder;

    public BlockESP() {
        super("BlockESP", Category.Render, false);
    }

    @Override
    public void onEnable() {
        closeFinder = new OreFinder(20, depth);
        farFinder = new OreFinder(range, depth);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public static float[] getColor(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return new float[]{0, 1, 1};
        } else if (Blocks.lapis_ore.equals(block)) {
            return new float[]{0, 0, 1};
        } else if (Blocks.iron_ore.equals(block)) {
            return new float[]{1, 1, 1};
        } else if (Blocks.gold_ore.equals(block)) {
            return new float[]{1, 1, 0};
        } else if (Blocks.coal_ore.equals(block)) {
            return new float[]{0, 0, 0};
        } else if (Blocks.emerald_ore.equals(block)) {
            return new float[]{0, 1, 0};
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return new float[]{1, 0, 0};
        }
        return new float[]{0, 0, 0};
    }

    public boolean isTarget(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        if (Blocks.diamond_ore.equals(block)) {
            return dia.getValueState();
        } else if (Blocks.lapis_ore.equals(block)) {
            return lapis.getValueState();
        } else if (Blocks.iron_ore.equals(block)) {
            return iron.getValueState();
        } else if (Blocks.gold_ore.equals(block)) {
            return gold.getValueState();
        } else if (Blocks.coal_ore.equals(block)) {
            return coal.getValueState();
        } else if (Blocks.emerald_ore.equals(block)) {
            return emerald.getValueState();
        } else if (Blocks.redstone_ore.equals(block) || Blocks.lit_redstone_ore.equals(block)) {
            return redstone.getValueState();
        }
        return false;
    }

    public Boolean oreTest(BlockPos origPos, double depth) {
        Collection<BlockPos> posesNew = new ArrayList<>();
        Collection<BlockPos> posesLast = new ArrayList<>(Collections.singletonList(origPos));
        Collection<BlockPos> finalList = new ArrayList<>();
        for (int i = 0; i < depth; i++) {
            for (BlockPos blockPos : posesLast) {
                posesNew.add(blockPos.up());
                posesNew.add(blockPos.down());
                posesNew.add(blockPos.north());
                posesNew.add(blockPos.south());
                posesNew.add(blockPos.west());
                posesNew.add(blockPos.east());
            }
            for (BlockPos pos : posesNew) {
                if (posesLast.contains(pos)) {
                    posesNew.remove(pos);
                }
            }
            posesLast = posesNew;
            finalList.addAll(posesNew);
            posesNew = new ArrayList<>();
        }

        List<Block> legitBlocks = Arrays.asList(Blocks.water, Blocks.lava, Blocks.flowing_lava, Blocks.air, Blocks.flowing_water);

        return finalList.stream().anyMatch(blockPos -> legitBlocks.contains(mc.theWorld.getBlockState(blockPos).getBlock()));
    }

    @EventTarget
    public void onRender(WorldRenderEvent event) {
        if (!closeFinder.isRunning) closeFinder.start();
        if (!farFinder.isRunning) farFinder.start();

        for (BlockPos blockPos : closeFinder.result) {
            renderBlock(blockPos);
        }

        for (BlockPos blockPos : farFinder.result) {
            renderBlock(blockPos);
        }
    }

    private void renderBlock(BlockPos pos) {
        if (mc.theWorld.getBlockState(pos).getBlock().getMaterial() == Material.air) return;
        double x = (double) pos.getX() - mc.getRenderManager().getRenderPosX();
        double y = (double) pos.getY() - mc.getRenderManager().getRenderPosY();
        double z = (double) pos.getZ() - mc.getRenderManager().getRenderPosZ();
        final float[] color = BlockESP.getColor(pos);
        drawOutlinedBlockESP(x, y, z, color[0], color[1], color[2], 0.5f, 3);
    }

    public static void drawOutlinedBlockESP(double x, double y, double z, float red, float green, float blue, float alpha, float lineWidth) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(lineWidth);
        GL11.glColor4f(red, green, blue, alpha);
        drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        tessellator.draw();
        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
        tessellator.draw();
    }
}

