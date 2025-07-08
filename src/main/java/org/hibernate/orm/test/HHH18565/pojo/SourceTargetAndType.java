package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "source", column = @Column(name = "FROM_VERSION")),
        @AttributeOverride(name = "target", column = @Column(name = "TO_VERSION"))
})
public class SourceTargetAndType extends SourceAndTarget {
    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "LEGAL_REL_ID", nullable = false, insertable = false, updatable = false)
    private TypedRelationDef relationType;

    public void setRelationType(TypedRelationDef relationDef) {
        this.relationType = relationDef;
    }
}
