package org.hibernate.orm.test.querycache.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "Account")
@DiscriminatorValue("Unone")
public abstract class Account extends Grantee {
    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "STATE", nullable = false)
    public String state;

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

    public String getLoginName() {
        return name;
    }
}
