package tech.atani.client.feature.theme.storage;

import de.florianmichael.rclasses.storage.Storage;
import org.reflections.Reflections;
import tech.atani.client.feature.theme.ThemeObject;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ScreenType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.listener.handling.EventHandling;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class ThemeStorage extends Storage<ThemeObject> {

    private static ThemeStorage instance;

    @Override
    public void init() {
        EventHandling.getInstance().registerListener(this);
        final Reflections reflections = new Reflections("tech.atani");
        reflections.getTypesAnnotatedWith(ThemeObjectInfo.class).forEach(aClass -> {
            try {
                this.add((ThemeObject) aClass.getDeclaredConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public <T extends ThemeObject> T getThemeObject(String name) {
        return (T) this.getList().stream().filter(themeObject -> themeObject.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }

    public <T extends ThemeObject> T getThemeObject(String name, ElementType elementType) {
        return (T) this.getList().stream().filter(themeObject -> themeObject.getName().equalsIgnoreCase(name) && themeObject.getThemeObjectType() == ThemeObjectType.ELEMENT && themeObject.getElementType() == elementType).findAny().orElse(null);
    }

    public <T extends ThemeObject> T getThemeObject(String name, ScreenType screenType) {
        return (T) this.getList().stream().filter(themeObject -> themeObject.getName().equalsIgnoreCase(name) && themeObject.getThemeObjectType() == ThemeObjectType.ELEMENT && themeObject.getScreenType() == screenType).findAny().orElse(null);
    }

    public List<ThemeObject> getThemeObjects(ElementType elementType) {
        return this.getList().stream().filter(themeObject -> themeObject.getThemeObjectType() == ThemeObjectType.ELEMENT && themeObject.getElementType() == elementType).collect(Collectors.toList());
    }

    public List<ThemeObject> getThemeObjects(ScreenType screenType) {
        return this.getList().stream().filter(themeObject -> themeObject.getThemeObjectType() == ThemeObjectType.SCREEN && themeObject.getScreenType() == screenType).collect(Collectors.toList());
    }

    public static ThemeStorage getInstance() {
        return instance;
    }

    public static void setInstance(ThemeStorage instance) {
        ThemeStorage.instance = instance;
    }
}
