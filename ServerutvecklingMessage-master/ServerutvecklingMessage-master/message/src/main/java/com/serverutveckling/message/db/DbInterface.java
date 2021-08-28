package com.serverutveckling.message.db;


import com.serverutveckling.message.objects.Message;
import com.serverutveckling.message.objects.UserSentMessage;
import com.serverutveckling.message.objects.Users;
import org.json.JSONException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Interface declaring methods used for handling communication with a database
 */
@Component
public interface DbInterface {

    public Iterable<Message> getMessages(String uuid, String sender);

    public ArrayList<Users> getUsernames(String uuid);

    public ArrayList<UserSentMessage> getSenders(String uuid);

    String updateMessage(String message, String uuid, String reciever) throws JSONException;

}
