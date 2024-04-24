package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class fogDensity extends Block {
    public fogDensity() {
        super( "fogDensity", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.FogDensity);
        this.description = "EntityViewRenderEvent.FogDensity";
    }


    public static class setDensity extends Block {
        public setDensity() {
            super( "setDensity", MainBlockType.Default, Tabs.Sub,Headers.FogDensity, BlockObjects.Name,BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((EntityViewRenderEvent.FogDensity) event).setDensity((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
            super.runCode(dragableBlock, event);
        }
    }
    public static class getState extends Block {
        public getState() {
            super( "getState", MainBlockType.IBlockState, Tabs.Sub, BlockObjects.Name, Headers.FogDensity);
            this.typesAccepted.add(typesCollapse(MainBlockType.IBlockState));
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.FogDensity)event).getState();
        }
    }
}
