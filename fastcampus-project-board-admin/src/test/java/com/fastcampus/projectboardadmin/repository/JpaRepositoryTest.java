package com.fastcampus.projectboardadmin.repository;

import com.fastcampus.projectboardadmin.domain.UserAccount;
import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    // @Autowired 생성자 위치에 사용하지 않는 이유는 전체를 autowired 하기 때문에 다른것을 모킹을 하거나 autowired 하지 않고 싶은것까지 되기 때문에
    // 선택적인 상황에서 제한이 있기 때문에 각필드에 건다.
    public JpaRepositoryTest(@Autowired UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenUserAccounts_whenSelecting_thenWorksFine() {
        // given

        // when
        List<UserAccount> userAccounts = userAccountRepository.findAll();

        // then
        assertThat(userAccounts)
                .isNotNull()
                .hasSize(4);
    }

    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenUserAccount_whenInserting_thenWorksFine() {
        // given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = UserAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);

        // when
        userAccountRepository.save(userAccount);

        // then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenUserAccountAndRoleType_whenUpdating_thenWorksFine() {
        // given
        UserAccount userAccount = userAccountRepository.getReferenceById("uno");
        userAccount.addRoleType(RoleType.DEVELOPER);
        userAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
        userAccount.removeRoleType(RoleType.ADMIN);

        // when
        UserAccount updatedAccount = userAccountRepository.saveAndFlush(userAccount);

        // then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "uno")
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenUserAccount_whenDeleting_thenWorksFine() {
        // given
        long previousCount = userAccountRepository.count();
        UserAccount userAccount = userAccountRepository.getReferenceById("uno");

        // when
        userAccountRepository.delete(userAccount);

        // then
        assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
    }


    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }


}