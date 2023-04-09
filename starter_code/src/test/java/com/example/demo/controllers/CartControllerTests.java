package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTests {

    CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);



    @Before
    public void wireUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);

    }

    @Test
    public void testAddToCart(){
        ModifyCartRequest cartRequest = new ModifyCartRequest();

        //Assign user and cart to go with item to be added
        User user = new User();
        Cart cart = new Cart();
        user.setUsername("TestUser");
        user.setCart(cart);
        cart.setUser(user);

        //Define item to be added
        Item item =  new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(29.99));

        cartRequest.setUsername("TestUser");
        cartRequest.setQuantity(2);
        cartRequest.setItemId(1);
        //Mock behaviour of userRepository and itemRepository actions
        when(userRepository.findByUsername("TestUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> returnedCart = cartController.addTocart(cartRequest);

        Assert.assertNotNull(returnedCart);

        Assert.assertEquals(200,returnedCart.getStatusCodeValue() );
        Assert.assertEquals(user.getUsername(), returnedCart.getBody().getUser().getUsername());

    }


}
