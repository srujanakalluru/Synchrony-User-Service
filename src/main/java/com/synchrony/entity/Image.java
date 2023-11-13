package com.synchrony.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Entity
public class Image implements Serializable {
    @Id
    private String id;
    private String title;
    private String type;
    @JsonProperty("deletehash")
    private String imageHash;
    private String link;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;
}