package com.example.demo.controller;

import com.example.demo.dto.BookFormDto;
import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.enums.BookCategory;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.InvalidLoanOperationException;
import com.example.demo.service.BookService;
import com.example.demo.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller handling book catalog CRUD, search, and detail views.
 */
@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final LoanService loanService;

    public BookController(BookService bookService, LoanService loanService) {
        this.bookService = bookService;
        this.loanService = loanService;
    }

    @GetMapping
    public String listBooks(@RequestParam(value = "search", required = false) String search,
                            @RequestParam(value = "category", required = false) BookCategory category,
                            Model model) {
        List<Book> books;
        if (search != null && !search.trim().isEmpty()) {
            books = bookService.searchBooks(search);
        } else if (category != null) {
            books = bookService.getBooksByCategory(category);
        } else {
            books = bookService.getAllBooks();
        }

        model.addAttribute("books", books);
        model.addAttribute("categories", BookCategory.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("searchTerm", search);
        model.addAttribute("activeNav", "books");
        return "books/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookFormDto());
        model.addAttribute("categories", BookCategory.values());
        model.addAttribute("isEdit", false);
        model.addAttribute("pageTitle", "Add New Book");
        model.addAttribute("activeNav", "books");
        return "books/form";
    }

    @PostMapping
    public String createBook(@Valid @ModelAttribute("book") BookFormDto formDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", BookCategory.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Add New Book");
            model.addAttribute("activeNav", "books");
            return "books/form";
        }

        try {
            Book created = bookService.saveBook(formDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Book '%s' successfully added to catalog.", created.getTitle()));
            return "redirect:/books/" + created.getId();
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("isbn", "error.book", ex.getMessage());
            model.addAttribute("categories", BookCategory.values());
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "Add New Book");
            model.addAttribute("activeNav", "books");
            return "books/form";
        }
    }

    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        List<Loan> bookLoans = loanService.getLoansByBook(id);

        model.addAttribute("book", book);
        model.addAttribute("loans", bookLoans);
        model.addAttribute("activeNav", "books");
        return "books/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", bookService.toDto(book));
        model.addAttribute("categories", BookCategory.values());
        model.addAttribute("isEdit", true);
        model.addAttribute("pageTitle", "Edit Book");
        model.addAttribute("bookId", id);
        model.addAttribute("activeNav", "books");
        return "books/form";
    }

    @PostMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") Long id,
                             @Valid @ModelAttribute("book") BookFormDto formDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", BookCategory.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Book");
            model.addAttribute("bookId", id);
            model.addAttribute("activeNav", "books");
            return "books/form";
        }

        try {
            Book updated = bookService.updateBook(id, formDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Book '%s' updated successfully.", updated.getTitle()));
            return "redirect:/books/" + id;
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("isbn", "error.book", ex.getMessage());
            model.addAttribute("categories", BookCategory.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Book");
            model.addAttribute("bookId", id);
            model.addAttribute("activeNav", "books");
            return "books/form";
        } catch (InvalidLoanOperationException ex) {
            bindingResult.rejectValue("totalCopies", "error.book", ex.getMessage());
            model.addAttribute("categories", BookCategory.values());
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "Edit Book");
            model.addAttribute("bookId", id);
            model.addAttribute("activeNav", "books");
            return "books/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(id);
            String title = book.getTitle();
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage",
                    String.format("Book '%s' deleted successfully.", title));
        } catch (InvalidLoanOperationException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete book: " + ex.getMessage());
        }
        return "redirect:/books";
    }
}

