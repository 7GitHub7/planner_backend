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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class NoteController {

    @Autowired
    NoteRepository noteRepository;

    @CrossOrigin
    @GetMapping(path = "/note")
    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    @CrossOrigin
    @GetMapping(path = "/note/{id}")
    public Optional<Note> getUser(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        return noteRepository.findById(userId);
    }

    @CrossOrigin
    @PostMapping("/note")
    public List<Note> createNote(@RequestBody Note note) {
//        String name = body.get("name");
//        String title = body.get("title");
//        String description =  body.get("description");
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("yyyy-MM-dd").parse(body.get("date"));
//            int userid = Integer.valueOf(body.get("userid"));
//            int eventid = Integer.valueOf(body.get("eventid"));
//            noteRepository.save(new Note(title, description, date, userid,eventid));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        System.out.println(note);
        noteRepository.save(note);
        return noteRepository.findAll();
    }

    @CrossOrigin
    @DeleteMapping("note/{id}")
    public List<Note> deleteUser(@PathVariable String id) {
        int noteId = Integer.parseInt(id);
        noteRepository.deleteById(noteId);
        return noteRepository.findAll();
    }

}
