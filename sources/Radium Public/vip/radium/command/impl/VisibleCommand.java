package vip.radium.command.impl;

import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.module.Module;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;

import java.util.Optional;

public final class VisibleCommand implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"visible", "v"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        if (arguments.length == 2) {
            String moduleName = arguments[1];

            Optional<Module> module = Optional.ofNullable(RadiumClient.getInstance().getModuleManager().getModule(moduleName));

            if (module.isPresent()) {
                Module m = module.get();
                m.setHidden(!m.isHidden());
                RadiumClient.getInstance().getNotificationManager().add(new Notification(
                        "Set '" + m.getLabel() + "' to " + (m.isHidden() ? "hidden" : "visible"),
                        NotificationType.SUCCESS));
                return;
            }
        }
        throw new CommandExecutionException(getUsage());
    }

    @Override
    public String getUsage() {
        return "visible/v <module>";
    }
}
