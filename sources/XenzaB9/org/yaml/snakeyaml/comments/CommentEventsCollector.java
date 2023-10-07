// 
// Decompiled by Procyon v0.6.0
// 

package org.yaml.snakeyaml.comments;

import org.yaml.snakeyaml.events.CommentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.AbstractQueue;
import org.yaml.snakeyaml.parser.Parser;
import org.yaml.snakeyaml.events.Event;
import java.util.Queue;
import java.util.List;

public class CommentEventsCollector
{
    private List<CommentLine> commentLineList;
    private Queue<Event> eventSource;
    private CommentType[] expectedCommentTypes;
    
    public CommentEventsCollector(final Parser parser, final CommentType... expectedCommentTypes) {
        this.eventSource = new AbstractQueue<Event>() {
            @Override
            public boolean offer(final Event e) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public Event poll() {
                return parser.getEvent();
            }
            
            @Override
            public Event peek() {
                return parser.peekEvent();
            }
            
            @Override
            public Iterator<Event> iterator() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public int size() {
                throw new UnsupportedOperationException();
            }
        };
        this.expectedCommentTypes = expectedCommentTypes;
        this.commentLineList = new ArrayList<CommentLine>();
    }
    
    public CommentEventsCollector(final Queue<Event> eventSource, final CommentType... expectedCommentTypes) {
        this.eventSource = eventSource;
        this.expectedCommentTypes = expectedCommentTypes;
        this.commentLineList = new ArrayList<CommentLine>();
    }
    
    private boolean isEventExpected(final Event event) {
        if (event == null || !event.is(Event.ID.Comment)) {
            return false;
        }
        final CommentEvent commentEvent = (CommentEvent)event;
        for (final CommentType type : this.expectedCommentTypes) {
            if (commentEvent.getCommentType() == type) {
                return true;
            }
        }
        return false;
    }
    
    public CommentEventsCollector collectEvents() {
        this.collectEvents(null);
        return this;
    }
    
    public Event collectEvents(final Event event) {
        if (event != null) {
            if (!this.isEventExpected(event)) {
                return event;
            }
            this.commentLineList.add(new CommentLine((CommentEvent)event));
        }
        while (this.isEventExpected(this.eventSource.peek())) {
            this.commentLineList.add(new CommentLine(this.eventSource.poll()));
        }
        return null;
    }
    
    public Event collectEventsAndPoll(final Event event) {
        final Event nextEvent = this.collectEvents(event);
        return (nextEvent != null) ? nextEvent : this.eventSource.poll();
    }
    
    public List<CommentLine> consume() {
        try {
            return this.commentLineList;
        }
        finally {
            this.commentLineList = new ArrayList<CommentLine>();
        }
    }
    
    public boolean isEmpty() {
        return this.commentLineList.isEmpty();
    }
}
