package com.teamzeromtu.studyr.ViewAdapters;

import com.teamzeromtu.studyr.Data.Message;

import java.util.Collection;

/**
 * Created by jbdaley on 6/17/16.
 */
public class MessageController {
    private MessageAdapter adapter;
    protected MessageController(MessageAdapter msgAdapter) {
        adapter = msgAdapter;
    }
    public void add(Message... msgs) {
        for(Message m: msgs) {
            adapter.messages.add(m);
        }
        adapter.notifyDataSetChanged();
    }
    public void clear() {
        adapter.messages.clear();
        adapter.notifyDataSetChanged();
    }
    public void addAll(Collection<? extends Message> msgs) {
        adapter.messages.addAll(msgs);
        adapter.notifyDataSetChanged();
    }
}
