package vip.radium.command.impl;

import net.minecraft.util.EnumChatFormatting;
import vip.radium.RadiumClient;
import vip.radium.command.Command;
import vip.radium.command.CommandExecutionException;
import vip.radium.module.Module;
import vip.radium.property.Property;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.utils.Wrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ModuleCommand implements Command {

    @Override
    public String[] getAliases() {
        List<String> moduleAlises = new ArrayList<>();
        String[] alias = new String[moduleAlises.size()];
        RadiumClient.getInstance().getModuleManager().getModules().stream().filter(Objects::nonNull).filter(module ->
                !module.getElements().isEmpty()).forEach(module -> moduleAlises.add(module.getLabel().replaceAll(" ", "")));
        return moduleAlises.toArray(alias);
    }

    @Override
    public void execute(String[] arguments) throws CommandExecutionException {
        if (arguments.length != 3)
            throw new CommandExecutionException(getUsage());

        Module module = RadiumClient.getInstance().getModuleManager().getModule(arguments[0]);
        Property property = Property.getPropertyLabel(module, arguments[1]);

        if(property == null) {
            Wrapper.addChatMessage(EnumChatFormatting.RED + arguments[1] + " \u00A77Invalid Property");
            return;
        }

        try {
            if (property.getType() == Boolean.class) {
                property.setValue(Boolean.parseBoolean(arguments[2]));
                Wrapper.addChatMessage("Property \u00A76" + property.getLabel() + " \u00A77set to \u00A76" + property.getValue());
            } else if (property.getType() == Double.class) {
                property.setValue(Double.parseDouble(arguments[2]));
                Wrapper.addChatMessage("Property \u00A76" + property.getLabel() + " \u00A77set to \u00A76" + property.getValue());
            } else {
                EnumProperty enumProperty = (EnumProperty) property;
                Arrays.stream(enumProperty.getValues()).filter(option -> option.name().equalsIgnoreCase(arguments[2])).forEach(option -> {
                    enumProperty.setValue(option);
                    Wrapper.addChatMessage("Property \u00A76" + enumProperty.getLabel() + " \u00A77set to \u00A76" + enumProperty.getValue());
                });
            }
        } catch (Exception e) {
            Wrapper.addChatMessage(EnumChatFormatting.RED + arguments[2] + " \u00A77Invalid Type");
        }
    }

    @Override
    public String getUsage() {
        return "<module> <property> <value>";
    }
}
