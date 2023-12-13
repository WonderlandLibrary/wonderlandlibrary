package org.apache.commons.compress.archivers.zip;

public enum Zip64Mode {
   Always,
   Never,
   AsNeeded;

   private static final Zip64Mode[] $VALUES = new Zip64Mode[]{Always, Never, AsNeeded};
}
