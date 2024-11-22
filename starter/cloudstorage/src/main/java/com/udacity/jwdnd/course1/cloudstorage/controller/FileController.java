package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;

@Controller
@RequestMapping
public class FileController {

    @Autowired
    HomeController homeController;

    @Autowired
    FileService fileService;

    @PostMapping(params = "fileUpload")
    public String newFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model) {
        try {
            Integer userId = homeController.getUserId(authentication);
            File[] files = fileService.getFiles(userId);
            for (File file : files) {
                if (file.getFileName().equals(fileUpload.getOriginalFilename())) {
                    model.addAttribute("toolargemsg", "This is a duplicate file.");
                    return homeController.getHomePage(authentication, model);
                }
            }
            fileService.addFile(fileUpload, userId);

        } catch (Exception ignored) {

        }
        return homeController.getHomePage(authentication, model);
    }
}
