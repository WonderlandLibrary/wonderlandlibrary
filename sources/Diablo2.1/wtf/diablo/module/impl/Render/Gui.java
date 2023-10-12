package wtf.diablo.module.impl.Render;

import lombok.Getter;
import lombok.Setter;
import wtf.diablo.module.Module;
import wtf.diablo.module.data.Category;
import wtf.diablo.module.data.ServerType;
import wtf.diablo.settings.impl.BooleanSetting;
import wtf.diablo.settings.impl.ModeSetting;

@Getter@Setter
public class Gui extends Module {
    public static BooleanSetting clearChat = new BooleanSetting("Clear Chat", true);
    public static ModeSetting guiContainerAnimation = new ModeSetting("Inventory", "Scale", "Slide","Default");
    public static BooleanSetting lowerScoreboard = new BooleanSetting("Lower Scoreboard", true);

    public Gui(){
        super("Gui","Custom GUI", Category.RENDER, ServerType.All);
        addSettings(lowerScoreboard,guiContainerAnimation, clearChat);
    }
}
