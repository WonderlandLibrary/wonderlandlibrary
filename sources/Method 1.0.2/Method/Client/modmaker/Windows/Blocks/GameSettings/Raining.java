package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;

public class Raining extends Block {

    public Raining() {
        super("Raining", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.BooleanTextEnter, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Boolean));
        ddOptions.add("Rain");
        ddOptions.add("Thunder");
        this.description = "Set world to rain or thunder (Client Side)" + "\n" + "mc.world.getWorldInfo().setRaining(true);" + "\n" + "mc.world.getWorldInfo().setThundering(true);";

    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "Rain":
                Minecraft.getMinecraft().world.getWorldInfo().setRaining(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
            case "Thunder":
                Minecraft.getMinecraft().world.getWorldInfo().setThundering(dragableBlock.local.codeExecuter.solveBoolean(dragableBlock,0, event));
                break;
        }
        super.runCode(dragableBlock, event);
    }

}
