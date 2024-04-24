package Method.Client.modmaker.Windows.Blocks.Logic;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.Contained;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class For extends Block {

    public For() {
        super("For", MainBlockType.Contained, Tabs.Logic, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Array));
    }


    @Override
    public void runCode(DragableBlock dragableBlock, Object event) {
        Object result = dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event);

        if (result instanceof ArrayList) {
            dragableBlock.local.codeExecuter.breakloop = false;
            int stored = 0;
            for (int i = 1; i < 10; i++) {
                if (dragableBlock.local.codeExecuter.loopObj[i] == null) {
                    stored = i;
                    break;
                }
            }

            List<Object> copyOfResult = new ArrayList<>((List<Object>) result);

            for (Object o : copyOfResult) {
                dragableBlock.local.codeExecuter.loopObj[stored] = o;
                ((Contained) dragableBlock).runContainedCode(dragableBlock, event);
                if (dragableBlock.local.codeExecuter.breakloop) {
                    dragableBlock.local.codeExecuter.breakloop = false;
                    dragableBlock.local.codeExecuter.loopObj[stored] = null;
                    break;
                }
            }

            if (stored != 0)
                dragableBlock.local.codeExecuter.loopObj[stored] = null;
            super.runCode(dragableBlock, event);
        } else {
            super.runCode(dragableBlock, event);
        }
    }


}
