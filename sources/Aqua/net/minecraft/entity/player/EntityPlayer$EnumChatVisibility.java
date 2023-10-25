package net.minecraft.entity.player;

public static enum EntityPlayer.EnumChatVisibility {
    FULL(0, "options.chat.visibility.full"),
    SYSTEM(1, "options.chat.visibility.system"),
    HIDDEN(2, "options.chat.visibility.hidden");

    private static final EntityPlayer.EnumChatVisibility[] ID_LOOKUP;
    private final int chatVisibility;
    private final String resourceKey;

    private EntityPlayer.EnumChatVisibility(int id, String resourceKey) {
        this.chatVisibility = id;
        this.resourceKey = resourceKey;
    }

    public int getChatVisibility() {
        return this.chatVisibility;
    }

    public static EntityPlayer.EnumChatVisibility getEnumChatVisibility(int id) {
        return ID_LOOKUP[id % ID_LOOKUP.length];
    }

    public String getResourceKey() {
        return this.resourceKey;
    }

    static {
        ID_LOOKUP = new EntityPlayer.EnumChatVisibility[EntityPlayer.EnumChatVisibility.values().length];
        EntityPlayer.EnumChatVisibility[] enumChatVisibilityArray = EntityPlayer.EnumChatVisibility.values();
        int n = enumChatVisibilityArray.length;
        for (int i = 0; i < n; ++i) {
            EntityPlayer.EnumChatVisibility entityplayer$enumchatvisibility;
            EntityPlayer.EnumChatVisibility.ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility = enumChatVisibilityArray[i];
        }
    }
}
