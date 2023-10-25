package net.optifine;

private static enum ConnectedTexturesCompact.Dir {
    UP,
    UP_RIGHT,
    RIGHT,
    DOWN_RIGHT,
    DOWN,
    DOWN_LEFT,
    LEFT,
    UP_LEFT;

    public static final ConnectedTexturesCompact.Dir[] VALUES;

    static {
        VALUES = ConnectedTexturesCompact.Dir.values();
    }
}
