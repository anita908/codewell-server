package com.codewell.server.persistence.entity;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "courses")
public class CourseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "age_lower")
    private Integer ageLower;

    @Column(name = "age_upper")
    private Integer ageUpper;

    @Column(name = "price")
    private Double price;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private List<ChapterEntity> chapters;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getAgeLower()
    {
        return ageLower;
    }

    public void setAgeLower(Integer ageLower)
    {
        this.ageLower = ageLower;
    }

    public Integer getAgeUpper()
    {
        return ageUpper;
    }

    public void setAgeUpper(Integer ageUpper)
    {
        this.ageUpper = ageUpper;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public List<ChapterEntity> getChapters()
    {
        return chapters;
    }

    public void setChapters(List<ChapterEntity> chapters)
    {
        this.chapters = chapters;
    }

    public OffsetDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }
}
