package net.minecraft.client.renderer.vertex;

public static enum VertexFormatElement.EnumUsage {
    POSITION("Position"),
    NORMAL("Normal"),
    COLOR("Vertex Color"),
    UV("UV"),
    MATRIX("Bone Matrix"),
    BLEND_WEIGHT("Blend Weight"),
    PADDING("Padding");

    private final String displayName;

    private VertexFormatElement.EnumUsage(String displayNameIn) {
        this.displayName = displayNameIn;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
