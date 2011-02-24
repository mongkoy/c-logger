package org.logview4j.listener;

import java.util.*;

import org.logview4j.config.*;
import org.logview4j.dto.*;
import org.logview4j.event.*;

import EDU.oswego.cs.dl.util.concurrent.*;


/**
 * A queue that restricts the number of inbound log events that get sent to the event manager to
 * prevent slower machines from being swamped by log events.
 * <p>
 * The configuration file parameter: <code>logview4j.events.per.second</code> determines how many
 * log events are removed from this queue per second
 */
public class InboundEventQueue {
    
    private static final InboundEventQueue INSTANCE = new InboundEventQueue();
    
    private LinkedQueue linkedQueue = new LinkedQueue();
    
    private InboundEventQueueProcessor processor = null;
    
    private long millisBetweenUpdates = ConfigurationManager.getInstance().getLong(ConfigurationKey.MILLIS_BETWEEN_UPDATES, 1000);
    private boolean recordEventsWhenPaused = ConfigurationManager.getInstance().getBoolean(ConfigurationKey.RECORD_EVENTS_WHEN_PAUSED);
    
    private InboundEventQueue() {
        processor = new InboundEventQueueProcessor();
        
        Thread thread = new Thread(processor);
        thread.setDaemon(true);
        thread.start();
    }
    
    public static InboundEventQueue getInstance() {
        return INSTANCE;
    }
    
    public void put(LogView4JLoggingEvent event) {
        try {
            linkedQueue.put(event);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public Object take() {
        Object event = null;
        
        try {
            event = linkedQueue.take();
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return event;
    }
    
    public Object poll(long millis) {
        Object event = null;
        
        try {
            event = linkedQueue.poll(millis);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        return event;
    }
    
    public boolean isEmpty() {
        return linkedQueue.isEmpty();
    }
    
    /**
     * Thread processor from the inbound event queue
     */
    private class InboundEventQueueProcessor implements Runnable {;
    
    public InboundEventQueueProcessor() {
    }
    
    /**
     * Fires the event
     * @param loggingEvent the event to fire
     */
    protected void fireEvent(List events) {
        
        LogView4JEvent event = new LogView4JEvent(LogView4JEventId.LOGGING_EVENTS_RECEIVED);
        event.set(LogView4JEventKey.LOGGING_EVENTS, events);
        LogView4JEventManager.getInstance().fireEvent(event);
    }
    
    /**
     * Grab events from the queue not processing events faster than this
     * storing events when paused
     */
    public void run() {
        long lastPass = System.currentTimeMillis();
        
        while (true) {
            
            /**
             * Fire events when not paused
             */
            if (!SocketProcessorManager.getInstance().isPaused()) {
                List events = new ArrayList();
                Object value = poll(millisBetweenUpdates);
                
                if (value != null) {
                    events.add(value);
                }
                
                while (!isEmpty()) {
                    value = poll(0);
                    
                    if (value != null) {
                        events.add(value);
                    }
                    
                }
                
                fireEvent(events);
            }
            /**
             * If we should ditch events when paused, eat the events off the queue
             */
            else if (!recordEventsWhenPaused) {
                while (!isEmpty()) {
                    poll(0);
                }
            }
            
            long now = System.currentTimeMillis();
            long elapsed = now - lastPass;
            
            try {
                if (elapsed < millisBetweenUpdates) {
                    // Sleep for the remainder of the wait time
                    Thread.sleep(millisBetweenUpdates - elapsed);
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            lastPass = System.currentTimeMillis();
        }
    }
    
    }
}
