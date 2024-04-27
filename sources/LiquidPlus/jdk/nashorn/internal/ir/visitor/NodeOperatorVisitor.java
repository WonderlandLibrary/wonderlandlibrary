/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.ir.visitor;

import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.LexicalContext;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.UnaryNode;
import jdk.nashorn.internal.ir.visitor.NodeVisitor;

public abstract class NodeOperatorVisitor<T extends LexicalContext>
extends NodeVisitor<T> {
    public NodeOperatorVisitor(T lc) {
        super(lc);
    }

    @Override
    public final boolean enterUnaryNode(UnaryNode unaryNode) {
        switch (unaryNode.tokenType()) {
            case ADD: {
                return this.enterADD(unaryNode);
            }
            case BIT_NOT: {
                return this.enterBIT_NOT(unaryNode);
            }
            case DELETE: {
                return this.enterDELETE(unaryNode);
            }
            case NEW: {
                return this.enterNEW(unaryNode);
            }
            case NOT: {
                return this.enterNOT(unaryNode);
            }
            case SUB: {
                return this.enterSUB(unaryNode);
            }
            case TYPEOF: {
                return this.enterTYPEOF(unaryNode);
            }
            case VOID: {
                return this.enterVOID(unaryNode);
            }
            case DECPREFIX: 
            case DECPOSTFIX: 
            case INCPREFIX: 
            case INCPOSTFIX: {
                return this.enterDECINC(unaryNode);
            }
        }
        return super.enterUnaryNode(unaryNode);
    }

    @Override
    public final Node leaveUnaryNode(UnaryNode unaryNode) {
        switch (unaryNode.tokenType()) {
            case ADD: {
                return this.leaveADD(unaryNode);
            }
            case BIT_NOT: {
                return this.leaveBIT_NOT(unaryNode);
            }
            case DELETE: {
                return this.leaveDELETE(unaryNode);
            }
            case NEW: {
                return this.leaveNEW(unaryNode);
            }
            case NOT: {
                return this.leaveNOT(unaryNode);
            }
            case SUB: {
                return this.leaveSUB(unaryNode);
            }
            case TYPEOF: {
                return this.leaveTYPEOF(unaryNode);
            }
            case VOID: {
                return this.leaveVOID(unaryNode);
            }
            case DECPREFIX: 
            case DECPOSTFIX: 
            case INCPREFIX: 
            case INCPOSTFIX: {
                return this.leaveDECINC(unaryNode);
            }
        }
        return super.leaveUnaryNode(unaryNode);
    }

    @Override
    public final boolean enterBinaryNode(BinaryNode binaryNode) {
        switch (binaryNode.tokenType()) {
            case ADD: {
                return this.enterADD(binaryNode);
            }
            case AND: {
                return this.enterAND(binaryNode);
            }
            case ASSIGN: {
                return this.enterASSIGN(binaryNode);
            }
            case ASSIGN_ADD: {
                return this.enterASSIGN_ADD(binaryNode);
            }
            case ASSIGN_BIT_AND: {
                return this.enterASSIGN_BIT_AND(binaryNode);
            }
            case ASSIGN_BIT_OR: {
                return this.enterASSIGN_BIT_OR(binaryNode);
            }
            case ASSIGN_BIT_XOR: {
                return this.enterASSIGN_BIT_XOR(binaryNode);
            }
            case ASSIGN_DIV: {
                return this.enterASSIGN_DIV(binaryNode);
            }
            case ASSIGN_MOD: {
                return this.enterASSIGN_MOD(binaryNode);
            }
            case ASSIGN_MUL: {
                return this.enterASSIGN_MUL(binaryNode);
            }
            case ASSIGN_SAR: {
                return this.enterASSIGN_SAR(binaryNode);
            }
            case ASSIGN_SHL: {
                return this.enterASSIGN_SHL(binaryNode);
            }
            case ASSIGN_SHR: {
                return this.enterASSIGN_SHR(binaryNode);
            }
            case ASSIGN_SUB: {
                return this.enterASSIGN_SUB(binaryNode);
            }
            case BIND: {
                return this.enterBIND(binaryNode);
            }
            case BIT_AND: {
                return this.enterBIT_AND(binaryNode);
            }
            case BIT_OR: {
                return this.enterBIT_OR(binaryNode);
            }
            case BIT_XOR: {
                return this.enterBIT_XOR(binaryNode);
            }
            case COMMARIGHT: {
                return this.enterCOMMARIGHT(binaryNode);
            }
            case COMMALEFT: {
                return this.enterCOMMALEFT(binaryNode);
            }
            case DIV: {
                return this.enterDIV(binaryNode);
            }
            case EQ: {
                return this.enterEQ(binaryNode);
            }
            case EQ_STRICT: {
                return this.enterEQ_STRICT(binaryNode);
            }
            case GE: {
                return this.enterGE(binaryNode);
            }
            case GT: {
                return this.enterGT(binaryNode);
            }
            case IN: {
                return this.enterIN(binaryNode);
            }
            case INSTANCEOF: {
                return this.enterINSTANCEOF(binaryNode);
            }
            case LE: {
                return this.enterLE(binaryNode);
            }
            case LT: {
                return this.enterLT(binaryNode);
            }
            case MOD: {
                return this.enterMOD(binaryNode);
            }
            case MUL: {
                return this.enterMUL(binaryNode);
            }
            case NE: {
                return this.enterNE(binaryNode);
            }
            case NE_STRICT: {
                return this.enterNE_STRICT(binaryNode);
            }
            case OR: {
                return this.enterOR(binaryNode);
            }
            case SAR: {
                return this.enterSAR(binaryNode);
            }
            case SHL: {
                return this.enterSHL(binaryNode);
            }
            case SHR: {
                return this.enterSHR(binaryNode);
            }
            case SUB: {
                return this.enterSUB(binaryNode);
            }
        }
        return super.enterBinaryNode(binaryNode);
    }

    @Override
    public final Node leaveBinaryNode(BinaryNode binaryNode) {
        switch (binaryNode.tokenType()) {
            case ADD: {
                return this.leaveADD(binaryNode);
            }
            case AND: {
                return this.leaveAND(binaryNode);
            }
            case ASSIGN: {
                return this.leaveASSIGN(binaryNode);
            }
            case ASSIGN_ADD: {
                return this.leaveASSIGN_ADD(binaryNode);
            }
            case ASSIGN_BIT_AND: {
                return this.leaveASSIGN_BIT_AND(binaryNode);
            }
            case ASSIGN_BIT_OR: {
                return this.leaveASSIGN_BIT_OR(binaryNode);
            }
            case ASSIGN_BIT_XOR: {
                return this.leaveASSIGN_BIT_XOR(binaryNode);
            }
            case ASSIGN_DIV: {
                return this.leaveASSIGN_DIV(binaryNode);
            }
            case ASSIGN_MOD: {
                return this.leaveASSIGN_MOD(binaryNode);
            }
            case ASSIGN_MUL: {
                return this.leaveASSIGN_MUL(binaryNode);
            }
            case ASSIGN_SAR: {
                return this.leaveASSIGN_SAR(binaryNode);
            }
            case ASSIGN_SHL: {
                return this.leaveASSIGN_SHL(binaryNode);
            }
            case ASSIGN_SHR: {
                return this.leaveASSIGN_SHR(binaryNode);
            }
            case ASSIGN_SUB: {
                return this.leaveASSIGN_SUB(binaryNode);
            }
            case BIND: {
                return this.leaveBIND(binaryNode);
            }
            case BIT_AND: {
                return this.leaveBIT_AND(binaryNode);
            }
            case BIT_OR: {
                return this.leaveBIT_OR(binaryNode);
            }
            case BIT_XOR: {
                return this.leaveBIT_XOR(binaryNode);
            }
            case COMMARIGHT: {
                return this.leaveCOMMARIGHT(binaryNode);
            }
            case COMMALEFT: {
                return this.leaveCOMMALEFT(binaryNode);
            }
            case DIV: {
                return this.leaveDIV(binaryNode);
            }
            case EQ: {
                return this.leaveEQ(binaryNode);
            }
            case EQ_STRICT: {
                return this.leaveEQ_STRICT(binaryNode);
            }
            case GE: {
                return this.leaveGE(binaryNode);
            }
            case GT: {
                return this.leaveGT(binaryNode);
            }
            case IN: {
                return this.leaveIN(binaryNode);
            }
            case INSTANCEOF: {
                return this.leaveINSTANCEOF(binaryNode);
            }
            case LE: {
                return this.leaveLE(binaryNode);
            }
            case LT: {
                return this.leaveLT(binaryNode);
            }
            case MOD: {
                return this.leaveMOD(binaryNode);
            }
            case MUL: {
                return this.leaveMUL(binaryNode);
            }
            case NE: {
                return this.leaveNE(binaryNode);
            }
            case NE_STRICT: {
                return this.leaveNE_STRICT(binaryNode);
            }
            case OR: {
                return this.leaveOR(binaryNode);
            }
            case SAR: {
                return this.leaveSAR(binaryNode);
            }
            case SHL: {
                return this.leaveSHL(binaryNode);
            }
            case SHR: {
                return this.leaveSHR(binaryNode);
            }
            case SUB: {
                return this.leaveSUB(binaryNode);
            }
        }
        return super.leaveBinaryNode(binaryNode);
    }

    public boolean enterADD(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveADD(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterBIT_NOT(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveBIT_NOT(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterDECINC(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveDECINC(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterDELETE(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveDELETE(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterNEW(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveNEW(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterNOT(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveNOT(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterSUB(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveSUB(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterTYPEOF(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveTYPEOF(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterVOID(UnaryNode unaryNode) {
        return this.enterDefault(unaryNode);
    }

    public Node leaveVOID(UnaryNode unaryNode) {
        return this.leaveDefault(unaryNode);
    }

    public boolean enterADD(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveADD(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterAND(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveAND(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_ADD(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_ADD(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_BIT_AND(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_BIT_AND(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_BIT_OR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_BIT_OR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_BIT_XOR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_BIT_XOR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_DIV(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_DIV(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_MOD(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_MOD(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_MUL(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_MUL(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_SAR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_SAR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_SHL(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_SHL(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_SHR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_SHR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterASSIGN_SUB(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveASSIGN_SUB(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterBIND(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveBIND(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterBIT_AND(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveBIT_AND(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterBIT_OR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveBIT_OR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterBIT_XOR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveBIT_XOR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterCOMMALEFT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveCOMMALEFT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterCOMMARIGHT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveCOMMARIGHT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterDIV(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveDIV(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterEQ(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveEQ(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterEQ_STRICT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveEQ_STRICT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterGE(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveGE(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterGT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveGT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterIN(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveIN(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterINSTANCEOF(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveINSTANCEOF(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterLE(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveLE(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterLT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveLT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterMOD(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveMOD(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterMUL(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveMUL(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterNE(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveNE(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterNE_STRICT(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveNE_STRICT(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterOR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveOR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterSAR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveSAR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterSHL(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveSHL(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterSHR(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveSHR(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }

    public boolean enterSUB(BinaryNode binaryNode) {
        return this.enterDefault(binaryNode);
    }

    public Node leaveSUB(BinaryNode binaryNode) {
        return this.leaveDefault(binaryNode);
    }
}

