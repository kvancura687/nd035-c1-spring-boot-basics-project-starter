package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, Model model) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", this.credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @PostMapping("add-credential")
    public String newCredential(Authentication authentication, @ModelAttribute Credential credential, Model model) {
        String userName = authentication.getName();
        String newUrl = credential.getUrl();
        String password = credential.getPassword();

        System.out.println("Credential ID " + credential.getCredentialid());

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if (credential.getCredentialid() == null) {
            credentialService.addCredential(newUrl, userName, credential.getUserName(), encodedKey, encryptedPassword);
            System.out.println("Add credential");
        } else {
            Credential existingCredential = getCredential(credential.getCredentialid());
            credentialService.updateCredential(existingCredential.getCredentialid(), credential.getUserName(), newUrl, encodedKey, encryptedPassword);
            System.out.println("Update credential");
        }
        
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping(value = "/delete-credential/{credentialId}")
    public String deleteCredential(
            Authentication authentication, @PathVariable Integer credentialId, Model model) {
        credentialService.deleteCredential(credentialId);
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", credentialService.getCredentialListings(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";

    }
}
