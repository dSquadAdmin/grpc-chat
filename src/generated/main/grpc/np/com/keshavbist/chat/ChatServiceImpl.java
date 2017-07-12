package np.com.keshavbist.chat;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static np.com.keshavbist.chat.ChatServiceGrpc.METHOD_CHAT_POOL;

/**
 * Created by kbist on 7/3/2017.
 */
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private static LinkedHashSet <StreamObserver<ChatResponse>> observers = new LinkedHashSet<>();
    private static LinkedHashSet <StreamObserver<Clients>> chatObservers = new LinkedHashSet<>();
    private static Map<String, Clients> clients = new HashMap<>();
    @Override
    public StreamObserver<ChatRequest> chatPool(StreamObserver<ChatResponse> responseObserver) {
        observers.add(responseObserver);
        return new StreamObserver<ChatRequest>() {
            @Override
            public void onNext(ChatRequest value) {
                for (StreamObserver<ChatResponse> observer: observers) {
                    Instant time = Instant.now();
                    observer.onNext(
                            ChatResponse.newBuilder()
                                    .setTimestamp(
                                            Timestamp.newBuilder()
                                                    .setSeconds(time.getEpochSecond())
                                                    .setNanos(time.getNano())
                                                    .build())
                                    .setMessage(value)
                                    .build()
                    );
                }
            }

            @Override
            public void onError(Throwable t) {
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }

    @Override
    public StreamObserver<ConectReq> getClients(StreamObserver<Clients> clientsStreamObserver){
        chatObservers.add(clientsStreamObserver);
        return  new StreamObserver<ConectReq>() {
            @Override
            public void onNext(ConectReq value) {
                System.out.println("New Incoming Connection ....");
                clients.put(value.getHost(), Clients.newBuilder().setClient(value).build());
                for (StreamObserver<Clients> observer : chatObservers) {
                    for (String key: clients.keySet()) {
                        observer.onNext(clients.get(key));
                    }
                }

                if(!value.getConnect()){
                    clients.remove(value.getHost());
                    System.out.println("Client Removed ......");
                    this.onCompleted();
                }
            }

            @Override
            public void onError(Throwable t) {
                chatObservers.remove(clientsStreamObserver);
            }

            @Override
            public void onCompleted() {
                chatObservers.remove(clientsStreamObserver);
            }
        };
    }
}
