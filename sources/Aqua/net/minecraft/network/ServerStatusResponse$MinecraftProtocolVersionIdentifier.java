package net.minecraft.network;

public static class ServerStatusResponse.MinecraftProtocolVersionIdentifier {
    private final String name;
    private final int protocol;

    public ServerStatusResponse.MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn) {
        this.name = nameIn;
        this.protocol = protocolIn;
    }

    public String getName() {
        return this.name;
    }

    public int getProtocol() {
        return this.protocol;
    }
}
