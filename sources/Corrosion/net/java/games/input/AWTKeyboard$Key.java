package net.java.games.input;

final class Key extends AbstractComponent {
    private float value;

    public Key(Component.Identifier.Key key_id) {
        super(key_id.getName(), key_id);
    }

    public final void setValue(float value) {
        this.value = value;
    }

    protected final float poll() {
        return this.value;
    }

    public final boolean isAnalog() {
        return false;
    }

    public final boolean isRelative() {
        return false;
    }
}
