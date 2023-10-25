/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.command.exception;

import baritone.api.command.ICommand;
import baritone.api.command.argument.ICommandArgument;
import baritone.api.command.exception.ICommandException;
import baritone.api.utils.Helper;
import java.util.List;
import net.minecraft.util.text.TextFormatting;

public class CommandUnhandledException
extends RuntimeException
implements ICommandException {
    public CommandUnhandledException(String message) {
        super(message);
    }

    public CommandUnhandledException(Throwable cause) {
        super(cause);
    }

    @Override
    public void handle(ICommand command, List<ICommandArgument> args) {
        Helper.HELPER.logDirect("\u041f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430 \u043f\u0435\u0440\u0435\u0437\u0430\u0439\u0434\u0438\u0442\u0435 \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 \u0434\u043b\u044f \u043a\u043e\u0440\u0440\u0435\u043a\u0442\u043d\u043e\u0439 \u0440\u0430\u0431\u043e\u0442\u044b \u0431\u0430\u0440\u0438\u0442\u043e\u043d\u0430!", TextFormatting.RED);
        this.printStackTrace();
    }
}

