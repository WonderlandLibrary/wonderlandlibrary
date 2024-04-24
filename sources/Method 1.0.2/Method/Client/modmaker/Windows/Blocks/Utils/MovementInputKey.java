package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class MovementInputKey extends Block {

    public MovementInputKey() {
        super("MovementInputKey", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("forwardKeyDown");
        ddOptions.add("backKeyDown");
        ddOptions.add("leftKeyDown");
        ddOptions.add("rightKeyDown");
        ddOptions.add("jump");
        ddOptions.add("sneak");
        this.description = "Returns the boolean value of the selected key";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "forwardKeyDown":
                return Minecraft.getMinecraft().player.movementInput.forwardKeyDown;
            case "backKeyDown":
                return Minecraft.getMinecraft().player.movementInput.backKeyDown;
            case "leftKeyDown":
                return Minecraft.getMinecraft().player.movementInput.leftKeyDown;
            case "rightKeyDown":
                return Minecraft.getMinecraft().player.movementInput.rightKeyDown;
            case "jump":
                return Minecraft.getMinecraft().player.movementInput.jump;
            case "sneak":
                return Minecraft.getMinecraft().player.movementInput.sneak;

        }
        return false;
    }


}
