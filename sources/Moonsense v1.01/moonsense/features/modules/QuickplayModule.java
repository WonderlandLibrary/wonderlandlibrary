// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules;

import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import moonsense.event.impl.SCWorldUnloadedEvent;
import moonsense.features.SettingsManager;
import moonsense.features.modules.type.mechanic.FreelookModule;
import moonsense.ui.utils.blur.BlurShader;
import moonsense.features.modules.type.hud.MenuBlurModule;
import moonsense.config.ModuleConfig;
import moonsense.features.quickplay.QuickplayGameMode;
import moonsense.features.quickplay.QuickplayGame;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import moonsense.features.quickplay.ui.QuickplayOption;
import moonsense.event.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import moonsense.features.quickplay.ui.QuickplayMenu;
import org.lwjgl.input.Keyboard;
import moonsense.event.impl.SCClientTickEvent;
import moonsense.enums.ModuleCategory;
import moonsense.utils.KeyBinding;
import java.util.ArrayList;
import java.util.List;
import moonsense.features.quickplay.QuickplayManager;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class QuickplayModule extends SCModule
{
    public static QuickplayModule INSTANCE;
    public final Setting menuKeybind;
    private final QuickplayManager quickplayManager;
    private List<String> recentlyPlayed;
    
    public QuickplayModule() {
        super("Quickplay", "Quickly join your favorite games on Hypixel.");
        this.recentlyPlayed = new ArrayList<String>();
        QuickplayModule.INSTANCE = this;
        this.quickplayManager = new QuickplayManager();
        this.menuKeybind = new Setting(this, "Quickplay Menu Key").setDefault(new KeyBinding(157));
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
    
    @SubscribeEvent
    public void onTick(final SCClientTickEvent event) {
        if (this.mc.currentScreen == null && Keyboard.isKeyDown(((KeyBinding)this.menuKeybind.getObject()).getKeyCode())) {
            this.mc.displayGuiScreen(new QuickplayMenu(this));
        }
    }
    
    public List<QuickplayOption> getRecentlyPlayed() {
        return this.recentlyPlayed.stream().map(fullId -> this.quickplayManager.getGame(fullId.substring(0, fullId.indexOf(46))).getMode(fullId.substring(fullId.indexOf(46) + 1))).collect((Collector<? super Object, ?, List<QuickplayOption>>)Collectors.toList());
    }
    
    public List<QuickplayOption> getGameOptions() {
        return new ArrayList<QuickplayOption>(this.quickplayManager.getGames().values());
    }
    
    public List<QuickplayGame> getGames() {
        return new ArrayList<QuickplayGame>(this.quickplayManager.getGames().values());
    }
    
    public void playGame(final QuickplayGameMode mode) {
        if (this.mc.isSingleplayer()) {
            if (ModuleConfig.INSTANCE.isEnabled(MenuBlurModule.INSTANCE) && MenuBlurModule.INSTANCE.pauseMenuBlur.getBoolean()) {
                BlurShader.INSTANCE.onGuiClose();
            }
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            FreelookModule.INSTANCE.serverEnable();
            SettingsManager.INSTANCE.hitDelayFix.setValue(true);
            new SCWorldUnloadedEvent(this.mc.theWorld).call();
            this.mc.loadWorld(null);
            this.mc.displayGuiScreen(new GuiConnecting(null, this.mc, "mc.hypixel.net", 25565));
            new Thread("Quickplay") {
                @Override
                public void run() {
                    while (Minecraft.getMinecraft().thePlayer == null) {}
                    QuickplayModule.this.mc.thePlayer.sendChatMessage(mode.getCommand());
                    if (!GuiScreen.isShiftKeyDown()) {
                        QuickplayModule.this.mc.displayGuiScreen(null);
                    }
                    QuickplayModule.this.recentlyPlayed.removeIf(mode.getFullId()::equals);
                    QuickplayModule.this.recentlyPlayed.add(0, mode.getFullId());
                    if (QuickplayModule.this.recentlyPlayed.size() > 15) {
                        QuickplayModule.this.recentlyPlayed.remove(QuickplayModule.this.recentlyPlayed.size() - 1);
                    }
                }
            }.start();
            return;
        }
        this.mc.thePlayer.sendChatMessage(mode.getCommand());
        if (!GuiScreen.isShiftKeyDown()) {
            this.mc.displayGuiScreen(null);
        }
        this.recentlyPlayed.removeIf(mode.getFullId()::equals);
        this.recentlyPlayed.add(0, mode.getFullId());
        if (this.recentlyPlayed.size() > 15) {
            this.recentlyPlayed.remove(this.recentlyPlayed.size() - 1);
        }
    }
}
