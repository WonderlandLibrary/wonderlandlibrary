package Method.Client.modmaker.Windows.Blocks.VarHelper;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Tabs;
import Method.Client.modmaker.Windows.Block;
import Method.Client.modmaker.Windows.DragableBlock;

public class ModeDependant extends Block {

    public ModeDependant() {
        super("ModeDependant", MainBlockType.Null, Tabs.VarHelper, BlockObjects.Name,
                BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
        this.words[0] = "Depends on";
        this.words[1] = "Selected";
        this.description = "Sets the dependancy of the setting";
    }

    @Override
    public void finalInit(DragableBlock dragableBlock) {
        Setting child = null;
        for (Setting setting : Main.setmgr.getSettingsByMod(dragableBlock.local)) {
            if (setting.getName().equalsIgnoreCase((String) (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 0, null)))) {
                child = setting;
                break;
            }
        }
        Setting parent = null;
        for (Setting setting : Main.setmgr.getSettingsByMod(dragableBlock.local)) {
            if (setting.getName().equalsIgnoreCase((String) (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 1, null)))) {
                parent = setting;
                break;
            }
        }
        if (parent == null || child == null) return;

        child.setDependant(parent);
        child.setSelected((String) (dragableBlock.local.codeExecuter.solveObject(dragableBlock, 2, null)));

    }


}
