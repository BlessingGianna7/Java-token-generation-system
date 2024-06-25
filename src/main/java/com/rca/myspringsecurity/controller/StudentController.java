package com.rca.myspringsecurity.controller;
import com.rca.myspringsecurity.dto.CreateStudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void addStudent(@RequestBody CreateStudentDTO student, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        UserData info=userServices.loadCurrentUser(username);
        Student student1 = new Student();
        student1.setFirstName(student.getFirstName());
        student1.setLastName(student.getLastName());
        student1.setEmail(student.getEmail());
        student1.setCreated(info);
        studentService.addStudent(student1);
    }
    private void checkAuthority(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role))) {
            throw new AccessDeniedException("You do not have permission to perform this action");
        }
    }
    @GetMapping("/info")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String info() {
        try{
        return "Amazing day";

        }
        catch (Exception e){
            System.out.println("AN ERROR OCCURED");
            return "Error";
        }
    }
}

