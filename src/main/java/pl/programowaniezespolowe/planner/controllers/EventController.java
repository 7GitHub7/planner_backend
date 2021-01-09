package pl.programowaniezespolowe.planner.controllers;

import io.swagger.models.auth.In;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
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


    @CrossOrigin
    @PostMapping("/{userid}/getTermEvents")
    public List<EventDto> getEventsFromPage(@PathVariable String userid) {
//        List<Event> events = eventRepository.findAll();
//        ArrayList<EventDto> mapedEvents = new ArrayList<>();
//        for (Event event : events) {
//            if(event.getStart() != null)
//                mapedEvents.add(new EventDto(new CalendarEventDto(event.getId(), event.getTitle(), Instant.ofEpochMilli(event.getStart().getTime()), Instant.ofEpochMilli(event.getEnd().getTime())), event.getUserID()));
//        }

        URL url;
        String inputLine = "";
        String html = "";
        try {
            // get URL content

            String a="https://www.p.lodz.pl/pl/rozklad-roku-akademickiego-202021";
            url = new URL(a);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));


            while ((inputLine = br.readLine()) != null) {
                html += inputLine;
            }
            br.close();

            System.out.println("Done");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String startWinter = "";
        String endWinter = "";
        String startLessonWinter = "";
        String endLessonWinter = "";
        String christmasBreakStart = "";
        String christmasBreakEnd = "";
        String winterSessionPartOneStart = "";
        String winterSessionPartOneEnd = "";
        String winterSessionPartTwoStart = "";
        String winterSessionPartTwoEnd = "";
        String winterHolidayStart = "";
        String winterHolidayEnd = "";
        String winterExtraDaysStart = "";
        String winterExtraDaysEnd = "";

        String startSummer = "";
        String endSummer = "";
        String startLessonSummer = "";
        String endLessonSummer = "";
        String springBreakStart = "";
        String springBreakEnd = "";
        String summerSessionPartOneStart = "";
        String summerSessionPartOneEnd = "";
        String summerSessionPartTwoStart = "";
        String summerSessionPartTwoEnd = "";
        String summerHolidayStart = "";
        String summerHolidayEnd = "";
        String summerExtraDaysStart = "";
        String summerExtraDaysEnd = "";

        if(html != null)
        {
            Document doc = Jsoup.parse(html);
            Element text = doc.select("div.text").first();

            System.out.println(text.toString());
            String [] lines = text.toString().split("\n");


            for(int i = 0; i < lines.length; i++) {
                //WINTER
                //Get start and end of term winter
                if(lines[i].contains("Semestr zimowy")) {
                    startWinter = lines[i];
                    endWinter = lines[i];
                }
                //Get start and end lessons
                else if(lines[i].contains("Okres zajęć")) {
                    startLessonWinter = lines[i];
                    endLessonWinter = lines[i];
                }
                //Get start and end lessons
                else if(lines[i].contains("Przerwa świąteczna")) {
                    christmasBreakStart = lines[i];
                    christmasBreakEnd = lines[i];
                }
                else if(lines[i].contains("Zimowa sesja egzaminacyjna:")) {
                    winterSessionPartOneStart = lines[i + 1];
                    winterSessionPartOneEnd = lines[i + 1];
                    winterSessionPartTwoStart = lines[i + 2];
                    winterSessionPartTwoEnd = lines[i + 2];
                }
                else if(lines[i].contains("Wakacje zimowe")) {
                    winterHolidayStart = lines[i + 1];
                    winterHolidayEnd = lines[i + 1];
                }
                else if(lines[i].contains("Dodatkowe dni wolne od zajęć:")) {
                    winterExtraDaysStart = lines[i + 1];
                    winterExtraDaysEnd = lines[i + 1];
                }

                //SUMMER
                //Get start and end of term winter
                if(lines[i].contains("Semestr letni")) {
                    startSummer = lines[i];
                    endSummer = lines[i];
                }
                else if(lines[i].contains("Okres zajęć:")) {
                    startLessonSummer = lines[i];
                    endLessonSummer = lines[i];
                }
                else if(lines[i].contains("Ferie wiosenne")) {
                    springBreakStart = lines[i];
                    springBreakEnd = lines[i];
                }
                else if(lines[i].contains("Letnia sesja egzaminacyjna:")) {
                    summerSessionPartOneStart = lines[i + 1];
                    summerSessionPartOneEnd = lines[i + 1];
                    summerSessionPartTwoStart = lines[i + 2];
                    summerSessionPartTwoEnd = lines[i + 2];
                }
                else if(lines[i].contains("Wakacje letnie")) {
                    summerHolidayStart = lines[i + 1];
                    summerHolidayEnd = lines[i + 1];
                }
            }
        }

        try {

            //Get dates of events for WINTER
            System.out.println("Obrobione wydarzenia");
            startWinter = startWinter.split("–")[0].substring(41, startWinter.split("–")[0].length() - 4);
            EventDto eventDto1 = createEventFromTime(startWinter, startWinter, "Poczatek semestru zimowego");

            endWinter = endWinter.split("–")[1].substring(1, endWinter.split("–")[1].length() - 9);
            EventDto eventDto2 = createEventFromTime(endWinter, endWinter, "Koniec semestru zimowego");

            startLessonWinter = startLessonWinter.split("–")[0].substring(34, startLessonWinter.split("–")[0].length() - 4);
            EventDto eventDto3 = createEventFromTime(startLessonWinter, startLessonWinter, "Poczatek zajec semestru zimowego");

            endLessonWinter = endLessonWinter.split("–")[1].substring(1, endLessonWinter.split("–")[1].length() - 7);
            EventDto eventDto4 = createEventFromTime(endLessonWinter, endLessonWinter, "Koniec zajec semestru zimowego");

            christmasBreakStart = christmasBreakStart.split("–")[0].substring(41, christmasBreakStart.split("–")[0].length() - 4);
            christmasBreakEnd = christmasBreakEnd.split("–")[1].substring(1, christmasBreakEnd.split("–")[1].length() - 7);
            EventDto eventDto5 = createEventFromTime(christmasBreakStart, christmasBreakEnd, "Przerwa swiateczna swieta Bozego Narodzenia");

            winterSessionPartOneStart = winterSessionPartOneStart.split("–")[0].substring(4, winterSessionPartOneStart.split("–")[0].length() - 4);
            winterSessionPartOneEnd = winterSessionPartOneEnd.split("–")[1].substring(1, winterSessionPartOneEnd.split("–")[1].length() - 7);
            EventDto eventDto6 = createEventFromTime(winterSessionPartOneStart, winterSessionPartOneEnd, "Sesja zimowa");

            winterSessionPartTwoStart = winterSessionPartTwoStart.split("–")[0].substring(4, winterSessionPartTwoStart.split("–")[0].length() - 4);
            winterSessionPartTwoEnd = winterSessionPartTwoEnd.split("–")[1].substring(1, winterSessionPartTwoEnd.split("–")[1].length() - 7);
            EventDto eventDto7 = createEventFromTime(winterSessionPartTwoStart, winterSessionPartTwoEnd, "Sesja zimowa");

            winterHolidayStart = winterHolidayStart.split("–")[0].substring(4, winterHolidayStart.split("–")[0].length() - 4);
            winterHolidayEnd = winterHolidayEnd.split("–")[1].substring(1, winterHolidayEnd.split("–")[1].length() - 7);
            EventDto eventDto8 = createEventFromTime(winterHolidayStart, winterHolidayEnd, "Wakacje zimowe");

            winterExtraDaysStart = winterExtraDaysStart.split("do")[0].substring(7, winterExtraDaysStart.split("do")[0].length() - 4);
            winterExtraDaysEnd = winterExtraDaysEnd.split("do")[1].substring(1, winterExtraDaysEnd.split("do")[1].length() - 7);
            EventDto eventDto9 = createEventFromTime(winterExtraDaysStart, winterExtraDaysEnd, "Dodatkowy dzien wolny od zajec");

            eventRepository.save(new Event(Integer.valueOf(userid), eventDto1.getCalendarEvent().getTitle(), Date.from(eventDto1.getCalendarEvent().getStart()), Date.from(eventDto1.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto2.getCalendarEvent().getTitle(), Date.from(eventDto2.getCalendarEvent().getStart()), Date.from(eventDto2.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto3.getCalendarEvent().getTitle(), Date.from(eventDto3.getCalendarEvent().getStart()), Date.from(eventDto3.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto4.getCalendarEvent().getTitle(), Date.from(eventDto4.getCalendarEvent().getStart()), Date.from(eventDto4.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto5.getCalendarEvent().getTitle(), Date.from(eventDto5.getCalendarEvent().getStart()), Date.from(eventDto5.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto6.getCalendarEvent().getTitle(), Date.from(eventDto6.getCalendarEvent().getStart()), Date.from(eventDto6.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto7.getCalendarEvent().getTitle(), Date.from(eventDto7.getCalendarEvent().getStart()), Date.from(eventDto7.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto8.getCalendarEvent().getTitle(), Date.from(eventDto8.getCalendarEvent().getStart()), Date.from(eventDto8.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto9.getCalendarEvent().getTitle(), Date.from(eventDto9.getCalendarEvent().getStart()), Date.from(eventDto9.getCalendarEvent().getEnd())));

            //Get dates of events for SUMMER

            startSummer = startSummer.split("–")[0].substring(40, startSummer.split("–")[0].length() - 4);
            EventDto eventDto10 = createEventFromTime(startSummer, startSummer, "Poczatek semestru letniego");

            endSummer = endSummer.split("–")[1].substring(1, endSummer.split("–")[1].length() - 9);
            EventDto eventDto11 = createEventFromTime(endSummer, endSummer, "Koniec semestru letniego");

            startLessonSummer = startLessonSummer.split("–")[0].substring(34, startLessonSummer.split("–")[0].length() - 4);
            EventDto eventDto12 = createEventFromTime(startLessonSummer, startLessonSummer, "Poczatek zajec semestru letniego");

            endLessonSummer = endLessonSummer.split("–")[1].substring(1, endLessonSummer.split("–")[1].length() - 7);
            EventDto eventDto13 = createEventFromTime(endLessonSummer, endLessonSummer, "Koniec zajec semestru letniego");

            springBreakStart = springBreakStart.split("–")[0].substring(37, springBreakStart.split("–")[0].length() - 4);
            springBreakEnd = springBreakEnd.split("–")[1].substring(1, springBreakEnd.split("–")[1].length() - 7);
            EventDto eventDto14 = createEventFromTime(springBreakStart, springBreakEnd, "Przerwa swiateczna swieta Wielkanocne");

            summerSessionPartOneStart = summerSessionPartOneStart.split("–")[0].substring(4, summerSessionPartOneStart.split("–")[0].length() - 4);
            summerSessionPartOneEnd = summerSessionPartOneEnd.split("–")[1].substring(1, summerSessionPartOneEnd.split("–")[1].length() - 7);
            EventDto eventDto15 = createEventFromTime(summerSessionPartOneStart, summerSessionPartOneEnd, "Sesja letnia");

            summerSessionPartTwoStart = summerSessionPartTwoStart.split("–")[0].substring(4, summerSessionPartTwoStart.split("–")[0].length() - 4);
            summerSessionPartTwoEnd = summerSessionPartTwoEnd.split("–")[1].substring(1, summerSessionPartTwoEnd.split("–")[1].length() - 7);
            EventDto eventDto16 = createEventFromTime(summerSessionPartTwoStart, summerSessionPartTwoEnd, "Sesja letnia");

            summerHolidayStart = summerHolidayStart.split("–")[0].substring(4, summerHolidayStart.split("–")[0].length() - 4);
            summerHolidayEnd = summerHolidayEnd.split("–")[1].substring(1, summerHolidayEnd.split("–")[1].length() - 7);
            EventDto eventDto17 = createEventFromTime(summerHolidayStart, summerHolidayEnd, "Wakacje letnie");

//            System.out.println(eventDto10);
//            System.out.println(eventDto11);
//            System.out.println(eventDto12);
//            System.out.println(eventDto13);
//            System.out.println(eventDto14);
//            System.out.println(eventDto15);
//            System.out.println(eventDto16);
//            System.out.println(eventDto17);

            eventRepository.save(new Event(Integer.valueOf(userid), eventDto10.getCalendarEvent().getTitle(), Date.from(eventDto10.getCalendarEvent().getStart()), Date.from(eventDto10.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto11.getCalendarEvent().getTitle(), Date.from(eventDto11.getCalendarEvent().getStart()), Date.from(eventDto11.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto12.getCalendarEvent().getTitle(), Date.from(eventDto12.getCalendarEvent().getStart()), Date.from(eventDto12.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto13.getCalendarEvent().getTitle(), Date.from(eventDto13.getCalendarEvent().getStart()), Date.from(eventDto13.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto14.getCalendarEvent().getTitle(), Date.from(eventDto14.getCalendarEvent().getStart()), Date.from(eventDto14.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto15.getCalendarEvent().getTitle(), Date.from(eventDto15.getCalendarEvent().getStart()), Date.from(eventDto15.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto16.getCalendarEvent().getTitle(), Date.from(eventDto16.getCalendarEvent().getStart()), Date.from(eventDto16.getCalendarEvent().getEnd())));
            eventRepository.save(new Event(Integer.valueOf(userid), eventDto17.getCalendarEvent().getTitle(), Date.from(eventDto17.getCalendarEvent().getStart()), Date.from(eventDto17.getCalendarEvent().getEnd())));


        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        System.out.println(eventDto1);
//        System.out.println(eventDto2);
//        System.out.println(eventDto3);
//        System.out.println(eventDto4);
//        System.out.println(eventDto5);
//        System.out.println(eventDto6);
//        System.out.println(eventDto7);
//        System.out.println(eventDto8);
//        System.out.println(eventDto9);

        List<EventDto> userEvents = new ArrayList<>();
        for(EventDto e : getAllEvents()) {
            if(e.getUserID().toString().equals(userid)) {
                userEvents.add(e);
            }
        }
        return userEvents;
    }

    @CrossOrigin
    @DeleteMapping("/{userid}/deleteTermEvents")
    public List<EventDto> deleteEventsFromPage(@PathVariable String userid) {

        List<Event> events = eventRepository.findAll();
        ArrayList<EventDto> mapedEvents = new ArrayList<>();

        for (Event event : events) {
            if(event.getStart() != null)
                mapedEvents.add(new EventDto(new CalendarEventDto(event.getId(), event.getTitle(), Instant.ofEpochMilli(event.getStart().getTime()), Instant.ofEpochMilli(event.getEnd().getTime())), event.getUserID()));
        }

        for( EventDto event : mapedEvents) {

            if(event.getCalendarEvent().getTitle().equals("Poczatek semestru zimowego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Koniec semestru zimowego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Poczatek zajec semestru zimowego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Koniec zajec semestru zimowego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Przerwa swiateczna swieta Bozego Narodzenia") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Sesja zimowa") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Dodatkowy dzien wolny od zajec") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Wakacje zimowe") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Poczatek semestru letniego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Koniec semestru letniego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Poczatek zajec semestru letniego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Koniec zajec semestru letniego") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Przerwa swiateczna swieta Wielkanocne") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Sesja letnia") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
            else if(event.getCalendarEvent().getTitle().equals("Wakacje letnie") && event.getUserID().equals(Integer.valueOf(userid))) {
                eventRepository.deleteById(event.getCalendarEvent().getId());
            }
        }

        List<EventDto> userEvents = new ArrayList<>();
        for(EventDto e : getAllEvents()) {
            if(e.getUserID().toString().equals(Integer.valueOf(userid))) {
                userEvents.add(e);
            }
        }
        //System.out.println(userEvents);

        return userEvents;
    }



    public EventDto createEventFromTime(String start, String end, String name) {
        CalendarEventDto event = new CalendarEventDto();
        event.setTitle(name);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String timestamp = start;
        TemporalAccessor temporalAccessor = formatter.parse(timestamp);
        LocalDate localDateTime = LocalDate.from(temporalAccessor);
        Instant result = localDateTime.atStartOfDay(ZoneId.systemDefault()).toInstant();

        String timestamp1 = end;
        TemporalAccessor temporalAccessor1 = formatter.parse(timestamp1);
        LocalDate  localDateTime1 = LocalDate.from(temporalAccessor1);
        Instant result1 = localDateTime1.atStartOfDay(ZoneId.systemDefault()).toInstant();


        event.setStart(result);
        event.setEnd(result1);
        EventDto eventDto = new EventDto();
        eventDto.setCalendarEvent(event);

        return eventDto;
    }
}
