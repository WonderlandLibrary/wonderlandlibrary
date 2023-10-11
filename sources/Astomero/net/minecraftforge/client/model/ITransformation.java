package net.minecraftforge.client.model;

import javax.vecmath.*;
import net.minecraft.util.*;

public interface ITransformation
{
    Matrix4f getMatrix();
    
    EnumFacing rotate(final EnumFacing p0);
    
    int rotate(final EnumFacing p0, final int p1);
}
