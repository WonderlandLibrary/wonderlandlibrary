package me.rhys.base.event.impl.init;

import java.util.Arrays;
import me.rhys.base.command.Command;
import me.rhys.base.command.CommandFactory;
import me.rhys.base.event.Event;

public class CommandInitializeEvent extends Event {
  private final CommandFactory factory;
  
  public CommandInitializeEvent(CommandFactory factory) {
    this.factory = factory;
  }
  
  public void register(Command command) {
    this.factory.add(command);
  }
  
  public void register(Command... commands) {
    Arrays.<Command>stream(commands).forEach(this::register);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\init\CommandInitializeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */