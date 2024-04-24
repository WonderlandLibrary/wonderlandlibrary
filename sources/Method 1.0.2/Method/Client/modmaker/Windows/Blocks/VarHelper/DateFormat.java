package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat extends Block {

    public DateFormat() {
        super("DateFormat", MainBlockType.String, Tabs.VarHelper, BlockObjects.Name,BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns the current date in the specified format.";
    }

    @Override
    public Object runCodeObject(DragableBlock dragableBlock, Object event) {
        return new SimpleDateFormat((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock,0, event)).format(new Date());
    }


}
