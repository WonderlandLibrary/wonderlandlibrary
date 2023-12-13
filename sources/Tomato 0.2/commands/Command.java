/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands;

import com.darkmagician6.eventapi.EventManager;
import cow.milkgod.cheese.events.EventChatSend;
import java.util.Objects;
import net.minecraft.client.Minecraft;

public class Command {
    public static String message;
    private String name;
    private String errorMessage;
    private String arguments;
    private String description;
    public Minecraft mc = Minecraft.getMinecraft();
    private boolean state;
    private String[] alias;

    public Command(String name, String errorMessage, String[] alias, String arguments, String description) {
        this.name = name;
        this.errorMessage = errorMessage;
        this.alias = alias;
        this.arguments = arguments;
        this.description = description;
    }

    public boolean getState() {
        if (this.state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        return this.state;
    }

    public void Toggle() {
        if (!this.state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        message = Objects.nonNull(EventChatSend.getMessage()) ? EventChatSend.getMessage() : "";
        this.state = !this.state;
    }

    public void Toggle(String message) {
        Command.message = message;
        if (!this.state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = !this.state;
    }

    public String getMessage() {
        return message;
    }

    public void setState(boolean state) {
        if (state) {
            EventManager.register(this);
        } else {
            EventManager.unregister(this);
        }
        this.state = state;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public String getDescription() {
        return this.description;
    }

    public String getArguments() {
        return this.arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}

