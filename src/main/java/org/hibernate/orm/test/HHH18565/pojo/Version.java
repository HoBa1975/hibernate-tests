package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("N")
public class Version extends AbstractVersion {
}
