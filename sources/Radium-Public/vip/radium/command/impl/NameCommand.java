package vip.radium.command.impl;

import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.notification.Notification;
import vip.radium.notification.NotificationType;
import vip.radium.utils.ClipboardUtils;
import vip.radium.utils.Wrapper;

public final class NameCommand implements Command {
    @Override
    public String[] getAliases() {
        return new String[]{"name", "copy", "ign"};
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        String name = Wrapper.getPlayer().getGameProfile().getName();
        ClipboardUtils.setClipboardContents(name);
        RadiumClient.getInstance().getNotificationManager().add(
                new Notification("Name Command",
                        String.format("'%s' has been copied to clipboard", name),
                        NotificationType.SUCCESS));
    }


    @Override
    public String getUsage() {
        return "name/copy/ign";
    }
}
