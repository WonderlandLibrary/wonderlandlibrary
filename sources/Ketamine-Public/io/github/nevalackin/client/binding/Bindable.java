package io.github.nevalackin.client.binding;

import com.google.gson.JsonObject;

public interface Bindable {

    String getName();

    void setActive(boolean active);

    boolean isActive();

    default void loadBind(final JsonObject object) {}

    default void saveBind(final JsonObject object) {}

    default void toggle() {
        this.setActive(!this.isActive());
    }

}
