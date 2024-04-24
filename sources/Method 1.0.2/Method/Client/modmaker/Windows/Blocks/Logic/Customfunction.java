package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.Headers;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Event;

public class Customfunction extends Block {


    public Customfunction() {
        super("Customfunction", MainBlockType.Default, Tabs.Logic, BlockObjects.Name, BlockObjects.DropDown);

        this.description = "Jump to a custom function See Headers";
        ddOptions.add("0");
        ddOptions.add("1");
        ddOptions.add("2");
        ddOptions.add("3");
        ddOptions.add("4");
        ddOptions.add("5");
        ddOptions.add("6");
        ddOptions.add("7");
        ddOptions.add("8");
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Object tempevent = event;
        if (event instanceof Packet)
            tempevent = null;
        switch (dragableBlock.dropDowns.getSelected()) {
            case "0":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader0, (Event) tempevent);
                break;
            case "1":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader1, (Event) tempevent);
                break;
            case "2":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader2, (Event) tempevent);
                break;
            case "3":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader3, (Event) tempevent);
                break;
            case "4":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader4, (Event) tempevent);
                break;
            case "5":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader5, (Event) tempevent);
                break;
            case "6":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader6, (Event) tempevent);
                break;
            case "7":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader7, (Event) tempevent);
                break;
            case "8":
                dragableBlock.local.codeExecuter.headerRun(Headers.CustomFunctionHeader8, (Event) tempevent);
                break;
        }
        super.runCode(dragableBlock, event);
    }


}
