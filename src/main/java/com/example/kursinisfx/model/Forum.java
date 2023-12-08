package com.example.kursinisfx.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Forum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private LocalDateTime postDateTime;
    private String author;
    @OneToMany(mappedBy = "parentForum", cascade = CascadeType.ALL)
    private List<Comment> comments;

    public Forum(String title, String description, LocalDateTime postDateTime, String author) {
        this.title = title;
        this.description = description;
        this.postDateTime = postDateTime;
        this.author = author;
        this.comments = new ArrayList<>();
    }
    public String toString(){
        return postDateTime + " " + author + " " + title;
    }
}