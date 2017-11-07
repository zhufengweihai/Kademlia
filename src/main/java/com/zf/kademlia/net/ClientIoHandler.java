package com.zf.kademlia.net;
import org.apache.mina.core.service.IoHandlerAdapter;  
import org.apache.mina.core.session.IoSession;  
  
/** 
 * @description 
 * @date：(2015-11-29 上午10:22:39) 
 * @author Administrator 
 * @version v1.0 
 * @since v1.0 
 * 
 * Modified history 
 * 
 *    Modified date:   
 *    Modifier user:      
 *    description:  
 * 
 * */  
public class ClientIoHandler extends IoHandlerAdapter{  
      
    @Override  
    public void sessionOpened(IoSession session) throws Exception {  
        System.out.println("创建session连接");  
    }  
      
    @Override  
    public void sessionClosed(IoSession iosession) throws Exception {  
          
        System.out.println("会话连接关闭");  
        iosession.close(true);  
    }  
      
    @Override  
    public void messageReceived(IoSession iosession, Object obj)  
            throws Exception {  
        System.out.println("客户端成功接收到消息:"+obj.toString());  
    }  
      
    @Override  
    public void messageSent(IoSession iosession, Object obj) throws Exception {  
          
        System.out.println("客户端消息已发送成功，发送数据:"+obj.toString());  
          
    }  
  
}  