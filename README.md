# API DOCUMENTATION

소셜 로그인을 제외한 문서는 모두 Swagger UI를 통해서 제공 예정
=======
# 테스트 가능한 API

### POST
- /auth/signup
- /auth/login
- /auth/refresh/{id}  -> 테스트용
- /auth/sign-up/emailCheck
- /auth/sign-up/verify
- /boards/write/{category}

### GET
- /refresh-token/{id}
- /boards/list/{category}
- /boards/read/{category}/{boardId}
- /boards/read/{category}/{boardId}/edit
- /boards/{category}/{boardId}/delete

### DELETE
- /auth/lougout/{id}
- /auth/delete

### 기타 테스트가 필요시
#### MongoDB 
- /mongo/find (GET)
- /mongo/save (POST)

#### S3
- /s3/upload (GET)
- /s3/download (POST)

### 일반 로그인

>>>>>>> Stashed changes

## 소셜 로그인
Callback URL을 직접 호출함으로써 로그인 기능을 실행

### NAVER
##### Login
- http://localhost:8080/oauth2/authorization/naver?redirect_uri={redirect_url}&mode=login
##### Logout
- http://localhost:8080/oauth2/authorization/naver?redirect_uri={redirect_url}&mode=unlink

### KAKAO
##### Login
- http://localhost:8080/oauth2/authorization/kakao?redirect_uri={redirect_url}&mode=login
##### Logout
- http://localhost:8080/oauth2/authorization/kakao?redirect_uri={redirect_url}&mode=unlink

현재 Spring Security 환경이 배포된 상태가 아니라 baseUrl을 localhost:8080로 설정.
.env 등 파일을 통해 환경 변수로 설정하는 걸 권장.

소셜 로그인은 URL의 쿼리 파라미터로 값을 제공, 액세스 토큰과 리프레쉬 토큰은 각각 access_token과 refresh_token 값을 가져오면 됨.
##### const accessToken = useSearchParams.get('access_token');
