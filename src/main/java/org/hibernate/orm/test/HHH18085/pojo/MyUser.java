package org.hibernate.orm.test.HHH18085.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "MyUser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MyUser {
    @Id
    @Column(name = "USER_ID")
    private String id;

    @Column(name = "NAME")
    private String lastName;

    @Column(name = "VORNAME")
    private String firstName;

    public MyUser() {

    }

    public MyUser(String id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
    }
}
