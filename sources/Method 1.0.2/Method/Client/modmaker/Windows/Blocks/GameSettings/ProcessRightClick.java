package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class ProcessRightClick extends Block {

    public ProcessRightClick() {
        super("ProcessRightClick", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.typesAccepted.add(typesCollapse(MainBlockType.EnumHand));
        this.words[0] = "Hand";
        this.description = "Tries to right click Entity" + "\n" + "mc.playerController.processRightClick(EntityPlayer , EnumHand )";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().playerController.processRightClick((EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event), Minecraft.getMinecraft().world,
                (EnumHand) dragableBlock.local.codeExecuter.solveObject(dragableBlock,1, event));

        super.runCode(dragableBlock, event);
    }

}
