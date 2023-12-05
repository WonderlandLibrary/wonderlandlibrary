package rip.helium.event.minecraft;

/**
 * @author antja03
 */
public class KeyPressEvent {
    private final int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return keyCode;
    }
}
