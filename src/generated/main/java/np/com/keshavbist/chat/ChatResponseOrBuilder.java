// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chat.proto

package np.com.keshavbist.chat;

public interface ChatResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:np.com.keshavbist.chat.ChatResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.google.protobuf.Timestamp timestamp = 1;</code>
   */
  boolean hasTimestamp();
  /**
   * <code>.google.protobuf.Timestamp timestamp = 1;</code>
   */
  com.google.protobuf.Timestamp getTimestamp();
  /**
   * <code>.google.protobuf.Timestamp timestamp = 1;</code>
   */
  com.google.protobuf.TimestampOrBuilder getTimestampOrBuilder();

  /**
   * <code>.np.com.keshavbist.chat.ChatRequest message = 2;</code>
   */
  boolean hasMessage();
  /**
   * <code>.np.com.keshavbist.chat.ChatRequest message = 2;</code>
   */
  np.com.keshavbist.chat.ChatRequest getMessage();
  /**
   * <code>.np.com.keshavbist.chat.ChatRequest message = 2;</code>
   */
  np.com.keshavbist.chat.ChatRequestOrBuilder getMessageOrBuilder();
}
