package vestige.ui.altmanager.components;

public abstract class AltButton {

    private String name;

    public AltButton(String name) {
        this.name = name;
    }

    public abstract void onClicked();

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

}
