package com.serverutveckling.message.db;


import com.serverutveckling.message.objects.Message;
import com.serverutveckling.message.objects.UserSentMessage;
import com.serverutveckling.message.objects.Users;
import com.serverutveckling.message.repositories.MessageRepository;
import com.serverutveckling.message.repositories.userRepository;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements DbInterface to use a MySQL database
 */
@Component
public class DbManager implements DbInterface {

    @Autowired
    private userRepository userRepository;
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public DbManager(){

    }

    /**
     * Gets the sender's message
     * @param uuid
     * @param sender
     * @return
     */
    @Override
    public Iterable<Message> getMessages(String uuid, String sender) {
        System.out.println("uuid: " + uuid + " sender: " + sender);
        ArrayList<Message> recievedMessages = (ArrayList<Message>) messageRepository.findByReciever(uuid);

        ArrayList<Message> messages = new ArrayList<>();

        ArrayList<Message> sentMessages= (ArrayList<Message>) messageRepository.findBySender(uuid);
        recievedMessages.addAll(sentMessages);

        for (Message mes : recievedMessages) {
            if(((mes.getUser_UUID().equals(sender)) || (mes.getRecipient_UUID().equals(sender))))
            {
                messages.add(mes);
            }
        }

        Optional<Users> currentUser;
        Optional<Users> currentSender;

        for (int i = 0; i < messages.size(); i++){
            currentUser = userRepository.findByUUID(messages.get(i).getUser_UUID());
            messages.get(i).setUser_UUID(currentUser.get().getUsername());
            currentSender = userRepository.findByUUID(messages.get(i).getRecipient_UUID());
            messages.get(i).setRecipient_UUID(currentSender.get().getUsername());
        }


        return messages;
    }

    /**
     * Gets all usernames except callers
     * @param uuid
     * @return
     */
    @Override
    public ArrayList<Users> getUsernames(String uuid) {
        Iterable<Users> userList = userRepository.findAll();

        ArrayList<Users> usernameList = new ArrayList<>();
        usernameList.addAll((ArrayList) userList);

        for(int i = 0; i < usernameList.size(); i++){
            if(usernameList.get(i).getUUID().equals(uuid)){
                usernameList.remove(i);
            }else{
                usernameList.get(i).setPassword("");
            }
        }
        return usernameList;
    }

    /**
     * Gets a list of users who's sent a message
     * @param uuid
     * @return
     */
    @Override
    public ArrayList<UserSentMessage> getSenders(String uuid) {
        ArrayList<Users> userList = (ArrayList<Users>) userRepository.findAll();
        ArrayList<UserSentMessage> senders = new ArrayList<>();
        ArrayList<Message> recievedmessages = messageRepository.findByReciever(uuid);

        ArrayList<String> uuids = new ArrayList<>();
        for (int i = 0 ; i < recievedmessages.size(); i++){
            uuids.add(recievedmessages.get(i).getUser_UUID());
        }

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(uuids);
        System.out.println(hashSet);
        List<String> removeDuplicates = uuids.stream().distinct().collect(Collectors.toList());
        System.out.println(removeDuplicates);


        for (int i = 0; i < removeDuplicates.size(); i++){
            for (int j = 0; j < userList.size(); j++){
                if(userList.get(j).getUUID().equals(removeDuplicates.get(i))){
                    UserSentMessage temp = new UserSentMessage();
                    temp.setUsername(userList.get(j).getUsername());
                    temp.setUUID(userList.get(j).getUUID());
                    senders.add(temp);
                }
            }
        }

       return senders;
    }

    /**
     * New message entry into the database
     * @param message
     * @param uuid
     * @param reciever
     * @return
     * @throws JSONException
     */
    @Override
    public String updateMessage(String message, String uuid, String reciever) throws JSONException {
        System.out.println(message+ " uuid: " + uuid + " Recipient: " + reciever);


        String[] temp = message.split("");
        String temp2 = "";
        for(int i = 0; i < temp.length; i++) {
             temp2 += temp[i];
        }

        Message mes = new Message();
        mes.setRecipient_UUID(reciever);
        mes.setUser_UUID(uuid);
        mes.setBody(temp2);
        mes.set_date(LocalDate.now());
        messageRepository.save(mes);
        return message;
    }

    /**
     * Gets the encrypted password
     * @return
     */
    @Bean
    private PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}
