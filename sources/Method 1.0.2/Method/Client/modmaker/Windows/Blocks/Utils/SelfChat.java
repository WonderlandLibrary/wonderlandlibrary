package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.utils.visual.ChatUtils;

public class SelfChat extends Block {

    public SelfChat() {
        super("SelfChat", MainBlockType.Default, Tabs.Utils, BlockObjects.Name, BlockObjects.DropDown, BlockObjects.NumericalTextEnter);
        ddOptions.add("message");
        ddOptions.add("error");
        ddOptions.add("warning");
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Sends a message to the chat";
    }

    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        switch (dragableBlock.dropDowns.getSelected()) {
            case "message":
                ChatUtils.message((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
                break;
            case "error":
                ChatUtils.error((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
                break;
            case "warning":
                ChatUtils.warning((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event));
                break;

        }
        super.runCode(dragableBlock, event);
    }

}
