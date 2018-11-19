
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {

    private static Socket socket;

    public static void main(String args[]) {
        try {
            String host = "localhost";
            int port = 25000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            //Send the message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
           
            Scanner in = new Scanner(System.in);
           
            System.out.print("Enter Book_ID :");
            String bookId = in.nextLine();
            System.out.print("Enter Book_Name:");
            String bookName  = in.nextLine();
 
            //String bookIdAndName = bookId+","+ bookName+ "\n";

            //String sendMessage = bookIdAndName + "\n";
            bw.write(bookId+","+ bookName+ "\n");
            bw.flush();
            System.out.println("Message sent to the server : " + bookId+","+ bookName+ "\n");

            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String bookStatusMsg = br.readLine();
            System.out.println("Message received from the server : " + bookStatusMsg);

            if (!"ERROR".equals(bookStatusMsg)) {
                System.out.println("please select any one option");
                System.out.println("1: Book Edition information");
                System.out.println("2: No. of copy available");
                
                String bookInfoChoice = in.nextLine() + "\n";
                

                //Send the message to the server
                OutputStream os1 = socket.getOutputStream();
                OutputStreamWriter osw1 = new OutputStreamWriter(os1);
                BufferedWriter bw1 = new BufferedWriter(osw1);

                               
                bw1.write(bookInfoChoice);
                bw1.flush();
                System.out.println("Message sent to the server : " + bookInfoChoice);

                //Get the return message from the server
                InputStream is1 = socket.getInputStream();
                InputStreamReader isr1 = new InputStreamReader(is1);
                BufferedReader br1 = new BufferedReader(isr1);
                String bookInfoMsg = br1.readLine();
                System.out.println("Message received from the server : " + bookInfoMsg);

            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            //Closing the socket
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
