package net.minecraft.world.gen.structure;

import java.util.Random;
import net.minecraft.world.World;

public class StructureMineshaftStart extends StructureStart {
   public StructureMineshaftStart() {
   }

   public StructureMineshaftStart(World var1, Random var2, int var3, int var4) {
      super(var3, var4);
      StructureMineshaftPieces.Room var5 = new StructureMineshaftPieces.Room(0, var2, (var3 << 4) + 2, (var4 << 4) + 2);
      this.components.add(var5);
      var5.buildComponent(var5, this.components, var2);
      this.updateBoundingBox();
      this.markAvailableHeight(var1, var2, 10);
   }
}
