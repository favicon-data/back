package com.capstone.favicon.dataset.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="dataset")
public class Dataset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dataset_id")
    private Long datasetId;

    private String organization;
    private String title;
    private String description;
    private LocalDate createdDate;
    private LocalDate updateDate;
    private Integer view;
    private Integer download;
    private String license;
    private String keyword;
    private Boolean analysis;
    private String name;

    @ManyToOne
    @JoinColumn(name = "dataset_theme_id", nullable = false)
    private DatasetTheme datasetTheme;

    public Dataset(DatasetTheme datasetTheme, String name, String title, String organization) {
        this.datasetTheme = datasetTheme; // datasetTheme 객체를 설정
        this.name = name;
        this.title = title;
        this.organization = organization;
    }

    @OneToOne(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private Resource resource;

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Download> downloadSet;
}
