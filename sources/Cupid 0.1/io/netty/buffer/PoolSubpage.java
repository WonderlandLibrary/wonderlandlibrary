package io.netty.buffer;

final class PoolSubpage<T> {
  final PoolChunk<T> chunk;
  
  private final int memoryMapIdx;
  
  private final int runOffset;
  
  private final int pageSize;
  
  private final long[] bitmap;
  
  PoolSubpage<T> prev;
  
  PoolSubpage<T> next;
  
  boolean doNotDestroy;
  
  int elemSize;
  
  private int maxNumElems;
  
  private int bitmapLength;
  
  private int nextAvail;
  
  private int numAvail;
  
  PoolSubpage(int pageSize) {
    this.chunk = null;
    this.memoryMapIdx = -1;
    this.runOffset = -1;
    this.elemSize = -1;
    this.pageSize = pageSize;
    this.bitmap = null;
  }
  
  PoolSubpage(PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
    this.chunk = chunk;
    this.memoryMapIdx = memoryMapIdx;
    this.runOffset = runOffset;
    this.pageSize = pageSize;
    this.bitmap = new long[pageSize >>> 10];
    init(elemSize);
  }
  
  void init(int elemSize) {
    this.doNotDestroy = true;
    this.elemSize = elemSize;
    if (elemSize != 0) {
      this.maxNumElems = this.numAvail = this.pageSize / elemSize;
      this.nextAvail = 0;
      this.bitmapLength = this.maxNumElems >>> 6;
      if ((this.maxNumElems & 0x3F) != 0)
        this.bitmapLength++; 
      for (int i = 0; i < this.bitmapLength; i++)
        this.bitmap[i] = 0L; 
    } 
    addToPool();
  }
  
  long allocate() {
    if (this.elemSize == 0)
      return toHandle(0); 
    if (this.numAvail == 0 || !this.doNotDestroy)
      return -1L; 
    int bitmapIdx = getNextAvail();
    int q = bitmapIdx >>> 6;
    int r = bitmapIdx & 0x3F;
    assert (this.bitmap[q] >>> r & 0x1L) == 0L;
    this.bitmap[q] = this.bitmap[q] | 1L << r;
    if (--this.numAvail == 0)
      removeFromPool(); 
    return toHandle(bitmapIdx);
  }
  
  boolean free(int bitmapIdx) {
    if (this.elemSize == 0)
      return true; 
    int q = bitmapIdx >>> 6;
    int r = bitmapIdx & 0x3F;
    assert (this.bitmap[q] >>> r & 0x1L) != 0L;
    this.bitmap[q] = this.bitmap[q] ^ 1L << r;
    setNextAvail(bitmapIdx);
    if (this.numAvail++ == 0) {
      addToPool();
      return true;
    } 
    if (this.numAvail != this.maxNumElems)
      return true; 
    if (this.prev == this.next)
      return true; 
    this.doNotDestroy = false;
    removeFromPool();
    return false;
  }
  
  private void addToPool() {
    PoolSubpage<T> head = this.chunk.arena.findSubpagePoolHead(this.elemSize);
    assert this.prev == null && this.next == null;
    this.prev = head;
    this.next = head.next;
    this.next.prev = this;
    head.next = this;
  }
  
  private void removeFromPool() {
    assert this.prev != null && this.next != null;
    this.prev.next = this.next;
    this.next.prev = this.prev;
    this.next = null;
    this.prev = null;
  }
  
  private void setNextAvail(int bitmapIdx) {
    this.nextAvail = bitmapIdx;
  }
  
  private int getNextAvail() {
    int nextAvail = this.nextAvail;
    if (nextAvail >= 0) {
      this.nextAvail = -1;
      return nextAvail;
    } 
    return findNextAvail();
  }
  
  private int findNextAvail() {
    long[] bitmap = this.bitmap;
    int bitmapLength = this.bitmapLength;
    for (int i = 0; i < bitmapLength; i++) {
      long bits = bitmap[i];
      if ((bits ^ 0xFFFFFFFFFFFFFFFFL) != 0L)
        return findNextAvail0(i, bits); 
    } 
    return -1;
  }
  
  private int findNextAvail0(int i, long bits) {
    int maxNumElems = this.maxNumElems;
    int baseVal = i << 6;
    for (int j = 0; j < 64; j++) {
      if ((bits & 0x1L) == 0L) {
        int val = baseVal | j;
        if (val < maxNumElems)
          return val; 
        break;
      } 
      bits >>>= 1L;
    } 
    return -1;
  }
  
  private long toHandle(int bitmapIdx) {
    return 0x4000000000000000L | bitmapIdx << 32L | this.memoryMapIdx;
  }
  
  public String toString() {
    if (!this.doNotDestroy)
      return "(" + this.memoryMapIdx + ": not in use)"; 
    return String.valueOf('(') + this.memoryMapIdx + ": " + (this.maxNumElems - this.numAvail) + '/' + this.maxNumElems + ", offset: " + this.runOffset + ", length: " + this.pageSize + ", elemSize: " + this.elemSize + ')';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\PoolSubpage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */