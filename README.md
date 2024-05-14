## API DOCUMENTATION

### 일반 로그인


### 소셜 로그인
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

현재 Spring Security 환경이 배포된 상태가 아니라 localhost:8080로 설정.
.env 등 파일을 통해 환경 변수로 설정하는 걸 권장.

useSearchParams (리액트에서 제공하는 라이브러리)로 현재 access_token과 refresh_token을 직접 받아올 수 있음.
##### const accessToken = useSearchParams.get('access_token');
