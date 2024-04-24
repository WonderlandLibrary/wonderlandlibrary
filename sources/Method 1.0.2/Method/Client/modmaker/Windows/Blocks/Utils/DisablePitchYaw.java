package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.proxy.Overrides.PitchYawHelper;
import net.minecraft.client.Minecraft;

public class DisablePitchYaw extends Block {

    public DisablePitchYaw() {
        super("DisablePitchYaw", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.BooleanTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        ddOptions.add("Pitch");
        ddOptions.add("Yaw");
    this.description = "Disable mouse updates for Pitch or Yaw";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Pitch":
                ((PitchYawHelper) Minecraft.getMinecraft().mouseHelper).Pitch = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
                break;
            case "Yaw":
                ((PitchYawHelper) Minecraft.getMinecraft().mouseHelper).Yaw = dragableBlock.local.codeExecuter.solveBoolean(dragableBlock, 0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }


}
