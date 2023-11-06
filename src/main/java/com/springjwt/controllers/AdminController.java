package com.springjwt.controllers;

import com.springjwt.models.User;
import com.springjwt.payload.request.SignupRequest;
import com.springjwt.payload.request.UpdateUser;
import com.springjwt.payload.response.MessageResponse;
import com.springjwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
@NoArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody ResponseEntity<?> getAllUsers(){
        return ResponseEntity.ok(userRepository.findAll());
    }

    @DeleteMapping(path = "/user")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public @ResponseBody ResponseEntity<?> deleteById(@RequestParam UUID id){
        userRepository.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Deletion Successful"));
    }

    @PutMapping(path = "/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public @ResponseBody ResponseEntity<?> updateUser(@RequestBody UpdateUser updateUser){
        User user=userRepository.findById(updateUser.getUuid()).get();
        if(updateUser.getUsername()!=null){
            user.setUsername(updateUser.getUsername());
        }
        if(updateUser.getEmail()!=null){
            user.setEmail(updateUser.getEmail());
        }
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Update Successful"));
    }

}
