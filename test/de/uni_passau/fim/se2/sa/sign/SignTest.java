package de.uni_passau.fim.se2.sa.sign;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignInterpreter;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignTransferRelation;
import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;
import de.uni_passau.fim.se2.sa.sign.interpretation.TransferRelation.Operation;

public class SignTest {
    @Test
    public void SignValueTest() throws Exception {
        assertEquals(SignValue.isZero(SignValue.ZERO), true);
        assertEquals(SignValue.isMaybeZero(SignValue.ZERO), true);
        assertEquals(SignValue.isMaybeZero(SignValue.ZERO_MINUS), true);
        assertEquals(SignValue.isMaybeZero(SignValue.ZERO_PLUS), true);

        assertEquals(SignValue.isNegative(SignValue.MINUS), true);
        assertEquals(SignValue.isMaybeNegative(SignValue.MINUS), true);
        assertEquals(SignValue.isMaybeNegative(SignValue.PLUS_MINUS), true);
        assertEquals(SignValue.isMaybeNegative(SignValue.ZERO_MINUS), true);

        assertEquals(SignValue.BOTTOM.isLessOrEqual(SignValue.PLUS_MINUS), true);
        assertEquals(SignValue.MINUS.isLessOrEqual(SignValue.PLUS_MINUS), true);
        assertEquals(SignValue.PLUS.isLessOrEqual(SignValue.PLUS_MINUS), true);
        assertEquals(SignValue.TOP.isLessOrEqual(SignValue.PLUS_MINUS), false);
    }

    @Test
    public void SignInterpreterTest() throws Exception {
        var interpreter = new SignInterpreter();

        assertEquals(interpreter.newValue(Type.VOID_TYPE), null);
        assertEquals(interpreter.newValue(Type.INT_TYPE), SignValue.UNINITIALIZED_VALUE);

        assertEquals(interpreter.newOperation(new InsnNode(Opcodes.ICONST_M1)), SignValue.MINUS);
        assertEquals(interpreter.newOperation(new InsnNode(Opcodes.ICONST_0)), SignValue.ZERO);
        assertEquals(interpreter.newOperation(new InsnNode(Opcodes.ICONST_1)), SignValue.PLUS);
        assertEquals(interpreter.newOperation(new IntInsnNode(Opcodes.BIPUSH, -20)), SignValue.MINUS);

        assertEquals(interpreter.copyOperation(new VarInsnNode(Opcodes.ILOAD, 1), SignValue.MINUS), SignValue.MINUS);

        assertEquals(interpreter.unaryOperation(new InsnNode(Opcodes.INEG), SignValue.MINUS), SignValue.PLUS);

        assertEquals(interpreter.binaryOperation(new InsnNode(Opcodes.IADD), SignValue.MINUS, SignValue.MINUS),
                SignValue.MINUS);

        assertEquals(interpreter.merge(SignValue.MINUS, SignValue.PLUS), SignValue.PLUS_MINUS);
    }

    @Test
    public void SignTransferRelationTest() throws Exception {
        var tr = new SignTransferRelation();

        assertEquals(tr.evaluate(0), SignValue.ZERO);
        assertEquals(tr.evaluate(-1), SignValue.MINUS);
        assertEquals(tr.evaluate(1), SignValue.PLUS);

        assertEquals(tr.evaluate(Operation.NEG, SignValue.MINUS), SignValue.PLUS);
        assertEquals(tr.evaluate(Operation.NEG, SignValue.PLUS), SignValue.MINUS);

        assertEquals(tr.evaluate(Operation.ADD, SignValue.MINUS, SignValue.MINUS), SignValue.MINUS);
        assertEquals(tr.evaluate(Operation.SUB, SignValue.MINUS, SignValue.MINUS), SignValue.TOP);
        assertEquals(tr.evaluate(Operation.MUL, SignValue.MINUS, SignValue.MINUS), SignValue.PLUS);
        assertEquals(tr.evaluate(Operation.DIV, SignValue.MINUS, SignValue.MINUS), SignValue.ZERO_PLUS);
        assertEquals(tr.evaluate(Operation.DIV, SignValue.MINUS, SignValue.ZERO), SignValue.BOTTOM);
    }

    @Test
    public void SignAnalysisTest() throws Exception {
        String className = "de.uni_passau.fim.se2.sa.examples.PublicFunctional";
        var analysis = new SignAnalysisImpl();

        var result = analysis.analyse(className, "allCases:()I");
        assertEquals(result.size(), 7);
        var result2 = analysis.analyse(className, "ifelse:()I");
        assertEquals(result2.size(), 1);
        var result3 = analysis.analyse(className, "loop0:()V");
        assertEquals(result3.size(), 1);
    }
}
