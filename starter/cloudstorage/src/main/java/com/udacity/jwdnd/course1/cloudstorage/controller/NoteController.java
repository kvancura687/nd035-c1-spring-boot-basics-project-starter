package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;

@Controller
@RequestMapping("note")
public class NoteController {

    @Autowired
    HomeController homeController;
    
    @Autowired
    NoteService noteService;

    @PostMapping("add-note")
    public String newNote(
            Authentication authentication, @ModelAttribute Note note, Model model) {
        Integer userId = homeController.getUserId(authentication);
        note.setUserId(userId);
        if (note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note);
        }

        return homeController.getHomePage(authentication, model);
    }

    @GetMapping(value = "/get-note/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @GetMapping(value = "/delete-note/{noteId}")
    public String deleteNote(
            Authentication authentication, @PathVariable Integer noteId, Model model) {
        noteService.deleteNote(noteId);

        return homeController.getHomePage(authentication, model);
    }
}