package io.github.raze.commands.collection;

import io.github.raze.commands.system.Command;

public class ClipCommand extends Command {

    public ClipCommand() {
        super("Clip", "Set your position", "clip <Up/Down/Left/Right> <Blocks>", "vc");
    }

    public String onCommand(String[] arguments, String command) {
        if (arguments.length < 3) {
            return "Usage: " + getSyntax();
        }

        String direction = arguments[1].toLowerCase();
        double distance = Double.parseDouble(arguments[2]);

        double offsetX = 0;
        double offsetY = 0;

        switch (direction) {
            case "up":
                offsetY = distance;
                break;
            case "down":
                offsetY = -distance;
                break;
            case "left":
                offsetX = -distance;
                break;
            case "right":
                offsetX = distance;
                break;
            default:
                return "Invalid direction. Please specify 'up', 'down', 'left', or 'right'.";
        }

        double newX = mc.thePlayer.posX + offsetX;
        double newY = mc.thePlayer.posY + offsetY;
        double newZ = mc.thePlayer.posZ;

        mc.thePlayer.setPosition(newX, newY, newZ);

        String directionText = getDirection(direction);
        return String.format("Clipped you %s blocks %s.", Math.abs(distance), directionText);
    }

    private String getDirection(String direction) {
        switch (direction) {
            case "up":
                return "upwards";
            case "down":
                return "downwards";
            case "left":
                return "to the left";
            case "right":
                return "to the right";
            default:
                return direction;
        }
    }

}
