package com.orange.jiachen.landlords.handler;

import com.orange.jiachen.landlords.transfer.TransferProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class DefaultDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        int startIndex;
        int endIndex;
        if ((startIndex = in.indexOf(in.readerIndex(), in.writerIndex(), TransferProtocolUtils.PROTOCOL_HAED)) != -1 &&
                (endIndex = in.indexOf(startIndex + 1, in.writerIndex(), TransferProtocolUtils.PROTOCOL_TAIL)) != -1) {
            endIndex++;
            byte[] bytes = new byte[endIndex - startIndex];
            in.skipBytes(startIndex - in.readerIndex());
            in.readBytes(bytes, 0, bytes.length);
            out.add(bytes);
        }
    }

}
