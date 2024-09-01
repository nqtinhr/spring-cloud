package com.syshero.accountservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.syshero.accountservice.client.NotificationService;
import com.syshero.accountservice.client.StatisticService;
import com.syshero.accountservice.model.AccountDTO;
import com.syshero.accountservice.model.MessageDTO;
import com.syshero.accountservice.model.StatisticDTO;
import com.syshero.accountservice.service.AccountService;

import jakarta.annotation.security.PermitAll;
import java.security.Principal;
import java.util.*;

@RestController
public class AccountController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private StatisticService statisticService;

    @Autowired
    NotificationService notificationService;

    // add new
    @PostMapping("/register")
    @PermitAll
    public AccountDTO register(@RequestBody AccountDTO accountDTO, @RequestHeader("Authorization") String bearerToken) {
        accountDTO.setRoles(new HashSet<String>(Arrays.asList("ROLE_USER")));
        accountService.add(accountDTO);

        statisticService.add(new StatisticDTO("Account " + accountDTO.getUsername() + " is created", new Date()));

        // send notificaiton
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFrom("jmaster.io@gmail.com");
        messageDTO.setTo(accountDTO.getEmail());// username is email
        messageDTO.setToName(accountDTO.getName());
        messageDTO.setSubject("Welcome to JMaster.io");
        messageDTO.setContent("JMaster is online learning platform.");

        notificationService.sendNotification(messageDTO);

        return accountDTO;
    }

    @PreAuthorize("hasAuthority('SCOPE_read') && hasRole('ADMIN')")
    @PostMapping("/account")
    public AccountDTO addAccount(@RequestBody AccountDTO accountDTO, @RequestHeader("Authorization") String bearerToken) {
        accountService.add(accountDTO);

        statisticService.add(new StatisticDTO("Account " + accountDTO.getUsername() + " is created", new Date()));

        // send notificaiton
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setFrom("itnetwork102@gmail.com");
        messageDTO.setTo(accountDTO.getEmail());// username is email
        messageDTO.setToName(accountDTO.getName());
        messageDTO.setSubject("Welcome to Syshero");
        messageDTO.setContent("This is Syshero");

        notificationService.sendNotification(messageDTO);

        return accountDTO;
    }

    // get all
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/accounts")
    public List<AccountDTO> getAll() {
        logger.info("AccountService Controller: get all accounts");

        statisticService.add(new StatisticDTO("Get all accounts", new Date()));

        return accountService.getAll();
    }

    @PreAuthorize("hasAuthority('SCOPE_read') && isAuthenticated()")
    @PostAuthorize("returnObject.body.username == authentication.name || hasRole('ADMIN')")
    @GetMapping("/account/{id}")
    public ResponseEntity<AccountDTO> get(@PathVariable(name = "id") Long id) {
        return Optional.of(new ResponseEntity<AccountDTO>(accountService.getOne(id), HttpStatus.OK))
                .orElse(new ResponseEntity<AccountDTO>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAuthority('SCOPE_write') && hasRole('ADMIN')")
    @DeleteMapping("/account/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        statisticService.add(new StatisticDTO("Delete account id " + id, new Date()));

        accountService.delete(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_write') && hasRole('ADMIN')")
    @PutMapping("/account")
    public void update(@RequestBody AccountDTO accountDTO) {
        statisticService.add(new StatisticDTO("Update account: " + accountDTO.getUsername(), new Date()));

        accountService.update(accountDTO);
    }

//    @PreAuthorize("#oauth2.hasScope('read') && isAuthenticated()")
    @GetMapping("/me")
    public Principal me(Principal principal, @RequestHeader("Authorization") String bearerToken) {
        return principal;
    }
}