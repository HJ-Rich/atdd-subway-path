<p align="center">
    <img src="./woowacourse.png" alt="우아한테크코스" width="250px">
</p>

# Level 2, 지하철 경로 조회

---

![Generic badge](https://img.shields.io/badge/Level2-subway_path-green.svg)
![Generic badge](https://img.shields.io/badge/test-115_passed-blue.svg)
![Generic badge](https://img.shields.io/badge/version-2.0.0-brightgreen.svg)

> 우아한테크코스 웹 백엔드 4기, 지하철 경로 조회 저장소입니다.

<p align="center">
    <img src="./subway-path-operation.gif" alt="subway-path-operation" width="500px">
</p>

<br>

# How to Start

---

### 1. Run SpringBootApplication

```
./gradlew bootRun
```

<br>

### 2. Open in Browser

- [https://d2owgqwkhzq0my.cloudfront.net/](https://d2owgqwkhzq0my.cloudfront.net/)

<br><br>

# 요구사항 목록

## 경로 조회

- [x] GET /paths?source={source}&target={target}&age={age} : 출발역부터 도착역까지 최단경로와 요금을 조회할 수 있다
    - [x] 조회 성공 시, 출발역부터 도착역까지 순차적으로 지하철 역 정보와 거리 및 요금을 반환한다.
    - [x] [예외처리] 도착지까지 구간이 이어져 있지 않을 경우 400 BAD_REQUEST 를 반환한다.

<br><br>

## 지하철 역

### 등록

- [x] POST /stations : name 을 전달하여 지하철역을 등록할 수 있다
    - [x] 생성 성공 시 생성된 id 와 name 을 body 에 담아 200 CREATED 를 응답한다
    - [x] [예외처리] 이름이 중복될 경우 생성에 실패하며, 요청했던 이름을 body에 담아 400 BAD_REQUEST 를 응답한다

### 전체 조회

- [x] GET /stations : 등록된 지하철역 전체를 조회한다
    - [x] 조회 성공시 id와 name 목록을 body 에 담아 200 OK 를 반환응답한다
    - [x] [예외처리] 등록된 지하철이 없을 경우 404 NOT_FOUND 응답을 반환한다

### 삭제

- [x] DELETE /stations/{id} : id 를 전달하여 지하철역을 삭제할 수 있다
    - [x] 삭제 성공 시 204 NO_CONTENT 응답을 반환한다
    - [x] [예외처리] id에 해당하는 지하철역이 없을 경우, 요청했던 id를 body에 담아 404 NOT_FOUND 를 응답한다

<br><br>

# 지하철 노선

### 등록

- [x] POST /lines : name, color, upStationId, downStationId, distance 를 전달해 노선을 등록할 수 있다
    - [x] 생성 성공 시 생성 시 요청한 정보와 생성된 노선에 속하는 지하철역을 body 에 담아 201 CREATED 를 응답한다
    - [x] [예외처리] name 이 중복될 경우 생성에 실패하며, 요청했던 name 을 body 에 담아 400 BAD_REQUEST 를 응답한다
    - [x] [예외처리] color 가 중복될 경우 생성에 실패하며, 요청했던 color 를 body 에 담아 400 BAD_REQUEST 를 응답한다

### 전체 조회

- [x] GET /lines : 전체 지하철 노선을 조회할 수 있다
    - [x] 조회 성공 시 전체 노선 정보를 body 에 담아 200 OK 를 응답한다
    - [x] [예외처리] 등록된 노선이 없을 경우 요청했던 id 를 body 에 담아 404 NOT_FOUND 를 응답회다

### 단건 조회

- [x] GET /lines/{id} : id 로 하나의 지하철 노선을 조회할 수 있다
    - [x] 조회 성공 시 노선 정보를 body 에 담아 200 OK 를 응답한다
    - [x] [예외처리] 등록된 노선이 없을 경우 요청했던 id 를 body 에 담아 404 NOT_FOUND 를 응답회다

### 수정

- [x] PUT /lines/{id} : name, color 를 전달해 id 에 해당하는 노선을 수정할 수 있다
    - [x] 수정 성공 시 200 OK 를 응답한다
    - [x] [예외처리] id 에 해당하는 노선이 없을 경우 요청했던 id 를 body 에 담아 404 NOT_FOUND 를 응답한다
    - [x] [예외처리] name 이 중복될 경우 수정에 실패하며, 요청했던 name 을 body 에 담아 400 BAD_REQUEST 를 응답한다
    - [x] [예외처리] color 가 중복될 경우 수정에 실패하며, 요청했던 color 를 body 에 담아 400 BAD_REQUEST 를 응답한다

### 삭제

- [x] DELETE /lines/{id} : id 를 전달하여 노선을 삭제할 수 있다
    - [x] 삭제 성공 시 204 NO_CONTENT 를 응답한다
    - [x] [예외처리] 등록된 노선이 없을 경우 요청했던 id 를 body 에 담아 404 NOT_FOUND 를 응답한다

<br><br>

# 지하철 구간

### 등록

- [x] POST /lines/{id}/sections : upStationId, downStationId, distance 를 전달하여 구간을 등록할 수 있다
    - [x] 생성 성공시 200 OK 를 응답한다
    - [x] [예외처리] 생성 실패 조건 추가 확인 필요

- [x] DELETE /lines/{id}/sections?stationId={stationId}
    - [x] 삭제 성공 시 200 OK 를 응답한다 (NO_CONTENT 가 되어야 할 것 같으나 문서를 따름)
    - [x] [예외처리] 등록된 구간이 없을 경우 요청했던 id 와 stationId 를 body 에 담아 404 NOT_FOUND 를 응답한다

<br><br>

# 도메인 안내

<p align="center">
    <img src="./domain.png" alt="front" width="500px">
</p>

### 지하철 역(station)

- 지하철 역 속성:
    - 이름(name)

### 지하철 구간(section)

- 지하철 (상행 방향)역과 (하행 방향)역 사이의 연결 정보
- 지하철 구간 속성:
    - 길이(distance)

### 지하철 노선(line)

- 지하철 구간의 모음으로 구간에 포함된 지하철 역의 연결 정보
- 지하철 노선 속성:
    - 노선 이름(name)
    - 노선 색(color)

<br><br>

## API Document

- [Subway Path API Document](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/c4c291f19953498e8eda8a38253eed51#Path)

<br><br>
