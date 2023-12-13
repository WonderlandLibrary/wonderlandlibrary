package org.apache.commons.compress.archivers.dump;

class Dirent {
   private final int ino;
   private final int parentIno;
   private final int type;
   private final String name;

   Dirent(int var1, int var2, int var3, String var4) {
      this.ino = var1;
      this.parentIno = var2;
      this.type = var3;
      this.name = var4;
   }

   int getIno() {
      return this.ino;
   }

   int getParentIno() {
      return this.parentIno;
   }

   int getType() {
      return this.type;
   }

   String getName() {
      return this.name;
   }

   public String toString() {
      return String.format("[%d]: %s", this.ino, this.name);
   }
}
