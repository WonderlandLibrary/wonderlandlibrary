package me.Womas.Light.Module;

import me.Womas.Light.Core.Aquatic;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {


    public List<Template> moduleList = new ArrayList<>();

    public void loadModule() {

    }

    public void add(Template module) {
        try {
            Aquatic.getInstance().getLogger().logger("Loaded Module : " + module.getName());
        } catch (Exception e) {
            Aquatic.getInstance().getLogger().warn("Failed while loading module : " + module.getName());
            e.printStackTrace();
        }
    }


    public void add(String name) {
        try {
            Class clazz = Class.forName("me.Womas.Light.Module.modules." + name);

            if(clazz.getSuperclass() == Template.class){
               Template module = (Template) clazz.newInstance();
               Aquatic.getInstance().getLogger().logger("Loaded Module : " + module.getName());
               moduleList.add(module);
            }

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            Aquatic.getInstance().getLogger().warn("Failed while loading module : " + name);
            e.printStackTrace();
        }
    }



}
