package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Residue0 extends FuncResidue {
  void pack(Object vr, Buffer opb) {
    InfoResidue0 info = (InfoResidue0)vr;
    int acc = 0;
    opb.write(info.begin, 24);
    opb.write(info.end, 24);
    opb.write(info.grouping - 1, 24);
    opb.write(info.partitions - 1, 6);
    opb.write(info.groupbook, 8);
    int j;
    for (j = 0; j < info.partitions; j++) {
      int i = info.secondstages[j];
      if (Util.ilog(i) > 3) {
        opb.write(i, 3);
        opb.write(1, 1);
        opb.write(i >>> 3, 5);
      } else {
        opb.write(i, 4);
      } 
      acc += Util.icount(i);
    } 
    for (j = 0; j < acc; j++)
      opb.write(info.booklist[j], 8); 
  }
  
  Object unpack(Info vi, Buffer opb) {
    int acc = 0;
    InfoResidue0 info = new InfoResidue0();
    info.begin = opb.read(24);
    info.end = opb.read(24);
    info.grouping = opb.read(24) + 1;
    info.partitions = opb.read(6) + 1;
    info.groupbook = opb.read(8);
    int j;
    for (j = 0; j < info.partitions; j++) {
      int cascade = opb.read(3);
      if (opb.read(1) != 0)
        cascade |= opb.read(5) << 3; 
      info.secondstages[j] = cascade;
      acc += Util.icount(cascade);
    } 
    for (j = 0; j < acc; j++)
      info.booklist[j] = opb.read(8); 
    if (info.groupbook >= vi.books) {
      free_info(info);
      return null;
    } 
    for (j = 0; j < acc; j++) {
      if (info.booklist[j] >= vi.books) {
        free_info(info);
        return null;
      } 
    } 
    return info;
  }
  
  Object look(DspState vd, InfoMode vm, Object vr) {
    InfoResidue0 info = (InfoResidue0)vr;
    LookResidue0 look = new LookResidue0();
    int acc = 0;
    int maxstage = 0;
    look.info = info;
    look.map = vm.mapping;
    look.parts = info.partitions;
    look.fullbooks = vd.fullbooks;
    look.phrasebook = vd.fullbooks[info.groupbook];
    int dim = look.phrasebook.dim;
    look.partbooks = new int[look.parts][];
    int j;
    for (j = 0; j < look.parts; j++) {
      int i = info.secondstages[j];
      int stages = Util.ilog(i);
      if (stages != 0) {
        if (stages > maxstage)
          maxstage = stages; 
        look.partbooks[j] = new int[stages];
        for (int k = 0; k < stages; k++) {
          if ((i & 1 << k) != 0)
            look.partbooks[j][k] = info.booklist[acc++]; 
        } 
      } 
    } 
    look.partvals = (int)Math.rint(Math.pow(look.parts, dim));
    look.stages = maxstage;
    look.decodemap = new int[look.partvals][];
    for (j = 0; j < look.partvals; j++) {
      int val = j;
      int mult = look.partvals / look.parts;
      look.decodemap[j] = new int[dim];
      for (int k = 0; k < dim; k++) {
        int deco = val / mult;
        val -= deco * mult;
        mult /= look.parts;
        look.decodemap[j][k] = deco;
      } 
    } 
    return look;
  }
  
  void free_info(Object i) {}
  
  void free_look(Object i) {}
  
  private static int[][][] _01inverse_partword = new int[2][][];
  
  static synchronized int _01inverse(Block vb, Object vl, float[][] in, int ch, int decodepart) {
    LookResidue0 look = (LookResidue0)vl;
    InfoResidue0 info = look.info;
    int samples_per_partition = info.grouping;
    int partitions_per_word = look.phrasebook.dim;
    int n = info.end - info.begin;
    int partvals = n / samples_per_partition;
    int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
    if (_01inverse_partword.length < ch)
      _01inverse_partword = new int[ch][][]; 
    int j;
    for (j = 0; j < ch; j++) {
      if (_01inverse_partword[j] == null || (_01inverse_partword[j]).length < partwords)
        _01inverse_partword[j] = new int[partwords][]; 
    } 
    for (int s = 0; s < look.stages; s++) {
      for (int i = 0, l = 0; i < partvals; l++) {
        if (s == 0)
          for (j = 0; j < ch; j++) {
            int temp = look.phrasebook.decode(vb.opb);
            if (temp == -1)
              return 0; 
            _01inverse_partword[j][l] = look.decodemap[temp];
            if (_01inverse_partword[j][l] == null)
              return 0; 
          }  
        for (int k = 0; k < partitions_per_word && i < partvals; k++, i++) {
          for (j = 0; j < ch; j++) {
            int offset = info.begin + i * samples_per_partition;
            int index = _01inverse_partword[j][l][k];
            if ((info.secondstages[index] & 1 << s) != 0) {
              CodeBook stagebook = look.fullbooks[look.partbooks[index][s]];
              if (stagebook != null)
                if (decodepart == 0) {
                  if (stagebook.decodevs_add(in[j], offset, vb.opb, samples_per_partition) == -1)
                    return 0; 
                } else if (decodepart == 1 && 
                  stagebook.decodev_add(in[j], offset, vb.opb, samples_per_partition) == -1) {
                  return 0;
                }  
            } 
          } 
        } 
      } 
    } 
    return 0;
  }
  
  static int[][] _2inverse_partword = (int[][])null;
  
  static synchronized int _2inverse(Block vb, Object vl, float[][] in, int ch) {
    LookResidue0 look = (LookResidue0)vl;
    InfoResidue0 info = look.info;
    int samples_per_partition = info.grouping;
    int partitions_per_word = look.phrasebook.dim;
    int n = info.end - info.begin;
    int partvals = n / samples_per_partition;
    int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
    if (_2inverse_partword == null || _2inverse_partword.length < partwords)
      _2inverse_partword = new int[partwords][]; 
    for (int s = 0; s < look.stages; s++) {
      for (int i = 0, l = 0; i < partvals; l++) {
        if (s == 0) {
          int temp = look.phrasebook.decode(vb.opb);
          if (temp == -1)
            return 0; 
          _2inverse_partword[l] = look.decodemap[temp];
          if (_2inverse_partword[l] == null)
            return 0; 
        } 
        for (int k = 0; k < partitions_per_word && i < partvals; k++, i++) {
          int offset = info.begin + i * samples_per_partition;
          int index = _2inverse_partword[l][k];
          if ((info.secondstages[index] & 1 << s) != 0) {
            CodeBook stagebook = look.fullbooks[look.partbooks[index][s]];
            if (stagebook != null && 
              stagebook.decodevv_add(in, offset, ch, vb.opb, samples_per_partition) == -1)
              return 0; 
          } 
        } 
      } 
    } 
    return 0;
  }
  
  int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
    int used = 0;
    for (int i = 0; i < ch; i++) {
      if (nonzero[i] != 0)
        in[used++] = in[i]; 
    } 
    if (used != 0)
      return _01inverse(vb, vl, in, used, 0); 
    return 0;
  }
  
  class LookResidue0 {
    Residue0.InfoResidue0 info;
    
    int map;
    
    int parts;
    
    int stages;
    
    CodeBook[] fullbooks;
    
    CodeBook phrasebook;
    
    int[][] partbooks;
    
    int partvals;
    
    int[][] decodemap;
    
    int postbits;
    
    int phrasebits;
    
    int frames;
  }
  
  class InfoResidue0 {
    int begin;
    
    int end;
    
    int grouping;
    
    int partitions;
    
    int groupbook;
    
    int[] secondstages = new int[64];
    
    int[] booklist = new int[256];
    
    float[] entmax = new float[64];
    
    float[] ampmax = new float[64];
    
    int[] subgrp = new int[64];
    
    int[] blimit = new int[64];
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\Residue0.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */