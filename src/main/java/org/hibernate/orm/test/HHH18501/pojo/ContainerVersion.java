package org.hibernate.orm.test.HHH18501.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("G")
public class ContainerVersion extends Version{
    @Column(name = "CONTENTCOUNT", updatable = false)
    private Long contentCount = 0L;
}
