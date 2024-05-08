package org.hibernate.orm.test.HHHxxxxx.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity(name = "DomainAccount")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DomainAccount {
    @Id
    public String id;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account.class)
    @Fetch(FetchMode.SELECT)
    public Account account;

    public DomainAccount() {

    }

    public DomainAccount(String id) {
        super();
        this.id = id;
    }
}
