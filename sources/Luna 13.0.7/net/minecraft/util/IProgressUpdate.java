package net.minecraft.util;

public abstract interface IProgressUpdate
{
  public abstract void displaySavingString(String paramString);
  
  public abstract void resetProgressAndMessage(String paramString);
  
  public abstract void displayLoadingString(String paramString);
  
  public abstract void setLoadingProgress(int paramInt);
  
  public abstract void setDoneWorking();
}
