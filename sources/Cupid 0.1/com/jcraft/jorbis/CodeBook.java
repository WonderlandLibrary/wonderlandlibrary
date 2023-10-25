package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class CodeBook {
  int dim;
  
  int entries;
  
  StaticCodeBook c = new StaticCodeBook();
  
  float[] valuelist;
  
  int[] codelist;
  
  DecodeAux decode_tree;
  
  int encode(int a, Buffer b) {
    b.write(this.codelist[a], this.c.lengthlist[a]);
    return this.c.lengthlist[a];
  }
  
  int errorv(float[] a) {
    int best = best(a, 1);
    for (int k = 0; k < this.dim; k++)
      a[k] = this.valuelist[best * this.dim + k]; 
    return best;
  }
  
  int encodev(int best, float[] a, Buffer b) {
    for (int k = 0; k < this.dim; k++)
      a[k] = this.valuelist[best * this.dim + k]; 
    return encode(best, b);
  }
  
  int encodevs(float[] a, Buffer b, int step, int addmul) {
    int best = besterror(a, step, addmul);
    return encode(best, b);
  }
  
  private int[] t = new int[15];
  
  synchronized int decodevs_add(float[] a, int offset, Buffer b, int n) {
    int step = n / this.dim;
    if (this.t.length < step)
      this.t = new int[step]; 
    int i;
    for (i = 0; i < step; i++) {
      int entry = decode(b);
      if (entry == -1)
        return -1; 
      this.t[i] = entry * this.dim;
    } 
    int o;
    for (i = 0, o = 0; i < this.dim; i++, o += step) {
      for (int j = 0; j < step; j++)
        a[offset + o + j] = a[offset + o + j] + this.valuelist[this.t[j] + i]; 
    } 
    return 0;
  }
  
  int decodev_add(float[] a, int offset, Buffer b, int n) {
    if (this.dim > 8) {
      for (int i = 0; i < n; ) {
        int entry = decode(b);
        if (entry == -1)
          return -1; 
        int t = entry * this.dim;
        for (int j = 0; j < this.dim;)
          a[offset + i++] = a[offset + i++] + this.valuelist[t + j++]; 
      } 
    } else {
      for (int i = 0; i < n; ) {
        int entry = decode(b);
        if (entry == -1)
          return -1; 
        int t = entry * this.dim;
        int j = 0;
        switch (this.dim) {
          case 8:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 7:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 6:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 5:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 4:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 3:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 2:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
          case 1:
            a[offset + i++] = a[offset + i++] + this.valuelist[t + j++];
        } 
      } 
    } 
    return 0;
  }
  
  int decodev_set(float[] a, int offset, Buffer b, int n) {
    for (int i = 0; i < n; ) {
      int entry = decode(b);
      if (entry == -1)
        return -1; 
      int t = entry * this.dim;
      for (int j = 0; j < this.dim;)
        a[offset + i++] = this.valuelist[t + j++]; 
    } 
    return 0;
  }
  
  int decodevv_add(float[][] a, int offset, int ch, Buffer b, int n) {
    int chptr = 0;
    for (int i = offset / ch; i < (offset + n) / ch; ) {
      int entry = decode(b);
      if (entry == -1)
        return -1; 
      int t = entry * this.dim;
      for (int j = 0; j < this.dim; j++) {
        a[chptr++][i] = a[chptr++][i] + this.valuelist[t + j];
        if (chptr == ch) {
          chptr = 0;
          i++;
        } 
      } 
    } 
    return 0;
  }
  
  int decode(Buffer b) {
    int ptr = 0;
    DecodeAux t = this.decode_tree;
    int lok = b.look(t.tabn);
    if (lok >= 0) {
      ptr = t.tab[lok];
      b.adv(t.tabl[lok]);
      if (ptr <= 0)
        return -ptr; 
    } 
    while (true) {
      switch (b.read1()) {
        case 0:
          ptr = t.ptr0[ptr];
          break;
        case 1:
          ptr = t.ptr1[ptr];
          break;
        default:
          return -1;
      } 
      if (ptr <= 0)
        return -ptr; 
    } 
  }
  
  int decodevs(float[] a, int index, Buffer b, int step, int addmul) {
    int i, o, entry = decode(b);
    if (entry == -1)
      return -1; 
    switch (addmul) {
      case -1:
        for (i = 0, o = 0; i < this.dim; i++, o += step)
          a[index + o] = this.valuelist[entry * this.dim + i]; 
        break;
      case 0:
        for (i = 0, o = 0; i < this.dim; i++, o += step)
          a[index + o] = a[index + o] + this.valuelist[entry * this.dim + i]; 
        break;
      case 1:
        for (i = 0, o = 0; i < this.dim; i++, o += step)
          a[index + o] = a[index + o] * this.valuelist[entry * this.dim + i]; 
        break;
    } 
    return entry;
  }
  
  int best(float[] a, int step) {
    int besti = -1;
    float best = 0.0F;
    int e = 0;
    for (int i = 0; i < this.entries; i++) {
      if (this.c.lengthlist[i] > 0) {
        float _this = dist(this.dim, this.valuelist, e, a, step);
        if (besti == -1 || _this < best) {
          best = _this;
          besti = i;
        } 
      } 
      e += this.dim;
    } 
    return besti;
  }
  
  int besterror(float[] a, int step, int addmul) {
    int i, o, best = best(a, step);
    switch (addmul) {
      case 0:
        for (i = 0, o = 0; i < this.dim; i++, o += step)
          a[o] = a[o] - this.valuelist[best * this.dim + i]; 
        break;
      case 1:
        for (i = 0, o = 0; i < this.dim; i++, o += step) {
          float val = this.valuelist[best * this.dim + i];
          if (val == 0.0F) {
            a[o] = 0.0F;
          } else {
            a[o] = a[o] / val;
          } 
        } 
        break;
    } 
    return best;
  }
  
  void clear() {}
  
  private static float dist(int el, float[] ref, int index, float[] b, int step) {
    float acc = 0.0F;
    for (int i = 0; i < el; i++) {
      float val = ref[index + i] - b[i * step];
      acc += val * val;
    } 
    return acc;
  }
  
  int init_decode(StaticCodeBook s) {
    this.c = s;
    this.entries = s.entries;
    this.dim = s.dim;
    this.valuelist = s.unquantize();
    this.decode_tree = make_decode_tree();
    if (this.decode_tree == null) {
      clear();
      return -1;
    } 
    return 0;
  }
  
  static int[] make_words(int[] l, int n) {
    int[] marker = new int[33];
    int[] r = new int[n];
    int i;
    for (i = 0; i < n; i++) {
      int length = l[i];
      if (length > 0) {
        int entry = marker[length];
        if (length < 32 && entry >>> length != 0)
          return null; 
        r[i] = entry;
        int j;
        for (j = length; j > 0; j--) {
          if ((marker[j] & 0x1) != 0) {
            if (j == 1) {
              marker[1] = marker[1] + 1;
              break;
            } 
            marker[j] = marker[j - 1] << 1;
            break;
          } 
          marker[j] = marker[j] + 1;
        } 
        for (j = length + 1; j < 33 && 
          marker[j] >>> 1 == entry; j++) {
          entry = marker[j];
          marker[j] = marker[j - 1] << 1;
        } 
      } 
    } 
    for (i = 0; i < n; i++) {
      int temp = 0;
      for (int j = 0; j < l[i]; j++) {
        temp <<= 1;
        temp |= r[i] >>> j & 0x1;
      } 
      r[i] = temp;
    } 
    return r;
  }
  
  DecodeAux make_decode_tree() {
    int top = 0;
    DecodeAux t = new DecodeAux();
    int[] ptr0 = t.ptr0 = new int[this.entries * 2];
    int[] ptr1 = t.ptr1 = new int[this.entries * 2];
    int[] codelist = make_words(this.c.lengthlist, this.c.entries);
    if (codelist == null)
      return null; 
    t.aux = this.entries * 2;
    for (int i = 0; i < this.entries; i++) {
      if (this.c.lengthlist[i] > 0) {
        int ptr = 0;
        int k;
        for (k = 0; k < this.c.lengthlist[i] - 1; k++) {
          int bit = codelist[i] >>> k & 0x1;
          if (bit == 0) {
            if (ptr0[ptr] == 0)
              ptr0[ptr] = ++top; 
            ptr = ptr0[ptr];
          } else {
            if (ptr1[ptr] == 0)
              ptr1[ptr] = ++top; 
            ptr = ptr1[ptr];
          } 
        } 
        if ((codelist[i] >>> k & 0x1) == 0) {
          ptr0[ptr] = -i;
        } else {
          ptr1[ptr] = -i;
        } 
      } 
    } 
    t.tabn = Util.ilog(this.entries) - 4;
    if (t.tabn < 5)
      t.tabn = 5; 
    int n = 1 << t.tabn;
    t.tab = new int[n];
    t.tabl = new int[n];
    for (int j = 0; j < n; j++) {
      int p = 0;
      int k = 0;
      for (k = 0; k < t.tabn && (p > 0 || k == 0); k++) {
        if ((j & 1 << k) != 0) {
          p = ptr1[p];
        } else {
          p = ptr0[p];
        } 
      } 
      t.tab[j] = p;
      t.tabl[j] = k;
    } 
    return t;
  }
  
  class DecodeAux {
    int[] tab;
    
    int[] tabl;
    
    int tabn;
    
    int[] ptr0;
    
    int[] ptr1;
    
    int aux;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\CodeBook.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */