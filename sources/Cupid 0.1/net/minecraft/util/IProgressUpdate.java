package net.minecraft.util;

public interface IProgressUpdate {
  void displaySavingString(String paramString);
  
  void resetProgressAndMessage(String paramString);
  
  void displayLoadingString(String paramString);
  
  void setLoadingProgress(int paramInt);
  
  void setDoneWorking();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\IProgressUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */