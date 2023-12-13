package me.earth.earthhack.impl.services.chat;

import me.earth.earthhack.api.event.bus.EventListener;
import me.earth.earthhack.api.event.bus.SubscriberImpl;
import me.earth.earthhack.impl.event.events.network.DisconnectEvent;
import me.earth.earthhack.impl.util.helpers.Counter;
import me.earth.earthhack.impl.util.text.ChatUtil;
import net.minecraft.util.text.ITextComponent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatManager extends SubscriberImpl
{
    private static final ChatManager INSTANCE = new ChatManager();

    private final Map<Integer, Map<String, Integer>> message_ids = new ConcurrentHashMap<>();
    private final Counter counter = new Counter(1337);

    private ChatManager()
    {
        //TODO: clear map more often
        this.listeners.add(new EventListener<DisconnectEvent>(DisconnectEvent.class)
        {
            @Override
            public void invoke(DisconnectEvent event)
            {
                message_ids.clear();
                counter.reset();
            }
        });
    }

    public static ChatManager getInstance()
    {
        return INSTANCE;
    }

    public void sendDeleteMessage(String message, String uniqueWord, int senderID)
    {
        Integer id = message_ids.computeIfAbsent(senderID, v -> new ConcurrentHashMap<>()).computeIfAbsent(uniqueWord, v -> counter.next());
        ChatUtil.sendMessage(message, id);
    }

    public void deleteMessage(String uniqueWord, int senderID)
    {
        Map<String, Integer> map = message_ids.remove(senderID);
        if (map != null)
        {
            Integer id = map.remove(uniqueWord);
            if (id != null)
            {
                ChatUtil.deleteMessage(id);
            }
        }
    }

    public void sendDeleteComponent(ITextComponent component, String uniqueWord, int senderID)
    {
        Integer id = message_ids.computeIfAbsent(senderID, v -> new ConcurrentHashMap<>()).computeIfAbsent(uniqueWord, v -> counter.next());
        ChatUtil.sendComponent(component, id);
    }

}
