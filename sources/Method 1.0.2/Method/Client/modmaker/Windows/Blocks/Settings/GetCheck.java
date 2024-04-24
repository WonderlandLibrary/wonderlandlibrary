package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class GetCheck extends Block {

    public GetCheck() {
        super("GetCheck", MainBlockType.Boolean, Tabs.Settings);

    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        return ((SettingObject) dragableBlock.blockPointer).setting.getValBoolean();
    }

}
