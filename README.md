# BookstoreEnquiry

## Author
Fawaz Bukhowa

## Abstract
An application called "Bookstore Enquiry", and it is implemented in Java using TCP client-server program. It contains two programs; one program is called "Server" and another one is called "Client". In this application, the 'server' maintains information about books and for each book it stores information like 'BookId', 'BookName', 'BookEdition', 'AvailableStock', 'UnitPrice', 'Discount'. This application works in such a way that, the server runs indefinitely and waits for client requests. The Client will accept the BookId & BookName from console and send it to server. If the server finds any books that matches with sent details, then it shows "BOOK FOUND" message to the client. If there is no any such book, the server sends ‘ERROR’ message to client and then prints that message and quits. In the case of having "BOOK FOUND", the client again prompts the user with what kind of book information is required, like Book Edition and Stack available. When entering the choice, the client sends the choice to server and server sends the corresponding information to the client which will be printed by the client. TCP is used as the transport layer protocol, since it provides reliable delivery which is critical for the given application. TCP does not provide timing guarantee, which is not very important in the given scenario. The server is implemented as a singleton class. The main thread opens a server socket on the local inet address and port 25000, which has been arbitrarily chosen and hard coded. It then waits for clients to connect to it. The Client is also implemented as a singleton class. The main thread opens the socket and connects to the server. The main thread takes care of input and output messaged by opening input buffered reader and output buffered writer respectively. It also creates a buffered stream to read from the console. Current design does not encrypt the text strings and also there is no graphical user interface.

## Compile
Open two terminals and type:
```
javac TCPServer.java
```
and
```
javac TCPClient.java
```

## Run
Then type:
```
java TCPServer
```
and
```
javac TCPClient
```


