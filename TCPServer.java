
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private static Socket socket;

    public static void main(String[] args) {

        Book[] books = new Book[]{new Book("CR2018", "Java Complete Ref.", "3rd", 10, 15, 3),
            new Book("RDBMS06", "Data Base- SQL", "1st", 30, 10, 4),
            new Book("CR2018", "Object Oriented Programming", "2nd", 20, 7, 10)};

        try {

            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");

            //Server is running always. This is done using this while(true) loop
            while (true) {
                //Reading the message from the client
                System.out.println("\nWaiting for the new client request(s).....");
                socket = serverSocket.accept();
                System.out.println("Client request accepted \n");

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String bookIdAndName = br.readLine();
                System.out.println("Message received from client is " + bookIdAndName);

                //Multiplying the number by 2 and forming the return message
                String bookStatusMsg = "ERROR";
                String edition = null;
                int copies = 0;
                try {
                    String[] bookInfo = bookIdAndName.split(",");

                    for (int i = 0; i < books.length; i++) {
                        if (books[i].getBookId().equals(bookInfo[0]) && books[i].getBookName().equals(bookInfo[1])) {
                            bookStatusMsg = "BOOK FOUND";
                            copies = books[i].getNoOfCopies();
                            edition = books[i].getBookEdition();
                        }
                    }
                    // returnMessage = "returnMessage = " + returnMessage + "\n";
                } catch (Exception e) {
                    //Input was not a number. Sending proper message back to client.
                    bookStatusMsg = "Please send a proper book id & name\n";
                }

                //Sending the book status response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(bookStatusMsg +"\n");
                System.out.println("Message sent to the client is " + bookStatusMsg + "\n");
                bw.flush();

                if (!"ERROR".equals(bookStatusMsg)) {
                    InputStream is1 = socket.getInputStream();
                    InputStreamReader isr1 = new InputStreamReader(is1);
                    BufferedReader br1 = new BufferedReader(isr1);
                    String bookInfoChoice = br1.readLine();
                    System.out.println("Message received from client is " + bookInfoChoice);

                    String bookInfoMsg = null;
                    if ("1".equals(bookInfoChoice)) {
                        bookInfoMsg = edition+"\n";
                    } else if ("2".equals(bookInfoChoice)) {
                        bookInfoMsg = copies+"\n";
                    }

                    //Sending the final response back to the client.
                    OutputStream os1 = socket.getOutputStream();
                    OutputStreamWriter osw1 = new OutputStreamWriter(os1);
                    BufferedWriter bw1 = new BufferedWriter(osw1);
                    bw1.write(bookInfoMsg+"\n");
                    System.out.println("Book information sent to the client is " + bookInfoMsg);
                    bw1.flush();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }
}

class Book {

    private String bookId;
    private String bookName;
    private String bookEdition;
    private int NoOfCopies;
    private float unitPrice;
    private float discount;

    Book(String id, String name, String edition, int copies, float price, float discnt) {
        bookId = id;
        bookName = name;
        bookEdition = edition;
        NoOfCopies = copies;
        unitPrice = price;
        discount = discnt;
    }

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public int getNoOfCopies() {
        return NoOfCopies;
    }

    public String getBookEdition() {
        return bookEdition;
    }

}
