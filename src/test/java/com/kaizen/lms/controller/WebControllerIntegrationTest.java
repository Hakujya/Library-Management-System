package com.kaizen.lms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end integration tests verifying that MVC controllers,
 * model bindings, and Thymeleaf templates render with HTTP 200 without any syntax errors.
 */
@SpringBootTest
class WebControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Verify Dashboard renders correctly with statistics")
    void testDashboardPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("stats"))
                .andExpect(content().string(containsString("Library Overview")))
                .andExpect(content().string(containsString("Total Titles")));
    }

    @Test
    @DisplayName("Verify Books Catalog list renders properly")
    void testBooksListPage() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(content().string(containsString("Book Catalog")))
                .andExpect(content().string(containsString("Clean Architecture")));
    }

    @Test
    @DisplayName("Verify Book Creation Form renders with empty DTO and categories")
    void testBookCreateForm() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(content().string(containsString("Add Book to Catalog")));
    }

    @Test
    @DisplayName("Verify Book Details page renders with inventory info")
    void testBookDetailsPage() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/details"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("loans"))
                .andExpect(content().string(containsString("Catalog Information")));
    }

    @Test
    @DisplayName("Verify Member Directory page renders with registered members")
    void testMembersListPage() throws Exception {
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/list"))
                .andExpect(model().attributeExists("members"))
                .andExpect(content().string(containsString("Library Members")))
                .andExpect(content().string(containsString("Aarav Sharma")));
    }

    @Test
    @DisplayName("Verify Member Registration Form renders")
    void testMemberRegistrationForm() throws Exception {
        mockMvc.perform(get("/members/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/form"))
                .andExpect(model().attributeExists("member"))
                .andExpect(content().string(containsString("Register New Member")));
    }

    @Test
    @DisplayName("Verify Member Profile Details renders with borrowing history")
    void testMemberDetailsPage() throws Exception {
        mockMvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/details"))
                .andExpect(model().attributeExists("member"))
                .andExpect(model().attributeExists("loans"))
                .andExpect(content().string(containsString("Membership Profile")));
    }

    @Test
    @DisplayName("Verify Circulation Loans page renders with tabs")
    void testLoansListPage() throws Exception {
        mockMvc.perform(get("/loans"))
                .andExpect(status().isOk())
                .andExpect(view().name("loans/list"))
                .andExpect(model().attributeExists("loans"))
                .andExpect(content().string(containsString("Book Loans & Circulation")))
                .andExpect(content().string(containsString("Active Loans")));
    }

    @Test
    @DisplayName("Verify Book Issue Form renders with books and members dropdowns")
    void testLoansIssueForm() throws Exception {
        mockMvc.perform(get("/loans/issue"))
                .andExpect(status().isOk())
                .andExpect(view().name("loans/issue-form"))
                .andExpect(model().attributeExists("availableBooks"))
                .andExpect(model().attributeExists("activeMembers"))
                .andExpect(content().string(containsString("Issue Book to Member")));
    }

    @Test
    @DisplayName("Verify friendly error page renders when book is not found")
    void testResourceNotFoundRendersErrorPage() throws Exception {
        mockMvc.perform(get("/books/999999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/error"))
                .andExpect(model().attribute("errorCode", 404))
                .andExpect(content().string(containsString("Record Not Found")));
    }
}
