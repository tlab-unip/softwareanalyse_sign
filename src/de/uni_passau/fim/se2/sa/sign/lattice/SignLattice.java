package de.uni_passau.fim.se2.sa.sign.lattice;

import de.uni_passau.fim.se2.sa.sign.interpretation.SignValue;

/** A lattice for {@link SignValue}. */
public class SignLattice implements Lattice<SignValue> {

  /** {@inheritDoc} */
  @Override
  public SignValue top() {
    // TODO Implement me
    return SignValue.TOP;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue bottom() {
    // TODO Implement me
    return SignValue.BOTTOM;
  }

  /** {@inheritDoc} */
  @Override
  public SignValue join(final SignValue pFirst, final SignValue pSecond) {
    // TODO Implement me
    return pFirst.join(pSecond);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isLessOrEqual(final SignValue pFirst, final SignValue pSecond) {
    // TODO Implement me
    return pFirst.isLessOrEqual(pSecond);
  }
}
