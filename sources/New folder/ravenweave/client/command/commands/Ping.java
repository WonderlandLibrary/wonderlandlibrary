package ravenweave.client.command.commands;

import ravenweave.client.command.Command;
import ravenweave.client.utils.PingChecker;

public class Ping extends Command {
    public Ping() {
        super("ping", "Gets your ping", 0, 0, new String[] {}, new String[] { "p", "connection", "lag" });
    }

    @Override
    public void onCall(String[] args) {
        PingChecker.checkPing();
    }
}
