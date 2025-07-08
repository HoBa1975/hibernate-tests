package org.hibernate.orm.test.HHH18565.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Embeddable
@MappedSuperclass
public class SourceAndTarget {
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "BEHAELTER")
    private AbstractVersion source;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "INHALT")
    private AbstractVersion target;

    public void setSource(AbstractVersion container) {
        this.source = container;
    }

    public void setTarget(AbstractVersion content) {
        this.target = content;
    }
}
