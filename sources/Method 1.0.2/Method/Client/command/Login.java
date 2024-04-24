package Method.Client.command;

import Method.Client.utils.LoginUtils;
import Method.Client.utils.system.Wrapper;
import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "Change session.", Syntax = "login <email> <password>/<nick>")

public class Login extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (args.length > 1 || args[0].contains(":")) {
            String email;
            String password;
            if (args[0].contains(":")) {
                String[] split = args[0].split(":", 2);
                email = split[0];
                password = split[1];
            } else {
                email = args[0];
                password = args[1];
            }
            String log = LoginUtils.loginAlt(email, password);
            ChatUtils.warning(log);
        } else {
            LoginUtils.changeCrackedName(args[0]);
            ChatUtils.warning("Logged [Cracked]: " + Wrapper.INSTANCE.mc().getSession().getUsername());
        }
    }

}