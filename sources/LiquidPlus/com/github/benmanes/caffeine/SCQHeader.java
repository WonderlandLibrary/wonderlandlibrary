/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.checkerframework.checker.nullness.qual.Nullable
 */
package com.github.benmanes.caffeine;

import com.github.benmanes.caffeine.SingleConsumerQueue;
import com.github.benmanes.caffeine.base.UnsafeAccess;
import java.util.AbstractQueue;
import org.checkerframework.checker.nullness.qual.Nullable;

final class SCQHeader {
    SCQHeader() {
    }

    static abstract class HeadAndTailRef<E>
    extends PadHeadAndTail<E> {
        static final long TAIL_OFFSET = UnsafeAccess.objectFieldOffset(HeadAndTailRef.class, "tail");
        volatile  @Nullable SingleConsumerQueue.Node<E> tail;

        HeadAndTailRef() {
        }

        void lazySetTail(SingleConsumerQueue.Node<E> next) {
            UnsafeAccess.UNSAFE.putOrderedObject(this, TAIL_OFFSET, next);
        }

        boolean casTail(SingleConsumerQueue.Node<E> expect, SingleConsumerQueue.Node<E> update) {
            return UnsafeAccess.UNSAFE.compareAndSwapObject(this, TAIL_OFFSET, expect, update);
        }
    }

    static abstract class PadHeadAndTail<E>
    extends HeadRef<E> {
        byte p120;
        byte p121;
        byte p122;
        byte p123;
        byte p124;
        byte p125;
        byte p126;
        byte p127;
        byte p128;
        byte p129;
        byte p130;
        byte p131;
        byte p132;
        byte p133;
        byte p134;
        byte p135;
        byte p136;
        byte p137;
        byte p138;
        byte p139;
        byte p140;
        byte p141;
        byte p142;
        byte p143;
        byte p144;
        byte p145;
        byte p146;
        byte p147;
        byte p148;
        byte p149;
        byte p150;
        byte p151;
        byte p152;
        byte p153;
        byte p154;
        byte p155;
        byte p156;
        byte p157;
        byte p158;
        byte p159;
        byte p160;
        byte p161;
        byte p162;
        byte p163;
        byte p164;
        byte p165;
        byte p166;
        byte p167;
        byte p168;
        byte p169;
        byte p170;
        byte p171;
        byte p172;
        byte p173;
        byte p174;
        byte p175;
        byte p176;
        byte p177;
        byte p178;
        byte p179;
        byte p180;
        byte p181;
        byte p182;
        byte p183;
        byte p184;
        byte p185;
        byte p186;
        byte p187;
        byte p188;
        byte p189;
        byte p190;
        byte p191;
        byte p192;
        byte p193;
        byte p194;
        byte p195;
        byte p196;
        byte p197;
        byte p198;
        byte p199;
        byte p200;
        byte p201;
        byte p202;
        byte p203;
        byte p204;
        byte p205;
        byte p206;
        byte p207;
        byte p208;
        byte p209;
        byte p210;
        byte p211;
        byte p212;
        byte p213;
        byte p214;
        byte p215;
        byte p216;
        byte p217;
        byte p218;
        byte p219;
        byte p220;
        byte p221;
        byte p222;
        byte p223;
        byte p224;
        byte p225;
        byte p226;
        byte p227;
        byte p228;
        byte p229;
        byte p230;
        byte p231;
        byte p232;
        byte p233;
        byte p234;
        byte p235;
        byte p236;
        byte p237;
        byte p238;
        byte p239;

        PadHeadAndTail() {
        }
    }

    static abstract class HeadRef<E>
    extends PadHead<E> {
         @Nullable SingleConsumerQueue.Node<E> head;

        HeadRef() {
        }
    }

    static abstract class PadHead<E>
    extends AbstractQueue<E> {
        byte p000;
        byte p001;
        byte p002;
        byte p003;
        byte p004;
        byte p005;
        byte p006;
        byte p007;
        byte p008;
        byte p009;
        byte p010;
        byte p011;
        byte p012;
        byte p013;
        byte p014;
        byte p015;
        byte p016;
        byte p017;
        byte p018;
        byte p019;
        byte p020;
        byte p021;
        byte p022;
        byte p023;
        byte p024;
        byte p025;
        byte p026;
        byte p027;
        byte p028;
        byte p029;
        byte p030;
        byte p031;
        byte p032;
        byte p033;
        byte p034;
        byte p035;
        byte p036;
        byte p037;
        byte p038;
        byte p039;
        byte p040;
        byte p041;
        byte p042;
        byte p043;
        byte p044;
        byte p045;
        byte p046;
        byte p047;
        byte p048;
        byte p049;
        byte p050;
        byte p051;
        byte p052;
        byte p053;
        byte p054;
        byte p055;
        byte p056;
        byte p057;
        byte p058;
        byte p059;
        byte p060;
        byte p061;
        byte p062;
        byte p063;
        byte p064;
        byte p065;
        byte p066;
        byte p067;
        byte p068;
        byte p069;
        byte p070;
        byte p071;
        byte p072;
        byte p073;
        byte p074;
        byte p075;
        byte p076;
        byte p077;
        byte p078;
        byte p079;
        byte p080;
        byte p081;
        byte p082;
        byte p083;
        byte p084;
        byte p085;
        byte p086;
        byte p087;
        byte p088;
        byte p089;
        byte p090;
        byte p091;
        byte p092;
        byte p093;
        byte p094;
        byte p095;
        byte p096;
        byte p097;
        byte p098;
        byte p099;
        byte p100;
        byte p101;
        byte p102;
        byte p103;
        byte p104;
        byte p105;
        byte p106;
        byte p107;
        byte p108;
        byte p109;
        byte p110;
        byte p111;
        byte p112;
        byte p113;
        byte p114;
        byte p115;
        byte p116;
        byte p117;
        byte p118;
        byte p119;

        PadHead() {
        }
    }
}

