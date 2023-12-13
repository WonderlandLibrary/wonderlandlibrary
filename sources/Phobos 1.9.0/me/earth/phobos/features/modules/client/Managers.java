package me.earth.phobos.features.modules.client;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.ClientEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.TextUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Managers
        extends Module {
    private static Managers INSTANCE = new Managers();
    public Setting<Boolean> betterFrames = this.register(new Setting<Boolean>("BetterMaxFPS", false));
    public Setting<String> commandBracket = this.register(new Setting<String>("Bracket", "<"));
    public Setting<String> commandBracket2 = this.register(new Setting<String>("Bracket2", ">"));
    public Setting<String> command = this.register(new Setting<String>("Command", "Phobos.eu"));
    public Setting<Boolean> rainbowPrefix = this.register(new Setting<Boolean>("RainbowPrefix", false));
    public Setting<TextUtil.Color> bracketColor = this.register(new Setting<TextUtil.Color>("BColor", TextUtil.Color.BLUE));
    public Setting<TextUtil.Color> commandColor = this.register(new Setting<TextUtil.Color>("CColor", TextUtil.Color.BLUE));
    public Setting<Integer> betterFPS = this.register(new Setting<Object>("MaxFPS", Integer.valueOf(300), Integer.valueOf(30), Integer.valueOf(1000), v -> this.betterFrames.getValue()));
    public Setting<Boolean> potions = this.register(new Setting<Boolean>("Potions", true));
    public Setting<Integer> textRadarUpdates = this.register(new Setting<Integer>("TRUpdates", 500, 0, 1000));
    public Setting<Integer> respondTime = this.register(new Setting<Integer>("SeverTime", 500, 0, 1000));
    public Setting<Integer> moduleListUpdates = this.register(new Setting<Integer>("ALUpdates", 1000, 0, 1000));
    public Setting<Float> holeRange = this.register(new Setting<Float>("HoleRange", Float.valueOf(6.0f), Float.valueOf(1.0f), Float.valueOf(256.0f)));
    public Setting<Integer> holeUpdates = this.register(new Setting<Integer>("HoleUpdates", 100, 0, 1000));
    public Setting<Integer> holeSync = this.register(new Setting<Integer>("HoleSync", 10000, 1, 10000));
    public Setting<Boolean> safety = this.register(new Setting<Boolean>("SafetyPlayer", false));
    public Setting<Integer> safetyCheck = this.register(new Setting<Integer>("SafetyCheck", 50, 1, 150));
    public Setting<Integer> safetySync = this.register(new Setting<Integer>("SafetySync", 250, 1, 10000));
    public Setting<ThreadMode> holeThread = this.register(new Setting<ThreadMode>("HoleThread", ThreadMode.WHILE));
    public Setting<Boolean> speed = this.register(new Setting<Boolean>("Speed", true));
    public Setting<Boolean> oneDot15 = this.register(new Setting<Boolean>("1.15", false));
    public Setting<Boolean> tRadarInv = this.register(new Setting<Boolean>("TRadarInv", true));
    public Setting<Boolean> unfocusedCpu = this.register(new Setting<Boolean>("UnfocusedCPU", false));
    public Setting<Integer> cpuFPS = this.register(new Setting<Object>("UnfocusedFPS", Integer.valueOf(60), Integer.valueOf(1), Integer.valueOf(60), v -> this.unfocusedCpu.getValue()));
    public Setting<Integer> baritoneTimeOut = this.register(new Setting<Integer>("Baritone", 5, 1, 20));
    public Setting<Boolean> oneChunk = this.register(new Setting<Boolean>("OneChunk", false));

    public Managers() {
        super("Management", "ClientManagement", Module.Category.CLIENT, false, false, true);
        this.setInstance();
    }

    public static Managers getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Managers();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onLoad() {
        Phobos.commandManager.setClientMessage(this.getCommandMessage());
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2) {
            if (this.oneChunk.getPlannedValue().booleanValue()) {
                Managers.mc.gameSettings.renderDistanceChunks = 1;
            }
            if (event.getSetting() != null && this.equals(event.getSetting().getFeature())) {
                if (event.getSetting().equals(this.holeThread)) {
                    Phobos.holeManager.settingChanged();
                }
                Phobos.commandManager.setClientMessage(this.getCommandMessage());
            }
        }
    }

    public String getCommandMessage() {
        if (this.rainbowPrefix.getPlannedValue().booleanValue()) {
            StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
            stringBuilder.insert(0, "\u00a7+");
            stringBuilder.append("\u00a7r");
            return stringBuilder.toString();
        }
        return TextUtil.coloredString(this.commandBracket.getPlannedValue(), this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command.getPlannedValue(), this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2.getPlannedValue(), this.bracketColor.getPlannedValue());
    }

    public String getRainbowCommandMessage() {
        StringBuilder stringBuilder = new StringBuilder(this.getRawCommandMessage());
        stringBuilder.insert(0, "\u00a7+");
        stringBuilder.append("\u00a7r");
        return stringBuilder.toString();
    }

    public String getRawCommandMessage() {
        return this.commandBracket.getValue() + this.command.getValue() + this.commandBracket2.getValue();
    }

    public enum ThreadMode {
        POOL,
        WHILE,
        NONE

    }
}

