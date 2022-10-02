/**
 * Copyright(C) 2022 Phạm Hồng Đạt
 * 02/10/2022
 */
package com.hongdatchy.security;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 *
 * @author hongdatchy
 */
@RestController
@RequestMapping("api/v1")
public class Controller {

    @Data
    private static class Cat{
        String name;
        int age;
        public Cat(String name, int age){
            this.name = name;
            this.age = age;
        }
    }

    private List<Cat> catList = new ArrayList<>(Arrays.asList(
            new Cat("Tom", 1),
            new Cat("Garfield", 2)
    ));

    @GetMapping("/us/cat/{id}")
    public ResponseEntity<Object> test(@PathVariable int id){
        return ResponseEntity.ok(catList.get(id));
    }

    @GetMapping("/ad/cat")
    public ResponseEntity<Object> test2(){
        return ResponseEntity.ok(catList);
    }

    @DeleteMapping("/ad/cat/{id}")
    public ResponseEntity<Object> test3(@PathVariable int id){
        catList.remove(id);
        return ResponseEntity.ok(id);
    }
}
