package net.minecraft.client.resources.data;

import com.google.gson.JsonDeserializer;

public abstract interface IMetadataSectionSerializer
  extends JsonDeserializer
{
  public abstract String getSectionName();
}
