package club.dortware.client.file;

public abstract class MFile {

    private final String name;

    public MFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void load();

    public abstract void save();

}
