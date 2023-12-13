package vip.radium.property;

@FunctionalInterface
public interface ValueChangeListener<T> {

    void onValueChange(T oldValue, T value);

}
