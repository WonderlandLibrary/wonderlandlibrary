package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class FogColor extends Block {

    public FogColor() {
        super("FogColor", MainBlockType.Default, Tabs.Game,BlockObjects.Name, BlockObjects.NumericalTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.description = "Sets the Red Green and Blue fog colors" + "\n" + "mc.entityRenderer.fogColorRed = (float) "
                + "\n" + "mc.entityRenderer.fogColorGreen = (float) " + "\n" + "mc.entityRenderer.fogColorBlue = (float) ";
        ddOptions.add("Red");
        ddOptions.add("Green");
        ddOptions.add("Blue");
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Red":
                Minecraft.getMinecraft().entityRenderer.fogColorRed = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "Green":
                Minecraft.getMinecraft().entityRenderer.fogColorBlue = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "Blue":
                Minecraft.getMinecraft().entityRenderer.fogColorGreen = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
