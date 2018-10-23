package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

public class JdbcTimeEntryRepository implements TimeEntryRepository {

    //private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public JdbcTimeEntryRepository(DataSource ds)
    {
        //dataSource = ds;
        jdbcTemplate = new JdbcTemplate(ds);

    }


    @Override
    public TimeEntry create(TimeEntry t) {

        GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                c-> {
                    PreparedStatement s = c.prepareStatement("insert into time_entries(project_id, user_id, date, hours)" +
                        "VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                    s.setLong(1,t.getProjectId());
                    s.setLong(2,t.getUserId());
                    s.setDate(3, Date.valueOf(t.getDate()));
                    s.setInt(4,t.getHours());
                    return s;
                },generatedKeyHolder);

        return find(generatedKeyHolder.getKey().longValue());
    }



    @Override
    public TimeEntry find(long id) {
        return jdbcTemplate.query(
                "SELECT id, project_id, user_id, date, hours FROM time_entries WHERE id = ?",
                new Object[]{id},
                extractor);
    }

    public List<TimeEntry> list() {
        return jdbcTemplate.query("SELECT id, project_id, user_id, date, hours FROM time_entries", mapper);
    }



    private final RowMapper<TimeEntry> mapper = (rs, rowNum) -> new TimeEntry(
            rs.getLong("id"),
            rs.getLong("project_id"),
            rs.getLong("user_id"),
            rs.getDate("date").toLocalDate(),
            rs.getInt("hours")
    );

    private final ResultSetExtractor<TimeEntry> extractor =
            (rs) -> rs.next() ? mapper.mapRow(rs, 1) : null;


    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        jdbcTemplate.update("UPDATE time_entries " +
                        "SET project_id = ?, user_id = ?, date = ?,  hours = ? " +
                        "WHERE id = ?",
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                Date.valueOf(timeEntry.getDate()),
                timeEntry.getHours(),
                id);

        return find(id);
    }

    @Override
    public void delete(long id1) {
        jdbcTemplate.update("DELETE FROM time_entries WHERE id = ?", id1);
    }
}
