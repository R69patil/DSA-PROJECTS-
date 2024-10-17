
import java.util.*;

// Class to represent a Book
class Book {
    int bookID;
    String title;
    String author;
    String genre;
    boolean isAvailable;
    Date dueDate;

    // Constructor
    public Book(int bookID, String title, String author, String genre) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = true;
    }

    @Override
    public String toString() {
        return "ID: " + bookID + ", Title: " + title + ", Author: " + author + ", Genre: " + genre + ", Available: " + isAvailable +
                (isAvailable ? "" : ", Due Date: " + dueDate);
    }
}

// Class to represent a User (Patron)
class User {
    int userID;
    String name;
    String email;
    List<Book> borrowedBooks;

    // Constructor
    public User(int userID, String name, String email) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    // Check if user can borrow more books (limit is 5)
    public boolean canBorrow() {
        return borrowedBooks.size() < 5;
    }

    @Override
    public String toString() {
        return "User ID: " + userID + ", Name: " + name + ", Email: " + email;
    }
}

// Class to manage the Library
class Library {
    private List<Book> books = new ArrayList<>();
    private Map<Integer, User> users = new HashMap<>();
    private Map<Integer, Date> dueDates = new HashMap<>();

    // Method to add a new book
    public void addBook(Book book) {
        books.add(book);
        System.out.println("Added: " + book);
    }

    // Method to remove a book by ID
    public void removeBook(int bookID) {
        books.removeIf(book -> book.bookID == bookID);
        System.out.println("Removed book with ID: " + bookID);
    }

    // Method to display all books
    public void displayBooks() {
        System.out.println("Books in Library:");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // Method to register a new user
    public void registerUser(User user) {
        users.put(user.userID, user);
        System.out.println("Registered: " + user);
    }

    // Method for a user to borrow a book
    public void borrowBook(int userID, int bookID) {
        User user = users.get(userID);
        for (Book book : books) {
            if (book.bookID == bookID && book.isAvailable) {
                if (user.canBorrow()) {
                    book.isAvailable = false;
                    user.borrowedBooks.add(book);
                    // Set a due date (e.g., 14 days from now)
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 14);
                    book.dueDate = calendar.getTime();
                    dueDates.put(bookID, book.dueDate);
                    System.out.println(user.name + " borrowed: " + book.title + ". Due Date: " + book.dueDate);
                } else {
                    System.out.println("User has reached the limit of borrowed books.");
                }
                return;
            }
        }
        System.out.println("Book is not available or does not exist.");
    }

    // Method for a user to return a book
    public void returnBook(int userID, int bookID) {
        User user = users.get(userID);
        for (Book book : user.borrowedBooks) {
            if (book.bookID == bookID) {
                book.isAvailable = true;
                user.borrowedBooks.remove(book);
                dueDates.remove(bookID);
                System.out.println(user.name + " returned: " + book.title);
                return;
            }
        }
        System.out.println("This user did not borrow this book.");
    }

    // Method to search for books by title
    public void searchBookByTitle(String title) {
        System.out.println("Searching for books with title: " + title);
        for (Book book : books) {
            if (book.title.equalsIgnoreCase(title)) {
                System.out.println(book);
            }
        }
    }

    // Method to generate report on borrowed books
    public void generateReport() {
        System.out.println("Report on Borrowed Books:");
        for (User user : users.values()) {
            System.out.println(user.name + " has borrowed:");
            for (Book book : user.borrowedBooks) {
                System.out.println("  - " + book.title + " (Due Date: " + book.dueDate + ")");
            }
        }
    }

    // Method to allow guest registration
    public void registerGuest(int userID, String name, String email) {
        registerUser(new User(userID, name, email));
    }
}

// Main class to run the Library Management System
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Library manager's actions
        System.out.println("Library Manager: Adding books...");
        library.addBook(new Book(1, "1984", "George Orwell", "Dystopian"));
        library.addBook(new Book(2, "To Kill a Mockingbird", "Harper Lee", "Fiction"));
        library.addBook(new Book(3, "The Great Gatsby", "F. Scott Fitzgerald", "Classic"));

        // Displaying all books
        library.displayBooks();

        // Registering users
        System.out.println("\nEnter the number of users to register:");
        int numberOfUsers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numberOfUsers; i++) {
            System.out.println("Enter user ID:");
            int userID = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println("Enter user name:");
            String name = scanner.nextLine();
            System.out.println("Enter user email:");
            String email = scanner.nextLine();
            library.registerUser(new User(userID, name, email));
        }

        // Borrowing books
        System.out.println("\nEnter user ID to borrow a book:");
        int borrowUserID = scanner.nextInt();
        System.out.println("Enter book ID to borrow:");
        int borrowBookID = scanner.nextInt();
        library.borrowBook(borrowUserID, borrowBookID);

        // Returning books
        System.out.println("\nEnter user ID to return a book:");
        int returnUserID = scanner.nextInt();
        System.out.println("Enter book ID to return:");
        int returnBookID = scanner.nextInt();
        library.returnBook(returnUserID, returnBookID);

        // Searching for books
        System.out.println("\nEnter title to search for books:");
        scanner.nextLine(); // Consume newline
        String searchTitle = scanner.nextLine();
        library.searchBookByTitle(searchTitle);

        // Generating report
        library.generateReport();

        // Displaying all books after borrowing and returning
        library.displayBooks();

        scanner.close(); // Close the scanner to avoid resource leaks
    }
}
