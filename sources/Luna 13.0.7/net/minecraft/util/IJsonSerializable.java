package net.minecraft.util;

import com.google.gson.JsonElement;

public abstract interface IJsonSerializable
{
  public abstract void func_152753_a(JsonElement paramJsonElement);
  
  public abstract JsonElement getSerializableElement();
}
