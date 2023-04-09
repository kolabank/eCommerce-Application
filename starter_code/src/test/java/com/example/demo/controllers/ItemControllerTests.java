package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTests {

    private ItemRepository itemRepository = mock(ItemRepository.class);
    ItemController itemController;
    @Before
    public void wireUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

    }

    @Test
    public void getItemsTest(){

        Item testItem = sampleItem();
        when(itemRepository.findAll()).thenReturn(new ArrayList<Item>(Arrays.asList(testItem)));
        ResponseEntity<List<Item>> items = itemController.getItems();
        Assert.assertNotNull(items);
        Assert.assertEquals(HttpStatus.OK, items.getStatusCode());
        Assert.assertEquals(testItem.getName(), items.getBody().get(0).getName());

    }


    @Test
    public void getItemsByIdTest(){
        Item testItem = sampleItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(testItem));
        ResponseEntity<Item> foundItem = itemController.getItemById(1L);
        Assert.assertNotNull(foundItem);
        Assert.assertEquals(HttpStatus.OK, foundItem.getStatusCode());
        Assert.assertEquals(testItem.getName(), foundItem.getBody().getName());

    }


    //Helper class to create a new item
    public Item sampleItem(){
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("firstItem");
        item1.setDescription("This is the first item");
        item1.setPrice(BigDecimal.valueOf(29.99));
        return item1;
    }


}
