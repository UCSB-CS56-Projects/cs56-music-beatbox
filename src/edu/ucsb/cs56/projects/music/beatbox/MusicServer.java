package edu.ucsb.cs56.projects.music.beatbox;
import java.io.*;
import java.net.*;
import java.util.*;

/** Class for Music Server
 *@version UCSB, S13, 05/14/2013
 *@author Callum Steele
 *@author Miranda Aperghis
 */
public class MusicServer {
    ArrayList<ObjectOutputStream> clientOutputStreams;
    /**
     * Main that creates a new MusicServer and calls the method go
     */
    public static void main (String[] args) {
	new MusicServer().go() ;
    }
    /** 
     * Create a new input stream with the client's socket and
     * while there is something to read in, reads in two objects and
     * call the method tellEveryone
     */
    public class ClientHandler implements Runnable {
	ObjectInputStream in;       
	Socket clientSocket;
   
	public ClientHandler(Socket socket) {
	    try {
		clientSocket = socket;
		in = new ObjectInputStream(clientSocket.getInputStream() ) ;
          
	    } catch(Exception ex) {ex.printStackTrace() ; }
	} // close constructor

        public void run() {
	    Object o2 = null;
	    Object o1 = null;
	    try {
		while ((o1 = in.readObject() ) != null) {
		    o2 = in.readObject() ;
		    System.out.println("read two objects") ;
		    tellEveryone(o1, o2) ;
		} // close while
	    } catch(Exception ex) {ex.printStackTrace() ; }
	} // close run
    } // close inner class

    /**
     * makes a connection with the client on port 4242,
     *connects the output stream to the client socket
     *and create a new thread with a client handler.
     */
    public void go() {
	clientOutputStreams = new ArrayList<ObjectOutputStream>() ;
        try {
	    ServerSocket serverSock = new ServerSocket(4242) ;
	    while(true) {
		Socket clientSocket = serverSock.accept() ;
		ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream() ) ;
		clientOutputStreams.add(out) ;
		Thread t = new Thread(new ClientHandler(clientSocket) ) ;
		t.start() ;
		System.out.println("got a connection") ;
	    }
	}catch(Exception ex) {
	    ex.printStackTrace() ;
	}
    } // close go

    /**
     * write the two objects to each of the client output streams
     * @param one object to output
     * @param two object to output
     */
    public void tellEveryone(Object one, Object two) {
	Iterator it = clientOutputStreams.iterator() ;
	while(it.hasNext() ) {
	    try {
		ObjectOutputStream out = (ObjectOutputStream) it.next() ;
		out.writeObject(one) ;
		out.writeObject(two) ;
	    } catch(Exception ex) { ex.printStackTrace() ; }
	}
    } // close tellEveryone
} // close class 
