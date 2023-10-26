package org.hibernate.orm.test.querycache.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    public Account() {
        super();
        this.setGranteeType("U");
        this.setAuthType("none");
    }

    public Account(String id, String name) {
        super(id);
        this.setGranteeType("U");
        this.setAuthType("none");
        this.name = name;
        this.state = "O";
    }

    public Account(String id, String name, User user) {
        super(id);
        this.setGranteeType("U");
        this.setAuthType("none");
        this.name = name;
        this.state = "O";
        this.user = user;
    }

    public String getLoginName() {
        return name;
    }

    public User getUser() {
        return user;
    }
}
