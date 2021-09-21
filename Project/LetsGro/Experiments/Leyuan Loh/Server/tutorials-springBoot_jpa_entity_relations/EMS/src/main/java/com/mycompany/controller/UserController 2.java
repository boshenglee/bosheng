package com.mycompany.controller;

import com.mycompany.vo.LoginVO;
import com.mycompany.vo.Message;
import com.mycompany.model.User;
import com.mycompany.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping(value = "/", produces = "application/json")
    public @ResponseBody Message registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping(value = "/", produces = "application/json")
    public @ResponseBody User validateUser(@RequestBody LoginVO loginVO){
        return userService.validateUser(loginVO);
    }

    @GetMapping(value = "/", produces = "application/json")
    public @ResponseBody List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
