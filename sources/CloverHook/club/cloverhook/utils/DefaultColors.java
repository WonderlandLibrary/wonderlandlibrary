package club.cloverhook.utils;

/**
 * @author antja03
 */
public enum DefaultColors {
    CLOVERHOOK_1(0xff32CD32), CLOVERHOOK_2(0xff32CD32);

    private int color;

    public int getColor()
    {
        return this.color;
    }

    private DefaultColors(int color)
    {
        this.color = color;
    }
}
