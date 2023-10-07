package com.ohare.client.event;

import com.ohare.client.Client;

/**
 * made by oHare for Client
 *
 * @since 5/25/2019
 **/
public class Event {

    public void dispatch() {
    	Client.INSTANCE.getBus().publish(this);
    }
}
