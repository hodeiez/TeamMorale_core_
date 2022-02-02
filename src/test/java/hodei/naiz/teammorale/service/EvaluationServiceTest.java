package hodei.naiz.teammorale.service;

import hodei.naiz.teammorale.domain.Evaluation;
import hodei.naiz.teammorale.domain.Team;
import hodei.naiz.teammorale.domain.User;
import hodei.naiz.teammorale.persistance.EvaluationRepo;
import hodei.naiz.teammorale.persistance.TeamRepo;
import hodei.naiz.teammorale.persistance.UserRepo;
import hodei.naiz.teammorale.presentation.mapper.EvaluationMapper;
import hodei.naiz.teammorale.presentation.mapper.resources.EvaluationResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


/**
 * Created by Hodei Eceiza
 * Date: 2/1/2022
 * Time: 21:48
 * Project: TeamMorale
 * Copyright: MIT
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EvaluationServiceTest {
    @Mock
    EvaluationRepo evaluationRepo;
    @Mock
    TeamRepo teamRepo;
    @Mock
    UserRepo userRepo;

    @Mock
   EvaluationMapper evaluationMapper;
    @Autowired
    private NotificationService notificationService;

    EvaluationService evaluationService;

    @BeforeAll
    public void setUp(){
       MockitoAnnotations.openMocks(this);
       evaluationService=new EvaluationService(evaluationRepo,teamRepo,userRepo,evaluationMapper,notificationService);
    }
    private Mono<Evaluation> mockEvaluation() {
        return Mono.just(new Evaluation()
                .setEnergy(10L)
                .setProduction(10L)
                .setWellBeing(10L)
                .setId(1L)
                .setUserTeamsId(1L)
                .setTeamId(1L)
                .setUserId(1L)
                .setCreatedDate(LocalDateTime.of(2022,1,1,1,1))
                .setModifiedDate(LocalDateTime.of(2022,1,1,1,1))
        .setUser(mockUser().block()).setTeam(mockTeam().block()));
    }
    private Evaluation mockEvaluationDTO() {
        return new Evaluation()
                .setEnergy(10L)
                .setProduction(10L)
                .setWellBeing(10L)
                .setUserTeamsId(1L)
                .setTeamId(1L)
                .setUserId(1L)
        ;   }
    private Mono<User> mockUser(){
        return Mono.just(new User().setId(1L).setUsername("userNameTest").setPassword("passwordTest").setEmail("emailTest"));
    }
    private Mono<Team> mockTeam(){
        return Mono.just(new Team().setId(1L).setName("nameTest"));
    }
    private EvaluationResource mockEvaluationResource(Long production,Long energy,Long wellBeing){
        var er=new EvaluationResource();
        er.setUsername("userNameTest");
        er.setTeam("nameTest");
        er.setProduction(production);
        er.setEnergy(energy);
        er.setWellBeing(wellBeing);
        er.setDate("today");
        er.setId(1L);
        return er;
    }
    @Test
    void createShouldReturnEventResource() {
        Mono<Evaluation> mock=mockEvaluation();
        Mono<Team> mockTeam=mockTeam();
        Mono<User> mockUser=mockUser();
        given(evaluationRepo.save(any(Evaluation.class))).willReturn(mock);
        given(evaluationRepo.evaluationExists(anyLong(),anyString())).willReturn(Mono.just(false));
        given(userRepo.getByUserTeamsId(anyLong())).willReturn(mockUser);
        given(teamRepo.getByUserTeamsId(anyLong())).willReturn(mockTeam);
        given(teamRepo.findById(anyLong())).willReturn(mockTeam);
        given(userRepo.findById(anyLong())).willReturn(mockUser);
        given(evaluationMapper.toEvaluationResource(any(Evaluation.class))).willReturn(mockEvaluationResource(10L,10L,10L));

        Mono<EvaluationResource> create= evaluationService.create(mockEvaluationDTO());
        StepVerifier.create(create).thenConsumeWhile(result->{
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(result,mockEvaluationResource(10L,10L,10L));

            return true;
        }).verifyComplete();
    } @Test
    void createShouldThrowExceptionWhenEvaluationExists() {
        Mono<Evaluation> mock=mockEvaluation();
        Mono<Team> mockTeam=mockTeam();
        Mono<User> mockUser=mockUser();
        given(evaluationRepo.save(any(Evaluation.class))).willReturn(mock);
        given(evaluationRepo.evaluationExists(anyLong(),anyString())).willReturn(Mono.just(true));
        given(userRepo.getByUserTeamsId(anyLong())).willReturn(mockUser);
        given(teamRepo.getByUserTeamsId(anyLong())).willReturn(mockTeam);
        given(teamRepo.findById(anyLong())).willReturn(mockTeam);
        given(userRepo.findById(anyLong())).willReturn(mockUser);
        given(evaluationMapper.toEvaluationResource(any(Evaluation.class))).willReturn(mockEvaluationResource(10L,10L,10L));

        Mono<EvaluationResource> create= evaluationService.create(mockEvaluationDTO());
        StepVerifier.create(create).thenConsumeWhile(result->{assertNotNull(result); return true;}).verifyError(IllegalArgumentException.class);
    }




    @Test
    void itShouldUpdate() {
        Mono<Evaluation> mock= mockEvaluation().map(e->e.setEnergy(1L).setProduction(1L).setWellBeing(1L));
        Mono<Team> mockTeam=mockTeam();
        Mono<User> mockUser=mockUser();
        given(teamRepo.findById(anyLong())).willReturn(mockTeam);
        given(userRepo.findById(anyLong())).willReturn(mockUser);
        given(evaluationMapper.toEvaluationResource(any(Evaluation.class))).willReturn(mockEvaluationResource(1L,1L,1L));
        given(evaluationRepo.findById(anyLong())).willReturn(mockEvaluation());
        given(evaluationRepo.save(any(Evaluation.class))).willReturn(mock);
        Mono<EvaluationResource> create= evaluationService.update(mockEvaluationDTO().setEnergy(1L).setProduction(1L).setWellBeing(1L).setId(1L));


     var expected=mockEvaluationResource(1L,1L,1L);


        StepVerifier.create(create).thenConsumeWhile(result->{
            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals(result,expected);
            return true;
        }).verifyComplete();
    }
    @Test
    void itShouldShowExceptionInUpdateWhenIdIsNull() {
        Mono<Evaluation> mock= mockEvaluation().map(e->e.setEnergy(1L).setProduction(1L).setWellBeing(1L));
        Mono<Team> mockTeam=mockTeam();
        Mono<User> mockUser=mockUser();
        given(teamRepo.findById(anyLong())).willReturn(mockTeam);
        given(userRepo.findById(anyLong())).willReturn(mockUser);
        given(evaluationMapper.toEvaluationResource(any(Evaluation.class))).willReturn(mockEvaluationResource(1L,1L,1L));
        given(evaluationRepo.findById(anyLong())).willReturn(mockEvaluation());
        given(evaluationRepo.save(any(Evaluation.class))).willReturn(mock);
        Mono<EvaluationResource> create= evaluationService.update(mockEvaluationDTO().setEnergy(1L).setProduction(1L).setWellBeing(1L).setId(null));



        StepVerifier.create(create).thenConsumeWhile(result->{assertNotNull(result); return true;}).verifyError(IllegalArgumentException.class);

    }


}
