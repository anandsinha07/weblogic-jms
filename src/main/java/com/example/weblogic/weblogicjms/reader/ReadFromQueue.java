package com.example.weblogic.weblogicjms.reader;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

public class ReadFromQueue implements MessageListener {

    public final static String SERVER= "t3://localhost:7001";
    public final static String JNDI_FACTORY= "weblogic.jndi.WLInitialContextFactory";
    public final static String JMS_FACTORY= "/com/conduent/weblogic/base/cf";
    public final static String QUEUE= "/com/conduent/weblogic/base/dq";

    private QueueConnectionFactory queueConnectionFactory;
    private QueueSession queueSession;
    private QueueConnection queueConnection;
    private QueueReceiver queueReceiver;
    private Queue queue;
    private Boolean quit = false;


    public void init(Context context, String queueName) throws NamingException, JMSException {
        queueConnectionFactory = (QueueConnectionFactory) context.lookup(JMS_FACTORY);
        queue = (Queue) context.lookup(QUEUE);
        queueConnection = queueConnectionFactory.createQueueConnection();
        queueSession = queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
        queueReceiver = queueSession.createReceiver(queue);
        queueReceiver.setMessageListener(this);
        queueConnection.start();
    }


    public void close() throws JMSException {
        queueReceiver.close();
        queueSession.close();
        queueConnection.close();
    }


    private static InitialContext getInitialContext() throws NamingException {
		/*Properties p = new Properties();
		p.put(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
		p.put(Context.PROVIDER_URL, "t3://localhost:7001");*/

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, SERVER);
        return new InitialContext(env);
    }

    public static void main(String args[]) throws Exception {
        System.out.println("Initiating...");
        InitialContext initialContext = getInitialContext();
        ReadFromQueue queueReader = new ReadFromQueue();
        queueReader.init(initialContext, QUEUE);


        synchronized (queueReader){
            while (!queueReader.quit){
                try {
                    queueReader.wait();
                } catch (InterruptedException ie){}
                queueReader.close();

            }
        }


        queueReader.close();
        System.out.println("Finishing...");

    }

    @Override
    public void onMessage(Message message) {

        try {
            String msgText;
            if (message instanceof TextMessage){
                msgText = ((TextMessage)message).getText();
            } else {
                msgText = message.toString();
            }

            try{
                FileWriter fw=new FileWriter("JMS.xml");
                fw.write(msgText);
                fw.close();
                System.out.println(msgText);
//		        sftpOutBoundService.uploadToSftp(pipedFilePath);
            }catch(Exception e){
                e.printStackTrace();
            }

            if(msgText.equalsIgnoreCase("quit")){
                synchronized (this){
                    quit = true;
                    this.notifyAll();
                }
            }
        } catch(JMSException jmsException){
            System.err.println("Exception: " + jmsException.getMessage());
        } finally {

        }

    }
}


