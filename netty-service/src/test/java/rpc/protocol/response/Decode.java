package rpc.protocol.response;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import rpc.util.SerializableUtil;

import java.io.IOException;
import java.util.List;

public class Decode extends ByteToMessageDecoder {

    static int HEAD_LENGTH = 0;

    static {
        try {
            byte[] bytes = SerializableUtil.toBytes(new Head().setId(0L).setBodyLength(0));
            HEAD_LENGTH = bytes.length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf byteBuf = in;
        int bodyLength = 0;
        while (byteBuf.readableBytes() >= HEAD_LENGTH) {
            byte[] bytes = new byte[HEAD_LENGTH];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            Head head = SerializableUtil.toObject(bytes);
            bodyLength = head.getBodyLength();
            if (byteBuf.readableBytes() >= HEAD_LENGTH + bodyLength) {
                bytes = new byte[bodyLength];
                byteBuf.readBytes(HEAD_LENGTH);
                byteBuf.readBytes(bytes);
                Body body = SerializableUtil.toObject(bytes);
                Response response = new Response().setHead(head).setBody(body);
                out.add(response);
            } else {
                break;
            }
        }
    }
}
