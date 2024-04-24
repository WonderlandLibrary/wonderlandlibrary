
package Method.Client.module.ModMaker;

import Method.Client.Main;
import Method.Client.managers.Setting;
import Method.Client.modmaker.MainBlockType;
import Method.Client.modmaker.Windows.Blocks.Settings.DSetting;
import Method.Client.modmaker.Windows.DragableBlockTypes.SettingObject;
import Method.Client.module.Category;
import Method.Client.module.Module;
import Method.Client.module.ModuleManager;
import org.lwjgl.input.Keyboard;

public class AddModule extends Module {
    public AddModule() {
        super("AddModule", Keyboard.KEY_NONE, Category.MAKER, "Add New Module");
    }


    @Override
    public void toggle() {
        String name = "Custom" + (int) System.currentTimeMillis() / 1000;
        Module temp = new Module(name, Keyboard.KEY_NONE, Category.MAKER, "Custom");
        ModuleManager.addModule(temp);
        Main.Maker.setModule(temp);
        SettingObject setting = new SettingObject(new DSetting("Edit Module", MainBlockType.Null), Main.Maker);
        setting.visible = false;
        setting.setSetting(new Setting("Edit Module", temp, Main.Maker));
        Main.setmgr.add(setting.setting);
        Main.Maker.needsUpdate = true;
        Main.Maker.module.ActiveBlocks.add(setting);

        mc.displayGuiScreen(Main.Maker);
    }

    @Override
    public void onDisable() {
        this.toggle();
    }
}
