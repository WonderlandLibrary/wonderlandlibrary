package javassist.bytecode.annotation;

public interface MemberValueVisitor {
   void visitAnnotationMemberValue(AnnotationMemberValue var1);

   void visitArrayMemberValue(ArrayMemberValue var1);

   void visitBooleanMemberValue(BooleanMemberValue var1);

   void visitByteMemberValue(ByteMemberValue var1);

   void visitCharMemberValue(CharMemberValue var1);

   void visitDoubleMemberValue(DoubleMemberValue var1);

   void visitEnumMemberValue(EnumMemberValue var1);

   void visitFloatMemberValue(FloatMemberValue var1);

   void visitIntegerMemberValue(IntegerMemberValue var1);

   void visitLongMemberValue(LongMemberValue var1);

   void visitShortMemberValue(ShortMemberValue var1);

   void visitStringMemberValue(StringMemberValue var1);

   void visitClassMemberValue(ClassMemberValue var1);
}
