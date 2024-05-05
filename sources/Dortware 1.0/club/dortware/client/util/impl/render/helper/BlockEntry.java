package club.dortware.client.util.impl.render.helper;

public class BlockEntry {

    private final int id;
    private final int color;

    public BlockEntry(int id, int color) {
        this.id = id;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public int getColor() {
        return color;
    }
}
