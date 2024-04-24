package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraftforge.client.event.EntityViewRenderEvent;

public class fogColor extends Block {
    public fogColor() {
        super("fogColor", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.FogColors);
        this.description = "EntityViewRenderEvent.FogColors";
    }

    public static class setColor extends Block {
        public setColor() {
            super("setColor", MainBlockType.Default, Tabs.Sub, Headers.FogColors, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
            ddOptions.add("Red");
            ddOptions.add("Green");
            ddOptions.add("Blue");
            this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        }

        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            switch (dragableBlock.dropDowns.getSelected()) {
                case "Red":
                    ((EntityViewRenderEvent.FogColors) event).setRed((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                    break;
                case "Green":
                    ((EntityViewRenderEvent.FogColors) event).setGreen((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                    break;
                case "Blue":
                    ((EntityViewRenderEvent.FogColors) event).setBlue((float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event));
                    break;
            }
            super.runCode(dragableBlock, event);
        }
    }


}
