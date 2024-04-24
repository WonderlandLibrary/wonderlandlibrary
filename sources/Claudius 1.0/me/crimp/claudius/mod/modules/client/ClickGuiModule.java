package me.crimp.claudius.mod.modules.client;

import me.crimp.claudius.claudius;
import me.crimp.claudius.event.events.ClientEvent;
import me.crimp.claudius.mod.gui.ClickGui;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Bind;
import me.crimp.claudius.mod.setting.Setting;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.input.Keyboard;

import java.awt.*;

import static me.crimp.claudius.claudius.configManager;

public class ClickGuiModule extends Module {
    public static final ClickGuiModule INSTANCE = new ClickGuiModule();
    public Setting<Boolean> customFov = register(new Setting<>("CustomFov", false));
    public Setting<Boolean> ColourSettings = register(new Setting<>("ColourSettings", false));
    public Setting<Integer> fov = register(new Setting<>("Fov", 110, -180, 180));
    public Setting<Integer> red = register(new Setting<Integer>("Red", 99, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> green = register(new Setting<>("Green", 102, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> blue = register(new Setting<>("Blue", 123, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> hoverAlpha = register(new Setting<>("Alpha", 195, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> topRed = register(new Setting<>("topRed", 23, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> topGreen = register(new Setting<>("topGreen", 3, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> topBlue = register(new Setting<>("topBlue", 137, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> topAlpha = register(new Setting<>("topAlpha", 255, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> alpha = register(new Setting<>("HoverAlpha", 137, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> BGRed = register(new Setting<>("BGRed", 253, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> BGGreen = register(new Setting<>("BGGreen", 239, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> BGBlue = register(new Setting<>("BGBlue", 236, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Integer> BGalpha = register(new Setting<>("BGalpha", 150, 0, 255, v -> ColourSettings.getValue()));
    public Setting<Boolean> Rainbow = register(new Setting<>("Rainbow", true, v -> ColourSettings.getValue()));
    public Setting<Boolean> Guimove = register(new Setting<>("ClickGuiMove", true, ""));
    public Setting<Boolean> Cross = register(new Setting<>("Plus/Minus", true, ""));
    public Setting<Boolean> Descriptions = register(new Setting<>("Descriptions", true, ""));

    public ClickGuiModule() {
        super("ClickGui", "Opens the ClickGui", Module.Category.Client, false, false);
        this.bind.setValue(new Bind(Keyboard.KEY_J));
    }

    private static final KeyBinding[] KEYS = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};

    @Override
    public void onUpdate() {
        if (this.customFov.getValue()) ClickGuiModule.mc.gameSettings.setOptionFloatValue(GameSettings.Options.FOV, this.fov.getValue());
        if ((mc.currentScreen instanceof ClickGui &&  this.Guimove.getValue())) {
            for (final KeyBinding keyBinding : KEYS) {
                if (Keyboard.isKeyDown(keyBinding.getKeyCode())) {
                    if (keyBinding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
                        keyBinding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                    }
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
                } else {
                    KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
                }
            }
            if (Rainbow.getValue()) {
                doRainbow();
            }
        }
    }

    public void doRainbow() {
        float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int color_rgb_o = Color.HSBtoRGB(tick_color[0], 0.8f, 0.8f);
        this.red.setValue(color_rgb_o >> 16 & 0xFF);
        this.green.setValue(color_rgb_o >> 8 & 0xFF);
        this.blue.setValue(color_rgb_o & 0xFF);
    }

    @SubscribeEvent
    public void onSettingChange(ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) claudius.colorManager.setColor(this.red.getPlannedValue(), this.green.getPlannedValue(), this.blue.getPlannedValue(), this.hoverAlpha.getPlannedValue());
    }

    @Override
    public void onLogout() {
        configManager.saveConfig(configManager.config.replaceFirst("claudius/", ""));
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(me.crimp.claudius.mod.gui.ClickGui.getClickGui());
    }

    @Override
    public void onLoad() {
        claudius.colorManager.setColor(red.getValue(), green.getValue(), blue.getValue(), hoverAlpha.getValue());
    }

    @Override
    public void onTick() {
        if (!(ClickGuiModule.mc.currentScreen instanceof me.crimp.claudius.mod.gui.ClickGui)) disable();
    }
}

