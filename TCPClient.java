
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

	private static Socket socket;
	private final static int port = 25000;
	private final static String host = "localhost";

	public static void main(String args[]) {
		try {
			InetAddress address = InetAddress.getByName(host);
			socket = new Socket(address, port);
			OutputStream os = socket.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			Scanner in = new Scanner(System.in);
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			/* start a reader thread */
			Thread reader = new Thread() {
				public void run() {
					try {
						while (socket.isClosed() == false) {
							// System.out.println(socket.isClosed());
							String userInput = in.nextLine();
							if (socket.isClosed() == true) {
								break;
							}
							bw.write(userInput + "\n");
							// System.out.println("Sending to server: "+userInput);
							bw.flush();
						}
					} catch (IOException e) {
					}
				}
			};
			/* start a writer thread */
			Thread writer = new Thread() {
				public void run() {
					while (socket.isClosed() == false) {
						try {
							String serverMsg = br.readLine();
							if (serverMsg == null) {
								socket.close();
								break;
							} else if (serverMsg.toLowerCase().contains("bye")) {
								socket.close();
								break;
							}
							System.out.println(serverMsg);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			writer.start();
			reader.start();
			reader.join();
			writer.join();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}




