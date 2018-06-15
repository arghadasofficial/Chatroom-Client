/*
 * Copyright (C) 2018 Argha Das
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package argha.chatroom.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Argha Das
 */
public class Message extends Thread {

    private PrintWriter output;
    private Scanner input;
    private Socket socket;
    private String message = "";

    public Message(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {
        try {
            getMessage();
        } catch (IOException ex) {
            Log.E(ex.getMessage());
        }
    }

    private void getMessage() throws IOException {
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new Scanner(socket.getInputStream());
        while (input.hasNext()) {
            message = input.nextLine();
            if (message.contains("+-+")) {
                String data = message.substring(3);
                data = data.replace("[", "");
                data = data.replace("]", "");
                String [] onlineUsers = data.split(", ");
                UI.onlineUserList.setListData(onlineUsers);
            } else {
                Log.P(socket + message);
                MessageHandler.showMessage(message);
            }
        }
    }

    public void sendMessage(String message) {
        output.println(message);
    }

    public void closeConnection() {
        try {
            socket.close();
        } catch (IOException ex) {
            Log.E(ex.getMessage());
        }
    }

    public void closeStream() {
        output.close();
        input.close();
    }
}
