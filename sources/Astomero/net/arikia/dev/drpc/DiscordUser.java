package net.arikia.dev.drpc;

import com.sun.jna.*;
import java.util.*;

public class DiscordUser extends Structure
{
    public String userId;
    public String username;
    public String discriminator;
    public String avatar;
    
    public List<String> getFieldOrder() {
        return Arrays.asList("userId", "username", "discriminator", "avatar");
    }
}
