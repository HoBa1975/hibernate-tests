package org.hibernate.orm.test.HHH18413.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("N")
public class Version extends AbstractVersion {

    @Column(name = "VERSION_DATA_1", nullable = false)
    private String versionDataOne;

    @Column(name = "VERSION_DATA_2", nullable = false)
    private String versionDataTwo;

    public void setVersionData1(String versionData1) {
        this.versionDataOne = versionData1;
    }

    public void setVersionData2(String versionData2) {
        this.versionDataTwo = versionData2;
    }

    public String getVersionData1() {
        return versionDataOne;
    }
}
