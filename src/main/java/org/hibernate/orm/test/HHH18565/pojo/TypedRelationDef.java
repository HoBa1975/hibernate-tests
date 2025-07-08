package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "m_nestor_legale_vers_relatione")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TypedRelationDef {

    @Id
    @Column(name = "LEGAL_REL_ID")
    private String legalRelId;

    public void setId(String number) {
        this.legalRelId = number;
    }
}