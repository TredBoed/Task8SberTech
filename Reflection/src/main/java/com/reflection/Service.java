package com.reflection;

public interface Service {
  @Cache
  double doHardWork(@MyAnnotation String name, Integer value);
}
