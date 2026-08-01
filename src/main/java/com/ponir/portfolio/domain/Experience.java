package com.ponir.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "experiences")
public class Experience {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 200)
    private String company;
    @NotBlank @Size(max = 200)
    private String title;
    @Size(max = 180)
    private String location;
    @NotBlank @Size(max = 80)
    private String startLabel;
    @Size(max = 80)
    private String endLabel;
    @Column(name = "current_position")
    private boolean currentRole;
    @NotBlank
    @Column(columnDefinition = "text")
    private String summary;
    private int sortOrder;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getStartLabel() { return startLabel; }
    public void setStartLabel(String startLabel) { this.startLabel = startLabel; }
    public String getEndLabel() { return endLabel; }
    public void setEndLabel(String endLabel) { this.endLabel = endLabel; }
    public boolean isCurrentRole() { return currentRole; }
    public void setCurrentRole(boolean currentRole) { this.currentRole = currentRole; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
