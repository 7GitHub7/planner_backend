package pl.programowaniezespolowe.planner.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.event.Event;
import pl.programowaniezespolowe.planner.event.EventRepository;
import pl.programowaniezespolowe.planner.user.User;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @CrossOrigin
    @GetMapping(path = "/event")
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }


    @CrossOrigin
    @GetMapping(path = "/event/{id}")
    public Optional<Event> getEvent(@PathVariable String id) {
        int eventId = Integer.parseInt(id);
        return eventRepository.findById(eventId);
    }

    @CrossOrigin
    @PostMapping("/event")
    public List<Event> createEvent(@RequestBody Map<String, String> body) {
        System.out.println(body);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
        LocalDateTime startDate = LocalDateTime.parse(body.get("start"), formatter);
        LocalDateTime endDate = LocalDateTime.parse(body.get("end"), formatter);
        Integer noteid =  Integer.valueOf(body.get("noteid"));
        Integer userid =  Integer.valueOf(body.get("userid"));
        String title = body.get("title");
//        LocalDate startDate =  LocalDate.parse(body.get("start"));
//        LocalDate endDate = LocalDate.parse(body.get("end"));
        String color = body.get("color");
        String actions = body.get("actions");
//        Integer draggable = Integer.valueOf(body.get("draggable"));
        Integer draggable = 1;
//        Integer beforeStart =  Integer.valueOf(body.get("beforestart"));
        Integer beforeStart =  1;
//        Integer afterEnd =  Integer.valueOf(body.get("afterend"));
        Integer afterEnd =  1;

        eventRepository.save(new Event(title, startDate, endDate, color, actions, draggable,beforeStart,afterEnd,userid,noteid));
        return eventRepository.findAll();
    }

    @CrossOrigin
    @DeleteMapping("event/{id}")
    public List<Event> deleteEvent(@PathVariable String id) {
        int eventId = Integer.parseInt(id);
        eventRepository.deleteById(eventId);
        return eventRepository.findAll();
    }


}
