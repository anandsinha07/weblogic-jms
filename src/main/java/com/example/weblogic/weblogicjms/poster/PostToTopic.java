package com.example.weblogic.weblogicjms.poster;

import weblogic.rmi.extensions.PortableRemoteObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class PostToTopic {

    public final static String SERVER= "t3://localhost:7001";
    public final static String JNDI_FACTORY="weblogic.jndi.WLInitialContextFactory";
    public final static String JMS_FACTORY="/com/conduent/weblogic/base/cf";
    public final static String TOPIC="/com/weblogic/base/dt";


    protected TopicConnectionFactory tconFactory;
    protected TopicConnection tcon;
    protected TopicSession tsession;
    protected TopicPublisher tpublisher;
    protected Topic topic;
    protected TextMessage message;
    private boolean quit = false;

    public void init(Context context, String topicName) throws NamingException, JMSException {
        tconFactory = (TopicConnectionFactory) PortableRemoteObject.narrow(context.lookup(JMS_FACTORY),TopicConnectionFactory.class);
        tcon = tconFactory.createTopicConnection();
        tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = (Topic) PortableRemoteObject.narrow(context.lookup(topicName), Topic.class);
        tpublisher = tsession.createPublisher(topic);
        message = tsession.createTextMessage();
        tcon.start();
    }

    public void post(String msg) throws JMSException {
        message.setText(msg);
        tpublisher.send(message);
        System.out.println("Message sent");
    }

    public void close() throws JMSException {
        tpublisher.close();
        tsession.close();
        tcon.close();
    }

    public static void main(String[] args) throws Exception {
        InitialContext ic = getInitialContext();
        PostToTopic ts = new PostToTopic();
        ts.init(ic, TOPIC);
        readAndSend(ts);
        ts.close();
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

    protected static void readAndSend(PostToTopic ts)throws IOException, JMSException
    {
        BufferedReader msgStream = new BufferedReader (new InputStreamReader(System.in));
        String line=null;
        System.out.print("nt TopicSender Started ... Enter message ('quit' to quit): n");
        do {
            System.out.print("Topic Sender Says > ");
            line = msgStream.readLine();
            if (line != null && line.trim().length() != 0) {
                ts.post(line);
            }
        } while (line != null && ! line.equalsIgnoreCase("quit"));
    }
}


