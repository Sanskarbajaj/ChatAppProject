package com.example.ChatApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<String>users=new ArrayList<>();
    static ArrayList<MessangingThread>clients=new ArrayList<>();
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket=new ServerSocket(80,10);
        System.out.println("Waiting for the client to connected");
//Dbops.createChatTable("chat_backup");
//Dbops.createUserTable("users");

        while (true) {
            Socket client = serverSocket.accept();
            MessangingThread thread = new MessangingThread(client);
            clients.add(thread);

            thread.start();
        }
    }
    public static void sendToAll(String user,String msg)
    {
        for(MessangingThread c:clients)
        {
            if(c.getUser().equals(user))
            {
            c.sendToMe(user,msg);
            }
            else
            {
                c.sendMessage(user,msg);
            }
        }
    }
    public static class MessangingThread extends Thread
    {
        String user="";
        BufferedReader in;
        PrintWriter out;

      MessangingThread(Socket client) throws Exception {
          in=new BufferedReader(new InputStreamReader(client.getInputStream()));
          out=new PrintWriter(client.getOutputStream(),true);
          user=in.readLine();
          users.add(user);
          Dbops.addUserInDB(user);
      }
        public void saveInDB(String chatUser, String msg) throws Exception {
            String msg_id = chatUser + "_" + System.currentTimeMillis();
            Dbops.chatBackUp(user, msg_id, msg);
        }
        @Override
        public void run() {
          String msg;
         try {
             while (true) {
                 msg = in.readLine();
                 if(msg.equals("end"))
                 {
                     users.remove(user);
                     clients.remove(this);
                     break;
                 }
                 else {
                     sendToAll(user, msg);
                     saveInDB(user,msg);
                 }
             }
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
        }
        public String getUser()
        {
            return this.user;
        }
        public void sendMessage(String chatUser, String msg) {
            out.println(chatUser + ": " + msg);
        }
        public void sendToMe(String chatUser, String msg){
            out.println("You: " + msg);
        }
    }

}
