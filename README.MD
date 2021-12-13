Các bước để có thể chạy chương trình:

Docs: https://drive.google.com/drive/folders/10NJvz2xErLEeGMYUXACltZFDuI2XaAdQ?usp=sharing

1. Tạo cơ sở dữ liệu

2. Sửa application.properties

3. Run Application


Đăng kí tài khoản:

```sh
curl --location --request POST 'http://localhost:8080/api/auth/signup' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=275643686A1E327FDC937AF044C8DDD9' \
--data-raw '{
    "username": "ngocpv",
    "password": "123456",
    "role": ["user", "admin"]
}'
```
=> Bảng users có thêm dữ liệu.

Đăng nhập:

```sh
curl --location --request POST 'http://localhost:8080/api/auth/signin' \
--header 'Content-Type: application/json' \
--header 'Cookie: JSESSIONID=275643686A1E327FDC937AF044C8DDD9' \
--data-raw '{
    "username": "ngocpv",
    "password": "123456"
}'
```

Sử dụng API được phân quyền - API trong TestController:

```sh
curl --location --request GET 'http://localhost:8080/api/test/user' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ29jcHYiLCJpYXQiOjE2MzkzOTIwMDQsImV4cCI6MTYzOTQ3ODQwNH0.4sdxdX43t_KINsHPe0R8CGJsI4EWAsuBoFlp2fx9W52v4m1lSKmiGU2fSkEhq9-3R1H0y3ojc2l7D-03mXumPg' \
--header 'Cookie: JSESSIONID=275643686A1E327FDC937AF044C8DDD9'


curl --location --request GET 'http://localhost:8080/api/test/mod' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ29jcHYiLCJpYXQiOjE2MzkzOTIwMDQsImV4cCI6MTYzOTQ3ODQwNH0.4sdxdX43t_KINsHPe0R8CGJsI4EWAsuBoFlp2fx9W52v4m1lSKmiGU2fSkEhq9-3R1H0y3ojc2l7D-03mXumPg' \
--header 'Cookie: JSESSIONID=275643686A1E327FDC937AF044C8DDD9'

curl --location --request GET 'http://localhost:8080/api/test/admin' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZ29jcHYiLCJpYXQiOjE2MzkzOTIwMDQsImV4cCI6MTYzOTQ3ODQwNH0.4sdxdX43t_KINsHPe0R8CGJsI4EWAsuBoFlp2fx9W52v4m1lSKmiGU2fSkEhq9-3R1H0y3ojc2l7D-03mXumPg' \
--header 'Cookie: JSESSIONID=275643686A1E327FDC937AF044C8DDD9'
```
=> API /user, /admin truy cập được  
=> API /mod không thể truy cập
