package org.alphacentauri.management.commands;

import java.util.ArrayList;
import org.alphacentauri.management.commands.Command;

public interface ICommandHandler {
   String getName();

   boolean execute(Command var1);

   String[] getAliases();

   String getDesc();

   ArrayList autocomplete(Command var1);
}
