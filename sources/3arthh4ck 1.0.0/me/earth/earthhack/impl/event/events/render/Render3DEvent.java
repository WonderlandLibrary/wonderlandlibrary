package me.earth.earthhack.impl.event.events.render;

public class Render3DEvent
{
    private final float partialTicks;

    public Render3DEvent(float partialTicks)
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public static class Pre extends Render3DEvent
    {
        public Pre(float partialTicks)
        {
            super(partialTicks);
        }
    }

    public static class Post extends Render3DEvent
    {
        public Post(float partialTicks)
        {
            super(partialTicks);
        }
    }

}
