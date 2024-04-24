package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.RenderBlockModelEvent;

public class onRenderBlockModel extends Block {
    public onRenderBlockModel() {
        super("onRenderBlockModel", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.onRenderBlockModel);
        this.description = "Called when a block is rendered. MUST CALL RENDERBLOCK BEGIN IN ONENABLE!!!! " +
                "\n" + "And when you are done rendering call RENDERBLOCK END IN ONDISABLE!!!!";
    }


    public static class getStatedel extends Block {
        public getStatedel() {
            super("getStatedel", MainBlockType.IBlockState, Tabs.Sub, BlockObjects.Name, Headers.onRenderBlockModel);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((RenderBlockModelEvent) event).getState();
        }
    }


}
