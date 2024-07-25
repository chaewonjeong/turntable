# 🎧 Turntable ; 매일 당신만을 위한 플레이리스트
**매일 새로운 음악을 발견하고 싶은 당신을 위해 맞춤형 플레이리스트를 제공합니다.**
**당신의 취향과 분위기에 맞춘 신선한 곡들을 추천받아 음악 감상의 즐거움을 누려보세요.**


![image](https://github.com/user-attachments/assets/9e553810-402c-4055-b895-1f998020481a)


## 1. 프로젝트 소개
  **본 프로젝트는 사용자가 원하는 취향의 음악을 추천받고 서로 공유할 수 있는 미니블로그 형태의 애플리케이션을 개발하는 것입니다. 
  이 애플리케이션은 음악을 매개로 한 소셜 네트워킹 플랫폼으로서의 유용한 기능들을 제공합니다.**
  ```
  - 매일 좋아하는 가수와 음악 장르를 기반으로 추천 플레이리스트를 받아볼 수 있습니다.
  - 추천받은 플레이리스트는 물론 좋아하는 노래를 모아 플레이리스트를 만들고 공유할 수 있습니다.
  - 다른 사용자들의 플레이리스트를 보고 댓글을 남길 수 있습니다.
  - 공유한 노래들은 바로 재생해볼 수 있습니다.
```

  ### 팀원 구성 (멀티잇 백엔드 개발(스프링) 24회차 5조)
  | 정채원 | 강지원 |
  | :--------: | :--------: |
  |   @chaewonjeong    |      @onegqueen      |

## 2. 개발 환경

- **Front** : 
![HTML5](https://img.shields.io/badge/html5-%23E34F26.svg?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/css3-%231572B6.svg?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
- **Back-end** : 
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![Spotify](https://img.shields.io/badge/Spotify-1ED760?style=for-the-badge&logo=spotify&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-005571?style=for-the-badge&logo=fastapi)
- **DB** : 
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
- **버전 및 이슈관리** : 
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)
- **협업 툴** :
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white)
![Google Drive](https://img.shields.io/badge/Google%20Drive-4285F4?style=for-the-badge&logo=googledrive&logoColor=white)
- **서비스 배포 환경** :
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

- **디자인** :
![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white)

## 3. 채택한 개발 기술과 브랜치 전략
- `Git-flow` 전략을 기반으로 `main`브랜치를 개발(`develop`)브랜치로 운용하며 `feature,fix,refact` 등의 보조 브랜치를 운용했습니다.
- 기능별 이슈 생성 : 이슈템플릿을 활용하여 가독성 높은 이슈 작성을 하였고 이슈번호에 따른 브랜치를 생성하였습니다.
- 브랜치명 : `feat/{이슈번호}`


## 4. 프로젝트 구조
```
├─.github
│  └─ISSUE_TEMPLATE
├─.idea
│  └─modules
├─crawlingserver
│  └─__pycache__
└─turntable
    ├─.gradle
    ├─build
    ├─gradle
    ├─out
    └─src
        ├─main
        │  ├─java
        │  │  └─com
        │  │      └─example
        │  │          └─turntable
        │  │              ├─auth
        │  │              ├─config
        │  │              ├─controller
        │  │              ├─domain
        │  │              ├─dto
        │  │              ├─event
        │  │              ├─exception
        │  │              ├─repository
        │  │              ├─service
        │  │              ├─spotify
        │  │              │  └─dto
        │  │              └─youtube
        │  │                  └─dto
        │  ├─resources
        │  │  ├─META-INF
        │  │  └─static
        │  │      ├─bgimg
        │  │      ├─css
        │  │      │  └─bgimg
        │  │      └─js
        │  └─webapp
        │      └─WEB-INF
        │          └─views
        └─test
            └─java
                └─com
                    └─example
                        └─turntable
```

## 5. 역할 분담
#### 🎀 강지원
- `Backend(Springboot)` / `Frontend`
- 주요기능
  - 사용자 인증 및 권한관리
  - 음악 추천 알고리즘 적용
    
#### 💩정채원
- `Backend(Springboot , Fastapi)`
- 기능

## 6. 개발 기간 및 작업 관리
**개발 기간**
  - 전체 개발 기간 : `2024-06-14 ~ 2022-07-25`

**작업 관리**
  - `GitHub Projects`와 `Issues`를 사용하여 진행 상황을 공유했습니다.
  - 일일회의를 진행하며 작업 순서와 방향성에 대한 고민을 나누고 일일 개발 회의록을 작성하였습니다.

## 7. 페이지별 기능
### 1. 메인페이지
 사용자의 메인페이지로 사용자 닉네임, 최근게시물, 댓글, 플레이리스트 등을 확인할 수 있다.
 
### 2. 마이페이지
 사용자의 마이페이지로 사용자 정보조회, 로그아웃 , 닉네임 및 배경화면 변경, 탈퇴등을 할 수 있다.
 
### 3. 유저검색페이지
 타 사용자의 페이지를 검색할 수 있는 페이지로 닉네임을 통한 사용자 검색을 할 수 있다.
 
### 4. 음악추천페이지
 음악추천을 받을 수 있는 페이지로, 선호하는 가수,음악,장르를 선택하여 음악을 추천받을 수 있다.
 
### 5. 플레이리스트 서랍
 사용자의 플레이리스트를 조회하고 재생할 수 있는 페이지

## 8. 트러블 슈팅
 ### 1. 회원 탈퇴 기능 구현 중 발생한 연관 데이터 삭제 오류 해결
 ### 2. 비동기 이벤트 처리 중 발생한 트랜잭션 문제 해결
 
## 9. 개선 목표
### 1. 기능추가
사용자 간 상호작용을 강화하고 음악 공유를 활성화하며, 개인 맞춤형 음악 추천 서비스로 인한 사용자 유입 및  수익 창출을 기대할 수 있습니다.

   **1-1. 팔로우 및 좋아요 기능 추가**
     
     - 사용자들이 서로를 팔로우하고 좋아요를 표시할 수 있는 기능을 추가하여 소셜 네트워킹을 강화합니다.
  
   **1-2. 플레이리스트 공유 기능 추가**
     
     - 다른 사용자의 플레이리스트를 자신의 플레이리스트에 저장할 수 있는 기능을 구현하여 사용자 간 음악 공유를 활성화합니다.
  
   **1-3. 프리미엄 구독 모델 고안**
     
     - 프리미엄 구독 모델을 도입하여 사용자 취향 분석을 고도화하고, 개인 맞춤형 음악 추천 서비스를 제공합니다.
     
### 2. 성능 개선
부족한 성능을 개선하여 사용자 경험을 향상시킬 수 있습니다.

  **2-1. 부족한 예외처리 추가**

   **2-2. 단위테스트 코드 추가**

   **2-3. 음악 검색 성능 개선**

## 10. 프로젝트 회고
🎀강지원

💩정채원
