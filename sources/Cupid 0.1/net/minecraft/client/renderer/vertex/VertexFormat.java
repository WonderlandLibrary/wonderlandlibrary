package net.minecraft.client.renderer.vertex;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VertexFormat {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public VertexFormat(VertexFormat vertexFormatIn) {
    this();
    for (int i = 0; i < vertexFormatIn.getElementCount(); i++)
      func_181721_a(vertexFormatIn.getElement(i)); 
    this.nextOffset = vertexFormatIn.getNextOffset();
  }
  
  private final List<VertexFormatElement> elements = Lists.newArrayList();
  
  private final List<Integer> offsets = Lists.newArrayList();
  
  private int nextOffset = 0;
  
  private int colorElementOffset = -1;
  
  private List<Integer> uvOffsetsById = Lists.newArrayList();
  
  private int normalElementOffset = -1;
  
  public VertexFormat() {}
  
  public void clear() {
    this.elements.clear();
    this.offsets.clear();
    this.colorElementOffset = -1;
    this.uvOffsetsById.clear();
    this.normalElementOffset = -1;
    this.nextOffset = 0;
  }
  
  public VertexFormat func_181721_a(VertexFormatElement p_181721_1_) {
    if (p_181721_1_.isPositionElement() && hasPosition()) {
      LOGGER.warn("VertexFormat error: Trying to add a position VertexFormatElement when one already exists, ignoring.");
      return this;
    } 
    this.elements.add(p_181721_1_);
    this.offsets.add(Integer.valueOf(this.nextOffset));
    switch (p_181721_1_.getUsage()) {
      case NORMAL:
        this.normalElementOffset = this.nextOffset;
        break;
      case COLOR:
        this.colorElementOffset = this.nextOffset;
        break;
      case UV:
        this.uvOffsetsById.add(p_181721_1_.getIndex(), Integer.valueOf(this.nextOffset));
        break;
    } 
    this.nextOffset += p_181721_1_.getSize();
    return this;
  }
  
  public boolean hasNormal() {
    return (this.normalElementOffset >= 0);
  }
  
  public int getNormalOffset() {
    return this.normalElementOffset;
  }
  
  public boolean hasColor() {
    return (this.colorElementOffset >= 0);
  }
  
  public int getColorOffset() {
    return this.colorElementOffset;
  }
  
  public boolean hasUvOffset(int id) {
    return (this.uvOffsetsById.size() - 1 >= id);
  }
  
  public int getUvOffsetById(int id) {
    return ((Integer)this.uvOffsetsById.get(id)).intValue();
  }
  
  public String toString() {
    String s = "format: " + this.elements.size() + " elements: ";
    for (int i = 0; i < this.elements.size(); i++) {
      s = s + ((VertexFormatElement)this.elements.get(i)).toString();
      if (i != this.elements.size() - 1)
        s = s + " "; 
    } 
    return s;
  }
  
  private boolean hasPosition() {
    int i = 0;
    for (int j = this.elements.size(); i < j; i++) {
      VertexFormatElement vertexformatelement = this.elements.get(i);
      if (vertexformatelement.isPositionElement())
        return true; 
    } 
    return false;
  }
  
  public int func_181719_f() {
    return getNextOffset() / 4;
  }
  
  public int getNextOffset() {
    return this.nextOffset;
  }
  
  public List<VertexFormatElement> getElements() {
    return this.elements;
  }
  
  public int getElementCount() {
    return this.elements.size();
  }
  
  public VertexFormatElement getElement(int index) {
    return this.elements.get(index);
  }
  
  public int func_181720_d(int p_181720_1_) {
    return ((Integer)this.offsets.get(p_181720_1_)).intValue();
  }
  
  public boolean equals(Object p_equals_1_) {
    if (this == p_equals_1_)
      return true; 
    if (p_equals_1_ != null && getClass() == p_equals_1_.getClass()) {
      VertexFormat vertexformat = (VertexFormat)p_equals_1_;
      return (this.nextOffset != vertexformat.nextOffset) ? false : (!this.elements.equals(vertexformat.elements) ? false : this.offsets.equals(vertexformat.offsets));
    } 
    return false;
  }
  
  public int hashCode() {
    int i = this.elements.hashCode();
    i = 31 * i + this.offsets.hashCode();
    i = 31 * i + this.nextOffset;
    return i;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\renderer\vertex\VertexFormat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */