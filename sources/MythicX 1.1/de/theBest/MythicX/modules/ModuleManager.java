package de.theBest.MythicX.modules;

import de.theBest.MythicX.MythicX;
import de.theBest.MythicX.modules.combat.Killaura;
import de.theBest.MythicX.modules.combat.NoClickDelay;
import de.theBest.MythicX.modules.combat.Velocity;
import de.theBest.MythicX.modules.movement.*;
import de.theBest.MythicX.modules.player.ChestStealer;
import de.theBest.MythicX.modules.player.FastPlace;
import de.theBest.MythicX.modules.visual.*;
import de.theBest.MythicX.modules.world.Fucker;
import de.theBest.MythicX.modules.world.NoFall;
import de.theBest.MythicX.modules.world.Scaffold;
import de.Hero.example.clickgui;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    public List<Module> modules = new ArrayList<Module>();

    public ModuleManager(){
        System.out.println("Init");
        addModule(new Sprint());
        addModule(new HUD());
        addModule(new Arraylist());
        addModule(new Killaura());
        addModule(new Fly());
        addModule(new Scaffold());
        addModule(new Velocity());
        addModule(new ESP());
        addModule(new clickgui());
        addModule(new Step());
        addModule(new NoFall());
        addModule(new FastPlace());
        addModule(new Speed());
        addModule(new Strafe());
        //addModule(new PikaDisabler());
        addModule(new NoClickDelay());
        addModule(new NoSlow());
        addModule(new Fullbright());
        addModule(new Xray());
        addModule(new ChestStealer());
        addModule(new Fucker());
    }

    public void addModule(Module module) {
        this.modules.add(module);
    }

    public List<Module> getModules(){
        return modules;
    }

    public Module getModuleByName(String moduleName){
        try {
            for (Module mod : modules){
                if((mod.getName().trim().equalsIgnoreCase(moduleName)) || (mod.toString().equalsIgnoreCase(moduleName.trim()))){
                    return mod;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public Module getModule(Class<? extends Module> clazz){
        for(Module m : modules){
            if(m.getClass() == clazz){
                return m;
            }
        }
        return null;
    }

    public void sendChatMessage(String message){
        message = "\u00A7d" + MythicX.name + "\u00A7f" + message;

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

}
