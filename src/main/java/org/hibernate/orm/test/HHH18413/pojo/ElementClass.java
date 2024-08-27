package org.hibernate.orm.test.HHH18413.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TABLE_CLASSES")
public class ElementClass {

    @Id
    @Column(name = "CLASS_ID")
    private String id;

    @Column(name = "CLASS_NAME", nullable = false)
    private String name;

    @Column(name = "CLASS_LOCATION", nullable = false)
    private String location;

    @Column(name = "STATUS", nullable = false)
    @Convert(converter = org.hibernate.type.YesNoConverter.class)
    private boolean valid;

    public void setId(String number) {
        this.id = number;
    }

    public void setElementClassName(String elementClassName) {
        this.name = elementClassName;
    }

    public void setValid(boolean value) {
        this.valid = value;
    }

    public void setLocation(String value) {
        this.location = value;
    }
}