package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.renderer.DestroyBlockProgress;

public class DamagedBlockInfo extends Block {

    public DamagedBlockInfo() {
        super("DamagedBlockInfo", MainBlockType.Numbers, Tabs.Game, BlockObjects.Name,BlockObjects.NumericalTextEnter,BlockObjects.Words,BlockObjects.DropDown);
        this.description = "Use only with getDamagedBlocks";
        this.words[0] = "Get ";
        ddOptions.add("miningPlayerEntId");
        ddOptions.add("partialBlockProgress");
        ddOptions.add("createdAtCloudUpdateTick");
        this.typesAccepted.add(typesCollapse(MainBlockType.Wild));
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "miningPlayerEntId":
                return ((DestroyBlockProgress) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).miningPlayerEntId;
            case "partialBlockProgress":
                return ((DestroyBlockProgress) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).partialBlockProgress;
            case "createdAtCloudUpdateTick":
                return ((DestroyBlockProgress) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).createdAtCloudUpdateTick;

        }
 return 0;
    }

}
