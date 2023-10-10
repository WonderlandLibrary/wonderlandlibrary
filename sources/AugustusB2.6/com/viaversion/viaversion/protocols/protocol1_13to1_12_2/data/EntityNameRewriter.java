// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.data;

import java.util.HashMap;
import java.util.Map;

public class EntityNameRewriter
{
    private static final Map<String, String> entityNames;
    
    private static void reg(final String past, final String future) {
        EntityNameRewriter.entityNames.put("minecraft:" + past, "minecraft:" + future);
    }
    
    public static String rewrite(final String entName) {
        String entityName = EntityNameRewriter.entityNames.get(entName);
        if (entityName != null) {
            return entityName;
        }
        entityName = EntityNameRewriter.entityNames.get("minecraft:" + entName);
        if (entityName != null) {
            return entityName;
        }
        return entName;
    }
    
    static {
        entityNames = new HashMap<String, String>();
        reg("commandblock_minecart", "command_block_minecart");
        reg("ender_crystal", "end_crystal");
        reg("evocation_fangs", "evoker_fangs");
        reg("evocation_illager", "evoker");
        reg("eye_of_ender_signal", "eye_of_ender");
        reg("fireworks_rocket", "firework_rocket");
        reg("illusion_illager", "illusioner");
        reg("snowman", "snow_golem");
        reg("villager_golem", "iron_golem");
        reg("vindication_illager", "vindicator");
        reg("xp_bottle", "experience_bottle");
        reg("xp_orb", "experience_orb");
    }
}
