package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class GetHandProgress extends Block {

    public GetHandProgress() {
        super("GetHandProgress", MainBlockType.Numbers, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("OffHand");
        ddOptions.add("MainHand");
        this.description = "Returns Equipped progress of Player" + "\n" + "mc.entityRenderer.itemRenderer.equippedProgressMainHand" + "\n" + "mc.entityRenderer.itemRenderer.equippedProgressOffHand";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "OffHand":
                return Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgressMainHand;

            case "MainHand":
                return Minecraft.getMinecraft().entityRenderer.itemRenderer.equippedProgressOffHand;

        }
        return 0;
    }

}
