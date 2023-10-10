/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import jaco.mp3.player.MP3Player;
import java.net.URL;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiMainMenu;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.ClientTickEvent;
import tk.rektsky.event.impl.ModuleTogglePreEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class Sound
extends Module {
    private static final URL MENU = Sound.class.getClassLoader().getResource("assets/minecraft/rektsky/sound/menu.mp3");
    private static final URL ENABLE = Sound.class.getClassLoader().getResource("assets/minecraft/rektsky/sound/enable.mp3");
    private static final URL DISABLE = Sound.class.getClassLoader().getResource("assets/minecraft/rektsky/sound/disable.mp3");
    public static MP3Player menuPlayer = new MP3Player(MENU);
    public static ArrayList<MP3Player> currentPlayers = new ArrayList();

    public Sound() {
        super("Sound", "Sigma Time!", Category.REKTSKY);
        this.toggle();
    }

    public static void playEnableSound() {
        MP3Player player = new MP3Player(ENABLE);
        player.play();
        player.setRepeat(false);
        currentPlayers.add(player);
    }

    public static void playDisableSound() {
        MP3Player player = new MP3Player(DISABLE);
        player.play();
        player.setRepeat(false);
        currentPlayers.add(player);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof ModuleTogglePreEvent) {
            if (this.mc.theWorld == null || this.mc.thePlayer == null) {
                return;
            }
            if (((ModuleTogglePreEvent)event).isEnabled()) {
                Sound.playEnableSound();
            } else {
                Sound.playDisableSound();
            }
        }
        if (!(event instanceof ClientTickEvent) || this.mc.theWorld != null || !GuiMainMenu.rendered.booleanValue() || !menuPlayer.isStopped() || GuiMainMenu.rendered.booleanValue()) {
            // empty if block
        }
    }
}

