package net.minecraftforge.property;

public interface IUnlistedProperty<V>
{
    String getName();

    boolean isValid(V var1);

    Class<V> getType();

    String valueToString(V var1);
}
