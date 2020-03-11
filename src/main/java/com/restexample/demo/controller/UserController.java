package com.restexample.demo.controller;

import com.restexample.demo.component.UserModelAssembler;
import com.restexample.demo.entity.User;
import com.restexample.demo.exception.UserNotFoundException;
import com.restexample.demo.repo.UserRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
//@RequestMapping("/api/v1")
public class UserController {

    private final UserRepository userRepository;

    private final UserModelAssembler userModelAssembler;

    public UserController(UserRepository userRepository, UserModelAssembler userModelAssembler){
        this.userRepository = userRepository;
        this.userModelAssembler = userModelAssembler;
    }

    // Aggregate root

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> all() {
        List<EntityModel<User>> userEntityModelList = this.userRepository.findAll()
                .stream()
                .map(this.userModelAssembler::toModel)
                .collect(Collectors.toList());
        return new CollectionModel<>(userEntityModelList, linkTo(methodOn(UserController.class).all()).withSelfRel());

    }

    @PostMapping("/users")
    public ResponseEntity<?> newUser(@RequestBody User newUser) throws URISyntaxException{

        EntityModel<User> entityModel = this.userModelAssembler.toModel(this.userRepository.save(newUser));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // Single item

    @GetMapping("/users/{id}")
    public EntityModel<User> one(@PathVariable Long id) {

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return this.userModelAssembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<?> updateUser(@RequestBody User newUser, @PathVariable Long id) throws URISyntaxException{

        User updatedUser = this.userRepository.findById(id)
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setRole(newUser.getRole());
                    return this.userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setUserID(id);
                    return this.userRepository.save(newUser);
                });

        EntityModel<User> entityModel = this.userModelAssembler.toModel(updatedUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        this.userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
