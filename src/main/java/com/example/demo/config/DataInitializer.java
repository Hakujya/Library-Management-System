package com.example.demo.config;

import com.example.demo.entity.Book;
import com.example.demo.entity.Loan;
import com.example.demo.entity.Member;
import com.example.demo.enums.BookCategory;
import com.example.demo.enums.LoanStatus;
import com.example.demo.enums.MemberStatus;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Development Data Initializer.
 * Seeds realistic college-level catalog, member, and circulation records
 * on first startup when app.seed-sample-data is enabled and database is empty.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    @Value("${app.seed-sample-data:true}")
    private boolean seedData;

    public DataInitializer(BookRepository bookRepository,
                           MemberRepository memberRepository,
                           LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public void run(String... args) {
        if (!seedData) {
            log.info("Sample data initialization is disabled (app.seed-sample-data=false).");
            return;
        }

        if (bookRepository.count() > 0) {
            log.info("Catalog already contains {} books; skipping sample data seeding.", bookRepository.count());
            return;
        }

        log.info("Seeding initial library catalog, members, and circulation records...");

        // 1. Seed Books
        List<Book> books = new ArrayList<>();

        Book b1 = createBook(
                "Clean Architecture: A Craftsman's Guide",
                "Robert C. Martin",
                "978-0134494166",
                BookCategory.COMPUTER_SCIENCE,
                "Prentice Hall",
                2017,
                5,
                4,
                "Shelf CS-101",
                "Practical rules for crafting lasting software architectures and system boundaries."
        );
        books.add(b1);

        Book b2 = createBook(
                "Introduction to Algorithms (CLRS)",
                "Thomas H. Cormen, Charles E. Leiserson",
                "978-0262033848",
                BookCategory.COMPUTER_SCIENCE,
                "MIT Press",
                2022,
                4,
                2,
                "Shelf CS-102",
                "Comprehensive reference text on data structures, graph theory, and algorithmic complexity."
        );
        books.add(b2);

        Book b3 = createBook(
                "Effective Java (3rd Edition)",
                "Joshua Bloch",
                "978-0134685991",
                BookCategory.COMPUTER_SCIENCE,
                "Addison-Wesley Professional",
                2018,
                3,
                2,
                "Shelf CS-103",
                "The definitive guide to best practices and idiomatic design patterns in Java."
        );
        books.add(b3);

        Book b4 = createBook(
                "The Pragmatic Programmer",
                "David Thomas, Andrew Hunt",
                "978-0135957059",
                BookCategory.COMPUTER_SCIENCE,
                "Addison-Wesley",
                2019,
                4,
                4,
                "Shelf CS-104",
                "Your journey to mastery in software craftsmanship, testing, and continuous learning."
        );
        books.add(b4);

        Book b5 = createBook(
                "A Brief History of Time",
                "Stephen Hawking",
                "978-0553380163",
                BookCategory.SCIENCE,
                "Bantam Books",
                1998,
                3,
                3,
                "Shelf SCI-201",
                "Explores black holes, the big bang, general relativity, and the nature of space-time."
        );
        books.add(b5);

        Book b6 = createBook(
                "Sapiens: A Brief History of Humankind",
                "Yuval Noah Harari",
                "978-0062316097",
                BookCategory.HISTORY,
                "Harper",
                2015,
                4,
                3,
                "Shelf HIST-302",
                "Explores how biology and history have shaped modern humanity over millennia."
        );
        books.add(b6);

        Book b7 = createBook(
                "Meditations",
                "Marcus Aurelius",
                "978-0140449334",
                BookCategory.PHILOSOPHY,
                "Penguin Classics",
                2006,
                2,
                2,
                "Shelf PHIL-401",
                "Private philosophical reflections and stoic principles from the Roman emperor."
        );
        books.add(b7);

        Book b8 = createBook(
                "Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                "978-0201633610",
                BookCategory.COMPUTER_SCIENCE,
                "Addison-Wesley",
                1994,
                3,
                1,
                "Shelf CS-105",
                "Foundational catalogue of 23 classic software design patterns for object-oriented engineering."
        );
        books.add(b8);

        bookRepository.saveAll(books);

        // 2. Seed Members
        List<Member> members = new ArrayList<>();

        Member m1 = new Member(
                "MEM-2026-1001",
                "Aarav Sharma",
                "aarav.sharma@college.edu",
                "+91 98765 43210",
                LocalDate.now().minusMonths(6),
                MemberStatus.ACTIVE
        );
        m1.setNotes("Computer Science Undergraduate, 3rd Year");
        members.add(m1);

        Member m2 = new Member(
                "MEM-2026-1002",
                "Priya Patel",
                "priya.patel@college.edu",
                "+91 98765 12345",
                LocalDate.now().minusMonths(4),
                MemberStatus.ACTIVE
        );
        m2.setNotes("Information Technology Graduate Scholar");
        members.add(m2);

        Member m3 = new Member(
                "MEM-2026-1003",
                "Dr. Vikram Malhotra",
                "vikram.malhotra@college.edu",
                "+91 98111 22334",
                LocalDate.now().minusYears(1),
                MemberStatus.ACTIVE
        );
        m3.setNotes("Faculty - Department of Computer Engineering");
        members.add(m3);

        Member m4 = new Member(
                "MEM-2026-1004",
                "Sneha Rao",
                "sneha.rao@college.edu",
                "+91 98222 33445",
                LocalDate.now().minusMonths(2),
                MemberStatus.ACTIVE
        );
        m4.setNotes("Physics & Mathematics Student");
        members.add(m4);

        Member m5 = new Member(
                "MEM-2026-1005",
                "Rohan Verma",
                "rohan.verma@college.edu",
                "+91 98333 44556",
                LocalDate.now().minusMonths(8),
                MemberStatus.ACTIVE
        );
        m5.setNotes("Mechanical Engineering, 2nd Year");
        members.add(m5);

        memberRepository.saveAll(members);

        // 3. Seed Circulation Loans (Active, Overdue, Returned)
        List<Loan> loans = new ArrayList<>();

        // Loan 1: Active loan, due in 7 days
        Loan l1 = new Loan(b1, m1, LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
        l1.setStatus(LoanStatus.ISSUED);
        l1.setNotes("Standard borrow for course project");
        loans.add(l1);

        // Loan 2: Active loan, due in 3 days
        Loan l2 = new Loan(b2, m2, LocalDate.now().minusDays(11), LocalDate.now().plusDays(3));
        l2.setStatus(LoanStatus.ISSUED);
        l2.setNotes("Reference material for algorithms midterm");
        loans.add(l2);

        // Loan 3: OVERDUE loan! Issued 21 days ago, due 7 days ago!
        Loan l3 = new Loan(b2, m5, LocalDate.now().minusDays(21), LocalDate.now().minusDays(7));
        l3.setStatus(LoanStatus.ISSUED);
        l3.setNotes("Automated overdue reminder sent to student email");
        loans.add(l3);

        // Loan 4: Active loan, due in 10 days
        Loan l4 = new Loan(b3, m3, LocalDate.now().minusDays(4), LocalDate.now().plusDays(10));
        l4.setStatus(LoanStatus.ISSUED);
        l4.setNotes("Faculty course prep reference");
        loans.add(l4);

        // Loan 5: Active loan, due in 5 days
        Loan l5 = new Loan(b8, m4, LocalDate.now().minusDays(9), LocalDate.now().plusDays(5));
        l5.setStatus(LoanStatus.ISSUED);
        l5.setNotes("Design patterns study");
        loans.add(l5);

        // Loan 6: Active loan, due in 12 days
        Loan l6 = new Loan(b8, m1, LocalDate.now().minusDays(2), LocalDate.now().plusDays(12));
        l6.setStatus(LoanStatus.ISSUED);
        l6.setNotes("Assigned reading");
        loans.add(l6);

        // Loan 7: Returned loan
        Loan l7 = new Loan(b6, m2, LocalDate.now().minusDays(30), LocalDate.now().minusDays(16));
        l7.setReturnDate(LocalDate.now().minusDays(17));
        l7.setStatus(LoanStatus.RETURNED);
        l7.setNotes("Returned on time in pristine condition");
        loans.add(l7);

        // Loan 8: Returned loan
        Loan l8 = new Loan(b5, m4, LocalDate.now().minusDays(25), LocalDate.now().minusDays(11));
        l8.setReturnDate(LocalDate.now().minusDays(12));
        l8.setStatus(LoanStatus.RETURNED);
        l8.setNotes("Returned on time");
        loans.add(l8);

        loanRepository.saveAll(loans);

        log.info("Sample library data successfully populated: {} books, {} members, {} circulation loans.",
                bookRepository.count(), memberRepository.count(), loanRepository.count());
    }

    private Book createBook(String title, String author, String isbn, BookCategory category,
                            String publisher, Integer year, int total, int available,
                            String shelf, String description) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setPublicationYear(year);
        book.setTotalCopies(total);
        book.setAvailableCopies(available);
        book.setLocationShelf(shelf);
        book.setDescription(description);
        return book;
    }
}

