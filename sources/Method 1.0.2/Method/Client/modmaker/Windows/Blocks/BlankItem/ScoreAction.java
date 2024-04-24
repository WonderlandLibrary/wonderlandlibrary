package Method.Client.modmaker.Windows.Blocks.BlankItem;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.play.server.SPacketUpdateScore;

public class ScoreAction extends Block {

    public ScoreAction() {
        super("ScoreAction", MainBlockType.ScoreAction, Tabs.NewItem, BlockObjects.Name, BlockObjects.DropDown);
        for (SPacketUpdateScore.Action value : SPacketUpdateScore.Action.values()) {
            ddOptions.add(value.toString());
        }
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return SPacketUpdateScore.Action.valueOf(dragableBlock.dropDowns.getSelected());
    }


}
