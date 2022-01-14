package hodei.naiz.teammorale.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Created by Hodei Eceiza
 * Date: 1/4/2022
 * Time: 10:16
 * Project: TeamMorale
 * Copyright: MIT
 */
@Table("public.user")
@Data
@Accessors(chain = true)
@NoArgsConstructor
public class User {
    @Id
    private Long id;
    private String username;
    private String email;
    @Getter(onMethod_=@JsonIgnore)
    private String password;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
