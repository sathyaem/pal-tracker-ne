package io.pivotal.pal.tracker;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    List<TimeEntry> data = new ArrayList<TimeEntry>();
    private long sequence = 0;

    @Override
    public TimeEntry create(TimeEntry timeEntryToCreate) {


        if (data.add(timeEntryToCreate)) {
            long id = ++sequence;
            timeEntryToCreate.setId(id);
            return timeEntryToCreate;
        } else return null;
    }

    @Override
    public TimeEntry find(long id) {
        //data.forEach(return(x->x.getId()==id));
        return getEntry(id);
    }

    @Override
    public List<TimeEntry> list() {
        return data;
    }

    @Override
    public TimeEntry update(long id, TimeEntry entry) {
        TimeEntry expected = getEntry(id);
        entry.setId(expected.getId());
        if (expected != null) {
/*            data.remove(expected);
            data.add(entry);*/
            data.set(data.indexOf(expected), entry);
            return entry;

        } else {
            return null;
        }
    }

    @Override
    public void delete(long id) {
        TimeEntry entry = getEntry(id);
        data.remove(entry);
    }

    private TimeEntry getEntry(long id) {
        for (TimeEntry entry : data) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }
}
