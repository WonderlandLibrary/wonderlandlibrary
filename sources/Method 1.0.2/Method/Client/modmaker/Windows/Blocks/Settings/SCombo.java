package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class SCombo extends DSetting {

    public SCombo(String name) {
        super(name, MainBlockType.Null, BlockObjects.Name, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.String));
    }

    @Override
    public void save(DragableBlock activeBlock) {
        if (activeBlock.local.codeExecuter.solveObject(activeBlock,0, null) != null)
            if (((String) activeBlock.local.codeExecuter.solveObject(activeBlock,0, null)).contains(",")) {
                String[] strings = ((String) activeBlock.local.codeExecuter.solveObject(activeBlock,0, null)).split(",");
                ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module, strings[0], strings));
            }
    }

}
