package cn.hanabi.injection.interfaces;

import java.util.List;

public interface IShaderGroup {
   List getShaders();

   void createBindFramebuffers(int var1, int var2);

   void loadShaderGroup(float var1);
}
