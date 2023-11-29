#  🍳Overview

![logo](https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/0e157e21-c028-4ac6-9e3e-f5fd7b2c54db)


> 💚ECO-READING(에코리딩)💚 프로젝트


  개개인의 헌책들을 자유롭게 올리고 무료로 기부 & TAKE 할 수 있는 플랫폼을 제공함으로써👀
  
  환경 보호에 기여합니다. ~❤


  


#  🚩Project

<details>
<summary>SKILL</summary>
<div markdown="1">       

**[Front-end]**  
<img src="https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black"> <img src="https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white">
<img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" /> 

**[Back-end]**   
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
<img src="https://img.shields.io/badge/apache tomcat-F8DC75?style=for-the-badge&logo=apachetomcat&logoColor=white"> <img src="https://img.shields.io/badge/MyBatis-%232BA9E1.svg?style=for-the-badge&logoColor=white" /> 

**[Tool & Environment]**  
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/eclipse ide-2C2255?style=for-the-badge&logo=eclipse ide&logoColor=white"> <img src="https://img.shields.io/badge/figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"> <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white" />

</div>
</details>

<details>
<summary>Architecture</summary>
<div markdown="1">       

![image](https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/a73a80ec-aee2-46c9-bae8-f4e972b5f969)



</div>
</details>

<details>
<summary>ERD</summary>
<div markdown="1">       

![image](https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/ab3c3bae-eb1c-4f6f-a341-46d8a70489bf)



</div>
</details>


<details>
<summary>WIRE FRAME</summary>
<div markdown="1">  
  
![image](https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/0fe1bab7-0319-4ceb-ad37-411c3f078ba0)


[피그마 링크 입니다.](https://www.figma.com/file/rxLKOIfFVjn3o0MMHGPFzD/checkcheck?type=design&node-id=0-1&mode=design)

</div>
</details>

<details>
<summary>기능 영상</summary>
<div markdown="1">       

😎숨겨진 내용😎

</div>
</details>

#  📍 주요 기능

## **사용자**
### 1. 로그인 및 회원가입
- 사용자는 로그인과 회원가입 후에 서비스를 이용할 수 있다.
  - 로그인(Spring Security, JWT API, Redis)
  - 회원가입 및 비밀번호 변경(Redis, gmail smtp)


### 2. 책 나눔하기(기버)
- 자신의 책을 나눔하고 싶은 사용자는 책 나눔하기 서비스를 이용하여 책을 나눔할 수 있다.
  - 이미지 업로드(amazon S3)
  - 도서 API(Naver Books API)

### 2. 책 나눔 받기(테이커)
- 책을 나눔 받고 싶은 사용자는 책 나눔 받기 서비스를 활용하여 책을 나눔 받을 수 있다.
  - 주소 API(카카오 주소 API)

 ## **관리자**
 - 관리자는 체크리스트를 활용하여 기버가 올린 책을 검수할 수 있다.
 - 관리자는 1년 이상 로그인 되지 않은 유저를 비활성화 계정으로 전환할 수 있다.
   -   휴먼계정 프로세스(Spring Scheduler)
 - 관리자가 수거를 시작하거나 검수가 끝난 책에 대해 사용자에게 알림이 간다.
   - 실시간 알림 서비스(Spring SSE 통신)

## **CI / CD**
- CI/CD를 활용하여 배포
  - gitHub action과 AWS의 beanstalk을 활용


#  🚀 참여자 : 솔라파워즈 (23.11.01 ~ 23.11.27)


|<img src="https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/c5259aff-07fe-4837-81a1-be5226d184b1" width="120" height="160"/><br/>BE 조솔하 <a href="https://github.com/josolha">GitHub</a>|<img src="https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/4ddcd83d-4c48-4575-a5e6-ad30735fa1e8" width="120" height="160"/><br/>BE 임유빈 <a href="https://github.com/yubin-im">GitHub</a>|<img src="https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/5ad2d7ab-16af-485d-a650-44cb5f833b6f" width="120" height="160"/><br/>BE 강유신 <a href="https://github.com/simidot">GitHub</a>|<img src="https://github.com/Team-Solar-Powers/eco_reading/assets/74632395/366dd0fa-6e4e-4064-94d6-c17ded5662e2" width="120" height="160"/><br/>BE 김락윤 <a href="https://github.com/rakyun1">GitHub</a>|
|:---:|:---:|:---:|:---:|



#  💊 트러블 슈팅
## 1.실시간 알림 서비스
[ [실시간 알림 서비스 적용기] ](https://josolha.tistory.com/36)

## 2.S3 이미지 업로드
[ [AWS S3 (이미지 다운로드 에러)] ](https://josolha.tistory.com/35)

## 3.JWT 로그인
[ [JWT를 위해] ](https://josolha.tistory.com/28)

## 4.나눔글 수정
[ [나눔글 수정 중 올라간 이미지 삭제] ](https://www.notion.so/rakyun/e6b65542efdf443cade7229cf397e7d6)

## 5.활성화, 비활성화 프로세스
[[1년 이상 로그인 하지 않은 유저 비활성화로 전환]](https://www.notion.so/rakyun/44d6245c8db24fb0bf1b22ee2268fe86)
