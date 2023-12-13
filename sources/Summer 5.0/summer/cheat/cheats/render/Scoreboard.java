package summer.cheat.cheats.render;

import summer.Summer;
import summer.base.manager.config.Cheats;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.utilities.ChatUtils;

public class Scoreboard extends Cheats {

    public static Setting height;
    public static Setting hide;
    public static Setting drawShadow;
    public static Setting transparent;
    public static Setting hideNumbers;

    public Scoreboard() {
        super("Scoreboard", "", Selection.RENDER);
        Summer.INSTANCE.settingsManager.Property(height = new Setting("Height", this, 70, 1, 138, false));
        Summer.INSTANCE.settingsManager.Property(hide = new Setting("Hide", this, false));
        Summer.INSTANCE.settingsManager.Property(drawShadow = new Setting("Draw Shadow", this, false));
        Summer.INSTANCE.settingsManager.Property(transparent = new Setting("Transparent", this, false));
        Summer.INSTANCE.settingsManager.Property(hideNumbers = new Setting("Hide Numbers", this, false));
    }

    public void onEnable() {
        toggle();
        ChatUtils.sendMessage("This module doesn't turn on dummy");
    }
}
