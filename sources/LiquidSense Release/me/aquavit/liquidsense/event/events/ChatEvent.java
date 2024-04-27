package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class ChatEvent extends CancellableEvent {
    private IChatComponent component;
    private List<ChatLine> chatLineId;

    public IChatComponent getComponent() {
        return this.component;
    }

    public List<ChatLine> getChatLineId() {
        return this.chatLineId;
    }

    public void setChatLineId(List<ChatLine> chatLineId) {
        this.chatLineId = chatLineId;
    }

    public ChatEvent(IChatComponent component, List<ChatLine> chatLineId) {
        this.component = component;
        this.chatLineId = chatLineId;
    }
}
