package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class ArmPos extends Block {

    public ArmPos() {
        super("ArmPos", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("ArmPitch");
        ddOptions.add("ArmYaw");
        this.description = "Sets the arm position of the player ( Client Only )"
                + "\n mc.player.renderArm(Pitch / Yaw)";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "ArmPitch":
                Minecraft.getMinecraft().player.renderArmPitch = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "ArmYaw":
                Minecraft.getMinecraft().player.renderArmYaw = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
