package io.netty.buffer;

final class PoolChunk<T> {
  final PoolArena<T> arena;
  
  final T memory;
  
  final boolean unpooled;
  
  private final byte[] memoryMap;
  
  private final byte[] depthMap;
  
  private final PoolSubpage<T>[] subpages;
  
  private final int subpageOverflowMask;
  
  private final int pageSize;
  
  private final int pageShifts;
  
  private final int maxOrder;
  
  private final int chunkSize;
  
  private final int log2ChunkSize;
  
  private final int maxSubpageAllocs;
  
  private final byte unusable;
  
  private int freeBytes;
  
  PoolChunkList<T> parent;
  
  PoolChunk<T> prev;
  
  PoolChunk<T> next;
  
  PoolChunk(PoolArena<T> arena, T memory, int pageSize, int maxOrder, int pageShifts, int chunkSize) {
    this.unpooled = false;
    this.arena = arena;
    this.memory = memory;
    this.pageSize = pageSize;
    this.pageShifts = pageShifts;
    this.maxOrder = maxOrder;
    this.chunkSize = chunkSize;
    this.unusable = (byte)(maxOrder + 1);
    this.log2ChunkSize = log2(chunkSize);
    this.subpageOverflowMask = pageSize - 1 ^ 0xFFFFFFFF;
    this.freeBytes = chunkSize;
    assert maxOrder < 30 : "maxOrder should be < 30, but is: " + maxOrder;
    this.maxSubpageAllocs = 1 << maxOrder;
    this.memoryMap = new byte[this.maxSubpageAllocs << 1];
    this.depthMap = new byte[this.memoryMap.length];
    int memoryMapIndex = 1;
    for (int d = 0; d <= maxOrder; d++) {
      int depth = 1 << d;
      for (int p = 0; p < depth; p++) {
        this.memoryMap[memoryMapIndex] = (byte)d;
        this.depthMap[memoryMapIndex] = (byte)d;
        memoryMapIndex++;
      } 
    } 
    this.subpages = newSubpageArray(this.maxSubpageAllocs);
  }
  
  PoolChunk(PoolArena<T> arena, T memory, int size) {
    this.unpooled = true;
    this.arena = arena;
    this.memory = memory;
    this.memoryMap = null;
    this.depthMap = null;
    this.subpages = null;
    this.subpageOverflowMask = 0;
    this.pageSize = 0;
    this.pageShifts = 0;
    this.maxOrder = 0;
    this.unusable = (byte)(this.maxOrder + 1);
    this.chunkSize = size;
    this.log2ChunkSize = log2(this.chunkSize);
    this.maxSubpageAllocs = 0;
  }
  
  private PoolSubpage<T>[] newSubpageArray(int size) {
    return (PoolSubpage<T>[])new PoolSubpage[size];
  }
  
  int usage() {
    int freeBytes = this.freeBytes;
    if (freeBytes == 0)
      return 100; 
    int freePercentage = (int)(freeBytes * 100L / this.chunkSize);
    if (freePercentage == 0)
      return 99; 
    return 100 - freePercentage;
  }
  
  long allocate(int normCapacity) {
    if ((normCapacity & this.subpageOverflowMask) != 0)
      return allocateRun(normCapacity); 
    return allocateSubpage(normCapacity);
  }
  
  private void updateParentsAlloc(int id) {
    while (id > 1) {
      int parentId = id >>> 1;
      byte val1 = value(id);
      byte val2 = value(id ^ 0x1);
      byte val = (val1 < val2) ? val1 : val2;
      setValue(parentId, val);
      id = parentId;
    } 
  }
  
  private void updateParentsFree(int id) {
    int logChild = depth(id) + 1;
    while (id > 1) {
      int parentId = id >>> 1;
      byte val1 = value(id);
      byte val2 = value(id ^ 0x1);
      logChild--;
      if (val1 == logChild && val2 == logChild) {
        setValue(parentId, (byte)(logChild - 1));
      } else {
        byte val = (val1 < val2) ? val1 : val2;
        setValue(parentId, val);
      } 
      id = parentId;
    } 
  }
  
  private int allocateNode(int d) {
    int id = 1;
    int initial = -(1 << d);
    byte val = value(id);
    if (val > d)
      return -1; 
    while (val < d || (id & initial) == 0) {
      id <<= 1;
      val = value(id);
      if (val > d) {
        id ^= 0x1;
        val = value(id);
      } 
    } 
    byte value = value(id);
    assert value == d && (id & initial) == 1 << d : String.format("val = %d, id & initial = %d, d = %d", new Object[] { Byte.valueOf(value), Integer.valueOf(id & initial), Integer.valueOf(d) });
    setValue(id, this.unusable);
    updateParentsAlloc(id);
    return id;
  }
  
  private long allocateRun(int normCapacity) {
    int d = this.maxOrder - log2(normCapacity) - this.pageShifts;
    int id = allocateNode(d);
    if (id < 0)
      return id; 
    this.freeBytes -= runLength(id);
    return id;
  }
  
  private long allocateSubpage(int normCapacity) {
    int d = this.maxOrder;
    int id = allocateNode(d);
    if (id < 0)
      return id; 
    PoolSubpage<T>[] subpages = this.subpages;
    int pageSize = this.pageSize;
    this.freeBytes -= pageSize;
    int subpageIdx = subpageIdx(id);
    PoolSubpage<T> subpage = subpages[subpageIdx];
    if (subpage == null) {
      subpage = new PoolSubpage<T>(this, id, runOffset(id), pageSize, normCapacity);
      subpages[subpageIdx] = subpage;
    } else {
      subpage.init(normCapacity);
    } 
    return subpage.allocate();
  }
  
  void free(long handle) {
    int memoryMapIdx = (int)handle;
    int bitmapIdx = (int)(handle >>> 32L);
    if (bitmapIdx != 0) {
      PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
      assert subpage != null && subpage.doNotDestroy;
      if (subpage.free(bitmapIdx & 0x3FFFFFFF))
        return; 
    } 
    this.freeBytes += runLength(memoryMapIdx);
    setValue(memoryMapIdx, depth(memoryMapIdx));
    updateParentsFree(memoryMapIdx);
  }
  
  void initBuf(PooledByteBuf<T> buf, long handle, int reqCapacity) {
    int memoryMapIdx = (int)handle;
    int bitmapIdx = (int)(handle >>> 32L);
    if (bitmapIdx == 0) {
      byte val = value(memoryMapIdx);
      assert val == this.unusable : String.valueOf(val);
      buf.init(this, handle, runOffset(memoryMapIdx), reqCapacity, runLength(memoryMapIdx));
    } else {
      initBufWithSubpage(buf, handle, bitmapIdx, reqCapacity);
    } 
  }
  
  void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int reqCapacity) {
    initBufWithSubpage(buf, handle, (int)(handle >>> 32L), reqCapacity);
  }
  
  private void initBufWithSubpage(PooledByteBuf<T> buf, long handle, int bitmapIdx, int reqCapacity) {
    assert bitmapIdx != 0;
    int memoryMapIdx = (int)handle;
    PoolSubpage<T> subpage = this.subpages[subpageIdx(memoryMapIdx)];
    assert subpage.doNotDestroy;
    assert reqCapacity <= subpage.elemSize;
    buf.init(this, handle, runOffset(memoryMapIdx) + (bitmapIdx & 0x3FFFFFFF) * subpage.elemSize, reqCapacity, subpage.elemSize);
  }
  
  private byte value(int id) {
    return this.memoryMap[id];
  }
  
  private void setValue(int id, byte val) {
    this.memoryMap[id] = val;
  }
  
  private byte depth(int id) {
    return this.depthMap[id];
  }
  
  private static int log2(int val) {
    return 31 - Integer.numberOfLeadingZeros(val);
  }
  
  private int runLength(int id) {
    return 1 << this.log2ChunkSize - depth(id);
  }
  
  private int runOffset(int id) {
    int shift = id ^ 1 << depth(id);
    return shift * runLength(id);
  }
  
  private int subpageIdx(int memoryMapIdx) {
    return memoryMapIdx ^ this.maxSubpageAllocs;
  }
  
  public String toString() {
    StringBuilder buf = new StringBuilder();
    buf.append("Chunk(");
    buf.append(Integer.toHexString(System.identityHashCode(this)));
    buf.append(": ");
    buf.append(usage());
    buf.append("%, ");
    buf.append(this.chunkSize - this.freeBytes);
    buf.append('/');
    buf.append(this.chunkSize);
    buf.append(')');
    return buf.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\netty\buffer\PoolChunk.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */