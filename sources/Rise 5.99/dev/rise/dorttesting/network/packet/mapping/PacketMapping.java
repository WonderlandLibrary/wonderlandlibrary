package dev.rise.dorttesting.network.packet.mapping;

public class PacketMapping {

    private int javaID;
    private int bedrockID;

    public PacketMapping(final int javaID, final int bedrockID) {
        this.javaID = javaID;
        this.bedrockID = bedrockID;
    }

    public int getJavaID() {
        return javaID;
    }

    public int getBedrockID() {
        return bedrockID;
    }

    public void setJavaID(final int javaID) {
        this.javaID = javaID;
    }

    public void setBedrockID(final int bedrockID) {
        this.bedrockID = bedrockID;
    }
}
