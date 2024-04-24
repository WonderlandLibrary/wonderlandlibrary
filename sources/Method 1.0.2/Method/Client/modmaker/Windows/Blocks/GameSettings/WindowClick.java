package Method.Client.modmaker.Windows.Blocks.GameSettings;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;

public class WindowClick extends Block {

    public WindowClick() {
        super("WindowClick", MainBlockType.Default, Tabs.Game, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter
                , BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.ClickType));
        this.typesAccepted.add(typesCollapse(MainBlockType.Entity));
        this.words[0] = "WindowId";
        this.words[1] = "Slot";
        this.words[2] = "ClickType";
        this.words[3] = "Entity";
        this.description = "Click in a window in a slot" + "\n" + "You may also want CPacketClickWindow"
                + "\n" + "mc.playerController.windowClick(WindowId, Slot, 0, ClickType, Entity)" + "\n" + "NOTE IF <9 WE ADD 36 TO IT";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {

        Minecraft.getMinecraft().playerController.windowClick(
                (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 0, event),
                (int) dragableBlock.local.codeExecuter.solveNumerical(dragableBlock, 1, event),
                0,
                (ClickType) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, event),
                (EntityPlayer) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 3, event));

        super.runCode(dragableBlock, event);
    }

}
