package ru.msu.cmc.library_manager.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.msu.cmc.library_manager.DAO.IssueDAO;
import ru.msu.cmc.library_manager.DAO.ReaderDAO;
import ru.msu.cmc.library_manager.lib.Event;
import ru.msu.cmc.library_manager.model.Issue;
import ru.msu.cmc.library_manager.model.Reader;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

@Controller
public class ReaderController {
    @Autowired
    private ReaderDAO readerDAO;
    @Autowired
    private IssueDAO issueDAO;

    @GetMapping(value = "/readers/{id}")
    public String getReader(@PathVariable int id, @NonNull Model model) {
        Reader reader = readerDAO.getById(id);
        model.addAttribute("reader", reader);

        IssueDAO.Filter issueFilter = new IssueDAO.Filter();
        issueFilter.addReader(reader);
        List<Event> eventsList = Event.eventsFromIssues(issueDAO.getIssuesByFilter(issueFilter));
        eventsList.sort(Comparator.reverseOrder());
        model.addAttribute("eventsList", eventsList);
        return "reader";
    }

    @PostMapping(value = "/readers/{id}/edit")
    public String postReaderEdit(@PathVariable int id,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false) String address,
                                 @RequestParam(required = false) Long phone) {
        Reader reader = readerDAO.getById(id);
        if (name != null)
            if (!name.isEmpty())
                reader.setName(name);
        if (address != null)
            if (!address.isEmpty())
                reader.setAddress(address);
        if (phone != null)
            reader.setPhone(phone);
        readerDAO.update(reader);

        return "redirect:../../readers/{id}";
    }

    @PostMapping(value = "/readers/{id}/extend")
    public String postReaderExtend(@PathVariable int id,
                                   @RequestParam int issueId,
                                   @RequestParam(required = false) String date) {

        Date dateSql = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            dateSql = new Date(format.parse(date).getTime());
        } catch (Exception ignored) {}

        Issue issue = issueDAO.getById(issueId);
        issue.setDeadline(dateSql);
        issueDAO.update(issue);

        return "redirect:../../readers/{id}";
    }
}
