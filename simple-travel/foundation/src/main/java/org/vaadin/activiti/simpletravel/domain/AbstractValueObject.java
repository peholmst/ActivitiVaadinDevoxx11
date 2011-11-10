package org.vaadin.activiti.simpletravel.domain;

import com.github.peholmst.stuff4vaadin.clone.CloneUtil;

public abstract class AbstractValueObject implements java.io.Serializable, Cloneable {

    @Override
    public AbstractValueObject clone() {
        try {
            return CloneUtil.deepClone((AbstractValueObject) super.clone());
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex.getMessage());
        }
    }
}
