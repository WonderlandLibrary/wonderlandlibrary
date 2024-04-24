package Method.Client.modmaker.Windows.Blocks.Entity;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class PlayerModelPart extends Block {

    public PlayerModelPart() {
        super("PlayerModelPart", MainBlockType.Default, Tabs.Entity, BlockObjects.Name, BlockObjects.DropDown);
        for (EnumPlayerModelParts value : EnumPlayerModelParts.values()) {
            ddOptions.add(value.toString());
        }
        this.description = "Switch model part from dropdown   " + "\n" + "mc.gameSettings.switchModelPartEnabled(dropdown)";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.valueOf(dragableBlock.dropDowns.getSelected()));
        super.runCode(dragableBlock, event);
    }

}
