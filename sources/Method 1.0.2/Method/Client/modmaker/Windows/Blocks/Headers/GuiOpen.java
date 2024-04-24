package Method.Client.modmaker.Windows.Blocks.Headers;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;

public class GuiOpen extends Block {
    public GuiOpen() {
        super("GuiOpen", MainBlockType.Header, Tabs.Events, BlockObjects.Name, Headers.GuiOpenEvent);
        this.description = "Calls every time the GUI is opened";
    }

    public static class getGui extends Block {
        public getGui() {
            super("GuiOpen/getGui", MainBlockType.Gui, Tabs.Sub, BlockObjects.Name, Headers.GuiOpenEvent);
        }

        @Override
        public Object runCodeObject(DragableBlock dragableBlock, Object event) {
            return ((GuiOpenEvent) event).getGui();
        }
    }

    public static class setGui extends Block {
        public setGui() {
            super("setGui", MainBlockType.Default, Tabs.Sub, Headers.GuiOpenEvent, BlockObjects.Name, BlockObjects.NumericalTextEnter);
            this.typesAccepted.add(typesCollapse(MainBlockType.Gui));
        }
        @Override
        public void runCode(DragableBlock dragableBlock, Object event) {
            ((GuiOpenEvent) event).setGui((GuiScreen) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
            super.runCode(dragableBlock, event);
        }
    }

}
