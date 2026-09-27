package LAB4;

// Bài 3 - Dịch vụ ngày giờ (UDP, dùng chung logic xử lý lệnh với DateTimeTcpServer)

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class DateTimeUdpServer {
    private static final int PORT = 5021;

    public static void main(String[] args) {
        byte[] buffer = new byte[4096];

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("DateTime UDP server listening on port " + PORT);

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request);

                String message = new String(request.getData(),
                        request.getOffset(), request.getLength(), StandardCharsets.UTF_8);
                String responseText = DateTimeTcpServer.process(message);

                byte[] responseData = responseText.getBytes(StandardCharsets.UTF_8);
                DatagramPacket response = new DatagramPacket(
                        responseData, responseData.length,
                        request.getAddress(), request.getPort());
                socket.send(response);
            }
        } catch (IOException e) {
            System.err.println("UDP server error: " + e.getMessage());
        }
    }
}
