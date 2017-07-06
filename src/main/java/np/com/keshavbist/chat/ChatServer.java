package np.com.keshavbist.chat;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Created by kbist on 7/4/2017.
 */
public class ChatServer {
    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(8090).addService(new ChatServiceImpl()).build();
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException e) {
        } catch (InterruptedException ie){
        }

    }
}
