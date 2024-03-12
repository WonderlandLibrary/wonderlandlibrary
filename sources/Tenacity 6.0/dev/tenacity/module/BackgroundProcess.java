// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module;

import dev.tenacity.module.impl.movement.Flight;
import dev.tenacity.event.impl.game.WorldEvent;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMultiplayer;
import dev.tenacity.ui.mainmenu.CustomMainMenu;
import dev.tenacity.event.impl.game.TickEvent;
import java.util.function.Predicate;
import java.util.Arrays;
import dev.tenacity.module.impl.render.Statistics;
import net.minecraft.util.StringUtils;
import dev.tenacity.event.impl.player.ChatReceivedEvent;
import dev.tenacity.config.DragManager;
import dev.tenacity.event.impl.game.GameCloseEvent;
import java.util.Iterator;
import dev.tenacity.event.impl.game.KeyPressEvent;
import dev.tenacity.Tenacity;
import dev.tenacity.module.impl.player.Scaffold;
import dev.tenacity.utils.Utils;
import dev.tenacity.event.ListenerAdapter;

public class BackgroundProcess extends ListenerAdapter implements Utils
{
    private final Scaffold scaffold;
    
    public BackgroundProcess() {
        this.scaffold = (Scaffold)Tenacity.INSTANCE.getModuleCollection().get(Scaffold.class);
    }
    
    @Override
    public void onKeyPressEvent(final KeyPressEvent event) {
        for (final Module module : Tenacity.INSTANCE.getModuleCollection().getModules()) {
            if (module.getKeybind().getCode() == event.getKey()) {
                module.toggle();
            }
        }
    }
    
    @Override
    public void onGameCloseEvent(final GameCloseEvent event) {
        Tenacity.INSTANCE.getConfigManager().saveDefaultConfig();
        DragManager.saveDragData();
    }
    
    @Override
    public void onChatReceivedEvent(final ChatReceivedEvent event) {
        if (BackgroundProcess.mc.thePlayer == null) {
            return;
        }
        final String message = event.message.getUnformattedText();
        final String strippedMessage = StringUtils.stripControlCodes(message);
        final String messageStr = event.message.toString();
        if (!strippedMessage.contains(":") && Arrays.stream(Statistics.KILL_TRIGGERS).anyMatch(strippedMessage.replace(BackgroundProcess.mc.thePlayer.getName(), "*")::contains)) {
            ++Statistics.killCount;
        }
        else if (messageStr.contains("ClickEvent{action=RUN_COMMAND, value='/play ") || messageStr.contains("Want to play again?")) {
            ++Statistics.gamesPlayed;
            if (messageStr.contains("You died!")) {
                ++Statistics.deathCount;
            }
        }
    }
    
    @Override
    public void onTickEvent(final TickEvent event) {
        if (Statistics.endTime == -1L && ((!BackgroundProcess.mc.isSingleplayer() && BackgroundProcess.mc.getCurrentServerData() == null) || BackgroundProcess.mc.currentScreen instanceof CustomMainMenu || BackgroundProcess.mc.currentScreen instanceof GuiMultiplayer || BackgroundProcess.mc.currentScreen instanceof GuiDisconnected)) {
            Statistics.endTime = System.currentTimeMillis();
        }
        else if (Statistics.endTime != -1L && (BackgroundProcess.mc.isSingleplayer() || BackgroundProcess.mc.getCurrentServerData() != null)) {
            Statistics.reset();
        }
    }
    
    @Override
    public void onShaderEvent(final ShaderEvent event) {
        if (BackgroundProcess.mc.thePlayer != null) {
            this.scaffold.renderCounterBlur();
        }
    }
    
    @Override
    public void onRender2DEvent(final Render2DEvent event) {
        if (BackgroundProcess.mc.thePlayer != null) {
            this.scaffold.renderCounter();
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        if (event instanceof WorldEvent.Load) {
            Flight.hiddenBlocks.clear();
        }
    }
}
