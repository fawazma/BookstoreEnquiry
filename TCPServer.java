
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TCPServer implements Runnable {

	private Book[] books = new Book[] { new Book("CR2018", "Java Complete Ref.", "3rd", 10, 15.00f, 3),
			new Book("RDBMS06", "Data Base SQL", "1st", 30, 10.00f, 4),
			new Book("GEOG23", "Atlantic Geography", "4th", 54, 69.99f, 8),
			new Book("CPSC353", "Advanced Data Communications", "9th", 12, 45.00f, 4),
			new Book("COMPSCI8", "Computer Science III", "6th", 23, 19.99f, 0),
			new Book("PHILOS666", "Philosophy of Religion", "6th", 43, 15.99f, 3),
			new Book("NUTRI3", "Organic Nutrition", "3rd", 55, 9.99f, 0),
			new Book("FDESIGN22", "Classic Fashion Design", "1st", 34, 32.40f, 5),
			new Book("CHEM4034", "Elemental Chemistry", "2nd", 31, 99.99f, 20),
			new Book("CPSC235", "Aspects of Visual Programming", "2nd", 87, 70.00f, 0),
			new Book("HIST10103", "World War II", "2nd", 56, 150.00f, 25),
			new Book("THEAT44", "Modern Theatre Arts", "7th", 64, 100.00f, 20),
			new Book("HIST22343", "Life of George H.W Bush", "1st", 11, 122.50f, 0),
			new Book("WORK0012", "Business and Tech", "5th", 17, 35.00f, 5),
			new Book("POLSCI933", "Ancient Politics", "3rd", 23, 55.00f, 15),
			new Book("MEDIC8787", "How to be a Doctor", "3rd", 49, 39.99f, 0),
			new Book("MUSC3123", "Country and Jazz Music", "4th", 32, 84.99f, 40),
			new Book("MATH900", "Relational Algebra", "1st", 12, 29.99f, 9),
			new Book("FIN5543", "Financial Management", "2nd", 5, 10.00f, 0),
			new Book("FILM11", "Hollywood Celebrities", "1st", 30, 6.66f, 0),
			new Book("CR2017", "Object Oriented Programming", "2nd", 20, 19.99f, 10) };

	private Socket socket;
	private static ServerSocket serverSocket;
	private BufferedReader socketReader;
	private BufferedWriter socketWriter;
	private static final int port = 25000;

	private final String menu = "Welcome to our Bookstore Enquiry! " + "Your options are:\n" + "1. List all the available books.\n"
			+ "2. Rate a book.\n" + "3. Search for a book.\n" + "4. List book(s) on sale.\n" + "Q. To quit.\n";

	private final String keywordPrompt = "Enter keyword(s) to search seperated by spaces";

	private final String notValidMsg = "Not a valid entry\n";

	public TCPServer(Socket socket) throws IOException {
		this.socket = socket;
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		socketReader = new BufferedReader(isr);
		OutputStream os = socket.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os);
		socketWriter = new BufferedWriter(osw);
	}

	private int getUserChoiceFromMenu() throws IOException {
		socketWriter.write(this.menu);
		System.out.println("Sent MENU to USER");
		socketWriter.flush();
		int response = -1;
		String responseStr = socketReader.readLine();
		System.out.println("User choice is " + responseStr);
		if (responseStr.charAt(0) != 'Q') {
			response = Integer.parseInt(responseStr);
		}
		return response;
	}

	public void rateABook() throws IOException {
		socketWriter.write("Enter book id: ");
		socketWriter.write("\n");
		socketWriter.flush();
		String bookId = socketReader.readLine();
		System.out.println("Book id is" + bookId);
		boolean isSuccess = false;
		Book ratedBook = null;
		for (Book book : books) {
			if (book.getBookId().toLowerCase().contains(bookId.toLowerCase())) {
				isSuccess = true;
				socketWriter.write("\nPlease enter new rating between 0-5 ");
				socketWriter.write("\n");
				socketWriter.flush();
				int userVote = Integer.parseInt(socketReader.readLine());
				book.rate(userVote);
				ratedBook = book;
			}
		}

		if (!isSuccess) {
			socketWriter.write("\n Your search didn't find any books in our catalogue\n");
		} else {
			socketWriter.write("\nThanks for rating the book. Your response has been noted\n");
			socketWriter.write(ratedBook.toString());

		}
		socketWriter.write("\n");
		socketWriter.flush();

	}

	public void sendCatalogue() throws IOException {
		socketWriter.write("\n=============================> Catalogue <=============================\n");
		for (Book book : this.books) {
			socketWriter.write(book.toString());
			socketWriter.write("\n");
		}
		socketWriter.write("\n========================================================================\n");

	}

	public void sendNotValidMsg() throws IOException {
		socketWriter.write(this.notValidMsg);
	}

	public void showSale() throws IOException {
		Object[] discounted = Arrays.stream(this.books).filter(b -> b.getDiscount() > 0.0).toArray();
		socketWriter.write("\n=============================> Sale <=============================\n");
		for (Object book : discounted) {
			socketWriter.write(book.toString());
			socketWriter.write("\n");
		}
		socketWriter.write("\n===================================================================\n");

	}

	@Override
	public void run() {
		try {
			int response = getUserChoiceFromMenu();
			while (response != -1) {

				switch (response) {
				case 1:
					sendCatalogue();
					break;
				case 2:
					rateABook();
					break;
				case 3:
					searchByKeyword();
					break;
				case 4:
					showSale();
					break;
				default:
					sendNotValidMsg();
					break;
				}
				response = getUserChoiceFromMenu();
			}
			socketWriter.write("\n\nThanks for browsing our books! Visit us again!\n\n");
			socketWriter.write("Bye\n");
			socketWriter.flush();
			socket.close();
			System.out.println("Closed socket at server side");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

	}

	private void searchByKeyword() throws IOException {
		socketWriter.write(this.keywordPrompt);
		socketWriter.write("\n");
		socketWriter.flush();
		String keywordList = socketReader.readLine();
		String[] keywords = keywordList.split(" ");
		List<Book> matches = new ArrayList<Book>();
		for (String word : keywords) {

			for (Book book : books) {
				if (book.getBookName().toLowerCase().contains(word.toLowerCase())) {
					matches.add(book);
				}
			}
		}
		if (matches.isEmpty()) {
			socketWriter.write("\n Your search didn't find any books in our catalogue\n");
		} else {
			socketWriter.write("\n =============================>Your matches are <=============================\n");
			for (Book book : matches) {
				socketWriter.write(book.toString());
			}
			socketWriter.write("\n");
		}

	}

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server Started and listening to the port " + TCPServer.port);
			while (true) {
				System.out.println("\nWaiting for the new client request(s).....");
				Socket socket = serverSocket.accept();
				System.out.println("Client request accepted. Starting new thread \n");
				Thread t = new Thread(new TCPServer(socket));
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	private float ratings = 0.0f;
	private int voteCount = -1;

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

	public float getUnitPrice() {
		return unitPrice;
	}

	public float getDiscount() {
		return discount;
	}

	public void rate(int vote) {
		if (voteCount == -1) {
			voteCount++;
		}
		float temp = voteCount * ratings;
		ratings = (temp + vote) / (++voteCount);
	}

	@Override
	public String toString() {
		StringBuffer bookDesc = new StringBuffer();
		bookDesc.append("\nID: " + this.bookId);
		bookDesc.append("\tName: " + this.bookName);
		bookDesc.append("\nEdition: " + this.bookEdition);
		bookDesc.append("\tPrice: " + this.unitPrice);
		bookDesc.append("\tDiscount: " + this.discount);
		if (voteCount == -1) {
			bookDesc.append("\tRatings: Not rated yet");
		} else {
			bookDesc.append("\tRatings: " + this.ratings);
		}
		return bookDesc.toString();

	}

}



