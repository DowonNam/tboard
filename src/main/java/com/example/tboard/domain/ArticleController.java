package com.example.tboard.domain;


import com.example.tboard.base.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Scanner;

// Model - Controller - View
@Controller // 붙여줘야만 웹이랑 연관이 됨
public class ArticleController { // Model + Controller

    CommonUtil commonUtil = new CommonUtil();
    ArticleView articleView = new ArticleView();
    ArticleRepository articleRepository = new ArticleRepository();

    Scanner scan = commonUtil.getScanner();
    int WRONG_VALUE = -1;
@RequestMapping("/search")
@ResponseBody
    public ArrayList<Article> search(@RequestParam(value = "keyword", defaultValue = "") String keyword) {
        // 검색어를 입력
//        System.out.println("검색 키워드를 입력해주세요 :");
//        String keyword = scan.nextLine();
        ArrayList<Article> searchedList = articleRepository.findArticleByKeyword(keyword);

//        articleView.printArticleList(searchedList);
    return searchedList;

}
    @RequestMapping("/detail/{articleId}")
    public String detail(@PathVariable("articleId") int articleId, Model model) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        article.increaseHit();

        // 위에 거 삭제하고 생성자에 모델을 추가한 뒤 모델로 html에 넘겨준다
        model.addAttribute("article", article);
        return "detail";
    }
    @RequestMapping("/delete/{articleId}")
    @ResponseBody
    public String delete(@PathVariable("articleId") int articleId) {


        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "redirect:/list";
    }
    // 보여주기용이기 때문에 get을 사용한다
    @GetMapping("/update/{articleId}")
    public String updateForm(@PathVariable("articleId") int articleId, Model model){

        Article article = articleRepository.findArticleById(articleId);

        if(article==null){
            //예외처리
            throw new RuntimeException("없는 게시물입니다.");
        }

        model.addAttribute("article", article);
        return "updateForm";
    }

    // 실제로 서버 처리를 하는 것이기 때문에 post
    @PostMapping("/update/{articleId}")
    public String update(@PathVariable("articleId") int articleId,
                         @RequestParam("title") String title,
                         @RequestParam("body") String body) {

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            throw new RuntimeException("없는 게시물입니다.");
        }

        articleRepository.updateArticle(article, title, body);
        // 내가 수정한 id로 변경되어 주소가 들어간다
        return "redirect:/detail/%d".formatted(articleId);

    }

    @RequestMapping("/list")
    public String list(Model model) {
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList",articleList);

        return "list";
    }

    // add 참고) 실제 데이터 저장 처리 부분
    @PostMapping("/add")
    public String add(@RequestParam("title")String title,
                      @RequestParam("body")String body,
                      Model model) {

        articleRepository.saveArticle(title, body);
//        System.out.println("게시물이 등록되었습니다.");

//        // 위에서 받은 입력값을 저장해주는 역할
        // list로 브라우저 주소를 변경해주면서 필요가 없어졌음  > list 자체에서 기능을 하면 되기 때문
//        ArrayList<Article> articleList = articleRepository.findAll();
//        model.addAttribute("articleList", articleList);

        // 문제 : 새로고침 할때마다 강제로 값이 추가가 되는 문제
        // 문제 원인 : add 요청의 결과 화면을 list로 보여주고 있다
        // 문제 해결 : add url을 list로 바꾸면 된다
        // controller에서 주소를 바꾸는 법 : redirect
        return "redirect:/list"; // 브라우저의 주소가 /list로 바뀜

    }
    // add 참고)입력화면 보여 주기
    // get이랑 postmapping 으로 각각 바꿔주면 같은 add를 사용할 수 있다 안 바꿔주면 오류 뜸
    @GetMapping("/add")
    public String form(){
    return "form";
    }




}
