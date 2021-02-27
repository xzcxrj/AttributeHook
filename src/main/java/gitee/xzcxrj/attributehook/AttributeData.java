package gitee.xzcxrj.attributehook;

import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SubAttribute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AttributeData extends SXAttributeData {

    private static Method getSubAttribute;
    private static Method getAttributes;


    static {
        if (AttributeHook.getSXAttributeVersion().contains("2.0.2")) {
            try {
                Class<?> dataClass = Class.forName("github.saukiya.sxattribute.data.attribute.SXAttributeData");
                Class<?> attributeClass = Class.forName("github.saukiya.sxattribute.data.attribute.SubAttribute");
                getSubAttribute = dataClass.getMethod("getSubAttribute", String.class);
                getAttributes = attributeClass.getMethod("getAttributes");
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private static SubAttribute getAttribute(String s, SXAttributeData data) {
        try {
            return (SubAttribute) getSubAttribute.invoke(data, s);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    private static double[] get(SubAttribute attribute) {
        try {
            return (double[]) getAttributes.invoke(attribute);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return new double[]{};
        }
    }


    public AttributeData add(SXAttributeData attributeData) {
        super.add(attributeData);
        return this;
    }

    public AttributeData take(SXAttributeData data) {
        if (AttributeHook.getSXAttributeVersion().contains("2.0.2")) {
            return this;
        }
        super.take(data);
        return this;
    }

    public double[] getAttributeValues(SubAttribute attribute) {
        if (AttributeHook.getSXAttributeVersion().contains("2.0.2")) {
            return get(attribute);
        }else {
            return getValues(attribute);
        }
    }

    public double[] getAttributeValues(String attribute) {
        if (AttributeHook.getSXAttributeVersion().contains("2.0.2")) {
            SubAttribute subAttribute = AttributeData.getAttribute(attribute, this);
            if (subAttribute != null) {
                return AttributeData.get(subAttribute);
            }else {
                return new double[]{};
            }
        }else {
            return getValues(attribute);
        }
    }
}
