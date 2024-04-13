package com.example.progettopsw.modules;
import com.example.progettopsw.token.Token;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "client")
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "id")
    private long id;

    @Basic
    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @NotBlank
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @NotBlank
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Client(String email, String firstName, String lastName) {
        this.email= email;
        this.firstName=firstName;
        this.lastName=lastName;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // se fosse false non riusciremmo a far connettere i nostri client
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // se fosse false non riusciremmo a far connettere i nostri client
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @OneToMany(mappedBy = "client")
    private List<Token> tokens;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Order> orders = new LinkedList<>();

    @OneToOne(mappedBy = "client", cascade=CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

}

