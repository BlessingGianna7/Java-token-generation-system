package com.rca.myspringsecurity.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.rca.myspringsecurity.entity.Student;
import com.rca.myspringsecurity.entity.UserData;
import com.rca.myspringsecurity.service.JwtService;
import com.rca.myspringsecurity.service.StudentService;
import com.rca.myspringsecurity.service.UserDataService;
import jakarta.servlet.http.HttpServletRequest;
@RestController
@RequestMapping("/academics")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserDataService userServices;
    @Autowired
    private JwtService jwtService;
    @PostMapping("/registration")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void addStudent(@RequestBody Student student,HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        UserData info=userServices.loadCurrentUser(username);
        student.setCreated(info);
        studentService.addStudent(student);
    }
    @GetMapping("/info")
    public String info() {
        return "Amazing day";
    }
}

