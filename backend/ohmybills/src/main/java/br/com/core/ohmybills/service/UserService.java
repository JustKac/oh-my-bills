package br.com.core.ohmybills.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.core.ohmybills.model.User;
import br.com.core.ohmybills.repository.UserRepository;

@Service
public class UserService extends GenericServiceImpl<User, UUID, UserRepository> {

    public UserService(UserRepository repository) {
        super(repository);
    }

}
