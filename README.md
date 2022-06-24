---
##  트리플여행자 클럽 마일리지 서비스


---
### 1. API
| METHOD | URI | DESCRIPTION |
| ------ | ----------- | ----------- |
| GET   | /users?user-name={user-name}  | 유저 조회 API       |
| GET   | /reviews?user-id={user-id}    | 리뷰 조회 API       |
| GET   | /places                       | 장소 조회 API       |
| GET   | /recentImgs?user-id={user-id} | 장소 조회 API       |
| GET   | /points/{user-id}             | 유저 포인트 조회 API|
| POST  | /events                       | 이벤트 발생 API     |


---
### 2. 애플리케이션 실행 방법
- docker-compose up 명령어를 실행해주세요.
- domain class들의 Qclass가 생성이 안되어있다면 아래 사진에서 compileQuerydsl을 더블 클릭해주세요.

     ![image](https://user-images.githubusercontent.com/52684942/175560201-70372967-f48c-4f51-875a-f13f76ca3b33.png)
 
- 어플리케이션을 실행시켜 주세요.
- 실행 시점에 users, user_profile_imgs, places, reviews, recent_imgs, review_imgs, point 테이블들과 인덱스들이 생성됩니다.
- 실행 시점에 user, place, recentImg, review, reviewImg, point 더미 데이터가 저장되어집니다.



---
### 3. 리뷰 생성 EVENT 실행 방법
- users 테이블에는 이름이 '정연재', '유재석' 유저가 저장되어있습니다.
- **유저 조회 API**를 통해 정연재 유저 정보를 확인해주세요.
- **장소 조회 API**를 통해 장소의 정보를 확인해주세요.
- 리뷰에 이미지를 첨부할 경우엔 **최근 이미지 조회 API**를 통해 해당 유저의 최근 이미지들을 확인해주세요.
- 조회한 정보들을 통해 아래 사진과 같이 **리뷰 생성 EVENT API** 요청을 보냅니다.

    ![image](https://user-images.githubusercontent.com/52684942/175563017-4ac87012-f391-413b-b7f6-7594b4f1540e.png)
- 만약 userId값이 없거나 유효하지 않은 userId라면 code:2050, message:"존재하지 않은 유저입니다." 값이 반환됩니다.
- 만약 placeId값이 없거나 유효하지 않은 placeId라면 code:2060, message:"존재하지 않은 장소입니다." 값이 반환됩니다.
- 만약 유저가 해당 장소에 리뷰를 작성했더라면 code: 2070, message: "이미 해당 장소에 리뷰를 작성하셨습니다." 값이 반환됩니다.
- 만약 유효하지 않은 atteachedPhotoIds를 입력했더라면 code:2030, message: "존재하지 않은 이미지입니다."값이 반환됩니다.
- 만약 action 값을 ADD로 주고 reviewId를 주었다면 요청한 리뷰 정보로 리뷰가 수정되어집니다.
- 생성되어진 리뷰는 **리뷰 조회 API**를 통해 확인 가능합니다.
- **유저 포인트 조회 API**를 통해 총 포인트값 확인이 가능합니다.



---
### 4. 리뷰 수정 EVENT 실행 방법
- users 테이블에는 이름이 '정연재', '유재석' 유저가 저장되어있습니다.
- **유저 조회 API**를 통해 정연재 유저 정보를 확인해주세요.
- **장소 조회 API**를 통해 장소의 정보를 확인해주세요.
- 리뷰에 이미지를 첨부할 경우엔 **최근 이미지 조회 API**를 통해 해당 유저의 최근 이미지들을 확인해주세요.
- 조회한 정보들을 통해 아래 사진과 같이 **리뷰 수정 EVENT API** 요청을 보냅니다.

    ![image](https://user-images.githubusercontent.com/52684942/175563838-64bf22da-741c-4a11-acf3-c381a4088d5a.png)
 
- 만약 userId값이 없거나 유효하지 않은 userId라면 code:2050, message:"존재하지 않은 유저입니다." 값이 반환됩니다.
- 만약 reviewId값이 없거나 유효하지 않은 reviewId라면 code:2010, message:"존재하지 않은 리뷰입니다." 값이 반환됩니다.
- 만약 유효하지 않은 atteachedPhotoIds를 입력했더라면 code:2030, message: "존재하지 않은 이미지입니다."값이 반환됩니다.
- 수정되어진 리뷰는 **리뷰 조회 API**를 통해 확인 가능합니다.
- 기존의 리뷰가 변경되고 리뷰의 이미지들도 변경됩니다.
- **유저 포인트 조회 API**를 통해 총 포인트값 확인이 가능합니다.



---
### 5. 리뷰 삭제 EVENT 실행 방법
- users 테이블에는 이름이 '정연재', '유재석' 유저가 저장되어있습니다.
- **유저 조회 API**를 통해 정연재 유저 정보를 확인해주세요.
- **리뷰 조회 API**를 통해 삭제할 리뷰의 정보를 확인해주세요.
- 조회한 정보들을 통해 아래 사진과 같이 **리뷰 삭제 EVENT API 요청**을 보냅니다.

    ![image](https://user-images.githubusercontent.com/52684942/175564591-51d6a264-65db-45e2-a5bc-562e2fc91945.png)
  
- 만약 userId값이 없거나 유효하지 않은 userId라면 code:2050, message:"존재하지 않은 유저입니다." 값이 반환됩니다.
- 만약 reviewId값이 없거나 유효하지 않은 reviewId라면 code:2010, message:"존재하지 않은 리뷰입니다." 값이 반환됩니다.
- 삭제되어진 리뷰는 **리뷰 조회 API**를 통해 확인 가능합니다.
- **유저 포인트 조회 API**를 통해 총 포인트값 확인이 가능합니다.


---
### 6. 추가사항
- 비즈니스 로직에 대한 테스트 코드를 작성하였습니다.


감사합니다  :)__


