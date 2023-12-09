package com.example.springproject.controller;

import ch.qos.logback.classic.Logger;

import com.example.springproject.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.springproject.dto.ArticleForm;
import com.example.springproject.repository.ArticleRepository;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller

public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;
    @GetMapping("/articles/new")
    public String newArticleForm(){
        return "articles/new";
    }



    @PostMapping("/articles/create")
            public String createArticle(ArticleForm form){

                log.info(form.toString());
                // System.out.println(form.toString());
                //1. DTO를 엔티티로 변환
                Article article = form.toEntity();
                log.info(article.toString());
                //  System.out.println(article.toString());// dto가 엔티티로 잘 변환되는지 확인 출력
                //2. 리파지터리로 엔티티를 DB에 저장
                Article saved = articleRepository.save(article);
                log.info(saved.toString());
                // System.out.println(saved.toString()); // article이 db에 잘 저장되는지 확인 출력
                return "redirect:/articles/"+saved.getId();
        }

    @GetMapping("/articles/{id}") // 데이터 조회 요청 접수
    public String show(@PathVariable Long id, Model model){
        log.info("id = " +id);
        // 1. id 조회후 데이터 가져오기
        // Optional<Article> articleEntity = articleRepository.findById(id);
        Article articleEntity = articleRepository.findById(id).orElse(null);
        // 2. 모델에 데이터 등록하기
        model.addAttribute("article",articleEntity);
        // 3. 뷰 페이지 반환하기
        return "articles/show";
    }
    @GetMapping("/articles")
    public String index(Model model){
        // 1. 모든 데이터 가져오기
        // 다운 캐스팅 List<Article> articleEntityList = (List<Article>) articleRepository.findAll();
        // 업캐스팅    Iterable<Article> articleEntityList = articleRepository.findAll();
        List<Article> articleEntityList = articleRepository.findAll();
        // 2. 모델에 데이터 등록하기
        model.addAttribute("articleList",articleEntityList);
        // 3. 뷰 페이지 설정하기
        return "articles/index";
    }

    @GetMapping("articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        Article articleEntity = articleRepository.findById(id).orElse(null);
        model.addAttribute("article", articleEntity);
        return "articles/edit";
    }

    @PostMapping("articles/update")
    public String update(ArticleForm form){
        log.info(form.toString());
        // dto > 엔티티 변환
        Article articleEntity = form.toEntity();
        // 엔티티 db 저장
        Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
        if (target !=null){
            articleRepository.save(articleEntity);
        }
        // 수정결과 페이지로 리다이렉트
        return "redirect:/articles/"+target.getId();
    }
    @GetMapping("articles/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr){
        //삭제할 대상 가져오기
        Article target = articleRepository.findById(id).orElse(null);
        //대상 엔티티 삭제하기
        if(target != null){
            articleRepository.delete(target);
            rttr.addFlashAttribute("msg","삭제됐습니다!!!!");
        }
        // 결과페이지로 리다이렉트하기
        return "redirect:/articles";
    }
}

