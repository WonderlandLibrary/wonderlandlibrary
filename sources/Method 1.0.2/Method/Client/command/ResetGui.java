package Method.Client.command;


import Method.Client.Main;
import Method.Client.clickgui.component.Frame;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Resets gui pos", Syntax = "ResetGui")

public class ResetGui extends Command {

    @Override
    public void runCommand(String s, String[] args) {

        int xOffset = 5;
        for (Frame frame : Main.ClickGui.frames) {
            frame.setY(20);
            frame.setX(xOffset + 10);
            xOffset = xOffset + frame.getWidth();
        }
        ChatUtils.message("Guireset!");

    }


}