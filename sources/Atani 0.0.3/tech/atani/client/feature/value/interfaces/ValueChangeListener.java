package tech.atani.client.feature.value.interfaces;

import tech.atani.client.feature.value.Value;

public interface ValueChangeListener {
    void onChange(Stage stage, Value value, Object oldValue, Object newValue);

    enum Stage {
        PRE, POST
    }
}
