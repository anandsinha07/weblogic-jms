# weblogic-jms
Setting up with WebLogic Server on MacOs &amp; spring-boot integration for posting messages to JMS queue - reading messages from JMS queue.

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