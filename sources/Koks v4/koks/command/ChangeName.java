package koks.command;

import koks.api.registry.command.Command;
import koks.api.utils.LoginUtil;
import koks.api.utils.MojangUtil;
import net.minecraft.util.Tuple;

import java.io.IOException;

@Command.Info(name = "changename", description = "change your name", aliases = {"cname"})
public class ChangeName extends Command {

    @Override
    public boolean execute(String[] args) {
        if (args.length == 1) {
            final MojangUtil mojangUtil = MojangUtil.getInstance();
            try {
                Tuple<Integer, String> tuple = mojangUtil.changeName(args[0], mc.session.getToken());
                if(tuple.getFirst() == 200) {
                    sendMessage("§aName changed to: §e" + args[0]);
                    sendMessage("§aplease relogin into your account!");
                } else {
                    sendMessage("§cCant set your name!");
                    sendMessage("§cResponse Code: §e" + tuple.getFirst() + " §b" + tuple.getSecond());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
