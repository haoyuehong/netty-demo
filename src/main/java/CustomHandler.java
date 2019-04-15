import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * 自定义助手类
 */
//SimpleChannelInboundHandler 对于请求来讲 其实相当于入站/入境
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {
        Channel channel = ctx.channel();
        if(httpObject instanceof HttpRequest){
            //客户端地址
            System.out.println(channel.remoteAddress());

            //将要发送的数据copy到缓冲区
            ByteBuf content = Unpooled.copiedBuffer("hello Netty", CharsetUtil.UTF_8);

            //构建一个http响应  指定数据类型和长度
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,HttpResponseStatus.OK,content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //将响应发送到客户端
            ctx.writeAndFlush(response);
        }

    }
}
