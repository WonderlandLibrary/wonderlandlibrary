package com.masterof13fps.features.commands;

import com.masterof13fps.Methods;
import com.masterof13fps.Wrapper;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.utils.time.TimeHelper;
import net.minecraft.client.Minecraft;

public abstract class Command implements Wrapper, Methods {

    public static Minecraft mc = Minecraft.mc();
    public TimeHelper timeHelper = new TimeHelper();
    private String name, label, alias;

    public Command(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public abstract void execute(String[] args);

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public void onEvent(Event event) {

    }

    public String[] getArguments() {
        return this.label.split(" ");
    }

}