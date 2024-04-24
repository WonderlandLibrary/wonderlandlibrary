package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class BlocksAroundEntity extends Block {

    public BlocksAroundEntity() {
        super("BlocksAroundEntity", MainBlockType.Array, Tabs.Entity, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);

        this.words[0] = "Entity";
        this.words[1] = "XRadius";
        this.words[2] = "YRadius";
        this.words[3] = "ZRadius";
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Returns an array of blocks (Blocks) around the entity" +
                "\n" + "XRadius, YRadius, ZRadius are the radius "+"\n" +"of the blocks around the entity";

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        ArrayList<net.minecraft.block.Block> blocks = new ArrayList<>();
        for (int x = (int) (((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posX - dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event)); x <= ((int) ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posX + dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,1, event)); ++x)
            for (int y = (int) (((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posY - dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event)); y <= ((int) ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posY + dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,2, event)); ++y)
                for (int z = (int) (((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posZ - dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event)); z <= ((int) ((EntityLivingBase) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).posZ + dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,3, event)); ++z) {
                    blocks.add(Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock());

                }
        return blocks;
    }
}
