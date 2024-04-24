package Method.Client.modmaker.Windows.Blocks.Render;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class LightMap extends Block {

    public LightMap() {
        super("LightMap", MainBlockType.Default, Tabs.Render, BlockObjects.Name, BlockObjects.DropDown);

        ddOptions.add("Enable");
        ddOptions.add("Disable");
        this.description = "Enables or disables the lightmap";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Enable":
                Minecraft.getMinecraft().entityRenderer.enableLightmap();
                break;
            case "Disable":
                Minecraft.getMinecraft().entityRenderer.disableLightmap();
                break;
        }
        super.runCode(dragableBlock, event);
    }


}
