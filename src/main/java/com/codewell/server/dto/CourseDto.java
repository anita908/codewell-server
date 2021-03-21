package com.codewell.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto
{
    private Integer id;
    private String name;
    private Integer ageLower;
    private Integer ageUpper;
    private Float price;

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

    public Float getPrice()
    {
        return price;
    }

    public void setPrice(Float price)
    {
        this.price = price;
    }
}
