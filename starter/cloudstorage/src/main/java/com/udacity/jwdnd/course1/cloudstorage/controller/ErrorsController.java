package com.udacity.jwdnd.course1.cloudstorage.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Controller
@ControllerAdvice
public class ErrorsController implements ErrorController {
    
    @Autowired
    private HomeController homeController;

    @Override
    public String getErrorPath() {
        return "error";
    }

    @RequestMapping("/error")
    public String getError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        model.addAttribute("errorTitle", "Error " + status);
        return "error";
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public String exceptionHandler(Authentication authentication, Model model) {
        model.addAttribute("toolargemsg", "File size too large");
        return homeController.getHomePage(authentication, model);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String exceptionHandler2(Authentication authentication, Model model) {
        model.addAttribute("toolargemsg", "File size too large");
        return homeController.getHomePage(authentication, model);
    }

}
