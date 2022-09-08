package org.vsdl.common.mmo.consistency;

import org.vsdl.common.mmo.exceptions.UnrecognizedClassMaintenanceException;

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

    public void applyTo(Maintainable target) throws UnrecognizedClassMaintenanceException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> targetClass = Class.forName(forClass);
        if (!(target.getClass().equals(targetClass))) {
            throw new UnrecognizedClassMaintenanceException(forClass, target.getClass().getName());
        }
        Method targetMethod = targetClass.getMethod(methodToCall, parameterTypes);
        targetMethod.invoke(target, parameters);
        target.incrementVersion();
    }
}

