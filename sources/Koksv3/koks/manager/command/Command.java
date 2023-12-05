package koks.manager.command;

import koks.api.interfaces.Debug;
import koks.api.interfaces.Methods;
import koks.api.interfaces.Wrapper;

/**
 * @author deleteboys | lmao | kroko
 * @created on 12.09.2020 : 20:47
 */
public abstract class Command implements Methods, Wrapper, Debug {

    public String name,alias;

    public Command() {
        CommandInfo commandInfo = getClass().getAnnotation(CommandInfo.class);
        this.name = commandInfo.name();
        this.alias = commandInfo.alias().equals("") ? this.name : commandInfo.alias();
    }

    public abstract void execute(String[] args);

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
