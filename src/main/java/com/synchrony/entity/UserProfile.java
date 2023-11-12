package com.synchrony.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "user_profiles")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    public UserProfile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}