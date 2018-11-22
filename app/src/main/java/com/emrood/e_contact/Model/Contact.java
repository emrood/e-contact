package com.emrood.e_contact.Model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Noel Emmanuel Roodly on 11/18/2018.
 */

@Entity(nameInDb = "contacts")
public class Contact implements Serializable {

    @Id(autoincrement = true)
    Long id;
    private long contact_ID;


    static final long serialVersionUID = 0;

    @Unique
    String cellular_phone;

    @Unique
    String work_phone;

    @Unique
    String other_phone;

    String first_name;

    String last_name;

    String email;

    String personal_email;

    String work_email;

    String other_email;

    String note;

    Date birthday;

    String entreprise;

    String photo;

    Boolean isSecret = false;

    Boolean isFav = false;

    @Generated(hash = 802002569)
    public Contact(Long id, long contact_ID, String cellular_phone,
            String work_phone, String other_phone, String first_name,
            String last_name, String email, String personal_email,
            String work_email, String other_email, String note, Date birthday,
            String entreprise, String photo, Boolean isSecret, Boolean isFav) {
        this.id = id;
        this.contact_ID = contact_ID;
        this.cellular_phone = cellular_phone;
        this.work_phone = work_phone;
        this.other_phone = other_phone;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.personal_email = personal_email;
        this.work_email = work_email;
        this.other_email = other_email;
        this.note = note;
        this.birthday = birthday;
        this.entreprise = entreprise;
        this.photo = photo;
        this.isSecret = isSecret;
        this.isFav = isFav;
    }

    @Generated(hash = 672515148)
    public Contact() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getContact_ID() {
        return this.contact_ID;
    }

    public void setContact_ID(long contact_ID) {
        this.contact_ID = contact_ID;
    }

    public String getCellular_phone() {
        return this.cellular_phone;
    }

    public void setCellular_phone(String cellular_phone) {
        this.cellular_phone = cellular_phone;
    }

    public String getWork_phone() {
        return this.work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getOther_phone() {
        return this.other_phone;
    }

    public void setOther_phone(String other_phone) {
        this.other_phone = other_phone;
    }

    public String getFirst_name() {
        return this.first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return this.last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPersonal_email() {
        return this.personal_email;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }

    public String getWork_email() {
        return this.work_email;
    }

    public void setWork_email(String work_email) {
        this.work_email = work_email;
    }

    public String getOther_email() {
        return this.other_email;
    }

    public void setOther_email(String other_email) {
        this.other_email = other_email;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEntreprise() {
        return this.entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getIsSecret() {
        return this.isSecret;
    }

    public void setIsSecret(Boolean isSecret) {
        this.isSecret = isSecret;
    }

    public Boolean getIsFav() {
        return this.isFav;
    }

    public void setIsFav(Boolean isFav) {
        this.isFav = isFav;
    }




}
