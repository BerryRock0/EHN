package EtherHack.utils;

import EtherHack.annotations.SubscribeLuaEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventSubscriber {
   private static Map<String, List<AbstractMap.SimpleEntry<Object, Method>>> subscribers = new HashMap<>();
   private static final Map<String, Long> lastExceptionLogAt = new HashMap<>();
   private static final long EXCEPTION_LOG_THROTTLE_MS = 5000L;

   public static void register(Object handler) {
      Logger.printLog("Registering a class object and subscribing to Lua events: " + handler);

      for (Method method : handler.getClass().getMethods()) {
         for (SubscribeLuaEvent annotation : method.getAnnotationsByType(SubscribeLuaEvent.class)) {
            String eventName = annotation.eventName();
            subscribers.computeIfAbsent(eventName, k -> new ArrayList<>())
                    .add(new AbstractMap.SimpleEntry<>(handler, method));
         }
      }
   }

   public static void invokeSubscriber(String eventName) {
      List<AbstractMap.SimpleEntry<Object, Method>> handlers = subscribers.get(eventName);
      if (handlers != null) {
         for (AbstractMap.SimpleEntry<Object, Method> entry : handlers) {
            try {
               entry.getValue().invoke(entry.getKey());
            } catch (InvocationTargetException e) {
               logSubscriberException(eventName, entry.getValue(), e.getCause() != null ? e.getCause() : e);
            } catch (Exception e) {
               logSubscriberException(eventName, entry.getValue(), e);
            }
         }
      }
   }

   private static void logSubscriberException(String eventName, Method method, Throwable throwable) {
      String key = eventName + ":" + method;
      long now = System.currentTimeMillis();
      Long lastLogAt = lastExceptionLogAt.get(key);
      if (lastLogAt != null && now - lastLogAt < EXCEPTION_LOG_THROTTLE_MS) {
         return;
      }

      lastExceptionLogAt.put(key, now);
      Logger.printLog(String.format("Exception when calling method '%s' for event '%s': %s",
              method, eventName, throwable));
      throwable.printStackTrace();
   }
}
