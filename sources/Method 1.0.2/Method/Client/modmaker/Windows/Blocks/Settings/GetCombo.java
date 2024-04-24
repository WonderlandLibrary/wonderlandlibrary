package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class GetCombo extends Block {

    public GetCombo() {
        super("GetCombo", MainBlockType.String, Tabs.Settings);

    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return ((SettingObject) dragableBlock.blockPointer).setting.getValString();
    }

}
