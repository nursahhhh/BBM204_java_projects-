public class Book {
        private String bookType;
        private int ID;
        private static int counter = 0;
        private boolean extended ;
        private String willReadInLibrary;
        public Book(String bookType) {
            String type = bookType.equals("P") ? "Printed" : "Handwritten";
            this.bookType = type;
            counter++;
            if (counter == 1000000)throw new Error();
            this.ID = counter;
            extended = false;
            this.willReadInLibrary="NO";

        }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public int getId() {
            return this.ID;

        }

    public String getBookType() {
        return bookType;
    }

    public String getWillReadInLibrary() {
        return willReadInLibrary;
    }

    public void setWillReadInLibrary(String willReadInLibrary) {
        this.willReadInLibrary = willReadInLibrary;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookType='" + bookType + '\'' +
                ", ID=" + ID +
                '}';
    }
}


