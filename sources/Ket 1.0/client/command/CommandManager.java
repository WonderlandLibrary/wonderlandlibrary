package client.command;

import client.Client;
import client.event.EventLink;
import client.event.Listener;
import client.event.impl.input.ChatInputEvent;
import client.module.DevelopmentFeature;
import client.util.ChatUtil;
import client.value.impl.BooleanValue;
import client.value.impl.ModeValue;
import client.value.impl.NumberValue;
import client.value.impl.StringValue;
import org.atteo.classindex.ClassIndex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CommandManager extends ArrayList<Command> {

    public void init() {
        ClassIndex.getSubclasses(Command.class, Command.class.getClassLoader()).forEach(clazz -> {
            try {
                if (!clazz.isAnnotationPresent(DevelopmentFeature.class) || Client.DEVELOPMENT_SWITCH) add(clazz.newInstance());
            } catch (Exception e) {
                if (Client.DEVELOPMENT_SWITCH) e.printStackTrace();
            }
        });
        Client.INSTANCE.getEventBus().register(this);
    }

    public <T extends Command> T get(final String name) {
        return (T) stream().filter(command -> Arrays.stream(command.getExpressions()).anyMatch(expression -> expression.equalsIgnoreCase(name))).findAny().orElse(null);
    }

    public <T extends Command> T get(final Class<? extends Command> clazz) {
        return (T) stream().filter(command -> command.getClass() == clazz).findAny().orElse(null);
    }

    @EventLink
    public final Listener<ChatInputEvent> onChatInput = event -> {
        String message = event.getMessage();
        if (!message.startsWith(".")) return;
        message = message.substring(1);
        final String[] args = message.split(" ");
        final AtomicBoolean commandFound = new AtomicBoolean();
        stream().filter(command -> Arrays.stream(command.getExpressions()).anyMatch(expression -> expression.equalsIgnoreCase(args[0]))).forEach(command -> {
            commandFound.set(true);
            command.execute(args);
        });
        Client.INSTANCE.getModuleManager().getAll().stream().filter(module -> module.getInfo().name().replace(" ", "").equalsIgnoreCase(args[0])).forEach(module -> {
            commandFound.set(true);
            switch (args.length) {
                case 2: {
                    final AtomicBoolean valueFound = new AtomicBoolean();
                    module.getAllValues().stream().filter(value -> value.getName().replace(" ", "").equalsIgnoreCase(args[1])).forEach(value -> {
                        valueFound.set(true);
                        if (value instanceof BooleanValue) {
                            final BooleanValue booleanValue = (BooleanValue) value;
                            booleanValue.setValue(!booleanValue.getValue());
                            ChatUtil.display("%s -> %s = %s", module.getInfo().name(), value.getName(), booleanValue.getValue());
                        } else {
                            ChatUtil.display("§cInvalid command arguments.");
                            ChatUtil.display("Correct Usage: " + String.format(".%s <key> <value>", args[0]));
                        }
                    });
                    if (!valueFound.get()) ChatUtil.display("Boolean value %s does not exist", args[1]);
                    break;
                }
                case 3: {
                    final AtomicBoolean valueFound = new AtomicBoolean();
                    module.getAllValues().stream().filter(value -> value.getName().replace(" ", "").equalsIgnoreCase(args[1])).forEach(value -> {
                        valueFound.set(true);
                        if (value instanceof BooleanValue) {
                            final BooleanValue booleanValue = (BooleanValue) value;
                            switch (args[2].toLowerCase()) {
                                case "true": {
                                    booleanValue.setValue(true);
                                    ChatUtil.display("%s -> %s = %s", module.getInfo().name(), value.getName(), booleanValue.getValue());
                                    break;
                                }
                                case "false": {
                                    booleanValue.setValue(false);
                                    ChatUtil.display("%s -> %s = %s", module.getInfo().name(), value.getName(), booleanValue.getValue());
                                    break;
                                }
                                default: {
                                    ChatUtil.display("§cInvalid command arguments.");
                                    ChatUtil.display("Correct Usage: " + String.format(".%s <key> <value>", module.getInfo().name()));
                                    ChatUtil.display("Boolean value can only be true or false");
                                    break;
                                }
                            }
                        }
                        if (value instanceof ModeValue) {
                            final ModeValue modeValue = (ModeValue) value;
                            final AtomicBoolean modeFound = new AtomicBoolean();
                            modeValue.getModes().stream().filter(mode -> mode.getName().equalsIgnoreCase(args[2])).forEach(mode -> {
                                modeFound.set(true);
                                modeValue.setValue(mode);
                                ChatUtil.display("%s -> %s = %s", module.getInfo().name(), value.getName(), mode.getName());
                            });
                            if (!modeFound.get()) ChatUtil.display("%s mode does not exist", args[2]);
                        }
                        if (value instanceof NumberValue) {
                            final NumberValue numberValue = (NumberValue) value;
                            numberValue.setValue(Double.parseDouble(args[2]));
                            ChatUtil.display("%s -> %s = %s", module.getInfo().name(), value.getName(), numberValue.getValue().doubleValue());
                        }
                        if (value instanceof StringValue) {
                            final StringValue stringValue = (StringValue) value;
                            stringValue.setValue(args[2]);
                        }
                    });
                    if (!valueFound.get()) ChatUtil.display("Value %s does not exist", args[1]);
                    break;
                }
                default: {
                    module.getAllValues().stream().filter(value -> value instanceof StringValue && value.getName().replace(" ", "").equalsIgnoreCase(args[1])).forEach(value -> {
                        final StringValue stringValue = (StringValue) value;
                        String s = "";
                        for (int i = 2; i < args.length; i++) {
                            if (i != 2) s += " ";
                            s += args[i];
                        }
                        stringValue.setValue(s);
                    });
                    break;
                }
            }
        });
        if (!commandFound.get()) ChatUtil.display("Unknown command! Try .help if you're lost");
        event.setCancelled(true);
    };
}