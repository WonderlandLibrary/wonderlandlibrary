package net.minecraft.command;

public static enum CommandResultStats.Type {
    SUCCESS_COUNT(0, "SuccessCount"),
    AFFECTED_BLOCKS(1, "AffectedBlocks"),
    AFFECTED_ENTITIES(2, "AffectedEntities"),
    AFFECTED_ITEMS(3, "AffectedItems"),
    QUERY_RESULT(4, "QueryResult");

    final int typeID;
    final String typeName;

    private CommandResultStats.Type(int id, String name) {
        this.typeID = id;
        this.typeName = name;
    }

    public int getTypeID() {
        return this.typeID;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public static String[] getTypeNames() {
        String[] astring = new String[CommandResultStats.Type.values().length];
        int i = 0;
        for (CommandResultStats.Type commandresultstats$type : CommandResultStats.Type.values()) {
            astring[i++] = commandresultstats$type.getTypeName();
        }
        return astring;
    }

    public static CommandResultStats.Type getTypeByName(String name) {
        for (CommandResultStats.Type commandresultstats$type : CommandResultStats.Type.values()) {
            if (!commandresultstats$type.getTypeName().equals((Object)name)) continue;
            return commandresultstats$type;
        }
        return null;
    }
}
