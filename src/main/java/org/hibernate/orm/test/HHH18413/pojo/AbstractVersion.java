package org.hibernate.orm.test.HHH18413.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "TABLE_VERSIONS")
@DiscriminatorFormula("case WHEN STATUS = 'O' THEN REPRAESENTATION ELSE 'DELETED' END")
public abstract class AbstractVersion {

    @Id
    @Column(name = "VERSION_ID")
    private String id;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String state;

    @Column(name = "REPRAESENTATION", length = 1)
    private String repraesentation;

    @Column(name = "IS_LATEST")
    @Convert(converter = org.hibernate.type.YesNoConverter.class)
    private boolean isLatest;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(nullable = false, name = "ELEMENT_ID")
    private Element element;

    public void setId(String number) {
        this.id = number;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public void setStatus(String v) {
        this.state = v;
    }

    public void setRepraesentation(String n) {
        this.repraesentation = n;
    }

    public void setIsLatest(boolean value) {
        this.isLatest = value;
    }
}
