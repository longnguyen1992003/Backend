package net.javaguides.mssql.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import net.javaguides.mssql.Enum.Role;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email_address", nullable = false)
    private String emailId;

    private Long age;

    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String account;

    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "employee",cascade = CascadeType.ALL)
    private List<RefreshToken> refreshToken;
}
