package com.razakor.demo.services;

import com.razakor.demo.documents.User;
import com.razakor.demo.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoUserService implements UserService {

    private final UserRepository userRepository;

    public MongoUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(ObjectId id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }
}
