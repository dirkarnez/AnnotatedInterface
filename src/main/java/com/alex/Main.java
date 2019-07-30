package com.alex;

import java.lang.reflect.Proxy;

interface IService {
    @BugReport(email = "somebody@gmail.com")
    void doTaskA();

    @BugReport
    void doTaskB();

    void doTaskC();
}

public class Main {
    public static void main(String[] args) {
        IService service = make(IService.class);
        service.doTaskA();
        System.out.println();
        service.doTaskB();
        System.out.println();
        service.doTaskC();
    }

    public static <T> T make(Class<T> clazz) {
        try {
            return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                    new Class<?>[]{clazz}, (proxy, method, args) -> {
                        final String methodName = method.getName();
                        System.out.println(String.format("invoked %s of %s", methodName, clazz.getName()));

                        if (method.isAnnotationPresent(BugReport.class)) {
                            BugReport cpy = method.getAnnotation(BugReport.class);
                            String email = cpy.email();
                            if (email != null && email.trim().length() > 0) {
                                System.out.println(String.format("email %s", email));
                            } else {
                                System.out.println(String.format("email is null or empty"));
                            }
                        } else {
                            System.out.println(String.format("No BugReport found"));
                        }
                        return null;
                    });
        } catch (Exception e) {
            return null;
        }
    }
}