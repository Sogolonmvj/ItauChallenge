package com.vieira.sogolon.ItauChallenge.controller;

import com.vieira.sogolon.ItauChallenge.entities.RegistrationRequest;
import com.vieira.sogolon.ItauChallenge.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path="/confirmation")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

}
