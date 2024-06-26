package org.hibernate.orm.test.HHH18085.pojo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "Account")
@DiscriminatorValue("Unone")
public abstract class Account extends Grantee {
    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "STATE", nullable = false)
    public String state;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL, targetEntity =
            DomainAccount.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<DomainAccount> domainAccounts;

    public Account() {
        super();
        this.setGranteeType("U");
        this.setAuthType("none");
        this.domainAccounts = new HashSet<>();
    }

    public Account(String id, String name) {
        super(id);
        this.setGranteeType("U");
        this.setAuthType("none");
        this.name = name;
        this.state = "O";
        this.domainAccounts = new HashSet<>();
    }

    public Account(String id, String name, User user) {
        super(id);
        this.setGranteeType("U");
        this.setAuthType("none");
        this.name = name;
        this.state = "O";
        this.user = user;
        this.domainAccounts = new HashSet<>();
    }

    public String getLoginName() {
        return name;
    }

    public User getUser() {
        return user;
    }

    public Set<DomainAccount> getDomainAccounts() {
        return domainAccounts;
    }

    public boolean hasDomainAccounts() {
        return domainAccounts.isEmpty();
    }
}
