package Method.Client.command;


import Method.Client.Main;
import Method.Client.utils.visual.ChatUtils;
import net.minecraft.util.text.TextFormatting;

@Command.CommandFeatures(description = "Opens gui", Syntax = "gui")

public class OpenGui extends Command {
    @Override
    public void runCommand(String s, String[] args) {

        mc.displayGuiScreen(null);
        Thread t = new Thread(new ThreadDemo());

        t.start();
        mc.mouseHelper.ungrabMouseCursor();
        ChatUtils.message(TextFormatting.GOLD + "Tried to open Gui");

    }

    // We do this to pause and let catch up any other things running then at highest prio insert Gui
    private static class ThreadDemo implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mc.displayGuiScreen(Main.ClickGui);
        }
    }
}