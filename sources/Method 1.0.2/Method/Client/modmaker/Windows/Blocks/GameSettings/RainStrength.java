package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class RainStrength extends Block {

    public RainStrength() {
        super("RainStrength", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.BooleanTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("Rain");
        ddOptions.add("Thunder");
        this.description = "Set strength of rain or Thunder" + "\n" + "mc.world.getWorldInfo().setRainTime(int);" + "\n" + "mc.world.getWorldInfo().setThunderTime(int)";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Rain":
                Minecraft.getMinecraft().world.getWorldInfo().setRainTime((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
                break;
            case "Thunder":
                Minecraft.getMinecraft().world.getWorldInfo().setThunderTime((int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event));
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
