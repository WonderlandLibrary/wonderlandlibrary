package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class GetSlider extends Block {

    public GetSlider() {
        super( "GetSlider", MainBlockType.Numbers, Tabs.Settings);
    }


    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        return ((SettingObject) dragableBlock.blockPointer).setting.getValDouble();
    }

}
