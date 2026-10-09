package com.example.demo.entity;

import com.example.demo.enums.MemberStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a registered library member.
 */
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @NotBlank(message = "Membership number is required")
    @Column(name = "membership_number", nullable = false, unique = true, length = 50)
    private String membershipNumber;

    @NotBlank(message = "Member name is required")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false, length = 30)
    private String phone;

    @NotNull(message = "Registration date is required")
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @NotNull(message = "Member status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MemberStatus status = MemberStatus.ACTIVE;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    public Member() {
    }

    public Member(String membershipNumber, String name, String email, String phone,
                  LocalDate registrationDate, MemberStatus status) {
        this.membershipNumber = membershipNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.status = status;
    }

    public boolean isActive() {
        return this.status == MemberStatus.ACTIVE;
    }

    // Getters and Setters
    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}

