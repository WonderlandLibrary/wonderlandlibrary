package appu26j.events.chat;

import appu26j.events.Event;
import net.minecraft.util.IChatComponent;

public class EventChat2 extends Event
{
    private IChatComponent message;
    private final int chatID;
    
    public EventChat2(IChatComponent message, int chatID)
    {
        this.message = message;
        this.chatID = chatID;
    }
    
    public IChatComponent getMessage()
    {
        return this.message;
    }
    
    public int getChatID()
    {
        return this.chatID;
    }
    
    public void appendText(String text)
    {
        this.message.appendText(text);
    }
}
