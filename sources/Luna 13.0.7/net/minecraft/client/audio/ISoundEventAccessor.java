package net.minecraft.client.audio;

public abstract interface ISoundEventAccessor
{
  public abstract int getWeight();
  
  public abstract Object cloneEntry();
}
