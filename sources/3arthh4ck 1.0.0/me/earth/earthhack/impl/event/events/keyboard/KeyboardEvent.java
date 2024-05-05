package me.earth.earthhack.impl.event.events.keyboard;

public class KeyboardEvent
{
    private final boolean eventState;
    private final int key;

    public KeyboardEvent(boolean eventState, int key)
    {
        this.eventState = eventState;
        this.key = key;
    }

    public boolean getEventState()
    {
        return eventState;
    }

    public int getKey()
    {
        return key;
    }

    public static class Post
    {
        //Will be send after all KeyBoardEvents have been fired.
    }

}
