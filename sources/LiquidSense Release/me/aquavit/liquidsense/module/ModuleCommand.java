package me.aquavit.liquidsense.module;

import me.aquavit.liquidsense.utils.block.BlockUtils;
import me.aquavit.liquidsense.utils.misc.StringUtils;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.value.*;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleCommand extends Command {

    private final Module module;

    private final List<Value<?>> values;

    @Override
    public void execute(String[] args) {
        String valueNames = values.stream().filter(it -> !(it instanceof FontValue)).map(it -> it.getName().toLowerCase()).collect(Collectors.joining("/", "",""));

        String moduleName = module.getName().toLowerCase();

        if (args.length < 2) {
            this.chatSyntax(this.values.size() == 1 ? moduleName + ' ' + valueNames + " <value>" : moduleName + " <" + valueNames + '>');
            return;
        }

        Value<?> value = module.getValue(args[1]);
        if (value == null) {
            this.chatSyntax(moduleName + " <" + valueNames + '>');
            return;
        }

        if (value instanceof BoolValue) {
            boolean newValue = !((BoolValue) value).get();
            ((BoolValue)value).set(newValue);
            this.chat("§7" + this.module.getName() + " §8" + args[1] + "§7 was toggled " + (newValue ? "§8on§7" : "§8off§7."));
            this.playEdit();
        } else {
            if (args.length < 3) {
                if (value instanceof IntegerValue || value instanceof FloatValue || value instanceof TextValue) {
                    chatSyntax(moduleName + " " + args[1].toLowerCase() + " <value>");
                } else if (value instanceof ListValue) {
                    chatSyntax(moduleName + " " + args[1].toLowerCase() + " <" + Arrays.stream(((ListValue) value).getValues()).collect(Collectors.joining("/", "", "")).toLowerCase() + ">");
                }
                return;
            }

            try {
                if (value instanceof BlockValue) {
                    int id;
                    try {
                        id = Integer.parseInt(args[2]);
                    } catch (NumberFormatException exception) {
                        id = Block.getIdFromBlock(Block.getBlockFromName(args[2]));

                        if (id <= 0) {
                            this.chat("§7Block §8" + args[2] + "§7 does not exist!");
                            return;
                        }
                    }

                    ((BlockValue) value).set(id);
                    chat("§7" + module.getName() + " §8" + args[1].toLowerCase() + "§7 was set to §8" + BlockUtils.getBlockName(id) + "§7.");
                    playEdit();
                    return;
                } else if (value instanceof IntegerValue) {
                    ((IntegerValue) value).set(Integer.parseInt(args[2]));
                } else if (value instanceof FloatValue) {
                    ((FloatValue) value).set(Float.parseFloat(args[2]));
                } else if (value instanceof ListValue) {
                    if (!((ListValue) value).contains(args[2])) {
                        chatSyntax(moduleName + " " + args[1].toLowerCase() + " <" + Arrays.stream(((ListValue) value).getValues()).collect(Collectors.joining("/", "", "")).toLowerCase() + ">");
                        return;
                    }

                    ((ListValue) value).set(args[2]);
                } else if (value instanceof TextValue) {
                    ((TextValue) value).set(StringUtils.toCompleteString(args, 2));
                }
                chat("§7" + this.module.getName() + " §8" + args[1] + "§7 was set to §8" + value.get() + "§7.");
                playEdit();


            } catch (NumberFormatException e) {
                this.chat("§8" + args[2] + "§7 cannot be converted to number!");
            }
        }
    }

    @Override
    public List<String> tabComplete(String[] args) {
        if (args.length == 0) return new ArrayList<>();

        switch (args.length) {
            case 1:
                return values.stream().filter(it -> !(it instanceof FontValue) && it.getName().startsWith(args[0]))
                        .map(it -> it.getName().toLowerCase()).collect(Collectors.toList());
            case 2:{
                if (module.getValue(args[0]) instanceof BlockValue) {
                    return Block.blockRegistry.getKeys().stream().map(it -> it.getResourcePath().toLowerCase()).filter(it -> it.startsWith(args[1])).collect(Collectors.toList());
                }
                return new ArrayList<>();
            }
            default:
                return new ArrayList<>();
        }
    }

    public final Module getModule() {
        return this.module;
    }

    public final List<Value<?>> getValues() {
        return this.values;
    }

    public ModuleCommand(Module module, List<Value<?>> values) {
        super(module.getName().toLowerCase());
        this.module = module;
        this.values = values;
        if (this.values.isEmpty()) {
            throw new IllegalArgumentException("Values are empty!");
        }
    }
}
