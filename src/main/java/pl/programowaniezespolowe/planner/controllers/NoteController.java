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
        Date dt = Calendar.getInstance().getTime();
        note.setDate(dt);
        note.setUserid(3);
        note.setEventid(Integer.valueOf(eventid));
        noteRepository.save(note);
        return getNotesByEvent(eventid);
    }

    @CrossOrigin
    @DeleteMapping("note/{id}")
    public List<Note> deleteUser(@PathVariable String id) {
        int noteId = Integer.parseInt(id);
        int eventid = 0;
        
        List<Note> notes = noteRepository.findAll();
        for(Note n : notes) {
            if(n.getId() == Integer.valueOf(id)) {
                eventid = n.getEventid();
            }
        }

        noteRepository.deleteById(noteId);
        return getNotesByEvent(String.valueOf(eventid));
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
