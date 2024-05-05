package graphite.api.module;

import com.google.gson.JsonObject;
import graphite.api.config.Serializable;
import graphite.api.module.util.Category;
import graphite.api.module.util.ModuleInfo;
import graphite.api.property.Property;
import graphite.api.property.impl.*;
import graphite.api.util.render.animate.Translate;
import graphite.impl.Graphite;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class Module implements Toggleable, Serializable {
    protected final static Minecraft mc = Minecraft.getMinecraft();

    private final String name = getAnnotation().name();
    private final String description = getAnnotation().description();
    private final Category category = getAnnotation().category();
    private int key = getAnnotation().key();

    private boolean enabled, hidden;
    
    public Translate translate = new Translate(0.0F, 0.0F);

    private Supplier<String> suffix;

    private final List<Property<?>> properties = new ArrayList<>();

    @Override
    public void load(final JsonObject object) {
        if (object.has("enabled")) {
            this.setEnabled(object.get("enabled").getAsBoolean());
        }

        if (object.has("hidden")) {
            this.setHidden(object.get("hidden").getAsBoolean());
        }

        if (object.has("properties")) {
            final JsonObject propsObject = object.get("properties").getAsJsonObject();

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
            }  else if (property instanceof EnumProperty) {
                final EnumProperty<?> enumProperty = (EnumProperty<?>) property;
                propertiesObject.addProperty(property.getName(), enumProperty.getValue().ordinal());
            }
        }

        savedDataObject.add("properties", propertiesObject);

        object.add(this.getName(), savedDataObject);
    }

    protected void register(final Property<?>... properties) {
        this.properties.addAll(Arrays.asList(properties));
    }

    public List<Property<?>> getProperties() {
        return properties;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Supplier<String> getSuffix() {
        return suffix;
    }

    public void setSuffix(Supplier<String> suffix) {
        this.suffix = suffix;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (enabled) {
                onEnable();
                Graphite.INSTANCE.getEventBus().subscribe(this);
            } else {
                Graphite.INSTANCE.getEventBus().unsubscribe(this);
                onDisable();
            }
        }
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void toggle() {
        setEnabled(!enabled);
    }

    private ModuleInfo getAnnotation() {
        if (getClass().isAnnotationPresent(ModuleInfo.class)) {
            return getClass().getAnnotation(ModuleInfo.class);
        }

        throw new IllegalStateException("Annotation 'ModuleInfo' not found!");
    }
}
