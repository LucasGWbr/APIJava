package br.univates.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "password_hash")
    private String password;
    @Column(name = "document")
    private String document;
    @Column(name = "phone")
    private String phone;
    @Column(name = "registration_status")
    private String status;

    public users() {};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setId(Integer id) {
        this.user_id = id;
    }

    public Integer getId() {
        return user_id;
    }
}
