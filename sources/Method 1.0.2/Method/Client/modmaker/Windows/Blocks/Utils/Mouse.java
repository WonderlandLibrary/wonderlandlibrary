package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class Mouse extends Block {

    public Mouse() {
        super("Mouse", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("0");
        ddOptions.add("1");
        ddOptions.add("2");
        this.description = "Returns true if the mouse button is down";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "0":
                return org.lwjgl.input.Mouse.isButtonDown(0);
            case "1":
                return org.lwjgl.input.Mouse.isButtonDown(1);
            case "2":
                return org.lwjgl.input.Mouse.isButtonDown(2);

        }
        return false;
    }

}
