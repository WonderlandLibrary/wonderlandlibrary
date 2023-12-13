package vip.radium.command.impl;

import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.config.Config;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.utils.Wrapper;

public final class ConfigCommand implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"config", "c", "preset"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        if (arguments.length >= 2) {
            String upperCaseFunction = arguments[1].toUpperCase();

            if (arguments.length == 3) {
                switch (upperCaseFunction) {
                    case "LOAD":
                        if (RadiumClient.getInstance().getConfigManager().loadConfig(arguments[2]))
                            success("loaded", arguments[2]);
                        else
                            fail("load", arguments[2]);
                        return;
                    case "SAVE":
                        if (RadiumClient.getInstance().getConfigManager().saveConfig(arguments[2]))
                            success("saved", arguments[2]);
                        else
                            fail("save", arguments[2]);
                        return;
                    case "DELETE":
                        if (RadiumClient.getInstance().getConfigManager().deleteConfig(arguments[2]))
                            success("deleted", arguments[2]);
                        else
                            fail("delete", arguments[2]);
                        return;
                }
            } else if (arguments.length == 2 && upperCaseFunction.equalsIgnoreCase("LIST")) {
                Wrapper.addChatMessage("Available Configs:");
                for (Config config : RadiumClient.getInstance().getConfigManager().getElements())
                    Wrapper.addChatMessage(config.getName());
                return;
            }
        }

        throw new CommandExecutionException(getUsage());
    }

    private void success(String type, String configName) {
        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                String.format("Successfully %s config: '%s'", type, configName), NotificationType.SUCCESS));
    }

    private void fail(String type, String configName) {
        RadiumClient.getInstance().getNotificationManager().add(new Notification(
                String.format("Failed to %s config: '%s'", type, configName), NotificationType.ERROR));
    }

    @Override
    public String getUsage() {
        return "config/c/preset <load/save/delete/list> <(optional)config>";
    }
}
