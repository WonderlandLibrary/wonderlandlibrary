package net.minecraft.network.play.server;

public static enum S45PacketTitle.Type {
    TITLE,
    SUBTITLE,
    TIMES,
    CLEAR,
    RESET;


    public static S45PacketTitle.Type byName(String name) {
        for (S45PacketTitle.Type s45packettitle$type : S45PacketTitle.Type.values()) {
            if (!s45packettitle$type.name().equalsIgnoreCase(name)) continue;
            return s45packettitle$type;
        }
        return TITLE;
    }

    public static String[] getNames() {
        String[] astring = new String[S45PacketTitle.Type.values().length];
        int i = 0;
        for (S45PacketTitle.Type s45packettitle$type : S45PacketTitle.Type.values()) {
            astring[i++] = s45packettitle$type.name().toLowerCase();
        }
        return astring;
    }
}
