package org.vsdl.common.mmo.consistency;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.vsdl.common.mmo.utils.GsonUtils.convertJSONStringToObject;
import static org.vsdl.common.mmo.utils.GsonUtils.convertObjectToJSONString;

public class MaintenanceTransaction implements Serializable {

    private final String forClass;
    private final String methodToCall;
    private final String[] methodParameterClassNames;
    private final String[] parameterObjectJSONs;
    private final String[] parameterObjectClassCastNames;

    public MaintenanceTransaction(String forClass, String methodToCall, Class[] parameterClasses, Object[] parameterObjects) {
        if (parameterClasses.length != parameterObjects.length) {
            throw new IllegalArgumentException("Number of parameter classes(" + parameterClasses.length +
                    ") must match number of parameter objects(" + parameterObjects.length + ")!");
        }
        this.forClass = forClass;
        this.methodToCall = methodToCall;
        this.methodParameterClassNames = new String[parameterClasses.length];
        for (int i = 0; i < parameterClasses.length; ++i) {
            this.methodParameterClassNames[i] = parameterClasses[i].getName();
        }
        this.parameterObjectJSONs = new String[parameterObjects.length];
        this.parameterObjectClassCastNames = new String[parameterObjects.length];
        for (int i = 0; i < parameterObjects.length; ++i) {
            this.parameterObjectJSONs[i] = convertObjectToJSONString(parameterObjects[i]);
            this.parameterObjectClassCastNames[i] = parameterObjects[i].getClass().getName();
        }
    }

    public void applyTo(Maintainable target) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Class<?> targetClass = Class.forName(forClass);
        Class[] parameterClasses = new Class[methodParameterClassNames.length];
        for (int i = 0; i < methodParameterClassNames.length; ++i) {
            parameterClasses[i] = Class.forName(methodParameterClassNames[i]);
        }
        Object[] parameterObjects = new Object[parameterObjectJSONs.length];
        for (int i = 0; i < parameterObjectJSONs.length; ++i) {
            parameterObjects[i] = convertJSONStringToObject(parameterObjectJSONs[i], Class.forName(parameterObjectClassCastNames[i]));
        }
        Method targetMethod = targetClass.getMethod(methodToCall, parameterClasses);
        targetMethod.invoke(target, parameterObjects);
        target.incrementVersion();
    }
}

