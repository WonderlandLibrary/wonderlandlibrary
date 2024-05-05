package tech.atani.client.feature.theme.data;

import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ScreenType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(java.lang.annotation.ElementType.TYPE)
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ThemeObjectInfo {

    String name();
    ThemeObjectType themeObjectType();
    ElementType elementType() default ElementType.NOT;
    ScreenType screenType() default ScreenType.NOT;

}
