package wooteco.subway.service.dto.request;

public class LineServiceRequest {
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
    private Long extraFare;

    public LineServiceRequest(Long id, String name, String color, Long upStationId, Long downStationId, Long distance,
                              Long extraFare) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public LineServiceRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this(null, name, color, upStationId, downStationId, distance, 0L);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getExtraFare() {
        return extraFare;
    }
}
