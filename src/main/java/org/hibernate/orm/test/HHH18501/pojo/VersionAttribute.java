package org.hibernate.orm.test.HHH18501.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "TABLE_VERSION_ATTRIBUTE")
public class VersionAttribute {

    @Id
    int id;

    @Column(name = "ATTRIBUTE_NAME")
    String attributeName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
//    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "VERSION_ID", nullable = false, referencedColumnName = "VERSION_ID", foreignKey =
    @jakarta.persistence.ForeignKey(name = "FK_VERSION_ATTRIBUTE"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    AbstractVersion version;

    public void setId(int number) {
        this.id = number;
    }

    public void setVersion(ContainerVersion container) {
        this.version = container;
    }

    public void setAttributeName(String name) {
        this.attributeName = name;
    }
}
