package com.razakor.demo.services;

import com.razakor.demo.documents.User;
import org.bson.types.ObjectId;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(ObjectId id);

    void save(User user);

    void saveAll(List<User> users);
}
