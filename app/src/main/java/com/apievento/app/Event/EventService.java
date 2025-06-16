package com.apievento.app.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    public Event findEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id){
        eventRepository.deleteById(id);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id)
                .map(event -> {
                    // Atualiza apenas os campos permitidos
                    event.setName(eventDetails.getName());
                    event.setCategory(eventDetails.getCategory());
                    event.setDescription(eventDetails.getDescription());
                    event.setEventDateInitial(eventDetails.getEventDateInitial());
                    event.setEventDateFinal(eventDetails.getEventDateFinal());
                    event.setEventTimeInitial(eventDetails.getEventTimeInitial());
                    event.setEventTimeFinal(eventDetails.getEventTimeFinal());
                    event.setState(eventDetails.getState());
                    event.setCity(eventDetails.getCity());
                    event.setStreet(eventDetails.getStreet());
                    event.setNumber(eventDetails.getNumber());
                    event.setComplement(eventDetails.getComplement());
                    event.setRegion(eventDetails.getRegion());
                    event.setCep(eventDetails.getCep());
                    event.setLatitude(eventDetails.getLatitude());
                    event.setLongitude(eventDetails.getLongitude());

                    // Organizador só pode ser alterado via endpoint específico
                    // event.setOrganizer(eventDetails.getOrganizer());

                    event.setUpdatedAt(new Date());
                    return eventRepository.save(event);
                })
                .orElse(null);
    }

    public List<Event> findEventsWithFilters(String state, String city, String region, Date date) {
        return eventRepository.findWithFilters(state, city, region, date);
    }

    public List<Event> findEventsByDateRange(Date startDate, Date endDate) {
        return eventRepository.findByDateRange(startDate, endDate);
    }

    public void softDeleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        if (event.getDeletedAt() == null) {
            event.setDeletedAt(new Date());
            eventRepository.save(event);

        }
    }

    public List<Event> findAllActiveEvents() {
        return eventRepository.findAllByDeletedAtIsNull();
    }
}
