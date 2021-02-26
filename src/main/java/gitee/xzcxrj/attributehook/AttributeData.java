package gitee.xzcxrj.attributehook;

import github.saukiya.sxattribute.data.attribute.SXAttributeData;

public class AttributeData extends SXAttributeData {

    public AttributeData add(SXAttributeData attributeData) {
        super.add(attributeData);
        return this;
    }

    public AttributeData take(SXAttributeData data) {
        if (AttributeHook.getVersion().contains("2.0.2")) {
            return this;
        }
        super.take(data);
        return this;
    }
}
