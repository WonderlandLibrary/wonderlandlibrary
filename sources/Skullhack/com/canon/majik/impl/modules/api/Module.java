package com.canon.majik.impl.modules.api;

import com.canon.majik.api.core.Initializer;
import com.canon.majik.api.utils.Globals;
import com.canon.majik.api.utils.client.ChatUtils;
import com.canon.majik.api.utils.client.TColor;
import com.canon.majik.impl.setting.Setting;
import com.canon.majik.impl.setting.settings.BooleanSetting;
import com.canon.majik.impl.setting.settings.ColorSetting;
import com.canon.majik.impl.setting.settings.ModeSetting;
import com.canon.majik.impl.setting.settings.NumberSetting;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Module implements Globals {

    private String name,description;
    private boolean toggle;
    private int key;
    private Category category;
    ArrayList<Setting> settings = new ArrayList<>();
    public NumberSetting x = new NumberSetting("X", 10, 0, 1000, this);
    public NumberSetting y = new NumberSetting("Y", 10, 0, 1000, this);

    public Module(String name, Category category){
        this.name = name;
        this.category = category;
    }

    public Module(String name, int key, Category category){
        this.name = name;
        this.key = key;
        this.category = category;
    }

    public Module(String name){
        this.name = name;
        this.category = Category.HUD;
        add(x,y);
    }

    public Module(String name, String description, int key, Category category){
        this.name = name;
        this.description = description;
        this.key = key;
        this.category = category;
    }

    public ArrayList<Setting> getSettings(){
        return settings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;

        if(toggle){
            Initializer.eventBus.registerListener(this);
            onEnable();
        }else{
            Initializer.eventBus.unregisterListener(this);
            onDisable();
        }
    }

    public void toggle(){
        this.toggle = !toggle;

        if(this.toggle){
            Initializer.eventBus.registerListener(this);
            onEnable();
        }else{
            Initializer.eventBus.unregisterListener(this);
            onDisable();
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void onEnable(){
        if(nullCheck()) return;
        MinecraftForge.EVENT_BUS.register(this);
        ChatUtils.tempMessage(ChatFormatting.DARK_GREEN + "[" + ChatFormatting.GREEN + "+" + ChatFormatting.DARK_GREEN + "] " + ChatFormatting.GREEN + getName(), 2);
    }

    public void onDisable(){
        if(nullCheck()) return;
        MinecraftForge.EVENT_BUS.unregister(this);
        ChatUtils.tempMessage(ChatFormatting.DARK_RED + "[" + ChatFormatting.RED + "-" + ChatFormatting.DARK_RED + "] " + ChatFormatting.RED + getName(), 1);
    }

    public void add(Setting... setting){
        settings.addAll(Arrays.asList(setting));
    }

    public BooleanSetting setting(String name, boolean value){
        BooleanSetting sett = new BooleanSetting(name, value, this);
        add(sett);
        return sett;
    }

    public NumberSetting setting(String name, Number value, Number min, Number max){
        NumberSetting sett = new NumberSetting(name,value,min,max,this);
        add(sett);
        return sett;
    }

    public ColorSetting setting(String name, TColor value){
        ColorSetting sett = new ColorSetting(name, value, this);
        add(sett);
        return sett;
    }

    public ModeSetting setting(String name, String value, String... values){
        ModeSetting sett = new ModeSetting(name, value, this, values);
        add(sett);
        return sett;
    }
}

