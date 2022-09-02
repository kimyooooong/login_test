## 프로젝트 소개

Java Spring / Rest API / JWT 기반 로그인 서비스 구현.

![image](https://user-images.githubusercontent.com/103039923/188109048-f708be2b-451c-44b3-8a1b-3a1a3fab449e.png)

## 사용언어 및 프레임 워크

언어 : 
* Java 11 / Spring Boot 2.7.3

DB : 
* MySQL ( Mybatis )

기타 기술 : 

* JWT 토큰 방식 사용
* SWagger 를 이용항 Rest API 문서화.
* ExceptionHandller 를 통한 오류 및 에러 페이지 처리.
* SpringSecurity 를 이용한 토큰 검증 및 URL 권한 처리.
* 데이터 암호화 - 개인 정보 ( 양방향 AES256 ) , 패스워드 - ( 단방향 SHA-256 )

---

## 구현 시 사용한 기술 및 특이사항

  * 휴대폰 인증 방식은 외부 연동을하지않고 DB 에만 기록하고 인증번호를 클라이언트에 전달하여 처리.
  * 모든 정보는 암호화 되어 디비에 저장하였으며 패스워드는 단방향 암호화 ( SHA-256 ) , 그 외 정보는 양방향 암호화 ( AES-256 ) 적용.
  * 사용된 REST-API 는 SWAGGER 로 문서 구현하여 확인 가능. ( localhost:8080/swagger-ui/index.html )
  * 프론트 기술은 thymeleaf / Jquery 를 사용하였으며 로그인 폼 오픈 소스를 기반으로 구현하였음. ( https://colorlib.com/wp/template/login-form-v2/ ) 
  
--- 

## 구동 방법

  - 사전 필요한 설치 파일 관련 
    * Java 11+
    * Mysql
  - 디비 스키마 실행
    * /result/db_schema.sql  mysql 에서 DDL 파일 실행. ( 디비 추가 및 테이블 추가. )
    * 기본 유저이름은 : root  비밀번호 : toor 로 설정.
  - 서버 실행
    * 기본 포트는 8080 이며 내장 톰캣으로 실행 됨.
    * /result/login_test.jar 파일 실행. ( java -jar login_test.jar )
    
    
    

