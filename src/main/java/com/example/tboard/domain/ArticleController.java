package com.example.tboard.domain;


import com.example.tboard.base.CommonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping("/detail")
    @ResponseBody
    public String detail(@RequestParam("articleId")int articleId) {
//        System.out.print("상세보기 할 게시물 번호를 입력해주세요 : ");
//
//        int inputId = getParamAsInt(scan.nextLine(), WRONG_VALUE);
//        if(inputId == WRONG_VALUE) {
//            return;
//        }

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
//            System.out.println("없는 게시물입니다.");
            return "없는 게시물입니다.";
        }

        article.increaseHit();
        String jsonString = "";
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            jsonString = objectMapper.writeValueAsString(article);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jsonString;
//        articleView.printArticleDetail(article);
    }
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("articleId") int articleId) {

//        System.out.print("삭제할 게시물 번호를 입력해주세요 : ");
//
//        int inputId = getParamAsInt(scan.nextLine(), WRONG_VALUE);
//        if(inputId == WRONG_VALUE) {
//            return;
//        }

        Article article = articleRepository.findArticleById(articleId);

        if (article == null) {
            return "없는 게시물입니다.";
        }

        articleRepository.deleteArticle(article);
        return "%d 게시물이 삭제되었습니다.".formatted(articleId);
    }

    @RequestMapping("/update")
    @ResponseBody
    public String update(@RequestParam("articleId") int inputId,
                         @RequestParam("newTitle") String newTitle,
                         @RequestParam("newBody") String newBody) {
//        System.out.print("수정할 게시물 번호를 입력해주세요 : ");

//        int inputId = getParamAsInt(scan.nextLine(), WRONG_VALUE);
//        if(inputId == WRONG_VALUE) {
//            return;
//        }

        Article article = articleRepository.findArticleById(inputId);

        if (article == null) {
//            System.out.println("없는 게시물입니다.");
            return "없는 게시물입니다.";
        }

//        System.out.print("새로운 제목을 입력해주세요 : ");
//        String newTitle = scan.nextLine();
//
//        System.out.print("새로운 내용을 입력해주세요 : ");
//        String newBody = scan.nextLine();
//
        articleRepository.updateArticle(article, newTitle, newBody);
        return "%d번 게시물이 수정되었습니다.".formatted(inputId);
//        System.out.printf("%d번 게시물이 수정되었습니다.\n", inputId);
    }

    @RequestMapping("/list")
    public String list(Model model) {
        ArrayList<Article> articleList = articleRepository.findAll();
        model.addAttribute("articleList",articleList);

        return "list";
//        articleView.printArticleList(articleList); // 전체 출력 -> 전체 저장소 넘기기
    }

    // add 참고) 실제 데이터 저장 처리 부분
    @RequestMapping("/add")
//    @ResponseBody
    public String add(@RequestParam("title")String title,
                      @RequestParam("body")String body,
                      Model model) {

//        System.out.print("게시물 제목을 입력해주세요 : ");
//        String title = scan.nextLine();
//
//        System.out.print("게시물 내용을 입력해주세요 : ");
//        String body = scan.nextLine();

        articleRepository.saveArticle(title, body);
//        System.out.println("게시물이 등록되었습니다.");

        // 위에서 받은 입력값을 저장해주는 역할
        ArrayList<Article> articleList = articleRepository.findAll();

        model.addAttribute("articleList", articleList);
        return "list";

    }
    // add 참고)입력화면 보여 주기
    @RequestMapping("/form")
    public String form(){
    return "form";
    }

//    private int getParamAsInt(String param, int defaultValue) {
//        try {
//            return Integer.parseInt(param);
//        } catch (NumberFormatException e) {
//            System.out.println("숫자를 입력해주세요.");
//            return defaultValue;
//        }
//    }
}
