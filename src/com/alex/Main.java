package com.alex;

import java.lang.reflect.Proxy;

interface IService {
    String getName();
}

public class Main {
    public static void main(String[] args) {
        IService service = make(IService.class);
        System.out.println(service.getName());
    }

    public static <T> T make(Class<T> clazz) {
        try {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                    new Class<?>[]{clazz}, (proxy, method, args) -> {
                        System.out.println(String.format("invoked %s", method));
                        return "1234";
                    });
        } catch (Exception e) {
            return null;
        }
    }
}