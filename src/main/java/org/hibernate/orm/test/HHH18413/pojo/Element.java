package org.hibernate.orm.test.HHH18413.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "TABLE_ELEMENTS")
public class Element {
    @Id
    @Column(name = "ELEMENT_ID")
    private String id;

    @Column(nullable = false, name = "ELEMENT_NAME")
    private String elementName;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(nullable = false, name = "CLASS_ID")
    private ElementClass elementClass;

    public void setElementClass(ElementClass elementClass) {
        this.elementClass = elementClass;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public void setId(String number) {
        this.id = number;
    }
}
