package com.example.weblogic.weblogicjms.reader;

import weblogic.rmi.extensions.PortableRemoteObject;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.FileWriter;
import java.util.Hashtable;

public class ReadFromTopic implements MessageListener {

    public final static String SERVER= "t3://localhost:7001";
    public final static String JNDI_FACTORY="weblogic.jndi.WLInitialContextFactory";
    public final static String JMS_FACTORY="/com/weblogic/base/cf";
    public final static String TOPIC="/com/weblogic/base/dt";

    private TopicConnectionFactory tconFactory;
    private TopicConnection tcon;
    private TopicSession tsession;
    private TopicSubscriber tsubscriber;
    private Topic topic;
    private boolean quit = false;

    @Override
    public void onMessage(Message msg)
    {
        try {
            String msgText;
            if (msg instanceof TextMessage){
                msgText = ((TextMessage)msg).getText();
            } else {
                msgText = msg.toString();
            }
            System.out.println("Message Received!!");
            System.out.println("Printing messages: \n" +msgText);
            System.out.println("Listening to the JMS queue continues...");

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

    public void init(Context ctx, String topicName)throws NamingException, JMSException
    {
        tconFactory = (TopicConnectionFactory)PortableRemoteObject.narrow(ctx.lookup(JMS_FACTORY), TopicConnectionFactory.class);
        tcon = tconFactory.createTopicConnection();
        tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = (Topic) PortableRemoteObject.narrow(ctx.lookup(topicName), Topic.class);
        tsubscriber = tsession.createSubscriber(topic);
        tsubscriber.setMessageListener(this);
        tcon.start();
    }

    public void close() throws JMSException {
        tsubscriber.close();
        tsession.close();
        tcon.close();
    }

    public static void main (String args[]) throws Exception {
        InitialContext ic = getInitialContext();
        ReadFromTopic tr = new ReadFromTopic();
        tr.init(ic, TOPIC);
        System.out.println("JMS Ready To Receive Messages (To quit, send a 'quit' message).");
        synchronized(tr) {
            while (! tr.quit) {
                try {
                    tr.wait();
                } catch (InterruptedException ie) {}
            }
        }
        tr.close();
    }

    private static InitialContext getInitialContext() throws NamingException
    {
        Hashtable<String,String> env = new Hashtable<String,String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
        env.put(Context.PROVIDER_URL, SERVER);
        env.put("weblogic.jndi.createIntermediateContexts", "true");
        return new InitialContext(env);
    }
}


