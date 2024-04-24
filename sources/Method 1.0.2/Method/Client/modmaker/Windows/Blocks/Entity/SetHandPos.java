package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class SetHandPos extends Block {

    public SetHandPos() {
        super("HandPos", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        ddOptions.add("OffHand");
        ddOptions.add("MainHand");
        this.description = "Sets Equipped progress of Player" + "\n" + "mc.entityRenderer.itemRenderer.equippedProgressMainHand" + "\n" + "mc.entityRenderer.itemRenderer.equippedProgressOffHand";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "OffHand":
                Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgressMainHand = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
            case "MainHand":
                Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgressOffHand = (float) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock,0, event);
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
