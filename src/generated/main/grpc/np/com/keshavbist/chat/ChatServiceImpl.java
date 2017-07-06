package np.com.keshavbist.chat;

import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;

import java.time.Instant;
import java.util.LinkedHashSet;

import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static np.com.keshavbist.chat.ChatServiceGrpc.METHOD_CHAT_POOL;

/**
 * Created by kbist on 7/3/2017.
 */
public class ChatServiceImpl extends ChatServiceGrpc.ChatServiceImplBase {
    private static LinkedHashSet <StreamObserver<ChatResponse>> observers = new LinkedHashSet<>();
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
}
