package io.github.nevalackin.client.module;

import com.google.gson.JsonObject;
import io.github.nevalackin.client.binding.Bind;
import io.github.nevalackin.client.binding.BindManager;
import io.github.nevalackin.client.binding.BindType;
import io.github.nevalackin.client.binding.Bindable;
import io.github.nevalackin.client.config.Serializable;
import io.github.nevalackin.client.property.*;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.game.input.InputType;
import io.github.nevalackin.client.module.combat.rage.TargetStrafe;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class Module implements Serializable, Bindable {

    private final String name;
    private final Category category;
    private final Category.SubCategory subCategory;

    private Bind bind;
    private boolean enabled;
    private boolean hidden;

    private Supplier<String> suffix;

    private final List<Property<?>> properties = new ArrayList<>();
    protected final Minecraft mc = Minecraft.getMinecraft();

    protected static TargetStrafe targetStrafeInstance;

    public Module(final String name, final Category category, final Category.SubCategory subCategory) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;

        this.setBind(null);
    }

    @Override
    public void loadBind(final JsonObject object) {
        InputType inputType = InputType.KEYBOARD;
        int button = 0;
        BindType bindType = BindType.TOGGLE;

        if (object.has("input")) {
            inputType = InputType.values()[object.get("input").getAsInt()];
        }

        if (object.has("button")) {
            button = object.get("button").getAsInt();
        }

        if (object.has("type")) {
            bindType = BindType.values()[object.get("type").getAsInt()];
        }

        this.setBind(new Bind(inputType, button, bindType));
    }

    @Override
    public void saveBind(final JsonObject object) {
        final Bind bind = this.getBind();
        if (bind == null) return;

        final JsonObject savedDataObject = new JsonObject();

        savedDataObject.addProperty("input", bind.getInputType().ordinal());
        savedDataObject.addProperty("button", bind.getCode());
        savedDataObject.addProperty("type", bind.getBindType().ordinal());

        object.add(this.getName(), savedDataObject);
    }

    @Override
    public void load(final JsonObject object) {
        if (object.has("enabled")) {
            this.setEnabled(object.get("enabled").getAsBoolean());
        }

        if (object.has("hidden")) {
            this.setHidden(object.get("hidden").getAsBoolean());
        }

        if (object.has("props")) {
            final JsonObject propsObject = object.get("props").getAsJsonObject();

            for (final Property<?> property : this.getProperties()) {
                if (propsObject.has(property.getName())) {
                    if (property instanceof BooleanProperty) {
                        final BooleanProperty booleanProperty = (BooleanProperty) property;
                        booleanProperty.setValue(propsObject.get(property.getName()).getAsBoolean());
                    } else if (property instanceof DoubleProperty) {
                        final DoubleProperty doubleProperty = (DoubleProperty) property;
                        doubleProperty.setValue(propsObject.get(property.getName()).getAsDouble());
                    } else if (property instanceof ColourProperty) {
                        final ColourProperty colourProperty = (ColourProperty) property;
                        colourProperty.setValue(propsObject.get(property.getName()).getAsInt());
                    } else if (property instanceof EnumProperty) {
                        final EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                        enumProperty.setValue(propsObject.get(property.getName()).getAsInt());
                    } else if (property instanceof MultiSelectionEnumProperty) {

                    }
                }
            }
        }
    }

    @Override
    public void save(final JsonObject object) {
        final JsonObject savedDataObject = new JsonObject();
        savedDataObject.addProperty("enabled", this.isEnabled());
        savedDataObject.addProperty("hidden", this.isHidden());

        final JsonObject propertiesObject = new JsonObject();

        for (final Property<?> property : this.getProperties()) {
            if (property instanceof BooleanProperty) {
                final BooleanProperty booleanProperty = (BooleanProperty) property;
                propertiesObject.addProperty(property.getName(), booleanProperty.getValue());
            } else if (property instanceof DoubleProperty) {
                final DoubleProperty doubleProperty = (DoubleProperty) property;
                propertiesObject.addProperty(property.getName(), doubleProperty.getValue());
            } else if (property instanceof ColourProperty) {
                final ColourProperty colourProperty = (ColourProperty) property;
                propertiesObject.addProperty(property.getName(), colourProperty.getValue());
            } else if (property instanceof EnumProperty) {
                final EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                propertiesObject.addProperty(property.getName(), enumProperty.getValue().ordinal());
            } else if (property instanceof MultiSelectionEnumProperty) {
                final MultiSelectionEnumProperty<?> enumProperty = (MultiSelectionEnumProperty<?>) property;
                propertiesObject.addProperty(property.getName(), Arrays.toString(enumProperty.getValueIndices()));
            }
        }

        savedDataObject.add("props", propertiesObject);

        object.add(this.getName(), savedDataObject);
    }

    protected void register(final Property<?>... properties) {
        this.properties.addAll(Arrays.asList(properties));
    }

    public Supplier<String> getSuffix() {
        return suffix;
    }

    public void setSuffix(Supplier<String> suffix) {
        this.suffix = suffix;
    }

    public String getDisplayName() {
        if (this.suffix == null) {
            return this.name;
        } else {
            return String.format("%s \2477%s", this.name, this.suffix.get());
        }
    }

    public List<Property<?>> getProperties() {
        return properties;
    }

    @Override
    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Category.SubCategory getSubCategory() {
        return subCategory;
    }

    public void setBind(Bind bind) {
        KetamineClient.getInstance().getBindManager().register(this, bind);
        this.bind = bind;
    }

    public Bind getBind() {
        return bind;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisplayed() {
        return !this.hidden && this.enabled;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            if (enabled) {
                this.onEnable();
                KetamineClient.getInstance().getEventBus().subscribe(this);
            } else {
                KetamineClient.getInstance().getEventBus().unsubscribe(this);
                this.onDisable();
            }
        }

        this.enabled = enabled;
    }

    @Override
    public void setActive(boolean active) {
        this.setEnabled(active);
    }

    @Override
    public boolean isActive() {
        return this.isEnabled();
    }

    public abstract void onEnable();

    public abstract void onDisable();
}
