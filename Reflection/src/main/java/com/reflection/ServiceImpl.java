package com.reflection;

public class ServiceImpl implements Service {
    @Override
    public double doHardWork(String name, Integer value) {
        return value;
    }

}