package de.uni_passau.fim.se2.sa.sign.interpretation;

import com.google.common.base.Preconditions;

public class SignTransferRelation implements TransferRelation {

  @Override
  public SignValue evaluate(final int pValue) {
    // TODO Implement me
    if (pValue == 0) {
      return SignValue.ZERO;
    } else {
      return pValue > 0 ? SignValue.PLUS : SignValue.MINUS;
    }
  }

  @Override
  public SignValue evaluate(final Operation pOperation, final SignValue pValue) {
    Preconditions.checkState(pOperation == Operation.NEG);
    Preconditions.checkNotNull(pValue);
    // TODO Implement me
    switch (pValue) {
      case MINUS:
        return SignValue.PLUS;
      case ZERO_MINUS:
        return SignValue.ZERO_PLUS;
      case PLUS:
        return SignValue.MINUS;
      case ZERO_PLUS:
        return SignValue.ZERO_MINUS;
      default:
        return pValue;
    }
  }

  @Override
  public SignValue evaluate(
      final Operation pOperation, final SignValue pLHS, final SignValue pRHS) {
    Preconditions.checkState(
        pOperation == Operation.ADD
            || pOperation == Operation.SUB
            || pOperation == Operation.MUL
            || pOperation == Operation.DIV);
    Preconditions.checkNotNull(pLHS);
    Preconditions.checkNotNull(pRHS);

    switch (pOperation) {
      case ADD:
        int[][] addMat = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 1, 1, 7, 7, 7, 7, 7 },
            { 0, 1, 2, 3, 4, 5, 6, 7, 7 },
            { 0, 1, 3, 3, 7, 7, 7, 7, 7 },
            { 0, 7, 4, 7, 4, 7, 4, 7, 7 },
            { 0, 7, 5, 7, 7, 7, 7, 7, 7 },
            { 0, 7, 6, 7, 4, 7, 6, 7, 7 },
            { 0, 7, 7, 7, 7, 7, 7, 7, 7 },
            { 0, 7, 7, 7, 7, 7, 7, 7, 7 },
        };
        int addIndex = addMat[pLHS.ordinal()][pRHS.ordinal()];
        return SignValue.values()[addIndex];
      case SUB:
        var right = evaluate(Operation.NEG, pRHS);
        return evaluate(Operation.ADD, pLHS, right);
      case MUL:
        int[][] mulMat = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 4, 2, 6, 1, 5, 3, 7, 7 },
            { 0, 2, 2, 2, 2, 2, 2, 2, 2 },
            { 0, 6, 2, 6, 3, 7, 3, 7, 7 },
            { 0, 1, 2, 3, 4, 5, 6, 7, 7 },
            { 0, 5, 2, 7, 5, 5, 7, 7, 7 },
            { 0, 3, 2, 3, 6, 7, 6, 7, 7 },
            { 0, 7, 2, 7, 7, 7, 7, 7, 7 },
            { 0, 7, 2, 7, 7, 7, 7, 7, 7 },
        };
        int mulIndex = mulMat[pLHS.ordinal()][pRHS.ordinal()];
        return SignValue.values()[mulIndex];
      case DIV:
        int[][] divMat = {
            { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 6, 0, 6, 3, 7, 3, 7, 7 },
            { 0, 2, 0, 2, 2, 2, 2, 7, 7 },
            { 0, 6, 0, 6, 3, 7, 3, 7, 7 },
            { 0, 3, 0, 3, 6, 7, 6, 7, 7 },
            { 0, 7, 0, 7, 7, 7, 7, 7, 7 },
            { 0, 3, 0, 3, 6, 7, 6, 7, 7 },
            { 0, 7, 0, 7, 7, 7, 7, 7, 7 },
            { 0, 7, 0, 7, 7, 7, 7, 7, 7 },
        };
        int divIndex = divMat[pLHS.ordinal()][pRHS.ordinal()];
        return SignValue.values()[divIndex];
      default:
        return SignValue.UNINITIALIZED_VALUE;
    }
  }
}
