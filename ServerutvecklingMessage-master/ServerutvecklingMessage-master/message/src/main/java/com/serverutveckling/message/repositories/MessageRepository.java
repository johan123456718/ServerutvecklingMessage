package com.serverutveckling.message.repositories;


import com.serverutveckling.message.objects.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * A repository for fetching message from a database
 */
public interface MessageRepository extends CrudRepository<Message, Integer> {
    Iterable<Message> findBySender(String UUID);

    ArrayList<Message> findByReciever(String uuid);

}
