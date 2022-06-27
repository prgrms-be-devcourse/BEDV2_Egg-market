package com.devcourse.eggmarket.domain.evaluation.api;

import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationRequest;
import com.devcourse.eggmarket.domain.evaluation.dto.EvaluationResponse;
import com.devcourse.eggmarket.domain.evaluation.service.EvaluationService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<Long> save(@RequestBody EvaluationRequest.Save request) {
        Long evaluationId = evaluationService.save(request);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/reviewer/{id}")
            .buildAndExpand(evaluationId)
            .toUri();

        return ResponseEntity.created(location)
            .body(evaluationId);
    }

    // TO DO:
//    @GetMapping
//    @RequestMapping("{/id}")
//    public ResponseEntity<List<EvaluationResponse>> getListByRevieweeId(@PathVariable Long id, Pageable pageable) {
//        evaluationService.getListByRevieweeId(id);
//        return ResponseEntity.ok();
//    }

    @GetMapping
    @RequestMapping("/reviewer/{id}")
    public ResponseEntity<EvaluationResponse> getByReviewerId(@PathVariable Long id) {
        EvaluationResponse review = evaluationService.getByReviewerId(id);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    @RequestMapping("/reviewee/{id}")
    public ResponseEntity<EvaluationResponse> getByRevieweeId(@PathVariable Long id) {
        EvaluationResponse review = evaluationService.getByRevieweeId(id);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        evaluationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
