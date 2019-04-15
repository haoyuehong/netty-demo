import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyHello {

    public static void main(String[] args){
        //定义线程组
        //主线程组,接受客户端连接，但不处理任何业务，服务器会返回hello netty
        EventLoopGroup mainGroup = new NioEventLoopGroup();
        //从线程组 主线程组会把任务交给从先线程处理
        EventLoopGroup workGroup = new NioEventLoopGroup();

       try {
           //服务端启动类
           ServerBootstrap serverBootstrap = new ServerBootstrap();
           serverBootstrap.group(mainGroup,workGroup)  //设置主从线程组
                   .channel(NioServerSocketChannel.class)  //设置NIO的双向通道
                   .childHandler(new HelloServerInitializer());    //子处理器 用于处理workGroup

           //启动server  并绑定端口为8088 同时启动方式为同步
           ChannelFuture channelFuture = serverBootstrap.bind(8088).sync();
           //监听关闭的channel 设置为同步方式
           channelFuture.channel().closeFuture().sync();
       }catch (Exception e){
           e.printStackTrace();
       }finally {
           //以优雅的方式关闭线程
           mainGroup.shutdownGracefully();
           workGroup.shutdownGracefully();
       }
    }
}
