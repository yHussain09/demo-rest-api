package com.restexample.demo.component;

import com.restexample.demo.controller.UserController;
import com.restexample.demo.entity.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {

    @Override
    public EntityModel<User> toModel(User user) {
        return new EntityModel<>(user,
                linkTo(methodOn(UserController.class).one(user.getUserID())).withSelfRel(),
                linkTo(methodOn(UserController.class).all()).withRel("users"));
    }

    @Override
    public CollectionModel<EntityModel<User>> toCollectionModel(Iterable<? extends User> entities) {
        return null;
    }
}
