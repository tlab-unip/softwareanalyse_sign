package de.uni_passau.fim.se2.sa.sign.interpretation;

import java.util.List;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.Interpreter;

import de.uni_passau.fim.se2.sa.sign.interpretation.TransferRelation.Operation;

public class SignInterpreter extends Interpreter<SignValue> implements Opcodes {

  public SignInterpreter() {
    this(ASM9);
  }

  /**
   * Constructs a new {@link Interpreter}.
   *
   * @param pAPI The ASM API version supported by this interpreter. Must be one of
   *             {@link #ASM4},
   *             {@link #ASM5}, {@link #ASM6}, {@link #ASM7}, {@link #ASM8}, or
   *             {@link #ASM9}
   */
  protected SignInterpreter(final int pAPI) {
    super(pAPI);
    if (getClass() != SignInterpreter.class) {
      throw new IllegalStateException();
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue newValue(final Type pType) {
    // TODO Implement me
    if (pType == null) {
      return SignValue.UNINITIALIZED_VALUE;
    }
    switch (pType.getSort()) {
      case Type.VOID:
        return null;
      default:
        return SignValue.UNINITIALIZED_VALUE;
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue newOperation(final AbstractInsnNode pInstruction) throws AnalyzerException {
    // TODO Implement me
    switch (pInstruction.getOpcode()) {
      case ICONST_M1:
        return SignValue.MINUS;
      case ICONST_0:
        return SignValue.ZERO;
      case ICONST_1:
      case ICONST_2:
      case ICONST_3:
      case ICONST_4:
      case ICONST_5:
        return SignValue.PLUS;
      case BIPUSH:
        var value = ((IntInsnNode) pInstruction).operand;
        return new SignTransferRelation().evaluate(value);
      default:
        return SignValue.UNINITIALIZED_VALUE;
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue copyOperation(final AbstractInsnNode pInstruction, final SignValue pValue) {
    // TODO Implement me
    return pValue;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue unaryOperation(final AbstractInsnNode pInstruction, final SignValue pValue)
      throws AnalyzerException {
    // TODO Implement me
    switch (pInstruction.getOpcode()) {
      case INEG:
        return new SignTransferRelation().evaluate(Operation.NEG, pValue);
      case IINC:
      case L2I:
      case F2I:
      case D2I:
      case I2B:
      case I2C:
      case I2S:
      case ARRAYLENGTH:
      case INSTANCEOF:
        return newValue(Type.INT_TYPE);
      case NEWARRAY:
      default:
        return SignValue.UNINITIALIZED_VALUE;
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue binaryOperation(
      final AbstractInsnNode pInstruction, final SignValue pValue1, final SignValue pValue2) {
    // TODO Implement me
    switch (pInstruction.getOpcode()) {
      case IALOAD:
        return SignValue.UNINITIALIZED_VALUE;
      case IADD:
        return new SignTransferRelation().evaluate(Operation.ADD, pValue1, pValue2);
      case ISUB:
        return new SignTransferRelation().evaluate(Operation.SUB, pValue1, pValue2);
      case IMUL:
        return new SignTransferRelation().evaluate(Operation.MUL, pValue1, pValue2);
      case IDIV:
        return new SignTransferRelation().evaluate(Operation.DIV, pValue1, pValue2);
      case IF_ICMPEQ:
      case IF_ICMPNE:
      case IF_ICMPLT:
      case IF_ICMPGE:
      case IF_ICMPGT:
      case IF_ICMPLE:
        return null;
      default:
        return null;
    }
  }

  /** {@inheritDoc} */
  @Override
  public SignValue ternaryOperation(
      final AbstractInsnNode pInstruction,
      final SignValue pValue1,
      final SignValue pValue2,
      final SignValue pValue3) {
    return null; // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public SignValue naryOperation(
      final AbstractInsnNode pInstruction, final List<? extends SignValue> pValues) {
    // TODO Implement me
    int opcode = pInstruction.getOpcode();
    if (opcode == MULTIANEWARRAY) {
    } else {
    }
    return SignValue.UNINITIALIZED_VALUE;
  }

  /** {@inheritDoc} */
  @Override
  public void returnOperation(
      final AbstractInsnNode pInstruction, final SignValue pValue, final SignValue pExpected) {
    // Nothing to do.
  }

  /** {@inheritDoc} */
  @Override
  public SignValue merge(final SignValue pValue1, final SignValue pValue2) {
    // TODO Implement me
    if (pValue1 == SignValue.UNINITIALIZED_VALUE || pValue2 == SignValue.UNINITIALIZED_VALUE) {
      return SignValue.UNINITIALIZED_VALUE;
    }
    return !pValue1.equals(pValue2) ? pValue1.join(pValue2) : pValue1;
  }
}
