package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class ViewerPos extends Block {

    public ViewerPos() {
        super("ViewerPos", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);

        ddOptions.add("X");
        ddOptions.add("Y");
        ddOptions.add("Z");
        this.description = "Gets the viewer's position";

    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "X":
                return Minecraft.getMinecraft().getRenderManager().viewerPosX;
            case "Y":
                return Minecraft.getMinecraft().getRenderManager().viewerPosY;
            case "Z":
                return Minecraft.getMinecraft().getRenderManager().viewerPosZ;

        }
        return 0;
    }


}
