package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTests {
    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void wireUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

    }

    @Test
    public void create_user_happy_path() {
        when(encoder.encode("user1234")).thenReturn("thisIsHashedPassword");
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("user1");
        userRequest.setPassword("user1234");
        userRequest.setConfirmPassword("user1234");
        ResponseEntity<User> response = userController.createUser(userRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0,user.getId());
        Assert.assertEquals("user1", user.getUsername());
        Assert.assertEquals("thisIsHashedPassword", user.getPassword());

    }

    @Test
    public void testFindUserById(){
        User user = new User();
        user.setUsername("testName");
        user.setId(1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

      ResponseEntity<User> foundUser =  userController.findById(1L);
      Assert.assertNotNull(foundUser);
      Assert.assertEquals(user.getId(), foundUser.getBody().getId());
      Assert.assertEquals(user.getUsername(), foundUser.getBody().getUsername());

    }

    @Test
    public void testFindByUsername(){
        User user = new User();
        user.setUsername("testName");
        when(userRepository.findByUsername("testName")).thenReturn(user);

        ResponseEntity<User> foundUser = userController.findByUserName("testName");
        Assert.assertNotNull(foundUser);
        Assert.assertEquals(user.getUsername(), foundUser.getBody().getUsername());


    }


    public ResponseEntity<User> mockUserRequest() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("testName");
        userRequest.setPassword("TestPassword");
        userRequest.setConfirmPassword("TestPassword");
        ResponseEntity<User> response = userController.createUser(userRequest);
        return response;
    }



}
