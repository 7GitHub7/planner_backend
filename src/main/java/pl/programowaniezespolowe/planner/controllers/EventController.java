package pl.programowaniezespolowe.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.dtos.CalendarEventDto;
import pl.programowaniezespolowe.planner.dtos.EventDto;
import pl.programowaniezespolowe.planner.event.Event;
import pl.programowaniezespolowe.planner.event.EventRepository;
import pl.programowaniezespolowe.planner.note.Note;
import pl.programowaniezespolowe.planner.note.NoteRepository;
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

    @Autowired
    NoteRepository noteRepository;

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
    public List<EventDto> createEvent(@RequestBody EventDto event) {
        System.out.println(event);

        eventRepository.save(new Event(event.getUserID(), event.getCalendarEvent().getTitle(), Date.from(event.getCalendarEvent().getStart()), Date.from(event.getCalendarEvent().getEnd())));

        return getAllEvents();
    }

    @CrossOrigin
    @PutMapping("/event/{eventId}")
    public ResponseEntity<?> updateEvent(@RequestBody EventDto event, @PathVariable Integer eventId) {
        Optional<Event> updateEvent = eventRepository.findById(eventId);
        if(updateEvent.isPresent()) {
            updateEvent.get().setStart(Date.from(event.getCalendarEvent().getStart()));
            updateEvent.get().setEnd(Date.from(event.getCalendarEvent().getEnd()));
            updateEvent.get().setTitle(String.valueOf(event.getCalendarEvent().getTitle()));
            updateEvent.get().setUserID(event.getUserID());
            eventRepository.save(updateEvent.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @CrossOrigin
    @DeleteMapping("event/{id}")
    public List<EventDto> deleteEvent(@PathVariable String id) {
        int eventId = Integer.parseInt(id);

        List<Note> notes =  noteRepository.findAll();
        List<Note> notesEventId = new ArrayList<Note>();

        for(Note n : notes) {
            if(n.getEventid() == eventId) {
                notesEventId.add(n);
            }
        }

        for(Note n : notesEventId) {
            noteRepository.delete(n);
        }

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
