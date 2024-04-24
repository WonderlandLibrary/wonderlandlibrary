package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class BlockActivated extends Block {

    public BlockActivated() {
        super("BlockActivated", MainBlockType.Boolean, Tabs.Game, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.description = "Apply on block activated to a block returns true if block activated " + "\n" + "Block.onBlockActivated()";

        this.words[0] = "Block";
        this.words[1] = "BlockPos";
        this.words[2] = "Hand";
        this.words[3] = "Facing";
        this.words[4] = "X";
        this.words[5] = "Y";
        this.words[6] = "Z";
        this.typesAccepted.add(typesCollapse(MainBlockType.Blocks));
        this.typesAccepted.add(typesCollapse(MainBlockType.BlockPos));
        this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        this.typesAccepted.add(typesCollapse(MainBlockType.Facing));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((net.minecraft.block.Block) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).onBlockActivated(
                Minecraft.getMinecraft().world, (BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event),
                Minecraft.getMinecraft().world.getBlockState((BlockPos) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event))
                , Minecraft.getMinecraft().player,
                (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock,2, event),
                (EnumFacing) dragableBlock.local.codeExecuter.solveObject(dragableBlock,3, event),
                (float) dragableBlock.local.codeExecuter.solveObject(dragableBlock,4, event), (float) dragableBlock.local.codeExecuter.solveObject(dragableBlock,5, event), (float) dragableBlock.local.codeExecuter.solveObject(dragableBlock,6, event));

    }

}
