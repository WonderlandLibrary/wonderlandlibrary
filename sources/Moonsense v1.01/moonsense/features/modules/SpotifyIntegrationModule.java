// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.features.modules;

import moonsense.enums.ModuleCategory;
import moonsense.event.SubscribeEvent;
import net.minecraft.client.gui.GuiScreen;
import moonsense.ui.screen.GuiSpotifyOverlay;
import moonsense.event.impl.SCKeyEvent;
import moonsense.utils.KeyBinding;
import moonsense.settings.Setting;
import moonsense.features.SCModule;

public class SpotifyIntegrationModule extends SCModule
{
    public static SpotifyIntegrationModule INSTANCE;
    public final Setting overlayKeybind;
    public final Setting proceedToNextFavorite;
    public final Setting startResumeKeybind;
    public final Setting favoriteSongKeybind;
    public final Setting favoritesMenuKeybind;
    
    public SpotifyIntegrationModule() {
        super("Spotify Integration", "Control Spotify playback from Minecraft.");
        SpotifyIntegrationModule.INSTANCE = this;
        new Setting(this, "General Options");
        this.overlayKeybind = new Setting(this, "Open Spotify Overlay").setDefault(new KeyBinding(184));
        this.favoriteSongKeybind = new Setting(this, "Favorite Song").setDefault(new KeyBinding(33).ignoreClashes(true));
        this.favoritesMenuKeybind = new Setting(this, "Favorites Menu").setDefault(new KeyBinding(28).ignoreClashes(true));
        new Setting(this, "Player Settings");
        this.proceedToNextFavorite = new Setting(this, "Automatically Play Next Favorite").setDefault(false);
        this.startResumeKeybind = new Setting(this, "Start/Resume").setDefault(new KeyBinding(57).ignoreClashes(true));
    }
    
    @SubscribeEvent
    public void onKeyPress(final SCKeyEvent event) {
        if (event.getKey() == ((KeyBinding)this.overlayKeybind.getObject()).getKeyCode() && this.mc.currentScreen == null) {
            this.mc.displayGuiScreen(new GuiSpotifyOverlay());
        }
    }
    
    @Override
    public ModuleCategory getCategory() {
        return ModuleCategory.ALL;
    }
    
    @Override
    public boolean isNewModule() {
        return true;
    }
}
