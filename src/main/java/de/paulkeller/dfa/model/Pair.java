package de.paulkeller.dfa.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * This represent a Pair as in javafx.util.Pair
 * with additional methods for comparison
 * @author Paul Keller
 * @version 1.0
 */
public class Pair<T extends Comparable<T> & Serializable,E extends Comparable<E> & Serializable> implements Serializable {
  private T x;
  private E y;


  public Pair(T x, E y) {
    this.x = x;
    this.y = y;
  }


  public T getX() {
    return x;
  }

  public void setX(T x) {
    this.x = x;
  }

  public E getY() {
    return y;
  }

  public void setY(E y) {
    this.y = y;
  }

  /**
   * Returns true, if this Pair is smaller (with both coordinates)
   * @param other
   * @return
   */
  public boolean isSmaller(Pair<T, E> other) {
    return x.compareTo(other.x)<0 && y.compareTo(other.y)<0;
  }

  /**
   * Returns true, if this Pair is smaller or equal (with both coordinates)
   * @param other
   * @return
   */
  public boolean isSmallerOrEqual(Pair<T, E> other) {
    return x.compareTo(other.x)<=0 && y.compareTo(other.y)<=0;
  }

  /**
   * Returns true, if this Pair is bigger (with both coordinates)
   * @param other
   * @return
   */
  public boolean isBigger(Pair<T, E> other) {
    return x.compareTo(other.x)>0 && y.compareTo(other.y)>0;
  }

  /**
   * Returns true, if this Pair is bigger or equal (with both coordinates)
   * @param other
   * @return
   */
  public boolean isBiggerOrEqual(Pair<T, E> other) {
    return x.compareTo(other.x)>=0 && y.compareTo(other.y)>=0;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pair<?, ?> pair = (Pair<?, ?>) o;
    return x.equals(pair.x) &&
        y.equals(pair.y);
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }
}
