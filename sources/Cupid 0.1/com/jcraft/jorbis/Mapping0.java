package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;

class Mapping0 extends FuncMapping {
  static int seq = 0;
  
  void free_info(Object imap) {}
  
  void free_look(Object imap) {}
  
  Object look(DspState vd, InfoMode vm, Object m) {
    Info vi = vd.vi;
    LookMapping0 look = new LookMapping0();
    InfoMapping0 info = look.map = (InfoMapping0)m;
    look.mode = vm;
    look.time_look = new Object[info.submaps];
    look.floor_look = new Object[info.submaps];
    look.residue_look = new Object[info.submaps];
    look.time_func = new FuncTime[info.submaps];
    look.floor_func = new FuncFloor[info.submaps];
    look.residue_func = new FuncResidue[info.submaps];
    for (int i = 0; i < info.submaps; i++) {
      int timenum = info.timesubmap[i];
      int floornum = info.floorsubmap[i];
      int resnum = info.residuesubmap[i];
      look.time_func[i] = FuncTime.time_P[vi.time_type[timenum]];
      look.time_look[i] = look.time_func[i].look(vd, vm, vi.time_param[timenum]);
      look.floor_func[i] = FuncFloor.floor_P[vi.floor_type[floornum]];
      look.floor_look[i] = look.floor_func[i].look(vd, vm, vi.floor_param[floornum]);
      look.residue_func[i] = FuncResidue.residue_P[vi.residue_type[resnum]];
      look.residue_look[i] = look.residue_func[i].look(vd, vm, vi.residue_param[resnum]);
    } 
    if (vi.psys == 0 || vd.analysisp != 0);
    look.ch = vi.channels;
    return look;
  }
  
  void pack(Info vi, Object imap, Buffer opb) {
    InfoMapping0 info = (InfoMapping0)imap;
    if (info.submaps > 1) {
      opb.write(1, 1);
      opb.write(info.submaps - 1, 4);
    } else {
      opb.write(0, 1);
    } 
    if (info.coupling_steps > 0) {
      opb.write(1, 1);
      opb.write(info.coupling_steps - 1, 8);
      for (int j = 0; j < info.coupling_steps; j++) {
        opb.write(info.coupling_mag[j], Util.ilog2(vi.channels));
        opb.write(info.coupling_ang[j], Util.ilog2(vi.channels));
      } 
    } else {
      opb.write(0, 1);
    } 
    opb.write(0, 2);
    if (info.submaps > 1)
      for (int j = 0; j < vi.channels; j++)
        opb.write(info.chmuxlist[j], 4);  
    for (int i = 0; i < info.submaps; i++) {
      opb.write(info.timesubmap[i], 8);
      opb.write(info.floorsubmap[i], 8);
      opb.write(info.residuesubmap[i], 8);
    } 
  }
  
  Object unpack(Info vi, Buffer opb) {
    InfoMapping0 info = new InfoMapping0();
    if (opb.read(1) != 0) {
      info.submaps = opb.read(4) + 1;
    } else {
      info.submaps = 1;
    } 
    if (opb.read(1) != 0) {
      info.coupling_steps = opb.read(8) + 1;
      for (int j = 0; j < info.coupling_steps; j++) {
        int testM = info.coupling_mag[j] = opb.read(Util.ilog2(vi.channels));
        int testA = info.coupling_ang[j] = opb.read(Util.ilog2(vi.channels));
        if (testM < 0 || testA < 0 || testM == testA || testM >= vi.channels || testA >= vi.channels) {
          info.free();
          return null;
        } 
      } 
    } 
    if (opb.read(2) > 0) {
      info.free();
      return null;
    } 
    if (info.submaps > 1)
      for (int j = 0; j < vi.channels; j++) {
        info.chmuxlist[j] = opb.read(4);
        if (info.chmuxlist[j] >= info.submaps) {
          info.free();
          return null;
        } 
      }  
    for (int i = 0; i < info.submaps; i++) {
      info.timesubmap[i] = opb.read(8);
      if (info.timesubmap[i] >= vi.times) {
        info.free();
        return null;
      } 
      info.floorsubmap[i] = opb.read(8);
      if (info.floorsubmap[i] >= vi.floors) {
        info.free();
        return null;
      } 
      info.residuesubmap[i] = opb.read(8);
      if (info.residuesubmap[i] >= vi.residues) {
        info.free();
        return null;
      } 
    } 
    return info;
  }
  
  float[][] pcmbundle = (float[][])null;
  
  int[] zerobundle = null;
  
  int[] nonzero = null;
  
  Object[] floormemo = null;
  
  synchronized int inverse(Block vb, Object l) {
    DspState vd = vb.vd;
    Info vi = vd.vi;
    LookMapping0 look = (LookMapping0)l;
    InfoMapping0 info = look.map;
    InfoMode mode = look.mode;
    int n = vb.pcmend = vi.blocksizes[vb.W];
    float[] window = vd.window[vb.W][vb.lW][vb.nW][mode.windowtype];
    if (this.pcmbundle == null || this.pcmbundle.length < vi.channels) {
      this.pcmbundle = new float[vi.channels][];
      this.nonzero = new int[vi.channels];
      this.zerobundle = new int[vi.channels];
      this.floormemo = new Object[vi.channels];
    } 
    int i;
    for (i = 0; i < vi.channels; i++) {
      float[] pcm = vb.pcm[i];
      int submap = info.chmuxlist[i];
      this.floormemo[i] = look.floor_func[submap].inverse1(vb, look.floor_look[submap], this.floormemo[i]);
      if (this.floormemo[i] != null) {
        this.nonzero[i] = 1;
      } else {
        this.nonzero[i] = 0;
      } 
      for (int j = 0; j < n / 2; j++)
        pcm[j] = 0.0F; 
    } 
    for (i = 0; i < info.coupling_steps; i++) {
      if (this.nonzero[info.coupling_mag[i]] != 0 || this.nonzero[info.coupling_ang[i]] != 0) {
        this.nonzero[info.coupling_mag[i]] = 1;
        this.nonzero[info.coupling_ang[i]] = 1;
      } 
    } 
    for (i = 0; i < info.submaps; i++) {
      int ch_in_bundle = 0;
      for (int j = 0; j < vi.channels; j++) {
        if (info.chmuxlist[j] == i) {
          if (this.nonzero[j] != 0) {
            this.zerobundle[ch_in_bundle] = 1;
          } else {
            this.zerobundle[ch_in_bundle] = 0;
          } 
          this.pcmbundle[ch_in_bundle++] = vb.pcm[j];
        } 
      } 
      look.residue_func[i].inverse(vb, look.residue_look[i], this.pcmbundle, this.zerobundle, ch_in_bundle);
    } 
    for (i = info.coupling_steps - 1; i >= 0; i--) {
      float[] pcmM = vb.pcm[info.coupling_mag[i]];
      float[] pcmA = vb.pcm[info.coupling_ang[i]];
      for (int j = 0; j < n / 2; j++) {
        float mag = pcmM[j];
        float ang = pcmA[j];
        if (mag > 0.0F) {
          if (ang > 0.0F) {
            pcmM[j] = mag;
            pcmA[j] = mag - ang;
          } else {
            pcmA[j] = mag;
            pcmM[j] = mag + ang;
          } 
        } else if (ang > 0.0F) {
          pcmM[j] = mag;
          pcmA[j] = mag + ang;
        } else {
          pcmA[j] = mag;
          pcmM[j] = mag - ang;
        } 
      } 
    } 
    for (i = 0; i < vi.channels; i++) {
      float[] pcm = vb.pcm[i];
      int submap = info.chmuxlist[i];
      look.floor_func[submap].inverse2(vb, look.floor_look[submap], this.floormemo[i], pcm);
    } 
    for (i = 0; i < vi.channels; i++) {
      float[] pcm = vb.pcm[i];
      ((Mdct)vd.transform[vb.W][0]).backward(pcm, pcm);
    } 
    for (i = 0; i < vi.channels; i++) {
      float[] pcm = vb.pcm[i];
      if (this.nonzero[i] != 0) {
        for (int j = 0; j < n; j++)
          pcm[j] = pcm[j] * window[j]; 
      } else {
        for (int j = 0; j < n; j++)
          pcm[j] = 0.0F; 
      } 
    } 
    return 0;
  }
  
  class InfoMapping0 {
    int submaps;
    
    int[] chmuxlist = new int[256];
    
    int[] timesubmap = new int[16];
    
    int[] floorsubmap = new int[16];
    
    int[] residuesubmap = new int[16];
    
    int[] psysubmap = new int[16];
    
    int coupling_steps;
    
    int[] coupling_mag = new int[256];
    
    int[] coupling_ang = new int[256];
    
    void free() {
      this.chmuxlist = null;
      this.timesubmap = null;
      this.floorsubmap = null;
      this.residuesubmap = null;
      this.psysubmap = null;
      this.coupling_mag = null;
      this.coupling_ang = null;
    }
  }
  
  class LookMapping0 {
    InfoMode mode;
    
    Mapping0.InfoMapping0 map;
    
    Object[] time_look;
    
    Object[] floor_look;
    
    Object[] floor_state;
    
    Object[] residue_look;
    
    PsyLook[] psy_look;
    
    FuncTime[] time_func;
    
    FuncFloor[] floor_func;
    
    FuncResidue[] residue_func;
    
    int ch;
    
    float[][] decay;
    
    int lastframe;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\Mapping0.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */