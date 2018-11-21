package com.emrood.e_contact.Model;

import com.emrood.e_contact.Utils.Constant;

/**
 * Created by Noel Emmanuel Roodly on 11/20/2018.
 */
public class QRObject {

    private Contact contact;
    private String token;

    public QRObject(Contact contact, String token) {
        this.contact = contact;
        this.token = token;
    }


    public QRObject(Contact contact) {
        this.contact = contact;
        this.token = Constant.token;
    }


    public Contact getContact() {
        return contact;
    }

    public String getToken() {
        return token;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
