package pl.programowaniezespolowe.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.dtos.CalendarEventDto;
import pl.programowaniezespolowe.planner.dtos.EventDto;
import pl.programowaniezespolowe.planner.event.Event;
import pl.programowaniezespolowe.planner.event.EventRepository;
import pl.programowaniezespolowe.planner.user.User;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @CrossOrigin
    @GetMapping(path = "/events")
    public List<EventDto> getEvents() {
        return getAllEvents();
    }

    @CrossOrigin
    @GetMapping(path = "/event/{id}")
    public Optional<Event> getEvent(@PathVariable String id) {
        int eventId = Integer.parseInt(id);
        return eventRepository.findById(eventId);
    }

    @CrossOrigin
    @PostMapping("/event")
    public ResponseEntity<?> createEvent(@RequestBody EventDto event) {
        System.out.println(event);

        eventRepository.save(new Event(event.getUserID(), event.getCalendarEvent().getTitle(), Date.from(event.getCalendarEvent().getStart()), Date.from(event.getCalendarEvent().getEnd())));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PutMapping("/event/{id}")
    public List<EventDto> updateEvent(@RequestBody EventDto event) {
        System.out.println(event);

        //eventRepository.save(new Event(event.getUserID(), event.getCalendarEvent().getTitle(), Date.from(event.getCalendarEvent().getStart()), Date.from(event.getCalendarEvent().getEnd())));

        List<Event> events = eventRepository.findAll();

        for(Event e : events) {
            if(e.getId() == event.getCalendarEvent().getId()) {
                e.setTitle(event.getCalendarEvent().getTitle());
                e.setStart(Date.from(event.getCalendarEvent().getStart()));
                e.setEnd(Date.from(event.getCalendarEvent().getEnd()));
                e.setUserID(event.getUserID());
                eventRepository.save(e);
            }
        }
        return getAllEvents();
    }


    @CrossOrigin
    @DeleteMapping("event/{id}")
    public List<EventDto> deleteEvent(@PathVariable String id) {
        int eventId = Integer.parseInt(id);
        eventRepository.deleteById(eventId);
        return getAllEvents();
    }

    public List<EventDto> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        ArrayList<EventDto> mapedEvents = new ArrayList<>();
        for (Event event : events) {
            if(event.getStart() != null)
                mapedEvents.add(new EventDto(new CalendarEventDto(event.getId(), event.getTitle(), Instant.ofEpochMilli(event.getStart().getTime()), Instant.ofEpochMilli(event.getEnd().getTime())), event.getUserID()));
        }
        return mapedEvents;
    }


}
