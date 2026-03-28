package com.hrms.admin.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/test")
    public String test() {
        return "Admin Access Working";
    }
}