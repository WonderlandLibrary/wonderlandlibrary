package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class SSlider extends DSetting {

    public SSlider(String name) {
        super(name, MainBlockType.Null, BlockObjects.Name, BlockObjects.Words, BlockObjects.NumericalTextEnter, BlockObjects.Words,
                BlockObjects.NumericalTextEnter, BlockObjects.Words, BlockObjects.NumericalTextEnter,
                BlockObjects.Words, BlockObjects.DropDown);
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.typesAccepted.add(typesCollapse(MainBlockType.Numbers));
        this.words[0] = "Default";
        this.words[1] = "Min";
        this.words[2] = "Max";
        this.words[3] = "Int";
        ddOptions.add("True");
        ddOptions.add("False");
    }


    @Override
    public void save(DragableBlock activeBlock) {
        switch (activeBlock.dropDowns.getSelected()) {
            case "True":
                ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module,
                        activeBlock.local.codeExecuter.solveNumerical(activeBlock, 0, null), activeBlock.local.codeExecuter.solveNumerical(activeBlock, 1, null),
                        activeBlock.local.codeExecuter.solveNumerical(activeBlock, 2, null), true));
                break;
            case "False":
                ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module,
                        activeBlock.local.codeExecuter.solveNumerical(activeBlock, 0, null), activeBlock.local.codeExecuter.solveNumerical(activeBlock, 1, null),
                        activeBlock.local.codeExecuter.solveNumerical(activeBlock, 2, null), false));
                break;
        }
    }
}
