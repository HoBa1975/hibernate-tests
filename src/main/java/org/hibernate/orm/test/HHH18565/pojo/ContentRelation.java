package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.AssociationOverride;
import jakarta.persistence.AssociationOverrides;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "m_nestor_verwendung")
public class ContentRelation {
    @Id
    @Column(name = "USE_ID")
    private String useId;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "INHALT", nullable = false)
    private AbstractVersion targetObject;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "BEHAELTER", nullable = false)
    private AbstractVersion sourceObject;

    @Embedded
    @AssociationOverrides(value = {
            @AssociationOverride(name = "source", joinColumns = @JoinColumn(name = "BEHAELTER", nullable = false, insertable = false, updatable = false, unique = true)),
            @AssociationOverride(name = "target", joinColumns = @JoinColumn(name = "INHALT", nullable = false, insertable = false, updatable = false, unique = true)) })
    private SourceAndTarget sourceAndTarget;

    public void setUseId(String number) {
        this.useId = number;
    }

    public void setSourceObject(AbstractVersion container) {
        this.sourceObject = container;
    }

    public void setTargetObject(AbstractVersion content) {
        this.targetObject = content;
    }

    public String getUseId() {
        return this.useId;
    }

    public void setSourceAndTarget(SourceAndTarget sourceAndTarget) {
        this.sourceAndTarget = sourceAndTarget;
    }
}
