package tech.atani.client.feature.module.impl.chat;

import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.listener.event.game.GameStartEvent;
import tech.atani.client.listener.radbus.Listen;

@ModuleData(name = "AutoGL", description = "Automatically sends good luck messages", category = Category.CHAT)
public class AutoGL extends Module {

    @Listen
    public void onGameStart(GameStartEvent gameStartEvent) {
        mc.thePlayer.sendChatMessage("gl hf");
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

}
