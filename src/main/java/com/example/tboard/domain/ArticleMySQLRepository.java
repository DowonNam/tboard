package com.example.tboard.domain;


import com.example.tboard.base.CommonUtil;

import java.sql.*;
import java.util.ArrayList;

public class ArticleMySQLRepository implements Repository {

    private static final String URL = "jdbc:mysql://localhost:3306/t2";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    @Override
    public void makeTestData() {

    }

    @Override
    public ArrayList<Article> findArticleByKeyword(String keyword) {
        String keywordWithWildCard = "%" + keyword + "%";
        String sql = "SELECT id, title, body, hit, regDate FROM article WHERE title LIKE ?";

        ArrayList<Article> articleList = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            // 쿼리 파라미터 설정
            pstmt.setString(1, keywordWithWildCard);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    // ResultSet에서 데이터 추출
                    int articleId = rs.getInt("id");
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    int hit = rs.getInt("hit");
                    String regDate = rs.getString("regDate");

                    // Article 객체 생성 및 반환
                    Article article = new Article(articleId, title, body, hit, regDate);
                    articleList.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return articleList;
    }

    @Override
    public Article findArticleById(int id) {
        String sql = "SELECT id, title, body, hit, regDate FROM article WHERE id = ?";

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            // 쿼리 파라미터 설정
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // ResultSet에서 데이터 추출
                    int articleId = rs.getInt("id");
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    int hit = rs.getInt("hit");
                    String regDate = rs.getString("regDate");

                    // Article 객체 생성 및 반환
                    return new Article(articleId, title, body, hit, regDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 조회 중 오류가 발생했습니다: " + e.getMessage());
        }

        // 일치하는 게시물이 없는 경우 null 반환
        return null;
    }

    @Override
    public void deleteArticle(Article article) {

        String sql = "DELETE FROM article WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // PreparedStatement에 파라미터 설정
            pstmt.setInt(1, article.getId());

            // 쿼리 실행
            pstmt.executeUpdate();
            System.out.println("데이터가 성공적으로 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

    }

    @Override
    public void updateArticle(Article article, String newTitle, String newBody) {
        String sql = "UPDATE article SET title = ?, `body` = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // PreparedStatement에 파라미터 설정
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newBody);
            pstmt.setInt(3, article.getId());

            // 쿼리 실행
            pstmt.executeUpdate();
            System.out.println("데이터가 성공적으로 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Article> findAll() {
        String sql = "SELECT * FROM article";
        ArrayList<Article> articleList = new ArrayList<>();

        try (
                Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // ResultSet에서 데이터 추출
                    int articleId = rs.getInt("id");
                    String title = rs.getString("title");
                    String body = rs.getString("body");
                    int hit = rs.getInt("hit");
                    String regDate = rs.getString("regDate");

                    // Article 객체 생성 및 반환
                    Article article = new Article(articleId, title, body, hit, regDate);
                    articleList.add(article);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return articleList;
    }

    @Override
    public Article saveArticle(String title, String body) {

        // article 테이블에 게시물 저장
        CommonUtil commonUtil = new CommonUtil();
        String sql = "INSERT INTO article (title, body, hit, regDate) VALUES (?, ?, ?, ?)";

        // 자동으로 닫혀야 하는 리소스들을 try-with-resources 구문을 사용해 관리
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // PreparedStatement에 파라미터 설정
            pstmt.setString(1, title);
            pstmt.setString(2, body);
            pstmt.setInt(3, 0);
            pstmt.setString(4, commonUtil.getCurrentDateTime());

            // 쿼리 실행
            pstmt.executeUpdate();
            System.out.println("데이터가 성공적으로 저장되었습니다.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("데이터 저장 중 오류가 발생했습니다: " + e.getMessage());
        }

        return null;
    }
}
