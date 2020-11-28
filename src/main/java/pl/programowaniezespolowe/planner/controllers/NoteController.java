package pl.programowaniezespolowe.planner.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.event.EventRepository;
import pl.programowaniezespolowe.planner.note.Note;
import pl.programowaniezespolowe.planner.note.NoteRepository;
import pl.programowaniezespolowe.planner.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @CrossOrigin
    @GetMapping(path = "/notes")
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @CrossOrigin
    @GetMapping(path = "/notes/{eventid}")
    public List<Note> getNotesByEventId(@PathVariable String eventid) {
        return getNotesByEvent(eventid);
    }

    @CrossOrigin
    @PostMapping("/note/{eventid}")
    public List<Note> createNote(@RequestBody Note note, @PathVariable String eventid) {
//        int eventID = Integer.valueOf(body.get("eventId"));
//        String title = body.get("title");
//        String description =  body.get("description");
        Date dt = Calendar.getInstance().getTime();
        note.setDate(dt);
        note.setUserid(3);
        note.setEventid(Integer.valueOf(eventid));
//        Note note = new Note(title, description, dt, 1, eventID);
        noteRepository.save(note);
        return getNotesByEvent(eventid);
    }

    @CrossOrigin
    @DeleteMapping("note/{id}")
    public List<Note> deleteUser(@PathVariable String id) {
        int noteId = Integer.parseInt(id);
        noteRepository.deleteById(noteId);
        return getNotesByEvent(id);
    }

    public List<Note> getNotesByEvent(String eventid) {
        int eventId = Integer.parseInt(eventid);
        List<Note> notes =  noteRepository.findAll();

        List<Note> notesEventId = new ArrayList<Note>();

        for(Note n : notes) {
            if(n.getEventid() == eventId) {
                notesEventId.add(n);
            }
        }
        return notesEventId;
    }

}
