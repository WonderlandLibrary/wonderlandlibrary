package me.dev.legacy.features.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.dev.legacy.Legacy;
import me.dev.legacy.event.events.ClientEvent;
import me.dev.legacy.features.command.Command;
import me.dev.legacy.features.gui.OyVeyGui;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.Util;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClickGui extends Module {
    private static ClickGui INSTANCE = new ClickGui();
    public Setting<String> prefix = register(new Setting<String>("Prefix", "."));
    public Setting<Boolean> customFov = register(new Setting<Boolean>("CustomFov", false));
    public Setting<Float> fov = register(new Setting<Float>("Fov", Float.valueOf(150.0f), Float.valueOf(-180.0f), Float.valueOf(180.0f)));
    public Setting<Boolean> gears = register(new Setting("Gears", Boolean.valueOf(false), "draws gears"));
    public Setting<Integer> red = register(new Setting<Integer>("Red", 210, 0, 255));
    public Setting<Integer> green = register(new Setting<Integer>("Green", 130, 0, 255));
    public Setting<Integer> blue = register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> hoverAlpha = register(new Setting<Integer>("Alpha", 180, 0, 255));
    public Setting<Integer> alpha = register(new Setting<Integer>("HoverAlpha", 240, 0, 255));
    public Setting<Boolean> rainbow = register(new Setting<Boolean>("Rainbow", false));
    public Setting<rainbowMode> rainbowModeHud = register(new Setting<Object>("HRainbowMode", rainbowMode.Static, v -> rainbow.getValue()));
    public Setting<rainbowModeArray> rainbowModeA = register(new Setting<Object>("ARainbowMode", rainbowModeArray.Static, v -> rainbow.getValue()));
    public Setting<Integer> rainbowHue = register(new Setting<Object>("Delay", Integer.valueOf(240), Integer.valueOf(0), Integer.valueOf(600), v -> rainbow.getValue()));
    public Setting<Float> rainbowBrightness = register(new Setting<Object>("Brightness ", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Float> rainbowSaturation = register(new Setting<Object>("Saturation", Float.valueOf(150.0f), Float.valueOf(1.0f), Float.valueOf(255.0f), v -> rainbow.getValue()));
    public Setting<Boolean> colorSync = register(new Setting("ColorSync", false));

    public Setting<Integer> startcolorred = register(new Setting<Integer>("StartColorRed", 210, 0, 255));
    public Setting<Integer> startcolorgreen = register(new Setting<Integer>("StartColorGreen", 210, 0, 255));
    public Setting<Integer> startcolorblue = register(new Setting<Integer>("StartColorBlue", 210, 0, 255));
    public Setting<Integer> endcolorred = register(new Setting<Integer>("EndColorRed", 210, 0, 255));
    public Setting<Integer> endcolorgreen = register(new Setting<Integer>("EndColorGreen", 210, 0, 255));
    public Setting<Integer> endcolorblue = register(new Setting<Integer>("EndColorBlue", 210, 0, 255));
    public Setting<Integer> fontcolor = register(new Setting<Integer>("FontColor", 210, 0, 255));
    public Setting<Boolean> outline = register(new Setting<Boolean>("Outline", false));
    public Setting<Integer> testcolorr = register(new Setting<Integer>("TestColorRed", 210, 0, 255));
    public Setting<Integer> testcolorgreen = register(new Setting<Integer>("TestColorGreen", 210, 0, 255));
    public Setting<Integer> testcolorblue = register(new Setting<Integer>("TestColorBlue", 210, 0, 255));
    public Setting<Integer> bordcolorred = register(new Setting<Integer>("BordColorRed", 210, 0, 255));
    public Setting<Integer> bordcolorgreen = register(new Setting<Integer>("BordColorGreen", 210, 0, 255));
    public Setting<Integer> bordcolorblue = register(new Setting<Integer>("BordColorBlue", 210, 0, 255));

    private OyVeyGui click;

    public ClickGui() {
        super("ClickGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        setInstance();
    }

    public static ClickGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGui();
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public void onUpdate() {
        if (customFov.getValue().booleanValue()) {
            ClickGui.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, fov.getValue().floatValue()); }
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(prefix)) {
                Legacy.commandManager.setPrefix(prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + Legacy.commandManager.getPrefix());
            }
            Legacy.colorManager.setColor(red.getPlannedValue(), green.getPlannedValue(), blue.getPlannedValue(), hoverAlpha.getPlannedValue());
        }
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(OyVeyGui.getClickGui());
    }

    @Override
    public void onLoad() {
        Legacy.colorManager.setColor(red.getValue(), green.getValue(), blue.getValue(), hoverAlpha.getValue());
        Legacy.commandManager.setPrefix(prefix.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGui.mc.currentScreen instanceof OyVeyGui)) {
            disable();
        }
    }

    public enum rainbowModeArray {
        Static,
        Up

    }

    public enum rainbowMode {
        Static,
        Sideway

    }
}

