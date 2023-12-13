/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.script;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import tk.rektsky.api.APIModule;
import tk.rektsky.api.Category;
import tk.rektsky.event.Event;
import tk.rektsky.module.Module;
import tk.rektsky.script.BoolListener;

public class API {
    public static ArrayList<Module> loadAPIModules(File file) {
        ArrayList<Module> modules = new ArrayList<Module>();
        try {
            JarFile jarFile = new JarFile(file);
            Enumeration<JarEntry> e2 = jarFile.entries();
            URL[] urls = new URL[]{new URL("jar:file:" + file.getAbsolutePath() + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);
            while (e2.hasMoreElements()) {
                JarEntry je = e2.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) continue;
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class<?> c2 = cl.loadClass(className);
                if (!className.startsWith("tk.rektsky.api.modules")) continue;
                try {
                    APIModule mod = (APIModule)c2.newInstance();
                    modules.add(API.translateModule(mod));
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
        return modules;
    }

    public static Module translateModule(final APIModule mod) {
        BoolListener setEnabledListener = null;
        BoolListener setRawEnabledListener = null;
        Module _mod = new Module(mod.name, "", API.translateCategory(mod.category)){

            @Override
            public void onEnable() {
                mod.onEnable();
            }

            @Override
            public void onDisable() {
                mod.onDisable();
            }

            @Override
            public void onEvent(Event event) {
                mod.onEvent(event);
            }
        };
        setEnabledListener = _mod::setToggled;
        setRawEnabledListener = _mod::rawSetToggled;
        mod.init(setEnabledListener, setRawEnabledListener);
        return _mod;
    }

    public static tk.rektsky.module.Category translateCategory(Category cat) {
        tk.rektsky.module.Category category = tk.rektsky.module.Category.REKTSKY;
        switch (cat) {
            case WORLD: {
                category = tk.rektsky.module.Category.WORLD;
                break;
            }
            case RENDER: {
                category = tk.rektsky.module.Category.RENDER;
                break;
            }
            case COMBAT: {
                category = tk.rektsky.module.Category.COMBAT;
                break;
            }
            case PLAYER: {
                category = tk.rektsky.module.Category.PLAYER;
                break;
            }
            case EXPLOIT: {
                category = tk.rektsky.module.Category.EXPLOIT;
                break;
            }
            case MOVEMENT: {
                category = tk.rektsky.module.Category.MOVEMENT;
            }
        }
        return category;
    }
}

