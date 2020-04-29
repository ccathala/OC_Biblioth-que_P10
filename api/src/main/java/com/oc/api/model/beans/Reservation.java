package com.oc.api.model.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "book_id"),
            @JoinColumn(name = "library_id")
    })
    private AvailableCopie availableCopie;

    @ManyToOne
    @JoinColumn(name = "registered_user_id")
    private RegisteredUser registereduser;

    @NotNull
    @Column(name = "notification_is_sent")
    private Boolean notificationIsSent;

    @Column(name = "avalaibility_date")
    private LocalDate avalaibilityDate;

    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AvailableCopie getAvailableCopie() {
        return availableCopie;
    }

    public void setAvailableCopie(AvailableCopie availableCopie) {
        this.availableCopie = availableCopie;
    }

    public RegisteredUser getRegistereduser() {
        return registereduser;
    }

    public void setRegistereduser(RegisteredUser registereduser) {
        this.registereduser = registereduser;
    }

    public Boolean getNotificationIsSent() {
        return notificationIsSent;
    }

    public void setNotificationIsSent(Boolean notificationIsSent) {
        this.notificationIsSent = notificationIsSent;
    }

    public LocalDate getAvaibilityDate() {
        return avalaibilityDate;
    }

    public void setAvaibilityDate(LocalDate avaibilityDate) {
        this.avalaibilityDate = avaibilityDate;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", availableCopie=" + availableCopie +
                ", registereduser=" + registereduser +
                ", notificationIsSent=" + notificationIsSent +
                ", avalaibilityDate=" + avalaibilityDate +
                '}';
    }
}
