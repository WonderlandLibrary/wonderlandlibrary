package wtf.expensive.command.impl;

import com.mojang.datafixers.types.Func;
import wtf.expensive.command.Command;
import wtf.expensive.command.CommandInfo;
import wtf.expensive.managment.Managment;
import wtf.expensive.modules.Function;
import wtf.expensive.util.ClientUtil;

@CommandInfo(name = "panic", description = "Выключает все функции чита")

public class PanicCommand extends Command {
    @Override
    public void run(String[] args) throws Exception {
        if (args.length == 1) {
            Managment.FUNCTION_MANAGER.getFunctions().stream().filter(function -> function.state).forEach(function -> function.setState(false));
            ClientUtil.sendMesage("Выключил все модули!");
        } else error();
    }

    @Override
    public void error() {

    }
}
