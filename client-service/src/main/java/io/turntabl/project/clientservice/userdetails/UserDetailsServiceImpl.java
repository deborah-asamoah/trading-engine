package io.turntabl.project.clientservice.userdetails;

import io.turntabl.project.clientservice.mappers.ClientRequestBodyMapper;
import io.turntabl.project.persistence.entities.Client;
import io.turntabl.project.persistence.repositories.ClientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(username).orElseThrow(()
                -> new UsernameNotFoundException
                ("user Not Found with email: " + username));
        return  UserDetailsImpl.build(client);
    }
}