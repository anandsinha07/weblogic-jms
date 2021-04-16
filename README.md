# weblogic-jms
Setting up with WebLogic Server on MacOs &amp; spring-boot integration for posting messages to JMS queue/topic - reading messages from JMS queue/topic.

## Prerequisites:
* make sure you have java8 installed and java home path well set i.e `/Library/Java/JavaVirtualMachines/jdk1.8.0_281.jdk/Contents/Home`

## Steps to install WebLogic Server
1. Download using the recommended version link provided:
   [GitHub]https://www.oracle.com/middleware/technologies/weblogic-server-installers-downloads.html#license-lightbox
   
2. Unzip it, go inside and execute following commands. I have this copied to **/oracle**:
    `$ java -jar fmw_12.2.1.3.0_wls_quick.jar`
    * Wait for some time until copying done to 100% complete.
    * Now check if you got this folder **wls12213**
        * Go to **/wls12213/oracle_common/common/bin/** and execute `./config.sh`
            * A configuration wizard should pop up. Just keep everything as defaults -> Next -> Create Password -> Next -> Create -> Finish
            * The domain location is **/wls12213/user_projects/domains/base_domain**
    
3. Go to **/wls12213/user_projects/domains/base_domain** and execute `./startWebLogic.sh` to start your weblogic server.
4. If all went good, go to your browser and hit `http://localhost:7001/console/`, you should be able to see the login dashboard now. Enter the username as **weblogic** and the password you already created.
5. Congratulations!! You are in.
<img width="1680" alt="Screenshot 2021-04-15 at 6 35 57 PM" src="https://user-images.githubusercontent.com/31561908/114874135-a4baab00-9e19-11eb-9837-480d0c485739.png">
This is the landing page we refer to for **Environment** and **Services** sections.

## WebLogic JMS configuration setup
1. Click on Server inside Environment on the landing page and you should have AdminServer running on 7001
<img width="1680" alt="Screenshot 2021-04-15 at 6 42 57 PM" src="https://user-images.githubusercontent.com/31561908/114874934-6ffb2380-9e1a-11eb-82c4-058809a3f321.png">
2. Now go back to landing page and click on **JMS Servers** inside Environmet - Messaging section :
   * Click New
   * Name your new **JMS Server** i.e **DemoJMSServer-0**
   * Next
   * Click on **Create a New Store**, select a store type as **File Store**
   * Name your new file store i.e **DemoFileStore-0**
   * Chose JMS File Store Targets as **AdminServer**
   * Finish
   * Chose Persistent Store as **DemoFileStore-0**
   * Select target as **AdminServer**
   * You are good to see this page:
<img width="1680" alt="Screenshot 2021-04-15 at 6 51 30 PM" src="https://user-images.githubusercontent.com/31561908/114876172-b00ed600-9e1b-11eb-982a-0382434b3909.png">
3. Now go back to landing page and click on **JMS Modules** inside Environmet - Messaging section :
   * Click New
   * Name your system module i.e **DemoSystemModule-0**
   * Next
   * Tick **AdminServer**
   * Tick **Would you like to add resources to this JMS system module?**
   * Finish
   * You are good to see this page
<img width="1680" alt="Screenshot 2021-04-15 at 6 58 16 PM" src="https://user-images.githubusercontent.com/31561908/114877123-9fab2b00-9e1c-11eb-9fbc-e2f2034d0927.png">
4. Now go back to landing page and click on **JMS Modules** inside Environmet - Messaging section :
   * Click on **DemoSystemModule-0**
   * Click New
   * Choose **Connection Factory**
   * Next
   * Fill **Name** your **JNDI Name** as **/com/weblogic/base/cf**
   * Next
   * Next
   * Finish
<img width="1680" alt="Screenshot 2021-04-15 at 7 04 22 PM" src="https://user-images.githubusercontent.com/31561908/114877973-69ba7680-9e1d-11eb-83af-930b8a91617c.png">

   * Click New again
   * Choose **Distributed Queue**
   * Next
   * Fill **Name** your **JNDI Name** as **/com/weblogic/base/dq**
   * Next
   * Finish
<img width="1680" alt="Screenshot 2021-04-15 at 7 07 21 PM" src="https://user-images.githubusercontent.com/31561908/114878394-d59cdf00-9e1d-11eb-8286-a8578e51e330.png">

   * Click New again
   * Choose **Distributed Topic**
   * Next
   * Fill **Name** your **JNDI Name** as **/com/weblogic/base/dt**
   * Next
   * Finish
<img width="1680" alt="Screenshot 2021-04-15 at 7 09 10 PM" src="https://user-images.githubusercontent.com/31561908/114878650-172d8a00-9e1e-11eb-8de0-19dc6b282b47.png">
5. Congratulations!! You are all set.

## JMS Queue Posting Program
* Go to **poster** directory inside **com.example.weblogic.weblogicjms**
* Now, run PostToQueue.main()

## JMS Queue Reading Program
* Go to **reader** directory inside **com.example.weblogic.weblogicjms**
* Now, run ReadFromQueue.main()

## JMS Topic Posting Program
* Go to **poster** directory inside **com.example.weblogic.weblogicjms**
* Now, run PostToTopic.main()

## JMS Topic Reading Program
* Go to **reader** directory inside **com.example.weblogic.weblogicjms**
* Now, run ReadFromTopic.main()

## Contact

The preferred way of contacting me is to mail me at
[anandkumarsinha07@gmail.com](anandkumarsinha07@gmail.com)
My twitter handle is
[`@anandsinha07`](https://twitter.com/anandsinha07)
You can also connect me on
LinkedIn -- https://www.linkedin.com/in/anand-kumar-sinha-54381b126/

## **`Happy Coding!`**
