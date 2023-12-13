// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.friend;

import java.util.HashMap;
import java.util.Map;

public class Nicknames
{
    private static Map<String, String> aliases;
    
    public static void addNickname(final String name, final String nick) {
        Nicknames.aliases.put(name, nick);
    }
    
    public static void removeNickname(final String name) {
        Nicknames.aliases.remove(name);
    }
    
    public static String getNickname(final String name) {
        return Nicknames.aliases.get(name);
    }
    
    public static boolean hasNickname(final String name) {
        return Nicknames.aliases.containsKey(name);
    }
    
    public static Map<String, String> getAliases() {
        return Nicknames.aliases;
    }
    
    static {
        Nicknames.aliases = new HashMap<String, String>();
    }
}
