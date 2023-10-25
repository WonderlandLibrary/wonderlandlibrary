package net.minecraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;

public class TileEntityComparator extends TileEntity {
  private int outputSignal;
  
  public void writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setInteger("OutputSignal", this.outputSignal);
  }
  
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.outputSignal = compound.getInteger("OutputSignal");
  }
  
  public int getOutputSignal() {
    return this.outputSignal;
  }
  
  public void setOutputSignal(int p_145995_1_) {
    this.outputSignal = p_145995_1_;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\tileentity\TileEntityComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */