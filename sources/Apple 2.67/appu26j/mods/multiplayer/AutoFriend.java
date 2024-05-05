package appu26j.mods.multiplayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.eventbus.Subscribe;

import appu26j.Apple;
import appu26j.events.chat.EventChat;
import appu26j.events.chat.EventChat2;
import appu26j.interfaces.ModInterface;
import appu26j.mods.Category;
import appu26j.mods.Mod;
import appu26j.mods.visuals.Chat;
import appu26j.utils.ServerUtil;

@ModInterface(name = "Auto Friend", description = "Automatically accepts friend requests from others in Hypixel.", category = Category.MULTIPLAYER)
public class AutoFriend extends Mod
{
    private Pattern pattern = Pattern.compile("§m-----------------------------------------------------Friend request from (?<name>.+)\\[ACCEPT\\] - \\[DENY\\] - \\[IGNORE\\].*");
    private Chat chat;
    
    @Override
    public void onEnable()
    {
        super.onEnable();
        
        if (this.chat == null)
        {
            this.chat = (Chat) Apple.CLIENT.getModsManager().getMod("Chat");
        }
    }
    
    @Subscribe
    public void onEventChat(EventChat e)
    {
        if (this.allowUsingTheMainMethod())
        {
            if (ServerUtil.isPlayerOnHypixel())
            {
                String message = e.getMessage().getUnformattedText().replace("\n", "");
                Matcher matcher = this.pattern.matcher(message);
                
                if (matcher.matches())
                {
                    String name = matcher.group("name");
                    
                    if (name.startsWith("["))
                    {
                        name = name.substring(name.indexOf("] ") + 2);
                    }
                    
                    this.mc.thePlayer.sendChatMessage("/friend accept " + name);
                }
            }
        }
    }
    
    @Subscribe
    public void onEventChat2(EventChat2 e)
    {
        if (!this.allowUsingTheMainMethod())
        {
            if (ServerUtil.isPlayerOnHypixel())
            {
                String message = e.getMessage().getUnformattedText().replace("\n", "");
                Matcher matcher = this.pattern.matcher(message);
                
                if (matcher.matches())
                {
                    String name = matcher.group("name");
                    
                    if (name.startsWith("["))
                    {
                        name = name.substring(name.indexOf("] ") + 2);
                    }
                    
                    this.mc.thePlayer.sendChatMessage("/friend accept " + name);
                }
            }
        }
    }
    
    public boolean allowUsingTheMainMethod()
    {
        return !(this.chat.isEnabled() && this.chat.getSetting("Stack Messages").getCheckBoxValue());
    }
}
