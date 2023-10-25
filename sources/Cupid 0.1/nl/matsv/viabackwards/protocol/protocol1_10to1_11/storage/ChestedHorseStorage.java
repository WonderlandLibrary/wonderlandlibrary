package nl.matsv.viabackwards.protocol.protocol1_10to1_11.storage;

import nl.matsv.viabackwards.api.entities.storage.EntityStorage;

public class ChestedHorseStorage implements EntityStorage {
  private boolean chested;
  
  private int liamaStrength;
  
  private int liamaCarpetColor = -1;
  
  private int liamaVariant;
  
  public boolean isChested() {
    return this.chested;
  }
  
  public void setChested(boolean chested) {
    this.chested = chested;
  }
  
  public int getLiamaStrength() {
    return this.liamaStrength;
  }
  
  public void setLiamaStrength(int liamaStrength) {
    this.liamaStrength = liamaStrength;
  }
  
  public int getLiamaCarpetColor() {
    return this.liamaCarpetColor;
  }
  
  public void setLiamaCarpetColor(int liamaCarpetColor) {
    this.liamaCarpetColor = liamaCarpetColor;
  }
  
  public int getLiamaVariant() {
    return this.liamaVariant;
  }
  
  public void setLiamaVariant(int liamaVariant) {
    this.liamaVariant = liamaVariant;
  }
  
  public String toString() {
    return "ChestedHorseStorage{chested=" + this.chested + ", liamaStrength=" + this.liamaStrength + ", liamaCarpetColor=" + this.liamaCarpetColor + ", liamaVariant=" + this.liamaVariant + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\storage\ChestedHorseStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */