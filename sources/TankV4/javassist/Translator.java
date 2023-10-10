package javassist;

public interface Translator {
   void start(ClassPool var1) throws NotFoundException, CannotCompileException;

   void onLoad(ClassPool var1, String var2) throws NotFoundException, CannotCompileException;
}
