package de.tired.util.render.shaderloader;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class ShaderManager {

    private static List<ShaderProgram> shaders;

    @SuppressWarnings("unchecked")
    public static <T extends ShaderProgram> T shaderBy(Class<T> tClass) {
        return (T) shaders.stream().filter(mod -> mod.getClass().equals(tClass)).findFirst().orElse(null);
    }

    public ShaderManager() {
        initialize();
    }

    public void initialize() {
        shaders = new ArrayList<>();

        final Reflections reflections = new Reflections("de.tired.util.render");
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ShaderAnnoation.class);
        for (Class<?> aClass : classes) {
            try {
                final ShaderProgram aShader = (ShaderProgram) aClass.newInstance();
                shaders.add(aShader);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}
