import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class library {
    protected ArrayList<Book> Library;
    protected ArrayList<LibraryMember> Members;
    protected HashMap<LibraryMember, HashMap<Book, LocalDate>> BarrowedBooks;

    public library() {
        Library = new ArrayList<>();
        Members = new ArrayList<>();
        BarrowedBooks = new HashMap<>();
    }
    public void ReadCommand(String[] Lines) {
        for (String line : Lines) {
            String[] Words = line.split("\t");
            switch (Words[0]) {
                case "addBook":
                    addBooks(Words[1]);
                    break;
                case "addMember":
                    AddMembers(Words[1]);
                    break;
                case "borrowBook":
                    int bookId = Integer.parseInt(Words[1]);
                    int memberId = Integer.parseInt(Words[2]);
                    LocalDate date = LocalDate.parse(Words[3]);
                    barrowBooks(memberId, bookId, date);
                    break;
                case "returnBook":
                    ReturnBook(line);
                    break;
                case "extendBook":
                    ExtendDate(line);
                    break;
                case "readInLibrary":
                    ReadInLibrary(line);
                    break;
                case "getTheHistory":
                    promptHistory();
                    break;
                default:
                    break;
            }
        }
    }
    public void addBooks(String bookType) {
        Book book = new Book(bookType);
        Library.add(book);
        IOClass.writeToOutputFile(String.format("Created new book: %s [id: %d]", book.getBookType(), book.getId()));
    }
    public void AddMembers(String MemberType) {
        LibraryMember member = new LibraryMember(MemberType);
        Members.add(member);
        IOClass.writeToOutputFile(String.format("Created new member: %s [id: %d]", member.getMemberType(), member.getID()));
    }
    public void barrowBooks(int memberId, int bookId, LocalDate date) {
        LibraryMember CurrentMember = FindMemberInLibrary(memberId);
        Book currentBook = FindBookInLibrary(bookId);
        BarrowedBooks.putIfAbsent(CurrentMember, new HashMap<>());

        if (CurrentMember == null) {
            IOClass.writeToOutputFile("You are not a member of library!");
        }
        else if ((currentBook == null)) IOClass.writeToOutputFile("Library does not have the book you ask!");
        else if (currentBook.getBookType().equals("HandWritten")) IOClass.writeToOutputFile("You cannot borrow this book");
        else if ((CurrentMember.getMemberType().equals("Student") && BarrowedBooks.get(CurrentMember).size() < 2)) {
                BarrowedBooks.get(CurrentMember).put(currentBook, date);
                IOClass.writeToOutputFile(String.format("The book [%d] was borrowed by member [%d] at %s", bookId, memberId, date));
        }
        else if((CurrentMember.getMemberType().equals("Academic")) && (BarrowedBooks.get(CurrentMember).size() <4)){
            BarrowedBooks.putIfAbsent(CurrentMember, new HashMap<>());
            BarrowedBooks.get(CurrentMember).put(currentBook, date);
            IOClass.writeToOutputFile(String.format("The book [%d] was borrowed by member [%d] at %s", bookId, memberId, date));
        } else {
            IOClass.writeToOutputFile("You have exceeded the borrowing limit!");
        }
    }

    public void ReturnBook(String line) {
        String[] words = line.split("\t");
        int memberId = Integer.parseInt(words[2]);
        int bookId = Integer.parseInt(words[1]);
        LocalDate leftBook = LocalDate.parse(words[3]);
        LibraryMember CurrentMember = FindMemberInLibrary(memberId);
        Book returnedBook = FindBookInLibrary(bookId);
        if ((BarrowedBooks.containsKey(CurrentMember)) && (BarrowedBooks.get(CurrentMember).containsKey(returnedBook))) {
            int timeLimit = CurrentMember.getMemberType().equals("Student") ? 7 : 14;
            LocalDate getBook = BarrowedBooks.get(CurrentMember).get(returnedBook);
            long daysBetween = ChronoUnit.DAYS.between(getBook, leftBook);
            int fee = Math.max((int) daysBetween - timeLimit, 0);
            if (fee > 0) IOClass.writeToOutputFile("You must pay a penalty!");
            returnedBook.setExtended(false);
            returnedBook.setWillReadInLibrary("NO");
            BarrowedBooks.get(CurrentMember).remove(returnedBook);
            IOClass.writeToOutputFile(String.format("The book [%d] was returned by member [%d] at %s Fee: %d", bookId, memberId, words[3], fee));
        }
    }
    public void ExtendDate(String Line) {
        String[] words = Line.split("\t");
        int memberId = Integer.parseInt(words[2]);
        int bookId = Integer.parseInt(words[1]);
        Book currentBook = FindBookInLibrary(bookId);
        LibraryMember CurrentMember = FindMemberInLibrary(memberId);
        LocalDate newDate = LocalDate.parse(words[3]);
        LocalDate oldDate = BarrowedBooks.get(CurrentMember).get(currentBook);
        if (newDate.isAfter(oldDate) && (!currentBook.isExtended())) {
            BarrowedBooks.get(CurrentMember).put(currentBook, newDate);
            currentBook.setExtended(true);
            IOClass.writeToOutputFile(String.format("The deadline of book [%d] was extended by member [%d] at %s" , bookId,memberId,oldDate));
            IOClass.writeToOutputFile(String.format("New deadline of book [%d] is %s",bookId , newDate));
        }else{
            IOClass.writeToOutputFile("You cannot extend the deadline!");
        }
    }
    public void ReadInLibrary(String Line) {
        String[] words = Line.split("\t");
        int memberId = Integer.parseInt(words[2]);
        int bookId = Integer.parseInt(words[1]);
        LocalDate date = LocalDate.parse(words[3]);
        Book ReadingBook = FindBookInLibrary(bookId);
        LibraryMember currentMember = FindMemberInLibrary(memberId);
        BarrowedBooks.putIfAbsent(currentMember, new HashMap<>());
        if (currentMember == null) IOClass.writeToOutputFile("You are not a member of Library!");
        else if (ReadingBook == null) IOClass.writeToOutputFile("Library does not have the book you ask!");
        else if (isBookTaken(ReadingBook)){
            IOClass.writeToOutputFile("You can not read this book!");
        } else if ((currentMember.getMemberType().equals("Student")) && ((ReadingBook).getBookType().equals("Handwritten"))) {
            IOClass.writeToOutputFile("Students can not read handwritten books!");
        } else {
            ReadingBook.setWillReadInLibrary("YES");
            BarrowedBooks.putIfAbsent(currentMember, new HashMap<>());
            BarrowedBooks.get(currentMember).put(ReadingBook, date);
            IOClass.writeToOutputFile(String.format("The book [%d] was read in library by member [%s] at %s ", bookId, memberId, words[3]));
        }
    }
    public void promptHistory() {
        IOClass.writeToOutputFile("History of library:");
        List<LibraryMember> Students = Members.stream()
                .filter(member -> member.getMemberType().equals("Student")).collect(Collectors.toList());
        IOClass.writeToOutputFile(String.format("\nNumber of students: %d", Students.size()));
        for (LibraryMember m : Students) {
            IOClass.writeToOutputFile(String.format("Student [id: %d]", m.getID()));
        }
        List<LibraryMember> Academics = Members.stream()
                .filter(member -> member.getMemberType().equals("Academic")).collect(Collectors.toList());
        IOClass.writeToOutputFile(String.format("\nNumber of academics: %d", Academics.size()));
        for (LibraryMember m : Academics) {
            IOClass.writeToOutputFile(String.format("Academic [id: %d]", m.getID()));
        }
        List<Book> PrintedBooks = Library.stream()
                .filter(book -> book.getBookType().equals("Printed")).collect(Collectors.toList());
        IOClass.writeToOutputFile(String.format("\nNumber of printed books: %d", PrintedBooks.size()));
        for (Book b : PrintedBooks) {
            IOClass.writeToOutputFile(String.format("Printed [id: %d]", b.getId()));
        }
        List<Book> HandWrittenBooks = Library.stream()
                .filter(book -> book.getBookType().equals("Handwritten")).collect(Collectors.toList());
        IOClass.writeToOutputFile(String.format("\nNumber of handwritten books: %d", HandWrittenBooks.size()));
        for (Book b : HandWrittenBooks) {
            IOClass.writeToOutputFile(String.format("Handwritten [id: %d]", b.getId()));
        }
        ArrayList<String> Borrowed = new ArrayList<>();
        ArrayList<String> ReadInLibraryBooks = new ArrayList<>();
        for (Map.Entry<LibraryMember, HashMap<Book, LocalDate>> memberEntry : BarrowedBooks.entrySet()) {
            HashMap<Book, LocalDate> bookMap = memberEntry.getValue();
            for (Map.Entry<Book, LocalDate> bookEntry : bookMap.entrySet()) {
                Book book = bookEntry.getKey();
                if (book.getWillReadInLibrary().equals("NO")) {
                    Borrowed.add(String.format("The book [%d] was borrowed by member [%d] at %s", book.getId(),memberEntry.getKey().getID(), bookMap.get(book)).toString());
                }
                if (book.getWillReadInLibrary().equals("YES")) {
                    ReadInLibraryBooks.add(String.format("The book [%d] was read in library by member [%d] at %s", book.getId(),memberEntry.getKey().getID(), bookMap.get(book)).toString());
                }
            }
        }
        IOClass.writeToOutputFile(String.format("\nNumber of borrowed books: %d", Borrowed.size()));
        Borrowed.stream().forEach(IOClass::writeToOutputFile);
        IOClass.writeToOutputFile(String.format("\nNumber of books read in library: %d", ReadInLibraryBooks.size()));
        ReadInLibraryBooks.stream().forEach(IOClass::writeToOutputFile);
    }

        public Book FindBookInLibrary(int Id) {
        for (Book book : Library) {
            if (book.getId() == (Id)) {
                return book;
            }
        }return null;
    }
    public LibraryMember FindMemberInLibrary(int Id) {
        for (LibraryMember member : Members) {
            if (member.getID() == (Id)) {
                return member;
            }
        }return null;
    }
    public boolean isBookTaken(Book book){
        for (Map.Entry<LibraryMember, HashMap<Book, LocalDate>> memberEntry : BarrowedBooks.entrySet()){
            HashMap<Book, LocalDate> bookMap = memberEntry.getValue();
            if(bookMap.containsKey(book)) return true;
        } return false;
    }
}

