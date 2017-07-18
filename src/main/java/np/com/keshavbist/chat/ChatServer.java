package np.com.keshavbist.chat;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * Created by kbist on 7/18/2017.
 */
public class ChatServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8090).addService(new ChatServiceImpl()).build();
        server.start();
        server.awaitTermination();
    }
}
