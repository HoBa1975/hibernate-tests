package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Date;

@Entity
@Table(name = "M_NESTOR_VERS_RELATIONEN")
public class TypedVersionRelation {

    @Id
    @Column(name = "RELATION_ID")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "FROM_VERSION")
    private Version sourceObject;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "TO_VERSION")
    private Version targetObject = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "LEGAL_REL_ID")
    private TypedRelationDef relationDefinition;

    @Embedded
    @AssociationOverrides(value = {
            @AssociationOverride(name = "source", joinColumns = @JoinColumn(name = "FROM_VERSION", insertable = false, updatable = false, unique = true, nullable = false)),
            @AssociationOverride(name = "target", joinColumns = @JoinColumn(name = "TO_VERSION", insertable = false, updatable = false, unique = true, nullable = false)),
            @AssociationOverride(name = "relationType", joinColumns = @JoinColumn(name = "LEGAL_REL_ID", insertable = false, updatable = false, unique = true, nullable = false))})
    private SourceTargetAndType sourceTargetAndType;

    public void setId(String number) {
        this.id = number;
    }

    public void setSourceObject(Version container) {
        this.sourceObject = container;
    }

    public void setTargetObject(Version content) {
        this.targetObject = content;
    }

    public void setRelationDefinition(TypedRelationDef relationDef) {
        this.relationDefinition = relationDef;
    }
}
