package com.klu.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.klu.model.Student;
import com.klu.repository.StudentRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtFilter extends OncePerRequestFilter 
{

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain filterChain)
    throws ServletException, IOException 
    {

        String authHeader = request.getHeader("Authorization");

        if(authHeader != null && authHeader.startsWith("Bearer ")) 
        {

            String token = authHeader.substring(7);

            try 
            {

                String email = jwtUtil.extractUsername(token);

                Student student = studentRepository.findByEmail(email);

                if(student != null) 
                {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, null, List.of(new SimpleGrantedAuthority(student.getRole())));

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            }
            catch(Exception e) 
            {
                System.out.println("Invalid token");
            }

        }

        filterChain.doFilter(request,response);

    }

}
