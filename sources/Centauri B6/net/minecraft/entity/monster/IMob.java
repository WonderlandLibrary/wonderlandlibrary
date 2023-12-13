package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import net.minecraft.entity.monster.IMob.1;
import net.minecraft.entity.monster.IMob.2;
import net.minecraft.entity.passive.IAnimals;

public interface IMob extends IAnimals {
   Predicate mobSelector = new 1();
   Predicate VISIBLE_MOB_SELECTOR = new 2();
}
