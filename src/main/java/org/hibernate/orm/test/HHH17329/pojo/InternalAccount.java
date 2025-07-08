package org.hibernate.orm.test.HHH17329.pojo;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity(name = "InternalAccount")
@DiscriminatorValue("Uinternal")
public class InternalAccount extends Account {

    public InternalAccount() {
        super();
        this.setAuthType("internal");
    }

    public InternalAccount(String id, String loginName) {
        super(id, loginName);
        this.setAuthType("internal");
    }

    public InternalAccount(String id, String loginName, MyUser myUser) {
        super(id, loginName, myUser);
        this.setAuthType("internal");
    }
}
