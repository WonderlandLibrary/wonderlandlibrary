package net.minecraftforge.client.model;

import com.google.common.base.Function;

public abstract interface IModelState
  extends Function<IModelPart, TRSRTransformation>
{
  public abstract TRSRTransformation apply(IModelPart paramIModelPart);
}
