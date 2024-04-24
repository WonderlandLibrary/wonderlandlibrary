package Method.Client.modmaker.Windows.Blocks.Settings;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.BlockObjects;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Windows.DragableBlock;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;

public class SCheck extends DSetting {

    public SCheck(String name) {
        super(name, MainBlockType.Null, BlockObjects.Name, BlockObjects.DropDown);

        ddOptions.add("True");
        ddOptions.add("False");
    }

    @Override
    public void save(DragableBlock activeBlock) {
        switch (activeBlock.dropDowns.getSelected()) {
            case "True":
                ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module, true));
                break;
            case "False":
                ((SettingObject) activeBlock).setSetting(new Setting(activeBlock.thisblock.getName(), Main.Maker.module, false));
                break;
        }
        Main.setmgr.remove(((SettingObject) activeBlock).getSetting());
        Main.setmgr.add(((SettingObject) activeBlock).getSetting());
    }


}
