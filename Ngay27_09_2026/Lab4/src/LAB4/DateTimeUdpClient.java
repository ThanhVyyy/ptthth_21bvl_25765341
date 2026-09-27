package LAB4;

// Bài 3 - Dịch vụ ngày giờ (UDP)

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

public class DateTimeUdpClient {
    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5021;
        InetAddress server = InetAddress.getByName(host);

        try (DatagramSocket socket = new DatagramSocket();
             BufferedReader console = new BufferedReader(
                     new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            socket.setSoTimeout(3000);
            System.out.println("Nhập DATE, TIME, DATETIME hoặc QUIT để thoát");

            String request;
            while ((request = console.readLine()) != null) {
                byte[] data = request.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(data, data.length, server, port));

                byte[] buffer = new byte[4096];
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(response);
                    String text = new String(response.getData(),
                            response.getOffset(), response.getLength(), StandardCharsets.UTF_8);
                    System.out.println("Server: " + text);
                } catch (SocketTimeoutException e) {
                    // Khác với TCP: UDP không có kết nối nên nếu server dừng giữa
                    // chừng, mỗi yêu cầu chỉ đơn giản là timeout độc lập; client
                    // vẫn có thể tiếp tục gửi yêu cầu tiếp theo bình thường.
                    System.err.println("Hết 3 giây nhưng chưa nhận được phản hồi");
                }

                if (request.trim().equalsIgnoreCase("QUIT")) break;
            }
        } catch (NumberFormatException e) {
            System.err.println("Port phải là số nguyên");
        }
    }
}
