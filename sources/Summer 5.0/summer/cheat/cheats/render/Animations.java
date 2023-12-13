package summer.cheat.cheats.render;

import java.util.ArrayList;

import summer.Summer;
import summer.base.manager.Selection;
import summer.cheat.guiutil.Setting;
import summer.base.utilities.ChatUtils;
import summer.base.manager.config.Cheats;

public class Animations extends Cheats {

    public static Setting blockAnimation;
    public static Setting speedAni;
    public static Setting itemDistance;
    public static Setting itemSize;

    public Animations() {
        super("Animations", "Sword blockhit animations", Selection.RENDER, true);
    }

    @Override
    public void onSetup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Old");
        options.add("Exhibition");
        options.add("Astolfo");
        options.add("Matt");
        options.add("Summer");
        options.add("1.7");
        options.add("Winter");
        options.add("Geo");
        options.add("Astro");
        options.add("Stella");
        options.add("Remix");
        Summer.INSTANCE.settingsManager.Property(blockAnimation = new Setting("Animation", this, "Exhibition", options));
        Summer.INSTANCE.settingsManager.Property(speedAni = new Setting("Speed Animation", this, 2.50F, .1F, 3.50F, false));
        Summer.INSTANCE.settingsManager.Property(itemDistance = new Setting("Item Distance", this, 1.0F, 1.0F, 2.5F, false));
        Summer.INSTANCE.settingsManager.Property(itemSize = new Setting("Item Size", this, 1.0F, 0.5F, 2.5F, false));
    }

    public void onEnable() {
        toggle();
        ChatUtils.sendMessage("This module doesn't turn on dummy");
    }
}
