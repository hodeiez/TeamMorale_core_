package hodei.naiz.teammorale.presentation;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import hodei.naiz.teammorale.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 11:03
 * Project: TeamMorale
 * Copyright: MIT
 */
@RestController
@RequestMapping("evaluation")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @PostMapping
    public Mono<EvaluationResource> create(@RequestBody Evaluation evaluation) {
        return evaluationService.create(evaluation);
    }

    @PutMapping
    public  Mono<ResponseEntity<EvaluationResource>> update(@RequestBody Evaluation evaluation) {
        return evaluationService.update(evaluation)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<EvaluationResource> getAll() {
        return evaluationService.getAll();
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<EvaluationResource>> deleteById(@PathVariable("id") Long id) {
        return evaluationService.delete(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
