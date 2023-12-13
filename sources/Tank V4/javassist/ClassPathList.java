package javassist;

final class ClassPathList {
   ClassPathList next;
   ClassPath path;

   ClassPathList(ClassPath var1, ClassPathList var2) {
      this.next = var2;
      this.path = var1;
   }
}
