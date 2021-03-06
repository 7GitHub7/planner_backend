package pl.programowaniezespolowe.planner.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.programowaniezespolowe.planner.dtos.EventDto;
import pl.programowaniezespolowe.planner.event.Event;
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
    @GetMapping(path = "/{userid}/notes/{eventid}")
    public List<Note> getNotesByEventId(@PathVariable String eventid, @PathVariable String userid) {
        return getNotesByEvent(eventid,userid);
    }

    @CrossOrigin
    @PostMapping(path = "/{userid}/note/{eventid}")
    public List<Note> createNote(@PathVariable String userid, @RequestBody Note note, @PathVariable String eventid) {
        Date dt = Calendar.getInstance().getTime();
        note.setDate(dt);
        note.setUserid(Integer.valueOf(userid));
        note.setEventid(Integer.valueOf(eventid));
        noteRepository.save(note);
        return getNotesByEvent(eventid,userid);
    }

    @CrossOrigin
    @PutMapping("/{userid}/note/{noteId}")
    public ResponseEntity<?> updateEvent(@RequestBody Note note, @PathVariable Integer noteId, @PathVariable String userid) {
        Optional<Note> updateNote = noteRepository.findById(noteId);
        if(updateNote.isPresent()) {
            updateNote.get().setTitle(note.getTitle());
            updateNote.get().setDescription(note.getDescription());
            updateNote.get().setEventid(note.getEventid());
            updateNote.get().setUserid(note.getUserid());
            updateNote.get().setDate(note.getDate());
            updateNote.get().setDone(note.getDone());
            noteRepository.save(updateNote.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @DeleteMapping("/{userid}/note/{id}")
    public List<Note> deleteNote(@PathVariable String id,@PathVariable String userid) {
        int noteId = Integer.parseInt(id);
        int eventid = 0;

        List<Note> notes = noteRepository.findAll();
        for(Note n : notes) {
            if(n.getId() == Integer.valueOf(id)) {
                eventid = n.getEventid();
            }
        }
        noteRepository.deleteById(noteId);
        return getNotesByEvent(String.valueOf(eventid),userid);
    }

    public List<Note> getNotesByEvent(String eventid, String userid) {
        int eventId = Integer.parseInt(eventid);
        List<Note> notes =  noteRepository.findAll();

        List<Note> notesEventId = new ArrayList<Note>();

        for(Note n : notes) {
            if(n.getEventid() == eventId && n.getUserid() == Integer.valueOf(userid)) {
                notesEventId.add(n);
            }
        }
        return notesEventId;
    }

}
