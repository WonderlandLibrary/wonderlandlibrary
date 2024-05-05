package net.minecraft.command;

public abstract interface IAdminCommand
{
  public abstract void notifyOperators(ICommandSender paramICommandSender, ICommand paramICommand, int paramInt, String paramString, Object... paramVarArgs);
}
