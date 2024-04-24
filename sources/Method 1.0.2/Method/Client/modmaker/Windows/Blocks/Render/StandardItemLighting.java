package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.renderer.RenderHelper;

public class StandardItemLighting extends Block {

    public StandardItemLighting() {
        super("EnableStandardItemLighting", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.DropDown);

        ddOptions.add("Enable");
        ddOptions.add("Disable");
        this.description = "Enables or disables standard item lighting.";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Enable":
                RenderHelper.enableStandardItemLighting();
                break;
            case "Disable":
                RenderHelper.disableStandardItemLighting();
                break;
        }
        super.runCode(dragableBlock, event);
    }


}
