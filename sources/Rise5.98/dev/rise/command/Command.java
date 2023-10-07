package dev.rise.command;

import dev.rise.Rise;
import dev.rise.command.api.CommandInfo;
import lombok.Getter;
import net.minecraft.client.Minecraft;

@Getter
public abstract class Command {

    protected final Minecraft mc = Minecraft.getMinecraft();

    protected CommandInfo commandInfo;

    public Command() {
        if (this.getClass().isAnnotationPresent(CommandInfo.class)) {
            this.commandInfo = this.getClass().getAnnotation(CommandInfo.class);
        } else {
            throw new RuntimeException("CommandInfo annotation has not been found on " + this.getClass().getSimpleName());
        }
    }

    public abstract void onCommand(final String command, final String[] args) throws Exception;

    protected final void sendMessage(final Object object) {
        Rise.addChatMessage(object);
    }
}
