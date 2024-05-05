package me.earth.earthhack.impl.util.helpers;

public class MutableWrapper<T>
{
    protected T value;

    public MutableWrapper(T value)
    {
        this.value = value;
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        this.value = value;
    }

}
