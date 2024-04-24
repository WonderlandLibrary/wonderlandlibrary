package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class MineTimer extends Block {

    public MineTimer() {
        super("GetMineTimer", MainBlockType.Numbers, Tabs.Game, BlockObjects.Name, BlockObjects.DropDown);
        ddOptions.add("elapsedTicks");
        ddOptions.add("renderPartialTicks");
        ddOptions.add("elapsedPartialTicks");
        ddOptions.add("tickLength");
        this.description = "Set ingame timer values" + "\n" + "mc.timer.elapsedTicks" + "\n" + "mc.timer.renderPartialTicks" + "\n" + "mc.timer.elapsedPartialTicks" + "\n" + "mc.timer.tickLength";
    }

    @Override
    public double runCodeSolve(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "elapsedTicks":
                return Minecraft.getMinecraft().timer.elapsedTicks;
            case "renderPartialTicks":
                return Minecraft.getMinecraft().timer.renderPartialTicks;
            case "elapsedPartialTicks":
                return Minecraft.getMinecraft().timer.elapsedPartialTicks;
            case "tickLength":
                return Minecraft.getMinecraft().timer.tickLength;
        }
        return 0;
    }

}
