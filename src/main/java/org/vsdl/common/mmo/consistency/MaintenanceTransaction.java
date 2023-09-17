package org.vsdl.common.mmo.consistency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MaintenanceTransaction {

    private final String forClass;
    private final String methodToCall;
    private final Class[] parameterTypes;
    private final Object[] parameters;

    public MaintenanceTransaction(String forClass, String methodToCall, Class[] parameterTypes, Object[] parameters) {
        this.forClass = forClass;
        this.methodToCall = methodToCall;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public void applyTo(Maintainable target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> targetClass = Class.forName(forClass);
        Method targetMethod = targetClass.getMethod(methodToCall, parameterTypes);
        targetMethod.invoke(target, parameters);
        target.incrementVersion();
    }
}

