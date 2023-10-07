package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.notifications.NotificationType;
import org.checkerframework.checker.nullness.qual.NonNull;


public class StatusCommand extends NovoCommand {

    public StatusCommand(@NonNull Novoline novoline) {
        super(novoline, "status");
    }


    @Override
    public void process(String[] args) {
        if (args.length != 0) {
            return;
        }

        String status = "Status: " + (ServerUtils.isHypixel() ? "Enabled" : "Disabled");
        NotificationType type = ServerUtils.isHypixel() ? NotificationType.SUCCESS : NotificationType.ERROR;

        notifyClient("AutoBypass", status, 5000, type);
    }
}