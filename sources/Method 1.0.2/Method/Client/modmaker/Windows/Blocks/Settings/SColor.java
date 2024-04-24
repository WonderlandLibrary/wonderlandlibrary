package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class SColor extends DSetting {

    public SColor(String name) {
        super(name, MainBlockType.Null, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.NumericalTextEnter);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "HUE:";
        this.words[1] = "Sat:";
        this.words[2] = "Bri:";
        this.words[3] = "Alpha:";

    }

    @Override
    public void save(DragableBlock activeBlock) {
        ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module,
                activeBlock.local.codeExecuter.solveNumerical(activeBlock,0, null), activeBlock.local.codeExecuter.solveNumerical(activeBlock,1, null),
                activeBlock.local.codeExecuter.solveNumerical(activeBlock,2, null), activeBlock.local.codeExecuter.solveNumerical(activeBlock,3, null)));

    }

}
