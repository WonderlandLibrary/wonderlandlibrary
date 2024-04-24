package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class GetMovementInput extends Block {

    public GetMovementInput() {
        super("GetMovementInput", MainBlockType.Numbers, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("moveForward");
        ddOptions.add("moveStrafe");
        this.description = "Gets the movement input of the player" + "\n" + "moveForward" + "\n" + "moveStrafe";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "moveForward":
                return Minecraft.getMinecraft().player.movementInput.moveForward;
            case "moveStrafe":
                return Minecraft.getMinecraft().player.movementInput.moveStrafe;

        }
        return 0;
    }


}
