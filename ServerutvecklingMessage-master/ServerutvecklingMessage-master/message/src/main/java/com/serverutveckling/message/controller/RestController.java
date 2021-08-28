package com.serverutveckling.message.controller;


import com.serverutveckling.message.db.DbInterface;
import com.serverutveckling.message.db.DbManager;
import com.serverutveckling.message.objects.Message;
import com.serverutveckling.message.objects.UserSentMessage;
import com.serverutveckling.message.objects.Users;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;


/**
 * Controller handling REST calls
 */
@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path="/demo", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST}, produces = { "application/json; charset=utf-8" })
public class RestController {

    @Autowired
    DbInterface DbManager = new DbManager();

    @GetMapping(path="/messages")
    public @ResponseBody Iterable<Message> getAllMessages (@RequestParam String uuid, @RequestParam String sender) {
        return DbManager.getMessages(uuid, sender);
    }

    @GetMapping(path="/usernames")
    public @ResponseBody
    ArrayList<Users> getUsernames(@RequestParam String uuid){
        return DbManager.getUsernames(uuid);
    }

    @GetMapping(path="/senders")
    public @ResponseBody
    ArrayList<UserSentMessage> getSenders(@RequestParam String uuid){
        return DbManager.getSenders(uuid);
    }

    @PostMapping(path="/addMessage")
    public @ResponseBody String addMessage(@RequestParam String message, @RequestParam String uuid, @RequestParam String reciever) throws JSONException {
        return DbManager.updateMessage(message, uuid, reciever);
    }

}
