package net.minecraft.client.gui;

public abstract interface IProgressMeter
{
  public static final String[] lanSearchStates = { "oooooo", "Oooooo", "oOoooo", "ooOooo", "oooOoo", "ooooOo", "oooooO" };
  
  public abstract void doneLoading();
}
