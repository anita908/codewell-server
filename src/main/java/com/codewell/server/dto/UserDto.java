package com.codewell.server.dto;

import java.time.OffsetDateTime;

public class UserDto
{
    private Integer id;
    private String userId;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Integer age;
    private String city;
    private String isAdmin;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private Integer id;
        private String userId;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Integer age;
        private String city;
        private String isAdmin;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;

        public Builder id(Integer id)
        {
            this.id = id;
            return this;
        }

        public Builder userId(String userId)
        {
            this.userId = userId;
            return this;
        }

        public Builder username(String username)
        {
            this.username = username;
            return this;
        }

        public Builder email(String email)
        {
            this.email = email;
            return this;
        }

        public Builder password(String password)
        {
            this.password = password;
            return this;
        }

        public Builder firstName(String firstName)
        {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName)
        {
            this.lastName = lastName;
            return this;
        }

        public Builder age(Integer age)
        {
            this.age = age;
            return this;
        }

        public Builder city(String city)
        {
            this.city = city;
            return this;
        }

        public Builder isAdmin(String isAdmin)
        {
            this.isAdmin = isAdmin;
            return this;
        }

        public Builder createdAt(OffsetDateTime createdAt)
        {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(OffsetDateTime updatedAt)
        {
            this.updatedAt = updatedAt;
            return this;
        }

        public UserDto build()
        {
            final UserDto userDto = new UserDto();
            userDto.setId(this.id);
            userDto.setUserId(this.userId);
            userDto.setUsername(this.username);
            userDto.setEmail(this.email);
            userDto.setPassword(this.password);
            userDto.setFirstName(this.firstName);
            userDto.setLastName(this.lastName);
            userDto.setAge(this.age);
            userDto.setCity(this.city);
            userDto.setIsAdmin(this.isAdmin);
            userDto.setCreatedAt(this.createdAt);
            userDto.setUpdatedAt(this.updatedAt);
            return userDto;
        }
    }


    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Integer getAge()
    {
        return age;
    }

    public void setAge(Integer age)
    {
        this.age = age;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin)
    {
        this.isAdmin = isAdmin;
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
