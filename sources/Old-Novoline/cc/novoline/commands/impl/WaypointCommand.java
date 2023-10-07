package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.modules.visual.Waypoints;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.Arrays;

import static cc.novoline.utils.messages.MessageFactory.usage;
import static cc.novoline.utils.notifications.NotificationType.ERROR;

public final class WaypointCommand extends NovoCommand {

    /* constructors */
    public WaypointCommand(@NonNull Novoline novoline) {
        super(novoline, "waypoint", "Manages waypoints", Arrays.asList("waypoints", "wp"));
    }

    /* methods */
    private void sendHelp() {
        sendHelp( // @off
                "Waypoints help:", ".waypoint",
                usage("add (x) (y) (z) (name)", "add waypoint"),
                usage("remove (name)", "removes waypoint"),
                usage("clear", "remove all waypoints"),
                usage("list", "show waypoints list")
        ); // @on
    }

    @Override
    public void process(String[] args) {
        final Waypoints module = Novoline.getInstance().getModuleManager().getModule(Waypoints.class);

        if (args.length == 0) {
            sendHelp();
            return;
        }

        final String action = args[0];

        switch (action.toLowerCase()) {
            case "add":
                if (args.length != 5) {
                    sendHelp();
                    return;
                }

                module.addWaypoint(Waypoints.Waypoint
                        .of(args[4], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));

                try {
                    module.getConfig().save();
                    notify("Waypoint " + args[4] + " was added successfully!", 5_000);
                } catch (IOException e) {
                    Novoline.getInstance().getNotificationManager().pop("Can't save to file", ERROR);
                    module.getLogger().warn("An error occurred while saving waypoints list", e);
                }

                break;

            case "remove":
            case "delete":
            case "del":
                if (args.length != 2) {
                    sendHelp();
                    return;
                }

                if (module.removeWaypoint(args[1])) {
                    notify("Removed " + args[1] + " waypoint!", 5_000);

                    try {
                        module.getConfig().save();
                    } catch (IOException e) {
                        Novoline.getInstance().getNotificationManager().pop("Can't save to file", ERROR);
                        module.getLogger().warn("An error occurred while saving waypoints list", e);
                    }
                } else {
                    notifyError("Waypoint doesn't exist!", 5_000);
                }

                break;

            case "clear":
                if (args.length != 1) {
                    sendHelp();
                    return;
                }

                if (!module.getWaypointsList().isEmpty()) {
                    module.getWaypointsList().clear();
                }

                break;

            case "list":
                if (args.length != 1) {
                    sendHelp();
                    return;
                }

                send("Waypoints:");

                for (Waypoints.Waypoint waypoint : module.getWaypointsList()) {
                    send(" - name: \u00A73" + waypoint.getName() + "\u00A7r, " +
                            "coordinates: \u00A78X:\u00A7r " + waypoint.getX() + " \u00A78Y:\u00A7r " + waypoint.getY() + " \u00A78Z:\u00A7r " + waypoint.getZ());
                }

                break;
        }
    }

}
