package me.aidanmees.trivia.client.tools;

public abstract interface FontObject
{
  public abstract void drawString(String paramString, float paramFloat1, float paramFloat2, int paramInt);
  
  public abstract void drawStringWithShadow(String paramString, float paramFloat1, float paramFloat2, int paramInt);
  
  public abstract int getStringWith(String paramString);
  
  public abstract int getStringHeight(String paramString);
}
