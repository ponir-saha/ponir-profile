package com.ponir.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "site_profile")
public class SiteProfile {
    @Id
    private Long id = 1L;

    @NotBlank @Size(max = 160)
    private String fullName;
    @NotBlank @Size(max = 300)
    private String headline;
    @NotBlank @Size(max = 160)
    private String heroEyebrow;
    @NotBlank @Size(max = 320)
    private String heroTitle;
    @NotBlank
    @Column(columnDefinition = "text")
    private String introduction;
    @NotBlank
    @Column(columnDefinition = "text")
    private String about;
    @NotBlank @Email @Size(max = 180)
    private String email;
    @Size(max = 80)
    private String phone;
    @Size(max = 180)
    private String location;
    @Size(max = 500)
    private String linkedinUrl;
    @Size(max = 500)
    private String githubUrl;
    @Size(max = 500)
    private String portraitUrl;
    @Size(max = 500)
    private String resumeUrl;
    @Size(max = 240)
    private String availability;
    @Min(0)
    private int yearsExperience;
    @Min(0)
    private int projectsDelivered;
    @Min(0)
    private int engineersMentored;
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }
    public String getHeroEyebrow() { return heroEyebrow; }
    public void setHeroEyebrow(String heroEyebrow) { this.heroEyebrow = heroEyebrow; }
    public String getHeroTitle() { return heroTitle; }
    public void setHeroTitle(String heroTitle) { this.heroTitle = heroTitle; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public void setLinkedinUrl(String linkedinUrl) { this.linkedinUrl = linkedinUrl; }
    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }
    public String getPortraitUrl() { return portraitUrl; }
    public void setPortraitUrl(String portraitUrl) { this.portraitUrl = portraitUrl; }
    public String getResumeUrl() { return resumeUrl; }
    public void setResumeUrl(String resumeUrl) { this.resumeUrl = resumeUrl; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public int getYearsExperience() { return yearsExperience; }
    public void setYearsExperience(int yearsExperience) { this.yearsExperience = yearsExperience; }
    public int getProjectsDelivered() { return projectsDelivered; }
    public void setProjectsDelivered(int projectsDelivered) { this.projectsDelivered = projectsDelivered; }
    public int getEngineersMentored() { return engineersMentored; }
    public void setEngineersMentored(int engineersMentored) { this.engineersMentored = engineersMentored; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
