package xyz.cucumber.base.events.ext;
import xyz.cucumber.base.events.Event;

public class EventSkyColor extends Event
{
    private float red, green, blue;

    public EventSkyColor(float red, float green, float blue)
    {
        super();
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed()
    {
        return red;
    }

    public void setRed(float red)
    {
        this.red = red;
    }

    public float getGreen()
    {
        return green;
    }

    public void setGreen(float green)
    {
        this.green = green;
    }

    public float getBlue()
    {
        return blue;
    }

    public void setBlue(float blue)
    {
        this.blue = blue;
    }
}
