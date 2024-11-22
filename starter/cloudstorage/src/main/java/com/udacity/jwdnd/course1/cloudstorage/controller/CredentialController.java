package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("credential")
public class CredentialController {

    @Autowired
    HomeController homeController;

    @Autowired
    CredentialService credentialService;
    
    @Autowired
    EncryptionService encryptionService;
    
    @Autowired
    UserService userService;

    @PostMapping("add-credential")
    public String newCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        System.out.println("DEBUG - newCredential");

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        credential.setKey(Base64.getEncoder().encodeToString(key));
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), credential.getKey()));
        credential.setUserid(homeController.getUserId(authentication));
        
        if (credential.getCredentialid() == null) {
            credentialService.addCredential(credential);
            System.out.println("DEBUG - addCredential");
        } else {
            credentialService.updateCredential(credential);
            System.out.println("DEBUG - updateCredential");
        }

        return homeController.getHomePage(authentication, model);
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);

        return homeController.getHomePage(authentication, model);

    }
}
