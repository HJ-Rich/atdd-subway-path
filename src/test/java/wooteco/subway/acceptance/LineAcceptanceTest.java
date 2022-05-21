package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.SubwayFixtures.BOONDANGLINE_REQUEST;
import static wooteco.subway.SubwayFixtures.LINES_URI;
import static wooteco.subway.SubwayFixtures.SECONDLINE_REQUEST;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.ui.dto.request.StationRequest;
import wooteco.subway.ui.dto.response.ExceptionResponse;
import wooteco.subway.ui.dto.response.LineResponse;

@DisplayName("노선 E2E")
@Sql("classpath:/schema-test.sql")
class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setupStations() {
        final List<String> jsons = Stream.of("잠실역", "역삼역", "강남역", "대림역")
                .map(StationRequest::new)
                .map(this::toJson)
                .collect(Collectors.toList());

        for (String json : jsons) {
            post("/stations", json);
        }
    }

    @Test
    @DisplayName("노선 생성 요청 성공 시, 응답코드는 201 CREATED 이고 응답헤더에는 Location 이 있다")
    void createLine() {
        // given
        final String newLineRequestJson = toJson(BOONDANGLINE_REQUEST);

        // when
        final ExtractableResponse<Response> createResponse = post(LINES_URI, newLineRequestJson);
        final LineResponse lineResponse = createResponse.as(LineResponse.class);

        // then
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(createResponse.header("Location")).isNotBlank(),
                () -> assertThat(lineResponse.getName()).isEqualTo(BOONDANGLINE_REQUEST.getName()),
                () -> assertThat(lineResponse.getColor()).isEqualTo(BOONDANGLINE_REQUEST.getColor())
        );
    }

    @Test
    @DisplayName("중복되는 노선 이름으로 노선 생성 시도 시, 생성에 실패하고 응답코드는 BAD_REQUEST 이다 ")
    void createLineWithDuplicateName() {
        // given
        final String createRequestJson = toJson(BOONDANGLINE_REQUEST);
        final ExtractableResponse<Response> createResponse = post(LINES_URI, createRequestJson);

        // when
        final ExtractableResponse<Response> createDuplicateResponse = post(LINES_URI, createRequestJson);

        // then
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(createDuplicateResponse.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );
    }

    @Test
    @DisplayName("노선을 조회 시 응답코드는 OK이다")
    void getLines() {
        /// given
        final ExtractableResponse<Response> firstLineCreate = post(LINES_URI, toJson(BOONDANGLINE_REQUEST));
        final ExtractableResponse<Response> secondLineCreate = post(LINES_URI, toJson(SECONDLINE_REQUEST));

        // when
        final Long firstId = Long.parseLong(firstLineCreate.header("Location").replace("/lines/", ""));
        final Long secondId = Long.parseLong(secondLineCreate.header("Location").replace("/lines/", ""));
        final ExtractableResponse<Response> findAllLinesResponse = get(LINES_URI);

        List<Long> actualLineIds = findAllLinesResponse.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(firstLineCreate.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(secondLineCreate.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(findAllLinesResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actualLineIds).containsAll(List.of(firstId, secondId))
        );
    }

    @Test
    @DisplayName("노선을 제거 시 응답코드는 NO_CONTENT 이다. 존재하지 않는 노선 조회 시, 응답코드는 NOT_FOUND 이다")
    void deleteLine() {
        // given
        final ExtractableResponse<Response> createResponse = post(LINES_URI, toJson(BOONDANGLINE_REQUEST));

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> deleteResponse = delete(uri);
        final ExtractableResponse<Response> findResponseAfterDeletion = get(uri);
        final ExceptionResponse exceptionResponseForNotExists = findResponseAfterDeletion.jsonPath()
                .getObject(".", ExceptionResponse.class);

        // then
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(findResponseAfterDeletion.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value()),
                () -> assertThat(exceptionResponseForNotExists.getMessage()).contains("요청한 노선이 존재하지 않습니다")
        );
    }

    @Test
    @DisplayName("ID로 특정 단일 노선의 정보를 조회할 수 있으며, 응답코드는 OK이다")
    void getLineById() {
        /// given
        final ExtractableResponse<Response> createResponse = post(LINES_URI, toJson(BOONDANGLINE_REQUEST));
        final LineResponse expected = createResponse.jsonPath().getObject(".", LineResponse.class);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> findResponse = get(uri);
        final LineResponse actual = findResponse.jsonPath().getObject(".", LineResponse.class);

        // then
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    @Test
    @DisplayName("ID로 특정 노선의 정보를 갱신할 수 있으며, 정상 갱신 시 응답코드는 OK이다")
    void updateLine() {
        // given
        final ExtractableResponse<Response> createResponse = post(LINES_URI, toJson(BOONDANGLINE_REQUEST));
        final LineResponse expected = createResponse.jsonPath().getObject(".", LineResponse.class);

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> updateResponse = put(uri, toJson(SECONDLINE_REQUEST));
        final ExtractableResponse<Response> findAfterUpdateResponse = get(uri);
        final LineResponse actual = findAfterUpdateResponse.jsonPath().getObject(".", LineResponse.class);

        // then
        assertAll(
                () -> assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(updateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(findAfterUpdateResponse.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getName()).isEqualTo(SECONDLINE_REQUEST.getName()),
                () -> assertThat(actual.getColor()).isEqualTo(SECONDLINE_REQUEST.getColor())
        );
    }
}
