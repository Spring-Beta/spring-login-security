package com.example.springloginsecurity.controller;

import com.example.springloginsecurity.entity.Role;
import com.example.springloginsecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/roles/")
@RequiredArgsConstructor
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody  Role role) {
        Role savedRole = roleRepository.save(role);
        return new ResponseEntity<>(savedRole, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> read(@PathVariable("id") Integer id){
        try {
            Role role = roleRepository.findById(id).orElseThrow();
            return new ResponseEntity<>(role, HttpStatus.OK);
        }catch (Exception e ){
            return new ResponseEntity<Object>(
                    "The rol with identificator " + id + " does not exist in our database", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable("id") Integer id,@RequestBody  Role role)  {
        try{
            Role updatedRole = roleRepository.findById(id).orElseThrow();
            updatedRole.setName(role.getName());
            roleRepository.save(updatedRole);
            return new ResponseEntity<>(updatedRole, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(
                    "The rol with id " + id + " does not exist in our database", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Integer id){
        try{
            Role deletedRole = roleRepository.findById(id).orElseThrow();
            roleRepository.delete(deletedRole);
            return new ResponseEntity<>("Role successfully deleted!", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(
                    "The rol with id " + id + " does not exist in our database", new HttpHeaders(), HttpStatus.NOT_FOUND);
        }

    }
    @GetMapping
    public List<Role> list() {
        return roleRepository.findAll();
    }

}
