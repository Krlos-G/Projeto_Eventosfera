package com.apievento.app.Event;

import com.apievento.app.Event_Image.EventImage;
import com.apievento.app.Organizer.Organizer;
import com.apievento.app.SoftDeleteConfig.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "event")
public class Event extends BaseEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "organizer_id", nullable = false)
        private Organizer organizer;

        @Column(nullable = false)
        private String name;

        @Column(nullable = false)
        private String category;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(name = "event_date_initial", nullable = false)
        @Temporal(TemporalType.DATE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date eventDateInitial;

        @Column(name = "event_date_final")
        @Temporal(TemporalType.DATE)
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date eventDateFinal;

        @Column(name = "event_time_initial")
        @Temporal(TemporalType.TIME)
        @JsonFormat(pattern = "HH:mm")
        private Date eventTimeInitial;

        @Column(name = "event_time_final")
        @Temporal(TemporalType.TIME)
        @JsonFormat(pattern = "HH:mm")
        private Date eventTimeFinal;

        @Column(nullable = false)
        private String state;

        @Column(nullable = false)
        private String city;

        @Column(nullable = false)
        private String street;

        private Integer number;

        private String complement;

        private String region;

        private String cep;

        private String latitude;

        private String longitude;

        @Column(name = "created_at", updatable = false)
        @Temporal(TemporalType.TIMESTAMP)
        private Date createdAt;

        @Column(name = "updated_at")
        @Temporal(TemporalType.TIMESTAMP)
        private Date updatedAt;

        @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<EventImage> images;

        @PrePersist
        protected void onCreate() {
            createdAt = new Date();
            updatedAt = new Date();
        }

        @PreUpdate
        protected void onUpdate() {
            updatedAt = new Date();
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Organizer getOrganizer() {
            return organizer;
        }

        public void setOrganizer(Organizer organizer) {
            this.organizer = organizer;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getEventDateInitial() {
            return eventDateInitial;
        }

        public void setEventDateInitial(Date eventDateInitial) {
            this.eventDateInitial = eventDateInitial;
        }

        public Date getEventDateFinal() {
            return eventDateFinal;
        }

        public void setEventDateFinal(Date eventDateFinal) {
            this.eventDateFinal = eventDateFinal;
        }

        public Date getEventTimeInitial() {
            return eventTimeInitial;
        }

        public void setEventTimeInitial(Date eventTimeInitial) {
            this.eventTimeInitial = eventTimeInitial;
        }

        public Date getEventTimeFinal() {
            return eventTimeFinal;
        }

        public void setEventTimeFinal(Date eventTimeFinal) {
            this.eventTimeFinal = eventTimeFinal;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public Integer getNumber() {
            return number;
        }

        public void setNumber(Integer number) {
            this.number = number;
        }

        public String getComplement() {
            return complement;
        }

        public void setComplement(String complement) {
            this.complement = complement;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<EventImage> getImages() {
            return images;
        }

        public void setImages(List<EventImage> images) {
            this.images = images;
        }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", organizer=" + organizer +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", eventDateInitial=" + eventDateInitial +
                ", eventDateFinal=" + eventDateFinal +
                ", eventTimeInitial=" + eventTimeInitial +
                ", eventTimeFinal=" + eventTimeFinal +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", complement='" + complement + '\'' +
                ", region='" + region + '\'' +
                ", cep='" + cep + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", images=" + images +
                '}';
    }
}
