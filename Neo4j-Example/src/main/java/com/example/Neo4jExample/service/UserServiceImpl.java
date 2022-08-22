package com.example.Neo4jExample.service;

import com.example.Neo4jExample.model.UserNode;
import com.example.Neo4jExample.model.UserRole;
import com.example.Neo4jExample.repository.UserNodeRepository;
import com.example.Neo4jExample.repository.UserRoleRepository;
import com.example.Neo4jExample.service.util.UserServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServiceInterface, UserDetailsService {
    private final UserNodeRepository userNodeRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserNode user = this.userNodeRepository.findByUsername(username);
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach( r -> authorities.add(new SimpleGrantedAuthority(r.getName())));
        return new User(user.getUsername(),user.getPassword(),authorities);

    }

    @Override
    public UserNode saveUser(UserNode user) {
        this.userNodeRepository.save(user);
        return user;
    }

    @Override
    public UserRole saveRole(UserRole userRole) {
        this.userRoleRepository.save(userRole);
        return userRole;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        UserNode user = this.userNodeRepository.findByUsername(username);
        UserRole role = this.userRoleRepository.findByName(roleName);
        if(!user.getRoles().contains(role)) user.getRoles().add(role);
        this.userNodeRepository.save(user);
    }

    @Override
    public List<UserNode> getUsers() {
        return this.userNodeRepository.findAll();
    }
}
