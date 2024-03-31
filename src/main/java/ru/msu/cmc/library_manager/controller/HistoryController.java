package ru.msu.cmc.library_manager.controller;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class HistoryController {
    @Autowired
    private ReaderDAO readerDAO;
    @Autowired
    private IssueDAO issueDAO;

    @NonNull
    private IssueDAO.Filter getIssuedFilter(String dateBegin, String dateEnd, Integer readerId) { // filter for issues
        IssueDAO.Filter filter = new IssueDAO.Filter();
        try { // ParseException
            if (dateBegin != null || dateEnd != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if (dateBegin != null)
                    filter.setIssueDateBegin(new Date(format.parse(dateBegin).getTime()));
                if (dateEnd != null)
                    filter.setIssueDateEnd(new Date(format.parse(dateEnd).getTime()));
            }
        } catch (Exception ignored) {} // just don't add date if it's invalid

        if (readerId != null) {
            Reader reader = readerDAO.getById(readerId);
            filter.addReader(reader);
        }
        return filter;
    }

    @NonNull
    private IssueDAO.Filter getReturnedFilter(String dateBegin, String dateEnd, Integer readerId) { // filter for returns
        IssueDAO.Filter filter = new IssueDAO.Filter();
        try { // ParseException
            if (dateBegin != null || dateEnd != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if (dateBegin != null)
                    filter.setReturnDateBegin(new Date(format.parse(dateBegin).getTime()));
                if (dateEnd != null)
                    filter.setReturnDateEnd(new Date(format.parse(dateEnd).getTime()));
            }
        } catch (Exception ignored) {} // just don't add date if it's invalid

        if (readerId != null) {
            Reader reader = readerDAO.getById(readerId);
            filter.addReader(reader);
        }
        return filter;
    }

    @GetMapping(value = "/history")
    public String getHistory(@RequestParam(required = false) String dateBegin,
                             @RequestParam(required = false) String dateEnd,
                             @RequestParam(required = false) Integer readerId,
                             @NonNull Model model) {
        IssueDAO.Filter filter = getIssuedFilter(dateBegin, dateEnd, readerId);
        List<Issue> issuesList = issueDAO.getIssuesByFilter(filter);
        filter = getReturnedFilter(dateBegin, dateEnd, readerId);
        List<Issue> returnsList = issueDAO.getIssuesByFilter(filter);
        List<Event> eventsList = Event.eventsFromIssuesSeparated(issuesList, returnsList);

        System.out.println(issuesList);
        System.out.println(returnsList);

        // from latest to earliest
        eventsList.sort(Comparator.reverseOrder());
        model.addAttribute("eventsList", eventsList);

        model.addAttribute("dateBegin", dateBegin);
        model.addAttribute("dateEnd", dateEnd);
        model.addAttribute("readerId", readerId);
        return "history";
    }
}
