package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.config.TestSecurityConfig;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {

    @MockBean
    private ArticleManagementService articleManagementService;

    private final MockMvc mvc;

    public ArticleManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        // given
        given(articleManagementService.getArticles()).willReturn(List.of());

        // when & then
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));
        then(articleManagementService).should().getArticles();
    }

    @DisplayName("[view][GET] 게시글 1개 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticle_thenReturnsArticle() throws Exception {
        // given
        Long articleId = 1L;
        ArticleDto articleDto = createArticleDto("title", "content");
        given(articleManagementService.getArticle(articleId)).willReturn(articleDto);

        // when & then
        mvc.perform(get("/management/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleId))
                .andExpect(jsonPath("$.title").value(articleDto.title()))
                .andExpect(jsonPath("$.content").value(articleDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleDto.userAccount().nickname()));
        then(articleManagementService).should().getArticle(articleId);
    }

    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        // given
        Long articleId = 1L;
        willDoNothing().given(articleManagementService).deleteArticle(articleId);

        // when & then
        mvc.perform(
                        post("/management/articles")
                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("management/articles"))
                .andExpect(redirectedUrl("/management/articles"));
        then(articleManagementService).should().deleteArticle(articleId);
    }


    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "unoTest",
                "uno-test@email.com",
                "uno-test",
                "test memo"
        );
    }


}