package summer.ui.changelog;

public class Change implements Labeled {

    private final String label;
    private final GuiChanges.Type type;

    public Change(String label, GuiChanges.Type type) {
        this.label = label;
        this.type = type;
    }

    @Override
    public final String getLabel() {
        return this.label;
    }

    public final GuiChanges.Type getType() {
        return this.type;
    }
}