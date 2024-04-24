package Method.Client.modmaker.Windows.Blocks.Utils;

import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;


public class IsModuleEnabled extends Block {

    public IsModuleEnabled() {
        super("IsModuleEnabled", MainBlockType.Boolean, Tabs.Utils, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.description = "Returns true if the module is enabled";
    }

    @Override
    public boolean runCodeBoolean(DragableBlock dragableBlock, Object event) {
        Module mod = ModuleManager.getModuleByName((String) dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, event));
        if (mod != null)
            return mod.isToggled();
        return false;
    }

}
