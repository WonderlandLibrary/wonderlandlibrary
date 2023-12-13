package koks.command;

import koks.api.registry.command.Command;
import koks.api.utils.MojangUtil;
import net.minecraft.util.Tuple;

import java.io.IOException;

@Command.Info(name = "changeskin", description = "You can change your skin", aliases = {"skin"})
public class ChangeSkin extends Command {
    @Override
    public boolean execute(String[] args) {
        if(args.length == 2) {
            final MojangUtil mojangUtil = MojangUtil.getInstance();
            try {
                String url = args[1];
                if(!args[1].startsWith("http"))
                    url = mojangUtil.getSkin(mojangUtil.getUUID(args[1]));
                final Tuple<Integer, String> tuple = mojangUtil.changeSkin(MojangUtil.SkinVariant.valueOf(args[0].toUpperCase()), url, mc.session.getToken());
                if(tuple.getFirst() == 200) {
                    sendMessage("§aSkin was changed!");
                    sendMessage("§aplease rejoin!");
                } else {
                    sendMessage("§cCant set your skin!");
                    sendMessage("§cResponse Code: §e" + tuple.getFirst() + " §b" + tuple.getSecond());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }
}
