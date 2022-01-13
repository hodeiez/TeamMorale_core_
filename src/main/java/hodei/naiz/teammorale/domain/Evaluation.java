package hodei.naiz.teammorale.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:51
 * Project: TeamMorale
 * Copyright: MIT
 */
@Table
@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Evaluation {
    @Id
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private Long energy;
    @JsonProperty("user_teams")
    private Long userTeamsId;
    @JsonProperty("well_being")
    private Long wellBeing;
    private Long production;
    @JsonProperty("team_id")
    private Long teamId;
    @Transient
    private User user;
    @Transient
    private Team team;

    @CreatedDate
    @JsonProperty("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonProperty("modified_date")
    private LocalDateTime modifiedDate;
}
