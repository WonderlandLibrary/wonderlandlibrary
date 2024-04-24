package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class HorseJumpPower extends Block {

    public HorseJumpPower() {
        super("HorseJumpPower", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);

        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("horseJumpPower");
        ddOptions.add("horseJumpPowerCounter");
        this.description = "Sets Horse jump power (On Player)   " + "\n" + "mc.player.horseJumpPower = " + "\n" + "mc.player.horseJumpPowerCounter = ";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {

        switch (dragableBlock.dropDowns.getSelected()) {
            case "horseJumpPower":
                Minecraft.getMinecraft().player.horseJumpPower = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "horseJumpPowerCounter":
                Minecraft.getMinecraft().player.horseJumpPowerCounter = (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;

        }
        super.runCode(dragableBlock, event);
    }

}
