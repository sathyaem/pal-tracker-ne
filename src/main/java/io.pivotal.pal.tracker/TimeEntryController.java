package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {


    private TimeEntryRepository repo;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {

        this.repo = timeEntryRepository;
    }


    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntryToCreate) {
        try {

            TimeEntry val = repo.create(timeEntryToCreate);

            if (val != null) {
                return new ResponseEntity<TimeEntry>(val, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        try {

            TimeEntry val = repo.find(id);

            if (val != null) {
                return new ResponseEntity<TimeEntry>(val, HttpStatus.OK);
            } else {
                return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        try {

            List<TimeEntry> all = repo.list();

            if (all != null) {
                return new ResponseEntity<List<TimeEntry>>(all, HttpStatus.OK);
            } else {
                return new ResponseEntity<List<TimeEntry>>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<List<TimeEntry>>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TimeEntry entry) {
        try {

            TimeEntry updated = repo.update(id, entry);

            if (updated != null) {
                return new ResponseEntity<TimeEntry>(updated, HttpStatus.OK);
            } else {
                return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            return new ResponseEntity<TimeEntry>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        repo.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
