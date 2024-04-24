package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class SettingChanged extends Block {
    public SettingChanged() {
        super( "SettingChanged", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.SettingChanged);
        this.description = "Called when a Setting is changed.";
    }

    public static class SettingType extends Block {
        public SettingType() {
            super( "SettingType", MainBlockType.String, Tabs.Sub, BlockObjects.Name, Headers.SettingChanged);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return (dragableBlock.local.changedSetting);
        }
    }

}
