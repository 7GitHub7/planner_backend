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
import pl.programowaniezespolowe.planner.user.UserRepository;


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

    @Autowired
    UserRepository userRepository;

    public boolean checkIsUserLogged(String userid) {
        List<User> users = userRepository.findAll();
        for(User u : users) {
            if(u.getId().toString().equals(userid)) {
                if (u.isLogged()) {
                    return true;
                }
            }
        }
        return false;
    }


    @CrossOrigin
    @GetMapping(path = "/{userid}/events")
    public List<EventDto> getEvents(@PathVariable String userid) {

        boolean canReturn = checkIsUserLogged(userid);

        List<EventDto> userEvents = new ArrayList<>();
        for(EventDto e : getAllEvents()) {
            if(e.getUserID().toString().equals(userid)) {
                userEvents.add(e);
            }
        }

        System.out.println(canReturn);

        if(canReturn) {
            return userEvents;
        }

        return null;
    }

    @CrossOrigin
    @GetMapping(path = "/{userid}/event/{id}")
    public Optional<Event> getEvent(@PathVariable String userid, @PathVariable String id) {

        boolean canReturn = checkIsUserLogged(userid);

        int eventId = Integer.parseInt(id);
        if(canReturn) return eventRepository.findById(eventId);
        else return null;
    }

    @CrossOrigin
    @PostMapping("/{userid}/event")
    public List<EventDto> createEvent(@PathVariable String userid, @RequestBody EventDto event) {

        boolean canReturn = checkIsUserLogged(userid);

        System.out.println(event);

        List<EventDto> userEvents = new ArrayList<>();
        for(EventDto e : getAllEvents()) {
            if(e.getUserID().toString().equals(userid)) {
                userEvents.add(e);
            }
        }

        if(canReturn) {
            eventRepository.save(new Event(Integer.valueOf(userid), event.getCalendarEvent().getTitle(), Date.from(event.getCalendarEvent().getStart()), Date.from(event.getCalendarEvent().getEnd())));
            return userEvents;
        }
        else return null;
    }

    @CrossOrigin
    @PutMapping("/{userid}/event/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable String userid, @RequestBody EventDto event, @PathVariable Integer eventId) {

        boolean canReturn = checkIsUserLogged(userid);

        Optional<Event> updateEvent = eventRepository.findById(eventId);
        if(updateEvent.isPresent()) {
            updateEvent.get().setStart(Date.from(event.getCalendarEvent().getStart()));
            updateEvent.get().setEnd(Date.from(event.getCalendarEvent().getEnd()));
            updateEvent.get().setTitle(String.valueOf(event.getCalendarEvent().getTitle()));
            updateEvent.get().setUserID(Integer.valueOf(userid));
            eventRepository.save(updateEvent.get());
            if(canReturn) return new ResponseEntity<>(HttpStatus.OK);
            else return null;
        }
        else{
            if(canReturn) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            else return null;
        }
    }


    @CrossOrigin
    @DeleteMapping("/{userid}/event/{id}")
    public List<EventDto> deleteEvent(@PathVariable String userid, @PathVariable String id) {

        boolean canReturn = checkIsUserLogged(userid);

        List<EventDto> userEvents = new ArrayList<>();
        for(EventDto e : getAllEvents()) {
            if(e.getUserID().toString().equals(userid)) {
                userEvents.add(e);
            }
        }

        int eventId = Integer.parseInt(id);

        List<Note> notes =  noteRepository.findAll();
        List<Note> notesEventId = new ArrayList<Note>();

        for(Note n : notes) {
            if(n.getEventid() == eventId) {
                notesEventId.add(n);
            }
        }

        for(Note n : notesEventId) {
            if(canReturn) noteRepository.delete(n);
        }

        eventRepository.deleteById(eventId);
        if(canReturn) return userEvents;
        else return null;
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
