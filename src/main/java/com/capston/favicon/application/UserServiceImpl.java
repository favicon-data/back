package com.capston.favicon.application;

import com.capston.favicon.application.repository.UserService;
import com.capston.favicon.domain.domain.User;
import com.capston.favicon.infrastructure.MemoryUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private MemoryUserRepository userRepository;
    public UserServiceImpl(MemoryUserRepository memoryUserRepository) {
        this.userRepository = memoryUserRepository;
    }


    @Override
    public void join(User user) {
        if (userRepository.findByUsername(user.getUsername())!=null) {
            throw new IllegalArgumentException("아이디가 이미 존재합니다. 재확인해주세요.");
        }
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        return user;
    }

}
