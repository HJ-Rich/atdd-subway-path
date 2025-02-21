package wooteco.subway.ui;

import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.service.PathService;
import wooteco.subway.service.dto.request.PathServiceRequest;
import wooteco.subway.service.dto.response.PathServiceResponse;
import wooteco.subway.ui.dto.request.PathRequest;
import wooteco.subway.ui.dto.response.PathResponse;

@RestController
@RequestMapping("/paths")
public class PathController {

    private final PathService pathService;

    public PathController(PathService pathService) {
        this.pathService = pathService;
    }

    @GetMapping
    public ResponseEntity<PathResponse> findPath(@ModelAttribute @Valid PathRequest pathRequest) {
        final PathServiceRequest pathServiceRequest = new PathServiceRequest(pathRequest.getSource(),
                pathRequest.getTarget(), pathRequest.getAge());
        final PathServiceResponse path = pathService.findPath(pathServiceRequest);

        return ResponseEntity.ok(PathResponse.from(path));
    }
}
