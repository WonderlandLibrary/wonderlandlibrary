package graphite.api.property;

public interface ValueChangeListener<T> {
    void onChange(T now);
}