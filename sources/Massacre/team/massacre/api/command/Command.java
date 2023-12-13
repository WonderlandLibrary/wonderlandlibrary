package team.massacre.api.command;

public interface Command {
   String[] getAliases();

   void execute(String[] var1) throws CommandExecutionException;

   String getUsage();

   boolean testArgsCount(int var1);
}
