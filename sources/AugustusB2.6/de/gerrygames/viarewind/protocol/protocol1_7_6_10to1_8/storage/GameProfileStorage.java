// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import com.viaversion.viaversion.util.ChatColorUtil;
import de.gerrygames.viarewind.utils.ChatUtil;
import com.viaversion.viaversion.api.minecraft.item.DataItem;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.Tag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.ListTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.StringTag;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashMap;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.UUID;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StoredObject;

public class GameProfileStorage extends StoredObject
{
    private Map<UUID, GameProfile> properties;
    
    public GameProfileStorage(final UserConnection user) {
        super(user);
        this.properties = new HashMap<UUID, GameProfile>();
    }
    
    public GameProfile put(final UUID uuid, final String name) {
        final GameProfile gameProfile = new GameProfile(uuid, name);
        this.properties.put(uuid, gameProfile);
        return gameProfile;
    }
    
    public void putProperty(final UUID uuid, final Property property) {
        this.properties.computeIfAbsent(uuid, profile -> new GameProfile(uuid, null)).properties.add(property);
    }
    
    public void putProperty(final UUID uuid, final String name, final String value, final String signature) {
        this.putProperty(uuid, new Property(name, value, signature));
    }
    
    public GameProfile get(final UUID uuid) {
        return this.properties.get(uuid);
    }
    
    public GameProfile get(String name, final boolean ignoreCase) {
        if (ignoreCase) {
            name = name.toLowerCase();
        }
        for (final GameProfile profile : this.properties.values()) {
            if (profile.name == null) {
                continue;
            }
            final String n = ignoreCase ? profile.name.toLowerCase() : profile.name;
            if (n.equals(name)) {
                return profile;
            }
        }
        return null;
    }
    
    public List<GameProfile> getAllWithPrefix(String prefix, final boolean ignoreCase) {
        if (ignoreCase) {
            prefix = prefix.toLowerCase();
        }
        final ArrayList<GameProfile> profiles = new ArrayList<GameProfile>();
        for (final GameProfile profile : this.properties.values()) {
            if (profile.name == null) {
                continue;
            }
            final String n = ignoreCase ? profile.name.toLowerCase() : profile.name;
            if (!n.startsWith(prefix)) {
                continue;
            }
            profiles.add(profile);
        }
        return profiles;
    }
    
    public GameProfile remove(final UUID uuid) {
        return this.properties.remove(uuid);
    }
    
    public static class GameProfile
    {
        public String name;
        public String displayName;
        public int ping;
        public UUID uuid;
        public List<Property> properties;
        public int gamemode;
        
        public GameProfile(final UUID uuid, final String name) {
            this.properties = new ArrayList<Property>();
            this.gamemode = 0;
            this.name = name;
            this.uuid = uuid;
        }
        
        public Item getSkull() {
            final CompoundTag tag = new CompoundTag();
            final CompoundTag ownerTag = new CompoundTag();
            tag.put("SkullOwner", ownerTag);
            ownerTag.put("Id", new StringTag(this.uuid.toString()));
            final CompoundTag properties = new CompoundTag();
            ownerTag.put("Properties", properties);
            final ListTag textures = new ListTag(CompoundTag.class);
            properties.put("textures", textures);
            for (final Property property : this.properties) {
                if (property.name.equals("textures")) {
                    final CompoundTag textureTag = new CompoundTag();
                    textureTag.put("Value", new StringTag(property.value));
                    if (property.signature != null) {
                        textureTag.put("Signature", new StringTag(property.signature));
                    }
                    textures.add(textureTag);
                }
            }
            return new DataItem(397, (byte)1, (short)3, tag);
        }
        
        public String getDisplayName() {
            String displayName = (this.displayName == null) ? this.name : this.displayName;
            if (displayName.length() > 16) {
                displayName = ChatUtil.removeUnusedColor(displayName, 'f');
            }
            if (displayName.length() > 16) {
                displayName = ChatColorUtil.stripColor(displayName);
            }
            if (displayName.length() > 16) {
                displayName = displayName.substring(0, 16);
            }
            return displayName;
        }
        
        public void setDisplayName(final String displayName) {
            this.displayName = displayName;
        }
    }
    
    public static class Property
    {
        public String name;
        public String value;
        public String signature;
        
        public Property(final String name, final String value, final String signature) {
            this.name = name;
            this.value = value;
            this.signature = signature;
        }
    }
}
