package org.knowm.xchange.internal.utils.retries;

public interface IPredicate<T> {
  boolean test(T t);
}
