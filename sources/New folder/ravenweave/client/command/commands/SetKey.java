package ravenweave.client.command.commands;

import ravenweave.client.clickgui.raven.Terminal;
import ravenweave.client.command.Command;
import ravenweave.client.main.Raven;
import ravenweave.client.utils.Utils;

public class SetKey extends Command {
    public SetKey() {
        super("setkey", "Sets hypixel's API key. To get a new key, run `/api new`", 2, 2, new String[] { "key" },
                new String[] { "apikey" });
    }

    @Override
    public void onCall(String[] args) {
        if (args.length == 0) {
            this.incorrectArgs();
            return;
        }

        Terminal.print("Setting...");
        String n;
        n = args[0];
        Raven.getExecutor().execute(() -> {
            if (Utils.URLS.isHypixelKeyValid(n)) {
                Utils.URLS.hypixelApiKey = n;
                Terminal.print("Success!");
                Raven.clientConfig.saveConfig();
            } else {
                Terminal.print("Invalid key.");
            }

        });

    }
}
