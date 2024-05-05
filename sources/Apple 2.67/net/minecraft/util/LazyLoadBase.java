package net.minecraft.util;

public abstract class LazyLoadBase<T>
{
    private T value;
    private boolean isLoaded = false;

    public T getValue()
    {
        if (!this.isLoaded)
        {
            synchronized (this)
            {
                this.value = this.load();
                this.isLoaded = true;
            }
        }

        return this.value;
    }

    protected abstract T load();
}
