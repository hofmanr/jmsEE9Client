package nl.hofmanr.jms.client.repository;

import javax.naming.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public abstract class BaseRepository<T> {
    private static final Logger LOGGER = Logger.getLogger(BaseRepository.class.getName());

    public List<T> findByType(Class<T> type) {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            return find(ctx, type, null, 1);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * ContextPatch e.g. java:comp/env/jdbc/ or java:jboss/datasources/ or "java:/jms/queue/"
     * @param ctx
     * @param type
     * @param contextPath
     * @param dept
     * @return
     */
    private List<T> find(InitialContext ctx, Class<T> type, String contextPath, int dept) {
        List<T> result = new ArrayList<>();
        if (contextPath == null)
            contextPath = "";
        try {
            ctx = new InitialContext();
            NamingEnumeration<NameClassPair> list = ctx.list(contextPath);
            while (list.hasMoreElements() && dept < 8) {
                NameClassPair nameClassPair = list.next();
                // nameClassPair className could be "org.apache.activemq.artemis.jms.client.ActiveMQQueue"
                // or "org.apache.activemq.artemis.jms.client.ActiveMQXAQueueConnectionFactory"
                if (nameClassPair.getClassName().endsWith(type.getSimpleName())) {
                    result.add((T)ctx.lookup(contextPath + "/" + nameClassPair.getName()));
                } else if (Context.class.getName().equals(nameClassPair.getClassName())) {
                    String newContextPath = contextPath.length() == 0 ?
                            nameClassPair.getName() :
                            contextPath + "/" + nameClassPair.getName();
                    result.addAll(find(ctx, type, newContextPath, dept + 1));
                }
            }
        } catch (NamingException e) {
            LOGGER.warning("JNDI namespace " + contextPath + " error: " + e.getMessage());
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException e) {
                    LOGGER.warning(e.getMessage());
                }
            }
        }
        return result;
    }

}
