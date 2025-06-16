package com.apievento.app.Event;

import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class EventSearchDTO {

    @Size(min = 2, max = 2, message = "O estado deve ter 2 caracteres")
    private String state;

    private String city;
    private String region;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
