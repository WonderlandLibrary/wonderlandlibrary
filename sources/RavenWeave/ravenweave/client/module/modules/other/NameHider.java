package ravenweave.client.module.modules.other;

import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.utils.Utils;

public class NameHider extends Module {
    public static DescriptionSetting a;
    public static String n = "ravenb++";
    public static String playerNick = "";

    public NameHider() {
        super("Name Hider", ModuleCategory.other);
        this.registerSetting(a = new DescriptionSetting(Utils.Java.capitalizeWord("command") + ": cname [name]"));
    }

    public static String format(String s) {
        if (mc.thePlayer != null) {
            s = playerNick.isEmpty() ? s.replace(mc.thePlayer.getName(), n) : s.replace(playerNick, n);
        }

        return s;
    }
}
