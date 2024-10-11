import java.io.*;

// Author class definition
class Author {
    String familyName;
    String firstName;
    String nationality;
    String birthYear;

    public Author(String familyName, String firstName, String nationality, String birthYear) {
        this.familyName = familyName;
        this.firstName = firstName;
        this.nationality = nationality;
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return firstName + " " + familyName + " (" + nationality + ", " + birthYear + ")";
    }
}

// Book class definition
class Book {
    String title;
    String isbn;
    boolean isEbook;
    int yearPublished;
    int edition;
    Author[] authors;

    public Book(String title, String isbn, boolean isEbook, int yearPublished, int edition, Author[] authors) {
        this.title = title;
        this.isbn = isbn;
        this.isEbook = isEbook;
        this.yearPublished = yearPublished;
        this.edition = edition;
        this.authors = authors;
    }

    @Override
    public String toString() {
        String result = "Title: " + title + "\n" +
                        "ISBN: " + isbn + "\n" +
                        "eBook: " + (isEbook ? "Yes" : "No") + "\n" +
                        "Year Published: " + yearPublished + "\n" +
                        "Edition: " + edition + "\n" +
                        "Authors: ";

        for (int i = 0; i < authors.length; i++) {
            if (authors[i] != null) {
                result += authors[i].toString();
                if (i < authors.length - 1 && authors[i + 1] != null) {
                    result += "; ";
                }
            }
        }
        return result;
    }
}

// Library class definition
public class Library {
    private Book[] books = new Book[100]; // Maximum of 100 books
    private int bookCount = 0;
    private final String dataFile = "LibraryData.csv";

    public Library() {
        loadBooks();
    }

    // Load books from file
    private void loadBooks() {
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            br.readLine(); // skip first line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String title = parts[0];
                
                Author[] authors = new Author[3]; // Up to 3 authors
                int authorIndex = 0;

                for (int i = 1; i <= 9; i += 4) {
                    String authorFamilyName = parts[i];
                    String authorFirstName = parts[i + 1];
                    String nationality = parts[i + 2];
                    String birthYear = parts[i + 3];
                    authors[authorIndex++] = new Author(authorFamilyName, authorFirstName, nationality, birthYear);
                }

                int yearPublished = Integer.parseInt(parts[13]);
                String isbn = parts[14];
                boolean isEbook = Boolean.parseBoolean(parts[15]);
                int edition = Integer.parseInt(parts[16]);

                books[bookCount++] = new Book(title, isbn, isEbook, yearPublished, edition, authors);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading books.");
        }
    }

    // Save books to file
    private void saveBooks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile))) {
            pw.println("title,familyNameOne,firstNameOne,nationalityOne,birthYearOne,familyNameTwo,firstNameTwo,nationalityTwo,birthYearTwo,familyNameThree,firstNameThree,nationalityThree,birthYearThree,year,isbn,ebook,edition");
            
            for (int i = 0; i < bookCount; i++) {
                Book book = books[i];
                pw.print(book.title);

                for (int j = 0; j < book.authors.length; j++) {
                    if (book.authors[j] != null) {
                        pw.print("," + book.authors[j].familyName + "," + book.authors[j].firstName + "," + book.authors[j].nationality + "," + book.authors[j].birthYear);
                    }
                    else {
                        pw.print(",,,,");
                    }
                }

                pw.print("," + book.yearPublished + "," + book.isbn + "," + book.isEbook + "," + book.edition);
                pw.println();
            }
        } catch (IOException e) {
            System.out.println("Error saving books.");
        }
    }

    // View all books
    public void viewAllBooks() {
        for (int i = 0; i < bookCount; i++) {
            System.out.println(books[i]);
            System.out.println("-----------------------------------");
        }
    }

    // View eBooks only
    public void viewEBooks() {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].isEbook) {
                System.out.println(books[i]);
                System.out.println("-----------------------------------");
            }
        }
    }

    // View non-eBooks only
    public void viewNonEBooks() {
        for (int i = 0; i < bookCount; i++) {
            if (!books[i].isEbook) {
                System.out.println(books[i]);
                System.out.println("-----------------------------------");
            }
        }
    }

    // View books by author
    public void viewBooksByAuthor(String authorName) {
        for (int i = 0; i < bookCount; i++) {
            for (int j = 0; j < books[i].authors.length; j++) {
                if (books[i].authors[j] != null && 
                    (books[i].authors[j].familyName.equalsIgnoreCase(authorName) 
                    || books[i].authors[j].firstName.equalsIgnoreCase(authorName))) {

                    System.out.println(books[i]);
                    System.out.println("-----------------------------------");
                }
            }
        }
    }

    // Add a new book
    public void addBook() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter book title: ");
            String title = br.readLine();

            System.out.print("Enter ISBN: ");
            String isbn = br.readLine();

            System.out.print("Is this an eBook? (true/false): ");
            boolean isEbook = Boolean.parseBoolean(br.readLine());

            System.out.print("Enter year published: ");
            int yearPublished = Integer.parseInt(br.readLine());

            System.out.print("Enter edition: ");
            int edition = Integer.parseInt(br.readLine());

            Author[] authors = new Author[3];
            for (int i = 0; i < 3; i++) {
                System.out.print("Enter author's family name (or press enter to skip): ");
                String familyName = br.readLine();
                if (familyName.isEmpty()) {
                    break;
                }

                System.out.print("Enter author's first name: ");
                String firstName = br.readLine();

                System.out.print("Enter author's nationality: ");
                String nationality = br.readLine();

                System.out.print("Enter author's birth year: ");
                String birthYear = br.readLine();

                authors[i] = new Author(familyName, firstName, nationality, birthYear);
            }

            books[bookCount++] = new Book(title, isbn, isEbook, yearPublished, edition, authors);
            saveBooks();
            System.out.println("Book added successfully.");
        } catch (IOException e) {
            System.out.println("Error while adding book.");
        }
    }

    // Edit a book (by ISBN)
    public void editBook() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Enter ISBN of book to edit: ");
            String isbn = br.readLine();

            for (int i = 0; i < bookCount; i++) {
                if (books[i].isbn.equals(isbn)) {
                    System.out.println("Editing book: " + books[i]);

                    System.out.print("Enter new title (or press enter to keep current): ");
                    String newTitle = br.readLine();
                    if (!newTitle.isEmpty()) {
                        books[i].title = newTitle;
                    }

                    System.out.print("Is this an eBook? (true/false, or press enter to keep current): ");
                    String isEbookInput = br.readLine();
                    if (!isEbookInput.isEmpty()) {
                        books[i].isEbook = Boolean.parseBoolean(isEbookInput);
                    }

                    System.out.print("Enter new year published (or press enter to keep current): ");
                    String yearInput = br.readLine();
                    if (!yearInput.isEmpty()) {
                        books[i].yearPublished = Integer.parseInt(yearInput);
                    }

                    saveBooks();
                    System.out.println("Book updated successfully.");
                    return;
                }
            }

            System.out.println("Book with ISBN " + isbn + " not found.");
        } catch (IOException e) {
            System.out.println("Error while editing book.");
        }
    }

    // Display menu and process user input
    public void showMenu() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("************");
                System.out.println("Welcome to the Library");
                System.out.println("************");
                System.out.println("1 > View all Books");
                System.out.println("2 > View eBooks");
                System.out.println("3 > View non-eBooks");
                System.out.println("4 > View an author's Books");
                System.out.println("5 > Add Book");
                System.out.println("6 > Edit Book");
                System.out.println("7 > Exit");
                System.out.println("************");
                System.out.print("Your choice: ");
                int choice = Integer.parseInt(br.readLine());

                switch (choice) {
                    case 1:
                        viewAllBooks();
                        break;
                    case 2:
                        viewEBooks();
                        break;
                    case 3:
                        viewNonEBooks();
                        break;
                    case 4:
                        System.out.print("Enter Author Name: ");
                        String authorName = br.readLine();
                        viewBooksByAuthor(authorName);
                        break;
                    case 5:
                        addBook();
                        break;
                    case 6:
                        editBook();
                        break;
                    case 7:
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error while displaying menu.");
        }
    }

    public static void main(String[] args) {
        Library library = new Library();
        library.showMenu();
    }
}
