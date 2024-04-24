package me.crimp.claudius.mod.modules.text;

import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;

public class ToggleMacros
        extends Module {
    public static final ToggleMacros INSTANCE = new ToggleMacros();
    public Setting<Boolean> one = register(new Setting<>("1", true));
    public Setting<Boolean> two = register(new Setting<>("2", true));
    public Setting<Boolean> three = register(new Setting<>("3", false));
    public Setting<String> oneone = register(new Setting<>("1 OnToggleMsg", "", v -> this.one.getValue()));
    public Setting<String> oneoneoff = register(new Setting<>("1 OffToggleMsg", "", v -> this.one.getValue()));
    public Setting<String> twotwo = register(new Setting<>("2 OnToggleMsg", "", v -> this.two.getValue()));
    public Setting<String> twotwooff = register(new Setting<>("2 OffToggleMsg", "", v -> this.two.getValue()));
    public Setting<String> threethree = register(new Setting<>("3 OnToggleMsg", "", v -> this.three.getValue()));
    public Setting<String> threethreeoff = register(new Setting<>("3 OffToggleMsg", "", v -> this.three.getValue()));

    public ToggleMacros() {
        super("ToggleMacros", "When Turned On Do X When Turned Off Do Y", Category.Text, false, false);
    }

    String oneonemsg;
    String oneoneoffmsg;

    String twotwomsg;
    String twotwooffmsg;

    String threethreemsg;
    String threethreeoffmsg;



    @Override
    public void onUpdate() {
        oneonemsg = oneone.getValue().replace("_"," ");
        oneoneoffmsg = oneoneoff.getValue().replace("_"," ");
        twotwomsg = twotwo.getValue().replace("_"," ");
        twotwooffmsg = twotwooff.getValue().replace("_"," ");
        threethreemsg = threethree.getValue().replace("_"," ");
        threethreeoffmsg = threethreeoff.getValue().replace("_"," ");
    }

    @Override
    public void onEnable() {
        if (one.getValue()) {
            mc.player.sendChatMessage(String.valueOf(oneonemsg));
        }
        if (two.getValue()) {
            mc.player.sendChatMessage(String.valueOf(twotwomsg));
        }
        if (three.getValue()) {
            mc.player.sendChatMessage(String.valueOf(threethreemsg));
        }
    }

    @Override
    public void onDisable() {
        if (one.getValue()) {
            mc.player.sendChatMessage(String.valueOf(oneoneoffmsg));
        }
        if (two.getValue()) {
            mc.player.sendChatMessage(String.valueOf(twotwooffmsg));
        }
        if (three.getValue()) {
            mc.player.sendChatMessage(String.valueOf(threethreeoffmsg));
        }
    }
}