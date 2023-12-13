package net.minecraft.entity;

import net.minecraft.entity.Entity;

public interface IEntityOwnable {
   Entity getOwner();

   String getOwnerId();
}
