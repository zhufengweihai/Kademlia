package com.zf.kademlia.net;
import org.apache.mina.core.service.IoHandlerAdapter;  
import org.apache.mina.core.session.IoSession;  
  
/** 
 * @description 
 * @date��(2015-11-29 ����10:22:39) 
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
        System.out.println("����session����");  
    }  
      
    @Override  
    public void sessionClosed(IoSession iosession) throws Exception {  
          
        System.out.println("�Ự���ӹر�");  
        iosession.close(true);  
    }  
      
    @Override  
    public void messageReceived(IoSession iosession, Object obj)  
            throws Exception {  
        System.out.println("�ͻ��˳ɹ����յ���Ϣ:"+obj.toString());  
    }  
      
    @Override  
    public void messageSent(IoSession iosession, Object obj) throws Exception {  
          
        System.out.println("�ͻ�����Ϣ�ѷ��ͳɹ�����������:"+obj.toString());  
          
    }  
  
}  