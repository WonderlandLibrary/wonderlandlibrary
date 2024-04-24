package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.Patcher.Events.GetAmbientOcclusionLightValueEvent;

public class onGetAmbientOcclusionLightValue extends Block {
    public onGetAmbientOcclusionLightValue() {
        super("AmbientOcclusionLightValue", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.onGetAmbientOcclusionLightValue);
        this.description = "Called when the block is getting the ambient occlusion light value.";
    }

    public static class getLightValue extends Block {
        public getLightValue() {
            super("getLightValue", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.onGetAmbientOcclusionLightValue);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((GetAmbientOcclusionLightValueEvent) event).getLightValue();
        }
    }

    public static class getDefaultLightValue extends Block {
        public getDefaultLightValue() {
            super("getDefaultLightValue", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.onGetAmbientOcclusionLightValue);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((GetAmbientOcclusionLightValueEvent) event).getDefaultLightValue();
        }
    }

    public static class getBlocklight extends Block {
        public getBlocklight() {
            super("getBlocklight", MainBlockType.Blocks, Tabs.Sub, BlockObjects.Name, Headers.onGetAmbientOcclusionLightValue);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GetAmbientOcclusionLightValueEvent) event).getState().getBlock();
        }
    }

    public static class setLightValue extends Block {
        public setLightValue() {
            super("setLightValue", MainBlockType.Default, Tabs.Sub, BlockObjects.NumericalTextEnter, Headers.onGetAmbientOcclusionLightValue);
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((GetAmbientOcclusionLightValueEvent) event).setLightValue((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));

            super.runCode(dragableBlock, event);
        }

    }

}
