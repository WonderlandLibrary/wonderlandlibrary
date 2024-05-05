package rip.helium.utils;

/**
 * @author antja03
 */
public enum DefaultColors {
    CLOVERHOOK_1(0xff32CD32), CLOVERHOOK_2(0xff32CD32);

    private final int color;

    DefaultColors(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }
}
