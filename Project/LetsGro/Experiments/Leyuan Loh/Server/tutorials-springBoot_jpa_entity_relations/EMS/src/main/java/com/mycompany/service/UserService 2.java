package com.mycompany.service;

import com.mycompany.vo.LoginVO;
import com.mycompany.vo.Message;
import com.mycompany.model.User;
import com.mycompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public Message registerUser(User user){
        userRepository.save(user);
        return new Message("Register Successful", 0);
    }

    public User validateUser(LoginVO loginVO){
        return userRepository.findByEmailAndPassword(loginVO.getEmail(), loginVO.getPassword());
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
}
