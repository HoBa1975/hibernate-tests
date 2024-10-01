package org.hibernate.orm.test.HHH18501.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.DiscriminatorFormula;

import java.util.List;

@Entity
@Table(name = "TABLE_VERSIONS")
@DiscriminatorFormula("case WHEN STATUS = 'O' THEN REPRAESENTATION ELSE 'DELETED' END")
public abstract class AbstractVersion {

    @Id
    @Column(name = "VERSION_ID")
    private String id;

    @Column(name = "VERSION_NAME", nullable = false)
    private String versionName;

    @Column(name = "STATUS", nullable = false, length = 1)
    private String state;

    @Column(name = "REPRAESENTATION", length = 1)
    private String repraesentation;

//    @OneToMany(mappedBy = "version", orphanRemoval = true)
//    List<VersionAttribute> versionAttributes;

    public void setId(String number) {
        this.id = number;
    }

    public void setVersionName(String name) {
        this.versionName = name;
    }

    public void setStatus(String status) {
        this.state = status;
    }

    public void setRepraesentation(String repraesentation) {
        this.repraesentation = repraesentation;
    }
}
