package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class FOVModifier extends Block {
    public FOVModifier() {
        super("FOVModifier", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.FOVModifier);
        this.description = "EntityViewRenderEvent.FOVModifier";
    }

    public static class getFOV extends Block {
        public getFOV() {
            super("getFOV", MainBlockType.Numbers, Tabs.Sub, BlockObjects.Name, Headers.FOVModifier);
        }

        @Override
        public double runCodeSolve(DragableBlock dragableBlock, Object event) {
            return ((EntityViewRenderEvent.FOVModifier) event).getFOV();
        }
    }

    public static class setFov extends Block {
        public setFov() {
            super("setFov", MainBlockType.Default, Tabs.Sub,Headers.FOVModifier,BlockObjects.Name, BlockObjects.NumericalTextEnter );
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((EntityViewRenderEvent.FOVModifier) event).setFOV((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
            super.runCode(dragableBlock, event);
        }
    }
}
