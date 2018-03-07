package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.CheckoutDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.model.Book;
import com.project.glib.model.Booking;
import com.project.glib.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//import org.springframework.web.bind.annotation.RestController;

@Controller
//@RestController
public class BookingController {

    private final BookingService bookingService;
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation audioVideoDao;


    @Autowired
    public BookingController(BookingService bookingService, BookDaoImplementation bookDao, JournalDaoImplementation journalDao, AudioVideoDaoImplementation audioVideoDao) {
        this.bookingService = bookingService;
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.audioVideoDao = audioVideoDao;
    }

    //return list of books
    @ResponseBody
    @RequestMapping(value = "/booking/book", method = RequestMethod.GET)
    public List<Book> bookingBook() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookDao.getListCountNotZeroOrRenewed());
        modelAndView.setViewName("order");
        return bookDao.getListCountNotZeroOrRenewed();
    }

    @RequestMapping(value = "/booking/book", method = RequestMethod.POST)
    public String b(@RequestBody Booking bookingForm, Model model)  {
        try {
            bookingService.toBookDocument(bookingForm.getIdDoc(),bookingForm.getDocType(),bookingForm.getIdUser());
            return "successfully ordered";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
  //      return "order";

    }

    @RequestMapping(value = "/booking/journal", method = RequestMethod.GET)
    public ModelAndView bookingJournal() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allJournals", journalDao.getListCountNotZeroOrRenewed());
        modelAndView.setViewName("orderJ");
        return modelAndView;
    }

    @RequestMapping(value = "/booking/av", method = RequestMethod.GET)
    public ModelAndView bookingAV() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allAv", audioVideoDao.getListCountNotZeroOrRenewed());
        modelAndView.setViewName("orderAV");
        return modelAndView;
    }


}

