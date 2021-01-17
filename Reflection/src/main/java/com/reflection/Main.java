package com.reflection;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;


import java.io.*;
import java.lang.reflect.Proxy;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


public class Main {
    public static void main(String args[]) {
        try {
            checkSize1();
            checkSize2();
            showInConsole();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkSize1() throws IOException {
        File file1 = new File("Data1");
        File file = new File("Data2");
        file.delete();
        Files.copy(file1.toPath(), file.toPath());

        Service delegate = new ServiceImpl();
        Service service = (Service) Proxy.newProxyInstance(ClassLoader.
                        getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CachedInvocationHandler(delegate, 5));
        PrintStream stream1 = mock(PrintStream.class);
        System.setOut(stream1);

        service.doHardWork("work1", 1);
        service.doHardWork("work11", 2);
        service.doHardWork("work112", 3);
        service.doHardWork("work13", 5);
        service.doHardWork("work11", 6);

        verify(stream1, times(5)).println("Cache value");
    }


    public static void checkSize2() throws IOException {
        File file1 = new File("Data1");
        File file = new File("Data2");
        file.delete();
        Files.copy(file1.toPath(), file.toPath());

        Service delegate = new ServiceImpl();
        Service service = (Service) Proxy.newProxyInstance(ClassLoader.
                        getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CachedInvocationHandler(delegate, 12));
        PrintStream stream1 = mock(PrintStream.class);
        System.setOut(stream1);

        service.doHardWork("work1", 1);
        service.doHardWork("work11", 2);
        service.doHardWork("work112", 3);
        service.doHardWork("work13", 5);
        service.doHardWork("work11", 6);
        service.doHardWork("work3", 7);
        service.doHardWork("work2", 8);
        service.doHardWork("work12", 4);
        service.doHardWork("work13", 5);
        service.doHardWork("work11", 6);
        service.doHardWork("work3", 7);
        double r3 = service.doHardWork("work2", 8);

        Assert.assertEquals(8, r3, 0.0);
        verify(stream1, times(12)).println("Cache value");
    }


    public static void showInConsole() {

        File file = new File("Data1");
        file.delete();
        PrintStream stream1 = mock(PrintStream.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        System.setOut(stream1);
        Service delegate = new ServiceImpl();
        Service service = (Service) Proxy.newProxyInstance(ClassLoader.
                        getSystemClassLoader(),
                delegate.getClass().getInterfaces(),
                new CachedInvocationHandler(delegate, 3));
        service.doHardWork("work1", 1);
        service.doHardWork("work1", 1);
        String params = "Cache value";
        verify(stream1).println(captor.capture());
        assertEquals(captor.getValue(), params);

    }
}
