package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class ReachDistance extends Block {

    public ReachDistance() {
        super("ReachDistance", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Set player Reach Distance   " + "\n" + "mc.player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(Num);";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).setBaseValue(dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
        super.runCode(dragableBlock, event);
    }

}
