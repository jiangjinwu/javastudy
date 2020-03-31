package top.homesoft;


import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleMBeanBase;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.concurrent.Executors;

public class SessionManager extends LifecycleMBeanBase implements Manager, PropertyChangeListener {


    int maxActiveSessions =0;
    private Container container;
    private String secureRandomClass = null;
    private String secureRandomAlgorithm = "SHA1PRNG";
    private String secureRandomProvider = null;
    private SessionIdGenerator sessionIdGenerator = null;
    private boolean disableListeners = false;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);




    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void setContext(Context context) {

    }

    @Override
    public SessionIdGenerator getSessionIdGenerator() {
        return null;
    }

    @Override
    public void setSessionIdGenerator(SessionIdGenerator sessionIdGenerator) {

    }

    @Override
    public long getSessionCounter() {

        return 0;
    }

    @Override
    public void setSessionCounter(long sessionCounter) {
    }

    @Override
    public int getMaxActive() {

        return 0;
    }

    @Override
    public void setMaxActive(int maxActive) {
    }

    public int getMaxActiveSessions() {

        return this.maxActiveSessions;
    }

    public void setMaxActiveSessions(int max) {

        int oldMaxActiveSessions = this.maxActiveSessions;
        this.maxActiveSessions = max;
        support.firePropertyChange("maxActiveSessions", Integer.valueOf(oldMaxActiveSessions), Integer.valueOf(this.maxActiveSessions));
    }

    @Override
    public int getActiveSessions() {


        return 0;
    }

    @Override
    public long getExpiredSessions() {

        return 0;
    }

    @Override
    public void setExpiredSessions(long expiredSessions) {
    }

    @Override
    public int getRejectedSessions() {

        return 0;
    }

    @Override
    public int getSessionMaxAliveTime() {

        return 0;
    }

    @Override
    public void setSessionMaxAliveTime(int sessionMaxAliveTime) {
    }

    @Override
    public int getSessionAverageAliveTime() {

        return 0;
    }

    @Override
    public int getSessionCreateRate() {

        return 0;
    }

    @Override
    public int getSessionExpireRate() {

        return 0;
    }

    @Override
    public void add(Session session) {
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

        support.addPropertyChangeListener(listener);
    }

    @Override
    public void changeSessionId(Session session) {

        String oldId = session.getIdInternal();
        session.setId(generateSessionId(), false);
        String newId = session.getIdInternal();

        container.fireContainerEvent(Context.CHANGE_SESSION_ID_EVENT, new String[]{oldId, newId});
    }

    @Override
    public void changeSessionId(Session session, String s) {

    }

    @Override
    public Session createEmptySession() {

        throw new UnsupportedOperationException("Cannot create empty session.");
    }

    @Override
    public Session createSession(String sessionId) {
         return null;
    }

    @Override
    public Session findSession(String id) throws IOException {
        return null;
    }

    @Override
    public Session[] findSessions() {
       return null;
    }

    @Override
    public void load() throws ClassNotFoundException, IOException {
    }

    @Override
    public void remove(Session session) {

        remove(session, false);
    }

    @Override
    public void remove(Session session, boolean update) {
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

        support.removePropertyChangeListener(listener);
    }

    @Override
    public void unload() throws IOException {
    }

    @Override
    public void backgroundProcess() {


    }

    @Override
    public boolean willAttributeDistribute(String s, Object o) {
        return false;
    }

    @Override
    protected String getDomainInternal() {
       // return MBeanUtils.getDomain(container);
        return "";
    }

    @Override
    protected String getObjectNameKeyProperties() {

        StringBuilder name = new StringBuilder("type=Manager");

        if (container instanceof Context) {
            name.append(",context=");
            String contextName = container.getName();
            if (!contextName.startsWith("/")) {
                name.append('/');
            }
            name.append(contextName);

            Context context = (Context) container;
            name.append(",host=");
            name.append(context.getParent().getName());
        } else {
            // Unlikely / impossible? Handle it to be safe
           /* name.append(",container=");
            name.append(container.getName());*/
        }

        return name.toString();
    }



    public boolean isDisableListeners() {
        return disableListeners;
    }

    public void setDisableListeners(boolean disableListeners) {
        this.disableListeners = disableListeners;
    }

    @Override
    protected void startInternal() throws LifecycleException {

//        sessionIdGenerator = new SessionIdGenerator();
//        sessionIdGenerator.setJvmRoute(getJvmRoute());
//        sessionIdGenerator.setSecureRandomAlgorithm(getSecureRandomAlgorithm());
//        sessionIdGenerator.setSecureRandomClass(getSecureRandomClass());
//        sessionIdGenerator.setSecureRandomProvider(getSecureRandomProvider());
//        sessionIdGenerator.setSessionIdLength(getSessionIdLength());

        //sessionIdGenerator.generateSessionId();

        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
           //initialContext.lookup("java:/comp/env/" + jedisJndiName);
        } catch (NamingException e) {
        }



//        if (!disableListeners) {
//            Executors.newSingleThreadExecutor().execute(eventListenerThread);
//        }

        setState(LifecycleState.STARTING);
    }
//
//    public Engine getEngine() {
//        Engine e = null;
//        for (Container c = getContainer(); e == null && c != null; c = c.getParent()) {
//            if (c instanceof Engine) {
//                e = (Engine) c;
//            }
//        }
//        return e;
//    }
//
//    public String getJvmRoute() {
//        Engine e = getEngine();
//        return e == null ? null : e.getJvmRoute();
//    }

    public String getSecureRandomClass() {
        return this.secureRandomClass;
    }

    public void setSecureRandomClass(String secureRandomClass) {
        String oldSecureRandomClass = this.secureRandomClass;
        this.secureRandomClass = secureRandomClass;
        support.firePropertyChange("secureRandomClass", oldSecureRandomClass, this.secureRandomClass);
    }

    public String getSecureRandomAlgorithm() {
        return secureRandomAlgorithm;
    }

    public void setSecureRandomAlgorithm(String secureRandomAlgorithm) {
        this.secureRandomAlgorithm = secureRandomAlgorithm;
    }

    public String getSecureRandomProvider() {
        return secureRandomProvider;
    }

    public void setSecureRandomProvider(String secureRandomProvider) {
        this.secureRandomProvider = secureRandomProvider;
    }

    @Override
    protected void stopInternal() throws LifecycleException {

        setState(LifecycleState.STOPPING);

        this.sessionIdGenerator = null;
//
//        if (!disableListeners) {
//            eventListenerThread.stop();
//        }

    }

    protected String generateSessionId() {
        String result;

        try {
            do {
                result = sessionIdGenerator.generateSessionId();
            } while (findSession(result) != null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    protected ClassLoader getContainerClassLoader() {
        if (container instanceof Context) {
            Context containerContext = (Context)container;
            Loader contextLoader = containerContext.getLoader();
            if (contextLoader == null) {
                return null;
            }
            return contextLoader.getClassLoader();
        }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {

        if (!(event.getSource() instanceof Context)) {
            return;
        }

        if (event.getPropertyName().equals("sessionTimeout")) {
            try {
               // setMaxInactiveInterval((Integer) event.getNewValue() * 60);
            } catch (NumberFormatException e) {
            }
        }
    }
}
