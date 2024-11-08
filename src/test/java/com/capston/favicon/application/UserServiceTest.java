package com.capston.favicon.application;

import com.capston.favicon.AppConfig;
import com.capston.favicon.application.repository.UserService;
import com.capston.favicon.domain.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.capston.favicon.infrastructure.MemoryUserRepository;

public class UserServiceTest {

    UserService userService;
    MemoryUserRepository userRepository;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        userService = appConfig.userService();
        userRepository = appConfig.memoryUserRepository();
        userRepository.clearStore();
    }

    @Test
    void join(){
         User user = new User("test", "1234");
         this.userService.join(user);
         User findUser = this.userService.findByUsername("test");
         Assertions.assertThat(user).isEqualTo(findUser);
    }

    @Test
    void joinDuplicateUsername() {
        User user1 = new User("duplicateUser", "1234");
        User user2 = new User("duplicateUser", "5678");

        userService.join(user1);

        // 중복 이름 가입 시 예외 발생
        Assertions.assertThatThrownBy(() -> userService.join(user2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디가 이미 존재합니다. 재확인해주세요.");
    }

    @Test
    void 로그인() {
        User user = new User("testUser", "1234");
        userService.join(user);

        User loginUser = userService.login("testUser", "1234");

        Assertions.assertThat(loginUser).isNotNull();
        Assertions.assertThat(loginUser.getUsername()).isEqualTo("testUser");
    }

    @Test
    void 로그인실패_비밀번호오류() {
        User user = new User("testUser", "1234");
        userService.join(user);

        // 잘못된 비밀번호로 로그인 시도 시 예외 발생 확인
        Assertions.assertThatThrownBy(() -> userService.login("testUser", "0000"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 잘못되었습니다.");
    }

    @Test
    void 로그인실패_존재하지않는사용자() {
        // 존재하지 않는 사용자로 로그인 시도 시 예외 발생 확인
        Assertions.assertThatThrownBy(() -> userService.login("nonexistentUser", "1234"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("아이디 또는 비밀번호가 잘못되었습니다.");
    }

}
