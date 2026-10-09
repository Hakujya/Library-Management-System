package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Centralized exception handler for MVC controllers.
 * Ensures friendly user-facing messages and strictly prevents exposing internal stack traces.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        log.warn("Resource not found: {}", ex.getMessage());
        model.addAttribute("errorCode", 404);
        model.addAttribute("errorTitle", "Record Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public String handleBookNotAvailable(BookNotAvailableException ex, RedirectAttributes redirectAttributes) {
        log.warn("Book not available: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/loans/issue";
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public String handleDuplicateResource(DuplicateResourceException ex, Model model) {
        log.warn("Duplicate resource violation: {}", ex.getMessage());
        model.addAttribute("errorCode", 409);
        model.addAttribute("errorTitle", "Duplicate Entry");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(InvalidLoanOperationException.class)
    public String handleInvalidLoanOperation(InvalidLoanOperationException ex, RedirectAttributes redirectAttributes) {
        log.warn("Invalid loan operation: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/loans";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unhandled application exception: {}", ex.getMessage(), ex);
        model.addAttribute("errorCode", 500);
        model.addAttribute("errorTitle", "System Notice");
        model.addAttribute("errorMessage", "An unexpected error occurred while processing your request. Please try again or contact the library administrator.");
        return "error/error";
    }
}

